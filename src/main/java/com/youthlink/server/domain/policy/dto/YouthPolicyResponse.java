package com.youthlink.server.domain.policy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class YouthPolicyResponse {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("youthPolicy")
    private List<PolicyItem> youthPolicies;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PolicyItem {
        private String bizId;
        private String polyBizSjnm;
        private String polyItcnCn;
        private String sporCn;
        private String rqutPrdCn;
        private String ageInfo;
        private String empmSttsCd;
        private String accrRqisCd;
        private String incmRqisCn;
        private String cnsgNmor;
        private String polyUrl;
        private String ctpvNm;
    }
}
