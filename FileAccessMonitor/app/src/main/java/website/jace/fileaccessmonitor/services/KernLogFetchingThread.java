package website.jace.fileaccessmonitor.services;

import java.io.DataOutputStream;
import java.io.IOException;

public class KernLogFetchingThread extends Thread{
    @Override
    public void run() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(p.getOutputStream());
            out.writeBytes("/system/bin/dmesg -w > /sdcard/kern.log\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
