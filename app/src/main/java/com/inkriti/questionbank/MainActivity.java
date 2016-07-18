package com.inkriti.questionbank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView boardsGridview = (GridView) findViewById(R.id.boardsGridView);
        boardsGridview.setAdapter(new BoardAdapter(this));
    }
}
