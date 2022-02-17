# loganalyzer

Project which supports loading log files into a database for easier analysis

This currently just loads data into a MySQL database to allow a familiar platform and tool for more detailed analysis.

Usage:

This tool is capable of importing two types of logs:

1. activity logs produced by the pihemr
2. ssl access logs from apache

Steps:

1. Create a MySQL database to import into:

```create database loganalyzer default charset utf8;```

2. Create a mysql.properties file that has connection information for this repository:

```properties
database.hostname=localhost
database.port=3308
database.user=root
database.password=root
database.name=loganalyzer
```

3. Create a directory to contain logs of each particular type, and add all of the logs into this directory (unzipped)

4. Execute the import with commands as follows:

```shell
java -jar target/loganalyzer-1.0.0-SNAPSHOT-jar-with-dependencies.jar \
--type=sslAccess \
--directory=$LOG_DIR/ssl_access_logs \
--connectionProperties=$LOG_DIR/mysql.properties
```

```shell
java -jar target/loganalyzer-1.0.0-SNAPSHOT-jar-with-dependencies.jar \
--type=activityLog \
--directory=$LOG_DIR/activity_logs \
--connectionProperties=$LOG_DIR/mysql.properties
```