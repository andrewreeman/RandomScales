package com.stepwise.random_scales;

import android.content.Intent;
import android.content.res.Resources;
//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

//Not ActionBarActivity ...
public class MainActivity extends Activity {

    public static final int REQUEST_CODE__GET_PRESETS = 0;
    public static Resources resources;

    SelectableExercises_Data m_selectableExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        setContentView(R.layout.activity_main);
        m_selectableExercises = new SelectableExercises_Data();
        setSelectableExercise_Data("Default");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.presets:
                startChangePreset();
                return true;
            default:
                return super.onOptionsItemSelected(item);
       }
    }

    public void generateScale(View view){
        ArrayList<Exercise> scales = m_selectableExercises.getScales();
        if(scales.size() > 0) {
            Random randGen = new Random();
            Exercise scale = scales.get(randGen.nextInt(scales.size()));
            setText(scale.getKey() + " " + scale.getName());
        }
        else
            setText("No scales in set");
    }

    public void generateArpeggio(View view){

        ArrayList<Exercise> arpeggios = m_selectableExercises.getArpeggios();
        if(arpeggios.size() > 0) {
            Random randGen = new Random();
            Exercise arp = arpeggios.get(randGen.nextInt(arpeggios.size()));
            setText(arp.getKey() + " " + arp.getName());
        }
        else
            setText("No arpeggios in set");
    }


    public void setSelectableExercise_Data(String preset){

        String[] notes = getResources().getStringArray(R.array.Notes);
        String[] scales = getResources().getStringArray(R.array.Scales);
        String[] arps = getResources().getStringArray(R.array.Arpeggios);

        m_selectableExercises.clear();
        for(String note : notes){
            for(String scale : scales){
                Exercise ex = new Exercise(note, scale, Exercise.ExerciseType.SCALE, "nothing");
                m_selectableExercises.addExercise(ex);
            }
            for(String arp : arps){
                Exercise ex = new Exercise(note, arp, Exercise.ExerciseType.ARPEGGIO, "nothing");
                m_selectableExercises.addExercise(ex);
            }
        }
    }

    public void startChangePreset(){
        Intent intent = new Intent(this, Presets.class);
        intent.putExtra(getString(R.string.com_stepwise_random_scales_presetList), m_selectableExercises);
        startActivityForResult(intent, REQUEST_CODE__GET_PRESETS);
    }

    private void setText(String text){
        TextView textView = (TextView)findViewById(R.id.randResult);
        textView.setText(text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE__GET_PRESETS){
            if(resultCode == RESULT_OK)
                m_selectableExercises = data.getParcelableExtra(getString(R.string.com_stepwise_random_scales_presetList));
            else
                Log.d("MainActivity", "MainActivity.onActivityResult result was not ok. Result was: " + String.valueOf(resultCode));
        }
    }

}
