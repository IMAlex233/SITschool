package com.example.luhongcheng.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class OAFragmentPagerAdapter extends FragmentPagerAdapter {
	//存储所有的fragment
	private List<Fragment> list;
	
	public OAFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list){
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}