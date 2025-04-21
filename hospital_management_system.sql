CREATE DATABASE Hospital_Management_System;
\c Hospital_Management_System;

CREATE TYPE treatment_type AS ENUM ('medication', 'surgery');

CREATE TABLE doctor (
    ssn VARCHAR(11) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    role VARCHAR(50),
    phone_number VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    proficiency VARCHAR(100)
);

CREATE TABLE nurse (
    ssn VARCHAR(11) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

CREATE TABLE patient (
    ssn VARCHAR(11) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(20)
);

CREATE TABLE prescription (
    id SERIAL PRIMARY KEY,
    doctor_ssn VARCHAR(11),
    patient_ssn VARCHAR(11),
    date DATE NOT NULL,
    description TEXT,
    FOREIGN KEY (doctor_ssn) REFERENCES doctor(ssn),
    FOREIGN KEY (patient_ssn) REFERENCES patient(ssn) ON DELETE CASCADE
);

CREATE TABLE treatment (
    id SERIAL PRIMARY KEY,
    patient_ssn VARCHAR(11),
    doctor_ssn VARCHAR(11),
    description TEXT,
    type treatment_type,
    FOREIGN KEY (patient_ssn) REFERENCES patient(ssn) ON DELETE CASCADE,
    FOREIGN KEY (doctor_ssn) REFERENCES doctor(ssn)
);

