package website.jace.fileaccessmonitor;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import website.jace.fileaccessmonitor.services.JNILogFetchingService;
import website.jace.fileaccessmonitor.services.KernLogFetchingService;

public class SettingsActivity extends AppCompatActivity {
    private TextView kernServiceStatus = null;
    private Button kernServiceStartButton = null;
    private Button kernServiceStopButton = null;

    private TextView jniServiceStatus = null;
    private Button jniServiceStartButton = null;
    private Button jniServiceStopButton = null;

    private boolean kernServiceRunning = false;
    private boolean jniServiceRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.kernServiceStatus = findViewById(R.id.kern_service_status);
        this.kernServiceStartButton = findViewById(R.id.kern_service_start);
        this.kernServiceStopButton = findViewById(R.id.kern_service_stop);

        this.jniServiceStatus = findViewById(R.id.jni_service_status);
        this.jniServiceStartButton = findViewById(R.id.jni_service_start);
        this.jniServiceStopButton = findViewById(R.id.jni_service_stop);

        refreshStatus();

        this.kernServiceStartButton.setOnClickListener(v -> {
            if (!kernServiceRunning) {
                Intent serviceIntent = new Intent(SettingsActivity.this, KernLogFetchingService.class);
                startService(serviceIntent);
                refreshStatus();
            }
        });

        this.kernServiceStopButton.setOnClickListener(v -> {
            if (kernServiceRunning) {
                Intent serviceIntent = new Intent(SettingsActivity.this, KernLogFetchingService.class);
                stopService(serviceIntent);
                refreshStatus();
            }
        });

        this.jniServiceStartButton.setOnClickListener(v -> {
            if (!jniServiceRunning) {
                Intent serviceIntent = new Intent(SettingsActivity.this, JNILogFetchingService.class);
                startService(serviceIntent);
                refreshStatus();
            }
        });

        this.jniServiceStopButton.setOnClickListener(v -> {
            if (jniServiceRunning) {
                Intent serviceIntent = new Intent(SettingsActivity.this, JNILogFetchingService.class);
                stopService(serviceIntent);
                refreshStatus();
            }
        });
    }

    private void setServiceStatus(Boolean started, String service) {
        TextView serviceStatus;
        if (service.startsWith("kern")){
            serviceStatus = this.kernServiceStatus;
            kernServiceRunning = started;
        }
        else{
            serviceStatus = this.jniServiceStatus;
            jniServiceRunning = started;
        }
        if (started) {
            serviceStatus.setText(getResources().getText(R.string.service_running));
            serviceStatus.setTextColor(getResources().getColor(R.color.black));
        } else {
            serviceStatus.setText(getResources().getText(R.string.service_not_running));
            serviceStatus.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void refreshStatus() {
        if (isMyServiceRunning(KernLogFetchingService.class)) setServiceStatus(true, "kern");
        else setServiceStatus(false, "kern");

        if (isMyServiceRunning(JNILogFetchingService.class)) setServiceStatus(true, "jni");
        else setServiceStatus(false, "jni");
    }
}
