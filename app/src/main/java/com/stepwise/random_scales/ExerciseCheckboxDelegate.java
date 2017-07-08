package com.stepwise.random_scales;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The ExerciseCheckBoxDelegate acts as the in-memory datastore for the selected exercises.
 */
public class ExerciseCheckboxDelegate implements View.OnClickListener {

    HashMap<CheckBox, Exercise> m_checkboxExerciseMap;
    HashMap<Exercise, CheckBox> m_exerciseCheckBoxMap;
    SelectableExercises_Data m_selectableExerciseData;
    Presets m_checkboxChangedListener;

    public ExerciseCheckboxDelegate(){
        m_checkboxExerciseMap = new HashMap<>();
        m_exerciseCheckBoxMap = new HashMap<>();
    }

    public void setCheckboxChangedListener(Presets context){ m_checkboxChangedListener = context;}

    public void setFromJSON(JSONObject obj, HashMap<String, ArrayList<Exercise>> allScales, HashMap<String, ArrayList<Exercise>> allArps){
        JSONArray scales;
        JSONArray arps;
        try{
            deselectAllCheckBoxes();
            m_selectableExerciseData.clear();
            scales = obj.getJSONArray(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_scales));
            arps = obj.getJSONArray(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_arps));
            setExerciseFromJSONArray(scales, allScales, Exercise.ExerciseType.SCALE);
            setExerciseFromJSONArray(arps, allArps, Exercise.ExerciseType.ARPEGGIO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void selectAllCheckBoxes(){
        for(Map.Entry<CheckBox, Exercise> entry : m_checkboxExerciseMap.entrySet()){
            if( !(entry.getKey().isChecked())) {
                entry.getKey().setChecked(true);
                m_selectableExerciseData.addExercise(entry.getValue());
            }
        }
    }

    public void deselectAllCheckBoxes(){
        for(Map.Entry<CheckBox, Exercise> entry : m_checkboxExerciseMap.entrySet()){
            if(entry.getKey().isChecked()) {
                entry.getKey().setChecked(false);
                m_selectableExerciseData.removeExercise(entry.getValue());
             }
        }
    }

    public void clear(){
        m_checkboxExerciseMap.clear();
        m_exerciseCheckBoxMap.clear();
    }

    @Override
    public void onClick(View v){
        if(!(v instanceof CheckBox))
            throw new ClassCastException("Error in ExerciseCheckboxDelegate.onClick: v " + v.toString() + "is not an instance of CheckBox");
        CheckBox checkBox = (CheckBox)v;
        Exercise ex = m_checkboxExerciseMap.get(checkBox);
        if(checkBox.isChecked())
            m_selectableExerciseData.addExercise(ex);
        else
            m_selectableExerciseData.removeExercise(ex);
        m_checkboxChangedListener.onCheckBoxChanged();
    }

    private void setExerciseFromJSONArray(JSONArray array, HashMap<String, ArrayList<Exercise>> allExercisesOfType, Exercise.ExerciseType type) throws JSONException{
        JSONObject obj;
        String name;
        String hint;
        JSONArray keys;
        ArrayList<Exercise> exerciseList;
        Exercise ex;

        for(int i=0; i<array.length(); ++i){
            obj = (JSONObject)array.get(i);
            name = obj.getString(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_name));
            hint = obj.getString(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_hint));
            keys = obj.getJSONArray(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_keyArray));

            if(!allExercisesOfType.containsKey(name))
                throw new AssertionError("Error in ExerciseCheckboxDelegate.setExerciseFromJSONArray: " + name + " is not included in allExercisesOfType");
            exerciseList = allExercisesOfType.get(name);

            for(int key=0; key<keys.length(); ++key){
                //If exercise exists within exerciseList then add the one in exercise list. Making use of currently existing exercises.
                try {
                    ex = new Exercise(keys.getString(key), name, type, hint);
                    if(exerciseList.contains(ex)){
                        int index = exerciseList.indexOf(ex);
                        m_selectableExerciseData.addExercise(exerciseList.get(index));
                    }
                } catch (Exercise.InvalidKeyException e) {
                    Log.e("ExerciseCheckboxDele", e.getMessage());
                }

            }
        }
    }

    private void update(ArrayList<Exercise> exerciseList){
        for(Exercise ex : exerciseList){
            CheckBox checkBox = m_exerciseCheckBoxMap.get(ex);
            checkBox.setChecked(true);
        }
    }
}