/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;

import java.util.ArrayList;
import java.util.HashMap;

import com.maas360.devops.intelligence.batchJob.constants.GraphiteServerNames;
import com.maas360.devops.intelligence.batchJob.constants.Urls;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class MaasServerNamesReader implements ItemReader<HashMap<GraphiteServerNames, ArrayList<String>>> {

    @Autowired
    RestTemplate restTemplate;


    @Override
    public HashMap<GraphiteServerNames, ArrayList<String>> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

     HashMap<GraphiteServerNames, ArrayList<String>> prodInstanceVsServerNames = new HashMap<>();
     for(GraphiteServerNames graphiteServer: GraphiteServerNames.values())
     {
         String graphitServerUrl = graphiteServer.getUrl();
         ArrayList<String> maasServerNamesList = fetchServerNamesFromGraphite(graphitServerUrl);
         System.out.println("graphite url -> "+graphitServerUrl+ " count of maas servers ->"+maasServerNamesList.size());
         prodInstanceVsServerNames.put(graphiteServer,maasServerNamesList);
     }
     return prodInstanceVsServerNames;
    }


    private ArrayList<String> fetchServerNamesFromGraphite(String url) {

        String getServerDetialsUrl = url+ Urls.SERVER_NAME_SUB_URL;
        ArrayList<String> serverNamesList = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>("",headers);
        ResponseEntity<ServerNamesModel[]> responseEntity = restTemplate.exchange(getServerDetialsUrl, HttpMethod.GET, requestEntity, ServerNamesModel[].class);
        for(int i=0;i<responseEntity.getBody().length;i++)
        {
            serverNamesList.add(responseEntity.getBody()[i].getId());
        }
        return serverNamesList;
    }
}
