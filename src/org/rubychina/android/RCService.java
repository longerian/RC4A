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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.rubychina.android.database.RCDBResolver;
import org.rubychina.android.type.Node;
import org.rubychina.android.type.SiteGroup;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;
import org.rubychina.android.util.GravatarUtil;
import org.rubychina.android.util.ImageUtil;
import org.rubychina.android.util.LogUtil;

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

	private static final String TAG = "RCService";
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
					Drawable drawable;
					try {
						URL url = new URL(source);
						InputStream is = url.openStream();
						drawable = Drawable.createFromStream(is, "");
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
					} catch (IOException e) {
						e.printStackTrace();
						return new BitmapDrawable();
					} catch (NullPointerException e) {
						return new BitmapDrawable();
					}
					 Bitmap sb = ImageUtil.getScaledBitmap((RCApplication) getApplication(), 
							 ((BitmapDrawable) drawable).getBitmap());
					 drawable = new BitmapDrawable(sb);
					 drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
					 return drawable;
				}
				
			};
		}
		return imgGetter;
	}
	
	public void requestUserAvatar(User user, ImageView view, int size) {
		String avatar = user.getAvatarUrl();
		String hash = user.getGravatarHash();
		if(!TextUtils.isEmpty(hash)) {
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
	
	public void requestImage(String url, ImageView view) {
		Bitmap ava = ((RCApplication) getApplication()).getImgLoader().load(url, view);
		if(ava != null) {
			view.setImageBitmap(ava);
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
	
	public List<User> fetchUsers() {
		List<User> users = GlobalResource.INSTANCE.getUsers();
		if(users.isEmpty()) {
			users = RCDBResolver.INSTANCE.fetchUsers(getApplicationContext());
			GlobalResource.INSTANCE.setUsers(users);
		}
		return users;
	}
	
	public boolean insertUsers(List<User> users) {
		GlobalResource.INSTANCE.setUsers(users);
		return RCDBResolver.INSTANCE.insertUsers(getApplicationContext(), users);
	}
	
	public boolean clearUsers() {
		GlobalResource.INSTANCE.getUsers().clear();
		return RCDBResolver.INSTANCE.clearUsers(getApplicationContext());
	}
	
	public List<SiteGroup> fetchSites() {
		List<SiteGroup> siteGroups = GlobalResource.INSTANCE.getSites();
		if(siteGroups.isEmpty()) {
			siteGroups = RCDBResolver.INSTANCE.fetchSites(getApplicationContext());
			GlobalResource.INSTANCE.setSites(siteGroups);
		}
		return siteGroups;
	}
	
	public boolean insertSites(List<SiteGroup> sites) {
		GlobalResource.INSTANCE.setSites(sites);
		return RCDBResolver.INSTANCE.insertSites(getApplicationContext(), sites);
	}
	
	public boolean clearSites() {
		GlobalResource.INSTANCE.getSites().clear();
		return RCDBResolver.INSTANCE.clearSites(getApplicationContext());
	}
	
}
