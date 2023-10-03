package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextNote;
    private RadioButton radioButtonLow;
    private RadioButton radioButtonMedium;
    private Button buttonSaveNote;
    private NoteDataBase noteDataBase;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        noteDataBase = NoteDataBase.getInstance(getApplication());
        initViews();

        buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    private void initViews(){
        editTextNote = findViewById(R.id.editTextNote);
        radioButtonLow = findViewById(R.id.radioButtonLow);
        radioButtonMedium = findViewById(R.id.radioButtonMedium);
        buttonSaveNote = findViewById(R.id.buttonSaveNote);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, AddNoteActivity.class);
    }

    private void saveNote(){
        String text = editTextNote.getText().toString().trim();
        int priority;
        if(radioButtonLow.isChecked()){
            priority = 0;
        } else if(radioButtonMedium.isChecked()){
            priority = 1;
        } else {
            priority = 2;
        }
        Note note = new Note(text, priority);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                noteDataBase.notesDao().add(note);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
        thread.start();
    }
}