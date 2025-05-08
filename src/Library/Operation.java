package Library;

public class Operation extends Treatment{
    private String operationName;
    public Operation(String startdate,String enddate,String operationName){
        super.setEndDate(enddate);
        super.setStartDate(startdate);
        this.operationName=operationName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}

