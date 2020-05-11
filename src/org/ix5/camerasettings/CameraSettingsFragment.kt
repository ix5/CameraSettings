package org.ix5.camerasettings

import android.content.ContentResolver
import android.content.Context
import android.provider.Settings
import android.os.Bundle
import android.os.UserHandle
import android.util.Log

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

import org.ix5.camerasettings.R


/* ========= */
/* Constants */
/* ========= */

private const val TAG = "CameraSettingsFragment"

// frameworks/base/core/java/android/provider/Settings.java:
//   public static final String CAMERA_LIFT_TRIGGER_ENABLED = "camera_lift_trigger_enabled";
//   public static final int CAMERA_LIFT_TRIGGER_ENABLED_DEFAULT = 1;
val CAMERA_LIFT_TRIGGER_ENABLED: String = "camera_lift_trigger_enabled"
val CAMERA_LIFT_TRIGGER_ENABLED_DEFAULT: Int = 1

// packages/apps/Settings/src/com/android/settings/core/BasePreferenceController.java
val AVAILABLE = 0
val UNSUPPORTED_ON_DEVICE = 3


/* ========= */
/* Main */
/* ========= */


class CameraSettingsFragment : PreferenceFragmentCompat() {

    // Is it common to use m-prefixed class member names in kotlin?
    private var mContext: Context? = getContext()

    //@UserIdInt
    private var mUserId: Int = 0

    private val ON = 1
    private val OFF = 0

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.main_pref, rootKey)

        mContext = getContext()

        mUserId = UserHandle.myUserId()

        var liftPreference: SwitchPreferenceCompat? = findPreference("pref_lift_trigger")
        // Careful with java and kotlin types! Object vs object
        if (getAvailabilityStatus() != AVAILABLE) {
            liftPreference?.setVisible(false)
            Log.w(TAG, "liftPreference not available")
            return
        }
        liftPreference?.isChecked = isChecked()
        liftPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
                _, newValue ->
                    // handle newValue...
                    if (newValue == true) setChecked(true) else setChecked(false)
                    true
        }
    }

    fun isChecked(): Boolean {
        Log.w(TAG, "isChecked: Running getContentResolver")
        val contentResolver: ContentResolver? = mContext?.getContentResolver()
        var cameraEnabled: Int = OFF
        if (contentResolver != null) {
            Log.w(TAG, "isChecked: Getting $CAMERA_LIFT_TRIGGER_ENABLED")
            cameraEnabled = Settings.Secure.getIntForUser(
                contentResolver,
                CAMERA_LIFT_TRIGGER_ENABLED,
                CAMERA_LIFT_TRIGGER_ENABLED_DEFAULT,
                mUserId
            )
        } else {
            Log.w(TAG, "isChecked: Could not get contentResolver")
        }
        return cameraEnabled == ON
    }

    fun setChecked(checked: Boolean): Boolean {
        var storageVal: Int
        if (checked) storageVal = ON else storageVal = OFF
        //return Settings.Secure.putInt(mContext?.getContentResolver(), CAMERA_LIFT_TRIGGER_ENABLED, storageVal)
        val contentResolver: ContentResolver? = mContext?.getContentResolver()
        if (contentResolver != null) {
            Log.w(TAG, "isChecked: Putting $CAMERA_LIFT_TRIGGER_ENABLED to $storageVal")
            return Settings.Secure.putIntForUser(
                contentResolver,
                CAMERA_LIFT_TRIGGER_ENABLED,
                storageVal,
                mUserId
            )
        } else {
            return false
            Log.w(TAG, "setChecked: Could not get contentResolver")
        }
    }

    fun isGestureAvailable(context: Context?): Boolean {
        return context?.getResources()?.getInteger(com.android.internal.R.integer.config_cameraLiftTriggerSensorType) != -1
    }

    fun getAvailabilityStatus(): Int {
        if (isGestureAvailable(mContext)) return AVAILABLE else return UNSUPPORTED_ON_DEVICE
    }

}
