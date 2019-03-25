package website.jace.fileaccessmonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KernDataItem extends DataItem {
    private final static String TIMEFORMAT_STRING = "[yyyy-MM-dd HH:mm:ss]";
    private final static String PATTERN_STR = "(\\[\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}])\\s(OPEN)\\s(\\d+),\\s(\\d+),\\s(\\d+),\\s(\\d+),\\s(\\S+)";
    private final static Pattern PATTERN = Pattern.compile(PATTERN_STR);
    private final static SimpleDateFormat TIMEFORMAT = new SimpleDateFormat(TIMEFORMAT_STRING);

    public KernDataItem(String logItem, Map<Integer, String> packageList) {
        super(logItem, packageList);
    }

    @Override
    protected void initialize(String logItem, Map<Integer, String> packageList) {
        String logData = logItem.split("YANG: ")[1];
        Matcher matcher = PATTERN.matcher(logData);
        if (matcher.find()) {
            String datetimeString = matcher.group(1);
            String uid = matcher.group(3);
            String accessPath = matcher.group(7);

            try {
                Date date = TIMEFORMAT.parse(datetimeString);
                System.out.println(date);
                setDatetime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            setUid(Integer.valueOf(uid));
            setAccessPath(accessPath);
        }
    }
}