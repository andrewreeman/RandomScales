package com.stepwise.random_scales;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 19/04/15.
 */
public class ExerciseCheckboxDelegate implements View.OnClickListener {

    HashMap<CheckBox, Exercise> m_checkboxExerciseMap;
    HashMap<Exercise, CheckBox> m_exerciseCheckBoxMap;
    SelectableExercises_Data m_selectableExerciseData;

    public ExerciseCheckboxDelegate(){
        m_checkboxExerciseMap = new HashMap<CheckBox, Exercise>();
        m_exerciseCheckBoxMap = new HashMap<Exercise, CheckBox>();
   }

    public void setExerciseCheckboxLink(Exercise ex, CheckBox check){
        m_checkboxExerciseMap.put(check, ex);
        m_exerciseCheckBoxMap.put(ex, check);
        check.setOnClickListener(this);
    }
    public void setModel(SelectableExercises_Data model){
        m_selectableExerciseData = model;
    }

    public void updateScales(){
        ArrayList<Exercise> scales = m_selectableExerciseData.getScales();
        update(scales);
    }

    public void updateArps(){
        ArrayList<Exercise> arps = m_selectableExerciseData.getArpeggios();
        update(arps);
    }

    private void update(ArrayList<Exercise> exerciseList){
        for(Exercise ex : exerciseList){
            CheckBox checkBox = m_exerciseCheckBoxMap.get(ex);
            checkBox.setChecked(true);
        }
    }

    public void selectAllCheckBoxes(){
        for(Map.Entry<CheckBox, Exercise> entry : m_checkboxExerciseMap.entrySet()){
            if( !(entry.getKey().isChecked())) {
                entry.getKey().setChecked(true);
                m_selectableExerciseData.addExercise(entry.getValue());
            }
        }
        //Log.d("Arpeggio array size: ", String.valueOf( m_selectableExerciseData.getArpeggios().size() ));
        //Log.d("Scale array size: ", String.valueOf(m_selectableExerciseData.getScales().size() ));
    }

    public void deselectAllCheckBoxes(){
        for(Map.Entry<CheckBox, Exercise> entry : m_checkboxExerciseMap.entrySet()){
            if(entry.getKey().isChecked()) {
                entry.getKey().setChecked(false);
                m_selectableExerciseData.removeExercise(entry.getValue());
                //Log.d("Clearing: ", entry.getValue().toString());
            }
        }
     //   Log.d("Arpeggio array size: ", String.valueOf( m_selectableExerciseData.getArpeggios().size() ));
       // Log.d("Scale array size: ", String.valueOf(m_selectableExerciseData.getScales().size() ));
    }
    public void clear(){
        m_checkboxExerciseMap.clear();
        m_exerciseCheckBoxMap.clear();
    }

    public Exercise getExercise(CheckBox check){
        return m_checkboxExerciseMap.get(check);
    }

    public CheckBox getCheckBox(Exercise ex){
        return m_exerciseCheckBoxMap.get(ex);
    }

    @Override
    public void onClick(View v){
        CheckBox checkBox = (CheckBox)v;
        Exercise ex = m_checkboxExerciseMap.get(checkBox);
        if(checkBox.isChecked())
            m_selectableExerciseData.addExercise(ex);
        else
            m_selectableExerciseData.removeExercise(ex);
    }

    public void setCheckBox(Exercise ex, boolean isChecked){
        m_exerciseCheckBoxMap.get(ex).setChecked(isChecked);
    }

}

