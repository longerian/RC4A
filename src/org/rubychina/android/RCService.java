package org.rubychina.android;

import org.rubychina.android.type.User;
import org.rubychina.android.util.GravatarUtil;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.ImageView;

public class RCService extends Service {

	// Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
    	public RCService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RCService.this;
        }
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	public void requestUserAvatar(User user, ImageView view) {
		String avatar = user.getAvatarUrl();
		String hash = user.getGravatarHash();
		if(TextUtils.isEmpty(avatar)) {
			Bitmap ava = ((RCApplication) getApplication()).getImgLoader().load(GravatarUtil.getBaseURL(hash), view);
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
	
}
