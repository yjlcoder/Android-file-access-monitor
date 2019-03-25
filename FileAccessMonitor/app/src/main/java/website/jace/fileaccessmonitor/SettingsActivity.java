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
import website.jace.fileaccessmonitor.services.KernLogFetchingService;

public class SettingsActivity extends AppCompatActivity {
    private TextView serviceStatus = null;
    private Button serviceStartButton = null;
    private Button serviceStopButton = null;

    private boolean serviceRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.serviceStatus = findViewById(R.id.service_status);
        this.serviceStartButton = findViewById(R.id.service_start);
        this.serviceStopButton = findViewById(R.id.service_stop);

        refreshStatus();

        this.serviceStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!serviceRunning) {
                    Intent serviceIntent = new Intent(SettingsActivity.this, KernLogFetchingService.class);
                    startService(serviceIntent);
                    refreshStatus();
                }
            }
        });

        this.serviceStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceRunning) {
                    Intent serviceIntent = new Intent(SettingsActivity.this, KernLogFetchingService.class);
                    stopService(serviceIntent);
                    refreshStatus();
                }
            }
        });

    }

    private void setServiceStatus(Boolean started) {
        if (started) {
            serviceStatus.setText(getResources().getText(R.string.service_running));
            serviceStatus.setTextColor(getResources().getColor(R.color.black));
        } else {
            serviceStatus.setText(getResources().getText(R.string.service_not_running));
            serviceStatus.setTextColor(getResources().getColor(R.color.red));
        }
        serviceRunning = started;
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
        if (isMyServiceRunning(KernLogFetchingService.class)) setServiceStatus(true);
        else setServiceStatus(false);
    }
}
