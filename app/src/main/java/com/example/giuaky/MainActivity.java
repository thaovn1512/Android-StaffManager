package com.example.giuaky;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewStaffList;
    private ImageView btnAddPerson, btnSearch;

    private ArrayList<Staff> staffs = new ArrayList<>();
    private CustomRecyclerViewAdapter staffListAdapter = null;

    private DatabaseHelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        myDatabase = new DatabaseHelper(this);
        myDatabase.createDB();
        myDatabase.openDB();

        staffs = myDatabase.getAllStaffs();

        recyclerViewStaffList = findViewById(R.id.staff_list);
        btnAddPerson = findViewById(R.id.btn_main_add_person);
        btnSearch = findViewById(R.id.btn_main_search);

        Intent intent = new Intent(this, Details.class);
        Intent intent1 = new Intent(this, EditStaff.class);
        Intent toAddStaff = new Intent(this, AddStaff.class);
        ActivityResultLauncher<Intent> toAddStaffResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            updateList();
                        }
                    }
                });
        ActivityResultLauncher<Intent> toEditStaffResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            updateList();
                        }
                    }
                });

        staffListAdapter = new CustomRecyclerViewAdapter(this, staffs, new StaffLongClickItemListener() {
            @Override
            public void onStaffClick(String staffID, int typeClick) {
                if (typeClick == STAFF_LONG_CLICK_REMOVE) {

                    Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_dialog);
                    Window window = dialog.getWindow();
                    if (window == null) {
                        return;
                    }
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    WindowManager.LayoutParams windowAttributes = window.getAttributes();
                    windowAttributes.gravity = Gravity.CENTER;
                    window.setAttributes(windowAttributes);

                    //khi click ra ngoai thi se thoat
                    if (Gravity.CENTER == Gravity.CENTER) {
                        dialog.setCancelable(true);
                    } else {
                        dialog.setCancelable(false);
                    }

                    TextView txtTieuDe = dialog.findViewById(R.id.txtTieuDe);
                    TextView txtNoiDung = dialog.findViewById(R.id.txtNoiDung);
                    txtTieuDe.setText("Xóa công nhân");
                    txtNoiDung.setText("Bạn thực sự muốn xoá công nhân  " + staffID.toString());
                    Button btnYes = dialog.findViewById(R.id.btnYes);
                    Button btnCancel = dialog.findViewById(R.id.btnCancel);

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                myDatabase.removeStaff(staffID);
                                updateList();
                                dialog.dismiss();
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "Có lỗi xảy ra vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    dialog.show();



                } else if (typeClick == STAFF_LONG_CLICK_VIEW) {
                    Toast.makeText(MainActivity.this, "Detail", Toast.LENGTH_SHORT).show();
                    intent.putExtra("staffID", staffID);
                    startActivity(intent);
                }
                else if(typeClick== STAFF_LONG_CLICK_EDIT){
                    intent1.putExtra("staffID",staffID);
                    toEditStaffResultLauncher.launch(intent1);
                   // startActivityForResult(intent1,1);


                }
            }
        });

        recyclerViewStaffList.setAdapter(staffListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewStaffList.setLayoutManager(linearLayoutManager);


        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddStaffResultLauncher.launch(toAddStaff);
            }
        });

        Intent toSearch = new Intent(this, SearchStaff.class);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toSearch);
            }
        });

    }

    public void updateList() {
        staffs.clear();
        staffs = myDatabase.getAllStaffs();
        staffListAdapter.setNewData(staffs);
        staffListAdapter.notifyDataSetChanged();
        recyclerViewStaffList.setAdapter(staffListAdapter);
    }

}