package com.stepwise.random_scales;

//import android.support.v7.app.ActionBarActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;


import org.json.JSONException;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Presets extends Activity implements AdapterView.OnItemSelectedListener{

    private SelectableExercises_Data m_selectableExercises;
    private int m_selectedType;
//    private JSONObject m_presets;

    //TODO less of a monster class

    private ExerciseCheckboxDelegate m_exerciseCheckBoxDelegate;
    private HashMap<String, ArrayList<Exercise>> m_allScales;
    private HashMap<String, ArrayList<Exercise>> m_allArps;
    private Boolean m_isCheckBoxTableBuilt;

    private PresetReadWriter m_presetReadWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_presets);
            if (savedInstanceState != null) {

            } else {
                m_isCheckBoxTableBuilt = false;
                m_selectableExercises = getIntent().getParcelableExtra(getString(R.string.com_stepwise_random_scales_presetList));
                fillAllScalesAndArps();

                m_exerciseCheckBoxDelegate = new ExerciseCheckboxDelegate();
                m_exerciseCheckBoxDelegate.setModel(m_selectableExercises);

                Spinner spinner = (Spinner) findViewById(R.id.scaleOrArp);
                spinner.setOnItemSelectedListener(this);
                Button toggleClear = (Button) findViewById(R.id.clearChecks);
                toggleClear.setTag(true); //TODO give private finals (or const) for this. true is not clear... what is it true for
                m_selectedType = Exercise.TYPE_SCALE;
                m_presetReadWriter = new PresetReadWriter(this, getString(R.string.com_stepwise_random_scales_PresetFile));
                initPresetSpinner();
            }
        }
        catch(JSONException e) {
            Log.d("onCreate", "JSON error:" + e.toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
//TODO have other classes for onItemSelected for separate spinners

        if(parent.getId() == R.id.scaleOrArp) {
            TableLayout exerciseTable = (TableLayout) findViewById(R.id.Presets_TableLayout);
            exerciseTable.removeAllViews();

            m_exerciseCheckBoxDelegate.clear();
            if (pos == 0) {
                buildTable(m_allScales);
                m_exerciseCheckBoxDelegate.updateScales();
                m_selectedType = Exercise.TYPE_SCALE;
            } else {
                buildTable(m_allArps);
                m_exerciseCheckBoxDelegate.updateArps();
                m_selectedType = Exercise.TYPE_ARPEGGIO;
            }
        }
        else{
            ArrayAdapter<String> presetNames = (ArrayAdapter<String>) parent.getAdapter();
            String presetName = presetNames.getItem(pos);

            m_exerciseCheckBoxDelegate.setFromJSON(m_presetReadWriter.getPreset(presetName), m_allScales, m_allArps);
            if(m_isCheckBoxTableBuilt) {
                if (m_selectedType == Exercise.TYPE_SCALE) {
                    m_exerciseCheckBoxDelegate.updateScales();
                } else {
                    m_exerciseCheckBoxDelegate.updateArps();
                }
            }

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


    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return super.openFileInput(name);
    }

    private void initPresetSpinner(){
        Spinner spinner = (Spinner)findViewById(R.id.presets);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arrayAdapter.addAll(m_presetReadWriter.getPresetNames());

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
        m_isCheckBoxTableBuilt = true;
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


    public void onSavePresetClicked(View v) {
        InputPresetNameDialogFragment inputDialog = new InputPresetNameDialogFragment();

        inputDialog.show(getFragmentManager(), getString(R.string.com_stepwise_random_scales_InputPresetNameDialog));
        //TODO check preset file integrity
    }
    //TODO pass preset file name to Main then back to Presets to select correct preset on init.
    //TODO * is invalid for preset input
    //TODO if preset is modified then append its name with * this is temporary
    //TODO save last used preset on close
    //TODO define overwrite types...

    public void inputPresetNameFinished(String newPreset, Boolean overwrite){

        if(overwrite || !m_presetReadWriter.doesPresetExist(newPreset)){
            m_presetReadWriter.savePreset(this, m_selectableExercises.toJSON(newPreset));
        }
        else{
            OverwritePresetDialogFragment overwriteDialog = new OverwritePresetDialogFragment();
            overwriteDialog.setNewPresetName(newPreset);
            overwriteDialog.show(getFragmentManager(), "Overwrite");
            // TODO set text hint in normal SavePreset as current preset
        }
    }

    public void deleteFile(View v){
        deleteFile(getString(R.string.com_stepwise_random_scales_PresetFile));
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
    }
}
