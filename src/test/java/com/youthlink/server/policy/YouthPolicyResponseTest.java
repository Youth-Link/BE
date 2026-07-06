package com.youthlink.server.policy;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.youthlink.server.domain.policy.dto.YouthPolicyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class YouthPolicyResponseTest {

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    @Test
    @DisplayName("온통청년 API XML 응답을 DTO로 올바르게 파싱한다")
    void parseXmlSample() throws Exception {
        String xml = """
                <response>
                  <youthPolicy>
                    <bizId>R2023101933648</bizId>
                    <polyBizSjnm>청년 일자리 도약 장려금</polyBizSjnm>
                    <polyItcnCn>중소기업 취업 청년에게 장려금을 지원합니다</polyItcnCn>
                    <sporCn>월 60만원, 최대 12개월 지원</sporCn>
                    <rqutPrdCn>2024.01.01 ~ 2024.12.31</rqutPrdCn>
                    <ageInfo>만 15세 ~ 34세</ageInfo>
                    <empmSttsCd>미취업자</empmSttsCd>
                    <accrRqisCd>제한없음</accrRqisCd>
                    <incmRqisCn>중위소득 150% 이하</incmRqisCn>
                    <cnsgNmor>고용노동부</cnsgNmor>
                    <polyUrl>https://www.work.go.kr/youngWork</polyUrl>
                    <ctpvNm>전국</ctpvNm>
                  </youthPolicy>
                  <youthPolicy>
                    <bizId>R2023081820227</bizId>
                    <polyBizSjnm>서울 청년 월세 지원</polyBizSjnm>
                    <polyItcnCn>서울 거주 청년의 월세 부담을 줄여줍니다</polyItcnCn>
                    <sporCn>월 20만원, 최대 12개월</sporCn>
                    <rqutPrdCn>2024.03.01 ~ 2024.06.30</rqutPrdCn>
                    <ageInfo>만 19세 ~ 39세</ageInfo>
                    <empmSttsCd>제한없음</empmSttsCd>
                    <accrRqisCd>제한없음</accrRqisCd>
                    <incmRqisCn>기준 중위소득 150% 이하</incmRqisCn>
                    <cnsgNmor>서울특별시</cnsgNmor>
                    <polyUrl>https://youth.seoul.go.kr</polyUrl>
                    <ctpvNm>서울특별시</ctpvNm>
                  </youthPolicy>
                </response>
                """;

        YouthPolicyResponse response = XML_MAPPER.readValue(xml, YouthPolicyResponse.class);

        assertThat(response.getYouthPolicies()).hasSize(2);

        YouthPolicyResponse.PolicyItem first = response.getYouthPolicies().get(0);
        assertThat(first.getBizId()).isEqualTo("R2023101933648");
        assertThat(first.getPolyBizSjnm()).isEqualTo("청년 일자리 도약 장려금");
        assertThat(first.getCnsgNmor()).isEqualTo("고용노동부");
        assertThat(first.getCtpvNm()).isEqualTo("전국");

        YouthPolicyResponse.PolicyItem second = response.getYouthPolicies().get(1);
        assertThat(second.getBizId()).isEqualTo("R2023081820227");
        assertThat(second.getCtpvNm()).isEqualTo("서울특별시");
    }

    @Test
    @DisplayName("youthPolicy 요소가 없는 빈 응답을 파싱해도 NPE가 발생하지 않는다")
    void parseEmptyResponse() throws Exception {
        String xml = "<response></response>";

        YouthPolicyResponse response = XML_MAPPER.readValue(xml, YouthPolicyResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getYouthPolicies()).isNull();
    }

    @Test
    @DisplayName("알 수 없는 XML 필드가 있어도 파싱에 실패하지 않는다")
    void parseIgnoresUnknownFields() throws Exception {
        String xml = """
                <response>
                  <unknownField>무시됨</unknownField>
                  <youthPolicy>
                    <bizId>TEST001</bizId>
                    <polyBizSjnm>테스트 정책</polyBizSjnm>
                    <futureField>미래 필드</futureField>
                  </youthPolicy>
                </response>
                """;

        YouthPolicyResponse response = XML_MAPPER.readValue(xml, YouthPolicyResponse.class);

        assertThat(response.getYouthPolicies()).hasSize(1);
        assertThat(response.getYouthPolicies().get(0).getBizId()).isEqualTo("TEST001");
    }
}
