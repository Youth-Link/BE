package com.youthlink.server.policy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youthlink.server.domain.policy.dto.YouthPolicyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class YouthPolicyResponseTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    @DisplayName("온통청년 API JSON 응답을 DTO로 올바르게 파싱한다")
    void parseJsonSample() throws Exception {
        String json = """
                {
                  "resultCode": 200,
                  "resultMessage": "성공적으로 데이터를 가지고 왔습니다.",
                  "result": {
                    "pagging": { "totCount": 2, "pageNum": 1, "pageSize": 2 },
                    "youthPolicyList": [
                      {
                        "plcyNo": "R2023101933648",
                        "plcyNm": "청년 일자리 도약 장려금",
                        "plcyExplnCn": "중소기업 취업 청년에게 장려금을 지원합니다",
                        "plcySprtCn": "월 60만원, 최대 12개월 지원",
                        "aplyYmd": "20240101 ~ 20241231",
                        "sprtTrgtMinAge": "15",
                        "sprtTrgtMaxAge": "34",
                        "sprtTrgtAgeLmtYn": "Y",
                        "jobCd": "0013010",
                        "schoolCd": "0049010",
                        "earnMinAmt": "0",
                        "earnMaxAmt": "0",
                        "sprvsnInstCdNm": "고용노동부",
                        "operInstCdNm": "고용노동부",
                        "aplyUrlAddr": "https://www.work.go.kr/youngWork"
                      },
                      {
                        "plcyNo": "R2023081820227",
                        "plcyNm": "서울 청년 월세 지원",
                        "plcyExplnCn": "서울 거주 청년의 월세 부담을 줄여줍니다",
                        "plcySprtCn": "월 20만원, 최대 12개월",
                        "aplyYmd": "20240301 ~ 20240630",
                        "sprtTrgtMinAge": "19",
                        "sprtTrgtMaxAge": "39",
                        "sprtTrgtAgeLmtYn": "Y",
                        "sprvsnInstCdNm": "서울특별시",
                        "operInstCdNm": "서울특별시",
                        "aplyUrlAddr": "https://youth.seoul.go.kr"
                      }
                    ]
                  }
                }
                """;

        YouthPolicyResponse response = OBJECT_MAPPER.readValue(json, YouthPolicyResponse.class);

        assertThat(response.getResult().getYouthPolicyList()).hasSize(2);

        YouthPolicyResponse.PolicyItem first = response.getResult().getYouthPolicyList().get(0);
        assertThat(first.getPlcyNo()).isEqualTo("R2023101933648");
        assertThat(first.getPlcyNm()).isEqualTo("청년 일자리 도약 장려금");
        assertThat(first.toCnsgNmor()).isEqualTo("고용노동부");
        assertThat(first.toAgeInfo()).isEqualTo("만 15세 ~ 34세");

        YouthPolicyResponse.PolicyItem second = response.getResult().getYouthPolicyList().get(1);
        assertThat(second.getPlcyNo()).isEqualTo("R2023081820227");
        assertThat(second.toCtpvNm()).isEqualTo("서울특별시");
    }

    @Test
    @DisplayName("youthPolicyList가 없는 빈 응답을 파싱해도 NPE가 발생하지 않는다")
    void parseEmptyResponse() throws Exception {
        String json = """
                {
                  "resultCode": 200,
                  "resultMessage": "성공",
                  "result": { "pagging": { "totCount": 0, "pageNum": 1, "pageSize": 100 } }
                }
                """;

        YouthPolicyResponse response = OBJECT_MAPPER.readValue(json, YouthPolicyResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getResult().getYouthPolicyList()).isNull();
    }

    @Test
    @DisplayName("알 수 없는 JSON 필드가 있어도 파싱에 실패하지 않는다")
    void parseIgnoresUnknownFields() throws Exception {
        String json = """
                {
                  "resultCode": 200,
                  "unknownField": "무시됨",
                  "result": {
                    "youthPolicyList": [
                      { "plcyNo": "TEST001", "plcyNm": "테스트 정책", "futureField": "미래 필드" }
                    ]
                  }
                }
                """;

        YouthPolicyResponse response = OBJECT_MAPPER.readValue(json, YouthPolicyResponse.class);

        assertThat(response.getResult().getYouthPolicyList()).hasSize(1);
        assertThat(response.getResult().getYouthPolicyList().get(0).getPlcyNo()).isEqualTo("TEST001");
    }

    @Test
    @DisplayName("연령 제한이 없으면 제한없음을 반환한다")
    void ageInfoNoLimit() {
        YouthPolicyResponse.PolicyItem item = new YouthPolicyResponse.PolicyItem();
        item.setSprtTrgtAgeLmtYn("N");

        assertThat(item.toAgeInfo()).isEqualTo("제한없음");
    }

    @Test
    @DisplayName("신청기간이 비어있으면 상시를 반환한다")
    void rqutPrdCnDefaultsToSangsi() {
        YouthPolicyResponse.PolicyItem item = new YouthPolicyResponse.PolicyItem();

        assertThat(item.toRqutPrdCn()).isEqualTo("상시");
    }
}
