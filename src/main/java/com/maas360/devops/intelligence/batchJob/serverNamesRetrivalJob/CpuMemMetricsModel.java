/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;

import java.util.HashMap;
import java.util.List;




public class CpuMemMetricsModel {

    private String graphiteUrl;
    HashMap<String,List<List<Double>>> cpuCoreVsMetric;
    HashMap<String,List<Double>> cpuVsMetric;
    HashMap<String, List<List<Double>>>serverVsMemoryMetric;
    HashMap<String, List<Double>> serverVsMemoryMetricReduced;
    HashMap<String,Double>cpuNameVsPeak;
    HashMap<String,Double>serverNameCpuPeak;
    HashMap<String,Double>serverNameMemPeak;
    long epoch;
    String graphiteDate;

    public String getGraphiteDate() {
        return graphiteDate;
    }

    public void setGraphiteDate(String graphiteDate) {
        this.graphiteDate = graphiteDate;
    }

    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public String getGraphiteUrl() {
        return graphiteUrl;
    }

    public void setGraphiteUrl(String graphiteUrl) {
        this.graphiteUrl = graphiteUrl;
    }

    public HashMap<String, List<List<Double>>> getCpuCoreVsMetric() {
        return cpuCoreVsMetric;
    }

    public void setCpuCoreVsMetric(HashMap<String, List<List<Double>>> cpuCoreVsMetric) {
        this.cpuCoreVsMetric = cpuCoreVsMetric;
    }

    public HashMap<String, List<Double>> getCpuVsMetric() {
        return cpuVsMetric;
    }

    public void setCpuVsMetric(HashMap<String, List<Double>> cpuVsMetric) {
        this.cpuVsMetric = cpuVsMetric;
    }


    public HashMap<String, Double> getCpuNameVsPeak() {
        return cpuNameVsPeak;
    }

    public void setCpuNameVsPeak(HashMap<String, Double> cpuNameVsPeak) {
        this.cpuNameVsPeak = cpuNameVsPeak;
    }

    public HashMap<String, Double> getServerNameCpuPeak() {
        return serverNameCpuPeak;
    }

    public void setServerNameCpuPeak(HashMap<String, Double> serverNameCpuPeak) {
        this.serverNameCpuPeak = serverNameCpuPeak;
    }


    public HashMap<String, List<List<Double>>> getServerVsMemoryMetric() {
        return serverVsMemoryMetric;
    }

    public void setServerVsMemoryMetric(HashMap<String, List<List<Double>>> serverVsMemoryMetric) {
        this.serverVsMemoryMetric = serverVsMemoryMetric;
    }

    public HashMap<String, List<Double>> getServerVsMemoryMetricReduced() {
        return serverVsMemoryMetricReduced;
    }

    public void setServerVsMemoryMetricReduced(HashMap<String, List<Double>> serverVsMemoryMetricReduced) {
        this.serverVsMemoryMetricReduced = serverVsMemoryMetricReduced;
    }

    public HashMap<String, Double> getServerNameMemPeak() {
        return serverNameMemPeak;
    }

    public void setServerNameMemPeak(HashMap<String, Double> serverNameMemPeak) {
        this.serverNameMemPeak = serverNameMemPeak;
    }


}
