package com.wisekrakr.david.teachwise.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class Extensions {
    public static String getFileExtension(Uri uri, Context context){
        ContentResolver contentResolver = context.getContentResolver();

        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
