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

    @Column(length = 200)
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

    @Column(length = 200)
    private String cnsgNmor;

    @Column(length = 500)
    private String polyUrl;

    @Column(length = 50)
    private String ctpvNm;

    /**
     * 외부 API 응답으로 필드를 갱신하고, 실제로 변경된 내용이 있으면 true를 반환한다.
     * 변경 없는 정책은 Chroma 재적재 및 알림 생성 대상에서 제외된다.
     */
    public boolean update(YouthPolicyResponse.PolicyItem item) {
        boolean changed =
                !equals(this.polyBizSjnm, item.getPolyBizSjnm()) ||
                !equals(this.polyItcnCn, item.getPolyItcnCn()) ||
                !equals(this.sporCn, item.getSporCn()) ||
                !equals(this.rqutPrdCn, item.getRqutPrdCn()) ||
                !equals(this.ageInfo, item.getAgeInfo()) ||
                !equals(this.empmSttsCd, item.getEmpmSttsCd()) ||
                !equals(this.accrRqisCd, item.getAccrRqisCd()) ||
                !equals(this.incmRqisCn, item.getIncmRqisCn()) ||
                !equals(this.cnsgNmor, item.getCnsgNmor()) ||
                !equals(this.polyUrl, item.getPolyUrl()) ||
                !equals(this.ctpvNm, item.getCtpvNm());

        if (changed) {
            this.polyBizSjnm = item.getPolyBizSjnm();
            this.polyItcnCn = item.getPolyItcnCn();
            this.sporCn = item.getSporCn();
            this.rqutPrdCn = item.getRqutPrdCn();
            this.ageInfo = item.getAgeInfo();
            this.empmSttsCd = item.getEmpmSttsCd();
            this.accrRqisCd = item.getAccrRqisCd();
            this.incmRqisCn = item.getIncmRqisCn();
            this.cnsgNmor = item.getCnsgNmor();
            this.polyUrl = item.getPolyUrl();
            this.ctpvNm = item.getCtpvNm();
        }

        return changed;
    }

    private static boolean equals(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}
