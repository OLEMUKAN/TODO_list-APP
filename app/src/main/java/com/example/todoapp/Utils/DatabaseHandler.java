package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_UNIT = "course_unit";
    private static final String REMINDER = "reminder";
    private static final String STATUS = "status";
    private static final int VERSION = 2;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TODO_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK + " TEXT, " +
                COURSE_NAME + " TEXT, " +
                COURSE_UNIT + " TEXT, " +
                REMINDER + " INTEGER, " +
                STATUS + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    @SuppressLint("Range")
    public List<TodoModel> getAllTasks() {
        List<TodoModel> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TODO_TABLE, null, null, null, null, null, null);
        if (cur.moveToFirst()) {
            do {
                TodoModel task = new TodoModel();
                task.setId(cur.getInt(cur.getColumnIndex(ID)));
                task.setTaskName(cur.getString(cur.getColumnIndex(TASK)));
                task.setCourseName(cur.getString(cur.getColumnIndex(COURSE_NAME)));
                task.setCourseUnit(cur.getString(cur.getColumnIndex(COURSE_UNIT)));
                task.setReminder(cur.getLong(cur.getColumnIndex(REMINDER)));
                task.setTaskStatus(cur.getInt(cur.getColumnIndex(STATUS)) > 0);
                taskList.add(task);
            } while (cur.moveToNext());
        }
        cur.close();
        return taskList;
    }


    public void insertTask(TodoModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTaskName());
        cv.put(COURSE_NAME, task.getCourseName());
        cv.put(COURSE_UNIT, task.getCourseUnit());
        cv.put(REMINDER, task.getReminder());
        cv.put(STATUS, task.isTaskStatus() ? 1 : 0);
        db.insert(TODO_TABLE, null, cv);
    }

    public void updateTaskStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }


    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String taskName, String courseName, String courseUnit, long reminder, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK, taskName);
        cv.put(COURSE_NAME, courseName);
        cv.put(COURSE_UNIT, courseUnit);
        cv.put(REMINDER, reminder);
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + " = ?", new String[] { String.valueOf(id) });
    }



}
