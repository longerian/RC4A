package org.rubychina.android.widget;

import java.util.ArrayList;
import java.util.List;

import org.rubychina.android.fragment.TopicDetailFragment;
import org.rubychina.android.type.Topic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TopicPagerAdapter extends FragmentPagerAdapter {

	List<Topic> topics = new ArrayList<Topic>();
	
	public TopicPagerAdapter(FragmentManager fm, List<Topic> topics) {
		super(fm);
		this.topics = topics;
	}
	
	@Override
	public Fragment getItem(int position) {
		return TopicDetailFragment.newInstance(topics.get(position));
	}

	@Override
	public int getCount() {
		return topics.size();
	}

}
