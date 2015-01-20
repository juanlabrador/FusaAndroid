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
import android.widget.Toast;
import com.dd.CircularProgressButton;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.juanlabrador.GroupLayout;

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
import java.util.List;

public class EstudianteAspiranteFragment extends Fragment implements View.OnClickListener, DatePickerDialogFragment.DatePickerDialogHandler, TextWatcher {

    private static String TAG = "IncialPostulacionesFragment";
    private GroupLayout mDatosBasicos;
    private GroupLayout mDatosMusicales;
    private GroupLayout mDatosContacto;
    private Toolbar mToolbar;
    private ScrollView mScroll;
    private Calendar calendar;
    private int day;
    private CircularProgressButton mBoton;
    private CircularProgressBar mProgressBar;
    private int month;
    private ArrayList<String> mCustomMenu;

    private View mView;
    private int year;
    private JSONParser jsonParser = new JSONParser();
    private List<Catedra> catedras = new ArrayList<Catedra>();
    private Catedra c = new Catedra();
    private Aspirante aspirante = new Aspirante();
    private int age;

    public static EstudianteAspiranteFragment newInstance() {
        EstudianteAspiranteFragment fragment = new EstudianteAspiranteFragment();
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
        mDatosBasicos.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mDatosBasicos.getEditTextLayoutAt(1).getEditText().addTextChangedListener(this);
        mDatosBasicos.getEditTextLayoutAt(2).getEditText().addTextChangedListener(this);
        
        mDatosMusicales = (GroupLayout) mView.findViewById(R.id.grupo_datos_musicales);
        mDatosMusicales.addSwitchLayout(R.string.postularse_instrumento);
        mDatosMusicales.getSwitchLayoutAt(0).setSwitchColor(getResources().getColor(R.color.azul));

        
        mDatosContacto = (GroupLayout) mView.findViewById(R.id.grupo_datos_contacto);
        mDatosContacto.addEditTextLayout(R.string.postularse_telefono);
        mDatosContacto.addValidatorLayout(R.string.postularse_correo);
        mDatosContacto.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mDatosContacto.getValidatorLayoutAt(1).getEditText().addTextChangedListener(this);
        mDatosContacto.getEditTextLayoutAt(0).setInputType(InputType.TYPE_CLASS_NUMBER);
        mDatosContacto.getValidatorLayoutAt(1).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        mBoton = (CircularProgressButton) mView.findViewById(R.id.boton_postulaciones);
        mBoton.setOnClickListener(this);
        mBoton.setVisibility(View.GONE);
        
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
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.postularse_titulo_barra);
        mToolbar.setNavigationIcon(R.drawable.ic_regresar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        mToolbar.setVisibility(View.GONE);
    }

