package com.inkriti.questionbank;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 14 July 2016.
 */
public class Question implements Serializable {
    public String id;
    public String question;
    public String answer;

    public HashMap<String, Boolean> subjects;
    public HashMap<String, Boolean> boards;

    //private DatabaseReference mDatabase;
    public Question(){

    }

    public Question(String id, String question, String answer){
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.subjects = new HashMap<>();
        this.boards = new HashMap<>();

        //mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("question", question);
        result.put("answer", answer);

        result.put("boards", this.boards);
        result.put("subjects", this.subjects);
        result.put("filters", this.getFilters());

        return result;
    }

    public void saveQuestion(){
        if(TextUtils.isEmpty(id)){
            this.id = FirebaseDatabase.getInstance().getReference().child("questions").push().getKey();
        }
        Map<String, Object> questionValues = this.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/questions/" + this.id, questionValues);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
    }

    public void setSubjects(HashMap<String, Boolean> subjects){
        this.subjects = subjects;
        Log.i("Subjects", String.valueOf(subjects.size()));
    }

    public void setBoards(HashMap<String, Boolean> boards){
        this.boards = boards;
    }

    protected HashMap<String, Boolean> getFilters(){
        HashMap<String, Boolean> filters = new HashMap<>();
        for(String subject: this.subjects.keySet()){
            filters.put(subject, true);
            for(String board: this.boards.keySet()){
                if(!filters.containsKey(board)){
                    filters.put(board, true);
                }
                filters.put(subject + "-" + board, true);
            }
        }
        return filters;
    }

}
