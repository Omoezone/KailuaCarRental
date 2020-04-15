DROP DATABASE IF EXISTS kailua;

CREATE DATABASE kailua;
/*Vi laver et CREATE statement hvor vi laver databasen ved navn Kailua.
Vi sørger for først at have et DROP statement der sletter databasen hvis den allerede eksiterer*/
USE kailua;
/*Vi laver et USE statment så vi ved at resten af vores script bruger den nye database*/

DROP TABLE IF EXISTS cars;
/*Før vi laver alle tabeller laver vi DROP statements ligesom vi gjorde da vi lavede databasen*/

/*Her laver vi vores første tabel som holder styr på bilerne hos biludlejningsfirmaet
Vi bruger bilernes nummerplader som PRIMARY KEY istedet for en car_id kolone. 
Det gjorde vi da nummerpladerne på bilerne alligevel vil være unikke for hver bil
Vi bruger TINY INT hos nogle af kolonerne som booleans. Hvis en TINY INT er lig 0, svare det til en falsk boolean.
Er den 1 eller over svare det til at den er sand*/
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

/*Vi laver en tabel over alle de forskellige postnumre som kunderne skal bruge.
Vi bruger selve postnummeret som PRIMARY KEY. 
Vi har tænkt os at bruge postnummerne som FOREIGN KEY i vores kunde tabel. Derfor er det vigtigt at lave dette tabel først.*/
CREATE TABLE zips
(
	zip_code INT  PRIMARY KEY UNIQUE  NOT NULL,
    zip_city VARCHAR(45)  NOT NULL
);

DROP TABLE IF EXISTS customers;
/*Vi laver en kunde tabel som bruger en customer_id som PRIMARY KEY. Vi bruger keywordet AUTO INCREMENT til at holde styr på customer_id
så vi ikke behøver at holde styr på hvor mange kunder der er i databasen.
Vi valgte at customer_phone ikke behøvede CONSTRAINTEN NOT NULL fordi ikke alle har en fastnet telefon mere.
Vi laver så en kolone CONSTRAINT til at tilsutter zip_code som FOREIGN KEY fra zips tabelen
En CONSTRAINT bruges til at begrænse hvilken data der kan tilføjes til kolonen, 
altså vil der kun kunne være en allerede eksisterende zip_code indsat som foreign key*/
CREATE TABLE customers
(
	customer_id  INT  PRIMARY KEY NOT NULL AUTO_INCREMENT, 
    customer_first_name VARCHAR(45)  NOT NULL, 
    customer_last_name VARCHAR(45)  NOT NULL, 
    customer_address VARCHAR(45)  NOT NULL, 
    customer_license_number VARCHAR(45)  NOT NULL, 
    customer_mobile_phone VARCHAR(45)  NOT NULL, 
    customer_phone VARCHAR(45), 
    customer_email VARCHAR(45)  NOT NULL, 
    customer_driver_since_date DATE  NOT NULL, 
    zip_code INT  NOT NULL, 
    CONSTRAINT customer_fk_zips 
		FOREIGN KEY (zip_code) REFERENCES zips (zip_code)
);

DROP TABLE IF EXISTS contracts;

/*Vores contracts tabel bruger contaract_id som PRIMARY KEY som igen bliver keywordet AUTO_INCREMENT af samme grund som customer_ID
Denne tabel bruger 2 CONSTRAINTS til 2 FOEIGN KEYS, customer_id og car_reg_number. 
Dette gør at vi kan/skal koble en kunde og en bil sammen på en kontrakt*/
CREATE TABLE contracts
(
	contract_id INT  PRIMARY KEY AUTO_INCREMENT  NOT NULL, 
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

/*Til sidst har vi en masse INSERT INTO statements som udfylder vores tabeller.
Vi sørger for at indsætte de nødvendige tabeller, hvis PRIMARY KEYS bruges som FOREIGN KEYS i andre tabeller først.*/
INSERT INTO zips VALUES 
	(2200,'Nørrebro'),
    (2000, 'Frederiksberg'),
    (2100,'Østerbro');
    
INSERT INTO customers VALUES
	(1,'Hans','Hansen','Nørrebrogade 45','2983989','83982021',null,'Hans@Hansen.dk','1960-02-12',2200),
    (2,'Jørgen','Andersen','Borgmester Fischers vej 15',' 2992093','27365481','31031929','j_jørgen@hotmail.com','1975-05-25',2000),
    (3,'Mette','Hjortberg','Østerbrogade 47','8908987', '75679065',null,'Mette89@gmail.com','1996-07-30',2100),
    (4,'Søren','Henriksen','Højstrupvej 20','29084591','2928594','295849','s@Henriksen.com','2010-06-13',2200);
    
INSERT INTO cars VALUES 
	('AT28938','Sport','Porsche','911 2017',1,0,370,'leather',2,1,4921,'benzin','2020-03-30',0),
    ('DC29309','Luxury','Mercedes Benz','C-class',1,1,250,'leather','5','1',2900,'Benzin','2011-11-25',100),
    ('PO20658','Family','Volvo','V90',1,0,210,'polyester',7,1,2900,'disel','2018-11-07',250);
    
INSERT INTO contracts VALUES
	(1,1,'2020-04-14','2020-03-31',4000,'AT28938'),
    (2,3,'2019-09-20','2019-9-27',5000,'PO20658'),
    (3,2,'2019-02-19','2019-2-26',3000,'DC29309');
    
/*Intellj og MySQL workbence kunne ikke kommunikere sammen da de obenbart ikke kørte på samme tidszone.
Derfor at vi tilføjet en SET GLOBAL statement til at sætte tidszonen for workbence til at matche vores. */
SET GLOBAL time_zone = '+1:00';

/*Et SELECT statement vi bruger til at lave en query over alle kunder som ikke har en kontrakt i vores database*/
SELECT *
FROM   customers c
WHERE  NOT EXISTS (SELECT * FROM   contracts con WHERE  c.customer_id = con.customer_id);


