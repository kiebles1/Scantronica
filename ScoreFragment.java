package com.jensenkiebles.scantronica;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoreFragment extends DialogFragment {
	OnOkPressedListener mListener;
	View layout = null;
	public double score;
	public int correct;

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
 	    //View aview = getView().findViewById(R.id.username);
	    layout = inflater.inflate(R.layout.score_fragment_layout, null); 
	    TextView scoreView = (TextView) layout.findViewById(R.id.textView1);
	    scoreView.setText(String.valueOf(score*100) + " %");
	    TextView questionsView = (TextView) layout.findViewById(R.id.textView2);
	    questionsView.setText(String.valueOf(correct) + " questions");
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(layout)
	    // Add action buttons
	           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
//	            	   View aview = layout.findViewById(R.id.username);
//	            	   mListener.onOkPressed(findText());
	               }
	           });	    
	    
	    return builder.create();

	}
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater,
//	        ViewGroup container, Bundle savedInstanceState){
//	    // -- inflate the layout for this fragment
//	    View myInflatedView = inflater.inflate(R.layout.score_fragment_layout, container, false);
//
//	    return myInflatedView;
//	}
	
	public void onAttach(Activity activity, double score) {
        super.onAttach(activity);
        try {
            mListener = (OnOkPressedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnOkPressedListener");
        }
    }
	
	// Container Activity must implement this interface
    public interface OnOkPressedListener {
//        public void onOkPressed(String textEntry);
    }
	
//	public String findText() {
//		return ((EditText) layout.findViewById(R.id.username)).getText().toString();
//	}
}
