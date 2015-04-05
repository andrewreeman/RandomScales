package com.stepwise.random_scales;

/**
 * Created by andy on 03/04/15.
 */
public class Exercise {
    private String m_key;
    private String m_exerciseName;
    private String m_exerciseHint;

    public Exercise(String key, String name, String hint){
        m_key = key;
        m_exerciseName = name;
        m_exerciseHint = hint;
    }

    public String getKey(){return m_key;}
    public String getName(){return m_exerciseName;}
    public String getHint(){return m_exerciseHint;}
}
