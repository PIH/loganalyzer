package org.pih.loganalyzer;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityLogLine {

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");

    public String timestamp;
    public String sessionId;
    public String user;
    public Integer loadTime;
    public String method;
    public String requestPath;
    public String queryParams;

    public Timestamp asTimestamp() {
        try {
            Date date = dateFormat.parse(timestamp);
            return new Timestamp(date.getTime());
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to parse " + timestamp + " into date");
        }
    }
}
