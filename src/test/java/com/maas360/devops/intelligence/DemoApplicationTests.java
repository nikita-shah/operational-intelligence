package com.maas360.devops.intelligence;

import com.maas360.devops.intelligence.restClient.GraphiteRestTest;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	GraphiteRestTest graphiteRestTest;

	@Autowired
	JobLauncher launcher;

	@Autowired
	Job job;

    /*	@Test
	public void test_graphite_json()
	{
		String res = graphiteRestTest.testSimpleRest();
		System.out.println(res);
	}*/

   /* @Test
	public void test_job() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		JobParameters jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis()).toJobParameters();
		launcher.run(job,jobParameters);
	}*/



}
