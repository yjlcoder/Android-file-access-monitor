package website.jace.fileaccessmonitor.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JNILogFetchingService extends Service {
    private final IBinder binder = new MyBinder();
    private Process p;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);

        try {
            this.p = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(this.p.getOutputStream());
            out.writeBytes("/system/bin/logcat | grep --line-buffered YANG >> /sdcard/log/jni.log\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public class MyBinder extends Binder {
        public JNILogFetchingService getService() {
            return JNILogFetchingService.this;
        }
    }

}
