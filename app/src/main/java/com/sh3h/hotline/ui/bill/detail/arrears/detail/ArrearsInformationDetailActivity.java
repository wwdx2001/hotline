package com.sh3h.hotline.ui.bill.detail.arrears.detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.sh3h.dataprovider.data.entity.response.DURecentBillDetail;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/26.
 */

public class ArrearsInformationDetailActivity extends ParentActivity implements ArrearsInformationDetailMvpView {

    @Inject
    ArrearsInformationDetailPresenter mPresenter;

    @BindView(R.id.tv_zilaishuigongsi)
    TextView tvZilaishuigongsi;

    @BindView(R.id.tv_zhandian)
    TextView tvZhandian;

    @BindView(R.id.tv_kaizhangbianhao)
    TextView tvKaizhangbianhao;

    @BindView(R.id.tv_cehao)
    TextView tvCehao;

    @BindView(R.id.tv_yonghuming)
    TextView tvYonghuming;

    @BindView(R.id.tv_dizhi)
    TextView tvDizhi;

    @BindView(R.id.tv_yonghuleibie)
    TextView tvYonghuleibie;

    @BindView(R.id.tv_zhanghao)
    TextView tvZhanghao;

    @BindView(R.id.tv_zhangdanleixing)
    TextView tvZhangdanleixing;

    @BindView(R.id.tv_kaihuyinhang)
    TextView tvKaihuyinhang;

    @BindView(R.id.tv_huhao)
    TextView tvHuhao;

    @BindView(R.id.tv_yongshuijia)
    TextView tvYongshuijia;

    @BindView(R.id.tv_paishuijia)
    TextView tvPaishuijia;

    @BindView(R.id.tv_zhekoulv)
    TextView tvZhekoulv;

    @BindView(R.id.tv_paishuilv)
    TextView tvPaishuilv;

    @BindView(R.id.tv_tiaojiahao)
    TextView tvTiaojiahao;

    @BindView(R.id.tv_kuaihao)
    TextView tvKuaihao;

    @BindView(R.id.tv_chaoci)
    TextView tvChaoci;

    @BindView(R.id.tv_cidaima)
    TextView tvCidaima;

    @BindView(R.id.tv_chaobiaoriqi)
    TextView tvChaobiaoriqi;

    @BindView(R.id.tv_chaobiaozhuangtai)
    TextView tvChaobiaozhuangtai;

    @BindView(R.id.tv_chaobiaoyuan)
    TextView tvChaobiaoyuan;

    @BindView(R.id.tv_shangcichaoma)
    TextView tvShangcichaoma;

    @BindView(R.id.tv_shangcishuiliang)
    TextView tvShangcishuiliang;

    @BindView(R.id.tv_shangciyue)
    TextView tvShangciyue;

    @BindView(R.id.tv_bencichaoma)
    TextView tvBencichaoma;

    @BindView(R.id.tv_yongshuilinag)
    TextView tvYongshuilinag;

    @BindView(R.id.tv_yongshuifei)
    TextView tvYongshuifei;

    @BindView(R.id.tv_budianshu)
    TextView tvBudianshu;

    @BindView(R.id.tv_paishuifei)
    TextView tvPaishuifei;

    @BindView(R.id.tv_yingshoujine)
    TextView tvYingshoujine;

    @BindView(R.id.tv_zhinajin)
    TextView tvZhinajin;

    @BindView(R.id.tv_shouxufei)
    TextView tvShouxufei;

    @BindView(R.id.tv_benciyue)
    TextView tvBenciyue;

    @BindView(R.id.tv_shishoujine)
    TextView tvShishoujine;

    @BindView(R.id.tv_chaobiaobiaozhi)
    TextView tvChaobiaobiaozhi;

    @BindView(R.id.tv_dayinshu)
    TextView tvDayinshu;

    @BindView(R.id.tv_xiaozhangbiaozhi)
    TextView tvXiaozhangbiaozhi;

    @BindView(R.id.tv_xiaozhangbianma)
    TextView tvXiaozhangbianma;

    @BindView(R.id.tv_xiaozhangriqi)
    TextView tvXiaozhangriqi;

    @BindView(R.id.tv_xiaozhangyuan)
    TextView tvXiaozhangyuan;

    @BindView(R.id.tv_zhuanzhangtuoshou)
    TextView tvZhuanzhangtuoshou;

    @BindView(R.id.tv_diyijietihunianyongshuiliang)
    TextView tvDiyijietihunianyongshuiliang;

    @BindView(R.id.tv_diyijietishuiliangqujian)
    TextView tvDiyijietishuiliangqujian;

    @BindView(R.id.tv_dierjietihunianyongshuiliang)
    TextView tvDierjietihunianyongshuiliang;

    @BindView(R.id.tv_dierjietishuiliangqujian)
    TextView tvDierjietishuiliangqujian;

    @BindView(R.id.tv_disanjietihunianyongshuiliang)
    TextView tvDisanjietihunianyongshuiliang;

    @BindView(R.id.tv_disanjietishuiliangqujian)
    TextView tvDisanjietishuiliangqujian;

    @BindView(R.id.tv_nianleijiliang)
    TextView tvNianleijiliang;

    @BindView(R.id.tv_jineiyongshuiliang)
    TextView tvJineiyongshuiliang;

    @BindView(R.id.tv_jiwaipaishuifei)
    TextView tvJiwaipaishuifei;

    @BindView(R.id.tv_jineiyongshuifei)
    TextView tvJineiyongshuifei;

    @BindView(R.id.tv_jiwaiyongshuifei)
    TextView tvJiwaiyongshuifei;

