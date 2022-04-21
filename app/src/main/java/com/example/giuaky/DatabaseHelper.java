package com.example.giuaky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "STAFFSDB.db";
    private static String DATABASE_PATH;

    private Context context;

    private SQLiteDatabase database;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

        if(Build.VERSION.SDK_INT>=17){
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else{
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

        this.context = context;

    }

    private void copyDB() throws IOException {
        InputStream inputStream = this.context.getAssets().open(DATABASE_NAME);
        String outFile = DATABASE_PATH+DATABASE_NAME;
        OutputStream outputStream = new FileOutputStream(outFile);

        byte[] buffer = new byte[1024];
        int mLength;
        while((mLength = inputStream.read(buffer))>0){
            outputStream.write(buffer, 0, mLength);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private boolean isDBExist(){
        File dbFile = new File(DATABASE_PATH+DATABASE_NAME);
        return dbFile.exists();
    }

    public void createDB(){
        if(!isDBExist()){
            this.getReadableDatabase();
            this.close();
            try{
                copyDB();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean openDB(){
        this.database = SQLiteDatabase.openDatabase(DATABASE_PATH+DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return this.database!=null;
    }

    public synchronized void closeDB(){
        if(this.database!=null){
            this.database.close();
        }
        super.close();
    }

    public ArrayList<Staff> getAllStaffs(){

        ArrayList<Staff> staffs = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "SELECT STAFFS.*, COUNT(CHAMCONG.MACN) FROM STAFFS LEFT JOIN CHAMCONG ON STAFFS.STAFF_ID=CHAMCONG.MACN GROUP BY STAFFS.STAFF_ID";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if(cursor!=null){
            if(cursor.getCount()>0){

                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    staffs.add(new Staff(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getInt(5)
                    ));
                    cursor.moveToNext();
                }

            }
        }

        return staffs;

    }

    public Staff getStaff(String id){

        Staff staff = new Staff();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "SELECT * FROM STAFFS WHERE STAFF_ID = '"+id+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if(cursor!=null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                staff = new Staff(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                );
            }

        }

        ArrayList<Day> dayList = new ArrayList<>();

        sqLiteDatabase = this.getWritableDatabase();
        sql = "SELECT * FROM CHAMCONG WHERE MACN = '"+id+"'";
        Cursor cursor2 = sqLiteDatabase.rawQuery(sql, null);
        if(cursor2!=null) {
            if (cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                while(!cursor2.isAfterLast()){

                    String idCC = cursor2.getString(0);

                    SQLiteDatabase sqLiteDatabase2 = this.getWritableDatabase();
                    sql = "SELECT CHITIETCHAMCONG.MACC, SANPHAM.MASP, SANPHAM.TENSP, SANPHAM.DONGIA, SANPHAM.IMAGE, CHITIETCHAMCONG.SOTHANHPHAM, CHITIETCHAMCONG.SOPHEPHAM FROM CHITIETCHAMCONG, SANPHAM WHERE CHITIETCHAMCONG.MASP = SANPHAM.MASP AND CHITIETCHAMCONG.MACC = '"+idCC+"'";
                    Cursor cursor3 = sqLiteDatabase2.rawQuery(sql, null);

                    ArrayList<DayDetails> dayDetailsList = new ArrayList<>();
                    if(cursor3!=null){
                        if(cursor3.getCount()>0){
                            cursor3.moveToFirst();
                            while(!cursor3.isAfterLast()){

                                dayDetailsList.add(new DayDetails(
                                   new Product(cursor3.getString(1), cursor3.getString(2), cursor3.getLong(3), cursor3.getString(4)),
                                   cursor3.getInt(5),
                                   cursor3.getInt(6)
                                ));

                                cursor3.moveToNext();
                            }
                        }
                    }

                    dayList.add(new Day(
                       idCC,
                       cursor2.getString(1),
                       dayDetailsList
                    ));

                    cursor2.moveToNext();
                }
            }

        }

        staff.setDays(dayList);

        return staff;

    }

    public ArrayList<Staff> findStaffs(String text){

        ArrayList<Staff> staffs = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "SELECT STAFFS.*, COUNT(STAFFS.STAFF_ID) FROM STAFFS LEFT JOIN CHAMCONG ON STAFFS.STAFF_ID=CHAMCONG.MACN WHERE STAFFS.staff_fname LIKE '%"+text+"%' OR STAFFS.staff_lname LIKE '%"+text+"%' GROUP BY STAFFS.STAFF_ID";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if(cursor!=null){
            if(cursor.getCount()>0){

                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    staffs.add(new Staff(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getInt(5)
                    ));
                    cursor.moveToNext();
                }

            }
        }

        return staffs;

    }

    public String getNextStaffID(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "SELECT STAFFS.STAFF_ID FROM STAFFS ORDER BY STAFFS.STAFF_ID DESC LIMIT 1";

        int id = 0;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if(cursor!=null){
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                String staffID = cursor.getString(0);
                id = Integer.parseInt(staffID.substring(2));
            }
        }

        return "CN"+(id+1<10?"0":"")+(id+1+"");

    }

    public void addStaff(Staff s){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("STAFF_ID", s.getId());
        values.put("STAFF_FNAME", s.getfName());
        values.put("STAFF_LNAME", s.getlName());
        values.put("STAFF_FACTORY", s.getFactory());
        values.put("STAFF_AVATAR", s.getImage());

        sqLiteDatabase.insert("STAFFS", null, values);
        sqLiteDatabase.close();
    }
    public void updateStaff(Staff staff) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STAFF_ID", staff.getId());
        values.put("STAFF_FNAME", staff.getfName());
        values.put("STAFF_LNAME", staff.getlName());
        values.put("STAFF_FACTORY", staff.getFactory());
        values.put("STAFF_AVATAR", staff.getImage());

        sqLiteDatabase.update("STAFFS", values, "STAFF_ID" + " = ?", new String[] { String.valueOf(staff.getId()) });
        sqLiteDatabase.close();
    }
    public void removeStaff(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete("STAFFS", "STAFF_ID" + " = ?", new String[] { id });
        sqLiteDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
