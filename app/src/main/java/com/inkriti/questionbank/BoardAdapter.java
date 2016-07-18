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
public class BoardAdapter extends BaseAdapter {
    private Context mContext;


    public ArrayList<Board> boards;
    private DatabaseReference  mDatabase;
    public BoardAdapter that;

    public BoardAdapter(Context c){
        mContext = c;
        this.boards = new ArrayList<Board>();
        this.that = this;

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
                        // that.notifyDataSetChanged();
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
        TextView textView;
        if (convertView == null) {
            textView = new TextView(mContext);
            textView.setPadding(8, 8, 8, 8);
        }else{
            textView = (TextView) convertView;
        }
        final Board b = boards.get(position);
        textView.setText(b.name);

        //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Snackbar.make(v, b.name + " Board was clicked", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                intent.putExtra("board.id", b.id);
                v.getContext().startActivity(intent);

            }
        });

        return textView;
    }

}