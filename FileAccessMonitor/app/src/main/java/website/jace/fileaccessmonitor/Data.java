package website.jace.fileaccessmonitor;

import android.os.Environment;
import android.util.Log;
import website.jace.fileaccessmonitor.dataItem.DataItem;
import website.jace.fileaccessmonitor.dataItem.JNIDataItem;
import website.jace.fileaccessmonitor.dataItem.KernDataItem;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    private static Data single_instance = null;

    public static Data getInstance() {
        if (single_instance == null)
            single_instance = new Data();

        return single_instance;
    }

    private void init() {
        this.uidPackagesMap = new HashMap<>();
        this.uidOwnedFolderMap = new HashMap<>();
        this.kernDataItems = new ArrayList<>();
        this.jniDataItems = new ArrayList<>();
        packageApplicationNameMap = new HashMap<>();
    }

    private Data() { }

    private final static String kernelLogPath = Environment.getExternalStorageDirectory().getPath() + "/log/kern.log";
    private final static String jniLogPath = Environment.getExternalStorageDirectory().getPath() + "/log/jni.log";
    private final static String UIDListPath = "/data/system/packages.list";

    // uid list
    public Map<Integer, String> uidPackagesMap;
    public Map<Integer, String> uidOwnedFolderMap;

    // kernel log
    public List<DataItem> kernDataItems;

    // jni log
    public List<DataItem> jniDataItems;

    // packageName to ApplicationName
    public Map<String, String> packageApplicationNameMap;

    public void startBuild() {
        init();
        readUIDList();
        buildKernelLog();
        buildJNILog();
    }

    private void readUIDList() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(p.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            out.writeBytes("cat " + UIDListPath + "\n");
            out.flush();
            out.writeBytes("exit\n");
            out.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] segments = line.split(" ");
                String pack = segments[0];
                int uid = Integer.valueOf(segments[1]);
                String folder = segments[3];

                this.uidPackagesMap.put(uid, pack);
                this.uidOwnedFolderMap.put(uid, folder);
            }

            Log.d("File Access Monitor", "Read local user ids successfully, number of users: " + this.uidOwnedFolderMap.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildKernelLog() {
        File file = new File(kernelLogPath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(reader.ready()) {
                String line = reader.readLine();
                if (line.contains("YANG: ") && line.contains("OPEN")) {
                    KernDataItem kernDataItem = new KernDataItem(line, uidPackagesMap);
                    if (kernDataItem.getPackageName() != null) {
                        kernDataItems.add(kernDataItem);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildJNILog() {
        File file = new File(jniLogPath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.contains("YANG") && line.contains("Open"))
                    jniDataItems.add(new JNIDataItem(line, uidPackagesMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}