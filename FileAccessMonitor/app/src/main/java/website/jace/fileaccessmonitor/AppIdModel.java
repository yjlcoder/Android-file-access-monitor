package website.jace.fileaccessmonitor;

import android.support.annotation.NonNull;

public class AppIdModel implements Comparable {
    String appName;
    String packageName;
    int uid;

    public AppIdModel(int uid, String packageName, String appName) {
        this.uid = uid;
        this.appName = appName;
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return appName;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o.getClass() != AppIdModel.class) return -1;
        else {
            AppIdModel other = (AppIdModel) o;
            return appName.compareTo(other.appName);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != AppIdModel.class) {
            return false;
        }
        AppIdModel other = (AppIdModel) obj;
        return other.uid == this.uid;
    }
}


