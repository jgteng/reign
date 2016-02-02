CREATE TABLE TASK_RUN_STATUS(
   id INTEGER PRIMARY KEY autoincrement,
   log_id           CHAR(64) NOT NULL,
   result         CHAR(2)      NOT NULL,
   process_id     char(20)
);