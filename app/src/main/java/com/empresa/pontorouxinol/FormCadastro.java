package com.empresa.pontorouxinol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {
// criar objetos que instanciam cada compnentes
    private EditText nome_cadastro, senha_cadastro, email_cadastro;
    private Button botao_cadastrar;

    //criar array de mensagens para erros.
    String[] mensagens = {"Preencha todos os campos ","Cadastro realizado com sucesso","Cadastre-se"};
    String usuarioId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();
        IniciarComponentes();

        botao_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String nome = nome_cadastro.getText().toString();
               String email = email_cadastro.getText().toString();
               String senha = senha_cadastro.getText().toString();

                if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                // o Snackbar.make,precisamos como parametros 1) view (v),
                // 2) mensagem de erro ou sucesso, 3) definir o tempo que a mensagem
                // vai mostrar em tela.
                    Snackbar snackbar = Snackbar.make(v,mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else{
                    cadastrarUsuario(v);
                }
            }
        });
    }
    private void cadastrarUsuario(View v){
    // pega oque e digitado e converte para texto com toString().
        String email = email_cadastro.getText().toString();
        String senha = senha_cadastro.getText().toString();
    // iniciando firebase Auth. para cadastrar usuarios (nesse contexto é cadastro).
    // e recuperando a instancia do servidor do fire base com getInstance.
    // e depois criando um usuário com email e senha.Pasando como parametro email e senha.
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()){//O objeto Task responsavel pelo cadastro e autenticação de usuário.
                     // ele vai ter o resultado do cadastro

                     SalvarDadosUsuario();// quando o usuáo for cadastrado com sucesso alem de mostrar,
                     // a menssagem, irá salvar os dados do usuário no banco. tipo nome telefone etc.

                     Snackbar snackbar = Snackbar.make(v,mensagens[1], Snackbar.LENGTH_SHORT);
                     snackbar.setBackgroundTint(Color.WHITE);
                     snackbar.setTextColor(Color.BLACK);
                     snackbar.show();
                 }else {
                     String erro;
                     try {// dentro do try vem  um throw, ele tenta obter uma exceção.
                         throw task.getException();
                     }catch (FirebaseAuthWeakPasswordException e) {
                         erro = "Digite uma senha com 6 caracteres";
                     }catch (FirebaseAuthUserCollisionException e) {
                         erro = "Essa conta já foi cadastrada";
                     }catch(FirebaseAuthInvalidCredentialsException e) {
                         erro = "E-mail inválido";
                     }catch (Exception e) {
                         erro = "Erro ao cadatrar";
                     }
                     Snackbar snackbar = Snackbar.make(v,erro, Snackbar.LENGTH_SHORT);
                     snackbar.setBackgroundTint(Color.WHITE);
                     snackbar.setTextColor(Color.BLACK);
                     snackbar.show();
                 }
            }
        });
    }

    private void SalvarDadosUsuario() {
        String nome = nome_cadastro.getText().toString();
        //instancia o o objeto Firestore.
        FirebaseFirestore bancoDeDados = FirebaseFirestore.getInstance();

        //criar um map de dados.

        Map<String, Object> usuarios = new HashMap<>();
        //a string é a chave e o objeto é o valor.
        usuarios.put("nome", nome);

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = bancoDeDados.collection("Usuários").document(usuarioId);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("BancoDeDados","Sucesso ao salvar dados!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BancoDeDados Falha", "Falha ao salvar os dados!" + e.toString());
            }
        });
    }



    private void IniciarComponentes() {
        // recuperando cada componente. e atribuindo aos objetos devidos.
        nome_cadastro = findViewById(R.id.nome_cadastro);
        email_cadastro = findViewById(R.id.email_cadastro);
        senha_cadastro = findViewById(R.id.senha_cadastro);
        botao_cadastrar = findViewById(R.id.button_cadastrar);
    }


}
