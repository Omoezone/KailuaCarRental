/*DROP DATABASE IF EXISTS kailua;

CREATE DATABASE kailua;

USE kailua;

DROP TABLE IF EXISTS cars;

CREATE TABLE cars
(
	car_reg_number VARCHAR(45)	PRIMARY KEY   UNIQUE  NOT NULL,
    car_type VARCHAR(45)  NOT NULL,
    car_brand VARCHAR(45)  NOT NULL,
    car_model VARCHAR(45)  NOT NULL,
    car_cruise_control TINYINT  NOT NULL,
    car_auto_gear TINYINT  NOT NULL,
    car_hp INT  NOT NULL,
    car_seat_material VARCHAR(45)  NOT NULL,
    car_seat_number INT  NOT NULL,
    car_ac TINYINT  NOT NULL,
    car_ccm INT  NOT NULL,
    car_fuel_type VARCHAR(45)  NOT NULL,
    car_reg_date DATE  NOT NULL,
    car_odometer INT  NOT NULL
    
);

DROP TABLE IF EXISTS zips;

CREATE TABLE zips
(
	zip_code INT  PRIMARY KEY  UNIQUE  NOT NULL,
    zip_city VARCHAR(45)  NOT NULL
);

DROP TABLE IF EXISTS customers;

CREATE TABLE customers
(
	customer_id  INT  PRIMARY KEY  UNIQUE  NOT NULL,
    customer_first_name VARCHAR(45)  NOT NULL,
    customer_last_name VARCHAR(45)  NOT NULL,
    customer_address VARCHAR(45)  NOT NULL,
    customer_license_number VARCHAR(45)  NOT NULL,
    customer_mobile_phone VARCHAR(45)  NOT NULL,
    customer_phone VARCHAR(45),
    customer_email VARCHAR(45)  NOT NULL,
    customer_driver_since_date DATE  NOT NULL,
    zip_code INT UNIQUE  NOT NULL,
    CONSTRAINT customer_fk_zips
		FOREIGN KEY (zip_code) REFERENCES zips (zip_code)
);

DROP TABLE IF EXISTS contracts;

CREATE TABLE contracts
(
	contract_id INT  PRIMARY KEY  UNIQUE  NOT NULL,
    customer_id INT  UNIQUE  NOT NULL,
    contract_to_date DATETIME  NOT NULL,
    contract_from_date DATETIME NOT NULL,
    contract_max_km INT  NOT NULL,
    car_reg_number VARCHAR(45),
    CONSTRAINT contracts_fk_customers
		FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
	CONSTRAINT contracts_fk_cars
		FOREIGN KEY (car_reg_number) REFERENCES cars (car_reg_number)
);

INSERT INTO zips VALUES 
	(2200,'Nørrebro');

INSERT INTO customers VALUES
	(1,'Hans','Hansen','Nørrebrogade 45','2983989','83982021',null,'Hans@Hansen.dk','1960-02-12',2200);
    
INSERT INTO cars VALUES 
	('AT28938','Sport','Porsche','911 2017',1,0,370,'leather',2,1,4921,'benzin','2020-03-30',0);
    
INSERT INTO contracts VALUES
	(1,1,'2020-04-14','2020-03-31',4000,'AT28938');*/
    
SET GLOBAL time_zone = '+1:00';