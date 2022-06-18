/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "test")
public class MongoDBMetricModel {


    @Id
    public String id;
    String serverId;
    Double memoryPeakAverage;
    Double cpuPeakAverage;
    String graphiteTime;
    Long epochTime;
    String graphiteDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Double getMemoryPeakAverage() {
        return memoryPeakAverage;
    }

    public void setMemoryPeakAverage(Double memoryPeakAverage) {
        this.memoryPeakAverage = memoryPeakAverage;
    }

    public Double getCpuPeakAverage() {
        return cpuPeakAverage;
    }

    public void setCpuPeakAverage(Double cpuPeakAverage) {
        this.cpuPeakAverage = cpuPeakAverage;
    }

    public Long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(Long epochTime) {
        this.epochTime = epochTime;
    }

    public String getGraphiteTime() {
        return graphiteTime;
    }

    public void setGraphiteTime(String graphiteTime) {
        this.graphiteTime = graphiteTime;
        if ( !graphiteTime.isEmpty() )
        this.graphiteDate = graphiteTime.split("_")[1];
    }



}
