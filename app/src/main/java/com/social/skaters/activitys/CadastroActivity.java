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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.social.skaters.R;
import com.social.skaters.firebaseRef.FirebaseRef;
import com.social.skaters.models.User;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth cadastrarUser;
    private DatabaseReference db_user;

    private Button btnCadastrar;

    private TextInputEditText textNome, textEmail, textSenha, textSenhaConfirme ;

    String nome, email, senha, confirmeSenha;

    User user;

    Intent mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btnCadastrar = findViewById(R.id.buttonCadastrar);

        textNome = findViewById(R.id.editTextNomeCad);
        textEmail = findViewById(R.id.editTextEmailCad);
        textSenha = findViewById(R.id.editTextPasswordCad);
        textSenhaConfirme = findViewById(R.id.editTextPasswordCadComfirm);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cadastro Skaters");
        }

        // button de cadastrar Usuário
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // verificar campos antes de fazer o cadastro
                if ( !textNome.getText().toString().isEmpty() ){
                    if( !textEmail.getText().toString().isEmpty() ){
                        if( !textSenha.getText().toString().isEmpty() ){
                            if( !textSenhaConfirme.getText().toString().isEmpty() ){

                                // obter textos dos campos
                                nome = textNome.getText().toString();
                                email = textEmail.getText().toString();
                                senha = textSenha.getText().toString();
                                confirmeSenha = textSenhaConfirme.getText().toString();

                                if ( senha.equals(confirmeSenha) ){

                                    // salvar informacao do usuario
                                    user = new User();
                                    user.setNome( nome );
                                    user.setEmail( email );
                                    user.setSenha( senha );
                                    user.setUrlFoto("");
                                    user.setNivel("");

                                 //cadastrarUserFirebase( email, senha, nome);
                                    cadastrarUserFirebase(user);

                                }else{
                                    Toast.makeText(getApplicationContext(), "As senhas de cadastro são diferentes", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Toast.makeText(getApplicationContext(), "Comfirme a senha de cadastro", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Preencha a Senha de acesso", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        // preencha a Senha de acesso
                        Toast.makeText(getApplicationContext(), "Preencha o E-mail de acesso", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    // preencha o E-mail de acesso
                    Toast.makeText(getApplicationContext(), "Preencha o Nome de usuário", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    //
    public void cadastrarUserFirebase(User user){

        cadastrarUser = FirebaseRef.getFirebaseAuthRef();

        cadastrarUser.createUserWithEmailAndPassword( user.getEmail(), user.getSenha() )
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful()){

                            user.salvarDadosNo_db();

                            // user cadastrado com sucesso
                            mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity( mainActivity );

                            finish();

                        }else{

                            try{
                                throw Objects.requireNonNull(task.getException());

                            }catch (FirebaseAuthWeakPasswordException e){

                                Toast.makeText(getApplicationContext(),
                                        "Digite uma senha mais forte", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }catch (FirebaseAuthEmailException e){

                                Toast.makeText(getApplicationContext(),
                                        "E-mail inválido", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }catch (FirebaseAuthUserCollisionException e){

                                Toast.makeText(getApplicationContext(),
                                        "Usuário ja tem cadastro no App", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                            catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}