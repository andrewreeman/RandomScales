package com.stepwise.random_scales;

//import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.List;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class Presets extends Activity {

    private String m_selectedPreset = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presets);
        if(savedInstanceState != null) {

        }
        else{
           onPresetSelected("All");
        }
    }

    protected void onStart(){
        super.onStart();
/*      Dynamic creation of buttons.
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.presetFrame);
        Button button = new Button(this);
        button.setText("This is a test");
        frameLayout.addView(button); */


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_presets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void onPresetSelected(String preset){
        if(preset == m_selectedPreset){
            return;
        }
        TableLayout exerciseTable = (TableLayout)findViewById(R.id.Presets_TableLayout);

       //TODO Dynamically populate a table
        //TODO this creates the table at the moment. this should be in the onCreate method.
        //TODO what I want is to modify the table after creation

        TableRow notes = new TableRow(this);
        ArrayList<String> notesString = new ArrayList<String>( Arrays.asList( getResources().getStringArray(R.array.Notes)));

        notesString.add(0, "   ");
        for(String noteString : notesString){
            TextView note = new TextView(this);
            note.setText(noteString);
            notes.addView(note);
        }
        exerciseTable.addView(notes);
        notesString.remove(0); //now just all notes

        ArrayList<String> exercisesString = new ArrayList<String>( Arrays.asList( getResources().getStringArray(R.array.Scales)) );
        for(String exerciseString : exercisesString) {
            TableRow row = new TableRow(this);
            TextView exercise = new TextView(this);

            exercise.setText(exerciseString);
            row.addView(exercise);
            for(String notUsed : notesString) {
                ToggleButton toggle = new ToggleButton(this);
                row.addView(toggle);
            }
            exerciseTable.addView(row);
        }



    }
}
