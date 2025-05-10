package com.cs320.hms.model;

import jakarta.persistence.Entity;

@Entity
public class Doctor extends User {
    public Doctor() {}

    public Doctor(String ssn, String userName, String userSurname,
                  String userRole, String e_mail, String phone, String password) {
        super(ssn, userName, userSurname, userRole, e_mail, phone, password);
    }
}
