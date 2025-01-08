--liquibase formatted sql
--changeset ayad:1
/*
     cv_processor_job Table Creation
 */

CREATE TABLE IF NOT EXISTS public.cv_processor_job
(
    process_id VARCHAR(255) PRIMARY KEY,   -- Primary key for process ID
    status VARCHAR(50),                   -- Store the status enum as a string
    file_path VARCHAR(255),                -- File path or file URL
    result TEXT,                           -- Large text column to store result (e.g., XML)
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

-- End changeset