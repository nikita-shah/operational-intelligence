/* created by nikita */
package com.maas360.devops.intelligence.batchJob.trial;

import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<String,String>{
    @Override
    public String process(String s) throws Exception {
        return s;
    }
}
