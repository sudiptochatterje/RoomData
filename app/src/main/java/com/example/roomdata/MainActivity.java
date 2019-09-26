package com.example.roomdata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ClickListener, AdapterView.OnItemSelectedListener {

    MyDatabase myDatabase;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    FloatingActionButton floatingActionButton;

    public static final int NEW_NOTE_REQUEST_CODE = 200;
    public static final int UPDATE_NOTE_REQUEST_CODE = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).fallbackToDestructiveMigration().build();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, NoteActivity.class), NEW_NOTE_REQUEST_CODE);
            }
        });

    }

    private void initViews() {
        floatingActionButton = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void launchIntent(int id) {
        startActivityForResult(new Intent(MainActivity.this, NoteActivity.class).putExtra("id", id), UPDATE_NOTE_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }
    @SuppressLint("StaticFieldLeak")
    private void fetchNoteByIdAndInsert(int id) {
        new AsyncTask<Integer, Void, Note>() {
            @Override
            protected Note doInBackground(Integer... params) {
                return myDatabase.daoAccess().fetchNoteListById(params[0]);
            }
            @Override
            protected void onPostExecute(Note noteList)
            {
                recyclerViewAdapter.addRow(noteList);
            }
        }.execute(id);
    }
    @SuppressLint("StaticFieldLeak")
    private void loadAllNotes() {
        new AsyncTask<String, Void, List<Note>>() {
            @Override
            protected List<Note> doInBackground(String... params) {
                return myDatabase.daoAccess().fetchAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> noteList) {
                recyclerViewAdapter.updateNoteList(noteList);
            }
        }.execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == NEW_NOTE_REQUEST_CODE) {
                long id = data.getLongExtra("id", -1);
                Toast.makeText(getApplicationContext(), "Row inserted", Toast.LENGTH_SHORT).show();
                fetchNoteByIdAndInsert((int) id);

            } else if (requestCode == UPDATE_NOTE_REQUEST_CODE) {

                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(), number + " rows deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), number + " rows updated", Toast.LENGTH_SHORT).show();
                }
                loadAllNotes();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No action done by user", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertList(List<Note> noteList) {
        new AsyncTask<List<Note>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Note>... params) {
                myDatabase.daoAccess().insertNoteList(params[0]);

                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(noteList);

    }
}
