/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob.mongo.MongoWriteGraphiteMetrics;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class MongoDBWriter implements ItemWriter<ArrayList<CpuMemMetricsModel>> {

    @Autowired
    MongoDBrepository mongoDBrepository ;

    @Autowired
    MongoWriteGraphiteMetrics mongoWriteGraphiteMetrics;

    @Override
    public void write(List<? extends ArrayList<CpuMemMetricsModel>> list) throws Exception {
        ArrayList<CpuMemMetricsModel> metricsList = new ArrayList<>();
        list.forEach(x->{metricsList.addAll(x);});
        mongoWriteGraphiteMetrics.populateAndSaveMongoDB(metricsList);
    }



}