    public void onClick(View view) {
        if (view == mBoton) {
            if (mBoton.getProgress() != 100) {
                if (ValidadorEmails.validarEmail(mDatosContacto.getValidatorLayoutAt(1).getContent()) != true) {
                    Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
                } else if (mDatosBasicos.getEditTextLayoutAt(0).getContent().equals("") && mDatosBasicos.getEditTextLayoutAt(1).getContent().equals("") && 
                        mDatosBasicos.getEditTextLayoutAt(2).getContent().equals("") && mDatosBasicos.getPopupLayoutAt(3).getContent().equals("") && mDatosBasicos.getOneButtonLayoutAt(4).getContent().equals("") &&
                        mDatosMusicales.getPopupLayoutAt(1).getContent().equals("") && mDatosContacto.getEditTextLayoutAt(0).getContent().equals("") && mDatosContacto.getValidatorLayoutAt(1).getContent().equals("")) {
                    Toast.makeText(getActivity(), R.string.mensaje_complete_campos_aspirante, Toast.LENGTH_SHORT).show();
                } else {
                    setAspirante();
                    new UploadAspirante().execute(aspirante);
                }
            } else {
                Toast.makeText(getActivity(), R.string.mensaje_error_aspirante, Toast.LENGTH_SHORT).show();
            }
        } else if (view == mDatosBasicos.getOneButtonLayoutAt(4).getButton()) {
            new DatePickerBuilder()
                    .setFragmentManager(getChildFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setTargetFragment(EstudianteAspiranteFragment.this)
                    .show();
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);


    }

    public void onResume() {
        super.onResume();
    }

    private void setAspirante() {
        aspirante.setCatedra(c);
        aspirante.setNombre(mDatosBasicos.getEditTextLayoutAt(1).getContent());
        aspirante.setApellido(mDatosBasicos.getEditTextLayoutAt(2).getContent());
        aspirante.setCedula(mDatosBasicos.getEditTextLayoutAt(0).getContent());
        aspirante.setCorreo(mDatosContacto.getValidatorLayoutAt(1).getContent());
        aspirante.setTelefonoMovil(mDatosContacto.getEditTextLayoutAt(0).getContent());
        aspirante.setEdad(age);
        aspirante.setSexo(mDatosBasicos.getPopupLayoutAt(3).getContent());
        if (mDatosMusicales.getSwitchLayoutAt(0).isChecked()) {
            aspirante.setInstrumentoPropio("Si");
        } else {
            aspirante.setInstrumentoPropio("No");
        }
        aspirante.setEstatus("activo");
        try {
            aspirante.setFechanac(new SimpleDateFormat("dd-MM-yyyy").parse(mDatosBasicos.getOneButtonLayoutAt(4).getContent()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogDateSet(int reference, int year, int month, int day) {
        if (month < 9)
            mDatosBasicos.getOneButtonLayoutAt(4).setContent(day + "-0" + (month + 1) + "-" + year);
        else
            mDatosBasicos.getOneButtonLayoutAt(4).setContent(day + "-" + (month + 1) + "-" + year);

        Calendar calendar = Calendar.getInstance();
        age = calendar.get(Calendar.YEAR) - year;
    }

    private void showMenuCatedra() {
        mDatosMusicales.getPopupLayoutAt(1).getPopupMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                c = catedras.get(menuItem.getOrder());
                Log.i(TAG, "Selecciono la catedra " + c.getDescripcion() + " de la posición " + menuItem.getOrder());
                //mDatosMusicales.getPopupLayoutAt(1).setContent((String) menuItem.getTitle());
                return true;
            }
        });
        //mDatosMusicales.getPopupLayoutAt(1).getPopupMenu().show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        mBoton.setProgress(0);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (ValidadorEmails.validarEmail(mDatosContacto.getValidatorLayoutAt(1).getContent()) != true) {
            mDatosContacto.getValidatorLayoutAt(1).dataError();
        } else if (ValidadorEmails.validarEmail(mDatosContacto.getValidatorLayoutAt(1).getContent()) == true)  {
            mDatosContacto.getValidatorLayoutAt(1).dataCheck();
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

            if (result == 0) {
                Toast.makeText(getActivity(), R.string.mensaje_error_busqueda, Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
                Log.i(TAG, "¡Error al buscar!");
            } else if (result == 100) {
                mDatosMusicales.addPopupLayout(R.string.postularse_catedra, mCustomMenu);
                mProgressBar.setVisibility(View.GONE);
                mScroll.setVisibility(View.VISIBLE);
                mBoton.setVisibility(View.VISIBLE);
                mToolbar.setVisibility(View.VISIBLE);
                Log.i(TAG, "¡Cargando sin problemas!");
            }
        }
    }

    private class UploadAspirante extends AsyncTask<Aspirante, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBoton.setProgress(0);
            mBoton.setIndeterminateProgressMode(true);
            mBoton.setProgress(50);
        }

        @Override
        protected Integer doInBackground(Aspirante... aspirante) {
            int result = -1;
            try {
                result = jsonParser.uploadAspirante(aspirante[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mBoton.setProgress(result);
            if (result == 100) {
                //getFragmentManager().popBackStackImmediate();
            } else {
                mBoton.setErrorText("Error, ¡reintentar!");
            }
        }
    }
}