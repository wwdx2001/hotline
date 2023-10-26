package com.sh3h.hotline.ui.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.CollectionPhotoAdapter;
import com.sh3h.hotline.entity.CollectionPhotoBean;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.umeng.message.common.Const;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CollectionMediaFragment extends ParentFragment  implements CollectionPhotoAdapter.OnRecyclerViewItemClickListener {
  private Unbinder mUnbinder;
  private ArrayList<CollectionPhotoBean> mCollectionPhotoBean,mCollectionVoiceBean,mCollectionCameraBean;
  private CollectionPhotoAdapter mCollectionPhotoAdapter,mCollectionVoiceAdapter,mCollectionCameraAdapter;
  @BindView(R.id.recycler_type1)
  RecyclerView mRecyclePhoto;
  @BindView(R.id.recycler_zhoubian)
  RecyclerView mRecycleVoice;
  @BindView(R.id.recycler_shuibiao)
  RecyclerView mRecycleCamera;

  @Override
  protected void lazyLoad() {

  }



  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragement_collection_duomeiti, container, false);
    mUnbinder = ButterKnife.bind(this,view);
    mCollectionPhotoBean= new ArrayList<>();
    mCollectionVoiceBean= new ArrayList<>();
    mCollectionCameraBean= new ArrayList<>();
    initView();
    return view;
  }

  private void initView() {
    mRecyclePhoto.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    mRecyclePhoto.setHasFixedSize(true);
    mRecyclePhoto.setNestedScrollingEnabled(false);
    mCollectionPhotoAdapter = new CollectionPhotoAdapter(getActivity(),mCollectionPhotoBean,3,1);
    mRecyclePhoto.setAdapter(mCollectionPhotoAdapter);


    mRecycleVoice.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    mRecycleVoice.setHasFixedSize(true);
    mRecycleVoice.setNestedScrollingEnabled(false);
    mCollectionVoiceAdapter = new CollectionPhotoAdapter(getActivity(),mCollectionVoiceBean,3,1);
    mRecycleVoice.setAdapter(mCollectionVoiceAdapter);

    mRecycleCamera.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    mRecycleCamera.setHasFixedSize(true);
    mRecycleCamera.setNestedScrollingEnabled(false);
    mCollectionCameraAdapter = new CollectionPhotoAdapter(getActivity(),mCollectionCameraBean,3,1);
    mRecycleCamera.setAdapter(mCollectionCameraAdapter);

    mCollectionPhotoAdapter.setOnItemClickListener(this);
    mCollectionVoiceAdapter.setOnItemClickListener(this);
    mCollectionCameraAdapter.setOnItemClickListener(this);
  }

  @Override
  public void onItemClick(View view, int position, int type) {
    Log.d( "onItemClick: ", String.valueOf(position));
  }
}
