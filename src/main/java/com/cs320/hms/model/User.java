package com.cs320.hms.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    private String ssn;

    private String userName;
    private String userSurname;
    private String userRole;
    private String e_mail;
    private String phone;
    private String password;

    public User() {}

    public User(String ssn, String userName, String userSurname,
                String userRole, String e_mail, String phone, String password) {
        this.ssn = ssn;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userRole = userRole;
        this.e_mail = e_mail;
        this.phone = phone;
        this.password = password;
    }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserSurname() { return userSurname; }
    public void setUserSurname(String userSurname) { this.userSurname = userSurname; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public String getE_mail() { return e_mail; }
    public void setE_mail(String e_mail) { this.e_mail = e_mail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
