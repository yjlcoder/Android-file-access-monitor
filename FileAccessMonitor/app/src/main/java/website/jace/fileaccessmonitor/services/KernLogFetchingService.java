package website.jace.fileaccessmonitor.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class KernLogFetchingService extends Service {
    private final IBinder binder = new MyBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);
        new KernLogFetchingThread().start();
        return ret;
    }

    public class MyBinder extends Binder {
        public KernLogFetchingService getService() {
            return KernLogFetchingService.this;
        }
    }
}
