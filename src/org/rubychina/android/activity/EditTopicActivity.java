package org.rubychina.android.activity;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import org.rubychina.android.R;

import android.os.Bundle;
import android.widget.Toast;

public class EditTopicActivity extends GDActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO change Type.Add to Type.Update
		addActionBarItem(Type.Add, R.id.action_bar_update);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
        case R.id.action_bar_add:
        	Toast.makeText(getApplicationContext(), "update post", Toast.LENGTH_SHORT).show();
        	return true;
        default:
            return super.onHandleActionBarItemClick(item, position);
		}
	}
	
}
