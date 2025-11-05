--liquibase formatted sql
--changeset ayad:1
/*
     deliveries Table Creation
 */

CREATE TABLE IF NOT EXISTS public.deliveries
(
    id UUID PRIMARY KEY,   -- Primary key for the delivery
    vehicle_id VARCHAR(64) NOT NULL,   --  vehicle id for the delivery
    address VARCHAR(255) NOT NULL,     -- delivery address
    started_at TIMESTAMP NOT NULL,     -- delivery started at
    finished_at TIMESTAMP NULL,        -- delivery finished at
    status VARCHAR(32) NOT NULL,       -- delivery status (IN_PROGRESS or DELIVERED )
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

-- End changeset