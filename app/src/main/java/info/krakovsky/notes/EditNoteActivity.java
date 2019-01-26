package info.krakovsky.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;

public class EditNoteActivity extends AppCompatActivity {
    Integer index;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Intent intent = getIntent();
        index =intent.getIntExtra("index",-1);
        if (index>-1) {
            String note = MainActivity.notes.get(index);
            editText = findViewById(R.id.editText);
            editText.setText(note);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }else{
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.notes.set(index,editText.getText().toString());
        MainActivity.arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences =  getSharedPreferences("info.krakovsky.notes", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String notesJson = gson.toJson(MainActivity.notes);
        prefsEditor.putString("notes", notesJson);
        prefsEditor.commit();
        super.onBackPressed();
    }
}
