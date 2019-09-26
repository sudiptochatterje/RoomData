package com.example.roomdata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

public class NoteActivity extends AppCompatActivity {

    Spinner spinner;
    EditText inTitle, inDesc;
    Button btnDone, btnDelete;
    boolean isNewNote = false;
    MyDatabase myDatabase;

    Note updateNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inTitle = findViewById(R.id.inTitle);
        inDesc = findViewById(R.id.inDescription);
        btnDone = findViewById(R.id.btnDone);
        btnDelete = findViewById(R.id.btnDelete);

        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).build();

        int note_id = getIntent().getIntExtra("id", -100);

        if (note_id == -100)
            isNewNote = true;

        if (!isNewNote) {
            fetchNoteById(note_id);
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewNote) {
                    Note note = new Note();
                    note.name = inTitle.getText().toString();
                    note.description = inDesc.getText().toString();
                    insertRow(note);
                } else {

                    updateNote.name = inTitle.getText().toString();
                    updateNote.description = inDesc.getText().toString();
                    updateRow(updateNote);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRow(updateNote);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save:
                Note notes=new Note();
                String noteTitle=title.getText().toString();
                String noteDescription=description.getText().toString();
                notes.setTitle(noteTitle);
                notes.setDescription(noteDescription);
                MainActivity.myAppDatabase.myDao().addNote(notes);
                Toast.makeText(getApplicationContext(), "Note saved sucessfully",Toast.LENGTH_SHORT).show();
                title.setText("");
                description.setText("");
                break;
            case R.id.back:
                startActivity(new Intent(AddNewNote.this,MainActivity.class));
                Toast.makeText(getApplicationContext(),"back",Toast.LENGTH_SHORT).show();
                break;
        }
        return(super.onOptionsItemSelected(item));
    }
    @SuppressLint("StaticFieldLeak")
    private void fetchNoteById(final int note_id) {
        new AsyncTask<Integer, Void, Note>() {
            @Override
            protected Note doInBackground(Integer... params) {

                return myDatabase.daoAccess().fetchNoteListById(params[0]);

            }

            @Override
            protected void onPostExecute(Note note) {
                super.onPostExecute(note);
                inTitle.setText(note.name);
                inDesc.setText(note.description);
                updateNote = note;
            }
        }.execute(note_id);

    }

    @SuppressLint("StaticFieldLeak")
    private void insertRow(Note note) {
        new AsyncTask<Note, Void, Long>() {
            @Override
            protected Long doInBackground(Note... params) {
                return myDatabase.daoAccess().insertNote(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(note);

    }

    @SuppressLint("StaticFieldLeak")
    private void deleteRow(Note note) {
        new AsyncTask<Note, Void, Integer>() {
            @Override
            protected Integer doInBackground(Note... params) {
                return myDatabase.daoAccess().deleteNote(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isDeleted", true).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(note);

    }


    @SuppressLint("StaticFieldLeak")
    private void updateRow(Note note) {
        new AsyncTask<Note, Void, Integer>() {
            @Override
            protected Integer doInBackground(Note... params) {
                return myDatabase.daoAccess().updateNote(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isNew", false).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(note);

    }
    public void back(View view)
    {
        Intent intent=new Intent(NoteActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
