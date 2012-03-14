package org.rubychina.android.activity;

import greendroid.app.GDListActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import org.rubychina.android.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


public class TopicsActivity extends GDListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addActionBarItem(Type.Refresh, R.id.action_bar_refresh);
		addActionBarItem(Type.Compose, R.id.action_bar_compose);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
        case R.id.action_bar_refresh:
        	Toast.makeText(getApplicationContext(), "refresh", Toast.LENGTH_SHORT).show();
        	return true;
        case R.id.action_bar_compose:
        	Intent i = new Intent();
        	i.setClass(getApplicationContext(), NewTopicActivity.class);
        	startActivity(i);
        	return true;
        default:
        	Toast.makeText(getApplicationContext(), "default", Toast.LENGTH_SHORT).show();
            return super.onHandleActionBarItemClick(item, position);
		}
	}

}
