package website.jace.fileaccessmonitor;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.serviceStatus = findViewById(R.id.service_status);
        this.serviceStartButton = findViewById(R.id.service_start);
        this.serviceStopButton = findViewById(R.id.service_stop);

        this.serviceStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(SettingsActivity.this, KernLogFetchingService.class);
                startService(serviceIntent);
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
    }
}
