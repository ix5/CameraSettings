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

class SettingsSwitchPreferenceCompat(ctx: Context, attrs: AttributeSet) : SwitchPreferenceCompat(ctx, attrs) {
    private val userId = UserHandle.myUserId()

    // Settings key is mandatory, but nullable because it is evaluated when the class is unparceled without a constructor invocation
    private val settingsKey: String? = attrs.getAttributeValue("http://schemas.android.com/apk/res/org.ix5.camerasettings", "settings_key")
            ?: throw Exception("settings_key is null!")
    private val defaultValue: Int = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/org.ix5.camerasettings", "settings_default_value", 0)

    override fun isChecked(): Boolean {
        // WARNING: isChecked is called to create a parcel BEFORE the constructor is invoked
        if (settingsKey == null) {
            Log.e(TAG, "isChecked: settingsKey is unset")
            return false
        }
        Log.i(TAG, "isChecked for $settingsKey")
        val contentResolver: ContentResolver? = context.getContentResolver()
        return if (contentResolver != null) {
            (Settings.Secure.getIntForUser(
                    contentResolver,
                    settingsKey,
                    defaultValue,
                    userId
            ) == 1).also { Log.w(TAG, "isChecked: Value is $it") }
        } else {
            Log.w(TAG, "setChecked: Could not get contentResolver")
            false
        }
    }

    override fun setChecked(checked: Boolean) {
        if (settingsKey == null) {
            Log.e(TAG, "setChecked: settingsKey is unset")
            return
        }

        val storageVal = if (checked) 1 else 0
        //return Settings.Secure.putInt(mContext?.getContentResolver(), settings_key, storageVal)
        val contentResolver: ContentResolver? = context.getContentResolver()
        if (contentResolver != null) {
            Log.w(TAG, "setChecked: Putting $settingsKey to $storageVal")
            Settings.Secure.putIntForUser(
                    contentResolver,
                    settingsKey,
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