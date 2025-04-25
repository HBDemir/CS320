package Library;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Doctor extends User {
    public Doctor(String ssn, String userName,String userSurname,
                  String userRole,String e_mail,String phone,String password){

        super(ssn,userName,userSurname,"Doctor",e_mail,phone,password);

    }

    public ArrayList<Patient> viewRecords(){
        ArrayList<Patient> patients= new ArrayList<>();

    return patients;}
    public ArrayList<Appointment> viewAppointments(){
        ArrayList<Appointment> appointments=new ArrayList<>();

    return appointments;}

    public Object viewVitals(String type, Patient patient) {

        if (type.equals("Prescription")) {
           ArrayList<Prescription>prescriptions= new ArrayList<Prescription>();
            return prescriptions;
        } else {
            ArrayList<Operation>operations=new ArrayList<>();
            return operations;
        }
    }

}

