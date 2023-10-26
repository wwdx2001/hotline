package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/5/28 14:12
 */
@Entity
public class MRMEMO {

    /**
     * Mrmemo_Code : 101
     * Mrmemo_Desc : 内地管漏水
     * MrParent_Code : 1
     * MrParent_Desc : 量高
     */

    private String Mrmemo_Code;
    private String Mrmemo_Desc;
    private String MrParent_Code;
    private String MrParent_Desc;


    @Generated(hash = 320324629)
    public MRMEMO(String Mrmemo_Code, String Mrmemo_Desc, String MrParent_Code,
            String MrParent_Desc) {
        this.Mrmemo_Code = Mrmemo_Code;
        this.Mrmemo_Desc = Mrmemo_Desc;
        this.MrParent_Code = MrParent_Code;
        this.MrParent_Desc = MrParent_Desc;
    }

    @Generated(hash = 1633545669)
    public MRMEMO() {
    }


    public String getMrmemo_Code() {
        return Mrmemo_Code;
    }

    public void setMrmemo_Code(String Mrmemo_Code) {
        this.Mrmemo_Code = Mrmemo_Code;
    }

    public String getMrmemo_Desc() {
        return Mrmemo_Desc;
    }

    public void setMrmemo_Desc(String Mrmemo_Desc) {
        this.Mrmemo_Desc = Mrmemo_Desc;
    }

    public String getMrParent_Code() {
        return MrParent_Code;
    }

    public void setMrParent_Code(String MrParent_Code) {
        this.MrParent_Code = MrParent_Code;
    }

    public String getMrParent_Desc() {
        return MrParent_Desc;
    }

    public void setMrParent_Desc(String MrParent_Desc) {
        this.MrParent_Desc = MrParent_Desc;
    }
}
