package com.stenobano.admin.sqlite_databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stenobano.admin.model.Helper_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 05-Feb-18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    Context context;
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "steno";

    // Contacts table name
    private static final String TABLE_CONTACTS = "steno_file";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IMAGE_TWO = "image_two";
    private static final String KEY_AUDIO = "audio";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
       + KEY_TITLE + " TEXT," + KEY_IMAGE + " TEXT," + KEY_IMAGE_TWO + " TEXT,"+ KEY_AUDIO + " TEXT " + ")";
                db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addContact(Helper_Model model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, model.getTitle());
        values.put(KEY_IMAGE, model.getImage());
        values.put(KEY_AUDIO, model.getAudio());

   db.insert(TABLE_CONTACTS, null, values);
       // long rowInserted = db.insert(TABLE_CONTACTS, null, values);


       // db.close();


        // Closing database connection
    }
    public void add_Long_Contact(Helper_Model model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, model.getTitle());
        values.put(KEY_IMAGE, model.getImage());
        values.put(KEY_IMAGE_TWO, model.getImage_two());
        values.put(KEY_AUDIO, model.getAudio());

        db.insert(TABLE_CONTACTS, null, values);
        // long rowInserted = db.insert(TABLE_CONTACTS, null, values);


        // db.close();


        // Closing database connection
    }

    // Getting All Contacts
    public List<Helper_Model> getAllContacts() {
        List<Helper_Model> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Helper_Model contact = new Helper_Model();
                contact.setId((cursor.getString(0)));
                contact.setTitle((cursor.getString(1)));
                contact.setImage((cursor.getString(2)));
                contact.setImage_two((cursor.getString(3)));
                contact.setAudio(cursor.getString(4));

                //Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }





    public List<Helper_Model> getSingleData(String id) {
        List<Helper_Model> contactList = new ArrayList<>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS  + " where "+ KEY_CATEGORY +"="+nam;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM steno_file WHERE id=? ";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { id });

        //  Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {

                    for (int i=0;i<2;i++)
                    {
                        if (i==0)
                        {
                            Helper_Model contact = new Helper_Model();
                            contact.setId((cursor.getString(0)));
                            contact.setTitle((cursor.getString(1)));
                            contact.setImage((cursor.getString(2)));
                            //contact.setImage_two((cursor.getString(3)));
                            contact.setAudio(cursor.getString(4));
                            contactList.add(contact);
                        }
                        else
                        {
                            Helper_Model contact = new Helper_Model();
                            contact.setId((cursor.getString(0)));
                            contact.setTitle((cursor.getString(1)));
                           // contact.setImage((cursor.getString(2)));
                            contact.setImage((cursor.getString(3)));
                          //  contact.setAudio(cursor.getString(4));
                            contactList.add(contact);
                        }

                    }

                }
                while (cursor.moveToNext());
            }

        }
        else {
            // Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();
        }


        // return contact list
        return contactList;
    }


    // Deleting single contact
    public void deleteContact(Helper_Model contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_CONTACTS,"image=?",new String[]{id});
        db.close();
        return i;

    }
    public Cursor getData(String img)
    {
        SQLiteDatabase dp=this.getWritableDatabase();
        Cursor res=dp.rawQuery("Select * from "+ TABLE_CONTACTS + " WHERE image=" + img,null);
        return res;
    }


}

