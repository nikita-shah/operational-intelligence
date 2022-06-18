/* created by nikita */
package com.maas360.devops.intelligence.batchJob.serverNamesRetrivalJob;


import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "leaf",
        "context",
        "text",
        "expandable",
        "id",
        "allowChildren"
})
public class ServerNamesModel {
/*
        "leaf": 0,
        "context": {},
        "text": "dp3analyticsbatch2-0_m3_sysint_local",
        "expandable": 1,
        "id": "servers.dp3analyticsbatch2-0_m3_sysint_local",
        "allowChildren": 1
 */


        @JsonProperty("leaf")
        private Integer leaf;
        @JsonProperty("context")
        private Context context;
        @JsonProperty("text")
        private String text;
        @JsonProperty("expandable")
        private Integer expandable;
        @JsonProperty("id")
        private String id;
        @JsonProperty("allowChildren")
        private Integer allowChildren;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("leaf")
        public Integer getLeaf() {
            return leaf;
        }

        @JsonProperty("leaf")
        public void setLeaf(Integer leaf) {
            this.leaf = leaf;
        }

        @JsonProperty("context")
        public Context getContext() {
            return context;
        }

        @JsonProperty("context")
        public void setContext(Context context) {
            this.context = context;
        }

        @JsonProperty("text")
        public String getText() {
            return text;
        }

        @JsonProperty("text")
        public void setText(String text) {
            this.text = text;
        }

        @JsonProperty("expandable")
        public Integer getExpandable() {
            return expandable;
        }

        @JsonProperty("expandable")
        public void setExpandable(Integer expandable) {
            this.expandable = expandable;
        }

        @JsonProperty("id")
        public String getId() {
            return id;
        }

        @JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        @JsonProperty("allowChildren")
        public Integer getAllowChildren() {
            return allowChildren;
        }

        @JsonProperty("allowChildren")
        public void setAllowChildren(Integer allowChildren) {
            this.allowChildren = allowChildren;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({})
    private static class Context {

        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }
}
