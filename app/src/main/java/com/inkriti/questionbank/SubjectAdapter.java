package com.inkriti.questionbank;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
    private Boolean isForm = false;


    public ArrayList<Subject> subjects;
    private DatabaseReference  mDatabase;
    public SubjectAdapter that;

    public SubjectAdapter(Context c, Boolean isForm){
        mContext = c;
        this.isForm = isForm;

        this.subjects = new ArrayList<Subject>();
        this.that = this;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.i("SAdapter", "Before event attachment");
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
        Log.i("Subject getCount", String.valueOf(subjects.size()));
        return subjects.size();
    }
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(this.isForm){
            return this.getCheckbox(position, convertView, parent);
        }else{
            return this.getTextView(position, convertView, parent);
        }
    }

    public View getTextView(int position, View convertView, ViewGroup parent){
        TextView textView;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            textView = (TextView) inflater.inflate(R.layout.item, null);
        }else{
            textView = (TextView) convertView;
        }
        final Subject o = subjects.get(position);
        textView.setText(o.name);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Snackbar.make(v, o.name + " Subject was clicked", Snackbar.LENGTH_LONG).show();
                ((MainActivity)that.mContext).setSelected("subject", o.id);

            }
        });

        return textView;
    }

    public View getCheckbox(int position, View convertView, ViewGroup parent){
        final CheckBox checkBox;
        if (convertView == null) {
            checkBox = new CheckBox(mContext);
            checkBox.setPadding(8, 8, 8, 8);
        }else{
            checkBox = (CheckBox) convertView;
        }
        final Subject o = subjects.get(position);
        checkBox.setText(o.name);
        if(((AddQuestionActivity)that.mContext).isSubjectSelected(o.id)) {
            checkBox.setChecked(true);
        }


        checkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String c;
                c = checkBox.isChecked() ? "Checked": "UnChecked";
                Snackbar.make(v, o.id + " Subject was " + c, Snackbar.LENGTH_LONG).show();
                ((AddQuestionActivity)that.mContext).selectSubjects(o.id, checkBox.isChecked());

            }
        });

        return checkBox;
    }

}
