package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.crypto.Mac;

public class Add_Todo extends AppCompatActivity {

    TextInputEditText date, time, txt_task, txt_msg;
    DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    String del_time, del_date;
    TimePickerDialog timePickerDialog;
    Button add_todo;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        date = findViewById(R.id.txt_ddate);
        time = findViewById(R.id.txt_time);
        add_todo = findViewById(R.id.add_todo);
        txt_task = findViewById(R.id.txt_task);
        txt_msg = findViewById(R.id.txt_msg);

        date.setInputType(InputType.TYPE_NULL);
        time.setInputType(InputType.TYPE_NULL);

        db = new DBHelper(this);

        // Set Date and Time Pickers
        setupDateAndTimePickers();

        add_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String task = txt_task.getEditableText().toString();
                String task_msg = txt_msg.getEditableText().toString();
                String date1 = date.getText().toString();
                String time1 = time.getText().toString();

                if (task_msg.equals("") && task.equals("")) {
                    txt_task.setError("Empty Task Title");
                    txt_msg.setError("Empty Task Message");
                    date.setError("Select Date");
                    time.setError("Select Time");
                } else if (time1.equals("")) {
                    time.setError("Select Time");
                } else if (date1.equals("")) {
                    date.setError("Select Date");
                } else if (task.equals("")) {
                    txt_task.setError("Empty Task Title");
                } else if (task_msg.equals("")) {
                    txt_msg.setError("Empty Task Message");
                } else {
                    Boolean checkInsertData = db.addTodo(task, task_msg, date1, time1);
                    if (checkInsertData) {
                        Toast.makeText(Add_Todo.this, "Task Added successfully", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(Add_Todo.this, "Data Already Exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void setupDateAndTimePickers() {
        // Set Date Picker
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                } else {
                    datePickerDialog.hide();
                }
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                date.setText(i2 + "/" + i1 + "/" + i);
                del_date = i2 + "/" + i1 + "/" + i;
            }
        };

        // Set Time Picker
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePicker();
                } else {
                    timePickerDialog.hide();
                }
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(Add_Todo.this, onDateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        time.setText(hour + ":" + min);

        timePickerDialog = new TimePickerDialog(Add_Todo.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String ampm;
                if (i < 12) {
                    ampm = "AM";
                } else {
                    ampm = "PM";
                }
                time.setText(i + ":" + i1 + " " + ampm);
                del_time = i + ":" + i1 + " " + ampm;
            }
        }, hour, min, true);
        timePickerDialog.show();

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
    }
}

