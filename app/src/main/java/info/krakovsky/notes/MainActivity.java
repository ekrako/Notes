package info.krakovsky.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes;
    static ArrayAdapter arrayAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        notes.add("");
        Intent intent = new Intent(getApplicationContext(),EditNoteActivity.class);
        intent.putExtra("index",notes.size()-1);
        startActivity(intent);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView notesListView = findViewById(R.id.notesListView);
        SharedPreferences sharedPreferences =  getSharedPreferences("info.krakovsky.notes", Context.MODE_PRIVATE);
        String notesJson = sharedPreferences.getString("notes","");
        if (notesJson.isEmpty()) {
            notes = new ArrayList<>();
        }else{
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            notes=gson.fromJson(notesJson,type);
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),EditNoteActivity.class);
                intent.putExtra("index",position);
                startActivity(intent);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int deletePosition = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Are you sure?")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(deletePosition);
                                MainActivity.arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences =  getSharedPreferences("info.krakovsky.notes", Context.MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String notesJson = gson.toJson(MainActivity.notes);
                                prefsEditor.putString("notes", notesJson);
                                prefsEditor.commit();

                            }
                        }).setNegativeButton("No",null)
                        .show();

                return true;
            }
        });
    }

}
