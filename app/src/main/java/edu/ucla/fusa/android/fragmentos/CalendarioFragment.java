package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import me.drakeet.materialdialog.MaterialDialog;

import com.juanlabrador.grouplayout.GroupContainer;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.ucla.fusa.android.DB.EventoTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.evento.Evento;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class CalendarioFragment extends Fragment implements CalendarPickerView.OnDateSelectedListener, CalendarPickerView.OnScrollListener {

    private static String TAG = "CalendarioFragment";
    private CalendarPickerView mCalendario;
    private View mView;
    private Calendar mProximoAño;
    private Calendar mDiaActual;
    private ArrayList<Evento> mEventos;
    private ArrayList<Date> mFechas;
    private ArrayList<Integer> mIds;
    private SimpleDateFormat mDateFormat;
    private EventoTable mEventoTable;
    private Toolbar mToolbar;
    private CircularProgressBar mLoading;
    private ArrayAdapter<String> mAdapter;
    private ListView mList;
    private LinearLayout mContenedorLeyenda;
    private int mLastScrollY;
    private int mPreviousFirstVisibleItem;
    private Animation mAnimation;
    private int mScrollThreshold;
    private AbsListView mScroll;
    
    private MaterialDialog mCustomView;
    private GroupContainer mGrupoEvento;
    private GroupContainer mGrupoLugar;
    private GroupContainer mGrupoDescripcion;
    private Evento mEvento;
    private SimpleDateFormat mTimeFormat;
    private TextView mTituloEvento;

    public static CalendarioFragment newInstance() {
        CalendarioFragment fragment = new CalendarioFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        mEventoTable = new EventoTable(getActivity());
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.calendario_titulo_barra);
        mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        mTimeFormat = new SimpleDateFormat("hh:mm aa");
        mFechas = new ArrayList<>();
        mIds = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        mView = paramLayoutInflater.inflate(R.layout.fragment_drawer_calendar, paramViewGroup, false);
        mCalendario = (CalendarPickerView) mView.findViewById(R.id.calendario);
        mCalendario.setOnDateSelectedListener(this);
        mCalendario.setOnScrollListener(this);
        mProximoAño = Calendar.getInstance();
        mProximoAño.add(Calendar.YEAR, 1);
        mDiaActual = Calendar.getInstance();
        mLoading = (CircularProgressBar) mView.findViewById(R.id.pb_cargando_calendario);

        mContenedorLeyenda = (LinearLayout) mView.findViewById(R.id.contenedor_leyenda);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new LoadingEventos().execute();
    }

    @Override
    public void onDateSelected(Date date) {
        String mFechaCalendario = mDateFormat.format(date);
        String mFechaEvento;
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        final MaterialDialog mDialog = new MaterialDialog(getActivity());

        for (int i = 0; i < mFechas.size(); i++) {
            mFechaEvento = mDateFormat.format(mFechas.get(i));
            if (mFechaEvento.equals(mFechaCalendario)) {
                mAdapter.add(mEventos.get(i).getNombre());
                mIds.add(mEventos.get(i).getId());
            }
        }
        if (mAdapter.getCount() > 1) {
            Log.i(TAG, "¡Hay más de un evento!");
            mList = new ListView(getActivity());
            mList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (8 * scale + 0.5f);
            mList.setPadding(0, dpAsPixels, 0, dpAsPixels);
            mList.setDividerHeight(0);
            mList.setAdapter(mAdapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    armarCustomLayout(mIds.get(i));
                    
                    Log.i(TAG, "¡Selecciono el evento " + i + " de la lista");
                }
            });
            
            mDialog.setTitle("Seleccione un evento")
                   .setContentView(mList)
                    .setNegativeButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDialog.dismiss();
                        }
                    }).show();

        } else if (mAdapter.getCount() == 1){
            Log.i(TAG, "¡Hay solo un evento!");
            
            armarCustomLayout(mIds.get(0));
         
        } else {
            Log.i(TAG, "¡No hay eventos en ese dia!");
        }
    }
    
    private void armarCustomLayout(int id) {
        
        mCustomView = new MaterialDialog(getActivity());
        mEvento = mEventoTable.searchEvento(id);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_evento, null);

        mTituloEvento = (TextView) view.findViewById(R.id.titulo_evento);
        mTituloEvento.setText(mEvento.getNombre().toUpperCase());
        mGrupoEvento = (GroupContainer) view.findViewById(R.id.grupo_datos_evento);
        mGrupoEvento.addTextLayout(R.string.evento_fecha, mDateFormat.format(mEvento.getFecha()));
        mGrupoEvento.addTextLayout(R.string.evento_hora, mTimeFormat.format(mEvento.getHora()));

        mGrupoDescripcion = (GroupContainer) view.findViewById(R.id.grupo_descripcion_evento);
        mGrupoDescripcion.addSimpleMultiTextLayout(mEvento.getDescripcion());

        mGrupoLugar = (GroupContainer) view.findViewById(R.id.grupo_lugar_evento);
        mGrupoLugar.addSimpleMultiTextLayout(mEvento.getLugar().getDescripcion());
        mGrupoLugar.addSimpleMultiTextLayout(mEvento.getLugar().getDireccion());

        mCustomView.setView(view);
        mCustomView.setCanceledOnTouchOutside(true);
        mCustomView.setBackgroundResource(R.color.gris_fondo);
        mCustomView.show();
        
    }

    @Override
    public void onDateUnselected(Date date) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(totalItemCount == 0) return;
        if (isSameRow(firstVisibleItem)) {
            int newScrollY = getTopItemScrollY();
            boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;
            if (isSignificantDelta) {
                if (mLastScrollY > newScrollY) {
                    Log.i(TAG, "Scroll Up");
                    startAnimationUp();
                } else {
                    startAnimationDown();
                    Log.i(TAG, "Scroll Down");
                }
            }
            mLastScrollY = newScrollY;
        } else {
            if (firstVisibleItem > mPreviousFirstVisibleItem) {
                Log.i(TAG, "Scroll Up");
                startAnimationUp();
            } else {
                Log.i(TAG, "Scroll Down");
                startAnimationDown();
            }

            mLastScrollY = getTopItemScrollY();
            mPreviousFirstVisibleItem = firstVisibleItem;
        }
    }

    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == mPreviousFirstVisibleItem;
    }

    private int getTopItemScrollY() {
        if (mScroll == null || mScroll.getChildAt(0) == null) return 0;
        View topChild = mScroll.getChildAt(0);
        return topChild.getTop();
    }

    private void startAnimationDown() {
        if (!mContenedorLeyenda.isShown()) {
            mContenedorLeyenda = (LinearLayout) getActivity().findViewById(R.id.contenedor_leyenda);
            mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_down);
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mContenedorLeyenda.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mContenedorLeyenda.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mContenedorLeyenda.startAnimation(mAnimation);
        }
    }

    private void startAnimationUp() {
        if (mContenedorLeyenda.isShown()) {
            mContenedorLeyenda = (LinearLayout) getActivity().findViewById(R.id.contenedor_leyenda);
            mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_up);
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mContenedorLeyenda.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mContenedorLeyenda.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mContenedorLeyenda.startAnimation(mAnimation);
        }
    }

    private class LoadingEventos extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            mEventos = mEventoTable.searchEventos();
            if (mEventos != null) {
                Log.i(TAG, "¡Hay eventos!");
                for(Evento mEvento : mEventos) {
                    mFechas.add(mEvento.getFecha());
                }
                return 100;
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MONTH, 9);
                    Log.i(TAG, "fecha: " + new SimpleDateFormat("dd-MM-yyyy").format(c.getTime()));
                    ArrayList<Date> prueba = new ArrayList<>();
                    prueba.add(c.getTime());
                    mCalendario.init(mDiaActual.getTime(), mProximoAño.getTime())
                            .withSelectedDate(mDiaActual.getTime())
                            .inMode(CalendarPickerView.SelectionMode.SINGLE)
                            .withHighlightedDates(mFechas)
                    .withHighlightedOthersDates(prueba);
                    break;
                case -1:
                    Log.i(TAG, "No hay eventos!");
                    mCalendario.init(mDiaActual.getTime(), mProximoAño.getTime())
                            .withSelectedDate(mDiaActual.getTime());
                    break;
            }
            mLoading.setVisibility(View.GONE);
            mCalendario.setVisibility(View.VISIBLE);
            mContenedorLeyenda.setVisibility(View.VISIBLE);
        }
    }
}