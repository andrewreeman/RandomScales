package com.stepwise.random_scales;

/**
 * Created by andy on 03/04/15.
 */
public class Exercise {

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
}
