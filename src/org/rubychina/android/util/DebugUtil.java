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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import org.rubychina.android.activity.BugReportActivity;

import android.app.Activity;
import android.content.Intent;

public class DebugUtil {

	private static boolean catchBug = true;
	
	private DebugUtil() {
		
	}
	
	public static void setupErrorHandler(final Activity host) {
		if(catchBug) {
			Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
					Exception e = new Exception(ex);
					e.printStackTrace();
					final Writer result = new StringWriter();
					final PrintWriter printWriter = new PrintWriter(result);
					ex.printStackTrace(printWriter);
					String stacktrace = result.toString();
					printWriter.close();
					Intent intent = new Intent(host, BugReportActivity.class);
					intent.putExtra(BugReportActivity.CAUSE, stacktrace);
					host.startActivity(intent);
					System.exit(0);
				}
			});
		}
	}
	
}
