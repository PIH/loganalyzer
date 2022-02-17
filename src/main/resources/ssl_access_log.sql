drop table if exists ssl_access_log;
create table ssl_access_log
(
    remote_host    varchar(50),
    user_identity varchar(255),
    user_name      varchar(255),
    request_date   timestamp,
    request        varchar(1024),
    status_code    int,
    size_bytes     int,
    referrer       varchar(1024),
    user_agent     varchar(1024)
);