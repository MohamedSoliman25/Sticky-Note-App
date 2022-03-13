package com.example.notesapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notesapp.Models.Notes;
import com.example.notesapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {

    EditText etTitle,etNotes;
    ImageView imgSave;
    Notes notes;
    Toolbar toolbar;
    //i will use this variable for check there is notes object for update or for insert data
    boolean isOldNote =  false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);
        etTitle = findViewById(R.id.editText_title);
        etNotes = findViewById(R.id.editText_notes);
        imgSave = findViewById(R.id.imageView_save);

        toolbar = findViewById(R.id.toolbar_notes);
        setSupportActionBar(toolbar);
        //for display back icon in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //go to privous activity when user press on top left arrow back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



//        notes = new Notes();

        if(getIntent().hasExtra("old_note")) {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            etTitle.setText(notes.getTitle());
            etNotes.setText(notes.getNotes());
            isOldNote=true;
        }


        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get title and description of notes and create date and passing it to MainActivity for saving this date in database
                String title = etTitle.getText().toString();
                String description = etNotes.getText().toString();

                if (description.isEmpty()){
                    Toast.makeText(NotesTakerActivity.this, "Please add some notes!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //patter of day like (EEE for weekDay, d for date ,MMM for month ,yyyy for year ,HH for hour,mm for minute ,a for am and pm )
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();
                //if is false doesnot create new object
                if (!isOldNote){
                    notes = new Notes();
                }
                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(formatter.format(date));

                Intent intent = new Intent();
                //i made Note class as Serializable for passing object using intent to MainActivity
                intent.putExtra("note",notes);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });



    }
}