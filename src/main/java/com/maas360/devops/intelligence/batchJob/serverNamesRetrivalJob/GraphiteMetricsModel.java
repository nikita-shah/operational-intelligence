/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "target",
        "datapoints"
})
public class GraphiteMetricsModel {
    @JsonProperty("target")
    private String target;
    @JsonProperty("datapoints")
    private List<List<Double>> datapoints = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("target")
    public String getTarget() {
        return target;
    }

    @JsonProperty("target")
    public void setTarget(String target) {
        this.target = target;
    }

    @JsonProperty("datapoints")
    public List<List<Double>> getDatapoints() {
        return datapoints;
    }

    @JsonProperty("datapoints")
    public void setDatapoints(List<List<Double>> datapoints) {
        this.datapoints = datapoints;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}






