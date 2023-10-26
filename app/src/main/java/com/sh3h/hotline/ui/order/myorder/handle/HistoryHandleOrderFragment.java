package com.sh3h.hotline.ui.order.myorder.handle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 显示已经上传的历史工单的处理信息和工单查询的处理信息
 * Created by zhangjing on 2016/9/19.
 */
public class HistoryHandleOrderFragment extends ParentFragment {

    private Unbinder mUnbinder;

    @BindView(R.id.iv_data_is_upload)
    ImageView dataUpload;

    @BindView(R.id.tv_handle_type)
    TextView mHandleType;

    @BindView(R.id.tv_handle_content)
    TextView mHandleContent;

    @BindView(R.id.tv_happened_reason)
    TextView mIssueReason;

    @BindView(R.id.tv_handle_measure)
    TextView mSolveMeasure;

    @BindView(R.id.tv_handle_result)
    TextView mSolveResult;

    @BindView(R.id.tv_shoulibeizhu)
    TextView mResolveComment;

    @BindView(R.id.tv_handle_time)
    TextView mHandleTime;

    @BindView(R.id.checkbox_finish_task)
    CheckBox mCheckFinishTask;

    @BindView(R.id.layout_is_finish_task)
    LinearLayout mLayoutFinishTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_handle_order, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            throw new NullPointerException("bundle is null");
        }
        String taskReply = bundle.getString(Constant.TASK_REPLY);
        DUProcess duProcess = bundle.getParcelable(Constant.INTENT_PARAM_ORDER_PROCESS);
        if (TextUtil.isNullOrEmpty(taskReply) && duProcess == null) {
            throw new NullPointerException("taskReply && orderProcess is null");
        }
        if (!TextUtil.isNullOrEmpty(taskReply)) {
            try {
                JSONObject jsonObject = new JSONObject(taskReply);
                mHandleType.setText(jsonObject.getString(Constant.RESOLVE_TYPE));
                mHandleContent.setText(jsonObject.getString(Constant.RESOLVE_CONTENT));
                mIssueReason.setText(jsonObject.getString(Constant.ISSUE_REASON));
                mSolveMeasure.setText(jsonObject.getString(Constant.RESOLVE_METHOD));
                mSolveResult.setText(jsonObject.getString(Constant.RESOLVE_RESULT));
                mResolveComment.setText(jsonObject.getString(Constant.RESOLVE_COMMENT));
                mHandleTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(jsonObject.getLong(Constant.RESOLVE_TIME)));
                mCheckFinishTask.setClickable(false);
                mCheckFinishTask.setChecked(jsonObject.getBoolean(Constant.IS_FINISH_TASK));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (duProcess != null) {
            mHandleType.setText(duProcess.getResolveType());
            mHandleContent.setText(duProcess.getResolveContent());
            mIssueReason.setText(duProcess.getIssueReason());
            mSolveMeasure.setText(duProcess.getResolveMethod());
            mSolveResult.setText(duProcess.getResolveResult());
            mResolveComment.setText(duProcess.getResolveComment());
            mHandleTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(duProcess.getTaskTime()));
            mCheckFinishTask.setClickable(false);
            mCheckFinishTask.setChecked(duProcess.isFinishTask());
        }
        if (mHandleType.getText().toString().equals("表务问题")) {
            mLayoutFinishTask.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void lazyLoad() {

    }
}
