package edu.ucla.fusa.android.fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListNoticiasAdapter;
import edu.ucla.fusa.android.modelo.ItemListNoticias;

/**
 * Created by juanlabrador on 23/10/14.
 *
 * Clase que implementa la lista de noticias.
 */
public class ListadoNoticiasFragment extends Fragment implements ListView.OnItemClickListener {

    private View view;
    private ListView list;
    private ArrayList<ItemListNoticias> items;
    private ListNoticiasAdapter adapter;

    public static ListadoNoticiasFragment newInstance() {
        ListadoNoticiasFragment activity = new ListadoNoticiasFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_list_noticias, container, false);
        list = (ListView) view.findViewById(R.id.lvListadoNoticias);

        items = new ArrayList<ItemListNoticias>();

        items.add(new ItemListNoticias(
               "Jóvenes de Barquisimeto y Villa de Leyva comparten atril en un concierto binacional Colombia-Venezuela",
               "22/10/2014",
               R.drawable.noticia1));
        items.add(new ItemListNoticias(
                "Las voces de 1500 niños guariqueños se escucharán en el festival de coros",
                "22/10/2014",
                R.drawable.noticia2));
        items.add(new ItemListNoticias(
                "Gustavo Dudamel dirigirá en Caracas fragmentos sinfónicos de las óperas de Wagner",
                "21/10/2014",
                R.drawable.noticia3));
        items.add(new ItemListNoticias(
                "El #FESTLATMÚSICA: Un espacio para decantar el acervo histórico de la música latinoamericana",
                "17/10/2014",
                R.drawable.noticia4));
        items.add(new ItemListNoticias(
                "Mozart sonará en el piano de un niño de diez años",
                "16/10/2014",
                R.drawable.noticia5));
        items.add(new ItemListNoticias(
                "Banda sinfónica juvenil Simón Bolívar realiza gira por Venezuela y Colombia",
                "16/10/2014",
                R.drawable.noticia6));
        items.add(new ItemListNoticias(
                "La influencia músical rusa se apodera de la sala Simón Bolívar",
                "16/10/2014",
                R.drawable.noticia7));
        items.add(new ItemListNoticias(
                "El festival de violín Aragua 2014 reúne a destacados intérpretes",
                "15/10/2014",
                R.drawable.noticia8));
        items.add(new ItemListNoticias(
                "Edicson Ruiz declarado visiante ilustre de la ciudad de Asunción",
                "15/10/2014",
                R.drawable.noticia9));
        items.add(new ItemListNoticias(
                "Filarmónica de Radio Francia grabará obra de la venezolana Marianela Arocha",
                "14/10/2014",
                R.drawable.noticia10));

        adapter = new ListNoticiasAdapter(getActivity(), items);
        list.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle(R.string.contenido_noticia_action_bar_titulo);
        getActivity().getActionBar().setIcon(R.drawable.ic_noticias);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
