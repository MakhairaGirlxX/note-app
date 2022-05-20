package com.example.finalproject_noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteScreen extends AppCompatActivity {
    EditText noteHeaderInput;
    EditText noteInfoInput;
    TextView noteDate;

    SQLiteDatabase myDB = null;
    int key = 0;
    int id = 0;
    int deleteKey = 0;

    public static final String TitlePreferences = "MyPrefs";
    SharedPreferences sp;

/////NOTE: commented out any instances where I attempt to clear the database. It crashes the app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_screen);

        Intent fromActivity = getIntent();
        if(fromActivity != null){
            /*
            try {
                File data = Environment.getDataDirectory();
                String currentDBPath = "/data/data/" + getPackageName() + "/mydatabase/notes.sqlite";
                File currentDB = new File(data, currentDBPath);
                boolean deleted = SQLiteDatabase.deleteDatabase(currentDB);
            }
            catch(SQLiteException e) {System.out.println("database does not exist"); }
            */
            key = fromActivity.getIntExtra("key", 0);
            id = fromActivity.getIntExtra("id", 0);
            noteHeaderInput = (EditText) findViewById(R.id.note_header);
            noteInfoInput = (EditText) findViewById(R.id.note_info);
            noteDate = (TextView) findViewById(R.id.date);

            SharedPreferences imageprefs = getSharedPreferences("imagekey", Context.MODE_PRIVATE);
            String imagetitle = imageprefs.getString("imagetitle", "");

            //checks that an image exists then sets the background of the note screen to the selected image
            if(imagetitle != null){
                String url = "drawable/" + imagetitle;

                int resID = getResources().getIdentifier(url, "drawable", getPackageName());
                ConstraintLayout cl = findViewById(R.id.note_background);
                Drawable newImage = getResources().getDrawable(resID);
                cl.setBackground(newImage);
            }

            //checks that a note exists
            if(key > 0) {
                getNoteInfo(key);
            }
            if(id > 0){
                getNoteInfo(id);
            }
        }

    }

    public void clearDatabase(String TABLE_NAME) {
        String clearDBQuery = "DELETE * FROM "+ TABLE_NAME;
        myDB.execSQL(clearDBQuery);
    }
    ////FUNCTION I TRIED TO DELETE NOTES
    /*public static void deleteDatabase(Context mContext){
        mContext.deleteDatabase("notes.sqlite");
    }
     */


    //creates a database and stores the note information in the appropriate spot
    public void storeNoteInfo(View v) {

        String headerValue = noteHeaderInput.getText().toString();
        String infoValue = noteInfoInput.getText().toString();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());


        String tableName = "noterecords";

        try {

            myDB = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/mydatabase/notes.sqlite", null, SQLiteDatabase.OPEN_READONLY);
            String createTable = "CREATE TABLE IF NOT EXISTS " + tableName + "(Note_ID INT, " +
                    " Title VARCHAR, " +
                    " Information VARCHAR, " +
                    " Date VARCHAR);";
            myDB.execSQL(createTable);

            String insertSql = "INSERT INTO " +
                    tableName +
                    " VALUES ('" +
                    key +
                    "','" +
                    headerValue +
                    "','" +
                    infoValue +
                    "','" +
                    strDate +
                    "');";

            myDB.execSQL(insertSql);

            myDB.close();

        } catch (SQLiteException e) {
           System.out.println("Database Error");
        }
        //clearDatabase("noterecords");

        //sets the current date that the note is created
        noteDate.setText(strDate);

        //checking if the correct records were stored at the right key
        System.out.println("Records stored" + key);

        }

        //gets the note information associated with the correct key
        public void getNoteInfo(int key) {
            String note_title = "";
            String note_info = "";
            String note_date = "";

            try{
                SQLiteDatabase myDB = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/mydatabase/notes.sqlite", null, SQLiteDatabase.OPEN_READONLY);
                String sql = "SELECT Title, Information, Date FROM noterecords WHERE note_id = " + key + ";";
                Cursor crs = myDB.rawQuery(sql,null);

                if(crs.moveToFirst()) {
                    do {
                        note_title += crs.getString(0);
                        note_info += crs.getString(1);
                        note_date += crs.getString(2);

                    } while (crs.moveToNext());
                    myDB.close();
                }
            }  catch(SQLiteException e) {
                System.out.println("Database does not exist");
            }


            noteHeaderInput.setText(note_title);
            noteInfoInput.setText(note_info);
            noteDate.setText(note_date);
        }

        //gets the note title
    public String getNoteTitle() {
        String note_title = "";

        try{
            SQLiteDatabase myDB = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/mydatabase/notes.sqlite", null, SQLiteDatabase.OPEN_READONLY);
            String sql = "SELECT Title FROM noterecords WHERE note_ID = " + key + ";";
            Cursor crs = myDB.rawQuery(sql,null);

            if(crs.moveToFirst()) {
                do {
                    note_title += crs.getString(0);

                } while (crs.moveToNext());
                myDB.close();
            }
        }  catch(SQLiteException e) {
            System.out.println("Database does not exist");
            return " ";
        }
        return note_title;
    }

    public void deleteNote(View v){
        deleteKey = 1;
        //makes sure a note exists to be deleted
       goBack(v);
    }

        public void goBack(View v) {

            //store note title as a shared preference
            sp = getSharedPreferences("titlekey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            System.out.println("Key: " + key);
            editor.putString("notevalue", getNoteTitle());
            editor.putInt("deletevalue", deleteKey);
            //send the key back because onResume is called first
            editor.putInt("keyvalue", key);

            editor.commit();

            //finish is called so the main screen retains the notes that were created
            finish();
        }
    }