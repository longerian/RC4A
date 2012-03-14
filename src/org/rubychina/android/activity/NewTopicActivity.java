package org.rubychina.android.activity;

import org.rubychina.android.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

public class NewTopicActivity extends GDActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addActionBarItem(Type.Add, R.id.action_bar_add);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
        case R.id.action_bar_add:
        	Toast.makeText(getApplicationContext(), "add new post", Toast.LENGTH_SHORT).show();
        	return true;
        default:
            return super.onHandleActionBarItemClick(item, position);
		}
	}
	
}
