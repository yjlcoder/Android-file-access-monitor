package website.jace.fileaccessmonitor.dataItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JNIDataItem extends DataItem {
    private final static String TIMEFORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    private final static String PATTERN_STR = "^[\\s\\S]+YANG:\\s\\[(\\d+\\-\\d+\\-\\d+ \\d+:\\d+:\\d+)]\\s<(\\d+)>\\sOpen file\\s(\\S+)$";
    private final static Pattern PATTERN = Pattern.compile(PATTERN_STR);
    private final static SimpleDateFormat TIMEFORMAT = new SimpleDateFormat(TIMEFORMAT_STRING);

    public JNIDataItem(String logItem, Map<Integer, String> packageList) {
        super(logItem, packageList);
        initialize(logItem, packageList);
    }

    @Override
    protected void initialize(String logItem, Map<Integer, String> packageList) {
        Matcher matcher = PATTERN.matcher(logItem);
        if (matcher.find()) {
            String datetimeString = matcher.group(1);
            int uid = Integer.valueOf(matcher.group(2));
            String accessPath = matcher.group(3);
            String pack = packageList.get(uid);

            try {
                Date date = TIMEFORMAT.parse(datetimeString);
                setDatetime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            setAccessPath(accessPath);
            setUid(uid);
            setPackageName(pack);
        }
    }
}
