package org.rubychina.android.activity;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class BugReportActivity extends SherlockActivity {

	public static final String CAUSE = "cause";
	private EditText cause;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_bug_report);
		setContentView(R.layout.bug_report_layout);
		cause = (EditText) findViewById(R.id.cause);
		String c = getIntent().getStringExtra(CAUSE);
		if(c != null)
			cause.setText(c);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.action_bar_send, 0, R.string.actionbar_send)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_bar_send:
	        	send("xlhongultimate@gmail.com", "I found a bug", cause.getText().toString());
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void send(String receiver, String subject, String content) {
		Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.setData(Uri.parse("mailto:" + receiver)); // or just "mailto:" for blank
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
		startActivity(intent);
	}
	
}
