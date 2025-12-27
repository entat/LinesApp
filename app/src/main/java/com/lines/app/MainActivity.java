package com.lines.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<SignData> signDataList;
    private SignAdapter adapter;
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_THEME = "SelectedTheme_V2";
    private static final String KEY_DATA_LIST = "SignDataList";
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadTheme();
        setTheme(R.style.Theme_Lines);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();

        adapter = new SignAdapter(signDataList, position -> new AlertDialog.Builder(this)
                .setTitle("Sign Remove")
                .setMessage("Remove this sign?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    signDataList.remove(position);
                    saveData();
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .show());
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_sign, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            EditText etLineNumber = dialogView.findViewById(R.id.etLineNumber);
            EditText etStationNumber = dialogView.findViewById(R.id.etStationNumber);
            Button btnDialogAdd = dialogView.findViewById(R.id.btnDialogAdd);

            btnDialogAdd.setOnClickListener(v1 -> {
                String line = etLineNumber.getText().toString();
                String station = etStationNumber.getText().toString();

                if (!line.isEmpty() && !station.isEmpty()) {
                    SignData newSign = new SignData(line, station, "12:00", "13:00", "14:00", "15:00", "16:00");
                    signDataList.add(newSign);
                    saveData();
                    adapter.notifyDataSetChanged();

                    dialog.dismiss();
                }
            });

            dialog.show();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom() + systemBars.bottom);
            return insets;
        });
    }
    private void loadTheme() {
        android.content.SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int themeId = prefs.getInt(KEY_THEME, R.style.Theme_Lines);
        setTheme(themeId);
    }
    private void saveData() {
        android.content.SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();

        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = gson.toJson(signDataList);

        editor.putString(KEY_DATA_LIST, json);
        editor.apply();
    }
    private void loadData() {
        android.content.SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_DATA_LIST, null);

        if (json != null) {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.List<SignData>>() {}.getType();
            signDataList = gson.fromJson(json, type);
        } else {
            signDataList = new ArrayList<>();
        }
    }
}