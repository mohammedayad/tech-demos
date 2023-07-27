--liquibase formatted sql
--changeset ayad:1
INSERT INTO stock (name, current_price,creation_date, last_update)
SELECT 'Stock ' || seqnum, seqnum * 10.00, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
FROM (
  SELECT ROW_NUMBER() OVER () AS seqnum
  FROM INFORMATION_SCHEMA.TABLES
  LIMIT 100
) AS t;





/*
DECLARE @i INT = 1;
DECLARE @name VARCHAR(50);
DECLARE @price DECIMAL(10,2);
DECLARE @date DATETIME = GETDATE();

WHILE @i <= 100
BEGIN
    SET @name = 'Product ' + CAST(@i AS VARCHAR(2));
    SET @price = @i * 10.00;
    INSERT INTO stock (name, current_price, last_update)
    VALUES (@name, @price, @date);
    SET @i = @i + 1;
END

 */