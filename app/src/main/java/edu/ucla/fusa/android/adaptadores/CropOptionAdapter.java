package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.ucla.fusa.android.modelo.CropOption;
import java.util.ArrayList;

public class CropOptionAdapter extends ArrayAdapter<CropOption>
{
  private LayoutInflater mInflater;
  private ArrayList<CropOption> mOptions;

  public CropOptionAdapter(Context paramContext, ArrayList<CropOption> paramArrayList)
  {
    super(paramContext, 2130903044, paramArrayList);
    this.mOptions = paramArrayList;
    this.mInflater = LayoutInflater.from(paramContext);
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null)
      paramView = this.mInflater.inflate(2130903044, null);
    CropOption localCropOption = (CropOption)this.mOptions.get(paramInt);
    if (localCropOption != null)
    {
      ((ImageView)paramView.findViewById(2131296298)).setImageDrawable(localCropOption.icon);
      ((TextView)paramView.findViewById(2131296299)).setText(localCropOption.title);
      return paramView;
    }
    return null;
  }
}

/* Location:           /home/juanlabrador/Escritorio/apk/dex2jar-0.0.9.15/classes_dex2jar.jar
 * Qualified Name:     edu.ucla.fusa.android.adaptadores.CropOptionAdapter
 * JD-Core Version:    0.6.2
 */