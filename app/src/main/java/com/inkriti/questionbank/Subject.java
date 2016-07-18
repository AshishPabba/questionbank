package com.inkriti.questionbank;

/**
 * Created by vidha on 18-07-2016.
 */

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class Subject {
    public String id;
    public String name;

    public Subject(){

    }

    public Subject(String id, String name){
        this.id = id;
        this.name = name;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);

        return result;
    }
}