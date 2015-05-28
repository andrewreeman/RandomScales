package com.stepwise.random_scales;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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
        m_checkboxExerciseMap = new HashMap<>();
        m_exerciseCheckBoxMap = new HashMap<>();
    }

    public void setFromJSON(JSONObject obj, HashMap<String, ArrayList<Exercise>> allScales, HashMap<String, ArrayList<Exercise>> allArps){
        JSONArray scales;
        JSONArray arps;
        try{
            deselectAllCheckBoxes();
            m_selectableExerciseData.clear();
            scales = obj.getJSONArray("scales");
            arps = obj.getJSONArray("arps");
            setExerciseFromJSONArray(scales, allScales, Exercise.TYPE_SCALE);
            setExerciseFromJSONArray(arps, allArps, Exercise.TYPE_ARPEGGIO);
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
    }

    private void setExerciseFromJSONArray(JSONArray array, HashMap<String, ArrayList<Exercise>> allExercisesOfType, int type) throws JSONException{
        JSONObject obj;
        String name;
        String hint;
        JSONArray keys;
        ArrayList<Exercise> exerciseList;
        Exercise ex;

        if(BuildConfig.DEBUG && (type != Exercise.TYPE_ARPEGGIO && type != Exercise.TYPE_SCALE)){
            String errorMessage = "Error in ExerciseCheckboxDelegate.setExerciseFromJSONArray: ";
            errorMessage += "int type (" + String.valueOf(type) + ") ";
            errorMessage += " does not match Exercise.TYPE_ARPEGGIO (" + String.valueOf(Exercise.TYPE_ARPEGGIO) + ") or Exercise.TYPE_SCALE (" + String.valueOf(Exercise.TYPE_SCALE) + ")";
            throw new AssertionError(errorMessage);
        }

        for(int i=0; i<array.length(); ++i){
            obj = (JSONObject)array.get(i);
            name = obj.getString("name");
            hint = obj.getString("hint");
            keys = obj.getJSONArray("keys");

            if(!allExercisesOfType.containsKey(name))
                throw new AssertionError("Error in ExerciseCheckboxDelegate.setExerciseFromJSONArray: " + name + " is not included in allExercisesOfType");
            exerciseList = allExercisesOfType.get(name);

            for(int key=0; key<keys.length(); ++key){
                //If exercise exists within exerciseList then add the one in exercise list. Making use of currently existing exercises.
                ex = new Exercise(keys.getString(key), name, type, hint);
                if(exerciseList.contains(ex)){
                    int index = exerciseList.indexOf(ex);
                    m_selectableExerciseData.addExercise(exerciseList.get(index));
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