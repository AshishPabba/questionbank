package com.inkriti.questionbank;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class QuestionAdapter extends BaseAdapter {
    private Context mContext;


    public ArrayList<Question> questions;
    private DatabaseReference  mDatabase;
    public QuestionAdapter that;
    private LayoutInflater mInflater;

    public QuestionAdapter(Context c, String subject, String board){
        mContext = c;
        this.questions = new ArrayList<Question>();
        this.that = this;
        this.mInflater = LayoutInflater.from(mContext);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String filter = "filters/" + subject + "-" + board;
        // Query query = mDatabase.child("questions").orderByChild("subjects/physics").equalTo(Boolean.TRUE);
        Query query = mDatabase.child("questions").orderByChild(filter).equalTo(Boolean.TRUE);
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
                                    (String) question.child("answer").getValue());
                            o.setBoards((HashMap<String, Boolean>) question.child("boards").getValue());
                            o.setSubjects((HashMap<String, Boolean>) question.child("subjects").getValue());
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
        QuestionHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.question_row, null);
            holder = new QuestionHolder();
            holder.question = (TextView) convertView.findViewById(R.id.list_question_view);
            holder.answer = (TextView) convertView.findViewById(R.id.list_answer_view);
            holder.edit = (ImageView) convertView.findViewById(R.id.edit);
            convertView.setTag(holder);
        }else{
            holder = (QuestionHolder) convertView.getTag();
        }
        final Question question = questions.get(position);
        holder.question.setText(question.question);
        holder.answer.setText(question.answer);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Edit", question.id + ":" + question.question);
                Intent intent = new Intent( that.mContext, AddQuestionActivity.class);
                intent.putExtra("question", question);
                that.mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    static class QuestionHolder{
        TextView question;
        TextView answer;
        ImageView edit;
    }

}
