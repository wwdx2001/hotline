package com.sh3h.dataprovider.data.entity.newentity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 自开单记录
 *
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/4 09:35
 */
public class ZikaidanJLEntity implements Parcelable {


    /**
     * faId : faId0
     * cmSta : cmSta0
     * faTypeCd : faTypeCd0
     * fynr : fynr0
     * cljb : cljb0
     * clsx : 2019-04-10 15:28:57
     * userId : null
     * entityName : entityName0
     * acctId : acctId0
     * contactValue : contactValue0
     * fsdz : fsdz0
     * disPatGrp : disPatGrp0
     * repCd : repCd0
     */

    private String faId;
    private String cmSta;
    private String faTypeCd;
    private String fynr;
    private String cljb;
    private String clsx;
    private String userId;
    private String entityName;
    private String acctId;
    private String contactValue;
    private String fsdz;
    private String disPatGrp;
    private String repCd;

    public static List<ZikaidanJLEntity> arrayZikaidanJLEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<ZikaidanJLEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ZikaidanJLEntity> arrayZikaidanJLEntityFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ZikaidanJLEntity>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getFaId() {
        return faId;
    }

    public void setFaId(String faId) {
        this.faId = faId;
    }

    public String getCmSta() {
        return cmSta;
    }

    public void setCmSta(String cmSta) {
        this.cmSta = cmSta;
    }

    public String getFaTypeCd() {
        return faTypeCd;
    }

    public void setFaTypeCd(String faTypeCd) {
        this.faTypeCd = faTypeCd;
    }

    public String getFynr() {
        return fynr;
    }

    public void setFynr(String fynr) {
        this.fynr = fynr;
    }

    public String getCljb() {
        return cljb;
    }

    public void setCljb(String cljb) {
        this.cljb = cljb;
    }

    public String getClsx() {
        return clsx;
    }

    public void setClsx(String clsx) {
        this.clsx = clsx;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public String getFsdz() {
        return fsdz;
    }

    public void setFsdz(String fsdz) {
        this.fsdz = fsdz;
    }

    public String getDisPatGrp() {
        return disPatGrp;
    }

    public void setDisPatGrp(String disPatGrp) {
        this.disPatGrp = disPatGrp;
    }

    public String getRepCd() {
        return repCd;
    }

    public void setRepCd(String repCd) {
        this.repCd = repCd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.faId);
        dest.writeString(this.cmSta);
        dest.writeString(this.faTypeCd);
        dest.writeString(this.fynr);
        dest.writeString(this.cljb);
        dest.writeString(this.clsx);
        dest.writeString(this.userId);
        dest.writeString(this.entityName);
        dest.writeString(this.acctId);
        dest.writeString(this.contactValue);
        dest.writeString(this.fsdz);
        dest.writeString(this.disPatGrp);
        dest.writeString(this.repCd);
    }

    public ZikaidanJLEntity() {
    }

    protected ZikaidanJLEntity(Parcel in) {
        this.faId = in.readString();
        this.cmSta = in.readString();
        this.faTypeCd = in.readString();
        this.fynr = in.readString();
        this.cljb = in.readString();
        this.clsx = in.readString();
        this.userId = in.readString();
        this.entityName = in.readString();
        this.acctId = in.readString();
        this.contactValue = in.readString();
        this.fsdz = in.readString();
        this.disPatGrp = in.readString();
        this.repCd = in.readString();
    }

    public static final Parcelable.Creator<ZikaidanJLEntity> CREATOR = new Parcelable.Creator<ZikaidanJLEntity>() {
        @Override
        public ZikaidanJLEntity createFromParcel(Parcel source) {
            return new ZikaidanJLEntity(source);
        }

        @Override
        public ZikaidanJLEntity[] newArray(int size) {
            return new ZikaidanJLEntity[size];
        }
    };
}
