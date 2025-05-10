package com.cs320.hms.model;

import jakarta.persistence.Entity;

@Entity
public class Operation extends Treatment {

    private String operationName;

    public Operation() {}

    public Operation(String startDate, String endDate, String operationName) {
        setStartDate(startDate);
        setEndDate(endDate);
        this.operationName = operationName;
    }

    public String getOperationName() { return operationName; }
    public void setOperationName(String operationName) { this.operationName = operationName; }
}
