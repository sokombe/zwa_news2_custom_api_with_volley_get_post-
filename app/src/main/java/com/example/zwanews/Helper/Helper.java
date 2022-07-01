package com.example.zwanews.Helper;

/*
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.zwanews.Models.Comments;

import java.util.ArrayList;
import java.util.List;

public class Helper extends SQLiteOpenHelper {
    public Helper(@Nullable Context context) {
        // Nom de la Bd, null, version
        super(context, "productManager", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Here we create tables
        db.execSQL("CREATE TABLE comments(_id INTEGER PRIMARY KEY, fk_new TEXT, content TEXT,date_comment TEXT,user_id TEXT)");

//###############################################################################################################
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS product");
        onCreate(db);
    }

    public void insertComment(Comments comment){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put("fk_new",comment.getFk_new());
        cv.put("content",comment.getContent());
        cv.put("date_comment",comment.getDate_comment());


        db.insert("comments",null,cv);
        db.close();
    }


    public List<Comments> getAllComments(String date){

        final List<Comments> comment = new ArrayList<>();

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM comments WHERE fk_new=?",new String[]{date});

        try {

            // If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }

            do {

                // Read the values of a row in the table using the indexes acquired above
                final String id = cursor.getString(0); // position of the column
                final String fk_new = cursor.getString(0);
                final String content = cursor.getString(2);
                final String date_comment = cursor.getString(3);
                final String user_id = cursor.getString(4);


                comment.add(new Comments( fk_new, content,date_comment,user_id));

            } while (cursor.moveToNext());

            return comment;

        } finally {
            // Don't forget to close the Cursor once you are done to avoid memory leaks.
            // Using a try/finally like in this example is usually the best way to handle this
            cursor.close();

            // close the database
            db.close();
        }
    }

    public int getCommentSize(String fk_new){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM comments where fk_new=?",new String[]{fk_new});
        cursor.moveToFirst();
        db.close();
        return cursor.getCount();
    }
}

*/