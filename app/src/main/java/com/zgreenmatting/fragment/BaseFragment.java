package com.zgreenmatting.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
	protected Context mContext;
	protected View contentView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(getContentLayout(),container,false);
		mContext = getActivity();
		ButterKnife.bind(this,contentView);
		preInitData();
		return contentView;
	}

	protected abstract int getContentLayout();

	protected abstract void preInitData();


	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	protected <T extends Activity> void  startActivity(Class<T> clazz){
		Intent intent = new Intent(mContext,clazz);
		startActivity(intent);
	}
	protected <T extends Activity> void  startActivity(Class<T> clazz,Bundle bundle){
		Intent intent = new Intent(mContext,clazz);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}