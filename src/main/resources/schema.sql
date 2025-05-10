-- === USERS ===
CREATE TABLE Users (
                       ssn VARCHAR(20) PRIMARY KEY,
                       userName VARCHAR(50) NOT NULL,
                       userSurname VARCHAR(50) NOT NULL,
                       userRole VARCHAR(20) NOT NULL CHECK (userRole IN ('Doctor', 'Nurse', 'Patient')),
                       e_mail VARCHAR(100),
                       phone VARCHAR(20),
                       password VARCHAR(100) NOT NULL
);

-- === PATIENTS ===
CREATE TABLE Patients (
                          ssn VARCHAR(20) PRIMARY KEY,
                          FOREIGN KEY (ssn) REFERENCES Users(ssn) ON DELETE CASCADE
);

-- === DOCTORS ===
CREATE TABLE Doctors (
                         ssn VARCHAR(20) PRIMARY KEY,
                         specialization VARCHAR(100),
                         FOREIGN KEY (ssn) REFERENCES Users(ssn) ON DELETE CASCADE
);

-- === NURSES ===
CREATE TABLE Nurses (
                        ssn VARCHAR(20) PRIMARY KEY,
                        department VARCHAR(100),
                        FOREIGN KEY (ssn) REFERENCES Users(ssn) ON DELETE CASCADE
);

-- === APPOINTMENTS ===
CREATE TABLE Appointments (
                              appointment_id SERIAL PRIMARY KEY,
                              date TIMESTAMP NOT NULL,
                              doctor_ssn VARCHAR(20) NOT NULL,
                              patient_ssn VARCHAR(20) NOT NULL,
                              FOREIGN KEY (doctor_ssn) REFERENCES Doctors(ssn) ON DELETE CASCADE,
                              FOREIGN KEY (patient_ssn) REFERENCES Patients(ssn) ON DELETE CASCADE
);

-- === TREATMENTS ===
CREATE TABLE Treatments (
                            treatment_id SERIAL PRIMARY KEY,
                            startDate DATE NOT NULL,
                            endDate DATE,
                            treatment_type VARCHAR(20) NOT NULL CHECK (treatment_type IN ('Operation', 'Prescription')),
                            patient_ssn VARCHAR(20) NOT NULL,
                            FOREIGN KEY (patient_ssn) REFERENCES Patients(ssn) ON DELETE CASCADE
);

-- === OPERATIONS ===
CREATE TABLE Operations (
                            treatment_id INTEGER PRIMARY KEY,
                            operationName VARCHAR(100) NOT NULL,
                            FOREIGN KEY (treatment_id) REFERENCES Treatments(treatment_id) ON DELETE CASCADE
);

-- === PRESCRIPTIONS ===
CREATE TABLE Prescriptions (
                               treatment_id INTEGER PRIMARY KEY,
                               prescriptionID DECIMAL(10, 2) UNIQUE NOT NULL,
                               FOREIGN KEY (treatment_id) REFERENCES Treatments(treatment_id) ON DELETE CASCADE
);

-- === MEDICATIONS ===
CREATE TABLE Medications (
                             medication_id SERIAL PRIMARY KEY,
                             treatment_id INTEGER NOT NULL,
                             medicationName VARCHAR(100) NOT NULL,
                             dosage VARCHAR(50) NOT NULL,
                             FOREIGN KEY (treatment_id) REFERENCES Prescriptions(treatment_id) ON DELETE CASCADE
);

-- === PATIENT-TREATMENT LINK ===
CREATE TABLE PatientTreatments (
                                   patient_ssn VARCHAR(20) NOT NULL,
                                   treatment_id INTEGER NOT NULL,
                                   PRIMARY KEY (patient_ssn, treatment_id),
                                   FOREIGN KEY (patient_ssn) REFERENCES Patients(ssn) ON DELETE CASCADE,
                                   FOREIGN KEY (treatment_id) REFERENCES Treatments(treatment_id) ON DELETE CASCADE
);
