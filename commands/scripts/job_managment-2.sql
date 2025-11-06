-- 2️⃣ Tell Postgres to use that schema for this session
SET search_path TO job_management;

--SELECT * FROM job_executions;
--SELECT * FROM job_status_history;

-- SELECT job_name, MAX(execution_id) AS last_execution_i FROM job_executions GROUP BY job_name
-- SELECT DISTINCT ON (jsh.execution_id)
--         jsh.execution_id,
--         jsh.status
--     FROM job_status_history jsh
--     ORDER BY jsh.execution_id, jsh.timestamp DESC

-- SELECT DISTINCT ON (jsh.execution_id)
--         jsh.execution_id,
--         jsh.status,
-- 		jsh.timestamp
--     FROM job_status_history jsh
--     ORDER BY jsh.execution_id, jsh.timestamp DESC

-- 3 runing, 2, finished ,1 finished


-- WITH last_execution AS (
--     -- Get the highest execution_id per job
--     SELECT 
--         job_name,
--         MAX(execution_id) AS last_execution_id
--     FROM job_executions
--     GROUP BY job_name
-- ),
-- last_status AS (
--     -- Get the latest status per execution
--     SELECT DISTINCT ON (jsh.execution_id)
--         jsh.execution_id,
--         jsh.status
--     FROM job_status_history jsh
--     ORDER BY jsh.execution_id, jsh.timestamp DESC
-- )
-- SELECT 
--     le.job_name,
--     le.last_execution_id,
--     ls.status AS last_status
-- FROM last_execution le
-- JOIN last_status ls
--     ON le.last_execution_id = ls.execution_id
-- ORDER BY le.job_name;


-- WITH last_execution AS (
--     -- Get the highest execution_id per job
--     SELECT 
--         job_name,
--         MAX(execution_id) AS last_execution_id
--     FROM job_executions
--     GROUP BY job_name
-- )
-- select l.job_name,
-- 	l.last_execution_id,
-- 	h.status
-- from last_execution l
-- join job_status_history h
-- on l.last_execution_id = h.execution_id
-- where h.timestamp = (select MAX(timestamp) from job_status_history where execution_id = l.last_execution_id )
-- order by l.job_name;


-- List all job executions and their most recent status. (Includes all executions, not just the latest per job.)

select j.execution_id, j.job_name,h.status,h.timestamp 

from job_executions j join job_status_history h on j.execution_id=h.execution_id
where h.status NOT in('finished')
order by j.job_name, h.timestamp;


