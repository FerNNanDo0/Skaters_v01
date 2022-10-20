package com.social.skaters.firebaseRef;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.social.skaters.helper.CodificarBase64;

public class FirebaseRef {

    // referencia do firebaseAuth
    public static FirebaseAuth getFirebaseAuthRef(){
        return FirebaseAuth.getInstance();
    }

    // referencia do firebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        return FirebaseDatabase.getInstance().getReference();
    }

    // referencia do firebaseStorage
    public static StorageReference getFirebaseStorageRef(){
        return FirebaseStorage.getInstance().getReference();
    }

    // retor o User atual se estiver logado
    public static FirebaseUser getFirebaseUserLogado(){
        return getFirebaseAuthRef().getCurrentUser();
    }

    public static String getIdUserAtual(){
        return CodificarBase64.codificarBase64(FirebaseRef.getFirebaseUserLogado().getEmail());
    }


    // atualizar img perfil
    public static boolean atulizarFotoUser(Uri url) {

        try {
            FirebaseUser userLogado = getFirebaseUserLogado();

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            userLogado.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (!task.isSuccessful()) {
                                Log.d("PERFIL", "Erro ao atulizar");
                            }

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
