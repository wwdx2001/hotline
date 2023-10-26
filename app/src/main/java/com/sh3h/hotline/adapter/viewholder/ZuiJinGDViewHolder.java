package com.sh3h.hotline.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sh3h.hotline.R;

/**
 * acctId : 0270707539
 * spId : 9270707539
 * faId : 9091201291
 * faTypeDesc : 定换
 * faCompDttm : 2019-03-01 00:00:00
 * uninstallBadgeNum : 14715100001170
 * uninstallRead : 201
 * newBadgeNum : 14715100001171
 * installRead : 0
 */

public class ZuiJinGDViewHolder extends RecyclerView.ViewHolder {
    public TextView spId,faId,faTypeDesc,faCompDttm,uninstallBadgeNum,uninstallRead,newBadgeNum,installRead;


    public ZuiJinGDViewHolder(View view) {
        super(view);
        spId = (TextView)view.findViewById(R.id.tv_spid);
        faId = (TextView)view.findViewById(R.id.tv_faId);
        faTypeDesc = (TextView)view.findViewById(R.id.tv_faTypeDesc);
        faCompDttm= (TextView)view.findViewById(R.id.tv_faCompDttm);
        uninstallBadgeNum = (TextView)view.findViewById(R.id.tv_uninstallBadgeNum);
        uninstallRead = (TextView)view.findViewById(R.id.tv_uninstallRead);
        newBadgeNum = (TextView)view.findViewById(R.id.tv_newBadgeNum);
        installRead = (TextView)view.findViewById(R.id.tv_installRead);

    }
}
