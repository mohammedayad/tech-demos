--liquibase formatted sql
--changeset ayad:1
/*
     stock Table Creation
 */

CREATE TABLE IF NOT EXISTS public.stock
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    current_price DECIMAL(10,2) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- End changeset