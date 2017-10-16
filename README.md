To run the program:
java -cp "parser.jar" com.ef.Parser --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
The tool will find any IPs that made more than 100 requests starting from 2017-01-01.13:00:00 to 2017-01-01.14:00:00 (one hour) and print them to console AND also load them to another MySQL table with comments on why it's blocked.

In order to run file first you need to create parser.jar.
	
Configuration for the program can be found in com.ef.Constants class like db user and password and log file path.
Log file access.log is bundled in jar if you want to change it just change the file in src/resources/com/ef/parser/access.log 
and after that build the jar.

Database DDL
------------
```sql
create database LogStash;
    
create table request_log (
  id BIGINT(11) NOT NULL AUTO_INCREMENT,
  ip VARCHAR(15) NOT NULL,
  no_of_requests INT,
  request_start_date TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  request_end_date TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  ban_comment VARCHAR(255) NOT NULL,
  created_timestamp TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY(id),
  INDEX RL_IP_INDEX (ip),
  INDEX RL_REQUEST_NO_INDEX (no_of_requests),
  INDEX RL_START_DT_INDEX (request_start_date),
  INDEX RL_END_DT_INDEX (request_end_date)
);


create table request (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    request_log_id BIGINT(11) NOT NULL,
    request VARCHAR(255) NOT NULL,
    status INT NOT NULL,
    user_agent VARCHAR(255),
    request_date TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    created_timestamp TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY(id),
    FOREIGN KEY (request_log_id) REFERENCES request_log(id)
);
```
#Write MySQL query to find IPs that mode more than a certain number of requests for a given time period.
```sql
SET @number_of_request = 200; #variable defining number of request occurrence
SET @start_date_time = '2017-01-01 13:00:00'; #start time
SET @end_date_time = '2017-01-02 13:00:00'; # end time
SELECT ip,sum(request_log.no_of_requests) as sumOfRequest  from request_log WHERE request_start_date >= @start_date_time AND request_end_date < @end_date_time
group by request_log.ip having sumOfRequest>=@number_of_request;


#Write MySQL query to find requests made by a given IP.
SET @ip = '192.168.228.188'; #variable defining search ip
SELECT * FROM request_log INNER JOIN request ON request_log.id = request.request_log_id WHERE request_log.ip = @ip;
```