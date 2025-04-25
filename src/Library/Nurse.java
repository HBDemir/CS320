package Library;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Nurse extends User {
    public Nurse(String ssn, String userName,String userSurname,
                 String userRole,String e_mail,String phone,String password){

        super(ssn,userName,userSurname,"Nurse",e_mail,phone,password);

    }



    public void sendEmail(String e_mail, Appointment appointment) {
        try {
            String subject = "Appointment Confirmation";
            String body = "Dear Patient,\n\n" +
                    "Your appointment is confirmed for " + appointment.getDate() +
                    " with Dr. " + appointment.getDoctor().getUserName() + ".\n\n" +
                    "Best regards,\nClinic Team";


            String uriStr = String.format("mailto:%s?subject=%s&body=%s",
                    e_mail,
                    URLEncoder.encode(subject, "UTF-8"),
                    URLEncoder.encode(body, "UTF-8")
            );

            URI mailto = new URI(uriStr);

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                Desktop.getDesktop().mail(mailto);
            } else {
                System.err.println("Desktop mail feature is not supported.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Appointment> viewAppointments(Patient patient){
        ArrayList<Appointment> appointments= new ArrayList<>();
        //implementation needed
    return appointments;}
    public void rescheduleAppointment(Appointment appointment,String Date){
        appointment.setDate(Date);


    }
    public void recordPatient(String ssn, String userName,String userSurname,
                              String userRole,String e_mail,String phone){
        Patient patient=new Patient(ssn,userName,userSurname,userRole,e_mail,phone);


    }
}

