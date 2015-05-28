package com.stepwise.random_scales;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by andy on 27/05/15.
 */
public class PresetReadWriter {

    private JSONObject m_presets;
    private String m_fileName;

    public PresetReadWriter(Context context, String fileName) throws JSONException {
        //TODO redo errors
        StringBuffer inBuffer = new StringBuffer();
        try {
            m_fileName = fileName;
            BufferedReader inFile = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));
            String inString;

            while ((inString = inFile.readLine()) != null) {
                inBuffer.append(inString);
            }
            //TODO next have dialog for saving preset name then select from multiple names
            m_presets = new JSONObject(inBuffer.toString());
            inFile.close();
            return;
        }
        catch (FileNotFoundException e){
            m_presets = new JSONObject();
            m_presets.put("presets", new JSONArray());
            Log.d("PresetReadWriter()", "Error finding file: " + e.toString());
        }
        catch (IOException e) {
            Log.d("readPresetFile", "Error writing to pupil_details.json: " + e.getMessage());
        }


    }
    public ArrayList<String> getPresetNames() {
        ArrayList<String> presetNames = new ArrayList<>();

        try {
            JSONArray jsonNames = m_presets.getJSONArray("presets");
            JSONObject obj;
            for (int i = 0; i < jsonNames.length(); ++i) {
                obj = (JSONObject) jsonNames.get(i);
                //TODO do not use literals
                presetNames.add((String) obj.get("preset_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return presetNames;
    }

    public JSONObject getPreset(String presetName){
        //TODO what if error
        try {
            JSONArray presets = m_presets.getJSONArray("presets");
            for(int i=0; i<presets.length(); ++i){
                JSONObject obj = (JSONObject)presets.get(i);
                if( obj.getString("preset_name").equals(presetName) ){
                    return obj;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public Boolean doesPresetExist(String preset){
        //TODO check for if preset exists
        return getPresetNames().contains(preset);
    }
//TODO change order of scales and arpeggios
    public void savePreset(Context context, JSONObject newPreset) {
        try {
            JSONArray presets = m_presets.getJSONArray("presets");
            String presetName = newPreset.getString("preset_name");
            if( doesPresetExist(presetName) ){
                presets = replacePreset(newPreset, presets);
            }
            else{
                presets.put(newPreset);
            }
            m_presets.put("presets", presets);

            FileOutputStream fileOutputStream = context.openFileOutput(m_fileName, Context.MODE_PRIVATE);
            fileOutputStream.write((m_presets.toString() + "\n").getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.d("onSavePresetClicked", "Error opening file: " + e.getMessage());
        } catch (IOException e) {
            Log.d("onSavePresetClicked", "Error writing to pupil_details.json: " + e.getMessage());
        } catch (JSONException e) {
            Log.d("onSavePresetClicked", "Error parsing JSON file" + e.getMessage());
        }
    }

    public JSONArray replacePreset(JSONObject newPreset, JSONArray presets) throws JSONException {
        JSONArray newPresets = new JSONArray();
        JSONObject obj;
        String newPresetName = newPreset.getString("preset_name");

        for (int i = 0; i < presets.length(); ++i) {
            obj = presets.getJSONObject(i);
            if(obj.getString("preset_name").equals(newPresetName)){
                obj = newPreset;
            }
            newPresets.put(obj);
        }
        return newPresets;
    }

}
