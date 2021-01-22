package com.nova.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.CircularDotsLoader;
import com.nova.onboarding.interfaces.AutenticarApi;
import com.nova.onboarding.model.AutenticarModel;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CapturaSelfieCedulaActivity extends AppCompatActivity {

    private ImageView imageViewFrontID, imageViewSelfie, imageViewSelfieFace;
    private Button buttonCaptureID, buttonSelfie, btnValidar, buttonSelfieFace;
    private TextView textViewCedula, textViewSelfie, textViewFotografia, textViewCambiarSelfie;
    private Bitmap frontBitmap, backBitmap, faceBitmap, frontSelfie;
    private Dialog ViewDatos, ViewSemaforo;
    private String nombre, fechaExpiracion, fechaNacimiento, apellidos, nacionalidad, numIdentificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_selfie_cedula);

        views();

        /*imageViewFrontID.setVisibility(View.VISIBLE);  //borrar
        imageViewSelfie.setVisibility(View.VISIBLE);*/

        frontBitmap = Informacion.getInstance().getFrontImageView();
        backBitmap = Informacion.getInstance().getBackImageView();
        faceBitmap = Informacion.getInstance().getFaceImageView();
        frontSelfie = Informacion.getInstance().getFrontSelfie();

        if(frontBitmap != null){
            buttonCaptureID.setBackground(new BitmapDrawable(getResources(), frontBitmap));
            imageViewFrontID.setImageResource(R.drawable.id_azul);
            textViewCedula.setText("");
            textViewFotografia.setVisibility(View.VISIBLE);
        }

        if(frontSelfie != null){
            //buttonSelfie.setBackground(new BitmapDrawable(getResources(), frontSelfie));

            buttonSelfieFace.setVisibility(View.VISIBLE);
            imageViewSelfieFace.setImageBitmap(frontSelfie);
            imageViewSelfie.setImageResource(R.drawable.selfie_azul);
            textViewSelfie.setText("");
            textViewCambiarSelfie.setVisibility(View.VISIBLE);

            Drawable botonValidar = getResources().getDrawable(R.drawable.boton_rojo);
            btnValidar.setEnabled(true);
            btnValidar.setBackground(botonValidar);
        }

        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarDatos();

            }
        });

        /*Intent intent = getIntent();  //borrar
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        buttonFrontID.setImageBitmap(bitmap);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            buttonFrontID.setImageBitmap(imageBitmap);
        }*/

        //buttonFrontID.setBackground(new BitmapDrawable(getResources(), bitmap));
        //obtenerBitmap(bitmap);

        buttonCaptureID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("cedula", 1);
                startActivity(intent);
            }
        });

        buttonSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("selfie", 2);
                startActivity(intent);
            }
        });

        buttonSelfieFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("selfie", 2);
                startActivity(intent);
            }
        });

    }

    public void views() {

        imageViewFrontID = findViewById(R.id.imgViewFrontID);
        imageViewSelfie = findViewById(R.id.imgViewSelfie);
        buttonCaptureID = findViewById(R.id.btnCaptureID);
        buttonSelfie = findViewById(R.id.btnSelfie);
        btnValidar = findViewById(R.id.btnValidar);
        textViewCedula = findViewById(R.id.tVCedula);
        textViewFotografia = findViewById(R.id.tVFotografia);
        textViewSelfie = findViewById(R.id.tVSelfie);
        textViewCambiarSelfie = findViewById(R.id.tVCambiarSelfie);
        imageViewSelfieFace = findViewById(R.id.imgViewSelfieFace);
        buttonSelfieFace = findViewById(R.id.btnSelfieFace);

    }

    public void mostrarDatos(){

        ViewDatos = new Dialog(this, R.style.AppTheme);
        ViewDatos.setContentView(R.layout.validar_datos);
        ViewDatos.show();

        autenticar();

        ImageView imageViewID = ViewDatos.findViewById(R.id.imgViewID);
        ImageView imageViewSelfie = ViewDatos.findViewById(R.id.imgViewSelfie);
        EditText editTextNombre = ViewDatos.findViewById(R.id.eTNombre);
        EditText editTextFechaExpiracion = ViewDatos.findViewById(R.id.eTFechaExpiracion);
        EditText editTextFechaNacimiento = ViewDatos.findViewById(R.id.eTFechaNacimiento);
        EditText editTextApellidos = ViewDatos.findViewById(R.id.eTApellidos);
        EditText editTextNacionalidad = ViewDatos.findViewById(R.id.eTNacionalidad);
        EditText editTextNumIdentificacion = ViewDatos.findViewById(R.id.eTNumIdentificacion);
        Button buttonSiguiente = ViewDatos.findViewById(R.id.btnSiguiente);

        TextView textViewPorcentaje = ViewDatos.findViewById(R.id.tVPorcentaje);
        ImageView imageViewCheckVerde = ViewDatos.findViewById(R.id.imgViewCheckVerificacionVerde);
        ImageView imageViewCheckRojo = ViewDatos.findViewById(R.id.imgViewCheckVerificacionRojo);
        CircularDotsLoader loader = ViewDatos.findViewById(R.id.progressBarLoader);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                loader.setSelectedColor(ContextCompat.getColor(CapturaSelfieCedulaActivity.this,R.color.celeste));
                loader.setFirstShadowColor(ContextCompat.getColor(CapturaSelfieCedulaActivity.this, R.color.celeste));
                loader.setSecondShadowColor(ContextCompat.getColor(CapturaSelfieCedulaActivity.this, R.color.celeste));
                textViewPorcentaje.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);

                /*imageViewCheckVerde.setVisibility(View.VISIBLE);  //borrar
                textViewPorcentaje.setText("0.99");
                loader.setVisibility(View.GONE);*/

                Drawable botonSiguiente = getResources().getDrawable(R.drawable.boton_rojo);
                buttonSiguiente.setEnabled(true);
                buttonSiguiente.setBackground(botonSiguiente);

                if(Informacion.getInstance().getEstado().equals("Positive")){

                    imageViewCheckVerde.setVisibility(View.VISIBLE);
                    textViewPorcentaje.setText(Informacion.getInstance().getSimilitud());

                }else{

                    imageViewCheckRojo.setVisibility(View.VISIBLE);
                    textViewPorcentaje.setText(Informacion.getInstance().getSimilitud());

                }
            }
        }, 3000);


        /*new android.os.Handler().postDelayed(

                new Runnable() {
                    public void run() {
                        TextView textViewPorcentaje = ViewDatos.findViewById(R.id.tVPorcentaje);
                        ImageView imageViewCheck = ViewDatos.findViewById(R.id.imgViewCheckVerificacion);
                        CircularDotsLoader loader = ViewDatos.findViewById(R.id.progressBarLoader);
                        loader.setSelectedColor(ContextCompat.getColor(CapturaSelfieCedulaActivity.this,R.color.celeste));
                        loader.setFirstShadowColor(ContextCompat.getColor(CapturaSelfieCedulaActivity.this, R.color.celeste));
                        loader.setSecondShadowColor(ContextCompat.getColor(CapturaSelfieCedulaActivity.this, R.color.celeste));
                        textViewPorcentaje.setVisibility(View.VISIBLE);
                        imageViewCheck.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.GONE);
                    }
                },
                5000);*/


        nombre = Informacion.getInstance().getNombre();
        fechaExpiracion = Informacion.getInstance().getFecha_expiracion();
        fechaNacimiento = Informacion.getInstance().getFecha_nacimiento();
        apellidos = Informacion.getInstance().getApellidos();
        nacionalidad = Informacion.getInstance().getNacionalidad();
        numIdentificacion = Informacion.getInstance().getNum_identificacion();

        imageViewID.setImageBitmap(faceBitmap);
        imageViewSelfie.setImageBitmap(frontSelfie);
        editTextNombre.setText(nombre);
        editTextFechaExpiracion.setText(fechaExpiracion);
        editTextFechaNacimiento.setText(fechaNacimiento);
        editTextApellidos.setText(apellidos);
        editTextNacionalidad.setText(nacionalidad);
        editTextNumIdentificacion.setText(numIdentificacion);

        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarFirma();
            }
        });

    }

    public void autenticar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AutenticarApi.AUTENTICAR_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /*String tokenFaceImage = getString(R.string.tokenFaceImage);  //borrar
        Informacion.getInstance().setTemplateFaceImage(tokenFaceImage);

        String templateSelfie = getString(R.string.templateSelfie);
        Informacion.getINSTANCE().setTemplateSelfieImage(templateSelfie);*/

        AutenticarApi autenticarApi = retrofit.create(AutenticarApi.class);
        Call<AutenticarResponse> call = autenticarApi.postAutenticar(new AutenticarModel(Informacion.getInstance().getTemplateFaceImage(), Informacion.getInstance().getTemplateSelfieImage()));
        call.enqueue(new Callback<AutenticarResponse>(){
            @Override
            public void onResponse(Call<AutenticarResponse> call, Response<AutenticarResponse> response) {
                if(response.isSuccessful()){

                    if(response.body().getAuthStatus().equals("Positive")){
                        //Toast.makeText(getApplicationContext(), "Positive", Toast.LENGTH_LONG).show();
                    }else{
                        //Toast.makeText(getApplicationContext(), "Negative", Toast.LENGTH_LONG).show();
                    }

                    /*DecimalFormat df = new DecimalFormat("#.##");
                    double number = response.body().getSimilarity();
                    df.format(number);*/

                    Double number = response.body().getSimilarity();
                    /*DecimalFormat df = new DecimalFormat("#.##");
                    String formatted = df.format(number);
                    Toast.makeText(getApplicationContext(), "Negative" + formatted, Toast.LENGTH_LONG).show();
                    FormatoPrecio.formatNum(number);*/

                    String strDouble = String.format(Locale.ENGLISH,"%.2f", number);
                    System.out.println(strDouble);

                    Informacion.getInstance().setEstado(response.body().getAuthStatus());
                    Informacion.getInstance().setSimilitud(strDouble);

                }
            }

            @Override
            public void onFailure(Call<AutenticarResponse> call, Throwable t) {

            }
        });
    }

    public void validarFirma() {

        androidx.appcompat.app.AlertDialog dialog;
        View view = getLayoutInflater().inflate(R.layout.validar_firma,null);
        androidx.appcompat.app.AlertDialog.Builder myAlertDialog = new androidx.appcompat.app.AlertDialog.Builder(CapturaSelfieCedulaActivity.this);

        LinearLayout mContent = view.findViewById(R.id.linearLayout);
        Button buttonCorregir = view.findViewById(R.id.btnCorregir);
        Button buttonAceptar = view.findViewById(R.id.btnAceptar);
        final CaptureSignatureView mSig = new CaptureSignatureView(CapturaSelfieCedulaActivity.this, null);
        //mSig.setBackgroundColor(ContextCompat.getColor(this, R.color.gris_oscuro));
        mContent.addView(mSig, LayoutParams.MATCH_PARENT, 500);

        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSig.firma == true){

                    ProgressDialog progress = new ProgressDialog(CapturaSelfieCedulaActivity.this);
                    progress.setTitle("Onboarding");
                    progress.setMessage("Cargando...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setCancelable(false);
                    progress.show();

                    validarSemaforo();

                    progress.dismiss();
                    //Toast.makeText(CapturaSelfieCedulaActivity.this, "Exito al guardar la firma", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CapturaSelfieCedulaActivity.this, "Por favor, debe realizar la firma para poder continuar.", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonCorregir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSig.clear();
            }
        });

        myAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
               @Override
               public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                   if (keyCode == KeyEvent.KEYCODE_BACK &&
                           event.getAction() == KeyEvent.ACTION_UP &&
                           !event.isCanceled()) {
                       dialog.cancel();
                       return true;
                   }
                   return false;
               }
       });

        myAlertDialog.setView(view);
        myAlertDialog.setCancelable(false);
        dialog = myAlertDialog.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    public void validarSemaforo() {

        ViewSemaforo = new Dialog(this, R.style.AppTheme);
        ViewSemaforo.setContentView(R.layout.validar_semaforo);
        ViewSemaforo.show();
        Button buttonProceder = ViewSemaforo.findViewById(R.id.btnProceder);
        ImageView imageViewSemaforo = ViewSemaforo.findViewById(R.id.imgViewSemaforo);
        TextView textViewCorrecta = ViewSemaforo.findViewById(R.id.tVVerCorrecta);

        /*Drawable semaforoVerde = getResources().getDrawable(R.drawable.semaforo_verde);
        imageViewSemaforo.setBackground(semaforoVerde);
        textViewCorrecta.setText(getResources().getString(R.string.validacion_correcta));*/ //borrar

        if(Informacion.getInstance().getEstado().equals("Positive")){

            Drawable semaforoVerde = getResources().getDrawable(R.drawable.semaforo_verde);
            imageViewSemaforo.setBackground(semaforoVerde);
            textViewCorrecta.setText(getResources().getString(R.string.validacion_correcta));

        }else{

            Drawable semaforoRojo = getResources().getDrawable(R.drawable.semaforo_rojo);
            imageViewSemaforo.setBackground(semaforoRojo);
            textViewCorrecta.setText(getResources().getString(R.string.validacion_incorrecta));
            int colorRojo = 0xffC0282F;
            textViewCorrecta.setTextColor(colorRojo);

        }

        /*if(variable == positive){  //borrar revisar

            Drawable botonValidar = getResources().getDrawable(R.drawable.semaforo_verde);
            btnValidar.setEnabled(true);
            btnValidar.setBackgroundDrawable(botonValidar);

            semaforo verde
        }else{

            Drawable botonValidar = getResources().getDrawable(R.drawable.semaforo_rojo);
            btnValidar.setEnabled(true);
            btnValidar.setBackgroundDrawable(botonValidar);

            rojo
        }*/

    }

   @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}