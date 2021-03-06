package com.zaze.tribe.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.HashSet;
import java.util.Set;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-02-16 - 16:18
 */
public class PermissionUtil {

    public static boolean checkUserPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkAndRequestUserPermission(Activity activity, String permission, int requestCode) {
        boolean hasPermission = checkUserPermission(activity, permission);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
        return hasPermission;
    }

    public static boolean checkAndRequestUserPermission(Activity activity, String[] permissions, int requestCode) {
        Set<String> permissionSet = new HashSet<>();
        for (String permission : permissions) {
            if (!checkUserPermission(activity, permission)) {
                permissionSet.add(permission);
            }
        }
        if (!permissionSet.isEmpty()) {
            String[] reqPermissions = new String[permissionSet.size()];
            permissionSet.toArray(reqPermissions);
            ActivityCompat.requestPermissions(activity, reqPermissions, requestCode);
            return false;
        } else {
            return true;
        }
    }
}
