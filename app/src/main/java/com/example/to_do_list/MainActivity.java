package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerview;
    ArrayList<String> task, taskMsg, taskDate, taskTime,id;
    TodoAdapter adapter;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        fab = findViewById(R.id.fab);
        recyclerview = findViewById(R.id.recyclerview);

        id = new ArrayList<>();
        task = new ArrayList<>();
        taskMsg = new ArrayList<>();
        taskDate = new ArrayList<>();
        taskTime = new ArrayList<>();

        adapter = new TodoAdapter(this,id, task, taskMsg, taskDate, taskTime);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        displayData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity.this, Add_Todo.class);
                startActivityForResult(i1, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            task.clear();
            taskMsg.clear();
            taskDate.clear();
            taskTime.clear();
            displayData();
            adapter.notifyDataSetChanged();
        }
    }

    private void displayData() {
        Cursor cursor = db.getTodo();
        if (cursor.getCount() == 0) {
//            Toast.makeText(this, "No Entry Exists.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                task.add(cursor.getString(1));
                taskMsg.add(cursor.getString(2));
                taskDate.add(cursor.getString(3));
                taskTime.add(cursor.getString(4));
            }
        }
    }
}
