package com.sh3h.hotline.util;


import android.app.Activity;
import android.util.Log;

import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentActivity;

import java.util.Stack;

public class ActivityManagerHelper {
  private static ActivityManagerHelper mInstance = null;
  private static final String TAG = "ActivityManagerHelper";

  private Stack<Activity> mActivityStack;

  private ActivityManagerHelper() {
    mActivityStack = new Stack<Activity>();
  }

  public static ActivityManagerHelper getInstance() {
    if (mInstance == null) {
      mInstance = new ActivityManagerHelper();
    }
    return mInstance;
  }

  public void pushActivity(Activity activity) {
    Log.e(TAG, "pushActivity: " + activity.getClass().getName());
    mActivityStack.add(activity);
  }

  public void popActivity(Activity activity) {
    if (activity != null) {
      if (mActivityStack.contains(activity)) {
        Log.e(TAG, "popActivity: " + activity.getClass().getName());
        activity.finish();
        mActivityStack.remove(activity);
        activity = null;
      }
    }
  }

  public void popActivity2(Activity activity) {
    if (activity != null) {
      if (mActivityStack.contains(activity)) {
        Log.e(TAG, "popActivity2: " + activity.getClass().getName());
        mActivityStack.remove(activity);
        activity = null;
      }
    }
  }

  public Activity currentActivity() {
    if (mActivityStack.size() > 0) {
      return mActivityStack.lastElement();
    } else {
      return null;
    }
  }

  public void popAllActivities() {
    while (true) {
      Activity activity = currentActivity();
      if (activity == null) {
        break;
      }

      popActivity(activity);
    }
  }

  public void popElesActivites(Class clazz) {
    while (true) {
      Activity activity = currentActivity();
      if (activity == null) {
        break;
      }

      if (!activity.getClass().equals(clazz)) {
        popActivity(activity);
      }
    }
  }


  public void popParentElesActivites() {
    while (true) {
      Activity activity = currentActivity();
      if (activity == null) {
        break;
      }

      if (!activity.getClass().equals(ParentActivity.class) || !activity.getClass().equals(BaseActivity.class)) {
        popActivity(activity);
      }
    }
  }


  public void popActivities(Class cls) {
    while (true) {
      Activity activity = currentActivity();
      if (activity == null) {
        break;
      }

      if (activity.getClass().equals(cls)) {
        break;
      }

      popActivity(activity);
    }
  }

  public void popActivities(int count) {
    while (count-- > 0) {
      Activity activity = currentActivity();
      if (activity == null) {
        break;
      }

      popActivity(activity);
    }
  }
}
