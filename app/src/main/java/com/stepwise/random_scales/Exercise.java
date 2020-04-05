package com.stepwise.random_scales;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exercise implements Parcelable {

    public class InvalidKeyException extends Exception {}

    /**
     * The type of exercise to perform.
     */
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

    final private String m_key;
    final private String m_exerciseName;
    final private String m_exerciseHint;
    final private ExerciseType m_type;

    /**
     *
     * @param key The key that the exercise is in. Must be a letter from a-g uppercase or lowercase and be preceded by nothing, a '#' or a 'b'
     * @param name The name of the exercise: e.g Dorian
     * @param type The type of exercise
     * @param hint A hint or description on how to perform this exercise
     * @throws InvalidKeyException This is thrown when the key is not a musical key
     */
    public Exercise(String key, String name, ExerciseType type, String hint) throws InvalidKeyException {
        validateKey(key);
        m_key = key;
        m_type = type;
        m_exerciseName = name;
        m_exerciseHint = hint;
    }

    public String getKey(){return m_key;}
    public String getName(){return m_exerciseName;}
    public String getHint(){return m_exerciseHint;}
    public ExerciseType getType(){ return m_type; }

    /**
     * Checks that the key is a valid musical key
     * @param key The key to check
     * @throws InvalidKeyException thrown if the key is not a valid musical key
     */
    private void validateKey(String key) throws InvalidKeyException {
        Pattern pattern = Pattern.compile("[a-gA-G](#|b)?");
        Matcher matcher = pattern.matcher(key);
        if(!(matcher.matches())) {
            Log.d("Exercise.validateKey", key + " is not a valid note");
            throw new InvalidKeyException();
        }
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Exercise)) return false;
        Exercise otherEx = (Exercise)other;
        return this.getHint().equals(otherEx.getHint()) && this.getKey().equals(otherEx.getKey()) && this.getType() == otherEx.getType() && this.getName().equals(otherEx.getName());
    }

    @Override
    public String toString(){
        return getName() + "\n" + getKey() + "\n" + getHint() + "\n" + getType();
    }

    // Parcelable methods

    /** Nobody knows what this does... */
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

            try {
                return new Exercise(key, name, exerciseType, hint);
            } catch (InvalidKeyException e) {
                Log.e("Exercise", e.getMessage());
                return null;
            }
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }

    };
}

