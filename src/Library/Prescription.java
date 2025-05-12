package Library;

import java.util.ArrayList;

public class Prescription extends Treatment{
    private double prescriptionID;
    private ArrayList<String> medicationName;
    private ArrayList<String> dosage;

    public Prescription(String startdate,String enddate,
                        double prescriptionID,ArrayList<String> medicationName,ArrayList<String>dosage){
        super.setEndDate(enddate);
        super.setStartDate(startdate);
        this.prescriptionID=prescriptionID;
        this.medicationName=medicationName;
        this.dosage=dosage;



    }

    public ArrayList<String[]> getMedication() {
        ArrayList<String[]>medication=new ArrayList<>();
        if (medicationName == null || dosage == null || medicationName.size() != dosage.size()) {
            // Handle potential mismatch or null lists - return empty list
            return medication;
        }
        for (int i=0; i < medicationName.size(); i++) {
            String [] tmp = new String[2]; // Create a NEW array for each medication
            tmp[0] = medicationName.get(i);
            tmp[1] = dosage.get(i);
            medication.add(tmp);
        }
        return medication;
    }

    public double getPrescriptionID() {
        return prescriptionID;
    }
}

