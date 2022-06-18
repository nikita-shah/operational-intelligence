/* created by nikita */
package com.maas360.devops.intelligence.batchJob.constants;

public class Urls {
   public static final String SERVER_NAME_SUB_URL = "/metrics/?query=servers.*";
   public static final String CPU_USAGE_SUB_URL = ".cpu*.percent-system&format=json";
   public static final String TIME_DURATION_15_MIN_SUB_URL = "&from=-15min";
   public static final String TIME_DURATION_ABSOLUTE_FROM_SUB_URL = "&from=";
   public static final String TIME_DURATION_ABSOLUTE_TO_SUB_URL = "&until=";
   public static final String MEM_USAGE_SUB_URL = ".memory.percent-used&format=json";
   public static final String RENDER_API_SUB_URL = "/render?target=";
}
