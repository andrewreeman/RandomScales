package com.stepwise.random_scales;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by andy on 27/05/15.
 */
public class InputPresetNameDialogFragment extends DialogFragment implements Dialog.OnClickListener {

    private String m_newPreset;
    private String m_hint;
    private EditText m_editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceBundle){
        m_editText = new EditText(getActivity());

        if(m_hint != null){
            m_editText.setText(m_hint);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Preset name");
        builder.setMessage("Please enter a preset name");
        builder.setView(m_editText);
        builder.setPositiveButton("OK", this);
        builder.setNegativeButton("Cancel", this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which){
        try {
            if (which == Dialog.BUTTON_POSITIVE) {
                m_newPreset = m_editText.getText().toString();
                if (m_newPreset.isEmpty()) return;
                if(m_newPreset.contains("*")){
                    Toast.makeText(getActivity(), "Name cannot contain *", Toast.LENGTH_LONG).show();
                    return;
                }
                Presets activity = (Presets) getActivity();
                activity.inputPresetNameFinished(m_newPreset, false);
            }
        }
        catch(AssertionError e){
            Log.d("Error","InputPresetNameDialogFragment.onClick " + e.getMessage());
        }
    }

    public void setHint(String hint){
        m_hint = hint;
    }

}
