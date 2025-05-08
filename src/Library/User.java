package Library;

public class User {
    private String ssn;
    private String userName;
    private String userSurname;
    private String userRole;
    private String e_mail;
    private String phone;
    private String password;
    public User(String ssn, String userName,String userSurname,
                String userRole,String e_mail,String phone,String password){
        this.ssn=ssn;
        this.userName=userName;
        this.userSurname=userSurname;
        this.userRole=userRole;
        this.e_mail=e_mail;
        this.phone=phone;
        this.password=password;


    }
    public String viewProfile(){
        return ssn +","+ userName +","+userSurname+","+userRole+","+e_mail+","+phone+","+password;
    }
    public void modifyPofile(String ssn, String userName,String userSurname,
                             String userRole,String e_mail,String phone,String password){
        setE_mail(e_mail);
        setPassword(password);
        setPhone(phone);
        setUserName(userName);
        setUserRole(userRole);
        setSsn(ssn);
        setUserSurname(userSurname);
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }
}


