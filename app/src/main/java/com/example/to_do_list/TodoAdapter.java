package com.example.to_do_list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    Context context;
    ArrayList task;
    ArrayList taskmsg;
    ArrayList taskdate;
    ArrayList tasktime;
    ArrayList id;

    public TodoAdapter(Context context,ArrayList id,ArrayList task, ArrayList taskmsg, ArrayList taskdate, ArrayList tasktime) {
        this.context = context;
        this.id = id;
        this.task = task;
        this.taskmsg = taskmsg;
        this.taskdate = taskdate;
        this.tasktime = tasktime;
    }

    @NonNull
    @Override
    public TodoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_todo_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.task_id.setText(String.valueOf(id.get(position)));
        holder.task.setText(String.valueOf(task.get(position)));
        holder.taskmessage.setText(String.valueOf(taskmsg.get(position)));
        holder.taskdate.setText(String.valueOf(taskdate.get(position)));
        holder.tasktime.setText(String.valueOf(tasktime.get(position)));

        holder.task_confirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked==true)
                {
//                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.ic_warning2)
                            .setTitle("Confirmation!!!")
                            .setMessage("Are You Sure To Complete Task!!!")
                            .setCancelable(false)

                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    DBHelper db = new DBHelper(context);
                                    db.deleteTodo(String.valueOf(id.get(position)));
                                    Boolean check = db.deleteTodo(String.valueOf(id));

                                    if (check==true)
                                    {
//                                           Toast.makeText(context, "Deleted Successfully..", Toast.LENGTH_LONG).show();
                                           ((Activity) context).recreate();
                                    }
                                    else {
                                        Toast.makeText(context, "Not Deleted Please Try Again..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    holder.task_confirm.setChecked(false);
                                }
                            }).show();

                }
                else {
//                    Toast.makeText(context, "Not", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.view_todo_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _task =String.valueOf(task.get(position));
                String _taskmsg =String.valueOf(taskmsg.get(position));
                String _taskdate =String.valueOf(taskdate.get(position));
                String _tasktime =String.valueOf(tasktime.get(position));
                String _id = String.valueOf(id.get(position));

                Intent i1 = new Intent(context,Update_Todo.class);
                i1.putExtra("task", _task);
                i1.putExtra("taskmsg", _taskmsg);
                i1.putExtra("taskdate", _taskdate);
                i1.putExtra("tasktime", _tasktime);
                i1.putExtra("taskId", _id);

                context.startActivity(i1);
                ((Activity)context).finish();
            }
        });



    }

    @Override
    public int getItemCount()    {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView task_id,task,taskmessage,taskdate,tasktime;
        CheckBox task_confirm;

        ConstraintLayout view_todo_demo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            task_id = itemView.findViewById(R.id.task_id);
            task = itemView.findViewById(R.id.task);
            taskmessage = itemView.findViewById(R.id.taskmessage);
            taskdate = itemView.findViewById(R.id.taskdate);
            tasktime = itemView.findViewById(R.id.tasktime);
            task_confirm = itemView.findViewById(R.id.task_confirm);
            view_todo_demo = itemView.findViewById(R.id.view_todo_demo);
        }
    }
}
