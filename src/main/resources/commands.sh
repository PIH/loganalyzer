#!/bin/bash

LOG_DIR="/tmp/loganalysis"

java -jar target/loganalyzer-1.0.0-SNAPSHOT-jar-with-dependencies.jar \
  --type=sslAccess \
  --directory=$LOG_DIR/ssl_access_logs \
  --connectionProperties=$LOG_DIR/mysql.properties

java -jar target/loganalyzer-1.0.0-SNAPSHOT-jar-with-dependencies.jar \
  --type=activityLog \
  --directory=$LOG_DIR/activity_logs \
  --connectionProperties=$LOG_DIR/mysql.properties