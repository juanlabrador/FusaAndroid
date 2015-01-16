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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.Catedra;
import edu.ucla.fusa.android.modelo.fundacion.InstructorAspirante;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

public class InstructorAspiranteFragment extends Fragment implements View.OnClickListener, DatePickerDialogFragment.DatePickerDialogHandler, PopupMenu.OnMenuItemClickListener, TextWatcher {

    private static String TAG = "InicialParticipaFragment";
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
    private FloatingHintEditText cedula;
    private FloatingHintEditText sexo;
    private FloatingHintEditText nombre;
    private FloatingHintEditText telefono;
    private View view;
    private FloatingHintEditText catedra;
    private int year;
    private int age;
    private Catedra c = new Catedra();
    private ImageView addSexo;
    private ImageView addCatedra;
    private ImageView addFechaNacimiento;
    private PopupMenu menuSexo;
    private PopupMenu menuCatedra;
    private List<Catedra> catedras = new ArrayList<Catedra>();
    private JSONParser jsonParser = new JSONParser();
    private InstructorAspirante aspirante = new InstructorAspirante();

    public static InstructorAspiranteFragment newInstance() {
        InstructorAspiranteFragment fragment = new InstructorAspiranteFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_participa, paramViewGroup, false);
        barraSuperior = (LinearLayout) view.findViewById(R.id.view_barra_superior_participa);
        enviar = ((CircularProgressButton) view.findViewById(R.id.btn_enviar_solicitud_participa));
        enviar.setOnClickListener(this);
        nombre = ((FloatingHintEditText) view.findViewById(R.id.et_nombre_participa));
        apellido = ((FloatingHintEditText) view.findViewById(R.id.et_apellido_participa));
        fechaNacimiento = ((FloatingHintEditText) view.findViewById(R.id.et_fecha_nacimiento_participa));
        email = ((FloatingHintEditText) view.findViewById(R.id.et_email_participa));
        telefono = (FloatingHintEditText) view.findViewById(R.id.et_telefono_participa);
        catedra = (FloatingHintEditText) view.findViewById(R.id.et_catedra_participa);
        sexo = (FloatingHintEditText) view.findViewById(R.id.et_sexo_participa);
        cedula = (FloatingHintEditText) view.findViewById(R.id.et_cedula_participa);

        addSexo = (ImageView) view.findViewById(R.id.add_sexo_participa);
        addCatedra = (ImageView) view.findViewById(R.id.add_catedra_participa);
        addFechaNacimiento = (ImageView) view.findViewById(R.id.add_fecha_nacimiento_participa);

        addSexo.setOnClickListener(this);
        addCatedra.setOnClickListener(this);
        addFechaNacimiento.setOnClickListener(this);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.participa_titulo_superior);
            getActivity().getActionBar().setIcon(R.drawable.ic_profesores_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }

        new LoadingCatedras().execute();
        return view;
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

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_enviar_solicitud_participa:
                if (enviar.getProgress() != 100) {
                    if (ValidadorEmails.validarEmail(email.getText().toString()) != true) {
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
            case R.id.add_fecha_nacimiento_participa:
                new DatePickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setTargetFragment(InstructorAspiranteFragment.this)
                        .show();
                //new DatePickerDialog(getActivity(), datePickerListener, year, month, day).show();
                break;
            case R.id.add_sexo_participa:
                Log.i(TAG, "Click en sexo");
                showMenuSexo();
                break;
            case R.id.add_catedra_participa:
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
            getActivity().getActionBar().setTitle(R.string.participa_titulo_superior);
            getActivity().getActionBar().setIcon(R.drawable.ic_profesores_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
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

    private void setAspirante() {
        aspirante.setCatedra(c);
        aspirante.setNombre(nombre.getText().toString());
        aspirante.setApellido(apellido.getText().toString());
        aspirante.setCedula(cedula.getText().toString());
        aspirante.setCorreo(email.getText().toString());
        aspirante.setTelefonoMovil(telefono.getText().toString());
        aspirante.setEdad(age);
        aspirante.setSexo(sexo.getText().toString());
        aspirante.setEstatus("activo");
        try {
            aspirante.setFechanac(new SimpleDateFormat("dd-MM-yyyy").parse(fechaNacimiento.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
            }
        }
    }

    private class UploadAspirante extends AsyncTask<InstructorAspirante, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            enviar.setProgress(0);
            enviar.setIndeterminateProgressMode(true);
            enviar.setProgress(50);
        }

        @Override
        protected Integer doInBackground(InstructorAspirante... aspirante) {
            int result = -1;
            try {
                result = jsonParser.uploadInstructorAspirante(aspirante[0]);
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