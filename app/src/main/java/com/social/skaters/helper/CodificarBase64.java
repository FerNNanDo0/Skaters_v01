package com.social.skaters.helper;

import android.util.Base64;

import com.social.skaters.firebaseRef.FirebaseRef;

public class CodificarBase64 {

    public static String codificarBase64(String tx){
        return Base64
                .encodeToString( tx.getBytes(), Base64.DEFAULT )
                .replaceAll("(\\n|\\r)", "");
    }

    public static String decoBase64(String tx){
        return new String( Base64.decode( tx, Base64.DEFAULT ) );
    }

}
