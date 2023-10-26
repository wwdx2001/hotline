package com.sh3h.hotline.injection.component;


import com.sh3h.hotline.injection.annotation.PerActivity;
import com.sh3h.hotline.injection.module.ActivityModule;
import com.sh3h.hotline.ui.bill.QueryBillActivity;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.bill.detail.jinqicm.JinQiCMFragment;
import com.sh3h.hotline.ui.bill.detail.servicepoint.details.JinQiCMActivity;
import com.sh3h.hotline.ui.bill.detail.zuijingd.ZuiJinGDFragment;
import com.sh3h.hotline.ui.bill.detail.arrears.ArrearsInformationFragment;
import com.sh3h.hotline.ui.bill.detail.arrears.detail.ArrearsInformationDetailActivity;
import com.sh3h.hotline.ui.bill.detail.basic.BasicInformationFragment;
import com.sh3h.hotline.ui.bill.detail.recentbill.RecentBillInformationFragment;
import com.sh3h.hotline.ui.bill.detail.recentbill.detail.RecentBillInformationDetailActivity;
import com.sh3h.hotline.ui.bill.result.QueryBillResultActivity;
import com.sh3h.hotline.ui.collection.CollectionBackfillingFragment;
import com.sh3h.hotline.ui.collection.CollectionTaskActivity;
import com.sh3h.hotline.ui.collection.CollectionTaskListActivity;
import com.sh3h.hotline.ui.collection.CollectionTaskOrderBackfillingActivity;
import com.sh3h.hotline.ui.knowledge.KnowledgeBaseActivity;
import com.sh3h.hotline.ui.main.LoginActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaNewFragment;
import com.sh3h.hotline.ui.nonresident.call.CallHandleFragment;
import com.sh3h.hotline.ui.nonresident.call.OverrateCallHandleActivity;
import com.sh3h.hotline.ui.nonresident.call.OverrateCallNewActivity;
import com.sh3h.hotline.ui.nonresident.media.MultimediaFileFragment;
import com.sh3h.hotline.ui.nonresident.receipt.OverrateReceiptHandleActivity;
import com.sh3h.hotline.ui.nonresident.receipt.OverrateReceiptNewActivity;
import com.sh3h.hotline.ui.nonresident.receipt.ReceiptHandleFragment;
import com.sh3h.hotline.ui.nonresident.selfbilling.EditSubmitActivity;
import com.sh3h.hotline.ui.nonresident.selfbilling.OverrateSelfBillingActivity;
import com.sh3h.hotline.ui.nonresident.selfbilling.QuerySelectResultActivity;
import com.sh3h.hotline.ui.nonresident.sign.SignatureNameActivity;
import com.sh3h.hotline.ui.nonresident.sign.SignatureNameNewActivity;
import com.sh3h.hotline.ui.order.myorder.handle.HistoryHandleOrderFragment;
import com.sh3h.hotline.ui.order.myorder.delayorback.DelayOrBackOrderActivity;
import com.sh3h.hotline.ui.order.myorder.detail.OrderDetailsFragment;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderFragment;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.hotline.ui.main.MainActivity;
import com.sh3h.hotline.ui.multimedia.PictureDetailsActivity;
import com.sh3h.hotline.ui.notice.NoWaterSupplyNoticeActivity;
import com.sh3h.hotline.ui.notice.WaterNoticeDetailsActivity;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersBackUpActivity;
import com.sh3h.hotline.ui.order.myorder.history.HistorySaveOrdersFragment;
import com.sh3h.hotline.ui.order.myorder.history.HistoryUploadOrdersFragment;
import com.sh3h.hotline.ui.order.myorder.illegalwater.IllegalWaterActivity;
import com.sh3h.hotline.ui.order.myorder.picture.PictureFileActivity;
import com.sh3h.hotline.ui.order.myorder.questionwater.QuestionWaterActivity;
import com.sh3h.hotline.ui.order.myorder.remotewater.RemoteWaterActivity;
import com.sh3h.hotline.ui.order.myorder.remotewater.RemoteWaterDetailFragment;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderActivity;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderNewActivity;
import com.sh3h.hotline.ui.order.self.create.receipt.ReceiptCallQueryResultActivity;
import com.sh3h.hotline.ui.order.self.create.receipt.ReceiptFragment;
import com.sh3h.hotline.ui.order.self.create.receipt.ReceiptNewFragment;
import com.sh3h.hotline.ui.order.self.detail.CreateSelfOrderDetailActivity;
import com.sh3h.hotline.ui.order.self.detail.receipt.ReceiptDetailFragment;
import com.sh3h.hotline.ui.order.self.history.CreateSelfOrderHistoryActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaFragment;
import com.sh3h.hotline.ui.order.myorder.list.MyOrderListActivity;
import com.sh3h.hotline.ui.process.OrderProcessFragment;
import com.sh3h.hotline.ui.query.ChooseSiteActivity;
import com.sh3h.hotline.ui.query.QueryOrderActivity;
import com.sh3h.hotline.ui.query.QueryOrderResultActivity;
import com.sh3h.hotline.ui.setting.SettingActivity;
import com.sh3h.hotline.ui.web.WebActivity;

