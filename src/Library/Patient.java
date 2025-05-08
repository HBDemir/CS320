package Library;

public class Patient extends User {

    public Patient(String ssn, String userName,String userSurname,
                   String userRole,String e_mail,String phone){

        super(ssn,userName,userSurname,"Patient",e_mail,phone,".");

    }


}

