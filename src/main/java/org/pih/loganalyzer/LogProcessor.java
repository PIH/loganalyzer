package org.pih.loganalyzer;

import java.sql.PreparedStatement;

public interface LogProcessor {
    String getBatchStatement();
    void processLine(PreparedStatement ps, String line) throws Exception;
}
