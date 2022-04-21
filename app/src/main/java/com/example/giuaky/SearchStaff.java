package com.example.giuaky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchStaff extends AppCompatActivity implements TextWatcher {

    private EditText edtSearch;
    private ImageView btnClose;
    private RecyclerView recyclerStaffList;

    private CustomRecyclerViewAdapter customRecyclerViewAdapter = null;
    private ArrayList<Staff> arrayListStaff = null;

    private DatabaseHelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_staff);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        myDatabase = new DatabaseHelper(this);
        myDatabase.createDB();
        myDatabase.openDB();

        edtSearch = findViewById(R.id.edt_search_search);
        btnClose = findViewById(R.id.btn_search_close);
        recyclerStaffList = findViewById(R.id.recyle_search_staff_list);

        edtSearch.addTextChangedListener(this);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        arrayListStaff = new ArrayList<>();
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this, arrayListStaff, false);
        recyclerStaffList.setAdapter(customRecyclerViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerStaffList.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(edtSearch.getText().length()>0){
            arrayListStaff.clear();
            arrayListStaff = myDatabase.findStaffs(edtSearch.getText().toString());
            customRecyclerViewAdapter.setNewData(arrayListStaff);
        }
        else{
            arrayListStaff.clear();
            customRecyclerViewAdapter.setNewData(arrayListStaff);
        }
        customRecyclerViewAdapter.notifyDataSetChanged();
        recyclerStaffList.setAdapter(customRecyclerViewAdapter);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}