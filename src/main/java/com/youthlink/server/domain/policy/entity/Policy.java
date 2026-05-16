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

    public void update(YouthPolicyResponse.PolicyItem item) {
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
}
