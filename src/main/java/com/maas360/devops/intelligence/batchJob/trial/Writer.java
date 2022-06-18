/* created by nikita */
package com.maas360.devops.intelligence.batchJob.trial;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class Writer implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> list) throws Exception {
        System.out.println(list);
    }
}
