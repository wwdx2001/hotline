package com.sh3h.localprovider.greendaoEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangjing on 2016/9/29.
 */

@Entity(nameInDb = "HL_WORD")
public class Word {

    @Id(autoincrement = true)
    @Property(nameInDb = "ID")
    private Long ID;//词语编号 主键 自增长

    private String PARENT_ID;//父级编号

    private String GROUP;//词语组

    @NotNull
    private String NAME;//词语文本

    @NotNull
    private String VALUE;//词语值

    private String VALUE_EX;//词语值扩展字段

    private String REMARK;

    private String EXTEND;//扩展信息 不做为表字段

    @Generated(hash = 1427124815)
    public Word(Long ID, String PARENT_ID, String GROUP, @NotNull String NAME,
            @NotNull String VALUE, String VALUE_EX, String REMARK, String EXTEND) {
        this.ID = ID;
        this.PARENT_ID = PARENT_ID;
        this.GROUP = GROUP;
        this.NAME = NAME;
        this.VALUE = VALUE;
        this.VALUE_EX = VALUE_EX;
        this.REMARK = REMARK;
        this.EXTEND = EXTEND;
    }

    @Generated(hash = 3342184)
    public Word() {
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getPARENT_ID() {
        return this.PARENT_ID;
    }

    public void setPARENT_ID(String PARENT_ID) {
        this.PARENT_ID = PARENT_ID;
    }

    public String getGROUP() {
        return this.GROUP;
    }

    public void setGROUP(String GROUP) {
        this.GROUP = GROUP;
    }

    public String getNAME() {
        return this.NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getVALUE() {
        return this.VALUE;
    }

    public void setVALUE(String VALUE) {
        this.VALUE = VALUE;
    }

    public String getVALUE_EX() {
        return this.VALUE_EX;
    }

    public void setVALUE_EX(String VALUE_EX) {
        this.VALUE_EX = VALUE_EX;
    }

    public String getREMARK() {
        return this.REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getEXTEND() {
        return this.EXTEND;
    }

    public void setEXTEND(String EXTEND) {
        this.EXTEND = EXTEND;
    }
}
