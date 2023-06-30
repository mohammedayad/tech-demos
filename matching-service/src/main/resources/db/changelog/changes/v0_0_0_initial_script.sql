--liquibase formatted sql
--changeset ayad:2
/*
     prefixes_matching Table Creation
 */
CREATE TABLE IF NOT EXISTS public.prefixes_matching
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prefix VARCHAR(255) NOT NULL UNIQUE
);

-- Insert data from CSV file
-- Make sure to adjust the file path and table name accordingly
-- Use the classpath prefix to specify the file path relative to the classpath
-- Change the FIELDS TERMINATED BY value based on your CSV file format
-- Replace `prefix` with the corresponding column name in your CSV file
INSERT INTO prefixes_matching (prefix)
SELECT * FROM CSVREAD('classpath:/sample_prefixes.csv', NULL, 'UTF-8', '\n');


-- End changeset

/*
INSERT INTO prefixes (prefix) VALUES
(''),
('XYZ:');

LOAD DATA INFILE '/sample_prefixes.csv'
INTO TABLE prefixes_matching
FIELDS TERMINATED BY '\n' -- Specify the field delimiter as a new line
(prefix);



LOAD DATA FROM 'classpath:springboot/sample_prefixes.csv' INTO prefixes_matching (prefix)
FORMAT CSV;

 */