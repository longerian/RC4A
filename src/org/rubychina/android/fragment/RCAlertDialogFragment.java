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
package org.rubychina.android.fragment;

import org.rubychina.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class RCAlertDialogFragment extends SherlockDialogFragment {

	private OnRCDialogFragmentClickListener listener;
	
	public static RCAlertDialogFragment newInstance(int title) {
		RCAlertDialogFragment frag = new RCAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
            listener = (OnRCDialogFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnRCDialogFragmentClickListener");
        }
		
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(R.string.p_yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	listener.doPositiveClick();
                        }
                    }
                )
                .setNegativeButton(R.string.p_no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	listener.doNegativeClick();
                        }
                    }
                )
                .create();
    }
    
    public interface OnRCDialogFragmentClickListener {
    	
    	public void doPositiveClick();
    	
    	public void doNegativeClick();
    	
    }
    
}
