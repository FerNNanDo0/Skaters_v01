package com.social.skaters.firebaseRef;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
}
