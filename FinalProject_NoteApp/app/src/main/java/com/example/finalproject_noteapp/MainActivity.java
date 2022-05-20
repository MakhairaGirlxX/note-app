package com.example.finalproject_noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout linLayout;
    int key = 0;
    TextView newNote;
    HashMap<Integer, TextView> noteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //declare a hashmap to store created textviews
        noteArray = new HashMap<Integer, TextView>();
        linLayout = (LinearLayout) findViewById(R.id.note_container);
    }

    public void search(View v){
        //calls the search screen
        Intent searchScreen = new Intent(MainActivity.this, SearchScreen.class);
        startActivity(searchScreen);
    }

    public void sort(View v) {
        //not used in project, only for show
        Spinner sortChoice = (Spinner) findViewById(R.id.sort_spinner);
        String sort = sortChoice.getSelectedItem().toString();
    }

    public void selectTheme(View v) {
        //calls the gallery to select an image for the theme
        Intent gallery = new Intent(MainActivity.this, Gallery.class);
        startActivity(gallery);

    }

    public void addNote(View v) {
        //creates a textview and adds it to a linear layout (for formatting purposes) then adds it into the hashmap with a key value
        TextView text = createTextView("Note");
        linLayout.addView(text);
        key++;
        noteArray.put(key, text);

        //calls the notescreen to store the note information into a database
        Intent noteScreen = new Intent(this, NoteScreen.class);
        //passes the key to store the correct information
        noteScreen.putExtra("key", key);
        startActivity(noteScreen);
    }

    public TextView createTextView(String text) {
        //creates a new textview with specified attributes
        final LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newNote = new TextView(MainActivity.this);

        newNote.setLayoutParams(cParams);
        newNote.setId(key);
        newNote.setText(text);
        newNote.setWidth(411);
        newNote.setHeight(64);
        newNote.setTextColor(Color.WHITE);
        newNote.setTextSize(20);

        return newNote;
    }
    @Override
    public void onResume(){
        super.onResume();
        //get shared preference for an image selected in the gallery
        SharedPreferences imageprefs = getSharedPreferences("imagekey", Context.MODE_PRIVATE);
        String imagetitle = imageprefs.getString("imagetitle", "");

        //returns the correct image url
        String url = "drawable/" + imagetitle;

        //get resource id and set the background image in main to the selected image
        int resID = getResources().getIdentifier(url, "drawable", getPackageName());
        ImageView background_image = (ImageView) findViewById(R.id.background_image);
        Drawable newImage = getResources().getDrawable(resID);
        background_image.setImageDrawable(newImage);

        //makes sure that a note exists before trying to update notes
        if(key > 0) {
            //gets a notes title and associated key (the key has to be passed back because onresume is called first when returning to MainAcitvity
            SharedPreferences prefs = getSharedPreferences("titlekey", Context.MODE_PRIVATE);
            String title = prefs.getString("notevalue", "");
            //delete key makes sure that a note exists before attempting to delete it
            int deleteKey = prefs.getInt("deletevalue", 0);
            int newKeyValue = prefs.getInt("keyvalue", 0);

            //get the current note by the id value and set the title of that note at that index
            System.out.println("Key on Resume: " + newKeyValue);
            noteArray.put(newKeyValue, getNote(newNote, newKeyValue));
            getNote(newNote, newKeyValue).setText(title);
            //checking that the note hashmap is adding notes correctly
            System.out.println("Note Array: " + noteArray);
            //note.setText(title);

            //gets the information from the appropriate note that is clicked (calls note screen to get the information from the database)
            newNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent noteScreen = new Intent(MainActivity.this, NoteScreen.class);
                    //passes the correct key value back
                    noteScreen.putExtra("key", getKey(getNote(newNote, newKeyValue)));
                    startActivity(noteScreen);
                }
            });

            //System.out.println("Key Number" + key + " Note title: " + title);

            if(deleteKey == 1) {
                //deletes the note from the hashmap and sets the visibility of that note to GONE
                noteArray.remove(getKey(getNote(newNote, newKeyValue)));

                getNote(newNote, newKeyValue).setVisibility(View.GONE);
            }
        }
        else {
            //checking that a note exists
            System.out.println("Nothing yet");
        }
    }

    //returns the note associated with the appropriate key value
    public TextView getNote(TextView newNote, int key){
        for(Map.Entry<Integer, TextView> entry: noteArray.entrySet()) {
            if (entry.getKey() == key) {
                newNote = entry.getValue();
            }
        }
        return newNote;
    }

//gets the key associated with an appropriate note value
    public int getKey(TextView note){
        int value = 0;
        for(Map.Entry<Integer, TextView> entry: noteArray.entrySet()) {
            if(entry.getValue() == note) {
                value = entry.getKey();
            }
        }
        System.out.println(value);
        return value;
    }
}
