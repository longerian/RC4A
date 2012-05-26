package org.rubychina.android.fragment;

import org.rubychina.android.type.User;

import android.widget.ImageView;

public interface TopicActor {

	public void visitUserProfile(User u);
	
	public void requestUserAvatar(User u, ImageView v, int size);
	
}