    @BindView(R.id.tv_paishuijishu)
    TextView tvPaishuijishu;

    @BindView(R.id.tv_yongshuijishu)
    TextView tvYongshuijishu;

    @BindView(R.id.tv_xiaofangfei)
    TextView tvXiaofangfei;

    @BindView(R.id.tv_shoufeifangshi)
    TextView tvShoufeifangshi;

    @BindView(R.id.tv_jihuayongshui)
    TextView tvJihuayongshui;

    @BindView(R.id.tv_koujing)
    TextView tvKoujing;

    @BindView(R.id.tv_tonghao)
    TextView tvTonghao;

    @BindView(R.id.tv_liangcheng)
    TextView tvLiangcheng;

    @BindView(R.id.tv_biaofenlei)
    TextView tvBiaofenlei;

    @BindView(R.id.tv_biaohao)
    TextView tvBiaohao;

    @BindView(R.id.tv_shangbaobiaozhi)
    TextView tvShangbaobiaozhi;

    @BindView(R.id.tv_xiasongbiaozhi)
    TextView tvXiasongbiaozhi;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_bill_information_detail);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.attachView(this);
        initToolBar(R.string.activity_arrears_information_detail);
        Intent intent = getIntent();
        showProgress(getString(R.string.dialog_loading));
        mPresenter.initData(intent.getStringExtra(Constant.CARDID), intent.getIntExtra(Constant.FEEID, -1));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mUnbinder.unbind();
    }

    @Override
    public void initData(DURecentBillDetail data) {
        hideProgress();
        tvZilaishuigongsi.setText(data.getWaterCompany());
        tvZhandian.setText(data.getStation());
        tvKaizhangbianhao.setText(data.getAccountNo());
        tvCehao.setText(data.getBookId());
        tvYonghuming.setText(data.getName());
        tvDizhi.setText(data.getAddress());
        tvYonghuleibie.setText(data.getType());
        tvZhanghao.setText(data.getAccount());
        tvZhangdanleixing.setText(data.getAccountType());
        tvKaihuyinhang.setText(data.getBank());
        tvHuhao.setText(data.getNumber());
        tvYongshuijia.setText(data.getWaterPrice());
        tvPaishuijia.setText(data.getDrainingPrice());
        tvZhekoulv.setText(data.getDiscountRate());
        tvPaishuilv.setText(data.getDrainingRate());
        tvTiaojiahao.setText(data.getAdjustingPriceCode());
        tvKuaihao.setText(data.getPartCode());
        tvChaoci.setText(data.getReadTimes());
        tvCidaima.setText(data.getReadCode());
        tvChaobiaoriqi.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(data.getReadTime())));
        tvChaobiaozhuangtai.setText(data.getReadState());
        tvChaobiaoyuan.setText(data.getReader());
        tvShangcichaoma.setText(data.getLastRead());
        tvShangcishuiliang.setText(data.getLastWater());
        tvShangciyue.setText(data.getLastBalance());
        tvBencichaoma.setText(data.getCurrentRead());
        tvYongshuilinag.setText(data.getReadWater());
        tvYongshuifei.setText(data.getPi1Money());
        tvBudianshu.setText(data.getSuppliedCount());
        tvPaishuifei.setText(data.getPi2Money());
        tvYingshoujine.setText(data.getReceivedMoney());
        tvZhinajin.setText(data.getLateFee());
        tvShouxufei.setText(data.getPoundage());
        tvBenciyue.setText(data.getCurrentBalance());
        tvShishoujine.setText(data.getRealMoney());
        tvChaobiaobiaozhi.setText(data.getReadFlag());
        tvDayinshu.setText(data.getPrintCount());
        tvXiaozhangbiaozhi.setText(data.getCancelAccountFlag());
        tvXiaozhangbianma.setText(data.getCancelAccountNo());
        tvXiaozhangriqi.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(data.getCancelAccountTime())));
        tvXiaozhangyuan.setText(data.getCancelAccountPerson());
        tvZhuanzhangtuoshou.setText(data.getTransferAccount());
        tvDiyijietihunianyongshuiliang.setText(data.getFirstStepReadWater());
        tvDiyijietishuiliangqujian.setText(data.getFirstStepWaterRange());
        tvDierjietihunianyongshuiliang.setText(data.getSecondStepReadWater());
        tvDierjietishuiliangqujian.setText(data.getSecondStepWaterRange());
        tvDisanjietihunianyongshuiliang.setText(data.getThirdStepReadWater());
        tvDisanjietishuiliangqujian.setText(data.getThirdStepWaterRange());
        tvNianleijiliang.setText(data.getAccumulativeCount());
        tvJineiyongshuiliang.setText(data.getInternalReadWater());
        tvJineiyongshuifei.setText(data.getInternalPi1Money());
        tvJiwaipaishuifei.setText(data.getExternalPi2Money());
        tvJiwaiyongshuifei.setText(data.getExternalPi1Money());
        tvPaishuijishu.setText(data.getDrainageBase());
        tvYongshuijishu.setText(data.getReadBase());
        tvXiaofangfei.setText(data.getFireCost());
        tvShoufeifangshi.setText(data.getChargeWay());
        tvJihuayongshui.setText(data.getPlanWater());
        tvKoujing.setText(data.getCaliber());
        tvLiangcheng.setText(data.getRange());
        tvTonghao.setText(data.getPriceCode());
        tvBiaofenlei.setText(data.getWaterClass());
        tvBiaohao.setText(data.getCardBarCode());
        tvShangbaobiaozhi.setText(data.getUploadFlag());
        tvXiasongbiaozhi.setText(data.getDownloadFlag());
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
