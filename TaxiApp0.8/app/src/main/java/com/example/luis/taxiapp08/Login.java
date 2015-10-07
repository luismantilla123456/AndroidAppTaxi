package com.example.luis.taxiapp08;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Login extends Activity {

    Button btn = null;
    Button nuevoU = null;
    Intent in;
    SharedPreferences sharedpreferences;
    EditText usuario, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (EditText) findViewById(R.id.editLoginUsuario);
        password = (EditText) findViewById(R.id.editLoginPasword);

        btn = (Button) findViewById(R.id.buttonLogin);
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String us = usuario.getText().toString();
                //String ps = password.getText().toString();
                String parametros[] = {usuario.getText().toString(),password.getText().toString()};
                Usuario log = new Usuario();
                log.execute(parametros);
                /*******************************
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(MainActivity.nombre,us);
                    editor.putString(MainActivity.pass, ps);
                    editor.commit();
                 *******/

                if (btn.getText().toString()=="SALIR"){
                    SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedpreferences.edit();
                    edit.clear();
                    edit.commit();
                    usuario.setText("");
                    password.setText("");
                    btn.setText("INICIAR");
                }
            }
        });

        //vista nuevo usuario
        nuevoU = (Button)findViewById(R.id.buttonLoginNuevoU);
        nuevoU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Login.this, NuevoUsuario.class);
                startActivity(in);
            }
        });
    }

    public class Usuario extends AsyncTask<String, Void, String> {

        HttpURLConnection coneccion=null;
        private String resp = "";
        BufferedReader reader = null;
        private String respuesta = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                String dir = "http://192.168.1.90:8080/AppTaxi/rest/mvlLoginUsuario?";
                dir = dir + "telefono="+ params[0]+"&";
                dir = dir + "password=" + params[1];
                URL url = new URL(dir);
                coneccion = (HttpURLConnection) url.openConnection();

                InputStream stream = coneccion.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line= "";

                while ((line = reader.readLine())!=null){
                    buffer.append(line);
                }

                resp ="{ \"Usuario\":["+ buffer.toString()+"]}";

                JSONObject objJson = new JSONObject(resp);
                //asignamos un array
                JSONArray jsonArray = objJson.optJSONArray("Usuario");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                //editor.putString(MainActivity.nombre,us);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    editor.putString(MainActivity.idUsuario, jsonObject.optString("id"));
                    editor.putString(MainActivity.nombre, jsonObject.optString("nombre"));
                    editor.putString(MainActivity.Email, jsonObject.optString("email"));
                    editor.putString(MainActivity.telefono, jsonObject.optString("telefono"));
                    editor.putString(MainActivity.direccion, jsonObject.optString("direccion"));
                    editor.putString(MainActivity.pass, jsonObject.optString("password"));
                    editor.putString(MainActivity.sexo, jsonObject.optString("sexo"));

                    //respuesta = jsonObject.optString("nombre").toString();
                }
                editor.commit();
                //JSONObject usuario = objJson.getJSONObject("1");

                //resp = coneccion.toString();
                //resp = resp + "aqui";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (coneccion != null){
                    coneccion.disconnect();
                }
            }
            return  resp;
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            //usuario.setText("hola "+respuesta);
            if (sharedpreferences.getString(MainActivity.nombre, "Anonimo").equals("Anonimo")==false){
                in = new Intent(Login.this,MainActivity.class);
                startActivity(in);
            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.getString(MainActivity.nombre, "Anonimo") != "Anonimo"){
            usuario.setText(sharedpreferences.getString(MainActivity.Email,""));
            password.setText(sharedpreferences.getString(MainActivity.pass,""));
            btn.setText("SALIR");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
