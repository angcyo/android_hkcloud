package com.lidroid.xutils.view;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.support.v4.app.Fragment;
import android.view.View;

/**@description: 增加对Fragment的注解支持
 * @author zhoukailong 2015-12-10
 */
/**
 * Author: wyouflf Date: 13-9-9 Time: 下午12:29
 */
public class ViewFinder {

	private View view;
	private Activity activity;
	private Fragment fragment;
	private PreferenceGroup preferenceGroup;
	private PreferenceActivity preferenceActivity;

	public ViewFinder(View view) {
		this.view = view;
	}

	public ViewFinder(Activity activity) {
		this.activity = activity;
	}

	public ViewFinder(Fragment fragment) {
		this.fragment = fragment;
	}

	public ViewFinder(PreferenceGroup preferenceGroup) {
		this.preferenceGroup = preferenceGroup;
	}

	public ViewFinder(PreferenceActivity preferenceActivity) {
		this.preferenceActivity = preferenceActivity;
		this.activity = preferenceActivity;
	}

	public View findViewById(int id) { // 修改这里
		// View v = activity == null ? view.findViewById(id) :
		// activity.findViewById(id);
		View v = null;
		if (activity != null) {
			v = activity.findViewById(id);
		} else if (this.fragment != null) {
			v = this.fragment.getView().findViewById(id);
		} else {
			v = view.findViewById(id);
		}
		return v;
	}

	public View findViewByInfo(ViewInjectInfo info) {
		return findViewById((Integer) info.value, info.parentId);
	}

	public View findViewById(int id, int pid) {
		View pView = null;
		if (pid > 0) {
			pView = this.findViewById(pid);
		}

		View view = null;
		if (pView != null) {
			view = pView.findViewById(id);
		} else {
			view = this.findViewById(id);
		}
		return view;
	}

	@SuppressWarnings("deprecation")
	public Preference findPreference(CharSequence key) {
		return preferenceGroup == null ? preferenceActivity.findPreference(key) : preferenceGroup.findPreference(key);
	}

	public Context getContext() {
		if (view != null)
			return view.getContext();
		if (activity != null)
			return activity;
		if (preferenceActivity != null)
			return preferenceActivity;
		if (fragment != null)
			return fragment.getActivity();
		return null;
	}
}
