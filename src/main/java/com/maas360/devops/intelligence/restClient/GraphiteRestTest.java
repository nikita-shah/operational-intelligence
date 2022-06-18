/* created by nikita */
package com.maas360.devops.intelligence.restClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class GraphiteRestTest {
    @Autowired
    RestTemplate restTemplate;

    public String testSimpleRest()
    {

        StringBuilder sb  = new StringBuilder("http://dp3graphite1-0.m3.sysint.local/metrics/find?query=servers.dp3portalapp3-2_m3_sysint_local.cpu*");
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(sb.toString()).build();
         String response = "";
        try
        {
            HttpEntity<String> requestEntity = new HttpEntity<String>("",new HttpHeaders());
            ResponseEntity<String> resFromRse = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, requestEntity, String.class);
            response = resFromRse.getBody();
        }
        catch (RestClientException e)
        {
            System.out.println(e);
        }
       return response;
    }


}
