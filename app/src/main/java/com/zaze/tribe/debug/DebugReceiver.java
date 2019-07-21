package com.zaze.tribe.debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-27 - 23:38
 */
public class DebugReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Debug", "DebugReceiver 111");
        SystemClock.sleep(3000L);
        Log.i("Debug", "DebugReceiver 2222");
    }
}
