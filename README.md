## Demo System Settings Screen

Camera Lift Trigger controller

## Validating
```
adb shell settings get --user current secure camera_lift_trigger_enabled
adb shell settings put --user current secure camera_lift_trigger_enabled 1
adb shell settings put --user current secure camera_lift_trigger_enabled 0
```
