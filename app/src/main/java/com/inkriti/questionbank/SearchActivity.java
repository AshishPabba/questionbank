package com.inkriti.questionbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        String boardId = getIntent().getExtras().getString("board.id");
        Snackbar.make(toolbar, "Board: " + boardId + " selected.", Snackbar.LENGTH_LONG).show();
        String subjectId = getIntent().getExtras().getString("subject.id");
        Snackbar.make(toolbar, "Subject: " + subjectId + " selected.", Snackbar.LENGTH_LONG).show();

        ListView questionsListView = (ListView) findViewById(R.id.searchlist);
        questionsListView.setAdapter(new QuestionAdapter(this));
    }

}
