package com.example.giuaky;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EditStaff extends AppCompatActivity {
    private LinearLayout linBack;
    private Button btnSave;
    private EditText edtID, edtName;
    private ImageView imgAva;
    private Spinner spnFactory;
    private ImageButton btnEditImg;
    private DatabaseHelper myDatabase;
    private Staff staff;
    private Uri imageUri;
    Bitmap bitmap;
    String pathImg;
    List<Integer> listFactory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);
        myDatabase = new DatabaseHelper(this);
        myDatabase.createDB();
        myDatabase.openDB();

        linBack = findViewById(R.id.lin_edit_staff_back);
        btnSave = findViewById(R.id.btn_edit_staff_save);
        edtID = findViewById(R.id.edt_edit_staff_id);
        edtName = findViewById(R.id.edt_edit_staff_fullname);
        imgAva = findViewById(R.id.img_edit_staff_ava);
        spnFactory = findViewById(R.id.spn_edit_staff_factory);


        btnEditImg = findViewById(R.id.btnEditImg);
        Intent intent = getIntent();
        String staffID = intent.getStringExtra("staffID");
        staff=myDatabase.getStaff(staffID);
        edtID.setText(staff.getId());
        edtName.setText(staff.getfName()+" "+staff.getlName());
        File f=new File(staff.getImage());
        Uri uri = null;
        uri = Uri.fromFile(f);
        Transformation transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();
        Picasso.get()
                .load(uri)
                .transform(transformation)
                .into(imgAva);

        linBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        Intent backMain = new Intent(this, MainActivity.class);
        btnEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream inputStream= null;
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap= BitmapFactory.decodeStream(inputStream);
                staff.setfName(edtName.getText().toString().substring(0, edtName.getText().toString().lastIndexOf(' ')))  ;
                staff.setlName(edtName.getText().toString().substring(edtName.getText().toString().lastIndexOf(' ') + 1));
                staff.setFactory(listFactory.get(spnFactory.getSelectedItemPosition()));
                staff.setImage(pathImg);

                myDatabase.updateStaff(staff);


                setResult(Activity.RESULT_OK);
                finish();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(EditStaff.this);
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

    public String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mypath = new File(directory,edtID.getText().toString()+ LocalDateTime.now() +".jpg");
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
        //return directory.getAbsolutePath();
        return mypath.getAbsolutePath();
    }
}