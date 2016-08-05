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
public class BoardAdapter extends BaseAdapter {
    private Context mContext;
    private Boolean isForm = false;

    public ArrayList<Board> boards;
    private DatabaseReference  mDatabase;
    public BoardAdapter that;

    public BoardAdapter(Context c, Boolean isForm){
        mContext = c;
        this.boards = new ArrayList<Board>();
        this.that = this;
        this.isForm = isForm;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("boards").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("Firebase:boards:", String.valueOf(dataSnapshot.getChildrenCount()));
                        for (DataSnapshot board : dataSnapshot.getChildren()){
                            Log.i("Firebase:" + board.getKey() + ":", (String) board.child("name").getValue());
                            Board b = new Board((String) board.getKey(), (String) board.child("name").getValue());
                            boards.add(b);
                        }
                        Log.i("Adapter:cns:loaded", String.valueOf(boards.size()));
                        that.notifyDataSetChanged();
                        try {
                            ((MainActivity) that.mContext).showProgress(false);
                        }catch(Exception e){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Firebase", "getBoards:onCancelled", databaseError.toException());
                    }
                });
    }
    public int getCount(){
        return boards.size();
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
            //textView = new TextView(mContext);
        }else{
            textView = (TextView) convertView;
        }
        final Board o = boards.get(position);
        textView.setText(o.name);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Snackbar.make(v, o.name + " Board was clicked", Snackbar.LENGTH_LONG).show();
                ((MainActivity)that.mContext).setSelected("board", o.id);

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
        final Board o = boards.get(position);
        checkBox.setText(o.name);
        if(((AddQuestionActivity)that.mContext).isBoardSelected(o.id)) {
            checkBox.setChecked(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String c;
                c = checkBox.isChecked() ? "Checked": "UnChecked";
                Snackbar.make(v, o.id + " Board was " + c, Snackbar.LENGTH_LONG).show();
                ((AddQuestionActivity)that.mContext).selectBoards(o.id, checkBox.isChecked());

            }
        });

        return checkBox;
    }

}
