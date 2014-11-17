package edu.ucla.fusa.android.fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListNoticiasAdapter;
import edu.ucla.fusa.android.modelo.ItemListNoticia;
import java.util.ArrayList;

public class NoticiasListadoFragment extends ListFragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView>, View.OnClickListener {

    private ListNoticiasAdapter adapter;
    private Bundle arguments;
    private View backToTop;
    private int index = -1;
    private ArrayList<ItemListNoticia> items = new ArrayList();
    private PullToRefreshListView list;
    private View view;

    public static NoticiasListadoFragment newInstance() {
        NoticiasListadoFragment fragment = new NoticiasListadoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        getListView().setOnItemClickListener(this);
        new LoadingNoticiasTaks().execute();
    }

    public void onClick(View paramView) {
        getListView().smoothScrollToPositionFromTop(0, 0);
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setTitle(R.string.contenido_noticia_action_bar_titulo);
        getActivity().getActionBar().setIcon(R.drawable.ic_noticias);
        backToTop = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_back_to_top_list_view, null, false);
        backToTop.setOnClickListener(this);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_drawer_list_noticias, paramViewGroup, false);
        list = ((PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_list));
        list.setOnRefreshListener(this);
        return view;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        ItemListNoticia item = (ItemListNoticia) paramAdapterView.getItemAtPosition(paramInt);
        this.arguments = new Bundle();
        this.arguments.putString("titulo_noticia", item.getTitulo());
        this.arguments.putString("fecha_noticia", item.getFecha());
        this.arguments.putInt("imagen_noticia", item.getImagen());
        this.arguments.putString("descripcion_noticia", item.getDescripcion());
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, NoticiasDetalleFragment.newInstance(this.arguments))
                .addToBackStack(null)
                .commit();
    }

    public void onPause() {
        super.onPause();
        index = getListView().getFirstVisiblePosition();
    }

    public void onRefresh(PullToRefreshBase<ListView> paramPullToRefreshBase) {
        new GetDataTask().execute();
    }

    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(R.string.contenido_noticia_action_bar_titulo);
        getActivity().getActionBar().setIcon(R.drawable.ic_noticias);
        setListAdapter(adapter);
        if (index != -1) {
            getListView().setSelectionFromTop(index, 0);
        }
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putInt("item_position", getListView().getSelectedItemPosition());
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void[] paramArrayOfVoid) {
            SystemClock.sleep(2000L);
            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            super.onPostExecute(paramVoid);
            NoticiasListadoFragment.this.list.onRefreshComplete();
        }
    }

    private class LoadingNoticiasTaks extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void[] paramArrayOfVoid) {
            SystemClock.sleep(2000L);
            items.add(new ItemListNoticia(1,
                    "Jóvenes de Barquisimeto y Villa de Leyva comparten atril en un concierto binacional Colombia-Venezuela",
                    "22/10/2014",
                    R.drawable.noticia1,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent at tellus augue. Fusce vel lorem congue, pulvinar ipsum ut, lobortis est. Morbi malesuada eu massa id vulputate. Nullam laoreet elementum dolor, ut porta enim. Mauris sollicitudin, purus id aliquet sodales, sem velit fringilla augue, vel tempor lacus dolor in purus. Sed eleifend odio in sapien vulputate ultricies. Suspendisse euismod malesuada massa, sit amet aliquet arcu. "));
            items.add(new ItemListNoticia(2,
                    "Las voces de 1500 niños guariqueños se escucharán en el festival de coros",
                    "22/10/2014",
                    R.drawable.noticia2,
                    "Aenean fringilla dictum ex auctor elementum. Morbi efficitur hendrerit bibendum. Praesent lectus neque, malesuada vitae condimentum vel, mollis ultrices nibh. Sed vel lectus eget neque dignissim ornare. Integer at dignissim massa, et lobortis ligula. Sed scelerisque nisi lacus, et finibus est suscipit at. Donec malesuada nulla vitae fermentum posuere. Suspendisse a tincidunt magna. Phasellus id leo in urna suscipit finibus. Pellentesque ut odio diam. Vestibulum leo magna, tempor vitae interdum quis, mollis eget justo. Vestibulum gravida eleifend magna, vel ullamcorper turpis. Integer lobortis enim est. Cras fringilla sagittis velit ut sodales. Phasellus dolor turpis, ultricies sed eleifend in, dapibus ut urna. "));
            items.add(new ItemListNoticia(3,
                    "Gustavo Dudamel dirigirá en Caracas fragmentos sinfónicos de las óperas de Wagner",
                    "21/10/2014",
                    R.drawable.noticia3,
                    "Nulla in urna posuere, posuere odio auctor, venenatis lacus. Donec sagittis semper felis, sit amet posuere ante tincidunt in. Pellentesque laoreet ultrices ipsum, vitae lobortis ante auctor id. Cras lectus risus, tempor ac metus sit amet, posuere scelerisque metus. Quisque vel arcu risus. Integer fringilla eros et dolor dapibus pharetra. Integer maximus odio quis rutrum convallis. Vestibulum vestibulum turpis nec lobortis tempor. Ut et finibus lectus. Suspendisse non egestas velit, a tincidunt nulla. Fusce posuere tellus ante, quis consequat purus tempor eu. "));
            items.add(new ItemListNoticia(4,
                    "El #FESTLATMÚSICA: Un espacio para decantar el acervo histórico de la música latinoamericana",
                    "17/10/2014",
                    R.drawable.noticia4,
                    "Fusce eget varius lacus. Vestibulum ullamcorper velit at nisl maximus blandit. Donec cursus commodo aliquam. Maecenas congue pharetra ligula, in dictum risus faucibus sollicitudin. Curabitur et tincidunt velit. Cras vel bibendum eros, vitae aliquam tellus. Vestibulum luctus sed diam sit amet feugiat. Etiam ut erat id erat bibendum placerat. Donec ut mollis. "));
            items.add(new ItemListNoticia(5,
                    "Mozart sonará en el piano de un niño de diez años",
                    "16/10/2014",
                    R.drawable.noticia5, ""));
            items.add(new ItemListNoticia(6,
                    "Banda sinfónica juvenil Simón Bolívar realiza gira por Venezuela y Colombia",
                    "16/10/2014", R.drawable.noticia6, ""));
            items.add(new ItemListNoticia(7,
                    "La influencia músical rusa se apodera de la sala Simón Bolívar",
                    "16/10/2014", R.drawable.noticia7, ""));
            items.add(new ItemListNoticia(8,
                    "El festival de violín Aragua 2014 reúne a destacados intérpretes",
                    "15/10/2014", R.drawable.noticia8, ""));
            items.add(new ItemListNoticia(9,
                    "Edicson Ruiz declarado visiante ilustre de la ciudad de Asunción",
                    "15/10/2014", R.drawable.noticia9, ""));
            items.add(new ItemListNoticia(10,
                    "Filarmónica de Radio Francia grabará obra de la venezolana Marianela Arocha",
                    "14/10/2014", R.drawable.noticia10, ""));
            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            super.onPostExecute(paramVoid);
            list.onRefreshComplete();
            adapter = new ListNoticiasAdapter(getActivity(), NoticiasListadoFragment.this, items);
            getListView().addFooterView(NoticiasListadoFragment.this.backToTop);
            setListAdapter(NoticiasListadoFragment.this.adapter);
            getListView().removeFooterView(NoticiasListadoFragment.this.backToTop);
            getListView().post(new Runnable() {
                public void run() {
                    int i = NoticiasListadoFragment.this.getListView().getLastVisiblePosition();
                    int j = NoticiasListadoFragment.this.getListAdapter().getCount();
                    if (i + 1 < j)
                        getListView().addFooterView(NoticiasListadoFragment.this.backToTop);
                    }
                });
            if ("error" == null) {
                getListView().setEmptyView(getView().findViewById(R.id.tv_empty_text));
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            items.clear();
            getListView().setEmptyView(getView().findViewById(R.id.progress_bar));
        }

        protected void onProgressUpdate(Void[] paramArrayOfVoid) {
            super.onProgressUpdate(paramArrayOfVoid);
        }
    }
}