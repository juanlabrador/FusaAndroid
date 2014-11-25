package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ExpandibleListAdapter;
import edu.ucla.fusa.android.modelo.academico.Grupo;
import edu.ucla.fusa.android.modelo.academico.AreaEstudio;
import edu.ucla.fusa.android.modelo.academico.Dia;
import edu.ucla.fusa.android.modelo.academico.Horario;
import edu.ucla.fusa.android.modelo.academico.HorarioPorGrupo;
import edu.ucla.fusa.android.modelo.academico.Instructor;

/**
 * Created by juanlabrador on 24/11/14.
 */
public class DrawerHorarioFragment extends Fragment {

    private View view;
    private ExpandibleListAdapter adapter;
    private ExpandableListView expandableListView;
    private List<String> grupo;
    private HashMap<String, List<HorarioPorGrupo>> contenido;

    public static DrawerHorarioFragment newInstance() {
        DrawerHorarioFragment fragment = new DrawerHorarioFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_drawer_schedule, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.list_view_horario);

        prepareListData();

        adapter = new ExpandibleListAdapter(getActivity(), grupo, contenido);

        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                /*Toast.makeText(
                        getActivity(),
                        grupo.get(groupPosition)
                                + " : "
                                + contenido.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/
                return false;
            }
        });

        // Listview Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getActivity(),
                        groupPosition.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

            }
        });
                return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.ic_mis_clases_blanco);
        getActivity().getActionBar().setTitle(R.string.mis_clases_action_bar);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setIcon(R.drawable.ic_mis_clases_blanco);
        getActivity().getActionBar().setTitle(R.string.mis_clases_action_bar);
    }

    private void prepareListData() {
        grupo = new ArrayList<String>();
        contenido = new HashMap<String, List<HorarioPorGrupo>>();

        // Adding child data
        grupo.add("Lunes");
        grupo.add("Martes");
        grupo.add("Miercoles");
        grupo.add("Jueves");
        grupo.add("Viernes");
        grupo.add("Sábado");




        // Adding child data
        List<HorarioPorGrupo> lunes = new ArrayList<HorarioPorGrupo>();
        lunes.add(new HorarioPorGrupo(1,
                new AreaEstudio(1, "Cerca del baño"),
                new Horario(1, new Dia(1, "Lunes"), Calendar.getInstance(), Calendar.getInstance()),
                new Grupo(1, "Orquesta Larense Juvenil", new Instructor(1, "Francisco Andrade"), 2)));

        List<HorarioPorGrupo> martes = new ArrayList<HorarioPorGrupo>();

        List<HorarioPorGrupo> miercoles = new ArrayList<HorarioPorGrupo>();
        miercoles.add(new HorarioPorGrupo(1,
                new AreaEstudio(1, "Cerca del baño"),
                new Horario(1, new Dia(3, "Miercoles"), Calendar.getInstance(), Calendar.getInstance()),
                new Grupo(1, "Orquesta Larense Juvenil", new Instructor(1, "Francisco Andrade"), 2)));
        miercoles.add(new HorarioPorGrupo(1,
                new AreaEstudio(1, "ET-09"),
                new Horario(1, new Dia(3, "Miercoles"), Calendar.getInstance(), Calendar.getInstance()),
                new Grupo(2, "Guitarra", new Instructor(2, "Axel Fernando"), 1)));

        // Adding child data
        List<HorarioPorGrupo> jueves = new ArrayList<HorarioPorGrupo>();

        List<HorarioPorGrupo> viernes = new ArrayList<HorarioPorGrupo>();
        viernes.add(new HorarioPorGrupo(1,
                new AreaEstudio(1, "ET-09"),
                new Horario(1, new Dia(3, "Viernes"), Calendar.getInstance(), Calendar.getInstance()),
                new Grupo(2, "Guitarra", new Instructor(2, "Axel Fernando"), 1)));

        List<HorarioPorGrupo> sabado = new ArrayList<HorarioPorGrupo>();

        contenido.put(grupo.get(0), lunes);
        contenido.put(grupo.get(1), martes);
        contenido.put(grupo.get(2), miercoles);
        contenido.put(grupo.get(3), jueves);
        contenido.put(grupo.get(4), viernes);
        contenido.put(grupo.get(5), sabado);
    }


}
