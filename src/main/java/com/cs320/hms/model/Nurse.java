package com.cs320.hms.model;

import jakarta.persistence.Entity;

@Entity
public class Nurse extends User {
    public Nurse() {}

    public Nurse(String ssn, String userName, String userSurname,
                 String userRole, String e_mail, String phone, String password) {
        super(ssn, userName, userSurname, userRole, e_mail, phone, password);
    }
}
