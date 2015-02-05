package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import java.util.List;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.ClaseParticular;

public class ListClasesAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity mActivity;
    private List<ClaseParticular> mClases;
    private ClaseParticular mClase;
    private HorarioClasesAdapter mHorarioAdapter;
    private ViewHolder mViewHolder;

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
            mViewHolder.mIntructor = (TextView) convertView.findViewById(R.id.instructor_clase);
            mViewHolder.mDatosInstructor = (GroupContainer) convertView.findViewById(R.id.grupo_instructor_clase);
            mViewHolder.mVerNotas = (GroupContainer) convertView.findViewById(R.id.ver_notas_clases);
            mViewHolder.mListaHorario = (ListView) convertView.findViewById(R.id.lista_horario_clases);
            convertView.setTag(mViewHolder);
            
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mClase = mClases.get(position);
        mViewHolder.mDatosInstructor.clear();
        mViewHolder.mCatedra.setText(mClase.getCatedra().getDescripcion().toUpperCase());
        mViewHolder.mNivel.setText("NIVEL: " + mClase.getNivel().getDescripcion().toUpperCase());
        mViewHolder.mIntructor.setText(mClase.getInstructor().getNombre() + " " + mClase.getInstructor().getApellido());
        mViewHolder.mDatosInstructor.addSimpleTwoButtonLayout(mClase.getInstructor().getTelefonoMovil(), R.drawable.ic_llamada, R.drawable.ic_sms);
        mViewHolder.mDatosInstructor.addSimpleOneButtonLayout(mClase.getInstructor().getCorreo(), R.drawable.ic_correo);
        mViewHolder.mVerNotas.addSimpleMultiTextLayout(R.string.agrupacion_horario_notas);
        mHorarioAdapter = new HorarioClasesAdapter(mActivity, mClase.getHorarioArea());
        mViewHolder.mListaHorario.setAdapter(mHorarioAdapter);
        mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(0).getOneButton().setOnClickListener(this);
        mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(0).getTwoButton().setOnClickListener(this);
        mViewHolder.mDatosInstructor.getSimpleOneButtonLayoutAt(1).getButton().setOnClickListener(this);
        mViewHolder.mVerNotas.getSimpleMultiTextLayoutAt(0).setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        if (mViewHolder.mDatosInstructor != null) {
            if (view == mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(0).getOneButton()) {
                try {
                    mActivity.startActivity(new Intent("android.intent.action.CALL",
                            Uri.parse("tel:" + mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(0).getContent())));
                } catch (Exception e) {
                    SnackbarManager.show(
                            Snackbar.with(mActivity)
                                    .text(R.string.error_llamada));
                    e.printStackTrace();
                }
            } else if (view == mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(0).getTwoButton()) {
                try {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse("smsto:"));
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", mViewHolder.mDatosInstructor.getSimpleTwoButtonLayoutAt(0).getContent());
                    smsIntent.putExtra("sms_body", "");
                    mActivity.startActivity(smsIntent);
                } catch (Exception e) {
                    SnackbarManager.show(
                            Snackbar.with(mActivity)
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.error_enviar_sms));
                    e.printStackTrace();
                }
            } else if (view == mViewHolder.mDatosInstructor.getSimpleOneButtonLayoutAt(1).getButton()) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                    String[] emails = new String[]{mViewHolder.mDatosInstructor.getSimpleOneButtonLayoutAt(1).getContent()};
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
            } else if (view == mViewHolder.mVerNotas.getSimpleMultiTextLayoutAt(0)) {
                //armarListaEvaluaciones();
            }
        }
    }

    public static class ViewHolder {
        TextView mCatedra;
        TextView mNivel;
        TextView mIntructor;
        GroupContainer mDatosInstructor;
        GroupContainer mVerNotas;
        ListView mListaHorario;
    }
}