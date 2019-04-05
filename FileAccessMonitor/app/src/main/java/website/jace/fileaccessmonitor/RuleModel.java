package website.jace.fileaccessmonitor;

public class RuleModel {

    private String path;
    private String realPath;
    private AppIdModel appId;

    public RuleModel(String appName, String packageName, int uid, String path, String realPath) {
        this.appId = new AppIdModel(uid, packageName, appName);
        this.path = path;
        this.realPath = realPath;
    }

    public String getAppName() {
        return appId.appName;
    }

    public String getPackageName() {
        return appId.packageName;
    }

    public String getPath() {
        return path;
    }

    public String getRealPath() {
        return realPath;
    }

    public int getUid() {
        return appId.uid;
    }

    public void setAppName(String appName) {
        this.appId.appName = appName;
    }

    public void setPackageName(String packageName) {
        this.appId.packageName = packageName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public void setUid(int uid) {
        this.appId.uid = uid;
    }

    public AppIdModel getAppId() {
        return appId;
    }

    public void setAppId(AppIdModel appId) {
        this.appId = appId;
    }
}
