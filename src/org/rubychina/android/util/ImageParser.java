package org.rubychina.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rubychina.android.RCApplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

public class ImageParser {

	private static final String TAG = "ImageParser";
	private static final String RC_IMG_PATTERN = "!\\[.*\\]\\(.*\\)"; 
	private Context mContext;
    private Pattern mPattern;
    private BitmapFactory.Options mBO;
    
    public ImageParser(Context context) {  
        mContext = context;  
        mPattern = buildPattern();  
        mBO = new Options();
    }  
  
    private Pattern buildPattern() {  
        return Pattern.compile(RC_IMG_PATTERN);  
    }  
  
    public CharSequence replace(String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);  
        Matcher matcher = mPattern.matcher(text);  
        while (matcher.find()) {
        	String rawUrl = text.substring(matcher.start(), matcher.end());
        	String url = getImageUrl(rawUrl);
        	Bitmap b = ((RCApplication) mContext.getApplicationContext()).getImgLoader().load(url, null);
        	if(b != null) {
        		builder.setSpan(
        				new ImageSpan(mContext, getScaledBitmap(b)), 
        						matcher.start(),
        						matcher.end(),
        						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        	}
        }  
        return builder;  
    }
    
	private String getImageUrl(String rawUrl) {
    	int start = rawUrl.indexOf("(");
    	int end = rawUrl.indexOf(")");
    	String url = rawUrl.substring(start + 1, end);
    	return url;
    }
	
	private Bitmap getScaledBitmap(Bitmap b) {
		int screenWidth = ((RCApplication) mContext.getApplicationContext()).getScreenWidth();
    	int newWidth = b.getWidth(), newHeight = b.getHeight();
    	if(b.getWidth() > screenWidth) {
    		int scale = 1;
    		int tempWith = b.getWidth();
    		do {
    			scale <<= 1;
    			tempWith /= 2;
    		} while(tempWith > screenWidth);
    		newWidth = b.getWidth() / scale;
    		newHeight = b.getHeight() / scale;
    	}
    	Bitmap nb = Bitmap.createScaledBitmap(b, newWidth, newHeight, true);
    	return nb;
	}
    
}
