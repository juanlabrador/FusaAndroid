package edu.ucla.fusa.android.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.ucla.fusa.android.modelo.academico.ClaseParticular;
import edu.ucla.fusa.android.modelo.academico.LapsoAcademico;
import edu.ucla.fusa.android.modelo.instrumentos.Prestamo;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;
import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;

/**
 * Created by juanlabrador on 19/11/14.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String NAME = "fusa";
    public static final int VERSION = 1;
    private static DataBaseHelper mInstance;

    public static DataBaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(context);
        }
        return mInstance;
    }

    public DataBaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.CREATE_TABLE);
        db.execSQL(EstudianteTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EstudianteTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
