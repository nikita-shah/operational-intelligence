/* created by nikita */
package com.maas360.devops.intelligence.batchJob.trial;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;


/*@Configuration
@EnableBatchProcessing
@EnableScheduling*/
public class BatchJobConfig {

    @Autowired
    private StepBuilderFactory sbf;

    @Autowired
    private JobBuilderFactory jbf;

    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(cron = "* * * */5 * *")
    public void perform() throws Exception {

        JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(job(), param);

    }

    @Bean
    public Job job(){
        return jbf.get("old job")
                .incrementer(new RunIdIncrementer())
                .listener(jobListener())
                .start(step())
                .build();

    }

   @Bean
   public Step step() {

        return sbf.get("old job step")
               .<String,String>chunk(1)
               .reader(reader())
               .processor(processor())
               .writer(writer())
               .build();

   }

   @Bean
   public  Reader reader(){
       return new Reader();
   }

    @Bean
    public  Writer writer(){
        return new Writer();
    }

    @Bean
    public Processor processor(){
       return new Processor();
    }

    @Bean
    public JobListener jobListener() {
        return new JobListener();
    }
}
