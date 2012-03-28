package org.rubychina.android.util;

import org.rubychina.android.RCApplication;

import android.graphics.Bitmap;

public class ImageUtil {

	private ImageUtil() {
		
	}
	
	/**
	 * scaled the image if its width is larger than screen's physical width
	 * @param rc app's global application
	 * @param b original bitmap
	 * @return the scaled bitmap
	 */
	public static Bitmap getScaledBitmap(RCApplication rc, Bitmap b) {
			int screenWidth = rc.getScreenWidth();
	    	int newWidth = b.getWidth(), newHeight = b.getHeight();
	    	if(b.getWidth() > screenWidth) {
	    		newWidth = screenWidth;
	    		float ratio = 1.0f * b.getWidth() / newWidth;
	    		newHeight = (int) (b.getHeight() / ratio);
	    	}
	    	Bitmap nb = Bitmap.createScaledBitmap(b, newWidth, newHeight, true);
	    	return nb;
	}
	
}
