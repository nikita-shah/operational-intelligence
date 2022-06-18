/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dataCollectionDate")
public class DataCollectionDates {
    @Id
    String id;
    String currentDate ;
    String endDate ;

    public  String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DataCollectionDates{" +
                "currentDate='" + currentDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
