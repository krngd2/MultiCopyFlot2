package com.example.kiran.multicopyflot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Kiran on 12/13/2016.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="MYDATA";
    public static final int DATABASE_VER = 1;
    public static final String TABLE_NAME = "List";
    public static final String COL_NAME = "name";
    public static final String ID = "_id";
    SQLiteDatabase db;

    Context context;
    public SqliteHelper(Context context){
        super(context,DATABASE_NAME,null,1);

     this.context = context;


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                ID + " integer PRIMARY KEY autoincrement," + // Define a primary key
                COL_NAME + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_POSTS_TABLE);

        Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {



    }

    /*public void saveList(ListItems items){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,items.getName());
        db.insert(TABLE_NAME,null,cv);
    }*/

  /*  public Cursor getList(){
         db =this.getWritableDatabase();
        Cursor mCursor = db.query(TABLE_NAME, new String[] {ID,COL_NAME},
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }*/
}
