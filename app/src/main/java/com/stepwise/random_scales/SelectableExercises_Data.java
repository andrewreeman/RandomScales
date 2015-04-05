package com.stepwise.random_scales;

import java.util.ArrayList;

/**
 * Created by andy on 03/04/15.
 */
public class SelectableExercises_Data {

    private ArrayList<Exercise> Scales;
    private ArrayList<Exercise> Arpeggios;

    public SelectableExercises_Data(){
        Scales = new ArrayList<Exercise>();
        Arpeggios = new ArrayList<Exercise>();
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
}
