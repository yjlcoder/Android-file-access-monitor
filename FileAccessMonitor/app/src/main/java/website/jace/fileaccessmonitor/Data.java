package website.jace.fileaccessmonitor;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.*;
import java.util.HashMap;
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
    }

    private final static String kernelLogPath = "/sdcard/kern.log";
    private final static String jniLogPath = "/sdcard/log";
    private final static String UIDListPath = "/data/system/packages.list";

    private Map<Integer, String> uidPackagesMap;
    private Map<Integer, String> uidOwnedFolderMap;

    public void startBuild() {
        readUIDList();
        buildKernelLog();
        buildJNILog();
    }

    private void readUIDList() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            System.out.println("!!!");
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

    }

    private void buildJNILog() {

    }
}