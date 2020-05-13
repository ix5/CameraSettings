/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.ix5.camerasettings

import android.content.ContentResolver
import android.content.Context
import android.os.UserHandle
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import androidx.preference.SwitchPreferenceCompat

private const val TAG = "CameraSettings"

private const val CAMERA_LIFT_TRIGGER_ENABLED: String = "camera_lift_trigger_enabled"
private const val CAMERA_LIFT_TRIGGER_ENABLED_DEFAULT: Int = 1

class SettingsSwitchPreferenceCompat(ctx: Context, attrs: AttributeSet) : SwitchPreferenceCompat(ctx, attrs) {
    private val userId = UserHandle.myUserId()

    override fun isChecked(): Boolean {
        val contentResolver: ContentResolver? = context.getContentResolver()
        return if (contentResolver != null) {
            (Settings.Secure.getIntForUser(
                    contentResolver,
                    CAMERA_LIFT_TRIGGER_ENABLED,
                    CAMERA_LIFT_TRIGGER_ENABLED_DEFAULT,
                    userId
            ) == 1).also { Log.w(TAG, "isChecked: Value is $it") }
        } else {
            Log.w(TAG, "setChecked: Could not get contentResolver")
            false
        }
    }

    override fun setChecked(checked: Boolean) {
        val storageVal = if (checked) 1 else 0
        //return Settings.Secure.putInt(mContext?.getContentResolver(), CAMERA_LIFT_TRIGGER_ENABLED, storageVal)
        val contentResolver: ContentResolver? = context.getContentResolver()
        if (contentResolver != null) {
            Log.w(TAG, "setChecked: Putting $CAMERA_LIFT_TRIGGER_ENABLED to $storageVal")
            Settings.Secure.putIntForUser(
                    contentResolver,
                    CAMERA_LIFT_TRIGGER_ENABLED,
                    storageVal,
                    userId
            ).also {
                // Update switch state only if successful:
                if (it)
                    super.setChecked(checked)
            }
        } else {
            Log.w(TAG, "setChecked: Could not get contentResolver")
        }
    }
}