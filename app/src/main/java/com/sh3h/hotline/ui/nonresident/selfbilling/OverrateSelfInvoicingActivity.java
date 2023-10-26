package com.sh3h.hotline.ui.nonresident.selfbilling;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.response.FYNRBean;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.QuerySelectAdapter;
import com.sh3h.hotline.entity.OverrateSelfBillingEntity;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.nonresident.sign.SignatureNameActivity;
import com.sh3h.hotline.ui.order.TaskState;
import com.xujiaji.happybubble.BubbleDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xujiaji.happybubble.Auto.UP_AND_DOWN;

public class OverrateSelfInvoicingActivity extends ParentActivity  implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.spinner_fylb)
    public Spinner mSpinnerFylb;

    @BindView(R.id.spinner_fynr)
    public Spinner mSpinnerFynr;

    @BindView(R.id.et_zhanghubh)
    public EditText mZhangHuBH;

    @BindView(R.id.et_kehumc)
    EditText mKeHuMC;

    @BindView(R.id.et_youjidz)
    EditText mYouJiDZ;

    @BindView(R.id.txt_reset)
    TextView mReset;

    @BindView(R.id.txt_query)
    TextView mQuery;

    private Unbinder mUnbinder;
    private List<OverrateSelfBillingEntity> itemBeans;
    private QuerySelectAdapter mAdapter;

    private OverrateSelfBillingEntity currentEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overrate_self_invoicing);
        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.label_overrate_selfbilling);

        initViews();
        initData();
    }

    private void initViews() {
        mReset.setOnClickListener(this);
        mQuery.setOnClickListener(this);

        mSpinnerFylb.setOnItemSelectedListener(this);
        mSpinnerFynr.setOnItemSelectedListener(this);
    }


    public void initData() {
        String[] spinnerArrFYLB = new String[]{"奖励工单"};
        String[] spinnerArrFYNR = new String[]{"非居累进加价客户资料变更"};

        ArrayAdapter<String> fylbAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrFYLB);
        fylbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFylb.setAdapter(fylbAdapter);
        ArrayAdapter<String> fynrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrFYNR);
        fynrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFynr.setAdapter(fynrAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_reset:
                mZhangHuBH.setText("");
                mKeHuMC.setText("");
                mYouJiDZ.setText("");

                break;
            case R.id.txt_query:
                if ("".equals(mZhangHuBH.getText().toString().trim())
                        && "".equals(mKeHuMC.getText().toString().trim())
                        && "".equals(mYouJiDZ.getText().toString().trim())) {
                    ToastUtils.showShort("请至少输入一个查询条件！");
                } else {
                    queryData();
                }
                break;
            default:
                break;
        }
    }

    private void queryData() {
        Intent intent = new Intent(this, QuerySelectResultActivity.class);
        intent.putExtra("fyly", mSpinnerFylb.getSelectedItem().toString().trim());
        intent.putExtra("fynr", mSpinnerFynr.getSelectedItem().toString().trim());
        intent.putExtra("zhbh", mZhangHuBH.getText().toString().trim());
        intent.putExtra("khmc", mKeHuMC.getText().toString().trim());
        intent.putExtra("yjdz", mYouJiDZ.getText().toString().trim());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String result = parent.getItemAtPosition(position).toString();
//        ToastUtils.showShort(result);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}