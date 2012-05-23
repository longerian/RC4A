package org.rubychina.android.test;

import org.rubychina.android.activity.NodesActivity;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class NodesActivityTest extends ActivityInstrumentationTestCase2<NodesActivity> {
	
	private Activity mActivity;
	
	public NodesActivityTest() {
		super("org.rubychina.android", NodesActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    setActivityInitialTouchMode(false);
	    mActivity = getActivity();
	}
	
	public void testActivity() {
		assertTrue(mActivity instanceof SherlockFragmentActivity);
	}
	
}
