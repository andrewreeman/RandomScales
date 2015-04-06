package com.stepwise.random_scales;

import android.os.Parcel;
import android.os.Parcelable;

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
        m_key = key;
        m_exerciseName = name;
        m_type = type;
        m_exerciseHint = hint;
    }

    public String getKey(){return m_key;}
    public String getName(){return m_exerciseName;}
    public String getHint(){return m_exerciseHint;}
    public int getType(){ return m_type; }

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

            return new Exercise(key,name,type,hint);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}

