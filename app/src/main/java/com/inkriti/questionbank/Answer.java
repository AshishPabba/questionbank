package com.inkriti.questionbank;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vidha on 20-07-2016.
 */
public class Answer {
    public String id;
    public String question;
    public String answer;

    public Answer(){

    }

    public Answer(String id, String question, String answer){
        this.id = id;
        this.question = question;
        this.answer = answer;
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("question", question);
        result.put("answer", answer);

        return result;
    }
}
