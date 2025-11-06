-- 1️⃣ Create your schema
CREATE SCHEMA job_management;

-- 2️⃣ Tell Postgres to use that schema for this session
SET search_path TO job_management;


-- Drop tables if they already exist (for re-running)
DROP TABLE IF EXISTS job_status_history;
DROP TABLE IF EXISTS job_executions;

-- 1. Create main table
CREATE TABLE job_executions (
    execution_id SERIAL PRIMARY KEY,
    job_name VARCHAR(100) NOT NULL
);

-- 2. Create status history table
CREATE TABLE job_status_history (
    id SERIAL PRIMARY KEY,
    execution_id INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_execution
        FOREIGN KEY (execution_id)
        REFERENCES job_executions (execution_id)
        ON DELETE CASCADE
);

-- 3. Insert data into job_executions
INSERT INTO job_executions (execution_id, job_name) VALUES
(1, 'PaymentProcessor'),
(2, 'BillingCalculator'),
(3, 'BillingCalculator');

-- 4. Insert data into job_status_history
INSERT INTO job_status_history (execution_id, timestamp, status) VALUES
(1, '2022-01-01 01:00:00', 'scheduled'),
(1, '2022-01-01 02:00:00', 'starting'),
(1, '2022-01-01 03:00:00', 'running'),
(2, '2022-01-01 04:00:00', 'starting'),
(3, '2022-01-01 05:00:00', 'starting'),
(3, '2022-01-01 06:00:00', 'running'),
(2, '2022-01-01 07:00:00', 'finished'),
(1, '2022-01-01 08:00:00', 'finished');

Verify the data
SELECT * FROM job_executions;
SELECT * FROM job_status_history;

Example: expected output query (latest execution per job)
SELECT
    je.job_name,
    MAX(je.execution_id) AS last_execution_id
FROM
    job_executions je
GROUP BY
    je.job_name
ORDER BY
    je.job_name;
