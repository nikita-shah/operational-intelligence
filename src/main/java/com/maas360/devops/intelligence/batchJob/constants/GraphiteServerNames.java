/* created by nikita */
package com.maas360.devops.intelligence.batchJob.constants;

public enum GraphiteServerNames {

   // M1_GRAPHITE("http://bp1graphite1-0.m1.sysint.local","M1_SERVERS"),
   // M2_GRAPHITE("http://fp2graphite1-0.m2.sysint.local","M2_SERVERS"),
    M3_GRAPHITE("http://dp3graphite1-0.m3.sysint.local","M3_SERVERS");
   // M4_GRAPHITE("http://cp4graphite1-0.m4.sysint.local","M4_SERVERS"),
   // M6_GRAPHITE("http://ap6graphite1-0.m6.sysint.local","M6_SERVERS");

    private String url;
    private String fileName;

    GraphiteServerNames(String url, String fileName)
    {
        this.url = url; this.fileName=fileName;
    }

    public String getFileName()
    {
        return this.fileName;
    }

    public String getUrl()
    {
        return this.url;
    }

}
