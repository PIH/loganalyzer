package org.pih.loganalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;

public class ActivityLogProcessor implements LogProcessor {

    public static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getBatchStatement() {
        return "insert into activity_log (" +
                "request_date, " +
                "session_id, " +
                "username, " +
                "load_time, " +
                "request_method, " +
                "request_path, " +
                "query_params" +
                ") values (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public void processLine(PreparedStatement ps, String line) throws Exception {
        ActivityLogLine logLine = mapper.readValue(line, ActivityLogLine.class);
        ps.setTimestamp(1, logLine.asTimestamp());
        ps.setString(2, logLine.sessionId);
        ps.setString(3, logLine.user);
        ps.setInt(4, logLine.loadTime);
        ps.setString(5, logLine.method);
        ps.setString(6, logLine.requestPath);
        ps.setString(7, logLine.queryParams);
    }
}
