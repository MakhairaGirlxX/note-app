package com.example.finalproject_noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchScreen extends AppCompatActivity {

    LinearLayout linLayout;
    TextView newNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        linLayout = (LinearLayout) findViewById(R.id.note_container);
    }

    public void search(View v){
        EditText searchbar = (EditText) findViewById(R.id.search_bar);
        String query = searchbar.getText().toString();

        searchDatabase(query);
    }

    public void searchDatabase(String query){
        String title_info = "";
        int note_id = 0;

        try{
            SQLiteDatabase myDB = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/mydatabase/notes.sqlite", null, SQLiteDatabase.OPEN_READONLY);
            String sql = "SELECT Note_ID, Title FROM noterecords WHERE Title LIKE '%" + query
                    + "%';";
            Cursor crs = myDB.rawQuery(sql,null);

            if(crs.moveToFirst()) {
                do {
                    note_id += crs.getInt(0);
                    title_info += crs.getString(1);

                } while (crs.moveToNext());
                myDB.close();
            }
        }  catch(SQLiteException e) {
            System.out.println("Database does not exist");
        }
        System.out.println("Title info" + title_info);
        System.out.println(note_id);
        addNotes(title_info, note_id);

    }

    public void addNotes(String title, int id){
        //add notes
        linLayout.addView(createTextView(title));
        //set onclick to get note info
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent noteScreen = new Intent(SearchScreen.this, NoteScreen.class);
                noteScreen.putExtra("id", id);
                startActivity(noteScreen);
            }
        });
    }

    public TextView createTextView(String text) {
        final LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newNote = new TextView(SearchScreen.this);

        newNote.setLayoutParams(cParams);
        newNote.setText(text);
        newNote.setWidth(411);
        newNote.setHeight(64);
        newNote.setTextColor(Color.WHITE);
        newNote.setTextSize(20);

        return newNote;
    }

}