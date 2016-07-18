package com.inkriti.questionbank;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {
    private Context mContext;


    public ArrayList<Question> questions;
    private DatabaseReference  mDatabase;
    public QuestionAdapter that;

    public QuestionAdapter(Context c){
        mContext = c;
        this.questions = new ArrayList<Question>();
        this.that = this;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Query query = mDatabase.child("questions").orderByChild("subjects/physics").equalTo(Boolean.TRUE);
        Query query = mDatabase.child("questions").orderByChild("subjects/physics").equalTo(Boolean.TRUE);
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("Firebase:questions:", String.valueOf(dataSnapshot.getChildrenCount()));
                        for (DataSnapshot question : dataSnapshot.getChildren()){
                            Log.i("Firebase:" + question.getKey() + ":", (String) question.child("question").getValue());
                            Question o = new Question(
                                    (String) question.getKey(),
                                    (String) question.child("question").getValue(),
                                    (String) question.child("question").getValue());
                            questions.add(o);
                        }
                        Log.i("Adapter:questionsloaded", String.valueOf(questions.size()));
                        that.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Firebase", "getQuestions:onCancelled", databaseError.toException());
                    }
                });
    }
    public int getCount(){
        return questions.size();
    }
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(mContext);
            textView.setPadding(8, 8, 8, 8);
        }else{
            textView = (TextView) convertView;
        }
        final Question question = questions.get(position);
        textView.setText(question.question);
        return textView;
    }

}