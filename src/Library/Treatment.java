package Library;

public class Treatment {
    private String startDate;
    private String endDate;



    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void updateTreatment(String endDate,String startDate){
        setStartDate(startDate);
        setEndDate(endDate);
    };
}


