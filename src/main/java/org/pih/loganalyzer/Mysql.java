package org.pih.loganalyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Mysql {

    public static Connection createConnection(String host, String port, String user, String password, String database) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String args = "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + args, user, password);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to connect to MySQL", e);
        }
    }

    public static Connection createConnection(Properties p) {
        String host = p.getProperty("database.hostname");
        String port = p.getProperty("database.port");
        String user = p.getProperty("database.user");
        String password = p.getProperty("database.password");
        String database = p.getProperty("database.name");
        return createConnection(host, port, user, password, database);
    }
}
