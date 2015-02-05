package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import java.util.List;

import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.ClaseParticular;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.academico.EvaluacionPorClase;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import me.drakeet.materialdialog.MaterialDialog;

public class ListClasesAdapter extends BaseAdapter {

    private static String TAG = "ListClasesAdapter";
    private Activity mActivity;
    private List<ClaseParticular> mClases;
    private ClaseParticular mClase;
    private HorarioClasesAdapter mHorarioAdapter;
    private ViewHolder mViewHolder;
    private MaterialDialog mCustomEvaluacion;
    private View mViewDialog;
    private ListView mListaEvaluaciones;
    private CircularProgressBar mProgressDialog;
    private LinearLayout mContenedorEvaluaciones;
    private View mBarra1;
    private View mBarra2;
    private View mBarra3;
    private View mBarra4;
    private View mBarra5;
    private TextView mProgreso;
    private EstudianteTable mEstudianteTable;
    private Estudiante mEstudiante;
    private List<EvaluacionPorClase> mEvaluaciones;
    private JSONParser mJSONParser;
    private int mTotalObjetivos;
    private int mObjetivosAprobados;
    private EvaluacionClasesAdapter mEvaluacionAdapter;

    public ListClasesAdapter(Activity activity, List<ClaseParticular> clases) {
        mActivity = activity;
        mClases = clases;
    }

    public void clear() {
        mClases.clear();
    }

    public int getCount() {
        return mClases.size();
    }

    public Object getItem(int position) {
        return mClases.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.custom_item_clases, null);
            mViewHolder.mCatedra = (TextView) convertView.findViewById(R.id.nombre_clase);
            mViewHolder.mNivel = (TextView) convertView.findViewById(R.id.nivel_clase);
            mViewHolder.mDatosInstructor = (GroupContainer) convertView.findViewById(R.id.grupo_instructor_clase);
            mViewHolder.mVerNotas = (GroupContainer) convertView.findViewById(R.id.ver_notas_clases);
            mViewHolder.mListaHorario = (ListView) convertView.findViewById(R.id.lista_horario_clases);
            mViewHolder.mDatosInstructor.addSimpleMultiTextLayout("");
            mViewHolder.mDatosInstructor.addSimpleTwoButtonLayout("", R.drawable.ic_llamada, R.drawable.ic_sms);
            mViewHolder.mDatosInstructor.addSimpleOneButtonLayout("", R.drawable.ic_correo);
            mViewHolder.mVerNotas.addSimpleMultiTextLayout(R.string.agrupacion_horario_notas);

            mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getOneButton().setTag(R.string.TAG_CALL, mClases.get(position).getInstructor().getTelefonoMovil());
            mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getTwoButton().setTag(R.string.TAG_SMS, mClases.get(position).getInstructor().getTelefonoMovil());
            mViewHolder.mDatosInstructor.getSimpleOneButtonLayoutAt(2).getButton().setTag(R.string.TAG_EMAIL, mClases.get(position).getInstructor().getCorreo());
            mViewHolder.mVerNotas.getSimpleMultiTextLayoutAt(0).setTag(R.string.TAG_NOTAS, mClases.get(position).getId());
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        
        mClase = mClases.get(position);
        Log.i(TAG, "ID Clase:" + mClase.getId());
        mViewHolder.mCatedra.setText(mClase.getCatedra().getDescripcion().toUpperCase());
        mViewHolder.mNivel.setText("NIVEL: " + mClase.getNivel().getDescripcion().toUpperCase());
        mViewHolder.mDatosInstructor.getSimpleMultiTextLayoutAt(0).setContent("Instructor: " + mClase.getInstructor().getNombre().toUpperCase() + " " + mClase.getInstructor().getApellido().toUpperCase());
        mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(1).setContent(mClase.getInstructor().getTelefonoMovil());
        mViewHolder.mDatosInstructor.getSimpleOneButtonLayoutAt(2).setContent(mClase.getInstructor().getCorreo());
        mHorarioAdapter = new HorarioClasesAdapter(mActivity, mClase.getHorarioArea());
        mViewHolder.mListaHorario.setAdapter(mHorarioAdapter);
        mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getOneButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mActivity.startActivity(new Intent("android.intent.action.CALL",
                            Uri.parse("tel:" + view.getTag(R.string.TAG_CALL))));
                } catch (Exception e) {
                    SnackbarManager.show(
                            Snackbar.with(mActivity)
                                    .text(R.string.error_llamada));
                    e.printStackTrace();
                }
            }
        });
        
        mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getTwoButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse("smsto:"));
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", (String) view.getTag(R.string.TAG_SMS));
                    smsIntent.putExtra("sms_body", "");
                    mActivity.startActivity(smsIntent);
                } catch (Exception e) {
                    SnackbarManager.show(
                            Snackbar.with(mActivity)
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.error_enviar_sms));
                    e.printStackTrace();
                }
            }
        });
        
        mViewHolder.mDatosInstructor.getSimpleOneButtonLayoutAt(2).getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                    String[] emails = new String[]{(String) view.getTag(R.string.TAG_EMAIL)};
                    mActivity.startActivity(Intent.createChooser(
                            intent.putExtra(Intent.EXTRA_EMAIL, emails)
                                    .putExtra(Intent.EXTRA_SUBJECT, "").setType("message/rfc822"),
                            mActivity.getResources().getString(R.string.mensaje_elegir_cliente_correo)));
                } catch (ActivityNotFoundException e) {
                    SnackbarManager.show(
                            Snackbar.with(mActivity)
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.error_enviar_correo));
                    e.printStackTrace();
                }
            }
        });
        
        mViewHolder.mVerNotas.getSimpleMultiTextLayoutAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "ID CLASE: " + view.getTag(R.string.TAG_NOTAS));
                armarListaEvaluaciones((Integer) view.getTag(R.string.TAG_NOTAS));
            }
        });

        return convertView;
    }


    public static class ViewHolder {
        TextView mCatedra;
        TextView mNivel;
        GroupContainer mDatosInstructor;
        GroupContainer mVerNotas;
        ListView mListaHorario;
    }

    private void armarListaEvaluaciones(int idClase) {
        mJSONParser = new JSONParser();
        mEstudianteTable = new EstudianteTable(mActivity);
        mEstudiante = mEstudianteTable.searchUser();
        mCustomEvaluacion = new MaterialDialog(mActivity);
        mViewDialog = LayoutInflater.from(mActivity).inflate(R.layout.custom_evaluacion_agrupacion, null);

        mListaEvaluaciones = (ListView) mViewDialog.findViewById(R.id.lista_evaluaciones_agrupacion);
        mProgressDialog = (CircularProgressBar) mViewDialog.findViewById(R.id.pb_cargando_evaluacion_agrupacion);
        mContenedorEvaluaciones = (LinearLayout) mViewDialog.findViewById(R.id.contenedor_evaluaciones_agrupacion);
        mBarra1 = mViewDialog.findViewById(R.id.barra_nivel_1);
        mBarra2 = mViewDialog.findViewById(R.id.barra_nivel_2);
        mBarra3 = mViewDialog.findViewById(R.id.barra_nivel_3);
        mBarra4 = mViewDialog.findViewById(R.id.barra_nivel_4);
        mBarra5 = mViewDialog.findViewById(R.id.barra_nivel_5);
        mProgreso = (TextView) mViewDialog.findViewById(R.id.tv_status_progreso);
        new LoadingEvaluacion().execute(idClase, mEstudiante.getId());


        mCustomEvaluacion.setCanceledOnTouchOutside(true);
        mCustomEvaluacion.setBackgroundResource(R.color.gris_fondo);
        mCustomEvaluacion.setView(mViewDialog);
        mCustomEvaluacion.show();

    }

    private class LoadingEvaluacion extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTotalObjetivos = 0;
            mObjetivosAprobados = 0;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            mEvaluaciones = mJSONParser.serviceEvaluacionPorClases(integers[0], integers[1]);
            if (mEvaluaciones != null) {
                if (mEvaluaciones.size() != 0) {
                    return 100;
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Tenemos evaluaciones!");
                    mTotalObjetivos = mEvaluaciones.size();
                    for (int i = 0; i < mTotalObjetivos; i++) {
                        if (mEvaluaciones.get(i).getEstadoCumplimiento().getDescripcion().equals("alcanzado")) {
                            mObjetivosAprobados++;
                        }
                    }
                    marcarProgreso(mTotalObjetivos, mObjetivosAprobados);
                    mEvaluacionAdapter = new EvaluacionClasesAdapter(mActivity, mEvaluaciones);
                    mListaEvaluaciones.setAdapter(mEvaluacionAdapter);
                    mProgressDialog.setVisibility(View.GONE);
                    mContenedorEvaluaciones.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    Log.i(TAG, "¡No tenemos evaluaciones!");
                    mCustomEvaluacion.dismiss();
                    SnackbarManager.show(
                            Snackbar.with(mActivity)
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.error_evaluaciones));
                    break;
            }
        }
    }

    private void marcarProgreso(int total, int aprobados) {
        if (total != 0) {
            int porcentaje = aprobados / total * 100;
            Log.i(TAG, "Porcentaje: " + porcentaje);
            if (0 == porcentaje) {
                mProgreso.setText("");
                mBarra1.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra2.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra3.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra4.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra5.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
            } else if (0 < porcentaje && porcentaje < 20) {
                mProgreso.setText(R.string.progreso_nivel_1);
                mProgreso.setTextColor(mActivity.getResources().getColor(R.color.nivel_1));
                mBarra1.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_1));
                mBarra2.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra3.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra4.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra5.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
            } else if (20 <= porcentaje && porcentaje < 40) {
                mProgreso.setText(R.string.progreso_nivel_2);
                mProgreso.setTextColor(mActivity.getResources().getColor(R.color.nivel_2));
                mBarra1.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_2));
                mBarra2.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_2));
                mBarra3.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra4.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra5.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
            } else if (40 <= porcentaje && porcentaje < 60) {
                mProgreso.setText(R.string.progreso_nivel_3);
                mProgreso.setTextColor(mActivity.getResources().getColor(R.color.nivel_3));
                mBarra1.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_3));
                mBarra2.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_3));
                mBarra3.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_3));
                mBarra4.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
                mBarra5.setBackgroundColor(mActivity.getResources().getColor(android.R.color.darker_gray));
            } else if (60 <= porcentaje && porcentaje < 80) {
                mProgreso.setText(R.string.progreso_nivel_4);
                mProgreso.setTextColor(mActivity.getResources().getColor(R.color.nivel_4));
                mBarra1.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_4));
                mBarra2.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_4));
                mBarra3.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_4));
                mBarra4.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_4));
                mBarra5.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_4));
            } else if (80 <= porcentaje && porcentaje <= 100) {
                mProgreso.setText(R.string.progreso_nivel_5);
                mProgreso.setTextColor(mActivity.getResources().getColor(R.color.nivel_5));
                mBarra1.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_5));
                mBarra2.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_5));
                mBarra3.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_5));
                mBarra4.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_5));
                mBarra5.setBackgroundColor(mActivity.getResources().getColor(R.color.nivel_5));
            }
        }
    }
}