package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListDrawer;
import java.util.ArrayList;

public class NavigationAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<ItemListDrawer> mItems;

    public NavigationAdapter(Activity paramActivity, ArrayList<ItemListDrawer> paramArrayList) {
        this.mActivity = paramActivity;
        this.mItems = paramArrayList;
    }

    public void clear() {
        mItems.clear();
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int paramInt) {
        return mItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = mActivity.getLayoutInflater();
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.custom_item_list_drawer, null);
            mViewHolder.mTitulo = ((TextView) convertView.findViewById(R.id.tv_funcionalidad_drawer));
            mViewHolder.mIcono = ((ImageView) convertView.findViewById(R.id.iv_icono_drawer));
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        
        ItemListDrawer item = mItems.get(position);
        mViewHolder.mTitulo.setText(item.getTitulo());
        mViewHolder.mIcono.setImageResource(item.getIcono());
        return convertView;
    }

    public static class ViewHolder {
        ImageView mIcono;
        TextView mTitulo;
    }
}