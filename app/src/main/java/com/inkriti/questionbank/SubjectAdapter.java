package com.inkriti.questionbank;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by User on 12 July 2016.
 */
public class SubjectAdapter extends BaseAdapter {
    private Context mContext;


    public ArrayList<Subject> subjects;
    private DatabaseReference  mDatabase;
    public SubjectAdapter that;

    public SubjectAdapter(Context c){
        mContext = c;
        this.subjects = new ArrayList<Subject>();
        this.that = this;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("subjects").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("Firebase:boards:", String.valueOf(dataSnapshot.getChildrenCount()));
                        for (DataSnapshot subject : dataSnapshot.getChildren()){
                            Log.i("Firebase:" + subject.getKey() + ":", (String) subject.child("name").getValue());
                            Subject o = new Subject((String) subject.getKey(), (String) subject.child("name").getValue());
                            subjects.add(o);
                        }
                        Log.i("Adapter:cns:loaded", String.valueOf(subjects.size()));
                        that.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Firebase", "getBoards:onCancelled", databaseError.toException());
                    }
                });
    }
    public int getCount(){
        return subjects.size();
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
        final Subject o = subjects.get(position);
        textView.setText(o.name);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Snackbar.make(v, o.name + " Subject was clicked", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                intent.putExtra("subject.id", o.id);
                v.getContext().startActivity(intent);

            }
        });

        return textView;
    }

}