package com.example.electrolab;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.Config.RGB_565;
import static android.graphics.PixelFormat.RGBA_1010102;
import static android.graphics.PixelFormat.RGBA_F16;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.util.Size;
import android.util.TimingLogger;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.google.common.util.concurrent.ListenableFuture;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;



public class MainActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "not found";
    private static final String TIMING_LOG_TAG =  "no se que es";

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    ImageView bordes;
    ImageView plantilla;
    TextView textAmigo;
    TextView textplantilla;
    Button script;
    Button clear;
    TextView coment;
    ImageView dot,dot1,dot2,dot3;
    VideoView video;
    ImageView amigo;

    int inf_izq=0;
    int inf_drch=0;
    int sup_izq=0;
    int sup_drch=0;

    String datos ;
    //-------------------------------------------
    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address = null;
    //-------------------------------------------

    float Y_sup=250;
    float Y_inf=70;
    float U_sup=10;
    float U_inf=-10;
    float V_sup=5;
    float V_inf=0;

    int bye=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewView = findViewById(R.id.previewView);
        bordes = findViewById(R.id.bordes);
       // plantilla = findViewById(R.id.plantilla);
        textAmigo = findViewById(R.id.textAmigo);
        textplantilla= findViewById(R.id.textplantilla);
        script = findViewById(R.id.script);
        clear = findViewById(R.id.clear);
        plantilla= findViewById(R.id.plantilla);
        coment = findViewById(R.id.coment);
        video = findViewById(R.id.video);
        amigo = findViewById(R.id.amigo);
        /*dot = findViewById(R.id.dot);

        dot1 = findViewById(R.id.dot1);

        dot2 = findViewById(R.id.dot2);

        dot3 = findViewById(R.id.dot3);*/
        //CONEXION BLUETOOTH

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    //Interacción con los datos de ingreso

                }
            }
        };
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        VerificarEstadoBT();

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));


        script.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Modulo bluetooth para ejecutar script maletin
                MyConexionBT.write(datos);

                /*DATOS
                 * =L led
                 * =P pantalla lcd
                 * =C placa
                 * =B buzzer
                 * =S servo
                 * =M matriz leds
                 * */


                amigo.setImageResource(R.drawable.imagen16);
                if (datos=="L"){
                    //cambiar expresion de laby y poner algo que acompañe a los leds
                    textAmigo.setText("Esto es un ejemplo \n" +
                            "        digitalWrite(4, HIGH);   \n" +
                            "        delay(250);\n" +
                            "        digitalWrite(4, LOW);    ");
                    FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(720,145);
                    layoutparams.setMarginStart(150);
                    layoutparams.setMargins(0, 1250, 0, 0);
                    video.setLayoutParams(layoutparams);

                    video.setVisibility(View.VISIBLE);
                    video.setVideoPath("android.resource://" + getPackageName() + "/" + "/raw/"+R.raw.luces4);
                    video.start();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            video.start();
                        }
                    }, 2000);

                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        public void run() {
                            video.setVisibility(View.GONE);
                            textAmigo.setText("Estos son los botones LED, que al pulsarse se iluminan y te permiten llevar acabo acciones");
                        }
                    }, 4000);

                }
                else if (datos=="P"){
                    //cambiar expresion de laby y poner un flecha indicando el detector temp y hum
                    textAmigo.setText("Esto es un ejemplo \n" +
                            "        lcd.setCursor(col, fila);\n" +
                            "        lcd.print(\"Electrolab\");");
                    FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(150,150);
                    layoutparams.setMarginStart(690);
                    layoutparams.setMargins(0, 990, 0, 0);
                    video.setLayoutParams(layoutparams);

                    video.setVisibility(View.VISIBLE);
                    video.setVideoPath("android.resource://" + getPackageName() + "/" + "/raw/"+R.raw.flecha);
                    video.start();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            video.start();
                        }
                    }, 2000);

                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        public void run() {
                            video.setVisibility(View.GONE);
                            textAmigo.setText("Esta es la pantalla LCD, con ella puedes mostrar el texto que tu quieras");
                        }
                    }, 4000);

                }
                else if (datos=="C"){
                    //cambiar expresion y texto de laby y mostrar un video
                    //que vaya mostrando como montar un circuito
                    FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(1500,350);
                    layoutparams.setMarginStart(350);
                    layoutparams.setMargins(0, 1250, 0, 0);
                    video.setLayoutParams(layoutparams);

                    video.setVisibility(View.VISIBLE);
                    video.setVideoPath("android.resource://" + getPackageName() + "/" + "/raw/"+R.raw.placa1);
                    video.start();
                    textAmigo.setText("Abajo te mostramos que sería el montaje de un circuito RC, (resistencia + condensador), que se utiliza para filtrar señales");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            video.setVideoPath("android.resource://" + getPackageName() + "/" + "/raw/"+R.raw.placa2);
                            video.start();
                            textAmigo.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
                            textAmigo.setText("La señal roja sería la señal de entrada y que da lugar a la señal azul a la salida 3(en esta vemos como se cargaría y descargaría el condensador)");
                        }
                    }, 4000);

                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        public void run() {
                            video.setVisibility(View.GONE);
                            textAmigo.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
                            textAmigo.setText("En esta placa puedes colocar diferentes componentes");
                        }
                    }, 8000);
                }
                else if (datos=="B"){
                    //añadir algo que acompañe el buzzer (mario)
                    textAmigo.setText("Esto es un ejemplo \n" +
                            "   buzz(PINBUZZER, melody[thisNote],noteDuration);");
                    FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(440,280);
                    layoutparams.setMarginStart(150);
                    layoutparams.setMargins(0, 1250, 0, 0);
                    video.setLayoutParams(layoutparams);

                    video.setVisibility(View.VISIBLE);
                    video.setVideoPath("android.resource://" + getPackageName() + "/" + "/raw/"+R.raw.mario);
                    video.start();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            video.start();
                        }
                    }, 2000);

                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        public void run() {
                            video.setVisibility(View.GONE);
                            textAmigo.setText("Te presento al zumbador que permite sacar un sonido a una determinada frecuencia");
                        }
                    }, 4000);


                }
                else if (datos=="S"){
                    //añadir algo que acompañe al servo
                    textAmigo.setText("Esto es un ejemplo \n" +
                            "        myservo.write(20);      \n" +
                            "        delay(1000);\n" +
                            "        myservo.write(40); ");
                    FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(150,100);
                    layoutparams.setMarginStart(840);
                    layoutparams.setMargins(0, 1050, 0, 0);
                    video.setLayoutParams(layoutparams);

                    video.setVisibility(View.VISIBLE);
                    video.setVideoPath("android.resource://" + getPackageName() + "/" + "/raw/"+R.raw.servo);
                    video.start();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            video.start();
                        }
                    }, 2000);

                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        public void run() {
                            video.setVisibility(View.GONE);
                            textAmigo.setText("Esto es un servo, se trata de un motor que gira hasta una posición según el ángulo indicado");
                        }
                    }, 4000);


                }
                else if (datos=="M"){
                    //añadir algo que acompañe a la matriz de leds
                    textAmigo.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
                    textAmigo.setText("Esto es un ejemplo \n" +
                            "        matrix.clear();\n" +
                            "        matrix.drawBitmap(fila, col, smile_bmp, ancho, alto, LED_ON);\n" +
                            "        matrix.writeDisplay(); ");
                    FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(150,100);
                    layoutparams.setMarginStart(440);
                    layoutparams.setMargins(0, 1025, 0, 0);
                    video.setLayoutParams(layoutparams);

                    video.setVisibility(View.VISIBLE);
                    video.setVideoPath("android.resource://" + getPackageName() + "/" + "/raw/"+R.raw.hola);
                    video.start();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            video.start();
                        }
                    }, 2000);

                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        public void run() {
                            video.setVisibility(View.GONE);
                            textAmigo.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
                            textAmigo.setText("Has seleccionado la matriz de 8x8 de LEDs que permite mostrar diversas figuras o letras");
                        }
                    }, 4000);



                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textplantilla.setVisibility(View.GONE);
                textAmigo.setText("Pulsa los elementos para conocerlos");
                datos="";
            }
        });

        final Bitmap bitmap = ((BitmapDrawable)plantilla.getDrawable()).getBitmap();
        plantilla.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y =  event.getY();
                float width = bitmap.getWidth();
                float heigth = bitmap.getHeight();
                float vertical = y/heigth;
                float horizontal = x/width;

                if(vertical>0.45 & vertical<0.8 & horizontal>0.15 & horizontal < 0.35){
                    textplantilla.setVisibility(View.VISIBLE);
                    textplantilla.setX((float)0.05*width+105);
                    //tener en cuenta los margenes a los que esta puesta la imagen
                    textplantilla.setY((float)0.42*heigth+550);
                    textplantilla.setBackgroundColor(Color.BLACK);
                    textplantilla.setText("Botones led");
                    datos = "L";
                    amigo.setImageResource(R.drawable.imagen17);
                    textAmigo.setText("Estos son los botones LED, que al pulsarse se iluminan y te permiten llevar acabo acciones");
                }
                else if(vertical>0.03 & vertical<0.25 & horizontal>0.40 & horizontal < 0.9){
                    textplantilla.setVisibility(View.VISIBLE);
                    textplantilla.setX((float)0.85*width+105);
                    textplantilla.setY((float)0.03*heigth+550);
                    textplantilla.setBackgroundColor(Color.BLACK);
                    textplantilla.setText("Pantalla LCD");
                    datos = "P";
                    amigo.setImageResource(R.drawable.imagen18);
                    textAmigo.setText("Esta es la pantalla LCD, con ella puedes mostrar el texto que tu quieras");
                }
                else if(vertical>0.25 & vertical<0.70 & horizontal>0.39 & horizontal < 0.85){
                    textplantilla.setVisibility(View.VISIBLE);
                    textplantilla.setX((float)0.80*width+105);
                    textplantilla.setY((float)0.30*heigth+550);
                    textplantilla.setBackgroundColor(Color.BLACK);
                    textplantilla.setText("Placa");
                    datos = "C";
                    amigo.setImageResource(R.drawable.imagen19);
                    textAmigo.setText("En esta placa puedes colocar diferentes componentes");
                }
                else if(vertical>1.05 & vertical<1.2 & horizontal>0.2 & horizontal < 0.25){
                    textplantilla.setVisibility(View.VISIBLE);
                    textplantilla.setX((float)0.05*width+105);
                    //tener en cuenta los margenes a los que esta puesta la image
                    textplantilla.setY((float)1.1*heigth+550);
                    textplantilla.setBackgroundColor(Color.BLACK);
                    textplantilla.setText("Buzzer");
                    datos = "B";
                    amigo.setImageResource(R.drawable.imagen17);
                    textAmigo.setText("Te presento al zumbador que permite sacar un sonido a una determinada frecuencia");
                }
                else if(vertical>0.55 & vertical<0.85 & horizontal>0.90 & horizontal < 1.2){
                    textplantilla.setVisibility(View.VISIBLE);
                    textplantilla.setX((float)0.9*width+105);
                    //tener en cuenta los margenes a los que esta puesta la image
                    textplantilla.setY((float)0.45*heigth+550);
                    textplantilla.setBackgroundColor(Color.BLACK);
                    textplantilla.setText("Servo");
                    datos = "S";
                    amigo.setImageResource(R.drawable.imagen18);
                    textAmigo.setText("Esto es un servo, se trata de un motor que gira hasta una posición según el ángulo indicado");
                }
                else if(vertical>0.8 & vertical<1.1 & horizontal>0.1 & horizontal < 0.4){
                    textplantilla.setVisibility(View.VISIBLE);
                    textplantilla.setX((float)0.4*width+105);
                    //tener en cuenta los margenes a los que esta puesta la image
                    textplantilla.setY((float)1.0*heigth+550);
                    textplantilla.setBackgroundColor(Color.BLACK);
                    textplantilla.setText("Matriz Leds");
                    datos = "M";
                    amigo.setImageResource(R.drawable.imagen19);
                    textAmigo.setText("Has seleccionado la matriz de 8x8 de LEDs que permite mostrar diversas figuras o letras");
                }

                return false;
            }
        });
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        address = intent.getStringExtra(DispositivosVinculados.EXTRA_DEVICE_ADDRESS);
        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establece la conexión con el socket Bluetooth.
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                btSocket.connect();
                //Toast.makeText(getBaseContext(), "CONEXION EXITOSA", Toast.LENGTH_SHORT).show();

                //return;
            }

            //btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
            }
        }
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        try { // Cuando se sale de la aplicación esta parte permite que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e2) {
        }
    }

    //Comprueba que el dispositivo Bluetooth
    //está disponible y solicita que se active si está desactivado
    private void VerificarEstadoBT() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    startActivityForResult(enableBtIntent, 1);
                    //return;
                }

            }
        }
    }

    //Crea la clase que permite crear el evento de conexion
    private class ConnectedThread extends Thread
    {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] byte_in = new byte[1];
            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    mmInStream.read(byte_in);
                    char ch = (char) byte_in[0];
                    bluetoothIn.obtainMessage(handlerState, ch).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //Envio de trama
        public void write(String input)
        {
            try {
                mmOutStream.write(input.getBytes());
            }
            catch (IOException e)
            {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private byte[] toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        return nv21;

        /*YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inMutable=true;
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, dbo);*/


    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        //cameraProvider.unbindAll();

        CameraSelector cameraSelector =  new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        // enable the following line if RGBA output is needed.
                        //.setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        //Executor executor;
        imageAnalysis.setAnalyzer(AsyncTask.THREAD_POOL_EXECUTOR, new ImageAnalysis.Analyzer() {

            @SuppressLint("UnsafeOptInUsageError")
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {

                @androidx.camera.core.ExperimentalGetImage Image  frame = imageProxy.getImage();
                @androidx.camera.core.ExperimentalGetImage byte[] bitmap=toBitmap(frame);


                int width= frame.getHeight();
                int height=frame.getWidth();
                int numPixels=width*height;

                boolean[] detected = new boolean[]{false, false, false, false, false};

                boolean[] detected1 = new boolean[]{false, false, false, false, false};
                boolean[] detected2 = new boolean[]{false, false, false, false, false};
                boolean[] detected3 = new boolean[]{false, false, false, false, false};
                /*
                * esquina superior izquierda x>0 x< 20 y> 190 y<225
                * esquina superior derecha x>940 x<960 y >190 y <225
                * esquina inferior izquierda x>0 x< 20 y> 1240 y< 1280
                 * esquina inferior derecha x>940 x<960 y> 1240 y< 1280
                * */
                 inf_izq=0;
                 inf_drch=0;
                 sup_izq=0;
                 sup_drch=0;

                 for (int x=5; x<25; x=x+1){
                     for (int y=195; y<215; y=y+1){
                         //
                         float Y = (float) (bitmap[y*width + x] & 0xff);
                         int xby2 = x/2;
                         int yby2 = y/2;
                         // make this V for NV12/420SP
                         float U = (float)(bitmap[numPixels + 2*xby2 + yby2*width] & 0xff) - 128.0f;
                         // make this U for NV12/420SP
                         float V = (float)(bitmap[numPixels + 2*xby2 + 1 + yby2*width] & 0xff) - 128.0f;
                         if(Y>Y_inf & Y<Y_sup & U>U_inf & U<U_sup & V>V_inf & V<V_sup){
                             sup_izq = sup_izq+1;
                             detected[x%5]=true;
                         }
                         /*else{
                             detected[x%5]=false;
                         }

                         if(detected[0] & detected[1] & detected[2]  & detected[3] & detected[4]) {
                             sup_izq=true;
                             dot.setX(x+100);
                             dot.setY(y+350);

                         }*/
                     }
                 }
                for (int x=890; x<910; x=x+1){
                    for (int y=195; y<215; y=y+1){
                        //
                        float Y = (float) (bitmap[y*width + x] & 0xff);
                        int xby2 = x/2;
                        int yby2 = y/2;
                        // make this V for NV12/420SP
                        float U = (float)(bitmap[numPixels + 2*xby2 + yby2*width] & 0xff) - 128.0f;
                        // make this U for NV12/420SP
                        float V = (float)(bitmap[numPixels + 2*xby2 + 1 + yby2*width] & 0xff) - 128.0f;

                        if(Y>Y_inf & Y<Y_sup & U>U_inf & U<U_sup & V>V_inf & V<V_sup){
                            detected[x%5]=true;
                            sup_drch = sup_drch+1;
                        }
                       /* else{
                            detected[x%5]=false;
                        }

                        if(detected[0] & detected[1] & detected[2]  & detected[3] & detected[4]) {
                            sup_drch=true;
                            dot1.setX(x+100);
                            dot1.setY(y+350);
                        }*/
                    }
                }
                for (int x=5; x<25; x=x+1){
                    for (int y=840; y<860; y=y+1){
                        //dot2.setX(15);
                        //dot2.setY(850);
                        float Y = (float) (bitmap[y*width + x] & 0xff);
                        int xby2 = x/2;
                        int yby2 = y/2;
                        // make this V for NV12/420SP
                        float U = (float)(bitmap[numPixels + 2*xby2 + yby2*width] & 0xff) - 128.0f;
                        // make this U for NV12/420SP
                        float V = (float)(bitmap[numPixels + 2*xby2 + 1 + yby2*width] & 0xff) - 128.0f;

                        if(Y>Y_inf & Y<Y_sup & U>U_inf & U<U_sup & V>V_inf & V<V_sup){
                            detected[x%5]=true;
                            inf_izq = inf_izq+1;
                        }
                        /*else{
                            detected[x%5]=false;
                        }

                        if(detected[0] & detected[1] & detected[2]  & detected[3] & detected[4]) {
                            inf_izq=true;
                            dot2.setX(x+100);
                            dot2.setY(y+350);
                        }*/
                    }
                }
                for (int x=890; x<910; x=x+1){
                    for (int y=840; y<860; y=y+1){
                        //dot3.setX(900);
                        //dot3.setY(850);
                        float Y = (float) (bitmap[y*width + x] & 0xff);
                        int xby2 = x/2;
                        int yby2 = y/2;
                        // make this V for NV12/420SP
                        float U = (float)(bitmap[numPixels + 2*xby2 + yby2*width] & 0xff) - 128.0f;
                        // make this U for NV12/420SP
                        float V = (float)(bitmap[numPixels + 2*xby2 + 1 + yby2*width] & 0xff) - 128.0f;

                        if(Y>Y_inf & Y<Y_sup & U>U_inf & U<U_sup & V>V_inf & V<V_sup){
                            detected[x%5]=true;
                            inf_drch = inf_drch+1;
                        }/*
                        else{
                            detected[x%5]=false;
                        }

                        if(detected[0] & detected[1] & detected[2]  & detected[3] & detected[4]) {
                            inf_drch=true;
                            dot3.setX(x+100);
                            dot3.setY(y+350);
                        }*/
                    }
                }


                if(inf_drch > 20 & inf_izq > 20 & inf_drch > 20 & inf_izq >20){
                    //imageView.setX(j+250);
                    //imageView.setY(i+350);
                    //encontrado =true;
                    bye=0;
                    plantilla.post(new Runnable() {
                        public void run() {
                            plantilla.setVisibility(View.VISIBLE);
                            script.setVisibility(View.VISIBLE);
                            clear.setVisibility(View.VISIBLE);
                            coment.setVisibility(View.VISIBLE);
                            //textAmigo.setText("¿Qué necesitas que te explique?");
                        }
                    });
                }
                else{
                    /*dot.setX(0);
                    dot.setY(0);
                    dot1.setX(0);
                    dot1.setY(0);
                    dot2.setX(0);
                    dot2.setY(0);
                    dot3.setX(0);
                    dot3.setY(0);*/
                    if(bye==99) {

                        plantilla.post(new Runnable() {
                            public void run() {
                                plantilla.setVisibility(View.GONE);
                                script.setVisibility(View.GONE);
                                clear.setVisibility(View.GONE);
                                coment.setVisibility(View.GONE);
                                textplantilla.setVisibility(View.GONE);
                                textAmigo.setText("Vuelve a ajustar la plantilla, por favor");
                            }
                        });
                        bye=0;
                    }
                    else{
                        bye=bye+1;
                    }
                }


                // after done, release the ImageProxy object
                imageProxy.close();
            }
        });

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);

        //cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }



}