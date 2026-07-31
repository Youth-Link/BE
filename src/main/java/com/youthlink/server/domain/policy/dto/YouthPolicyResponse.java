package com.youthlink.server.domain.policy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 온통청년 API 응답 형식: {resultCode, resultMessage, result: {pagging, youthPolicyList: [...]}}
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouthPolicyResponse {

    private int resultCode;
    private String resultMessage;
    private Result result;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private Pagging pagging;
        private List<PolicyItem> youthPolicyList;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pagging {
        private int totCount;
        private int pageNum;
        private int pageSize;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PolicyItem {
        private String plcyNo;
        private String plcyNm;
        private String plcyExplnCn;
        private String plcySprtCn;
        private String aplyYmd;
        private String sprtTrgtMinAge;
        private String sprtTrgtMaxAge;
        private String sprtTrgtAgeLmtYn;
        // 학력/취업 조건은 코드값만 내려오고(공통코드 API 별도 조회 필요), 텍스트 설명이 없어 코드 원문을 그대로 보관한다.
        private String jobCd;
        private String schoolCd;
        private String earnCndSeCd;
        private String earnMinAmt;
        private String earnMaxAmt;
        private String sprvsnInstCdNm;
        private String operInstCdNm;
        private String aplyUrlAddr;
        private String refUrlAddr1;
        private String refUrlAddr2;
        // 지역은 법정동코드 목록(zipCd)으로만 내려오고 시/도 명칭이 없어, 주관/운영기관명을 지역 표기 대용으로 쓴다.
        private String zipCd;

        public String toRqutPrdCn() {
            return (aplyYmd == null || aplyYmd.isBlank()) ? "상시" : aplyYmd;
        }

        public String toAgeInfo() {
            if ("N".equals(sprtTrgtAgeLmtYn)) {
                return "제한없음";
            }
            if (sprtTrgtMinAge == null || sprtTrgtMaxAge == null
                    || sprtTrgtMinAge.isBlank() || sprtTrgtMaxAge.isBlank()) {
                return "";
            }
            return "만 " + sprtTrgtMinAge + "세 ~ " + sprtTrgtMaxAge + "세";
        }

        public String toIncmRqisCn() {
            boolean noLimit = (earnMinAmt == null || "0".equals(earnMinAmt))
                    && (earnMaxAmt == null || "0".equals(earnMaxAmt));
            if (noLimit) {
                return "제한없음";
            }
            return "최소 " + earnMinAmt + "원 ~ 최대 " + earnMaxAmt + "원";
        }

        public String toCnsgNmor() {
            return (sprvsnInstCdNm != null && !sprvsnInstCdNm.isBlank()) ? sprvsnInstCdNm : operInstCdNm;
        }

        public String toPolyUrl() {
            if (aplyUrlAddr != null && !aplyUrlAddr.isBlank()) return aplyUrlAddr;
            if (refUrlAddr1 != null && !refUrlAddr1.isBlank()) return refUrlAddr1;
            return refUrlAddr2;
        }

        // 신 API는 시/도 명칭 대신 법정동코드(zipCd)만 내려줘서, 운영/주관기관명을 지역 표기 대용으로 사용한다.
        public String toCtpvNm() {
            return (operInstCdNm != null && !operInstCdNm.isBlank()) ? operInstCdNm : sprvsnInstCdNm;
        }
    }
}
