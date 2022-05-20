package com.example.finalproject_noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class Gallery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    public void setImage(View v){
        RadioButton default_green = (RadioButton) findViewById(R.id.default_green);
        RadioButton black = (RadioButton) findViewById(R.id.black);
        RadioButton orange = (RadioButton) findViewById(R.id.orange);
        RadioButton purple = (RadioButton) findViewById(R.id.purple);
        RadioButton blue = (RadioButton) findViewById(R.id.blue);
        RadioButton red = (RadioButton) findViewById(R.id.red);
        RadioButton purple_orange = (RadioButton) findViewById(R.id.purple_orange);
        RadioButton blue_pink = (RadioButton) findViewById(R.id.blue_pink);
        RadioButton stars = (RadioButton) findViewById(R.id.stars);
        RadioButton red_black = (RadioButton) findViewById(R.id.red_black);

        String imageChoice = "";

        if(default_green.isChecked()){
            imageChoice = "gradient";
        }
        if(black.isChecked()){
            imageChoice = "black";
        }
        if(orange.isChecked()){
            imageChoice = "orange";
        }
        if(purple.isChecked()){
            imageChoice = "purpleblack";
        }
        if(blue.isChecked()){
            imageChoice = "blue";
        }
        if(red.isChecked()){
            imageChoice = "red";
        }
        if(purple_orange.isChecked()){
            imageChoice = "redpurple";
        }
        if(blue_pink.isChecked()){
            imageChoice = "tech";
        }
        if(stars.isChecked()){
            imageChoice = "stars";
        }
        if(red_black.isChecked()){
            imageChoice = "redhex";
        }

        System.out.println("Image Choice:" + imageChoice);
        SharedPreferences sp1 = getSharedPreferences("imagekey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp1.edit();
        editor.putString("imagetitle", imageChoice);

        editor.commit();
       finish();
    }
}