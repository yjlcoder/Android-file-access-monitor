package website.jace.fileaccessmonitor.services;

import java.io.DataOutputStream;
import java.io.IOException;

public class KernLogFetchingThread extends Thread{
    private Process p;
    @Override
    public void run() {
        try {
            this.p = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(p.getOutputStream());
            out.writeBytes("/system/bin/dmesg -w > /sdcard/kern.log\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        p.destroy();
        super.interrupt();
    }
}
