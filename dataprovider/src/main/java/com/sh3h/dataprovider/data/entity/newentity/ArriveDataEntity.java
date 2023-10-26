package com.sh3h.dataprovider.data.entity.newentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ArriveDataEntity {

    @Id(autoincrement = true)
    Long ID;
    private String faId;
    private String arriveDt;
    @Generated(hash = 1524589110)
    public ArriveDataEntity(Long ID, String faId, String arriveDt) {
        this.ID = ID;
        this.faId = faId;
        this.arriveDt = arriveDt;
    }
    @Generated(hash = 835573103)
    public ArriveDataEntity() {
    }
    public Long getID() {
        return this.ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public String getFaId() {
        return this.faId;
    }
    public void setFaId(String faId) {
        this.faId = faId;
    }
    public String getArriveDt() {
        return this.arriveDt;
    }
    public void setArriveDt(String arriveDt) {
        this.arriveDt = arriveDt;
    }


}
