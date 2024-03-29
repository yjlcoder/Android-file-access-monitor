package website.jace.fileaccessmonitor.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.DataOutputStream;
import java.io.IOException;

public class KernLogFetchingService extends Service {
    private final IBinder binder = new MyBinder();
    private Process p = null;

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
            DataOutputStream out = new DataOutputStream(p.getOutputStream());
            out.writeBytes("/system/bin/dmesg -w  | grep --line-buffered YANG >> /sdcard/log/kern.log\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public class MyBinder extends Binder {
        public KernLogFetchingService getService() {
            return KernLogFetchingService.this;
        }
    }

    @Override
    public void onDestroy() {
        if(p != null) p.destroy();
    }
}
