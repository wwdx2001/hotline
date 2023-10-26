package com.sh3h.hotline.ui.order.myorder.illegalwater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IllegalWaterHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IllegalWaterHistoryFragment extends ParentFragment implements View.OnClickListener {

    @BindView(R.id.et_xcshqk)
    TextView mEtXcshqk;

    @BindView(R.id.et_shbz)
    TextView mEtShbz;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Unbinder mUnbinder;

    public IllegalWaterHistoryFragment() {
    }

    public static IllegalWaterHistoryFragment newInstance(String param1, String param2) {
        IllegalWaterHistoryFragment fragment = new IllegalWaterHistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_illegal_water_history, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
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