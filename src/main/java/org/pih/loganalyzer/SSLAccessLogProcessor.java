package org.pih.loganalyzer;

import java.sql.PreparedStatement;

public class SSLAccessLogProcessor implements LogProcessor {

    @Override
    public String getBatchStatement() {
        return "insert into ssl_access_log (" +
                "remote_host, " +
                "user_identity, " +
                "user_name, " +
                "request_date, " +
                "request, " +
                "status_code, " +
                "size_bytes, " +
                "referrer, " +
                "user_agent" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public void processLine(PreparedStatement ps, String line) throws Exception {
        SSLAccessLogLine logLine = new SSLAccessLogLine(line);
        ps.setString(1, logLine.remoteHost);
        ps.setString(2, logLine.userIdentity);
        ps.setString(3, logLine.username);
        ps.setTimestamp(4, logLine.timestamp);
        ps.setString(5, logLine.request);
        ps.setInt(6, logLine.statusCode);
        ps.setInt(7, logLine.sizeBytes);
        ps.setString(8, logLine.referrer);
        ps.setString(9, logLine.userAgent);
    }
}
