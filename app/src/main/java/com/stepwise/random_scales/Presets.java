package com.stepwise.random_scales;

//import android.support.v7.app.ActionBarActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Presets extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener{

    private SelectableExercises_Data m_selectableExercises;
    private long m_idSelectedPresetType;
    //TODO less of a monster class
    //TODO delagate handle checkbox exercise data mapping
    private ExerciseCheckboxDelegate m_exerciseCheckBoxDelegate;
    private HashMap<String, ArrayList<Exercise>> m_allScales;
    private HashMap<String, ArrayList<Exercise>> m_allArps;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presets);
        if(savedInstanceState != null) {

        }
        else{
            fillAllScalesAndArps();
            m_selectableExercises = new SelectableExercises_Data();
            //ArrayList<String> AllScales = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Scales)));
            //buildTable(AllScales);

            Spinner spinner  = (Spinner)findViewById(R.id.spinner);
            spinner.setOnItemSelectedListener(this);
            m_idSelectedPresetType = spinner.getSelectedItemId();
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
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

        TableLayout exerciseTable = (TableLayout) findViewById(R.id.Presets_TableLayout);

        exerciseTable.removeAllViews();
        if(pos == 0) {
            buildTable(Exercise.TYPE_SCALE);
            setTableCheckBoxes(m_selectableExercises.getScales());
        }
        else {
            buildTable(Exercise.TYPE_ARPEGGIO);
            setTableCheckBoxes(m_selectableExercises.getArpeggios());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){}

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


    private void buildTable(int exerciseType){

        TableLayout exerciseTable = (TableLayout) findViewById(R.id.Presets_TableLayout);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> notesString = new ArrayList<String>( Arrays.asList( getResources().getStringArray(R.array.Notes)));
        ArrayList<String> exercisesString;

        if(exerciseType == Exercise.TYPE_SCALE)
            exercisesString = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Scales)));
        else
            exercisesString = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Arpeggios)));
        lp.setMargins(0, 30, 0, 30);

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
                Exercise exerciseInfo = new Exercise(notesString.get(i), exerciseString, exerciseType, "nothing");
                checkBox.setTag(exerciseInfo);
//TODO have all scales and all arpeggios owned by Presets object. This allows persistance when table is destroyed.
                checkBox.setOnClickListener(this);
                row.addView(checkBox);
            }
            row.setLayoutParams(lp);
            exerciseTable.addView(row);
        }
        buildTableHeader(largestString, notesString);
    }

    private void fillAllScalesAndArps(){
        ArrayList<String> notesString = new ArrayList<>( Arrays.asList( getResources().getStringArray(R.array.Notes)));
        ArrayList<String> scaleTypes = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Scales)));
        ArrayList<String> arpTypes = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Arpeggios)));
        m_allScales = new HashMap<>();
        m_allArps = new HashMap<>();

        for(String scale : scaleTypes){
            ArrayList<Exercise> exercises = new ArrayList<>();
            for(String note : notesString) {
                Exercise ex = new Exercise(note, scale, Exercise.TYPE_SCALE, "nothing");
                exercises.add(ex);
            }
            m_allScales.put(scale, exercises);
        }
        for(String arp : arpTypes){
            ArrayList<Exercise> exercises = new ArrayList<>();
            for(String note : notesString) {
                Exercise ex = new Exercise(note, arp, Exercise.TYPE_ARPEGGIO, "nothing");
                exercises.add(ex);
            }
            m_allArps.put(arp, exercises);
        }
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

    private void setTableCheckBoxes(ArrayList<Exercise> exercises){
        TableLayout exerciseTable = (TableLayout) findViewById(R.id.Presets_TableLayout);

        for(Exercise ex : exercises){


        }

    }


    @Override
    public void onClick(View v) {
        CheckBox checkBox = (CheckBox)v;
        Exercise exercise = (Exercise)checkBox.getTag();

        if(checkBox.isChecked())
            m_selectableExercises.addExercise(exercise);
        else
            m_selectableExercises.removeExercise(exercise);
    }


    public void onSubmitClicked(View v){

     /*   TextView textView = (TextView)findViewById(R.id.DEBUG);

        ArrayList<Exercise> scales = m_selectableExercises.getScales();
        if(scales.size() > 0){
            textView.setText(scales.get(scales.size() - 1).getName());
        }
        else{
            textView.setText("Size is zero");
        }*/


        Intent intent = new Intent();
        intent.putExtra(getString(R.string.com_stepwise_random_scales_presetList), m_selectableExercises);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void onSavePresetClicked(View v) {
        m_selectableExercises.toJSON("presetName");
    }


}
         /*TODO move this over to app. exerciseList class has a scalesTOJSON and arpeggiosTOJSON methods



        JSONArray scalesToJSON(){
            return exerciseListToJSON(m_scales);
        }
        JSONArray arpsToJSON(){
            return exerciseListToJSON(m_arps);
        }

        JSONArray exerciseListToJSON(ArrayList<Exercises> exList){
            Map<String, JSONObject> tonalityToExerciseMap = new ...

            for(Exercise ex : exList){

                // new tonality
                if(!tonalityToExerciseMap.containsKey(ex.name){
                    JSONObject newTonalityData = new JSONObject();
                     newTonalityData.put("name", ex.name);
                     newTonalityData.put("hint", ex.hint);
                     newTonalityData.put("keys", new JSONArray());
                     tonalityToExerciseMap.put(ex.name, newTonalityData);
                }

                //append to keys
                JSONArray tonalityData = tonalityToExerciseMap.get(ex.name);
                tonalityData.add(ex.key);
                //IF not a pointer
                tonalityToExerciseMap.put(ex.name, tonalityData);
            }

            JSONArray exerciseList = new JSONArray();

            for(JSONObject tonality : tonalityToExerciseMap.values()){
                exerciseList.put(tonality);
            }

            return exerciseList;

        }
    } */



