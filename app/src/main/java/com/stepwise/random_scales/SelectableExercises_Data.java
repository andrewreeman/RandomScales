package com.stepwise.random_scales;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andy on 03/04/15.
 */
public class SelectableExercises_Data implements Parcelable{

    private ArrayList<Exercise> Scales;
    private ArrayList<Exercise> Arpeggios;

    public SelectableExercises_Data(){
        Scales = new ArrayList<Exercise>();
        Arpeggios = new ArrayList<Exercise>();
    }

    public SelectableExercises_Data(Parcel source){
        Scales = new ArrayList<Exercise>();
        Arpeggios = new ArrayList<Exercise>();
        source.readTypedList(Scales, Exercise.CREATOR);
        source.readTypedList(Arpeggios, Exercise.CREATOR);
    }

    public void addExercise(Exercise ex){
        if(ex.getType() == Exercise.TYPE_SCALE)
            Scales.add(ex);
        else
            Arpeggios.add(ex);
    }
    public void removeExercise(Exercise ex){
        if(ex.getType() == Exercise.TYPE_SCALE)
            Scales.remove(ex);
        else
            Arpeggios.remove(ex);
    }

    public ArrayList<Exercise> getScales(){ return Scales;}
    public ArrayList<Exercise> getArpeggios(){ return Arpeggios;}

    public void clear(){
        Scales.clear();
        Arpeggios.clear();
    }

    public JSONObject toJSON(String presetName){
        JSONObject exercises = new JSONObject();

        //TODO don't use hardcoded strings

        try {
            exercises.put("preset_name", presetName);
            exercises.put("scales", exerciseListToJson(Scales));
            exercises.put("arps", exerciseListToJson(Arpeggios));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    private JSONArray exerciseListToJson(ArrayList<Exercise> exList){
        HashMap<String, JSONObject> tonalityToExerciseMap = new HashMap<>();
        JSONArray exercises = new JSONArray();
        try{
            for (Exercise ex : exList ) {
                if (!tonalityToExerciseMap.containsKey(ex.getName())) {
                    JSONObject newTonalityData = new JSONObject();

                    newTonalityData.put("name", ex.getName());
                    newTonalityData.put("hint", ex.getHint());
                    newTonalityData.put("keys", new JSONArray());
                    tonalityToExerciseMap.put(ex.getName(), newTonalityData);
                }
                JSONObject tonalityData = tonalityToExerciseMap.get(ex.getName());
                JSONArray tonalityKeys = tonalityData.getJSONArray("keys");
                tonalityKeys.put(ex.getKey());
            }

        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        for(JSONObject ex : tonalityToExerciseMap.values()){
            exercises.put(ex);
        }

        return exercises;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Scales);
        dest.writeTypedList(Arpeggios);
    }

    public static final Creator<SelectableExercises_Data> CREATOR = new Creator<SelectableExercises_Data>(){


        @Override
        public SelectableExercises_Data createFromParcel(Parcel source) {
            return new SelectableExercises_Data(source);
        }

        @Override
        public SelectableExercises_Data[] newArray(int size) {
            return new SelectableExercises_Data[size];
        }
    };




}
