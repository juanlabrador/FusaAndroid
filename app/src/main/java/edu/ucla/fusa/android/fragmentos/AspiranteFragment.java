package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.juanlabrador.GroupLayout;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.Catedra;
import edu.ucla.fusa.android.modelo.fundacion.Aspirante;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.validadores.ValidadorEmails;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AspiranteFragment extends Fragment implements View.OnClickListener, DatePickerDialogFragment.DatePickerDialogHandler, TextWatcher, Toolbar.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {

    private static String TAG = "IncialPostulacionesFragment";
    private GroupLayout mDatosBasicos;
    private GroupLayout mDatosMusicales;
    private GroupLayout mDatosContacto;
    private Toolbar mToolbar;
    private ScrollView mScroll;
    private Calendar calendar;
    private int day;
    private CircularProgressBar mProgressBar;
    private int month;
    private ArrayList<String> mCustomMenu;

    private View mView;
    private int year;
    private JSONParser jsonParser = new JSONParser();
    private List<Catedra> catedras = new ArrayList<>();
    private Catedra c = new Catedra();
    private Aspirante mAspirante = new Aspirante();
    private int age;
    private DrawerArrowDrawable mDrawerArrow;

    public static AspiranteFragment newInstance() {
        AspiranteFragment fragment = new AspiranteFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        mView = inflater.inflate(R.layout.fragment_inicial_postulaciones, container, false);

        
        mScroll = (ScrollView) mView.findViewById(R.id.scroll_postulaciones);
        mScroll.setVisibility(View.GONE);
        mDatosBasicos = (GroupLayout) mView.findViewById(R.id.grupo_datos_basicos);
        mDatosBasicos.addEditTextLayout(R.string.postularse_cedula);
        mDatosBasicos.addEditTextLayout(R.string.postularse_nombre);
        mDatosBasicos.addEditTextLayout(R.string.postularse_apellido);
        mDatosBasicos.addPopupLayout(R.string.postularse_sexo, R.menu.sexo);
        mDatosBasicos.addOneButtonLayout(R.string.postularse_fecha_nacimiento, GroupLayout.ColorIcon.GRAY);
        mDatosBasicos.getOneButtonLayoutAt(4).getButton().setOnClickListener(this);
        mDatosBasicos.getEditTextLayoutAt(0).setInputType(InputType.TYPE_CLASS_NUMBER);
        mDatosBasicos.getEditTextLayoutAt(0).setMaxLength(8);
        mDatosBasicos.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mDatosBasicos.getEditTextLayoutAt(1).getEditText().addTextChangedListener(this);
        mDatosBasicos.getEditTextLayoutAt(2).getEditText().addTextChangedListener(this);
        
        mDatosMusicales = (GroupLayout) mView.findViewById(R.id.grupo_datos_musicales);
        mDatosMusicales.addSwitchLayout(R.string.postularse_instrumento);
        mDatosMusicales.getSwitchLayoutAt(0).setSwitchColor(getResources().getColor(R.color.azul));

        
        mDatosContacto = (GroupLayout) mView.findViewById(R.id.grupo_datos_contacto);
        mDatosContacto.addEditTextLayout(R.string.postularse_telefono);
        mDatosContacto.addValidatorLayout(R.string.postularse_correo);
        mDatosContacto.getEditTextLayoutAt(0).setMaxLength(11);
        mDatosContacto.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mDatosContacto.getValidatorLayoutAt(1).getEditText().addTextChangedListener(this);
        mDatosContacto.getEditTextLayoutAt(0).setInputType(InputType.TYPE_CLASS_NUMBER);
        mDatosContacto.getValidatorLayoutAt(1).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        
        mProgressBar = (CircularProgressBar) mView.findViewById(R.id.cargando_postulaciones);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        new LoadingCatedras().execute();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDrawerArrow = new DrawerArrowDrawable(getActivity()) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerArrow.setProgress(1f);
        
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
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
        mToolbar.setVisibility(View.GONE);
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {        
        switch (menuItem.getItemId()) {
            case R.id.action_enviar:
                if (!mDatosContacto.getValidatorLayoutAt(1).isCheck()) {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                            .text(R.string.mensaje_correo_invalido));
                } else if (mDatosBasicos.getEditTextLayoutAt(0).getContent().equals("") && mDatosBasicos.getEditTextLayoutAt(1).getContent().equals("") &&
                        mDatosBasicos.getEditTextLayoutAt(2).getContent().equals("") && mDatosBasicos.getPopupLayoutAt(3).getContent().equals("") && mDatosBasicos.getOneButtonLayoutAt(4).getContent().equals("") &&
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

    public void onClick(View view) {
        if (view == mDatosBasicos.getOneButtonLayoutAt(4).getButton()) {
            new DatePickerBuilder()
                    .setFragmentManager(getChildFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setTargetFragment(AspiranteFragment.this)
                    .show();
        }
    }

    private void setAspirante() {
        Log.i(TAG, "Posición: " + mDatosMusicales.getPopupLayoutAt(1).getItemPosition());
        mAspirante.setCatedra(catedras.get(mDatosMusicales.getPopupLayoutAt(1).getItemPosition()));
        mAspirante.setNombre(mDatosBasicos.getEditTextLayoutAt(1).getContent());
        mAspirante.setApellido(mDatosBasicos.getEditTextLayoutAt(2).getContent());
        mAspirante.setCedula(mDatosBasicos.getEditTextLayoutAt(0).getContent());
        mAspirante.setCorreo(mDatosContacto.getValidatorLayoutAt(1).getContent());
        mAspirante.setTelefonoMovil(mDatosContacto.getEditTextLayoutAt(0).getContent());
        mAspirante.setEdad(age);
        mAspirante.setSexo(mDatosBasicos.getPopupLayoutAt(3).getContent());
        if (mDatosMusicales.getSwitchLayoutAt(0).isChecked()) {
            mAspirante.setInstrumentoPropio("Si");
        } else {
            mAspirante.setInstrumentoPropio("No");
        }
        mAspirante.setEstatus("activo");
        try {
            mAspirante.setFechanac(new SimpleDateFormat("dd-MM-yyyy").parse(mDatosBasicos.getOneButtonLayoutAt(4).getContent()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogDateSet(int reference, int year, int month, int day) {
        try {
            Calendar calendar = Calendar.getInstance();
            Date mFechaNacimiento = new SimpleDateFormat("dd-MM-yyyy").parse(day + "-" + (month + 1) + "-" + year);
            if (mFechaNacimiento.before(calendar.getTime())) {  //Validamos que la fecha de nacimiento sea menor a la fecha actual.
                if (month < 9)
                    mDatosBasicos.getOneButtonLayoutAt(4).setContent(day + "-0" + (month + 1) + "-" + year);
                else
                    mDatosBasicos.getOneButtonLayoutAt(4).setContent(day + "-" + (month + 1) + "-" + year);

                age = calendar.get(Calendar.YEAR) - year;
            } else {
                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .type(SnackbarType.MULTI_LINE)
                                .text(R.string.mensaje_error_fecha));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            SnackbarManager.show(
                Snackbar.with(getActivity())
                       .type(SnackbarType.MULTI_LINE)
                       .text(R.string.mensaje_error_excepcion));
        }
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

    private class LoadingCatedras extends AsyncTask<Void, Void, Integer> {

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
            catedras = jsonParser.serviceLoadingCatedras();
            if (catedras == null) {
                return 0;
            } else if (catedras.size() != 0) {
                for (int i = 0; i < catedras.size(); i++) {
                    mCustomMenu.add(catedras.get(i).getDescripcion());
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
                    mProgressBar.setVisibility(View.GONE);
                    mScroll.setVisibility(View.VISIBLE);
                    mToolbar.setVisibility(View.VISIBLE);
                    Log.i(TAG, "¡Cargando sin problemas!");
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
        }

        @Override
        protected Integer doInBackground(Aspirante... aspirante) {
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
                    break;
                case 0:
                    Log.i(TAG, "¡Error al cargar el aspirante, datos malos!");
                   /* mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_excepcion));*/
                    new UploadAspirante().execute(mAspirante);
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
}