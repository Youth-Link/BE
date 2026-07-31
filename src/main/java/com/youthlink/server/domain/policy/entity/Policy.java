package com.youthlink.server.domain.policy.entity;

import com.youthlink.server.common.base.BaseTimeEntity;
import com.youthlink.server.domain.policy.dto.YouthPolicyResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Policy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String bizId;

    @Column(length = 300)
    private String polyBizSjnm;

    @Lob
    private String polyItcnCn;

    @Lob
    private String sporCn;

    @Column(length = 500)
    private String rqutPrdCn;

    @Column(length = 100)
    private String ageInfo;

    @Column(length = 100)
    private String empmSttsCd;

    @Column(length = 100)
    private String accrRqisCd;

    @Lob
    private String incmRqisCn;

    @Column(length = 300)
    private String cnsgNmor;

    // 실제 공공데이터 URL이 500자를 종종 넘어서 Lob으로 둔다.
    @Lob
    private String polyUrl;

    // 신 API는 시/도 명칭 대신 기관명을 지역 대용으로 쓰므로(PolicyConverter 참고) 길이를 넉넉히 잡는다.
    @Column(length = 300)
    private String ctpvNm;

    /**
     * 외부 API 응답으로 필드를 갱신하고, 실제로 변경된 내용이 있으면 true를 반환한다.
     * 변경 없는 정책은 Chroma 재적재 및 알림 생성 대상에서 제외된다.
     */
    public boolean update(YouthPolicyResponse.PolicyItem item) {
        String newPolyBizSjnm = item.getPlcyNm();
        String newPolyItcnCn = item.getPlcyExplnCn();
        String newSporCn = item.getPlcySprtCn();
        String newRqutPrdCn = item.toRqutPrdCn();
        String newAgeInfo = item.toAgeInfo();
        String newEmpmSttsCd = item.getJobCd();
        String newAccrRqisCd = item.getSchoolCd();
        String newIncmRqisCn = item.toIncmRqisCn();
        String newCnsgNmor = item.toCnsgNmor();
        String newPolyUrl = item.toPolyUrl();
        String newCtpvNm = item.toCtpvNm();

        boolean changed =
                !equals(this.polyBizSjnm, newPolyBizSjnm) ||
                !equals(this.polyItcnCn, newPolyItcnCn) ||
                !equals(this.sporCn, newSporCn) ||
                !equals(this.rqutPrdCn, newRqutPrdCn) ||
                !equals(this.ageInfo, newAgeInfo) ||
                !equals(this.empmSttsCd, newEmpmSttsCd) ||
                !equals(this.accrRqisCd, newAccrRqisCd) ||
                !equals(this.incmRqisCn, newIncmRqisCn) ||
                !equals(this.cnsgNmor, newCnsgNmor) ||
                !equals(this.polyUrl, newPolyUrl) ||
                !equals(this.ctpvNm, newCtpvNm);

        if (changed) {
            this.polyBizSjnm = newPolyBizSjnm;
            this.polyItcnCn = newPolyItcnCn;
            this.sporCn = newSporCn;
            this.rqutPrdCn = newRqutPrdCn;
            this.ageInfo = newAgeInfo;
            this.empmSttsCd = newEmpmSttsCd;
            this.accrRqisCd = newAccrRqisCd;
            this.incmRqisCn = newIncmRqisCn;
            this.cnsgNmor = newCnsgNmor;
            this.polyUrl = newPolyUrl;
            this.ctpvNm = newCtpvNm;
        }

        return changed;
    }

    private static boolean equals(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}
