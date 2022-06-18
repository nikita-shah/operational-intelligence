
package com.maas360.devops.intelligence;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DevOpsIntelligenceMainApp  {
	private static final Logger log = LoggerFactory.getLogger(DevOpsIntelligenceMainApp.class);




	public static void main(String[] args) throws ParseException {
		SpringApplication.run(DevOpsIntelligenceMainApp.class, args);


	}


}
            