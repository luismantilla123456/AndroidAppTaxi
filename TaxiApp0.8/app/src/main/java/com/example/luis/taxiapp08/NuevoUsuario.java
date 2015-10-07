package com.example.luis.taxiapp08;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class NuevoUsuario extends AppCompatActivity {

    EditText nombre = null;
    EditText email = null;
    EditText telefono = null;
    EditText direccion = null;
    EditText pass = null;
    Button  sexo = null;
    Button  btn     = null;

    String  Estado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        nombre   = (EditText) findViewById(R.id.textNuevoUsuarioNombre);
        email    = (EditText) findViewById(R.id.textNuevoUsuarioEmail);
        telefono = (EditText) findViewById(R.id.textNuevoUsuarioTelefono);
        direccion= (EditText) findViewById(R.id.textNuevoUsuarioDireccion);
        pass     = (EditText) findViewById(R.id.textNuevoUsuarioPassword);
        sexo     = (Button) findViewById(R.id.buttonNuevoUsuarioSexo);
        btn      = (Button)   findViewById(R.id.btnLogin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parametros[] = {
                        nombre.getText().toString(),
                        email.getText().toString(),
                        telefono.getText().toString(),
                        direccion.getText().toString(),
                        pass.getText().toString(),
                        sexo.getText().toString()
                };
                Usuario log = new Usuario();
                log.execute(parametros);

            }
        });

        sexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("HOMBRE".equals(sexo.getText())){
                    sexo.setText("MUJER");
                }else{
                    sexo.setText("HOMBRE");
                }
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
                String dir = "http://192.168.1.90:8080/AppTaxi/rest/registrarUsuario.html?" +
                        "nombre="+params[0]+"&enmail="+params[1]+"&" +
                        "telefono="+params[2]+"&direccion="+params[3]+"&" +
                        "password="+params[4];
                if (params[5] == "HOMBRE"){
                    dir = dir + "&sexo=1";
                }else{
                    dir = dir + "&sexo=0";
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nuevo_usuario, menu);
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
