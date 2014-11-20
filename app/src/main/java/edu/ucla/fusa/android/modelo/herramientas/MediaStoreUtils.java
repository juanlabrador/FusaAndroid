package edu.ucla.fusa.android.modelo.herramientas;

import android.content.Context;
import android.content.Intent;

public class MediaStoreUtils {
    public static Intent getPickImageIntent(Context paramContext) {
        Intent localIntent = new Intent();
        localIntent.setType("image/*");
        localIntent.setAction("android.intent.action.GET_CONTENT");
        return Intent.createChooser(localIntent, "Seleccionar foto");
    }
}
