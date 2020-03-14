package com.xently.ui.demo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.xently.xui.dialog.DialogFragment

class FullScreenDialog : DialogFragment() {
    override var launchMode: LaunchMode = LaunchMode.FULLSCREEN

    override var dialogOptionsMenuFromResource: Int? = R.menu.fullscreen

    override var dialogThemeFromResource: Int? = R.style.AppTheme_FullScreenDialog

    override var dialogAnimationFromResource: Int? = R.style.AppTheme_Slide

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fullscreen, container, false)
        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar_1)

        with((requireActivity() as AppCompatActivity)) {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_action_close)
                title = "Title"
            }
        }

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cancel -> {
                Log.i(MainActivity.TAG, "Clicked fullscreen: ${item.title}")
                onDialogNegativeButtonClick(this.requireDialog(), 1)
                true
            }
            R.id.okay -> {
                Log.i(MainActivity.TAG, "Clicked fullscreen: ${item.title}")
                onDialogPositiveButtonClick(this.requireDialog(), 1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}