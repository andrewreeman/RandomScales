package com.stepwise.random_scales;

import android.view.View;
import android.widget.CheckBox;

import java.util.HashMap;

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

    }

    public void setCheckBox(Exercise ex, boolean isChecked){
        m_exerciseCheckBoxMap.get(ex).setChecked(isChecked);
    }

}
