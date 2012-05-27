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
package org.rubychina.android.util;

import org.rubychina.android.RCApplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
