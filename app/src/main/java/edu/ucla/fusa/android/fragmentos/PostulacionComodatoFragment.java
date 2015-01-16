package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ExpandibleListPostulacionComodatoAdapter;
import edu.ucla.fusa.android.modelo.herramientas.ItemListEstudiantesComodato;

/**
 * Created by juanlabrador on 24/11/14.
 */
public class PostulacionComodatoFragment extends Fragment {

    private View view;
    private ExpandibleListPostulacionComodatoAdapter adapter;
    private ExpandableListView expandableListView;
    private List<String> grupo;
    private HashMap<String, List<ItemListEstudiantesComodato>> contenido;

    public static PostulacionComodatoFragment newInstance() {
        PostulacionComodatoFragment fragment = new PostulacionComodatoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_drawer_postulacion_comodato, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.list_view_estudiantes_comodato);

        prepareListData();

        adapter = new ExpandibleListPostulacionComodatoAdapter(getActivity(), grupo, contenido);

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
        getActivity().getActionBar().setIcon(R.drawable.ic_postular_comodato_blanco);
        getActivity().getActionBar().setTitle(R.string.comodato_action_bar);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setIcon(R.drawable.ic_postular_comodato_blanco);
        getActivity().getActionBar().setTitle(R.string.comodato_action_bar);
    }

    private void prepareListData() {
        grupo = new ArrayList<String>();
        contenido = new HashMap<String, List<ItemListEstudiantesComodato>>();

        // Adding child data
        grupo.add("Grupo - Edad 7");
        grupo.add("Grupo - Edad 11");



        // Adding child data
        List<ItemListEstudiantesComodato> nive7 = new ArrayList<ItemListEstudiantesComodato>();
        nive7.add(new ItemListEstudiantesComodato(R.drawable.no_avatar, "Juan Labrador", 7, "Guitarra", 3));
        nive7.add(new ItemListEstudiantesComodato(R.drawable.no_avatar, "Jaime Daza", 7, "Guitarra", 2));
        nive7.add(new ItemListEstudiantesComodato(R.drawable.no_avatar, "Ana Ure", 7, "Guitarra", 4));
        nive7.add(new ItemListEstudiantesComodato(R.drawable.no_avatar, "Jorge Aponte", 7, "Guitarra", 1));


        // Adding child data
        List<ItemListEstudiantesComodato> nive11 = new ArrayList<ItemListEstudiantesComodato>();
        nive11.add(new ItemListEstudiantesComodato(R.drawable.no_avatar, "Miguel Nesterovsky", 11, "Guitarra", 4));
        nive11.add(new ItemListEstudiantesComodato(R.drawable.no_avatar, "Xioang Sanguino", 11, "Guitarra", 3));
        nive11.add(new ItemListEstudiantesComodato(R.drawable.no_avatar, "Angelica Morales", 11, "Guitarra", 3));
        nive11.add(new ItemListEstudiantesComodato(R.drawable.no_avatar, "Alexis Colina", 11, "Guitarra", 2));


        contenido.put(grupo.get(0), nive7);
        contenido.put(grupo.get(1), nive11);

    }
}
