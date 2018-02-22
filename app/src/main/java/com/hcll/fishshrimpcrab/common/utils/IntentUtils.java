package com.hcll.fishshrimpcrab.common.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by hong on 2018/2/16.
 */

public class IntentUtils {

    public static Intent getCameraIntent(String imageFilePath) {
        Intent photoIntent = new Intent();
        photoIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageFilePath)));
        return photoIntent;
    }

}
