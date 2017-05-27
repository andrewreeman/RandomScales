package com.stepwise.random_scales;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exercise implements Parcelable {
    public enum ExerciseType {
        SCALE,
        ARPEGGIO;

        public int toInt() {
            switch (this) {
                case SCALE: return 1;
                case ARPEGGIO: return 2;
                default: return 0;
            }
        }

        @Nullable
        public static ExerciseType fromInt(int i) {
            switch (i) {
                case 1: return SCALE;
                case 2: return ARPEGGIO;
                default: return null;
            }
        }
    }

    public static final int TYPE_SCALE = 0;
    public static final int TYPE_ARPEGGIO = 1;

    private String m_key;
    private String m_exerciseName;
    private String m_exerciseHint;
    private ExerciseType m_type;

    public Exercise(String key, String name, ExerciseType type, String hint){
        setKey(key);
        m_type = type;
        m_exerciseName = name;
        m_exerciseHint = hint;

    }

    public String getKey(){return m_key;}
    public String getName(){return m_exerciseName;}
    public String getHint(){return m_exerciseHint;}
    public ExerciseType getType(){ return m_type; }

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

    @Override
    public boolean equals(Object other){
        Exercise otherEx = (Exercise)other;
        return this.getHint().equals(otherEx.getHint()) && this.getKey().equals(otherEx.getKey()) && this.getType() == otherEx.getType() && this.getName().equals(otherEx.getName());
    }

    @Override
    public String toString(){
        return getName() + "\n" + getKey() + "\n" + getHint() + "\n" + getType();
    }

    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_key);
        dest.writeString(m_exerciseName);
        dest.writeInt(m_type.toInt());
        dest.writeString(m_exerciseHint);
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            String key = source.readString();
            String name = source.readString();

            int type = source.readInt();
            ExerciseType exerciseType = ExerciseType.fromInt(type);
            if(exerciseType == null) exerciseType = ExerciseType.SCALE;

            String hint = source.readString();

            return new Exercise(key, name, exerciseType, hint);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }


    };
}

