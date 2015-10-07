package com.example.luis.taxiapp08;

import com.example.luis.taxiapp08.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class PedirTaxi extends Activity {

    String Estado = "";

    EditText numeroTaxis = null;
    EditText lugar       = null;
    EditText fecha       = null;
    EditText detalle     = null;

    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_taxi);
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        numeroTaxis = (EditText) findViewById(R.id.editPedirTaxiNumeroTaxis);
        lugar       = (EditText) findViewById(R.id.editPedirTaxiLugar);
        fecha       = (EditText) findViewById(R.id.editPedirTaxiFecha);
        //fecha.setText(String.valueOf(today.year)+"-"+String.valueOf(today.month)+"-"+String.valueOf(today.monthDay));
        fecha.setText(mydate);

        detalle     = (EditText) findViewById(R.id.editPedirTaxiDetalle);

        btn = (Button) findViewById(R.id.buttonPedirTaxiListo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                String param [] = {
                                    lugar.getText().toString(),
                                    numeroTaxis.getText().toString(),
                                    fecha.getText().toString(),
                                    detalle.getText().toString(),
                                    sharedpreferences.getString(MainActivity.idUsuario,"0")};
                Usuario log = new Usuario();
                log.execute(param);
            }
        });

    }

    public class Usuario extends AsyncTask<String, Void, String> {

        private String resp;
        HttpURLConnection coneccion=null;
        BufferedReader reader = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                String dir = "http://localhost:8080/AppTaxi/rest/mvlPedido.html?" +
                        "drccn="+params[0]+"&nUnidades="+params[1]+"&fecha="+params[2] +
                        "&anota="+params[3]+"&id="+params[4];

                URL url = new URL(dir);
                coneccion = (HttpURLConnection) url.openConnection();
                InputStream stream = coneccion.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line= "";
                while ((line = reader.readLine())!=null){
                    buffer.append(line);
                    //resp = resp + line.toString();
                }
                //resp = coneccion.toString();
                resp = resp + dir;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (coneccion != null){
                    coneccion.disconnect();
                    Estado = "Todo bien";
                }else{
                    Estado = "Algo fue mal";
                }
            }
            return  resp;
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            //nombre.setText(resp);
            Toast.makeText(getApplicationContext(), Estado, Toast.LENGTH_SHORT).show();
            //Intent in = new Intent(NuevoUsuario.this,MainActivity.class);
            //startActivity(in);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }



}
