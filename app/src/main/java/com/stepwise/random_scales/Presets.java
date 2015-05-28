//TODO if preset is modified then append its name with * this is temporary
// TODO set text hint in normal SavePreset as current preset

package com.stepwise.random_scales;

//import android.support.v7.app.ActionBarActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.ToggleButton;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Presets extends Activity implements AdapterView.OnItemSelectedListener{

    public static final Boolean CLEAR_CHECKBOXES = true;
    public static final Boolean CHECKALL_CHECKBOXES = false;
    private SelectableExercises_Data m_selectableExercises;
    private int m_selectedType;
    private ExerciseCheckboxDelegate m_exerciseCheckBoxDelegate;
    private LinkedHashMap<String, ArrayList<Exercise>> m_allScales; //HashMap does not preserve order. LinkedHashMap does
    private LinkedHashMap<String, ArrayList<Exercise>> m_allArps;
    private Boolean m_isCheckBoxTableBuilt;
    private PresetReadWriter m_presetReadWriter;

//TODO organise strings in different files
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presets);
        if (savedInstanceState != null) {

        }
        else{
            m_isCheckBoxTableBuilt = false;
            m_selectableExercises = getIntent().getParcelableExtra(getString(R.string.com_stepwise_random_scales_presetList));
            fillAllScalesAndArps();

            m_exerciseCheckBoxDelegate = new ExerciseCheckboxDelegate();
            m_exerciseCheckBoxDelegate.setModel(m_selectableExercises);

            Spinner spinner = (Spinner) findViewById(R.id.scaleOrArp);
            spinner.setOnItemSelectedListener(this);
            Button toggleClear = (Button) findViewById(R.id.clearChecks);
            toggleClear.setTag(CHECKALL_CHECKBOXES); //TODO give private finals (or const) for this. true is not clear... what is it true for
            m_selectedType = Exercise.TYPE_SCALE;

            try{
                m_presetReadWriter = new PresetReadWriter(this, getString(R.string.com_stepwise_random_scales_PresetFile));
                initPresetSpinner();
            }
            catch(JSONException e) {
                Log.d("Presets", "JSONException in Presets.onCreate: " + e.toString());
                m_presetReadWriter = null;
            }
            catch (IOException e){
                Log.d("Presets", "IOException in Presets.onCreate: " + e.toString());
                m_presetReadWriter = null;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        if(parent.getId() == R.id.scaleOrArp) {
            TableLayout exerciseTable = (TableLayout)findViewById(R.id.Presets_TableLayout);
            Button clearCheckToggle = (Button)findViewById(R.id.clearChecks);
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
            clearCheckToggle.setTag(CLEAR_CHECKBOXES);
            clearCheckToggle.setText("Clear");
        }
        else{
            if(m_presetReadWriter != null) {
                ArrayAdapter<String> presetNames = (ArrayAdapter<String>) parent.getAdapter();
                String presetName = presetNames.getItem(pos);

                m_exerciseCheckBoxDelegate.setFromJSON(m_presetReadWriter.getPreset(presetName), m_allScales, m_allArps);
                if (m_isCheckBoxTableBuilt) {
                    if (m_selectedType == Exercise.TYPE_SCALE) {
                        m_exerciseCheckBoxDelegate.updateScales();
                    } else {
                        m_exerciseCheckBoxDelegate.updateArps();
                    }
                }
            }
            else{
                Log.d("Presets", "Error in Presets.onItemSelected: m_presetReadWriter was not initialised");
            }
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
            case R.id.home:
            case R.id.homeAsUp:
            case R.id.up:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPresetSpinner(){
        Spinner spinner = (Spinner)findViewById(R.id.presets);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.addAll(m_presetReadWriter.getPresetNames());
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void fillAllScalesAndArps(){
        String[] notesString = getResources().getStringArray(R.array.Notes);
        String[] scaleTypes = getResources().getStringArray(R.array.Scales);
       String[] arpTypes = getResources().getStringArray(R.array.Arpeggios);
        ArrayList<Exercise> selectedScales = m_selectableExercises.getScales();
        ArrayList<Exercise> selectedArps = m_selectableExercises.getArpeggios();
        m_allScales = new LinkedHashMap<>();
        m_allArps = new LinkedHashMap<>();

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


    private void buildTable(LinkedHashMap<String, ArrayList<Exercise>> allExercises){
        m_isCheckBoxTableBuilt = false;
        TableLayout exerciseTable = (TableLayout) findViewById(R.id.Presets_TableLayout);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ArrayList<Exercise> exercises;
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
                row.addView(checkBox);
            }
            row.setLayoutParams(lp);
            exerciseTable.addView(row);
        }
        buildTableHeader(largestString);
        m_isCheckBoxTableBuilt = true;
    }

   private void buildTableHeader(String largestString){

        ArrayList<String> notesString = new ArrayList<>( Arrays.asList( getResources().getStringArray(R.array.Notes)));
        TableLayout headerTable = (TableLayout)findViewById(R.id.Presets_Header);
        headerTable.removeAllViews();

        TableRow row = new TableRow(this);
        TableRow emptyRow = new TableRow(this);
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

    }

    public void inputPresetNameFinished(String newPreset, Boolean overwrite) throws AssertionError{

        if(overwrite || !m_presetReadWriter.doesPresetExist(newPreset)){
            m_presetReadWriter.savePreset(this, m_selectableExercises.toJSON(newPreset));
        }
        else{
            OverwritePresetDialogFragment overwriteDialog = new OverwritePresetDialogFragment();
            overwriteDialog.setNewPresetName(newPreset);
            overwriteDialog.show(getFragmentManager(), "Overwrite");
        }
    }

    public void onClearClicked(View v){
        Boolean ClearState = (Boolean)v.getTag();
        Button button = (Button)v;
        if(ClearState == CLEAR_CHECKBOXES) {
            m_exerciseCheckBoxDelegate.deselectAllCheckBoxes();
            button.setTag(CHECKALL_CHECKBOXES);
            button.setText("All");
        }
        else{
            m_exerciseCheckBoxDelegate.selectAllCheckBoxes();
            button.setTag(CLEAR_CHECKBOXES);
            button.setText("Clear");
        }
    }
}
