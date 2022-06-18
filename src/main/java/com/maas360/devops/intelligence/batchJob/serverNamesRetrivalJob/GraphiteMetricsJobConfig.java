/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;

import java.util.ArrayList;
import java.util.HashMap;

import com.maas360.devops.intelligence.batchJob.constants.GraphiteServerNames;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableBatchProcessing
@EnableScheduling
public class GraphiteMetricsJobConfig {

    @Autowired
    private StepBuilderFactory sbf;

    @Autowired
    private JobBuilderFactory jbf;

    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(cron = "* * * */15 * *")
    public void runJob() throws Exception {

        //System.out.println("Server name retrival job Started at :" + new Date());
        JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(graphiteMetricsJob(), param);
        //System.out.println("Job finished with status :" + execution.getStatus());
    }

    @Bean
    public Job graphiteMetricsJob(){
        return jbf.get("DEV_OPS_INTELLIGENCE")
                .incrementer(new RunIdIncrementer())
                .listener(graphiteMetricsJobListener())
                .start(graphiteMetricsJobStep())
                .build();

    }

   @Bean
   public Step graphiteMetricsJobStep() {

        return sbf.get("graphiteMetricsJobStep")
               .<HashMap<GraphiteServerNames, ArrayList<String>>,ArrayList<CpuMemMetricsModel>>chunk(1)
               .reader(serverNamereader())
               .processor( graphiteMetricsProcessorprocessor())
               .writer(mongoDBWriterwriter())
               .build();

   }

   @Bean
   public MaasServerNamesReader serverNamereader(){
       return new MaasServerNamesReader();
   }

    @Bean
    public ItemWriter<? super ArrayList<CpuMemMetricsModel>> mongoDBWriterwriter(){
        return new MongoDBWriter();
    }

    @Bean
    public GraphiteMetricsProcessor graphiteMetricsProcessorprocessor(){
       return new GraphiteMetricsProcessor();
    }

    @Bean
    public GraphiteMetricsJobListener graphiteMetricsJobListener() {
        return new GraphiteMetricsJobListener();
    }
}
