package org.ix5.camerasettings

import android.os.Bundle
import androidx.fragment.app.FragmentActivity


class CameraSettingsActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, CameraSettingsFragment())
                .commit()
    }
}
