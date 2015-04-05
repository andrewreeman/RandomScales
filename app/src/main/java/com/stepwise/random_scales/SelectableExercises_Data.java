package com.stepwise.random_scales;

import java.util.ArrayList;

/**
 * Created by andy on 03/04/15.
 */
public class SelectableExercises_Data {

    private ArrayList<Exercise> Scales;
    private ArrayList<Exercise> Arpeggios;

    public void AddScale(Exercise scale){ Scales.add(scale); }
    public void AddArpeggio(Exercise arpeggio){ Arpeggios.add(arpeggio);}
    public ArrayList<Exercise> getScales(){ return Scales;}
    public ArrayList<Exercise> getArpeggios(){ return Arpeggios;}
}
