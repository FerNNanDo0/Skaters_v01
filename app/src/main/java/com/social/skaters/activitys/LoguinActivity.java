package com.social.skaters.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.social.skaters.R;
import com.social.skaters.firebaseRef.FirebaseRef;

import java.util.Objects;

public class LoguinActivity extends AppCompatActivity {

    private FirebaseUser userLogado;
    private FirebaseAuth log_user;
    private Intent mainActivity;
    private Intent cadastroActivity;

    private Button btnLogar;

    private TextInputEditText textEmailLog, textSenhaLog;

    String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loguin);

        btnLogar = findViewById(R.id.buttonLogar);
        textEmailLog = findViewById(R.id.editTextEmailLog);
        textSenhaLog = findViewById(R.id.editTextPasswordLog);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Loguin Skaters");
        }

        // btnLogar User no App
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // verificar campos antes de fazer o loguin
                if ( !textEmailLog.getText().toString().isEmpty() ){
                    if( !textSenhaLog.getText().toString().isEmpty() ){

                        // obter textos dos campos
                        email =  textEmailLog.getText().toString();
                        senha = textSenhaLog.getText().toString();

                        logarUserFirebase( email, senha );
                    }else{
                        // preencha a Senha de acesso
                        Toast.makeText(getApplicationContext(), "Preencha a Senha de acesso", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    // preencha o E-mail de acesso
                    Toast.makeText(getApplicationContext(), "Preencha o E-mail de acesso", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    // logar o usuario se ja for cadstrado
    public void logarUserFirebase(String email, String senha){

        log_user = FirebaseRef.getFirebaseAuthRef();

        log_user.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            // user logado com sucesso
                            mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity( mainActivity );

                            finish();
                        }else{

                            try{
                                throw task.getException();

                            }catch (FirebaseAuthInvalidCredentialsException e){

                                Toast.makeText(getApplicationContext(),
                                        "Dados inválido", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();

                            }catch (FirebaseAuthEmailException e){

                                Toast.makeText(getApplicationContext(),
                                        "E-mail de Usuário inválido ou não cadastrado", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();

                            }catch (FirebaseAuthInvalidUserException e){
                                Toast.makeText(getApplicationContext(),
                                        "Usuário não cadastrado", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();

                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Falha ao autenticar Usuário", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        // verificar se ja tem um usuario logado.
        userLogado = FirebaseRef.getFirebaseUserLogado();
        if ( userLogado != null){

            // se ja tem um user logado vai para tela MAIN.
            mainActivity = new Intent(this, MainActivity.class);
            startActivity( mainActivity );

            finish();
        }
    }


    // cadastrar User no App
    public void activityCadastrarUser( View view){

        // ir para tela de cadastro de usuarios
        cadastroActivity = new Intent(this, CadastroActivity.class);
        startActivity( cadastroActivity );

    }

}