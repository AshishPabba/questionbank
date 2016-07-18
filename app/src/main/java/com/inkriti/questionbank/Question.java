package com.inkriti.questionbank;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Question {
    public String id;
    public String question;
    public String answer;

    public Question(){

    }

    public Question(String id, String question, String answer){
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