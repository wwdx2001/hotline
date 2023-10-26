package com.sh3h.hotline.ui.bill.detail.recentbill;

import com.blankj.utilcode.util.TimeUtils;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.hotline.entity.RecentBillDetailInfoEntity;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public class RecentBillInformationPresenter extends ParentPresenter<RecentBillInformationMvpView> {

    public static final String TAG = "RecentBillInformationPresenter";
    private Disposable mDisposable1;

    @Inject
    public RecentBillInformationPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
    }

    public void getBillList(String acctId) {
//        List<RecentBillDetailInfoEntity.BillListBean> mlist = new ArrayList();
//        RecentBillDetailInfoEntity.BillListBean billListBean = new RecentBillDetailInfoEntity.BillListBean();
//        billListBean.setBillDate("20181202");
//        billListBean.setFtType("AD");
//        billListBean.setFtTypeDesc("啦啦啦");
//        billListBean.setSiBillid("141231234234");
//        billListBean.setSaTypeDesc("水费");
//        billListBean.setCmAmt(40.99);
//        billListBean.setPayFlg("0");
//        mlist.add(billListBean);
//        getMvpView().onBillList(mlist);

        mDisposable1 = EasyHttp
                .post(URL.BillDetailsQuery)
                .params("acctId", acctId)
                .params("billYM", TimeUtils.date2String(new Date(), new SimpleDateFormat("yyyyMM")))
                .execute(new CallBackProxy<CustomApiResult<RecentBillDetailInfoEntity>, RecentBillDetailInfoEntity>(
                        new CustomCallBack<RecentBillDetailInfoEntity>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                if (getMvpView() != null) {
                                    getMvpView().showProgressDialog("加载中");
                                }
                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e, false);
                                if (getMvpView() != null) {
                                    getMvpView().onError(e);
                                    getMvpView().hideProgressDialog();
                                }
                            }

                            @Override
                            public void onSuccess(RecentBillDetailInfoEntity billDetailInfoEntity) {
                                if (getMvpView() != null) {
                                    getMvpView().hideProgressDialog();
                                    if (billDetailInfoEntity != null) {
                                        List<RecentBillDetailInfoEntity.BillListBean> billList = billDetailInfoEntity.getBillList();
                                        getMvpView().onBillList(billList);
                                    } else {
                                        getMvpView().onCompleted("请求失败");
                                    }
                                }
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                if (getMvpView() != null) {
                                    getMvpView().hideProgressDialog();
                                    getMvpView().onCompleted("");
                                }
                            }
                        }) {
                });

    }
}
