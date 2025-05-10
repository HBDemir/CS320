package com.cs320.hms.model;

import jakarta.persistence.Entity;

@Entity
public class Patient extends User {
    public Patient() {}

    public Patient(String ssn, String userName, String userSurname,
                   String userRole, String e_mail, String phone) {
        super(ssn, userName, userSurname, userRole, e_mail, phone, ".");
    }
}
