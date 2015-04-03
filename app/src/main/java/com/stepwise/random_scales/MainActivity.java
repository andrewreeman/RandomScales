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

import java.util.Random;

//Not ActionBarActivity ...
public class MainActivity extends Activity {

    String m_SelectedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Resources res = getResources();
        String[] notes = res.getStringArray(R.array.Notes);
        Random randGen = new Random();
        int note = randGen.nextInt(notes.length);
        String[] scales = res.getStringArray(R.array.Scales);
        int scale = randGen.nextInt(scales.length);
        String outScale = scales[scale];
        if(scale == 0){
            String[] modes = res.getStringArray(R.array.Modes);
            int mode = randGen.nextInt(modes.length);
            outScale = modes[mode];
        }
        m_SelectedString = notes[note] + " " + outScale;
        setText(m_SelectedString);
    }

    public void generateArpeggio(View view){

        Resources res = getResources();
        String[] notes = res.getStringArray(R.array.Notes);
        Random randGen = new Random();
        int note = randGen.nextInt(notes.length);
        String[] arpeggios = res.getStringArray(R.array.arpeggios);
        int arpeggio = randGen.nextInt(arpeggios.length);

        m_SelectedString = notes[note] + " " + arpeggios[arpeggio];
        setText(m_SelectedString);
    }



    public void startChangePreset(){
        Intent intent = new Intent(this, Presets.class);
        startActivity(intent);
    }

    private void setText(String text){
        TextView textView = (TextView)findViewById(R.id.randResult);
        textView.setText(m_SelectedString);
    }
}
