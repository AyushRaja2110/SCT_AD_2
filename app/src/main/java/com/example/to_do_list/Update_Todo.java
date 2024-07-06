package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class Update_Todo extends AppCompatActivity {

    String task, taskmsg, taskdate, tasktime, id;
    TextInputEditText date, time, txt_task, txt_msg;
    Button update_todo;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo);

        db = new DBHelper(this);

        txt_task = findViewById(R.id.update_txt_task);
        txt_msg = findViewById(R.id.update_txt_msg);
        date = findViewById(R.id.update_txt_ddate);
        time = findViewById(R.id.update_txt_time);
        update_todo = findViewById(R.id.update_todo);

        Intent i = getIntent();
        task = i.getStringExtra("task");
        taskmsg = i.getStringExtra("taskmsg");
        taskdate = i.getStringExtra("taskdate");
        tasktime = i.getStringExtra("tasktime");
        id = i.getStringExtra("taskId");

        txt_task.setText(task);
        txt_msg.setText(taskmsg);
        date.setText(taskdate);
        time.setText(tasktime);

        // Set Date and Time Pickers
        setupDateAndTimePickers();

        update_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedTask = txt_task.getEditableText().toString();
                String updatedTaskMsg = txt_msg.getEditableText().toString();
                String updatedDate = date.getText().toString();
                String updatedTime = time.getText().toString();

                Log.d("Update_Todo", "Updating task with ID: " + id + ", Title: " + updatedTask + ", Message: " + updatedTaskMsg + ", Date: " + updatedDate + ", Time: " + updatedTime);

                if (updatedTask.isEmpty()) {
                    txt_task.setError("Empty Task Title");
                } else if (updatedTaskMsg.isEmpty()) {
                    txt_msg.setError("Empty Task Message");
                } else if (updatedDate.isEmpty()) {
                    date.setError("Select Date");
                } else if (updatedTime.isEmpty()) {
                    time.setError("Select Time");
                } else {
                    Boolean updatedata = db.updateTodo(id, updatedTask, updatedTaskMsg, updatedDate, updatedTime);
                    if (updatedata) {
                        Toast.makeText(Update_Todo.this, "Updated.. Successfully", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(Update_Todo.this,MainActivity.class);
                        setResult(Activity.RESULT_OK);
                        startActivity(i1);
                        finish();
                    } else {
                        Toast.makeText(Update_Todo.this, "Fail! Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setupDateAndTimePickers() {
        date.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker();
            }
        });

        date.setOnClickListener(v -> showDatePicker());

        time.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showTimePicker();
            }
        });

        time.setOnClickListener(v -> showTimePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Update_Todo.this, (datePicker, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            date.setText(dayOfMonth + "/" + month1 + "/" + year1);
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Update_Todo.this, (timePicker, hourOfDay, minute) -> {
            String ampm = (hourOfDay < 12) ? "AM" : "PM";
            time.setText(hourOfDay + ":" + minute + " " + ampm);
        }, hour, min, true);
        timePickerDialog.show();
    }
}
