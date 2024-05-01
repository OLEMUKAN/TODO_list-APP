package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.Model.TodoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<TodoModel> todoList;
    private Context context;
    private DatabaseHandler db;

    public TodoAdapter(Context context, DatabaseHandler db) {
        this.context = context;
        this.db = db;
        todoList = new ArrayList<>();
    }

    public void setTasks(List<TodoModel> tasks) {
        todoList = tasks;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoModel task = todoList.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.courseName.setText(task.getCourseName());
        holder.courseUnit.setText(task.getCourseUnit());
        holder.reminder.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(task.getReminder())));
        holder.taskStatus.setChecked(task.isTaskStatus());

        holder.taskStatus.setOnCheckedChangeListener((buttonView, isChecked) -> db.updateTaskStatus(task.getId(), isChecked ? 1 : 0));

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("task", task);
            AddNewTask dialog = AddNewTask.newInstance();
            dialog.setArguments(bundle);
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "AddNewTask");
        });

        holder.itemView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this task?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                db.deleteTask(task.getId());
                todoList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, todoList.size());
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView courseName;
        TextView courseUnit;
        TextView reminder;
        CheckBox taskStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_name);
            courseName = itemView.findViewById(R.id.course_name);
            courseUnit = itemView.findViewById(R.id.course_unit);
            reminder = itemView.findViewById(R.id.reminder);
            taskStatus = itemView.findViewById(R.id.task_status);
        }
    }

    public void deleteItem(int position) {
        TodoModel item = todoList.get(position);
        db.deleteTask(item.getId());  // Assuming you have a method in DatabaseHandler to delete tasks by ID
        todoList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, todoList.size());
    }

    public void editItem(int position) {
        TodoModel task = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        AddNewTask dialog = AddNewTask.newInstance();
        dialog.setArguments(bundle);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), AddNewTask.TAG);
    }


}
