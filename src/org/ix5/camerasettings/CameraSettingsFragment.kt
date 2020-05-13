package org.ix5.camerasettings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

private const val TAG = "CameraSettingsFragment"

class CameraSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_pref, rootKey)

        val liftPreference: SettingsSwitchPreferenceCompat? = findPreference("pref_lift_trigger")
        liftPreference!!.setEnabled(isGestureAvailable())
        // TODO: Update summary
    }

    private fun isGestureAvailable() = context?.resources?.getInteger(com.android.internal.R.integer.config_cameraLiftTriggerSensorType) != -1
}