import dagger.Component;

/**
 * This component inject dependencies to all Activities across the application
 * 生命周期跟Activity一样
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(ReceiptCallQueryResultActivity receiptCallQueryResultActivity);

    void inject(PictureFileActivity pictureFileActivity);

    void inject(QuerySelectResultActivity querySelectResultActivity);

    void inject(OverrateCallNewActivity overrateCallNewActivity);

    void inject(OverrateReceiptNewActivity overrateReceiptNewActivity);

    void inject(CreateSelfOrderActivity createSelfOrderActivity);

    void inject(CreateSelfOrderNewActivity createSelfOrderNewActivity);

    void inject(CreateSelfOrderHistoryActivity createSelfOrderHistoryActivity);

    void inject(QueryBillActivity queryBillActivity);

    void inject(KnowledgeBaseActivity knowledgeBaseActivity);

    void inject(MyOrderListActivity myOrderListActivity);

    void inject(DelayOrBackOrderActivity delayOrderActivity);

    void inject(HandleOrderActivity handleOrderActivity);

    void inject(RemoteWaterActivity remoteWaterActivity);

    void inject(IllegalWaterActivity illegalWaterActivity);

    void inject(QuestionWaterActivity questionWaterActivity);

    void inject(HandleOrderFragment handleOrderFragment);

    void inject(HistoryHandleOrderFragment historyHandleOrderFragment);

    void inject(HistorySaveOrdersFragment historySaveOrdersFragment);

    void inject(HistoryUploadOrdersFragment historyUploadOrdersFragment);

    void inject(MultimediaFragment multimediaFragment);

    void inject(MultimediaNewFragment multimediaNewFragment);

    void inject(OrderDetailsFragment orderDetailsFragment);

    void inject(HistoryOrdersActivity historyOrdersActivity);

    void inject(HistoryOrdersBackUpActivity historyOrdersBackUpActivity);

    void inject(ReceiptFragment receiptFragment);

    void inject(NoWaterSupplyNoticeActivity noWaterSupplyNoticeActivity);

    void inject(WaterNoticeDetailsActivity waterNoticeDetailsActivity);

    void inject(QueryOrderActivity queryOrderActivity);

    void inject(QueryOrderResultActivity queryOrderResultActivity);

    void inject(CreateSelfOrderDetailActivity createSelfOrderDetailActivity);

    void inject(ReceiptDetailFragment receiptDetailFragment);

    void inject(UserDetailInformationActivity userDetailInformationActivity);

    void inject(QueryBillResultActivity queryBillResultActivity);

    void inject(RecentBillInformationFragment recentBillInformationFragment);

    void inject(ZuiJinGDFragment zuiJinGDFragment);

    void inject(JinQiCMFragment jinQiCMFragment);

    void inject(BasicInformationFragment basicInformationFragment);

    void inject(ArrearsInformationFragment arrearsInformationFragment);

    void inject(PictureDetailsActivity pictureDetailsActivity);

    void inject(RecentBillInformationDetailActivity recentBillInformationDetailActivity);

    void inject(ArrearsInformationDetailActivity arrearsInformationDetailActivity);

    void inject(SettingActivity settingActivity);

    void inject(OrderProcessFragment orderProcessFragment);

    void inject(WebActivity webActivity);

    void inject(ChooseSiteActivity chooseSiteActivity);

    void inject(JinQiCMActivity jinQiCMActivity);

    void inject(SignatureNameActivity signatureNameActivity);

    void inject(MultimediaFileFragment multimediaFileFragment);

    void inject(OverrateSelfBillingActivity overrateSelfBillingActivity);

    void inject(OverrateReceiptHandleActivity overrateReceiptHandleActivity);

    void inject(OverrateCallHandleActivity overrateCallHandleActivity);

    void inject(ReceiptHandleFragment receiptHandleFragment);

    void inject(CallHandleFragment callHandleFragment);

    void inject(EditSubmitActivity editSubmitActivity);

    void inject(CollectionTaskActivity collectionTaskActivity);

    void inject(CollectionTaskListActivity collectionTaskListActivity);

    void inject(CollectionTaskOrderBackfillingActivity collectionTaskOrderBackfillingActivity);

    void inject(SignatureNameNewActivity signatureNameNewActivity);

    void inject(ReceiptNewFragment receiptNewFragment);

  void inject(CollectionBackfillingFragment collectionBackfillingFragment);

  void inject(RemoteWaterDetailFragment remoteWaterDetailFragment);

}
