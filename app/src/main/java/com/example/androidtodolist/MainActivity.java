package com.example.androidtodolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerviewUser;
    ProgressBar progressBar;
    AppDatabase db;
    ToDoAdapter toDoAdapter;
    public static List<ToDo> Tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        recyclerviewUser = findViewById(R.id.recyclerview_id);
        recyclerviewUser.setLayoutManager(new LinearLayoutManager((this)));

//        final ToDoAdapter todoAdapter = new ToDoAdapter();
//        recyclerviewUser.setAdapter(todoAdapter);

        final Button btn_Add = (Button) findViewById(R.id.btn_ADD);

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertConfirm("Cofirm", "Would you like to add a new task ");

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getandDisplayTask();

    }

    public void getandDisplayTask() {
        new AsyncTask<Void, Void, List<ToDo>>() {
            @Override
            protected List<ToDo> doInBackground(Void... voids) {
                Tasks = db.toDoDao().getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("array", Tasks.get(0).task.toString());
                        toDoAdapter = new ToDoAdapter(this, Tasks);
                        //toDoAdapter.setOnClick(MainActivity.this);
                        recyclerviewUser.setAdapter(toDoAdapter);


                    }
                });
                return null;
            }
        }.execute();
    }


    public void onClickItemDelete(final int position) {
        Log.d("2", "2  " + position);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.toDoDao().delete(Tasks.get(position));
                Tasks.remove(position);

                return null;
            }
        }.execute();
        getandDisplayTask();
        Toast.makeText(getApplicationContext(), "Delete task successfully", Toast.LENGTH_SHORT).show();
    }

    private void showAlertConfirm(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, Activity_Add_ToDoList.class);
                        startActivity(intent);
//                        progressBar.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Lam chi do
                    }
                })
                .show();
    }
}
