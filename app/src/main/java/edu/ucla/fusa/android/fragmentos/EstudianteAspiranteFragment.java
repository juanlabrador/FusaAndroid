package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.dd.CircularProgressButton;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.Catedra;
import edu.ucla.fusa.android.modelo.fundacion.Aspirante;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EstudianteAspiranteFragment extends Fragment implements View.OnClickListener, DatePickerDialogFragment.DatePickerDialogHandler, PopupMenu.OnMenuItemClickListener, TextWatcher {

    private static String TAG = "IncialPostulacionesFragment";
    private FloatingHintEditText apellido;
    private Bundle arguments;
    private LinearLayout barraSuperior;
    private Calendar calendar;
    int contador = 0;
    private int day;
    private FloatingHintEditText email;
    private CircularProgressButton enviar;
    private FloatingHintEditText fechaNacimiento;
    private Handler handler = new Handler();
    private int month;
    private FloatingHintEditText nombre;
    private FloatingHintEditText cedula;
    private FloatingHintEditText sexo;
    private FloatingHintEditText catedra;
    private FloatingHintEditText telefono;
    private ArrayAdapter<String> arrayAdapter;
    private CheckBox poseeInstrumento;
    private View view;
    private PopupMenu menuSexo;
    private PopupMenu menuCatedra;
    private int year;
    private JSONParser jsonParser = new JSONParser();
    private ImageView addSexo;
    private ImageView addCatedra;
    private ImageView addFechaNacimiento;
    private List<Catedra> catedras = new ArrayList<Catedra>();
    private Catedra c = new Catedra();
    private Aspirante aspirante = new Aspirante();
    private int age;

    public static EstudianteAspiranteFragment newInstance() {
        EstudianteAspiranteFragment fragment = new EstudianteAspiranteFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_postulaciones, paramViewGroup, false);
        barraSuperior = (LinearLayout) view.findViewById(R.id.view_barra_superior_postulaciones);
        enviar = ((CircularProgressButton) view.findViewById(R.id.btn_enviar_solicitud_aspirante));
        enviar.setOnClickListener(this);
        nombre = ((FloatingHintEditText) view.findViewById(R.id.et_nombre_aspirante));
        apellido = ((FloatingHintEditText) view.findViewById(R.id.et_apellido_aspirante));
        fechaNacimiento = ((FloatingHintEditText) view.findViewById(R.id.et_fecha_nacimiento_aspirante));
        email = ((FloatingHintEditText) view.findViewById(R.id.et_email_aspirante));
        poseeInstrumento = ((CheckBox) view.findViewById(R.id.cb_instrumento_propio_aspirante));
        cedula = (FloatingHintEditText) view.findViewById(R.id.et_cedula_aspirante);
        sexo = (FloatingHintEditText) view.findViewById(R.id.et_sexo_aspirante);
        catedra = (FloatingHintEditText) view.findViewById(R.id.et_catedra_aspirante);
        telefono = (FloatingHintEditText) view.findViewById(R.id.et_telefono_aspirante);

        addSexo = (ImageView) view.findViewById(R.id.add_sexo);
        addCatedra = (ImageView) view.findViewById(R.id.add_catedra);
        addFechaNacimiento = (ImageView) view.findViewById(R.id.add_fecha_nacimiento);

        addSexo.setOnClickListener(this);
        addCatedra.setOnClickListener(this);
        addFechaNacimiento.setOnClickListener(this);
        nombre.addTextChangedListener(this);
        apellido.addTextChangedListener(this);
        cedula.addTextChangedListener(this);
        telefono.addTextChangedListener(this);
        email.addTextChangedListener(this);


        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.titulo_action_bar_postularse);
            getActivity().getActionBar().setIcon(R.drawable.ic_estudiantes_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }
        catedra.setEnabled(false);
        addCatedra.setEnabled(false);
        new LoadingCatedras().execute();
        return view;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_enviar_solicitud_aspirante:
                if (enviar.getProgress() != 100) {
                    if (ValidadorEmails.validarEmail(this.email.getText().toString()) != true) {
                        Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
                    } else if (nombre.getText().toString().equals("") && apellido.getText().toString().equals("") && cedula.getText().toString().equals("") &&
                            sexo.getText().toString().equals("") && fechaNacimiento.getText().toString().equals("") &&
                            catedra.getText().toString().equals("") && telefono.getText().toString().equals("") && email.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), R.string.mensaje_complete_campos_aspirante, Toast.LENGTH_SHORT).show();
                    } else {
                        setAspirante();
                        new UploadAspirante().execute(aspirante);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.mensaje_error_enviar_postulacion, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_fecha_nacimiento:
                new DatePickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setTargetFragment(EstudianteAspiranteFragment.this)
                        .show();
                //new DatePickerDialog(getActivity(), datePickerListener, year, month, day).show();
                break;
            case R.id.add_sexo:
                Log.i(TAG, "Click en sexo");
                showMenuSexo();
                break;
            case R.id.add_catedra:
                Log.i(TAG, "Click en catedra");
                showMenuCatedra();
                break;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);


    }

    public void onResume() {
        super.onResume();
        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.titulo_action_bar_postularse);
            getActivity().getActionBar().setIcon(R.drawable.ic_estudiantes_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }
    }

    private void setAspirante() {
        aspirante.setCatedra(c);
        aspirante.setNombre(nombre.getText().toString());
        aspirante.setApellido(apellido.getText().toString());
        aspirante.setCedula(cedula.getText().toString());
        aspirante.setCorreo(email.getText().toString());
        aspirante.setTelefonoMovil(telefono.getText().toString());
        aspirante.setEdad(age);
        aspirante.setSexo(sexo.getText().toString());
        if (poseeInstrumento.isChecked()) {
            aspirante.setInstrumentoPropio("Si");
        } else {
            aspirante.setInstrumentoPropio("No");
        }
        aspirante.setEstatus("activo");
        try {
            aspirante.setFechanac(new SimpleDateFormat("dd-MM-yyyy").parse(fechaNacimiento.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /*private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            fechaNacimiento.setText(day + "/" + (month + 1) + "/" + year);

        }
    };*/

    @Override
    public void onDialogDateSet(int reference, int year, int month, int day) {
        if (month < 9)
            fechaNacimiento.setText(day + "-0" + (month + 1) + "-" + year);
        else
            fechaNacimiento.setText(day + "-" + (month + 1) + "-" + year);

        Calendar calendar = Calendar.getInstance();
        age = calendar.get(Calendar.YEAR) - year;
    }

    private void showMenuSexo() {
        menuSexo = new PopupMenu(getActivity(), addSexo);
        menuSexo.getMenuInflater().inflate(R.menu.sexo, menuSexo.getMenu());
        menuSexo.setOnMenuItemClickListener(this);
        menuSexo.show();
    }

    private void showMenuCatedra() {
        menuCatedra.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                c = catedras.get(menuItem.getOrder());
                Log.i(TAG, "Selecciono la catedra " + c.getDescripcion() + " de la posición " + menuItem.getOrder());
                catedra.setText(menuItem.getTitle());
                return true;
            }
        });
        menuCatedra.show();
    }

        @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.sexo_femenino:
                sexo.setText(R.string.sexo_femenino);
                break;
            case R.id.sexo_masculino:
                sexo.setText(R.string.sexo_masculino);
                break;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        enviar.setProgress(0);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private class LoadingCatedras extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            menuCatedra = new PopupMenu(getActivity(), addCatedra);
            menuCatedra.getMenu().clear();
            catedras = jsonParser.serviceLoadingCatedras();
            if (catedras == null) {
                return 0;
            } else if (catedras.size() != 0) {
                for (int i = 0; i < catedras.size(); i++) {
                    menuCatedra.getMenu().add(0, 0, i, catedras.get(i).getDescripcion());
                }
                //arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, menu);
                //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return 100;
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == 0) {
                Toast.makeText(getActivity(), R.string.mensaje_error_cargar_catedras, Toast.LENGTH_SHORT).show();
            } else if (result == 100) {
                catedra.setEnabled(true);
                addCatedra.setEnabled(true);
            }
        }
    }

    private class UploadAspirante extends AsyncTask<Aspirante, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            enviar.setProgress(0);
            enviar.setIndeterminateProgressMode(true);
            enviar.setProgress(50);
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
            enviar.setProgress(result);
            if (result == 100) {
                //getFragmentManager().popBackStackImmediate();
            } else {
                enviar.setErrorText("Error, ¡reintentar!");
            }
        }
    }
}