/*Copyright (C) 2012 Longerian (http://www.longerian.me)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package org.rubychina.android;

import java.net.URL;
import java.util.List;

import org.rubychina.android.database.RCDBResolver;
import org.rubychina.android.type.Node;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;
import org.rubychina.android.util.GravatarUtil;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.widget.ImageView;

public class RCService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private ImageGetter imgGetter;
    
    public class LocalBinder extends Binder {
    	public RCService getService() {
            return RCService.this;
        }
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	public ImageGetter getImageGetter() {
		if(imgGetter == null) {
			imgGetter = new Html.ImageGetter() {
				
				public Drawable getDrawable(String source) {
					Drawable drawable = new BitmapDrawable();
					URL url;
					try {
						url = new URL(source);
						drawable = Drawable.createFromStream(url.openStream(), "");
					} catch (Exception e) {
						return new BitmapDrawable();
					}
					drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
							.getIntrinsicHeight());
					return drawable;
				}
				
			};
		}
		return imgGetter;
	}
	
	public void requestUserAvatar(User user, ImageView view, int size) {
		String avatar = user.getAvatarUrl();
		String hash = user.getGravatarHash();
		if(TextUtils.isEmpty(avatar)) {
			Bitmap ava = ((RCApplication) getApplication()).getImgLoader().load(GravatarUtil.getURLWithSize(hash, size), view);
			if(ava != null) {
				view.setImageBitmap(ava);
			}
		} else {
			Bitmap ava = ((RCApplication) getApplication()).getImgLoader().load(avatar, view);
			if(ava != null) {
				view.setImageBitmap(ava);
			}
		}
	}
	
	public List<Node> fetchNodes() {
		List<Node> nodes = GlobalResource.INSTANCE.getNodes();
		if(nodes.isEmpty()) {
			nodes = RCDBResolver.INSTANCE.fetchNodes(getApplicationContext());
			GlobalResource.INSTANCE.setNodes(nodes);
		}
		return nodes;
	}
	
	public boolean insertNodes(List<Node> nodes) {
		GlobalResource.INSTANCE.setNodes(nodes);
		return RCDBResolver.INSTANCE.insertNodes(getApplicationContext(), nodes);
	}
	
	public boolean clearNodes() {
		GlobalResource.INSTANCE.getNodes().clear();
		return RCDBResolver.INSTANCE.clearNodes(getApplicationContext());
	}
	
	public List<Topic> fetchTopics() {
		List<Topic> topics = GlobalResource.INSTANCE.getCurTopics();
		if(topics.isEmpty()) {
			topics = RCDBResolver.INSTANCE.fetchTopics(getApplicationContext());
			GlobalResource.INSTANCE.setCurTopics(topics);
		}
		return topics;
	}
	
	public boolean insertTopics(List<Topic> topics) {
		GlobalResource.INSTANCE.setCurTopics(topics);
		return RCDBResolver.INSTANCE.insertTopics(getApplicationContext(), topics);
	}
	
	public boolean clearTopics() {
		GlobalResource.INSTANCE.getCurTopics().clear();
		return RCDBResolver.INSTANCE.clearTopics(getApplicationContext());
	}
	
}
