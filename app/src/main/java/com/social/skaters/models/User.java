package com.social.skaters.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.social.skaters.firebaseRef.FirebaseRef;
import com.social.skaters.helper.CodificarBase64;

import java.util.HashMap;
import java.util.Map;

public class User {

    private DatabaseReference db_usuariosRef;

    private String nome;
    private String email;
    private String senha;
    private String urlFoto;
    private String nivel;

    public User(){

    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }


    public void salvarDadosNo_db(){

        String idUser = CodificarBase64.codificarBase64( getEmail() );

        db_usuariosRef = FirebaseRef.getFirebaseDatabase();

        db_usuariosRef
                .child("usuarios")
                .child( idUser )
                .setValue( this );
    }

    public void atualizarUrlFoto(){
        String idUser = FirebaseRef.getIdUserAtual();
        DatabaseReference database = FirebaseRef.getFirebaseDatabase();

        DatabaseReference userRef = database
                .child("usuarios")
                .child( idUser );

        Map<String, Object> valorUser = coverterParaMap();

        userRef.updateChildren( valorUser );

    }

    public void atulizarNivel(String nivel){
        String idUser = FirebaseRef.getIdUserAtual();
        DatabaseReference database = FirebaseRef.getFirebaseDatabase();

        DatabaseReference db_userRef = database
                .child("usuarios")
                .child( idUser );

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("nivel", nivel );

        db_userRef.updateChildren( usuarioMap );
    }


    @Exclude
    public Map<String, Object> coverterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put( "urlFoto", getUrlFoto() );

        return usuarioMap;
    }
}
