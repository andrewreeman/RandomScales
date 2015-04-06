package com.stepwise.random_scales;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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

    public SelectableExercises_Data(Parcel source) {
        source.readTypedList(Scales, Exercise.CREATOR);
        source.readTypedList(Arpeggios, Exercise.CREATOR);

    }

    public void AddExercise(Exercise ex){
        if(ex.getType() == Exercise.TYPE_SCALE)
            Scales.add(ex);
        else
            Arpeggios.add(ex);
    }
    public void RemoveExercise(Exercise ex){
        if(ex.getType() == Exercise.TYPE_SCALE)
            Scales.remove(ex);
        else
            Arpeggios.remove(ex);
    }

    public ArrayList<Exercise> getScales(){ return Scales;}
    public ArrayList<Exercise> getArpeggios(){ return Arpeggios;}

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
