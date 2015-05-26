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
//TODO this should be part of a Preset Manager class
    public void setFromJSON(JSONObject obj, HashMap<String, ArrayList<Exercise>> allScales, HashMap<String, ArrayList<Exercise>> allArps){
        JSONArray scales;
        JSONArray arps;
        JSONArray keys;
        String name;
        String hint;
        Exercise ex;
        ArrayList<Exercise> exerciseList;

        try{
            deselectAllCheckBoxes();
            m_selectableExerciseData.clear();

            scales = obj.getJSONArray("scales");
            arps = obj.getJSONArray("arps");

            for(int i=0; i<scales.length(); ++i){
                obj = (JSONObject)scales.get(i);
                name = obj.getString("name");
                hint = obj.getString("hint");
                keys = obj.getJSONArray("keys");
                exerciseList = allScales.get(name);

                for(int key=0; key<keys.length(); ++key){
                    ex = new Exercise(keys.getString(key), name, Exercise.TYPE_SCALE, hint);
                    if(exerciseList.contains(ex)){
                        int index = exerciseList.indexOf(ex);
                        m_selectableExerciseData.addExercise(exerciseList.get(index));
                    }


                    //m_selectableExerciseData.addExercise(ex);
                }
            }

            for(int i=0; i<arps.length(); ++i) {
                obj = (JSONObject) arps.get(i);
                name = obj.getString("name");
                hint = obj.getString("hint");
                keys = obj.getJSONArray("keys");
                exerciseList = allArps.get(name);

                for (int key = 0; key < keys.length(); ++key) {
                    ex = new Exercise(keys.getString(key), name, Exercise.TYPE_ARPEGGIO, hint);
                    if(exerciseList.contains(ex)){
                        int index = exerciseList.indexOf(ex);
                        m_selectableExerciseData.addExercise(exerciseList.get(index));
                    }
                }

            }
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
        CheckBox check = m_exerciseCheckBoxMap.get(ex);
        if( check != null)
            check.setChecked(isChecked);
    }

}

