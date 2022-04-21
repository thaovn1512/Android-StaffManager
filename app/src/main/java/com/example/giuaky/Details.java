package com.example.giuaky;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Details extends AppCompatActivity {

    private LinearLayout btn_Back;
    private ImageView imgStaffAva;
    private TextView txtStaffName, txtStaffID, txtStaffFactory, txtGoodProducts, txtBadProducts, txtSumTotal;
    private Button btnDetailsReport;
    private RecyclerView recyclerStaffDays;
    private ListView lstViewDetails;
    private ImageButton btnEdit;
    private Staff staff;

    private DatabaseHelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f6f6f6"));
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        myDatabase = new DatabaseHelper(this);
        myDatabase.createDB();
        myDatabase.openDB();

        Intent intent = getIntent();
        String staffID = intent.getStringExtra("staffID");
        staff = myDatabase.getStaff(staffID);

        btn_Back = findViewById(R.id.lin_details_back);
        imgStaffAva = findViewById(R.id.img_details_ava);
        txtStaffName = findViewById(R.id.txt_details_staff_name);
        txtStaffID = findViewById(R.id.txt_details_staff_id);
        txtStaffFactory = findViewById(R.id.txt_details_staff_factory);
        btnDetailsReport = findViewById(R.id.btn_details_report);
        txtGoodProducts = findViewById(R.id.txt_details_sum_good_product);
        txtBadProducts = findViewById(R.id.txt_details_sum_bad_product);
        txtSumTotal = findViewById(R.id.txt_details_sum_total_price);
        btnEdit = findViewById(R.id.img_edit_staff);

        recyclerStaffDays = findViewById(R.id.list_staff_days);
        lstViewDetails = findViewById(R.id.lstv_detail);

        txtStaffName.setText(staff.getfName() + " " + staff.getlName());
        txtStaffID.setText(staff.getId());
        txtStaffFactory.setText("Phân xưởng " + staff.getFactory());

        File f = new File(staff.getImage());
        Uri uri = null;
        uri = Uri.fromFile(f);
        Transformation transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();
        Picasso.get()
                .load(uri)
                .transform(transformation)
                .into(imgStaffAva);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Details.this, EditStaff.class);
                intent.putExtra("staffID", staffID);
                startActivityForResult(intent, Activity.RESULT_OK);

            }
        });

        StaffDaysRecyclerViewAdapter staffDaysRecyclerViewAdapter = new StaffDaysRecyclerViewAdapter(this, staff.getDays(), new ItemClickInterface() {
            @Override
            public void onDayClick(String id) {

                DetailListViewAdapter detailListViewAdapter = new DetailListViewAdapter(Details.this, R.layout.details_item, staff.getDay(id).getDetailsList());
                lstViewDetails.setAdapter(detailListViewAdapter);

                txtGoodProducts.setText(staff.getDay(id).getCountGoodProduct() + "");
                txtBadProducts.setText(staff.getDay(id).getCountBadProduct() + "");
                txtSumTotal.setText(staff.getDay(id).getSumPrice() + "");

            }
        });
        recyclerStaffDays.setAdapter(staffDaysRecyclerViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerStaffDays.setLayoutManager(linearLayoutManager);

        if (staff.getDays().size() > 0) {
            Day firstDay = staff.getDays().get(0);
            DetailListViewAdapter detailListViewAdapter = new DetailListViewAdapter(this, R.layout.details_item, firstDay.getDetailsList());
            lstViewDetails.setAdapter(detailListViewAdapter);

            txtGoodProducts.setText(firstDay.getCountGoodProduct() + "");
            txtBadProducts.setText(firstDay.getCountBadProduct() + "");
            txtSumTotal.setText(firstDay.getSumPrice() + "");
        } else {
            txtGoodProducts.setText("0");
            txtBadProducts.setText("0");
            txtSumTotal.setText("0");
        }
        btnDetailsReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPDF();
            }
        });
    }

    private void createPDF() {
        if (lstViewDetails.getCount() == 0) {
            Toast.makeText(this, "Không có thông tin để xuất file", Toast.LENGTH_SHORT).show();
        } else {
            PdfDocument pdfDocument = new PdfDocument();
            Paint myPaint = new Paint();
            Paint title = new Paint();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 3000, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.img);
            Bitmap scale=Bitmap.createScaledBitmap(bitmap,1200,520,false);
            canvas.drawBitmap(scale,0,0,myPaint);
            title.setTextAlign(Paint.Align.CENTER);
            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            title.setTextSize(70);
            canvas.drawText("THÔNG TIN CÔNG NHÂN", 600, 70, title);

            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setTextSize(30);
            myPaint.setColor(Color.BLACK);
            canvas.drawText("Mã công nhân: " + txtStaffID.getText(), 20, 590, myPaint);
            canvas.drawText("Tên công nhân: " + txtStaffName.getText(), 20, 640, myPaint);
            canvas.drawText("Tên phân xưởng: " + txtStaffFactory.getText(), 20, 690, myPaint);

            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setStrokeWidth(2);
            canvas.drawRect(20, 780, 1180, 860, myPaint);

            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setStyle(Paint.Style.FILL);
            canvas.drawText("Ngày công", 40, 830, myPaint);
            canvas.drawText("Số thành phẩm", 300, 830, myPaint);
            canvas.drawText("Số phế phẩm", 600, 830, myPaint);
            canvas.drawText("Tổng tiền ngày công", 880, 830, myPaint);

//            canvas.drawLine(290, 790, 290, 850, myPaint);
//            canvas.drawLine(590, 790, 590, 850, myPaint);
//            canvas.drawLine(870, 790, 870, 850, myPaint);
            //canvas.drawLine(1030, 790, 1030, 840, myPaint);

            int totalGood = 0;
            int totalBad = 0;
            int totalPrice = 0;
            int y = 950;
            for (int i = 0; i < staff.getDays().size(); i++) {
                canvas.drawText(staff.getDays().get(i).getDay(), 40, y, myPaint);
                canvas.drawText(String.valueOf(staff.getDays().get(i).getCountGoodProduct()), 300, y, myPaint);
                canvas.drawText(String.valueOf(staff.getDays().get(i).getCountBadProduct()), 600, y, myPaint);
                canvas.drawText(String.valueOf(staff.getDays().get(i).getSumPrice()), 880, y, myPaint);
                totalGood += staff.getDays().get(i).getCountGoodProduct();
                totalBad += staff.getDays().get(i).getCountBadProduct();
                totalPrice += staff.getDays().get(i).getSumPrice();
                y += 100;

            }
            canvas.drawLine(20, 780, 20, y, myPaint);
            canvas.drawLine(280, 780, 280, y, myPaint);
            canvas.drawLine(590, 780, 590, y, myPaint);
            canvas.drawLine(870, 780, 870, y, myPaint);
            canvas.drawLine(1180, 780, 1180, y, myPaint);

            canvas.drawLine(680, y + 50, 1180, y + 50, myPaint);

            myPaint.setTextSize(40);
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Tổng thành phẩm: ", 680, y + 150, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(totalGood), 1180, y + 150, myPaint);

            myPaint.setTextSize(40);

            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Tổng phế phẩm: ", 680, y + 200, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(totalBad), 1180, y + 200, myPaint);

            myPaint.setTextSize(40);

            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Tổng tiền: ", 680, y + 250, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(totalPrice), 1180, y + 250, myPaint);




            pdfDocument.finishPage(page);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), txtStaffID.getText().toString()+".pdf");
            try {
                pdfDocument.writeTo(new FileOutputStream(file));
                Toast.makeText(this, "In thành công", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pdfDocument.close();
        }


    }
}