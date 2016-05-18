package com.e.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @{# FragmentPagerAdapter Create on 2014年11月28日 下午3:32:37
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> fragments;

	public FragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}



	@Override
	public int getCount() {

		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

}
