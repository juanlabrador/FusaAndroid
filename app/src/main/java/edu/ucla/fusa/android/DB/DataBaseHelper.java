package edu.ucla.fusa.android.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juanlabrador on 19/11/14.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String NAME = "fusamovil.sqlite";
    public static final int VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);

        onCreate(db);
    }
}