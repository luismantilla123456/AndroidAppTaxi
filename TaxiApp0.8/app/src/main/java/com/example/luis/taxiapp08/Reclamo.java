package com.example.luis.taxiapp08;

import com.example.luis.taxiapp08.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
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


public class Reclamo extends Activity {

    EditText mensaje = null;
    EditText fecha   = null;
    Button  btn;
    String Estado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo);

        mensaje = (EditText) findViewById(R.id.editReclamoMensaje);
        fecha   = (EditText) findViewById(R.id.editReclamoFecha);
        btn = (Button) findViewById(R.id.buttonReclamoListo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                //if (sharedpreferences.getString(MainActivity.nombre, "Anonimo").equals("Anonimo")){
                String param [] = {mensaje.getText().toString(),
                                    fecha.getText().toString(),
                                    sharedpreferences.getString(MainActivity.idUsuario,"0")};
                Usuario log = new Usuario();
                log.execute(param);

            }
        });
    }

    public class Usuario extends AsyncTask<String, Void, String>{

        private String resp;
        HttpURLConnection coneccion=null;
        BufferedReader reader = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                //String dir = "http://localhost:8080/AppTaxi/rest/mvlReclamo.html?" +
                //        "reclamo="+params[0]+"&fecha="+params[1]+"&id="+params[2];

                String dir = "localhost:8080/AppTaxi/rest/mvlReclamo.html?reclamo=este&fecha=2015-07-15&id=8";

                URL url = new URL(dir);
                coneccion = (HttpURLConnection) url.openConnection();
                InputStream stream = coneccion.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();


                //resp = resp + dir;
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
            //Toast.makeText(getApplicationContext(), Estado, Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
