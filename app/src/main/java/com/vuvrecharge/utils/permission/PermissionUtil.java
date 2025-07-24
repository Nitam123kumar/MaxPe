package com.vuvrecharge.utils.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class PermissionUtil {
    private final String[] galleryPermissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.READ_MEDIA_IMAGES"
    };

    private final String[] cameraPermissions = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.READ_MEDIA_IMAGES"
    };

    private final String[] contactPermissions = {
            "android.permission.READ_CONTACTS"
    };
    private final String[] callPermissions = {
            "android.permission.CALL_PHONE"
    };
    private final String[] emailPermissions = {
            "android.permission.GET_ACCOUNTS"
    };

    private final String[] locationPermissions = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.BODY_SENSORS"
    };

    private final String[] allPermissions = {
            "android.permission.POST_NOTIFICATIONS"
    };

    public String[] getGalleryPermissions() {
        return galleryPermissions;
    }

    public String[] getContactPermissions() {
        return contactPermissions;
    }

    public String[] getCallPermissions() {
        return callPermissions;
    }

    public String[] getEmailPermissions() {
        return emailPermissions;
    }

    public String[] getCameraPermissions() {
        return cameraPermissions;
    }

    public String[] getLocationPermissions() {
        return locationPermissions;
    }

    public String[] getAllPermissions() {
        return allPermissions;
    }

    public boolean verifyPermissions(@NonNull int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean verifyPermissions(Context context, @NonNull String[] grantResults) {
        for (String result : grantResults) {
            if (ActivityCompat.checkSelfPermission(context, result) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean checkMarshMellowPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


}
