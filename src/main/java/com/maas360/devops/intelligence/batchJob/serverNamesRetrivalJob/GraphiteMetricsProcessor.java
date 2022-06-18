/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob.mongo.MongoWriteGraphiteMetrics;
import com.maas360.devops.intelligence.batchJob.utils.TimeQueryHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.stream.Collectors;


import com.maas360.devops.intelligence.batchJob.constants.GraphiteServerNames;
import com.maas360.devops.intelligence.batchJob.constants.Urls;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class GraphiteMetricsProcessor implements ItemProcessor<HashMap<GraphiteServerNames, ArrayList<String>>,ArrayList<CpuMemMetricsModel> > {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private GraphiteMetricsIntervalDataRepo dateRepoMong;

    @Autowired
    TimeQueryHelper timeQueryHelper;

    @Value("classpath:static/dates.json")
    Resource resourceFile;

    @Autowired
    MongoWriteGraphiteMetrics mongoWriteGraphiteMetrics;

    @Override
    public ArrayList<CpuMemMetricsModel> process(HashMap<GraphiteServerNames, ArrayList<String>> graphiteServerNamesArrayListHashMap) throws Exception {

        ArrayList<CpuMemMetricsModel> cpuMemMetricsModelList = null;
        //do this in a loop till mongo db end date is not crossed
        while(!timeQueryHelper.getNext15MinsTimeQuery().isEmpty()) {
            //1. collect the metrics
             cpuMemMetricsModelList = retrieve_cpu_mem_usage(graphiteServerNamesArrayListHashMap);
            //2. process/transform metrics to find cpu peak average and memory peak per instance
            transformMetrics(cpuMemMetricsModelList);
            mongoWriteGraphiteMetrics.populateAndSaveMongoDB(cpuMemMetricsModelList);
            timeQueryHelper.incrementCurrentTimeInMongo();

        }

        if(timeQueryHelper.getNext15MinsTimeQuery().isEmpty()) {
            //do once more for current time
            cpuMemMetricsModelList = retrieve_cpu_mem_usage(graphiteServerNamesArrayListHashMap);
            //2. process/transform metrics to find cpu peak average and memory peak per instance
            transformMetrics(cpuMemMetricsModelList);
        }
        return cpuMemMetricsModelList;
    }

    private void transformMetrics(ArrayList<CpuMemMetricsModel> cpuMemMetricsModelList) {

        cpuMemMetricsModelList.stream().forEach(x -> {
            transformCpuMetricsPerServer(x);
            transformMemMetrics(x);
            System.out.println("For server -> "+x.getGraphiteUrl()+" memory peak-> "+x.getServerNameMemPeak()+" cpu peak-> "+x.getServerNameCpuPeak());
        });

    }

    private void transformMemMetrics(CpuMemMetricsModel cpuMemMetricsModel) {
        reduceListMemory(cpuMemMetricsModel);
        populatePeak(cpuMemMetricsModel.getServerVsMemoryMetricReduced(),cpuMemMetricsModel.getServerNameMemPeak());
    }

    private void transformCpuMetricsPerServer(CpuMemMetricsModel cpuMemMetricsModel) {
        reduceListPerCpu(cpuMemMetricsModel);
        populatePeak(cpuMemMetricsModel.getCpuVsMetric(),cpuMemMetricsModel.getCpuNameVsPeak());
        calculateAveragePeak(cpuMemMetricsModel);
    }

    private void calculateAveragePeak(CpuMemMetricsModel cpuMemMetricsModel) {
        HashMap<String,Double>serverNameCpuPeak =  new HashMap<>();
                cpuMemMetricsModel.setServerNameCpuPeak(serverNameCpuPeak);
        HashMap<String,List<Double>>serverNameListPeak = new HashMap<>();
        cpuMemMetricsModel.getCpuNameVsPeak().entrySet().forEach((entry)->{
            String serverName = entry.getKey().split("\\.")[1];
            List<Double>cpuPeaks = serverNameListPeak.get(serverName);
            if(cpuPeaks!=null)
            { cpuPeaks.add(entry.getValue());}
            else{
                cpuPeaks = new ArrayList<>(Arrays.asList(entry.getValue()));
                serverNameListPeak.put(serverName,cpuPeaks);
            }
        });

        serverNameListPeak.entrySet().forEach(entry->{Double average = findAvereageOfList(entry.getValue());
            serverNameCpuPeak.put(entry.getKey(),average) ;});

    }

    private Double findAvereageOfList(List<Double>list)
    {
        return list.stream().mapToDouble(Double::valueOf).average().orElse(0.0D);
    }
    private void reduceListPerCpu(CpuMemMetricsModel cpuMemMetricsModel) {
        HashMap<String, List<List<Double>>> cpuCoreVsMetric = cpuMemMetricsModel.getCpuCoreVsMetric();
        HashMap<String, List<Double>> cpuVsMetric = new HashMap<>();
        HashMap<String, Double> cpuNameVsPeak = new HashMap<>();
        cpuCoreVsMetric.entrySet().forEach(x -> {
            cpuVsMetric.put(x.getKey(), reduceList(x.getValue()));
        });
        cpuMemMetricsModel.setCpuVsMetric(cpuVsMetric);
        cpuMemMetricsModel.setCpuNameVsPeak(cpuNameVsPeak);
    }

    private void reduceListMemory(CpuMemMetricsModel cpuMemMetricsModel) {
        HashMap<String, List<List<Double>>> serverVsMemoryMetric = cpuMemMetricsModel.getServerVsMemoryMetric();
        HashMap<String,List<Double>>serverVsMemoryMetricReduced = new HashMap<>();
        HashMap<String,Double>serverNameMemPeak = new HashMap<>();

        serverVsMemoryMetric.entrySet().forEach(x -> {
            serverVsMemoryMetricReduced.put(x.getKey(), reduceList(x.getValue()));
        });
        cpuMemMetricsModel.setServerVsMemoryMetricReduced(serverVsMemoryMetricReduced);
        cpuMemMetricsModel.setServerNameMemPeak(serverNameMemPeak);
    }

    private List<Double> reduceList(List<List<Double>> value) {
        ArrayList<Double> extractedUsageMetrics = new ArrayList<>();
        value.stream().forEach(x -> {
            if(null!= x.get(0))
            extractedUsageMetrics.add(x.get(0));
        });
        return extractedUsageMetrics;
    }

    private void populatePeak(HashMap<String, List<Double>> source,HashMap<String, Double> dest)
    {
        source.entrySet().forEach(x->{
            List<Double> metrics = x.getValue();
            metrics = metrics.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            if(metrics.size()>0)
             dest.put(x.getKey(),metrics.get(0));
        });
    }



    public  ArrayList<CpuMemMetricsModel> retrieve_cpu_mem_usage(HashMap<GraphiteServerNames, ArrayList<String>> graphiteServerNamesArrayListHashMap)
    {
        LocalDateTime cpuStartTime = LocalDateTime.now();
        HashMap<String,ArrayList<GraphiteMetricsModel>> maasServerVsCpuMetrics = new HashMap<>();
        ArrayList<CpuMemMetricsModel> metricsModelList = new ArrayList<>();


        for(Map.Entry<GraphiteServerNames,ArrayList<String>> entry:graphiteServerNamesArrayListHashMap.entrySet())
        {
            String graphitServerUrl = entry.getKey().getUrl();
            ArrayList<String> maasServerUrlsList = entry.getValue();
            //1.  cpu usage per instance per server processing
            HashMap<String,List<List<Double>>> cpuCoreVsMetric = getCpuMetricsFromGraphite(graphitServerUrl,maasServerUrlsList);
            //2.  memory usage per instance per server processing
            HashMap<String,List<List<Double>>> memVsMetric = getMemoryMetricsFromGraphite(graphitServerUrl,maasServerUrlsList);

            //store it in the model
            CpuMemMetricsModel metricsModel = new CpuMemMetricsModel();

            metricsModel.setGraphiteUrl(graphitServerUrl);
            metricsModel.setCpuCoreVsMetric(cpuCoreVsMetric);
            metricsModel.setServerVsMemoryMetric(memVsMetric);
            metricsModel.setGraphiteDate(timeQueryHelper.getCurrentTime().isEmpty()?timeQueryHelper.getGraphiteTimeNow():timeQueryHelper.getCurrentTime());
            metricsModel.setEpoch(timeQueryHelper.getCurrentTimeEpoch());
            metricsModelList.add(metricsModel);
        }
        LocalDateTime cpuFinishTime = LocalDateTime.now();
        System.out.println("Time for capturing usage metrics : "+ Duration.between(cpuStartTime, cpuFinishTime).toMillis()/1000+"sec");

        return metricsModelList;
    }

    private String getTimeQuerySubUrl()
    {

        StringBuilder timeQueryBuilder = new StringBuilder();
        String currentTime = timeQueryHelper.getCurrentTime();
        if(currentTime.isEmpty())
        {
           //mongo db document dates have been crossed hence only scan for last 15 mins
           timeQueryBuilder.append(Urls.TIME_DURATION_15_MIN_SUB_URL);
        }
        else
        {
            String endTime = timeQueryHelper.getNext15MinsTimeQuery();
            //we have a vaild to and from that is derived from the couch document
            timeQueryBuilder.append(Urls.TIME_DURATION_ABSOLUTE_FROM_SUB_URL)
                    .append(currentTime)
                    .append(Urls.TIME_DURATION_ABSOLUTE_TO_SUB_URL)
                    .append(endTime);
        }
        return timeQueryBuilder.toString();
    }

    private HashMap<String,List<List<Double>>> getCpuMetricsFromGraphite(String graphitServerUrl,ArrayList<String> maasServerUrlsList )
    {

        LocalDateTime cpuStartTime = LocalDateTime.now();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>("",headers);
        HashMap<String,List<List<Double>>> cpuCoreVsMetric = new HashMap<>();
        StringBuilder urlBuilder = new StringBuilder(graphitServerUrl);
        urlBuilder.append(Urls.RENDER_API_SUB_URL).append("servers.*").append(Urls.CPU_USAGE_SUB_URL).append(getTimeQuerySubUrl());
        try {
            ResponseEntity<GraphiteMetricsModel[]> responseEntity = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET,
                    requestEntity, GraphiteMetricsModel[].class);

            ArrayList<GraphiteMetricsModel> metricsList = new ArrayList<>(Arrays.asList(responseEntity.getBody()));
            for(GraphiteMetricsModel metric :metricsList)
            {
                List<List<Double>> cpuUsageList = cpuCoreVsMetric.get(metric.getTarget());
                if(null!=cpuUsageList)
                {
                    cpuUsageList.addAll(metric.getDatapoints());
                }
                else{
                    cpuUsageList = metric.getDatapoints();
                }
                cpuCoreVsMetric.put(metric.getTarget(),cpuUsageList);
            }

        }catch (Exception e)
        {
            System.out.println("Exception while reaching to "+urlBuilder.toString()+" exception-> "+e);
        }
        LocalDateTime cpuFinishTime = LocalDateTime.now();
        System.out.println("Fetched cpu usage metrics for graphite server -> " +graphitServerUrl
                + " query -> "+ getTimeQuerySubUrl()+" time -> "+ Duration.between(cpuStartTime, cpuFinishTime).toMillis()/1000+"sec");
        return cpuCoreVsMetric;
    }

    private HashMap<String,List<List<Double>>> getMemoryMetricsFromGraphite(String graphitServerUrl,ArrayList<String> maasServerUrlsList )
    {

        LocalDateTime cpuStartTime = LocalDateTime.now();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>("",headers);


        HashMap<String,List<List<Double>>> cpuCoreVsMetric = new HashMap<>();
        StringBuilder urlBuilder = new StringBuilder(graphitServerUrl);
        urlBuilder.append(Urls.RENDER_API_SUB_URL).append("servers.*").append(Urls.MEM_USAGE_SUB_URL).append(getTimeQuerySubUrl());
        try {
            ResponseEntity<GraphiteMetricsModel[]> responseEntity = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, requestEntity, GraphiteMetricsModel[].class);

        ArrayList<GraphiteMetricsModel> metricsList = new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        for(GraphiteMetricsModel metric :metricsList)
        {
            String maasServerUrl = metric.getTarget().split("\\.")[1];
            List<List<Double>> memUsageList = metric.getDatapoints();
            cpuCoreVsMetric.put(maasServerUrl,memUsageList);
        }
        }catch (Exception e)
        {
            System.out.println("Exception while reaching to "+urlBuilder.toString()+" exception-> "+e);
        }
        LocalDateTime cpuFinishTime = LocalDateTime.now();
        System.out.println("Fetched memory usage metrics for graphite server -> " +graphitServerUrl  + " query -> "+ getTimeQuerySubUrl()
                + " time -> "+ Duration.between(cpuStartTime, cpuFinishTime).toMillis()/1000+"sec");
        return cpuCoreVsMetric;
    }

    private DataCollectionDates getToAndFromDates()  {

        DataCollectionDates dataCollectionDates = new DataCollectionDates();

        try {
            FileReader reader = new FileReader(resourceFile.getFile());
            JSONParser jsonParser = new JSONParser();
            JSONObject dateJsonObj = (JSONObject)jsonParser.parse(reader);
            String currDate = dateJsonObj.get("currDate").toString();
            String endDate = dateJsonObj.get("endDate").toString();
            dataCollectionDates.setCurrentDate(currDate);
            dataCollectionDates.setEndDate(endDate);
        }catch (Exception e)
        {
            System.out.println("Exception"+e);
        }

        return dataCollectionDates;

    }

    private void updateCurrDate(DataCollectionDates dataCollectionDates)  {
        try {
            JSONObject dateJsonObj = new JSONObject();
            dateJsonObj.put("currDate", dataCollectionDates.getCurrentDate());
            dateJsonObj.put("endDate", dataCollectionDates.getEndDate());
            System.out.println("Writing file ->"+dateJsonObj.toJSONString());
            File file = resourceFile.getFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(dateJsonObj.toJSONString());
        }catch (Exception e)
        {
            System.out.println("Exception"+e);
        }

    }

}
