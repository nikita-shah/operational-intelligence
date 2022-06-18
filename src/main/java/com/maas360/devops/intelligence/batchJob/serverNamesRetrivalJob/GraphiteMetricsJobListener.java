/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class GraphiteMetricsJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Graphite metrics job started");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Graphite metrics job Finished with status :"+jobExecution.getStatus());
    }
}
