DROP TABLE IF EXISTS quantity_measurement;

CREATE TABLE quantity_measurement (
    id VARCHAR(50) PRIMARY KEY,
    value DOUBLE,
    unit VARCHAR(50)
);