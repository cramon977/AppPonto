package com.empresa.pontorouxinol;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;

public class Listener implements OnSuccessListener {

    //@Override
    //public void onSuccess(Void aVoid) {
     //   Log.d("BancoDeDados","Sucesso ao salvar dados!");
   // }

    @Override
    public void onSuccess(Object o) {
        Log.d("BancoDeDados","Sucesso ao salvar dados!");
    }
}

interface Carro {
    void buzinar ();
}

class Mercedes implements Carro {

    @Override
    public void buzinar() {

    }
}
class Mecanico {
    void testarCarro (Carro carro) {

    }
}