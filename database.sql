-- Users table
CREATE TABLE Users (
                       ssn VARCHAR(20) PRIMARY KEY,
                       userName VARCHAR(50) NOT NULL,
                       userSurname VARCHAR(50) NOT NULL,
                       userRole VARCHAR(20) NOT NULL CHECK (userRole IN ('Doctor', 'Nurse', 'Patient')),
                       e_mail VARCHAR(100),
                       phone VARCHAR(20),
                       password VARCHAR(100) NOT NULL
);

-- Patients table
CREATE TABLE Patients (
                          ssn VARCHAR(20) PRIMARY KEY,
                          FOREIGN KEY (ssn) REFERENCES Users(ssn)
);

-- Doctors table
CREATE TABLE Doctors (
                         ssn VARCHAR(20) PRIMARY KEY,
                         specialization VARCHAR(100),
                         FOREIGN KEY (ssn) REFERENCES Users(ssn)
);

-- Nurses table
CREATE TABLE Nurses (
                        ssn VARCHAR(20) PRIMARY KEY,
                        department VARCHAR(100),
                        FOREIGN KEY (ssn) REFERENCES Users(ssn)
);

-- Appointments table
CREATE TABLE Appointments (
                              appointment_id SERIAL PRIMARY KEY,
                              date TIMESTAMP NOT NULL,
                              doctor_ssn VARCHAR(20) NOT NULL,
                              patient_ssn VARCHAR(20) NOT NULL,
                              FOREIGN KEY (doctor_ssn) REFERENCES Doctors(ssn),
                              FOREIGN KEY (patient_ssn) REFERENCES Patients(ssn)
);

-- Treatments table
CREATE TABLE Treatments (
                            treatment_id SERIAL PRIMARY KEY,
                            startDate DATE NOT NULL,
                            endDate DATE,
                            treatment_type VARCHAR(20) NOT NULL CHECK (treatment_type IN ('Operation', 'Prescription'))
);

-- Operations table
CREATE TABLE Operations (
                            treatment_id INTEGER PRIMARY KEY,
                            operationName VARCHAR(100) NOT NULL,
                            FOREIGN KEY (treatment_id) REFERENCES Treatments(treatment_id)
);

-- Prescriptions table
CREATE TABLE Prescriptions (
                               treatment_id INTEGER PRIMARY KEY,
                               prescriptionID DECIMAL(10,2) UNIQUE NOT NULL,
                               FOREIGN KEY (treatment_id) REFERENCES Treatments(treatment_id)
);

-- Medications in prescriptions
CREATE TABLE Medications (
                             medication_id SERIAL PRIMARY KEY,
                             treatment_id INTEGER NOT NULL,
                             medicationName VARCHAR(100) NOT NULL,
                             dosage VARCHAR(50) NOT NULL,
                             FOREIGN KEY (treatment_id) REFERENCES Prescriptions(treatment_id)
);

-- Link patients to treatments
CREATE TABLE PatientTreatments (
                                   patient_ssn VARCHAR(20) NOT NULL,
                                   treatment_id INTEGER NOT NULL,
                                   PRIMARY KEY (patient_ssn, treatment_id),
                                   FOREIGN KEY (patient_ssn) REFERENCES Patients(ssn),
                                   FOREIGN KEY (treatment_id) REFERENCES Treatments(treatment_id)
);