package org.rubychina.android.activity;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import org.rubychina.android.GlobalResource;
import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.type.Reply;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;
import org.rubychina.android.util.GravatarUtil;
import org.rubychina.android.util.ImageParser;

import com.google.gson.Gson;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TopicDetailActivity extends GDActivity {

	public static final String POS = "org.rubychina.android.activity.TopicDetailActivity.POSITION";
	private static final String TAG = "TopicDetailActivity";
	
	private ImageView gravatar;
	private RCService mService;
	private boolean isBound = false; 
	private Topic t;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setActionBarContentView(R.layout.topic_detail_layout);
		addActionBarItem(Type.List, R.id.action_bar_replies);
		
		t = GlobalResource.INSTANCE.getCurTopics().get(getIntent().getIntExtra(POS, 0));
		
		gravatar = (ImageView) findViewById(R.id.gravatar);
		gravatar.setOnClickListener(mCheckProfileListener );
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(t.getTitle());
		
		TextView body = (TextView) findViewById(R.id.body);

		ImageParser ip = new ImageParser(getApplicationContext());
		body.setText(ip.replace(t.getBody()));  
	}
	
	@Override
	protected void onStart() {
		super.onStart();
        Intent intent = new Intent(this, RCService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	private OnClickListener mCheckProfileListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Gson g = new Gson();
			Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
			i.putExtra(UserProfileActivity.VIEW_PROFILE, g.toJson(t.getUser()));
			startActivity(i);
		}
	};
	
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            isBound = true;
            requestUserAvatar();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
    
    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
        case R.id.action_bar_replies:
        	Intent i = new Intent(getApplicationContext(), ReplyListActivity.class);
        	i.putExtra(ReplyListActivity.BELONG_TO_TOPIC, t.getId());
        	startActivity(i);
        	return true;
        default:
            return super.onHandleActionBarItemClick(item, position);
		}
	}
	
	private void requestUserAvatar() {
		if(gravatar == null) {
			gravatar = (ImageView) findViewById(R.id.gravatar);
		}
		mService.requestUserAvatar(t.getUser(), gravatar);
	}

	
}
