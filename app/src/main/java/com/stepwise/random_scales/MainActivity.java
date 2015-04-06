package com.stepwise.random_scales;

import android.content.Intent;
import android.content.res.Resources;
//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

//Not ActionBarActivity ...
public class MainActivity extends Activity {

    String m_SelectedString;
    public static final int REQUEST_CODE__GET_PRESETS = 0;
    SelectableExercises_Data m_selectableExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        /*Resources res = getResources();
        String[] notes = res.getStringArray(R.array.Notes);
        Random randGen = new Random();
        int note = randGen.nextInt(notes.length);
        String[] scales = res.getStringArray(R.array.Scales);
        int scale = randGen.nextInt(scales.length);
        String outScale = scales[scale];
        m_SelectedString = notes[note] + " " + outScale;
        setText(m_SelectedString);*/



    }

    public void generateArpeggio(View view){

        Resources res = getResources();
        String[] notes = res.getStringArray(R.array.Notes);
        Random randGen = new Random();
        int note = randGen.nextInt(notes.length);
        String[] arpeggios = res.getStringArray(R.array.Arpeggios);
        int arpeggio = randGen.nextInt(arpeggios.length);

        m_SelectedString = notes[note] + " " + arpeggios[arpeggio];
        setText(m_SelectedString);
    }
//TODO buildExerciseList from preset function ... default preset is ALL
//TODO preset activity is for modifying presets

    public void setSelectableExercise_Data(String exercise){
        //TODO select exercise from xml or json (JSON JSON!!!) file. atm just selects all exercises


        String[] notes = getResources().getStringArray(R.array.Notes);
        String[] scales = getResources().getStringArray(R.array.Scales);
        String[] arps = getResources().getStringArray(R.array.Arpeggios);

        m_selectableExercises.clear();
        for(String note : notes){
            for(String scale : scales){
                Exercise ex = new Exercise(note, scale, Exercise.TYPE_SCALE, "");
                m_selectableExercises.addExercise(ex);
            }
            for(String arp : arps){
                Exercise ex = new Exercise(note, arp, Exercise.TYPE_ARPEGGIO, "");
                m_selectableExercises.addExercise(ex);
            }
        }

    }

    public void startChangePreset(){
        Intent intent = new Intent(this, Presets.class);
        startActivityForResult(intent, REQUEST_CODE__GET_PRESETS);
    }

    private void setText(String text){
        TextView textView = (TextView)findViewById(R.id.randResult);
        textView.setText(text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE__GET_PRESETS){
            if(resultCode == RESULT_OK){
                SelectableExercises_Data exData = data.getParcelableExtra(getString(R.string.com_stepwise_random_scales_presetList));
                ArrayList<Exercise> scales = exData.getScales();
                if(scales.size() > 0){
                    setText(scales.get( scales.size() - 1 ).getName());
                }
            }
        }


    }

}
