package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.ucla.fusa.android.modelo.evento.Evento;

/**
 * Created by juanlabrador on 17/01/15.
 */
public class EventoTable {

    private static final String TAG = "EventoTable";
    public static final String TABLE_NAME = "evento";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_LOGISTICA = "logistica";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_HORA = "hora";
    public static final String COLUMN_ID_EVENTO = "evento_id";
    public static final String COLUMN_ID_LUGAR = "lugar_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOMBRE + " TEXT, "
            + COLUMN_FECHA + " DATE, "
            + COLUMN_HORA + " DATE, "
            + COLUMN_LOGISTICA + " BLOB, "
            + COLUMN_ID_EVENTO + " INTEGER, "
            + COLUMN_ID_LUGAR + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private SimpleDateFormat mDateFormat;
    private SimpleDateFormat mTimeFormat;
    private LugarTable mLugarTable;
    private ArrayList<Evento> mEventos;
    

    public EventoTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mLugarTable = new LugarTable(context);
        mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        mTimeFormat = new SimpleDateFormat("hh:mm aa");
        mEventos = new ArrayList<>();
    }

    private ContentValues generarValores (String nombre, byte[] logistica,
                                          Date fecha, Date hora, int evento, int lugar) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_NOMBRE, nombre);
        valores.put(COLUMN_LOGISTICA, logistica);
        valores.put(COLUMN_FECHA, mDateFormat.format(fecha));
        //valores.put(COLUMN_HORA, mTimeFormat.format(hora));
        valores.put(COLUMN_HORA, "21:00");
        valores.put(COLUMN_ID_EVENTO, evento);
        valores.put(COLUMN_ID_LUGAR, lugar);

        return valores;
    }

    public void insertData(String nombre, byte[] logistica,
                           Date fecha, Date hora, int evento, int idLugar) {
        mDatabase.insert(TABLE_NAME, null, generarValores(nombre, logistica, fecha, hora, evento, idLugar));
    }
    
    public ArrayList<Evento> searchEventos(String fecha) {
        String[] condicion = new String[]{fecha};
        String[] columnas = new String[]{COLUMN_ID, COLUMN_NOMBRE,
                COLUMN_FECHA, COLUMN_HORA, COLUMN_LOGISTICA, COLUMN_ID_EVENTO, COLUMN_ID_LUGAR};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_FECHA + "<?", condicion, null, null, null, null);
        mEventos.clear();
        while (mCursor.moveToNext()) {
            Log.i(TAG, "¡Buscando eventos!");
            Log.i(TAG, mCursor.getString(1));
            try {
                mEventos.add(new Evento(
                        mCursor.getInt(5), // ID
                        mCursor.getString(1), // Nombre
                        mCursor.getBlob(4), // Logistica
                        mDateFormat.parse(mCursor.getString(2)), // Fecha
                        //mTimeFormat.parse(mCursor.getString(3)), // Hora
                        null,
                        mLugarTable.searchLugar(String.valueOf(mCursor.getInt(6))), //Lugar
                        "activo"
                ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mEventos;
    }

    public int searchUltimoEvento() {
        int mUltimo = -1;
        String[] columnas = new String[]{COLUMN_ID, COLUMN_NOMBRE,
                COLUMN_FECHA, COLUMN_HORA, COLUMN_LOGISTICA, COLUMN_ID_EVENTO, COLUMN_ID_LUGAR};
        mCursor = mDatabase.query(TABLE_NAME, columnas, null, null, null, null, null, null);
        mEventos.clear();
        while (mCursor.moveToLast()) {
            Log.i(TAG, "¡Buscando eventos!");
            mUltimo = mCursor.getInt(5);
        }
        return mUltimo;
    }
}
