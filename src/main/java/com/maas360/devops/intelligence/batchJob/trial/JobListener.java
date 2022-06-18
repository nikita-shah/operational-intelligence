/* created by nikita */
package com.maas360.devops.intelligence.batchJob.trial;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Old Job started");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Old job finished with status :"+jobExecution.getStatus());
    }
}
