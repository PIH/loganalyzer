drop table if exists activity_log;
create table activity_log
(
    request_date   timestamp,
    session_id     varchar(40),
    username       varchar(50),
    load_time      int,
    request_method varchar(10),
    request_path   varchar(1024),
    query_params   text
);