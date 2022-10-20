package com.social.skaters.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.social.skaters.R;
import com.social.skaters.firebaseRef.FirebaseRef;
import com.social.skaters.helper.Permissions;
import com.social.skaters.models.User;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityConfigUserAtual extends AppCompatActivity {

    private CircleImageView imgPerfil;
    private TextView nomeUser;
    private EditText editNivelUser;
    private ImageView btnCamera, btnGallery, defNivel;

    private User user;
    private ValueEventListener valueEventListenerName;

    private DatabaseReference db_userAtualRef;
    private FirebaseUser userAtual;

    private static final int REQUEST_CODE_CAMERA = 100;
    private static final int REQUEST_CODE_GALLERY = 200;

    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_config_user_atual );

        imgPerfil = findViewById(R.id.circleImageViewFoto);
        nomeUser = findViewById(R.id.textViewNomeUser);
        editNivelUser = findViewById(R.id.editTextNivel);

        btnCamera = findViewById(R.id.imageViewCamera);
        btnGallery = findViewById(R.id.imageViewGallery);
        defNivel = findViewById(R.id.imageViewDefNivel);

        user = new User();

        // validate permissions
        Permissions.validatePermissions(permissions, this, 1);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Configurações do perfil");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // abrir camera
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View view) {

                Intent cameraOpen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraOpen, REQUEST_CODE_CAMERA);
//                if (cameraOpen.resolveActivity(getPackageManager()) != null) {

//                }

            }
        });

        //abrir galeria de foto
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galeriaOpen = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeriaOpen, REQUEST_CODE_GALLERY);

//                if (galeriaOpen.resolveActivity(getPackageManager()) != null) {
//                }

            }
        });

        // define o nivel do usuario
        defNivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nivel = editNivelUser.getText().toString();

                if ( !nivel.isEmpty() ){

                    user.atulizarNivel( nivel );
                    Toast.makeText(getApplicationContext(),
                            "Nível atulizado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void definirInfoUser(){

        userAtual = FirebaseRef.getFirebaseUserLogado();
        db_userAtualRef = FirebaseRef.getFirebaseDatabase()
                .child("usuarios")
                .child( FirebaseRef.getIdUserAtual() );

        valueEventListenerName = db_userAtualRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue( User.class );

                nomeUser.setText( user.getNome() );
                editNivelUser.setText( user.getNivel() );
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if( userAtual.getPhotoUrl() != null ){

            Glide.with(getApplicationContext()).load( userAtual.getPhotoUrl() ).into( imgPerfil );
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap image = null;

            try {
                switch (requestCode) {
                    case REQUEST_CODE_CAMERA:
                        assert data != null;

                        image = (Bitmap) data.getExtras().get("data");
                        break;

                    case REQUEST_CODE_GALLERY:
                        assert data != null;

                        Uri img_selecionada = data.getData();
                        image = MediaStore.Images.Media.getBitmap(getContentResolver(), img_selecionada);
                        break;
                }

                if (image != null) {
                    imgPerfil.setImageBitmap(image);
                    salvarImgStorage(image);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private void salvarImgStorage(Bitmap image) {

        String idUser = FirebaseRef.getIdUserAtual();

        // Recuperar dados de imagem para o firebase
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] dadosImg = baos.toByteArray();

        // salvar no storage Firebase
        final StorageReference imgRef = FirebaseRef.getFirebaseStorageRef()
                .child("imagens")
                .child("perfil")
                .child(idUser + ".jpeg");

        UploadTask uploadTask = imgRef.putBytes(dadosImg);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao fazer download da imagem",
                        Toast.LENGTH_SHORT).show();
            }
        })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        Toast.makeText(getApplicationContext(), "Sucesso ao fazer download da imagem ",
                                Toast.LENGTH_LONG).show();

                        imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                Uri url = task.getResult();
                                atualizarFotoPerfil(url);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                });
    }

    //atualizar foto de perfil
    private void atualizarFotoPerfil(Uri url) {
        boolean retorno = FirebaseRef.atulizarFotoUser(url);

        if (retorno ){
            user.setUrlFoto( url.toString() );
            user.atualizarUrlFoto();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        db_userAtualRef.removeEventListener( valueEventListenerName );
    }

    @Override
    protected void onStart() {
        super.onStart();
        definirInfoUser();
    }
}