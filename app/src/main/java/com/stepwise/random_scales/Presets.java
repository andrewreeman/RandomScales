package com.stepwise.random_scales;

//import android.support.v7.app.ActionBarActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Presets extends Activity implements AdapterView.OnItemSelectedListener{

    private SelectableExercises_Data m_selectableExercises;
    private long m_idSelectedPresetType;
    //TODO less of a monster class

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
            //m_selectableExercises = new SelectableExercises_Data();
            m_selectableExercises = getIntent().getParcelableExtra(getString(R.string.com_stepwise_random_scales_presetList));
            fillAllScalesAndArps();
            //m_selectableExercises = initFromIntent(getIntent());
            m_exerciseCheckBoxDelegate = new ExerciseCheckboxDelegate();
            m_exerciseCheckBoxDelegate.setModel(m_selectableExercises);

            Spinner spinner  = (Spinner)findViewById(R.id.scaleOrArp);
            spinner.setOnItemSelectedListener(this);
            m_idSelectedPresetType = spinner.getSelectedItemId();

            initPresetSpinner();

            Button toggleClear = (Button)findViewById(R.id.clearChecks);
            toggleClear.setTag(true);
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


        if(parent.getId() == R.id.scaleOrArp) {
            TableLayout exerciseTable = (TableLayout) findViewById(R.id.Presets_TableLayout);
            exerciseTable.removeAllViews();

            //TODO better exercise TYPE layout
            m_exerciseCheckBoxDelegate.clear();
            if (pos == 0) {
                buildTable(m_allScales);
                m_exerciseCheckBoxDelegate.updateScales();
            } else {
                buildTable(m_allArps);
                m_exerciseCheckBoxDelegate.updateArps();
            }
        }
        else {
            Log.d("", (String)parent.getItemAtPosition(pos));
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
        switch(id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
            case R.id.home:
            case R.id.homeAsUp:
            case R.id.up:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    private SelectableExercises_Data initFromIntent(Intent intent){
        SelectableExercises_Data intentData = getIntent().getParcelableExtra(getString(R.string.com_stepwise_random_scales_presetList));

        ArrayList<Exercise>


        return new SelectableExercises_Data();
    }*/

    private ArrayList<String> getPresetNames(){
        ArrayList<String> presets = new ArrayList<>();

        presets.add("this is another test");
        presets.add("and another");

        Log.d("File contents:", readPresetFile());

        return presets;
    }

    private String readPresetFile() {
        StringBuffer inBuffer = new StringBuffer();
        String fileName = getString(R.string.com_stepwise_random_scales_scalePresetFile);
        try {
            BufferedReader inFile = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
            String inString;

            while ((inString = inFile.readLine()) != null) {
                inBuffer.append(inString + "\n");
            }
            inFile.close();
        } catch (FileNotFoundException e) {
            Log.d("readPresetFile", "Error opening file: " + e.getMessage());
        } catch (IOException e) {
            Log.d("readPresetFile", "Error writing to pupil_details.json: " + e.getMessage());
        }
        return inBuffer.toString();
    }

    private void initPresetSpinner(){
        Spinner spinner = (Spinner)findViewById(R.id.presets);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arrayAdapter.addAll(getPresetNames());

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

    }



    private void buildTable(HashMap<String, ArrayList<Exercise>> allExercises){

        TableLayout exerciseTable = (TableLayout) findViewById(R.id.Presets_TableLayout);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ArrayList<Exercise> exercises;
        //ArrayList<String> exercisesString;

       /* if(exerciseType == Exercise.TYPE_SCALE)
            exercisesString = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Scales)));
        else
            exercisesString = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Arpeggios))); */
        lp.setMargins(0, 30, 0, 30);

        int[] noteColors = getResources().getIntArray(R.array.PresetCheckBoxColors);
        String largestString = "";

        for (String exerciseString : allExercises.keySet()) {
            TableRow row = new TableRow(this);
            TextView exerciseName = new TextView(this);
            exerciseName.setText(exerciseString + "    ");
            largestString = exerciseString.length() > largestString.length() ? exerciseString : largestString;
            exercises = allExercises.get(exerciseString);
            row.addView(exerciseName);
            for(int i=0; i<exercises.size(); ++i){
                CheckBox checkBox = new CheckBox(this);
                checkBox.setBackgroundColor(noteColors[i]);
                m_exerciseCheckBoxDelegate.setExerciseCheckboxLink(exercises.get(i), checkBox);
                //Exercise exerciseInfo = new Exercise(notesString.get(i), exerciseString, exerciseType, "nothing");

                //checkBox.setTag(exerciseInfo);

                //checkBox.setOnClickListener(this);
                row.addView(checkBox);
            }
            row.setLayoutParams(lp);
            exerciseTable.addView(row);
        }
        buildTableHeader(largestString);

    }

    private void fillAllScalesAndArps(){
        String[] notesString = getResources().getStringArray(R.array.Notes);
        String[] scaleTypes = getResources().getStringArray(R.array.Scales);
        String[] arpTypes = getResources().getStringArray(R.array.Arpeggios);
        ArrayList<Exercise> selectedScales = m_selectableExercises.getScales();
        ArrayList<Exercise> selectedArps = m_selectableExercises.getArpeggios();
        m_allScales = new HashMap<>();
        m_allArps = new HashMap<>();

        for(String scale : scaleTypes){
            ArrayList<Exercise> exercises = new ArrayList<>();
            for(String note : notesString) {
                Exercise ex = new Exercise(note, scale, Exercise.TYPE_SCALE, "nothing");
                if(selectedScales.contains(ex)){
                    int index = selectedScales.indexOf(ex);
                    ex = selectedScales.get(index);
                }
                exercises.add(ex);
            }
            m_allScales.put(scale, exercises);
        }
        for(String arp : arpTypes){
            ArrayList<Exercise> exercises = new ArrayList<>();
            for(String note : notesString) {
                Exercise ex = new Exercise(note, arp, Exercise.TYPE_ARPEGGIO, "nothing");
                if(selectedArps.contains(ex)){
                    int index = selectedArps.indexOf(ex);
                    ex = selectedArps.get(index);
                }
                exercises.add(ex);
            }
            m_allArps.put(arp, exercises);
        }
    }

    private void buildTableHeader(String largestString){

        ArrayList<String> notesString = new ArrayList<>( Arrays.asList( getResources().getStringArray(R.array.Notes)));
        TableLayout headerTable = (TableLayout)findViewById(R.id.Presets_Header);
        headerTable.removeAllViews();

        //TableRow row = (TableRow)headerTable.getChildAt(0);
        TableRow row = new TableRow(this);
        TableRow emptyRow = new TableRow(this);
        //TextView emptyText = (TextView)row.getChildAt(0);
        TextView emptyText = new TextView(this);

        emptyText.setText(largestString + "   ");
        emptyText.setVisibility(View.INVISIBLE);
        row.addView(emptyText);

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


        //TODO ...use layouts better to avoid this silly code
        headerTable.addView(row);
        headerTable.addView(emptyRow);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.com_stepwise_random_scales_presetList), m_selectableExercises);
        setResult(RESULT_OK, intent);
        finish();
    }

    //TODO send selectable exercises to presets when preset clicked


    public void onSavePresetClicked(View v) {
        JSONObject currentPreset = m_selectableExercises.toJSON("presetName");
        Log.d("Preset write: ", currentPreset.toString());

        try{
            FileOutputStream fileOutputStream = openFileOutput(getString(R.string.com_stepwise_random_scales_scalePresetFile), MODE_PRIVATE);
            fileOutputStream.write( (currentPreset.toString() + "\n").getBytes() );
            fileOutputStream.close();
        }
        catch(FileNotFoundException e){
            Log.d("onSavePresetClicked", "Error opening file: " + e.getMessage());
        }
        catch (IOException e){
            Log.d("onSavePresetClicked", "Error writing to pupil_details.json: " + e.getMessage());
        }
    }



    public void onClearClicked(View v){
        Boolean isClearState = (Boolean)v.getTag();
        if(isClearState) {
            m_exerciseCheckBoxDelegate.deselectAllCheckBoxes();
            v.setTag(false);
        }
        else{
            m_exerciseCheckBoxDelegate.selectAllCheckBoxes();
            v.setTag(true);
        }




        //m_exerciseCheckBoxDelegate.deselectAllCheckBoxes();

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



