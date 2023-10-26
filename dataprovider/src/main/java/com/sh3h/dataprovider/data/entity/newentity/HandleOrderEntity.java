package com.sh3h.dataprovider.data.entity.newentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/11 09:44
 */
@Entity
public class HandleOrderEntity {
    @Id(autoincrement = true)
    Long ID;
    private String faId;
    private String caseId;
    private String oldCaseId;
    private String cmSta;
    private String arriveDt;
    private String faTypeCd;
    private String clnr;
    private String faReason;
    private String faAct;
    private String comment;
    private String finishDt;
    private String repCd;
    private String cljg;
    private String regRead;
    private String cbzt;


    @Generated(hash = 902185343)
    public HandleOrderEntity(Long ID, String faId, String caseId, String oldCaseId,
            String cmSta, String arriveDt, String faTypeCd, String clnr,
            String faReason, String faAct, String comment, String finishDt,
            String repCd, String cljg, String regRead, String cbzt) {
        this.ID = ID;
        this.faId = faId;
        this.caseId = caseId;
        this.oldCaseId = oldCaseId;
        this.cmSta = cmSta;
        this.arriveDt = arriveDt;
        this.faTypeCd = faTypeCd;
        this.clnr = clnr;
        this.faReason = faReason;
        this.faAct = faAct;
        this.comment = comment;
        this.finishDt = finishDt;
        this.repCd = repCd;
        this.cljg = cljg;
        this.regRead = regRead;
        this.cbzt = cbzt;
    }


    @Generated(hash = 1308514606)
    public HandleOrderEntity() {
    }


    public String getCljg() {
        return cljg;
    }

    public void setCljg(String cljg) {
        this.cljg = cljg;
    }

    public String getRegRead() {
        return regRead;
    }

    public void setRegRead(String regRead) {
        this.regRead = regRead;
    }

    public String getCbzt() {
        return cbzt;
    }

    public void setCbzt(String cbzt) {
        this.cbzt = cbzt;
    }

    public String getFaId() {
        return this.faId;
    }

    public void setFaId(String faId) {
        this.faId = faId;
    }

    public String getCaseId() {
        return this.caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getOldCaseId() {
        return this.oldCaseId;
    }

    public void setOldCaseId(String oldCaseId) {
        this.oldCaseId = oldCaseId;
    }

    public String getCmSta() {
        return this.cmSta;
    }

    public void setCmSta(String cmSta) {
        this.cmSta = cmSta;
    }

    public String getArriveDt() {
        return this.arriveDt;
    }

    public void setArriveDt(String arriveDt) {
        this.arriveDt = arriveDt;
    }

    public String getFaTypeCd() {
        return this.faTypeCd;
    }

    public void setFaTypeCd(String faTypeCd) {
        this.faTypeCd = faTypeCd;
    }

    public String getClnr() {
        return this.clnr;
    }

    public void setClnr(String clnr) {
        this.clnr = clnr;
    }

    public String getFaReason() {
        return this.faReason;
    }

    public void setFaReason(String faReason) {
        this.faReason = faReason;
    }

    public String getFaAct() {
        return this.faAct;
    }

    public void setFaAct(String faAct) {
        this.faAct = faAct;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFinishDt() {
        return this.finishDt;
    }

    public void setFinishDt(String finishDt) {
        this.finishDt = finishDt;
    }

    public String getRepCd() {
        return this.repCd;
    }

    public void setRepCd(String repCd) {
        this.repCd = repCd;
    }


    public Long getID() {
        return this.ID;
    }


    public void setID(Long ID) {
        this.ID = ID;
    }


}
