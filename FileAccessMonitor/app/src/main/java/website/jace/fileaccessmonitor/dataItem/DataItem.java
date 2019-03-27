package website.jace.fileaccessmonitor.dataItem;

import android.content.pm.PackageManager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public abstract class DataItem {

    class FileType {
        public static final String IMAGE = "Image";
        public static final String VIDEO = "Video";
        public static final String RESOURCE = "Resource";
        public static final String LIBRARY = "Library";
        public static final String TEXT = "Text";
        public static final String AUDIO = "Audio";
        public static final String DATA = "Data";
        public static final String UNKNOWN = "Unknown";
    }

    private static final String[] IMAGE_EXTENSIONS = {"png", "jpg", "jpeg", "tif", "png", "bmp", "svg", "ico"};
    private static final String[] VIDEO_EXTENSIONS = {"mp4", "mkv", "webm", "flv", "ogg", "mov", "wmv", "rm", "rmvb", "m4v", "mpg", "3gp"};
    private static final String[] RESOURCE_EXTENSIONS = {"xml", "apk"};
    private static final String[] LIBRARY_EXTENSIONS = {"so", "a"};
    private static final String[] TEXT_EXTENSIONS = {"txt", "log"};
    private static final String[] AUDIO_EXTENSIONS = {"aac", "ape", "au", "flac", "mp3", "ogg", "raw", "wav", "wma"};
    private static final String[] DATA_EXTENSIONS = {"data", "db", "database", "databases"};

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

    public String getFileType() {
        String[] folders = this.accessPath.split("\\/");
        if (folders.length <= 1) return FileType.UNKNOWN;
        String filename = folders[folders.length - 1];
        String[] parts = filename.split("\\.");
        if (parts.length <= 1) return FileType.UNKNOWN;
        String extension = parts[parts.length - 1].toLowerCase();

        for (String ext: IMAGE_EXTENSIONS) if (extension.equals(ext)) return FileType.IMAGE;
        for (String ext: VIDEO_EXTENSIONS) if (extension.equals(ext)) return FileType.VIDEO;
        for (String ext: AUDIO_EXTENSIONS) if (extension.equals(ext)) return FileType.AUDIO;
        for (String ext: RESOURCE_EXTENSIONS) if (extension.equals(ext)) return FileType.RESOURCE;
        for (String ext: LIBRARY_EXTENSIONS) if (extension.equals(ext)) return FileType.LIBRARY;
        for (String ext: TEXT_EXTENSIONS) if (extension.equals(ext)) return FileType.TEXT;
        for (String ext: DATA_EXTENSIONS) if (extension.equals(ext)) return FileType.DATA;

        return FileType.UNKNOWN;
    }
}
