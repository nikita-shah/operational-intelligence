/* created by nikita */
package com.maas360.devops.intelligence.batchJob.trial;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;


public class Reader implements ItemReader<String> {

    @Autowired
    RestTemplate restTemplate;

    String url = "http://dp3graphite1-0.m3.sysint.local/metrics/find?query=servers.dp3portalapp3-2_m3_sysint_local.cpu*";

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        String res = fetchFromServer( url);
        return res;
    }

    private String fetchFromServer(String url) {
        return restTemplate.getForObject(url,String.class);
    }
}
