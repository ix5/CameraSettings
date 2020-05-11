package org.ix5.camerasettings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import org.ix5.camerasettings.R


class CameraSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, CameraSettingsFragment())
                .commit()
    }

}
