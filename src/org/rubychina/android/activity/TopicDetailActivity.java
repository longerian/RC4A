package org.rubychina.android.activity;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import org.rubychina.android.GlobalResource;
import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.type.Topic;
import org.rubychina.android.util.GravatarUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
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
	
	private Topic t;
//	private EditText replyContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setActionBarContentView(R.layout.topic_detail_layout);
		addActionBarItem(Type.List, R.id.action_bar_replies);
		
		//TODO if pos = 0; crash
		t = GlobalResource.INSTANCE.getCurTopics().get(getIntent().getIntExtra(POS, 0));
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(t.getTitle());
		
		ImageView gravatar = (ImageView) findViewById(R.id.gravatar);
		String avatar = t.getUser().getAvatarUrl();
		String hash = t.getUser().getGravatarHash();
		if(TextUtils.isEmpty(avatar)) {
			Bitmap ava = ((RCApplication) getApplication()).getImgLoader().load(GravatarUtil.getBaseURL(hash), gravatar);
			if(ava != null) {
				gravatar.setImageBitmap(ava);
			} else {
				gravatar.setImageResource(R.drawable.default_gravatar);
			}
		} else {
			Bitmap ava = ((RCApplication) getApplication()).getImgLoader().load(avatar, gravatar);
			if(ava != null) {
				gravatar.setImageBitmap(ava);
			} else {
				gravatar.setImageResource(R.drawable.default_gravatar);
			}
		}
		
		WebView webView = (WebView) findViewById(R.id.body_html);
		webView.getSettings().setJavaScriptEnabled(true); 
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.loadData(t.getBodyHTML(), "text/html", "UTF-8");
		
	}
	
	//no api for reply, so remove reply box
//	@Override
//	protected void onStart() {
//		super.onStart();
//		View replyBox = findViewById(R.id.comment);
//		View loginNoteBox = findViewById(R.id.login_note);
//		if(((RCApplication) getApplication()).isLogin()) {
//			replyBox.setVisibility(View.VISIBLE);
//			loginNoteBox.setVisibility(View.INVISIBLE);
//			replyContent = (EditText) findViewById(R.id.content);
//			ImageView submit = (ImageView) findViewById(R.id.submit);
//			submit.setOnClickListener(mSubmitListener);
//		} else {
//			replyBox.setVisibility(View.INVISIBLE);
//			loginNoteBox.setVisibility(View.VISIBLE);
//			loginNoteBox.setOnClickListener(mNeedLoginListener);
//		}
//	}
	
//	private OnClickListener mNeedLoginListener = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			Intent i = new Intent(getApplicationContext(), UserVerificationActivity.class);
//			startActivity(i);
//		}
//	};

//	private OnClickListener mSubmitListener = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			Intent i = new Intent();
//			Toast.makeText(getApplicationContext(), "submit: " + replyContent.getText().toString(), Toast.LENGTH_SHORT).show();
//		}
//	};

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

	
}
