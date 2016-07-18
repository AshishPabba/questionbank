package com.inkriti.questionbank;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private String[] mDrawerOptions;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        mDrawerOptions = getResources().getStringArray(R.array.MenuOptions);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//
//        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, mDrawerOptions));
//        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        GridView boardsGridview = (GridView) findViewById(R.id.boardsGridView);
        boardsGridview.setAdapter(new BoardAdapter(this));

        GridView subjectsGridView = (GridView) findViewById(R.id.subjectsGridView);
        subjectsGridView.setAdapter(new SubjectAdapter(this));
    }
}
