package com.example.giuaky;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddStaff extends AppCompatActivity {

    private LinearLayout linBack;
    private Button btnSave;
    private TextView tvMessage;
    private EditText edtID, edtName;
    private ImageView imgAva;
    private Spinner spnFactory;
    private ImageButton btnAddImg;
    private DatabaseHelper myDatabase;
    private Uri imageUri;
    String pathImg="https://ryl16zv916obj.vcdn.cloud/wp-content/uploads/2020/04/default-profile.png";
    Bitmap bitmap;
    private Staff newStaff;
    List<Integer> listFactory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        myDatabase = new DatabaseHelper(this);
        myDatabase.createDB();
        myDatabase.openDB();

        linBack = findViewById(R.id.lin_add_staff_back);
        btnSave = findViewById(R.id.btn_add_staff_save);
        tvMessage=findViewById(R.id.tvMessage);
        edtID = findViewById(R.id.edt_add_staff_id);
        edtName = findViewById(R.id.edt_add_staff_fullname);
        imgAva = findViewById(R.id.img_add_staff_ava);
        spnFactory = findViewById(R.id.spn_add_staff_factory);
        btnAddImg = findViewById(R.id.btnAddImg);

        linBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();

            }
        });

        edtID.setText(myDatabase.getNextStaffID());

        Transformation transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();
        Picasso.get()
                .load(pathImg)
                .transform(transformation)
                .into(imgAva);

        Intent backMain = new Intent(this, MainActivity.class);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname;
                String lname;
                String fullname = edtName.getText().toString();
                if (fullname.isEmpty()) {
                    tvMessage.setText("Họ và tên không được để trống");
                    tvMessage.setVisibility(View.VISIBLE);
                }else if(pathImg.equals("https://ryl16zv916obj.vcdn.cloud/wp-content/uploads/2020/04/default-profile.png")){
                    Toast.makeText(AddStaff.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                }
                else {
                    fname = fullname.substring(0, edtName.getText().toString().lastIndexOf(' '));
                    lname = fullname.substring(edtName.getText().toString().lastIndexOf(' ') + 1);
                    if (lname.isEmpty()) {
                        tvMessage.setText("Bạn phải nhập đủ họ và tên");
                        tvMessage.setVisibility(View.VISIBLE);
                    } else {
                        try {
                            newStaff = new Staff(edtID.getText().toString(),
                                    fname,
                                    lname,
                                    listFactory.get(spnFactory.getSelectedItemPosition()), pathImg
                            );

                            myDatabase.addStaff(newStaff);
                            tvMessage.setVisibility(View.GONE);
                            setResult(Activity.RESULT_OK);
                            finish();
                            Toast.makeText(AddStaff.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(AddStaff.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }

                    }





                }


            }
        });

        listFactory.add(1);
        listFactory.add(2);
        listFactory.add(3);
        listFactory.add(4);

        ArrayAdapter<Integer> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listFactory);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnFactory.setAdapter(adapter);


    }


    public String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mypath = new File(directory,
                    edtID.getText().toString()+ LocalDateTime.now() +".jpg");
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return directory.getAbsolutePath();
        return mypath.getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){


        if (resultCode == RESULT_OK && requestCode == 1){
            imageUri = data.getData();
            InputStream inputStream= null;
            try {
                inputStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap=BitmapFactory.decodeStream(inputStream);
            Transformation transformation = new RoundedTransformationBuilder()
                    .oval(true)
                    .build();
            Picasso.get()
                    .load(imageUri)
                    .transform(transformation)
                    .into(imgAva);
           pathImg= saveToInternalStorage(bitmap);

            System.out.println("Image Path : " + pathImg);


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddStaff.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


}