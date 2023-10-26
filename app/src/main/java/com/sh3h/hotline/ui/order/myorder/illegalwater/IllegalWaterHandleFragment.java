package com.sh3h.hotline.ui.order.myorder.illegalwater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.data.entity.newentity.CollectionHandleEntity;
import com.sh3h.dataprovider.data.entity.newentity.IllegalHandleEntity;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.CollectionHandleEntityDao;
import com.sh3h.dataprovider.greendaoDao.IllegalHandleEntityDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IllegalWaterHandleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IllegalWaterHandleFragment extends ParentFragment implements View.OnClickListener {

    @BindView(R.id.et_xcshqk)
    public EditText mEtXcshqk;

    @BindView(R.id.spinner_xcshqk)
    public Spinner mSpinnerQKSHJG;

    @BindView(R.id.et_shbz)
    public EditText mEtShbz;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Unbinder mUnbinder;
    private DUMyTask mDuMyTask;

    public IllegalWaterHandleFragment() {
    }

    public static IllegalWaterHandleFragment newInstance(String param1, String param2) {
        IllegalWaterHandleFragment fragment = new IllegalWaterHandleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_illegal_water_handle, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView(view);
        initData(view);
        return view;
    }

    private void initView(View view) {
        String[] spinnerArrQKSHJG = new String[]{"情况属实", "情况不属实"};
        ArrayAdapter<String> qkshjgAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrQKSHJG);
        qkshjgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerQKSHJG.setAdapter(qkshjgAdapter);

        mSpinnerQKSHJG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();
//                ToastUtils.showShort(result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEtXcshqk.setOnClickListener(this);
    }

    private void initData(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {

            mDuMyTask = bundle.getParcelable(Constant.DUMyTask);

            if (mDuMyTask != null) {
                List<IllegalHandleEntity> entityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                        .getIllegalHandleEntityDao()
                        .queryBuilder()
                        .where(IllegalHandleEntityDao.Properties.Albh.eq(mDuMyTask.getCaseId()))
                        .list();
                if (entityList != null && entityList.size() > 0) {
                    IllegalHandleEntity illegalHandleEntity = entityList.get(0);

                    String[] spinnerArrQKSHJG = new String[]{"情况属实", "情况不属实"};
                    List<String> qkshjgList = Arrays.asList(spinnerArrQKSHJG);
                    mSpinnerQKSHJG.setSelection(qkshjgList.indexOf(illegalHandleEntity.getXcshqk()));

                    mEtShbz.setText(illegalHandleEntity.getShbz());
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        mUnbinder.unbind();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View v) {

    }
}