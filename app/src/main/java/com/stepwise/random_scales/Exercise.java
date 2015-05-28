package com.stepwise.random_scales;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by andy on 03/04/15.
 */
public class Exercise implements Parcelable {

    public static final int TYPE_SCALE = 0;
    public static final int TYPE_ARPEGGIO = 1;

    private String m_key;
    private String m_exerciseName;
    private String m_exerciseHint;
    private int m_type;

    public Exercise(String key, String name, int type, String hint){
        setKey(key);
        setType(type);
        m_exerciseName = name;
        m_exerciseHint = hint;

    }

    public String getKey(){return m_key;}
    public String getName(){return m_exerciseName;}
    public String getHint(){return m_exerciseHint;}
    public int getType(){ return m_type; }

    private void setKey(String key){
        if(BuildConfig.DEBUG){
            //If A to G and b# or nothing
            Pattern pattern = Pattern.compile("[A-G][b#]?");
            Matcher matcher = pattern.matcher(key);
            if(!(matcher.matches()))
                Log.d("Exercise.setKey", key + " is not a valid note");
        }
        m_key = key;
    }

    private void setType(int inputType){
        if(BuildConfig.DEBUG && (inputType != Exercise.TYPE_ARPEGGIO && inputType != Exercise.TYPE_SCALE))
            Log.d("Exercise.setType", "type is not a valid exercise type");
        m_type = inputType;
    }


    @Override
    public boolean equals(Object other){
        Exercise otherEx = (Exercise)other;
        return this.getHint().equals(otherEx.getHint()) && this.getKey().equals(otherEx.getKey()) && this.getType() == otherEx.getType() && this.getName().equals(otherEx.getName());
    }

    @Override
    public String toString(){
        return getName() + "\n" + getKey() + "\n" + getHint() + "\n" + getType();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_key);
        dest.writeString(m_exerciseName);
        dest.writeInt(m_type);
        dest.writeString(m_exerciseHint);
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            String key = source.readString();
            String name = source.readString();
            int type = source.readInt();
            String hint = source.readString();

            return new Exercise(key, name, type, hint);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }


    };
}

