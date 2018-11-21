package savion.tns.com.savion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NANDHU on 20-12-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "savion";

    // Contacts table name
    private static final String TABLE_PROFILE = "service";
    private static final String TABLE_DISTRICT = "district";
    // Contacts Table Columns names
    private static final String name = "name";
    private static final String email = "email";
    private static final String mobile = "mobile";
    private static final String place = "place";
    private static final String vehicleno = "vehicleno";
    private static final String servicetype = "servicetype";
    private static final String date = "date";
    private static final String time = "time";
    private static final String vehicletype = "vehicletype";
    private static final String remarks = "remarks";
    SQLiteDatabase db = this.getWritableDatabase();
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + "("
                + name + " TEXT," + email + " TEXT," + mobile + " TEXT," + place + " TEXT," + vehicleno + " TEXT," + servicetype + " TEXT," + date + " TEXT," + time + " TEXT," + vehicletype + " TEXT,"+ remarks+ " TEXT)";
        db.execSQL(CREATE_PROFILE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        // Create tables again
        onCreate(db);
    }



   public void adddetail(String name1,String email1,String mobile1,String place1,String vehicleno1,String servicetype1,String date1,String time1,String vehicletype1,String remarks1) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put(name, name1);
       values.put(email, email1);
       values.put(mobile, mobile1);
       values.put(place, place1);
       values.put(vehicleno, vehicleno1);
       values.put(servicetype, servicetype1);
       values.put(date, date1);
       values.put(time, time1);
       values.put(vehicletype, vehicletype1);
       values.put(remarks, remarks1);
       // Inserting Row
       db.insert(TABLE_PROFILE, null, values);
       db.close(); // Closing database connection
   }

    public List<tickets> getAllEducation() {
        List<tickets> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PROFILE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                tickets book=new tickets();
                book.setName(cursor.getString(0));
                book.setMobile(cursor.getString(2));
                book.setPlace(cursor.getString(3));
                book.setVehicleno(cursor.getString(4));
                book.setServicetype(cursor.getString(5));
                book.setDate(cursor.getString(6));
                book.setTime(cursor.getString(7));
                book.setVehicletype(cursor.getString(8));
                book.setRemarks(cursor.getString(9));
                contactList.add(book);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public String getdistrict(String name){
        // Getting single contact

        Cursor cursor = null;
        String empName = "";
            String selectQuery = "SELECT districtauto FROM " + TABLE_DISTRICT+" WHERE district='"+name+"'";
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            /*if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                empName = cursor.getString(cursor.getColumnIndex("districtid"));

            }*/
            if (cursor.moveToFirst()) {
                do {
                    empName=cursor.getString(cursor.getColumnIndex("districtauto"));
                } while (cursor.moveToNext());
            }
        return empName;
    }


}
