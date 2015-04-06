package com.stepwise.random_scales;

//import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Array;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;


public class Presets extends Activity implements OnClickListener {



    private SelectableExercises_Data m_selectableExercises;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presets);
        if(savedInstanceState != null) {

        }
        else{
            m_selectableExercises = new SelectableExercises_Data();
            buildTable();
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


    private void buildTable(){

        TableLayout exerciseTable = (TableLayout) findViewById(R.id.Presets_TableLayout);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> notesString = new ArrayList<String>( Arrays.asList( getResources().getStringArray(R.array.Notes)));
        lp.setMargins(0, 30, 0, 30);

        ArrayList<String> exercisesString = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Scales)));
        int[] noteColors = getResources().getIntArray(R.array.PresetCheckBoxColors);
        String largestString = "";

        for (String exerciseString : exercisesString) {
            TableRow row = new TableRow(this);
            TextView exercise = new TextView(this);
            exercise.setText(exerciseString + "    ");
            largestString = exerciseString.length() > largestString.length() ? exerciseString : largestString;

            row.addView(exercise);
            for(int i=0; i<noteColors.length; ++i){
                CheckBox checkBox = new CheckBox(this);
                checkBox.setBackgroundColor(noteColors[i]);
                Exercise exerciseInfo = new Exercise(notesString.get(i), exerciseString, Exercise.TYPE_SCALE, "nothing");
                checkBox.setTag(exerciseInfo);

                checkBox.setOnClickListener(this);
                row.addView(checkBox);
            }
            row.setLayoutParams(lp);
            exerciseTable.addView(row);
        }
        buildTableHeader(largestString, notesString);
    }

    private void buildTableHeader(String largestString, ArrayList<String> notesString){



        TableLayout headerTable = (TableLayout)findViewById(R.id.Presets_Header);
        TableRow row = (TableRow)headerTable.getChildAt(0);
        TableRow emptyRow = new TableRow(this);
        TextView emptyText = (TextView)row.getChildAt(0);

        emptyText.setText(largestString + "   ");
        emptyText.setVisibility(View.INVISIBLE);


        CheckBox checkBox = new CheckBox(this);
        checkBox.setHeight(1);
        emptyRow.setVisibility(View.INVISIBLE);
        emptyRow.addView(checkBox);
        for(int i=0; i<notesString.size(); ++i){

            TextView note = new TextView(this);
            note.setGravity(Gravity.CENTER);
            note.setText(notesString.get(i));
            row.addView(note);
            checkBox = new CheckBox(this);
            checkBox.setHeight(1);
            emptyRow.addView(checkBox);
        }

        headerTable.addView(emptyRow);
    }


    @Override
    public void onClick(View v) {
        CheckBox checkBox = (CheckBox)v;
        Exercise exercise = (Exercise)checkBox.getTag();

        if(checkBox.isChecked())
            m_selectableExercises.AddExercise(exercise);
        else
            m_selectableExercises.RemoveExercise(exercise);
    }

    @Override
    public void onStop(){

    //TODO pass data back to mainactivity....use shared preferences or bundles?


        super.onStop();
    }

    public void onSubmitClicked(View v){
        Dialog dialog = new Dialog(this);
        dialog.show();
        this.finish();
    }


}
