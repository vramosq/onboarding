package com.nova.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsuario, editTextPassword;
    private Button buttonIngresar;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();

        editTextUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){

                    Drawable botonIngresar = getResources().getDrawable(R.drawable.boton_rojo);
                    buttonIngresar.setEnabled(true);
                    buttonIngresar.setBackground(botonIngresar);

                }
            }
        });

        /*editTextUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(s.toString().equals("")){
                    buttonIngresar.setEnabled(false);
                }else{
                    buttonIngresar.setEnabled(true);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().equals("")){
                    buttonIngresar.setEnabled(false);
                }else{
                    buttonIngresar.setEnabled(true);
                }

            }
        });*/

        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (!editTextUsuario.getText().toString().equals("")) {

                    /*new android.os.Handler().postDelayed(

                            new Runnable() {
                                public void run() {

                                    progress = new ProgressDialog(LoginActivity.this);
                                    progress.setTitle("Onboarding");
                                    progress.setMessage("Cargando...");
                                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progress.setCancelable(false);
                                    progress.show();

                                    Intent intent = new Intent(LoginActivity.this, CapturaSelfieCedulaActivity.class);
                                    startActivity(intent);

                                    progress.dismiss();

                                }
                            },
                            5000);*/

                    progress = new ProgressDialog(LoginActivity.this);
                    progress.setTitle("Onboarding");
                    progress.setMessage("Cargando...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setCancelable(false);
                    progress.show();

                    Intent intent = new Intent(LoginActivity.this, CapturaSelfieCedulaActivity.class);
                    startActivity(intent);

                    progress.dismiss();
                //}
            }
        });
    }

    private void bindViews() {
        editTextUsuario = (EditText) findViewById(R.id.eTUsuario);
        editTextPassword = (EditText) findViewById(R.id.eTPassword);
        buttonIngresar = (Button) findViewById(R.id.btnIngresar);
    }

    @Override
    public void onBackPressed(){

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}