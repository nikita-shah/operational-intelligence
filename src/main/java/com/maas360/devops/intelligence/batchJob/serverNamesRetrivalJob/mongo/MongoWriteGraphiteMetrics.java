/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob.mongo;

import java.util.ArrayList;
import java.util.HashMap;

import com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob.CpuMemMetricsModel;
import com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob.MongoDBMetricModel;
import com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob.MongoDBrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoWriteGraphiteMetrics {
    @Autowired
    MongoDBrepository mongoDBrepository ;
    public void populateAndSaveMongoDB(ArrayList<CpuMemMetricsModel> list)
    {
        ArrayList<MongoDBMetricModel> metricsList = new ArrayList<>();
        list.forEach(x->{
            //long epoch = Instant.now().toEpochMilli();
            long epoch = x.getEpoch();
            HashMap<String,Double> serverNameCpuPeak = x.getServerNameCpuPeak();
            HashMap<String,Double>serverNameMemPeak = x.getServerNameMemPeak();

            serverNameCpuPeak.entrySet().forEach(oneAppServer->{
                MongoDBMetricModel model = new MongoDBMetricModel();
                String serverName = oneAppServer.getKey();
                model.setId(serverName+epoch);
                model.setServerId(serverName);
                model.setCpuPeakAverage(oneAppServer.getValue());
                model.setEpochTime(epoch);
                model.setGraphiteTime(x.getGraphiteDate());
                model.setMemoryPeakAverage(serverNameMemPeak.get(serverName));
                metricsList.add(model);
            });

            //check if a valid use case exists where cpu usage was not available from graphite but memory usage was available.
        });
        System.out.println("Writer writing in mongo db objects number -> "+metricsList.size());
        mongoDBrepository.saveAll(metricsList);
    }
}
