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
                .setPositiveButton(R.string.p_yek,
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
