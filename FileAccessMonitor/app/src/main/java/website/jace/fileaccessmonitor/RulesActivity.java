package website.jace.fileaccessmonitor;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;

public class RulesActivity extends AppCompatActivity {
    private PackageManager pm;
    private FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        Data.getInstance().startReadUIDList();
        pm = getApplicationContext().getPackageManager();
        fab = findViewById(R.id.rules_save_button);

        for (String packageName: Data.getInstance().uidPackagesMap.values()) {
            if (!Data.getInstance().packageApplicationNameMap.keySet().contains(packageName)) {
                ApplicationInfo ai;
                try {
                    ai = pm.getApplicationInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    continue;
                }
                String applicationName = (String) pm.getApplicationLabel(ai);
                Data.getInstance().packageApplicationNameMap.put(packageName, applicationName);
            }
        }

        for (int uid: Data.getInstance().uidPackagesMap.keySet()) {
            String packageName = Data.getInstance().uidPackagesMap.get(uid);
            String applicationName = Data.getInstance().packageApplicationNameMap.get(packageName);
            Data.getInstance().appIdModelList.add(new AppIdModel(uid, packageName, applicationName));
            sort(Data.getInstance().appIdModelList);
        }

        ArrayList<RuleModel> rules = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(Environment.getExternalStorageDirectory().getPath() + "/log/rules.conf")));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] segments = line.split(",");
                int uid = Integer.valueOf(segments[0]);
                String path = segments[1];
                String real_path;
                if (segments.length <= 2) real_path = "";
                else real_path = segments[2];
                String packageName = Data.getInstance().uidPackagesMap.get(uid);

                String applicationName = Data.getInstance().packageApplicationNameMap.get(packageName);
                rules.add(new RuleModel(applicationName, packageName, uid, path, real_path));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RulesAdapter adapter = new RulesAdapter(RulesActivity.this, rules);
        ListView rulesListView = findViewById(R.id.rules_listview);
        rulesListView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            List<RuleModel> ruleList = adapter.getDataSet();

            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/log/rules.conf"));
                for (RuleModel rule: ruleList) {
                    String toWrite = String.valueOf(rule.getUid()) + "," + rule.getPath() + "," + rule.getRealPath() + "\n";
                    writer.write(toWrite);
                }
                writer.close();
                Toast.makeText(getApplicationContext(), "Save successfully", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to save: IOException", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
