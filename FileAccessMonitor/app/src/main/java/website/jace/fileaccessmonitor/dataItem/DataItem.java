package website.jace.fileaccessmonitor.dataItem;

import android.content.pm.PackageManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public abstract class DataItem {
    private int uid;
    private String packageName;
    private Date datetime;
    private String accessPath;

    private String applicationName;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public DataItem(String logItem, Map<Integer, String> packageList) {
        initialize(logItem, packageList);
    }

    protected abstract void initialize(String logItem, Map<Integer, String> packageList);

    public int getUid() {
        return uid;
    }

    public Date getDatetime() {
        return datetime;
    }

    public String getAccessPath() {
        return accessPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setAccessPath(String accessPath) {
        this.accessPath = accessPath;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getTimeString() {
        return dateFormat.format(datetime);
    }
}
