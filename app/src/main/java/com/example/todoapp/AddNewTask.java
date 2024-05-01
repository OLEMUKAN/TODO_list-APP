package com.example.todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.DialogCloseListener;
import com.example.todoapp.Model.TodoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private EditText courseName;
    private EditText courseUnit;
    private Button reminderButton;
    private Button saveButton;
    private DatabaseHandler db;
    private Calendar calendar;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        newTaskText = view.findViewById(R.id.newTask);
        courseName = view.findViewById(R.id.courseName);
        courseUnit = view.findViewById(R.id.courseUnit);
        reminderButton = view.findViewById(R.id.reminder);
        saveButton = view.findViewById(R.id.newTaskButton);

        db = new DatabaseHandler(getActivity());
        calendar = Calendar.getInstance();

        reminderButton.setOnClickListener(v -> showDatePickerDialog());

        final Bundle bundle = getArguments();
        if (bundle != null) {
            TodoModel task = (TodoModel) bundle.getSerializable("task");
            newTaskText.setText(task.getTaskName());
            courseName.setText(task.getCourseName());
            courseUnit.setText(task.getCourseUnit());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            calendar.setTimeInMillis(task.getReminder());
            reminderButton.setText(sdf.format(calendar.getTime()));
        }

        saveButton.setOnClickListener(v -> {
            String text = newTaskText.getText().toString();
            String course = courseName.getText().toString();
            String unit = courseUnit.getText().toString();
            if (!text.isEmpty()) {
                if (bundle != null && bundle.getInt("id") != 0) {
                    db.updateTask(bundle.getInt("id"), text, course, unit, calendar.getTimeInMillis(), 1);
                } else {
                    TodoModel task = new TodoModel();
                    task.setTaskName(text);
                    task.setCourseName(course);
                    task.setCourseUnit(unit);
                    task.setReminder(calendar.getTimeInMillis());
                    task.setTaskStatus(false);  // Change to true if you need to handle task status
                    db.insertTask(task);
                }
                dismiss();
            } else {
                Toast.makeText(getContext(), "Task description cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year1);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateText();
                }, year, month, day);

        datePickerDialog.show();
    }

    private void updateDateText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        reminderButton.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }
}
