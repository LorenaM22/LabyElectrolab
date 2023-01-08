package com.example.electrolab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

public class Monologo extends AppCompatActivity {
    ImageView laby;
    TextView laby_text;
    Button boton;
    boolean pasar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monologo);

        laby = findViewById(R.id.laby);
        laby_text = findViewById(R.id.laby_text);
        boton = findViewById(R.id.skip);

        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pasar = false;
                Intent intent = new Intent(Monologo.this, DispositivosVinculados.class);
                startActivity(intent);

            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                laby.setImageResource(R.drawable.imagen4);
                laby_text.setText("El maletin Electrolab como su nombre dice es un maletin que puedes llevar en la mano a cualquier lado");
            }
        }, 10000);

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            public void run() {
                laby.setImageResource(R.drawable.imagen13);
                laby_text.setText("Con el vas a aprender un montón de cosas, desde programación en Arduino hasta el montaje de circuitos electrónicos");
            }
        }, 20000);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                laby.setImageResource(R.drawable.imagen11);
                laby_text.setText("Y para ayudarte a empezar he creado esta app");
            }
        }, 30000);

        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            public void run() {
                laby.setImageResource(R.drawable.imagen7);
                laby_text.setText("Pero antes de comenzar debes seguir las instrucciones que te voy a dar a continuación");
            }
        }, 35000);

        Handler handler35 = new Handler();
        handler35.postDelayed(new Runnable() {
            public void run() {
                laby.setImageResource(R.drawable.imagen3);
                laby_text.setText("Primero deberás seleccionar el bluetooth del maletín para conectarte a él (no te olvides de activar el botón que habilita el bluetooth en el maletín)");
            }
        }, 45000);

        Handler handler4 = new Handler();
        handler4.postDelayed(new Runnable() {
            public void run() {
                laby.setImageResource(R.drawable.imagen5);
                laby_text.setText("Después superpuesta a la cámara va a aparecer un rectángulo donde deberás situar el maletín con 4 post-it verdes en sus esquinas (si tienes algun problema con la cámara accede a ajustes y da permisos a la app)");
            }
        }, 55000);

        Handler handler5 = new Handler();
        handler5.postDelayed(new Runnable() {
            public void run() {
                laby.setImageResource(R.drawable.imagen8);
                laby_text.setText("Y un vez que todo haya ido bien se te mostrará una plantilla con la que puedes interactuar. ¡Vamos a ello!");
            }
        }, 65000);


        Handler handler6 = new Handler();
        handler6.postDelayed(new Runnable() {
            public void run() {
                if(pasar) {
                    Intent intent = new Intent(Monologo.this, DispositivosVinculados.class);
                    startActivity(intent);
                }
              }
        }, 75000);
    }
}