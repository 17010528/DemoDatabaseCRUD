package com.example.demodatabasecrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VER = 2;
    private static final String DATABASE_NAME = "Note.db";
    private static final String TABLE_NOTE = "Note";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_noteContent = "noteContent";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NOTE +  "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_noteContent + " TEXT)";
        db.execSQL(createTableSql);
        Log.i("info" ,"created tables");

        for(int i =0; i<4 ; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_noteContent, "Data number " + i);
            db.insert(TABLE_NOTE, null, values);
        }
        Log.i("info", "dummy records inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db.execSQL("ALTER TABLE " + TABLE_NOTE + " ADD COLUMN module_name TEXT ");
        // Create table(s) again

    }

    public long insertNote(String noteContent){

        // Get an instance of the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // We use ContentValues object to store the values for
        //  the db operation
        ContentValues values = new ContentValues();
        // Store the column name as key and the description as value
        values.put(COLUMN_noteContent, noteContent);
        // Store the column name as key and the date as value
        // Insert the row into the TABLE_TASK
        long result = db.insert(TABLE_NOTE, null, values);
        // Close the database connection
        db.close();
        if (result==-1){
            Log.d("DHHelper" , "Insert failed");
        }
        Log.d("SQL Insert ", ""+ result);
        return result;
    }


    public ArrayList<String> getAllNotes() {
        ArrayList<String> notes = new ArrayList<String>();

        String selectQuery = "SELECT " + COLUMN_ID + ","
                + COLUMN_noteContent + " FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String content = cursor.getString(1);
                notes.add("ID:" + id + ", " + content);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }
    public int updateNote(Note data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_noteContent, data.getNoteContent());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.getId())};
        int result = db.update(TABLE_NOTE, values, condition, args);
        if (result < 1){
            Log.d("DBHelper", "Update failed");
        }

        db.close();
        return result;
    }

    public int deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_NOTE, condition, args);
        if (result < 1){
            Log.d("DBHelper", "Update failed");
        }
        db.close();
        return result;
    }

    public ArrayList<Note> getAllNotes(String keyword) {
        ArrayList<Note> notes = new ArrayList<Note>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns= {COLUMN_ID, COLUMN_noteContent};
        String condition = COLUMN_noteContent + " Like ?";
        String[] args = { "%" +  keyword + "%"};
        Cursor cursor = db.query(TABLE_NOTE, columns, condition, args,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }



//    public ArrayList<String> getTaskContent() {
//        // Create an ArrayList that holds String objects
//        ArrayList<String> tasks = new ArrayList<String>();
//        // Select all the tasks' description
//        String selectQuery = "SELECT " + COLUMN_noteContent
//                + " FROM " + TABLE_NOTE;
//
//        // Get the instance of database to read
//        SQLiteDatabase db = this.getReadableDatabase();
//        // Run the SQL query and get back the Cursor object
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // moveToFirst() moves to first row
//        if (cursor.moveToFirst()) {
//            // Loop while moveToNext() points to next row
//            //  and returns true; moveToNext() returns false
//            //  when no more next row to move to
//            do {
//                // Add the task content to the ArrayList object
//                //  0 in getString(0) return the data in the first
//                //  column in the Cursor object. getString(1)
//                //  return second column data and so on.
//                //  Use getInt(0) if data is an int
//                tasks.add(cursor.getString(0));
//            } while (cursor.moveToNext());
//        }
//        // Close connection
//        cursor.close();
//        db.close();
//
//        return tasks;
//    }




//    public ArrayList<Task> getTasks() {
//        ArrayList<Task> tasks = new ArrayList<Task>();
//        String selectQuery = "SELECT " + COLUMN_ID + ", "
//                + COLUMN_DESCRIPTION + ", "
//                + COLUMN_DATE
//                + " FROM " + TABLE_TASK;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                int id = cursor.getInt(0);
//                String description = cursor.getString(1);
//                String date = cursor.getString(2);
//                Task obj = new Task(id, description, date);
//                tasks.add(obj);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return tasks;
//    }

}


