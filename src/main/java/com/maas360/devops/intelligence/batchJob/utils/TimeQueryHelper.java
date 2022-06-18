/* created by nikita */
package com.maas360.devops.intelligence.batchJob.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob.DataCollectionDates;
import com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob.GraphiteMetricsIntervalDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeQueryHelper {

    private  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm_yyyyMMdd");

    @Autowired
    GraphiteMetricsIntervalDataRepo mongoDate;

    private  String add15MinsToCurrent(String currTime)
    {
        Date newDate = null;
        try {
            Date date = sdf.parse(currTime);
             newDate = Date.from( date.toInstant().plusSeconds(900));

        } catch (ParseException e) {
            e.printStackTrace();
            newDate = new Date();
        }
        return sdf.format(newDate);
    }

    public  String getNext15MinsTimeQuery()
    {
        String next15MinTimeQuery = "";
        DataCollectionDates dataCollectionDates = mongoDate.findById("DATE_ID").get();
        if(isNext15MinsValid(dataCollectionDates))
        {
            next15MinTimeQuery =  add15MinsToCurrent(dataCollectionDates.getCurrentDate());
        }

      return next15MinTimeQuery;

    }


    public String getCurrentTime()
    {
        DataCollectionDates dataCollectionDates = mongoDate.findById("DATE_ID").get();
        if(isNext15MinsValid(dataCollectionDates))
        return dataCollectionDates.getCurrentDate();
        else return "";
    }

    public long getCurrentTimeEpoch()
    {
        DataCollectionDates dataCollectionDates = mongoDate.findById("DATE_ID").get();
        Date currDate = new Date();
        if(isNext15MinsValid(dataCollectionDates)) {
            try {
                currDate =  sdf.parse(dataCollectionDates.getCurrentDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
       return currDate.toInstant().toEpochMilli();
    }

    public String getGraphiteTimeNow()
    {
        Date now = new Date(Instant.now().toEpochMilli());
        String graphiteStyleDate = sdf.format(now);
        return graphiteStyleDate;
    }

    public  void incrementCurrentTimeInMongo()
    {
        String next15MinTimeQuery =  getNext15MinsTimeQuery();
        DataCollectionDates dataCollectionDates = mongoDate.findById("DATE_ID").get();
        dataCollectionDates.setCurrentDate(next15MinTimeQuery);
        mongoDate.save(dataCollectionDates);
    }

    public boolean isNext15MinsValid(DataCollectionDates dataCollectionDates)
    {
        String proposedDateStr = add15MinsToCurrent(dataCollectionDates.getCurrentDate());
        boolean isValid = true;
        try {
            Date endDate = sdf.parse(dataCollectionDates.getEndDate());
            Date proposedDate = sdf.parse(proposedDateStr);
            if(proposedDate.after(endDate))
                isValid = false;
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return isValid;
    }


}
