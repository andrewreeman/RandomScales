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
        Scales = new ArrayList<>();
        Arpeggios = new ArrayList<>();
    }

    public SelectableExercises_Data(Parcel source){
        Scales = new ArrayList<>();
        Arpeggios = new ArrayList<>();
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
        if(Scales.contains(ex) || Arpeggios.contains(ex)) {
            if (ex.getType() == Exercise.TYPE_SCALE) {
                Scales.remove(ex);
            } else {
                Arpeggios.remove(ex);
            }
        }
        else{
            Log.d("Error", "Error in SelectableExercises_Data.removeExercise: Exercise " + ex.toString() + " is not contained within Scales or Arpeggios array");
        }
    }

    public ArrayList<Exercise> getScales(){ return Scales;}
    public ArrayList<Exercise> getArpeggios(){ return Arpeggios;}

    public void clear(){
        Scales.clear();
        Arpeggios.clear();
    }

    public JSONObject toJSON(String presetName){
        JSONObject exercises = new JSONObject();
        try {
            exercises.put(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_preset_name), presetName);
            exercises.put(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_scales), exerciseListToJson(Scales));
            exercises.put(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_arps), exerciseListToJson(Arpeggios));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    private JSONArray exerciseListToJson(ArrayList<Exercise> exList){
        //tonalityToExerciseMap maps the tonality names to the exercises.
        HashMap<String, JSONObject> tonalityToExerciseMap = new HashMap<>();
        JSONArray exercises = new JSONArray();
        try{
            for (Exercise ex : exList ) {
                if (!tonalityToExerciseMap.containsKey(ex.getName())) {
                    JSONObject newTonalityData = new JSONObject();

                    newTonalityData.put(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_name), ex.getName());
                    newTonalityData.put(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_hint), ex.getHint());
                    newTonalityData.put(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_keyArray), new JSONArray());
                    tonalityToExerciseMap.put(ex.getName(), newTonalityData);
                }
                JSONObject tonalityData = tonalityToExerciseMap.get(ex.getName());
                JSONArray tonalityKeys = tonalityData.getJSONArray(MainActivity.resources.getString(R.string.com_stepwise_random_scales_JSONKeys_keyArray));
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
