package com.stepwise.random_scales;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by andy on 28/05/15.
 */
public class OverwritePresetDialogFragment extends DialogFragment implements Dialog.OnClickListener{
    private String m_newPreset;
    //TODO check for is empty here
    public void setNewPresetName(String newPreset){ m_newPreset = newPreset; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceBundle){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Overwrite");
        builder.setMessage("Do you wish to overwrite " + m_newPreset);
        builder.setPositiveButton("OK", this);
        builder.setNegativeButton("Cancel", this);
        return builder.create();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == Dialog.BUTTON_POSITIVE) {
            //TODO greater use of assertions
            if (BuildConfig.DEBUG && m_newPreset.isEmpty()) {
                throw new AssertionError("Error: OverWritePresetDialogFragment.m_newPreset cannot be empty.");
            }
            Presets activity = (Presets) getActivity();
            activity.inputPresetNameFinished(m_newPreset, true);
        }
    }


}
