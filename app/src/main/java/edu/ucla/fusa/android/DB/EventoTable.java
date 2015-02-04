package edu.ucla.fusa.android.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.ucla.fusa.android.modelo.evento.Evento;
import edu.ucla.fusa.android.modelo.evento.Lugar;

/**
 * Created by juanlabrador on 17/01/15.
 */
public class EventoTable {

    private static final String TAG = "EventoTable";
    public static final String TABLE_NAME = "evento";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_HORA = "hora";
    public static final String COLUMN_ID_EVENTO = "evento_id";
    public static final String COLUMN_ID_LUGAR = "lugar_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOMBRE + " TEXT, "
            + COLUMN_FECHA + " TEXT, "
            + COLUMN_HORA + " TEXT, "
            + COLUMN_DESCRIPCION + " TEXT, "
            + COLUMN_ID_EVENTO + " INTEGER, "
            + COLUMN_ID_LUGAR + " INTEGER);";

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private SimpleDateFormat mDateFormat;
    private SimpleDateFormat mTimeFormat;
    private LugarTable mLugarTable;
    private ArrayList<Evento> mEventos;
    private Calendar mMesAnterior;
    private Evento mEvento;
    private Lugar mLugar;
    

    public EventoTable(Context context) {
        mHelper = DataBaseHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
        mLugarTable = new LugarTable(context);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTimeFormat = new SimpleDateFormat("hh:mm aa");
        mEventos = new ArrayList<>();
        mMesAnterior = Calendar.getInstance();
        mMesAnterior.set(Calendar.DAY_OF_MONTH, 1);
        mMesAnterior.set(Calendar.MONTH, mMesAnterior.get(Calendar.MONTH) - 1);
        mEvento = new Evento();
        mLugar = new Lugar();
    }

    private ContentValues generarValores (String nombre, String descripcion,
                                          String fecha, Date hora, int evento, int lugar) {

        ContentValues valores = new ContentValues();
        valores.put(COLUMN_NOMBRE, nombre);
        valores.put(COLUMN_DESCRIPCION, descripcion);
        valores.put(COLUMN_FECHA, fecha);
        valores.put(COLUMN_HORA, mTimeFormat.format(hora));
        valores.put(COLUMN_ID_EVENTO, evento);
        valores.put(COLUMN_ID_LUGAR, lugar);

        return valores;
    }

    public void insertData(String nombre, String descripcion,
                           String fecha, Date hora, int evento, int idLugar) {
        mDatabase.insert(TABLE_NAME, null, generarValores(nombre, descripcion, fecha, hora, evento, idLugar));
    }
    
    public ArrayList<Evento> searchEventos() {
        Date mViejaFecha;
        String tiraSQL = "SELECT * FROM " + TABLE_NAME;
        mEventos.clear();
        mCursor = mDatabase.rawQuery(tiraSQL, null);
        while (mCursor.moveToNext()) {
            Log.i(TAG, "¡Buscando eventos!");
            Log.i(TAG, "¡Overflow!");
            Log.i(TAG, mCursor.getString(1));
            try {
                mViejaFecha = mDateFormat.parse(mCursor.getString(2));
                Log.i(TAG, "Fecha Evento: " + mCursor.getString(2) + " Fecha mes: " + mDateFormat.format(mMesAnterior.getTime()));
                if (mViejaFecha.after(mMesAnterior.getTime())) {  // Si la fecha del evento no expiro
                    Log.i(TAG, "¡Evento aún disponible!");
                    mEventos.add(new Evento(
                            mCursor.getInt(5), // ID
                            mCursor.getString(1), // Nombre
                            mCursor.getString(4), // Descrpcion
                            mCursor.getString(2), // Fecha
                            mTimeFormat.parse(mCursor.getString(3)), // Hora
                            mLugarTable.searchLugar(mCursor.getInt(6)), //Lugar
                            "activo"
                    ));
                } else {
                    Log.i(TAG, "¡Borrando viejo evento!");
                    delete(mCursor.getInt(0));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mEventos;
    }

    public Evento searchEvento(int id) {
        Log.i(TAG, "¡Buscando el evento!");
        String[] columnas = new String[]{COLUMN_NOMBRE, COLUMN_DESCRIPCION, COLUMN_FECHA, COLUMN_HORA, COLUMN_ID_EVENTO, COLUMN_ID_LUGAR};
        mCursor = mDatabase.query(TABLE_NAME, columnas, COLUMN_ID_EVENTO + "=?", new String[] {String.valueOf(id)}, null, null, null);
        while (mCursor.moveToFirst()){
            mEvento.setId(mCursor.getInt(4));
            mEvento.setNombre(mCursor.getString(0));
            mEvento.setDescripcion(mCursor.getString(1));
            mEvento.setFecha(mCursor.getString(2));
            try {
                mEvento.setHora(mTimeFormat.parse(mCursor.getString(3)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mLugar = mLugarTable.searchLugar(mCursor.getInt(5));
            if (mLugar != null) {
                mEvento.setLugar(mLugar);
            }

            return mEvento;
        }
        return null;
    }
    
    public int searchUltimoEvento() {
        Log.i(TAG, "¡Buscando el ultimo!");
        String[] columnas = new String[]{"MAX(" + COLUMN_ID_EVENTO + ")"};
        mCursor = mDatabase.query(TABLE_NAME, columnas, null, null, null, null, null);
        mCursor.moveToFirst();
        return mCursor.getInt(0);
    }

    public void delete(int id) {
        mDatabase.delete(TABLE_NAME, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
    }
}
