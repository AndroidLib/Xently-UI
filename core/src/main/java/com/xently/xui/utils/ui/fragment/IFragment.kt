package com.xently.xui.utils.ui.fragment

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.xently.dialog.ButtonText
import com.xently.dialog.DialogParams
import com.xently.xui.R
import com.xently.xui.dialog.DialogFragment
import com.xently.xui.dialog.MessageDialog
import com.xently.xui.utils.showSnackBar
import com.xently.xui.utils.ui.view.IView

interface IFragment : IView {

    fun Fragment.hideKeyboard() = hideKeyboard(view)

    fun Fragment.onCreateDeletionDialog(message: String): MessageDialog {
        return MessageDialog.getInstance(
            DialogParams(
                getString(R.string.xui_delete_dialog_title),
                message, ButtonText(
                    getString(R.string.xui_delete_dialog_pos_btn),
                    getString(R.string.xui_delete_dialog_neg_btn)
                )
            ),
            icon = R.drawable.ic_action_warning
        ).apply {
            buttonClickListener = object : DialogFragment.ButtonClickListener {
                override fun onPositiveButtonClick(
                    dialog: DialogInterface,
                    index: Int,
                    tag: String?
                ) {
                    onDeletionDialogDeleteClick()
                    dialog.dismiss()
                }
            }
        }
    }

    fun Fragment.onCreateDeletionDialog(
        @StringRes message: Int,
        vararg format: Any
    ): MessageDialog = onCreateDeletionDialog(getString(message, *format))

    fun Fragment.showSnackBar(
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT, actionButtonText: String? = null,
        actionButtonClick: ((snackBar: Snackbar) -> Unit)? = null
    ): Snackbar =
        showSnackBar(requireView(), message, duration, actionButtonText, actionButtonClick)

    fun Fragment.showSnackBar(
        @StringRes message: Int,
        duration: Int = Snackbar.LENGTH_SHORT, actionButtonText: String? = null,
        actionButtonClick: ((snackBar: Snackbar) -> Unit)? = null
    ): Snackbar =
        showSnackBar(requireView(), message, duration, actionButtonText, actionButtonClick)

    fun onDeletionDialogDeleteClick() = Unit

    /**
     * @param permission permission requested for. Input should be from Manifest permission constants
     * **`Manifest.permission.CAMERA`** or **`Manifest.permission_group.STORAGE`**
     * @param requestCode it helps in properly responding to specific permissions as dictated by
     * [Fragment.onRequestPermissionsResult]
     * @param onPermissionGranted (Execute on Permission [permission] Granted/Available) - what to
     * do when the [permission] is granted by the user
     * @param onRationaleNeeded what to do when the [permission] request justification is needed for
     * the user to understand reason for permission request. Default behaviour is to show an alert
     * dialog explaining why the permission is required
     */
    fun <T> Fragment.requestFeaturePermission(
        permission: String,
        requestCode: Int,
        onPermissionGranted: (() -> T)? = null,
        onRationaleNeeded: (() -> Unit)? = null
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        // Check if Camera permission is already granted or available
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Camera permission is already available. Show the preview
            onPermissionGranted?.invoke()
        } else {
            // Permission not available
            // Provide additional rationale to the user if the permission was not granted and the
            // user will benefit from the additional activity of the use of the permission

            if (shouldShowRequestPermissionRationale(permission)) {
                // Inform user of why you need the permission
                onRationaleNeeded?.invoke()
                // Proceed to request permission again
            }

            // Request permission
            requestPermissions(arrayOf(permission), requestCode)
        }
    }
}