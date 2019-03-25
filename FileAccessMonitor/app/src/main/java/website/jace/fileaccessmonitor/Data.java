package website.jace.fileaccessmonitor;

import android.os.Environment;
import android.util.Log;

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

    private Data() {
        this.uidPackagesMap = new HashMap<>();
        this.uidOwnedFolderMap = new HashMap<>();
        this.kernDataItems = new ArrayList<>();
        this.jniDataItems = new ArrayList<>();
    }

    private final static String kernelLogPath = Environment.getExternalStorageDirectory().getPath() + "/log/kern.log";
    private final static String jniLogPath = Environment.getExternalStorageDirectory().getPath() + "/log/jni.log";
    private final static String UIDListPath = "/data/system/packages.list";

    // uid list
    private Map<Integer, String> uidPackagesMap;
    private Map<Integer, String> uidOwnedFolderMap;

    // kernel log
    private List<DataItem> kernDataItems;

    // jni log
    private List<DataItem> jniDataItems;

    public void startBuild() {
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

            Log.d("YANG", "Read local user ids successfully, number of users: " + this.uidOwnedFolderMap.size());
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
                    kernDataItems.add(new KernDataItem(line, uidPackagesMap));
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
                jniDataItems.add(new JNIDataItem(line, uidPackagesMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}