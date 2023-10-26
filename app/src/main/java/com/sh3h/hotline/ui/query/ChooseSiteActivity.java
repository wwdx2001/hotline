package com.sh3h.hotline.ui.query;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by BJB147 on 2018/4/16.
 */
public class ChooseSiteActivity extends ParentActivity implements ChooseSiteMvpView {

    private Unbinder mUnbinder;

    @Inject
    ChooseSitePresenter mChooseSitePresenter;

    @BindView(R.id.expand_list)
    ExpandableListView mExpandListView;

    private ChooseSiteAdapter mChooseSiteAdapter;

    List<DUWord> mSiteWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_site);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        initToolBar(R.string.label_choose_site);
        mChooseSitePresenter.attachView(this);
        showProgress(getResources().getString(R.string.dialog_loading));
        mChooseSitePresenter.getAllStations();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {//保存信息
            DUWord result = null;
            for (DUWord groupStation : mSiteWords) {
                if (groupStation.isChecked()) {
                    result = groupStation;
                    break;
                } else {
                    if (groupStation.getDuWordList() == null || groupStation.getDuWordList().size() == 0) {
                        continue;
                    } else {
                        for (DUWord tempStation : groupStation.getDuWordList()) {
                            if (tempStation.isChecked()) {
                                result = tempStation;
                                break;
                            }
                        }
                    }
                }
            }
            if (result == null) {
                Toast.makeText(this, R.string.toast_no_choose, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Intent intent = new Intent();
                intent.putExtra(Constant.CHOOSE_STATION, result);
                setResult(QueryOrderActivity.RESULT_CHOOSE_STATION, intent);
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mSiteWords = new ArrayList<>();
        mChooseSiteAdapter = new ChooseSiteAdapter(mSiteWords);
        mExpandListView.setAdapter(mChooseSiteAdapter);
//        mExpandListView.setGroupIndicator(null);
        mExpandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                resetAllCheckbox();
                mSiteWords.get(groupPosition).setChecked(true);
                mChooseSiteAdapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(), "你选择了" + mSiteWords.get(groupPosition).getName(), Toast.LENGTH_SHORT).show();
                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });
        mExpandListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                resetAllCheckbox();
                mSiteWords.get(groupPosition).getDuWordList().get(childPosition).setChecked(true);
                mChooseSiteAdapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(), "您选择了" + mSiteWords.get(groupPosition).getDuWordList().get(childPosition).getName(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void resetAllCheckbox() {
        for (DUWord groupStation : mSiteWords) {
            groupStation.setChecked(false);
            if (groupStation.getDuWordList() == null || groupStation.getDuWordList().size() == 0) {
                continue;
            } else {
                for (DUWord tempStation : groupStation.getDuWordList()) {
                    tempStation.setChecked(false);
                }
            }
        }
    }

    @Override
    public void onShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        hideProgress();
    }

    @Override
    public void onGetSites(List<DUWord> duWordList) {
        hideProgress();
        if (duWordList == null || duWordList.size() == 0) {
            Toast.makeText(this, R.string.toast_no_result, Toast.LENGTH_SHORT).show();
            return;
        }
        mSiteWords.clear();
        mSiteWords.addAll(duWordList);
        mChooseSiteAdapter.notifyDataSetChanged();
        for (int i = 0; i < mSiteWords.size(); i++) {
            mExpandListView.expandGroup(i);
        }
    }

    private class ChooseSiteAdapter extends BaseExpandableListAdapter {

        private List<DUWord> mList;

        public ChooseSiteAdapter() {
        }

        public ChooseSiteAdapter(List<DUWord> mList) {
            this.mList = mList;
        }

        @Override
        public int getGroupCount() {
            return mList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mList.get(groupPosition).getDuWordList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mList.get(groupPosition).getDuWordList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder groupViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ChooseSiteActivity.this).inflate(R.layout.item_expand_group, parent, false);
                groupViewHolder = new GroupViewHolder();
                groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);
                groupViewHolder.checkBox = (ImageView) convertView.findViewById(R.id.item_group_check);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }
            groupViewHolder.tvTitle.setText(mList.get(groupPosition).getName());
//            groupViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    resetAllCheckbox();
//                    mList.get(groupPosition).setChecked(b);
//                }
//            });
//            resetAllCheckbox();
//            mList.get(groupPosition).setChecked(groupViewHolder.checkBox.isChecked());
            boolean isChecked = mList.get(groupPosition).isChecked();
//            groupViewHolder.checkBox.setChecked(isChecked);
            if (isChecked) {
                groupViewHolder.checkBox.setVisibility(View.VISIBLE);
            } else {
                groupViewHolder.checkBox.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ChooseSiteActivity.this).inflate(R.layout.item_expand_child, parent, false);
                childViewHolder = new ChildViewHolder();
                childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_expand_child);
                childViewHolder.checkBox = (ImageView) convertView.findViewById(R.id.item_child_check);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            childViewHolder.tvTitle.setText(mList.get(groupPosition).getDuWordList().get(childPosition).getName());
            boolean isChecked = mList.get(groupPosition).getDuWordList().get(childPosition).isChecked();
            if (isChecked) {
                childViewHolder.checkBox.setVisibility(View.VISIBLE);
            } else {
                childViewHolder.checkBox.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

        private class GroupViewHolder {
            TextView tvTitle;
            ImageView checkBox;
        }

        private class ChildViewHolder {
            TextView tvTitle;
            ImageView checkBox;
        }
    }
}
