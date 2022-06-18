/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GraphiteMetricsIntervalDataRepo extends MongoRepository<DataCollectionDates, String> {
}
