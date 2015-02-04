package edu.ucla.fusa.android.fragmentos;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.juanlabrador.dateslider.SliderContainer;
import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.fundacion.Aspirante;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;
import edu.ucla.fusa.android.validadores.ValidadorEmails;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AspiranteFragment extends Fragment implements SliderContainer.OnTimeChangeListener , TextWatcher, Toolbar.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {

    private static String TAG = "IncialPostulacionesFragment";
    private GroupContainer mCedula;
    private GroupContainer mDatosBasicos;
    private GroupContainer mDatosMusicales;
    private GroupContainer mDatosContacto;
    private SliderContainer mContainerDate;
    protected Calendar mInitialTime;
    private Toolbar mToolbar;
    private ScrollView mScroll;
    private CircularProgressBar mProgressBar;
    private ArrayList<String> mCustomMenu;
    private View mView;
    private JSONParser jsonParser = new JSONParser();
    private List<TipoInstrumento> mInstrumentos = new ArrayList<>();
    private Aspirante mAspirante = new Aspirante();
    private int age;
    private DrawerArrowDrawable mDrawerArrow;
    private LinearLayout mParent;
    private NotificationManager mManager;
    private VistasPrincipalesActivity mActivity;

    public static AspiranteFragment newInstance() {
        AspiranteFragment fragment = new AspiranteFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        mView = inflater.inflate(R.layout.fragment_inicial_postulaciones, container, false);

        mParent = (LinearLayout) mView.findViewById(R.id.fragment_postulacion);
        mParent.setBackgroundColor(getResources().getColor(R.color.gris_fondo));
        mScroll = (ScrollView) mView.findViewById(R.id.scroll_postulaciones);
        mScroll.setVisibility(View.GONE);

        mCedula = (GroupContainer) mView.findViewById(R.id.cedula);
        mCedula.addEditTextLayout(R.string.postularse_cedula);
        mCedula.getEditTextLayoutAt(0).setInputType(InputType.TYPE_CLASS_NUMBER);
        mCedula.getEditTextLayoutAt(0).setMaxLength(8);
        mCedula.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        
        mDatosBasicos = (GroupContainer) mView.findViewById(R.id.grupo_datos_basicos);
        mDatosBasicos.addEditTextLayout(R.string.postularse_nombre);
        mDatosBasicos.addEditTextLayout(R.string.postularse_apellido);
        mDatosBasicos.addPopupLayout(R.string.postularse_sexo, R.menu.sexo);
        mDatosBasicos.addTextLayout(R.string.postularse_fecha_nacimiento);


        mDatosBasicos.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mDatosBasicos.getEditTextLayoutAt(1).getEditText().addTextChangedListener(this);
        
        mDatosMusicales = (GroupContainer) mView.findViewById(R.id.grupo_datos_musicales);
        mDatosMusicales.addSwitchLayout(R.string.postularse_instrumento);
        mDatosMusicales.getSwitchLayoutAt(0).setSwitchColor(getResources().getColor(R.color.azul));

        
        mDatosContacto = (GroupContainer) mView.findViewById(R.id.grupo_datos_contacto);
        mDatosContacto.addEditTextLayout(R.string.postularse_telefono);
        mDatosContacto.addValidatorLayout(R.string.postularse_correo);
        mDatosContacto.getEditTextLayoutAt(0).setMaxLength(11);
        mDatosContacto.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mDatosContacto.getValidatorLayoutAt(1).getEditText().addTextChangedListener(this);
        mDatosContacto.getEditTextLayoutAt(0).setInputType(InputType.TYPE_CLASS_NUMBER);
        mDatosContacto.getValidatorLayoutAt(1).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        mContainerDate = (SliderContainer) mView.findViewById(R.id.fecha_nacimiento);
        mContainerDate.setOnTimeChangeListener(this);
        mInitialTime = Calendar.getInstance();
        mContainerDate.setMaxTime(mInitialTime);
        mContainerDate.setTime(mInitialTime);
        
        mProgressBar = (CircularProgressBar) mView.findViewById(R.id.cargando_postulaciones);

        new LoadingInstrumentos().execute();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (mToolbar == null) {  // Esta en la parte externa
            mDrawerArrow = new DrawerArrowDrawable(getActivity()) {
                @Override
                public boolean isLayoutRtl() {
                    return false;
                }
            };

            mDrawerArrow.setProgress(1f);

            mToolbar = (Toolbar) view.findViewById(R.id.toolbar_interna);
            mToolbar.setTitle(R.string.postularse_titulo_barra);
            mToolbar.setNavigationIcon(mDrawerArrow);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().popBackStack();
                }
            });
            mToolbar.inflateMenu(R.menu.action_enviar);
            mToolbar.setOnMenuItemClickListener(this);
        } else {  // Esta dentro de la cuenta
            mToolbar.setTitle(R.string.postularse_titulo_barra_2);
            mToolbar.inflateMenu(R.menu.action_enviar);
            mToolbar.setOnMenuItemClickListener(this);
            mActivity = (VistasPrincipalesActivity) getActivity();
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {        
        switch (menuItem.getItemId()) {
            case R.id.action_enviar:
                if (!mDatosContacto.getValidatorLayoutAt(1).isCheck()) {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                            .text(R.string.mensaje_correo_invalido));
                } else if (mCedula.getEditTextLayoutAt(0).getContent().equals("") && mDatosBasicos.getEditTextLayoutAt(0).getContent().equals("") &&
                        mDatosBasicos.getEditTextLayoutAt(1).getContent().equals("") && mDatosBasicos.getPopupLayoutAt(2).getContent().equals("") &&
                        mDatosMusicales.getPopupLayoutAt(1).getContent().equals("") && mDatosContacto.getEditTextLayoutAt(0).getContent().equals("") && mDatosContacto.getValidatorLayoutAt(1).getContent().equals("")) {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text(R.string.mensaje_complete_campos));
                } else {
                    setAspirante();
                    new UploadAspirante().execute(mAspirante);
                }
                break;
        }
        return true;
    }

    private void setAspirante() {
        Log.i(TAG, "Posición: " + mDatosMusicales.getPopupLayoutAt(1).getItemPosition());
        mAspirante.setTipoInstrumento(mInstrumentos.get(mDatosMusicales.getPopupLayoutAt(1).getItemPosition()));
        mAspirante.setNombre(mDatosBasicos.getEditTextLayoutAt(0).getContent());
        mAspirante.setApellido(mDatosBasicos.getEditTextLayoutAt(1).getContent());
        mAspirante.setCedula(mCedula.getEditTextLayoutAt(0).getContent());
        mAspirante.setCorreo(mDatosContacto.getValidatorLayoutAt(1).getContent());
        mAspirante.setTelefonoMovil(mDatosContacto.getEditTextLayoutAt(0).getContent());
        mAspirante.setEdad(age);
        mAspirante.setSexo(mDatosBasicos.getPopupLayoutAt(2).getContent());
        if (mDatosMusicales.getSwitchLayoutAt(0).isChecked()) {
            mAspirante.setInstrumentoPropio("Si");
        } else {
            mAspirante.setInstrumentoPropio("No");
        }
        mAspirante.setEstatus("activo");
        mAspirante.setFechanac(new SimpleDateFormat("yyyy-MM-dd").format(mContainerDate.getTime().getTime()));
        Log.i(TAG, mContainerDate.getTime().getTime().toString());

    }

    protected void setFechaNacimiento() {
        final Calendar c = mContainerDate.getTime();
        mDatosBasicos.getTextLayoutAt(03).setContent(String.format("%td %tb %tY", c, c, c));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mDatosContacto.getValidatorLayoutAt(1).getEditText().getText().hashCode() == editable.hashCode()) {
            if (!ValidadorEmails.validarEmail(mDatosContacto.getValidatorLayoutAt(1).getContent())) {
                mDatosContacto.getValidatorLayoutAt(1).dataError();
            } else {
                mDatosContacto.getValidatorLayoutAt(1).dataCheck();
            }
        }
    }

    @Override
    public void onTimeChange(Calendar calendar) {
        Calendar year = Calendar.getInstance();
        Calendar mDate = calendar;
        setFechaNacimiento();
        if (calendar.getTime().before(mInitialTime.getTime())) {  //Validamos que la fecha de nacimiento sea menor a la fecha actual.
            age = year.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        } 
    }

    private class LoadingInstrumentos extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCustomMenu = new ArrayList<>();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Log.i(TAG, "¡Buscando catedras!");
            SystemClock.sleep(3000);
            if (mDatosMusicales.getPopupLayoutAt(1) != null) {
                mDatosMusicales.getPopupLayoutAt(1).getPopupMenu().getMenu().clear();
            }
            mInstrumentos = jsonParser.serviceLoadingTipoInstrumento();
            if (mInstrumentos == null) {
                return 0;
            } else if (mInstrumentos.size() != 0) {
                for (int i = 0; i < mInstrumentos.size(); i++) {
                    mCustomMenu.add(mInstrumentos.get(i).getDescripcion());
                }
                return 100;
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 0:
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_busqueda));
                    getFragmentManager().popBackStack();
                    Log.i(TAG, "¡Error al buscar!");
                    break;
                case 100:
                    mDatosMusicales.addPopupLayout(R.string.postularse_catedra, mCustomMenu);
                    try {
                        mProgressBar.setVisibility(View.GONE);
                        mScroll.setVisibility(View.VISIBLE);
                        mToolbar.setVisibility(View.VISIBLE);
                        mParent.setBackgroundColor(getResources().getColor(R.color.gris_fondo));
                        Log.i(TAG, "¡Cargando sin problemas!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private class UploadAspirante extends AsyncTask<Aspirante, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mToolbar.getMenu().findItem(R.id.action_enviar).getActionView() == null) {
                mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(R.layout.custom_progress_bar);
            }
            sendNotificacion();
        }

        @Override
        protected Integer doInBackground(Aspirante... aspirante) {
            SystemClock.sleep(4000);
            int result;
            try {
                result = jsonParser.uploadAspirante(aspirante[0]);
            } catch (Exception e) {
                e.printStackTrace();
                result = 0;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Aspirante guardado!");
                    getFragmentManager().popBackStack();
                    if (mActivity != null) {
                        mActivity.actualizarPostulacion();
                    }
                    mManager.cancel(1);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.postularse_enviado));
                    mToolbar.getMenu().clear();
                    break;
                case 0:
                    Log.i(TAG, "¡Error al cargar el aspirante, datos malos!");
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_excepcion));
                    //new UploadAspirante().execute(mAspirante);
                    break;
                case -1:
                    Log.i(TAG, "¡Error con el servidor!");
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_enviar));
                    break;
            }
        }
    }

    private void sendNotificacion() {

        try {
            NotificationCompat.Builder mNotificacion = new NotificationCompat.Builder(getActivity())
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(getString(R.string.postularse_enviar))
                    .setTicker(getString(R.string.postularse_enviar))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            mNotificacion.setAutoCancel(true);
            mManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mManager.notify(1, mNotificacion.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}