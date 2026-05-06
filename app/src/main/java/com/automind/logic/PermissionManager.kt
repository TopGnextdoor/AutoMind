package com.automind.logic

import android.app.AppOpsManager
import android.content.Context
import android.os.Process
import android.provider.Settings

class PermissionManager(private val context: Context) {
    
    fun hasUsageAccess(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun canDrawOverlays(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun isAllGranted(): Boolean {
        return hasUsageAccess() && canDrawOverlays()
    }
}
