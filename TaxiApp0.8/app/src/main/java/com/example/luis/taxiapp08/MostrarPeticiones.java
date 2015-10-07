package com.example.luis.taxiapp08;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MostrarPeticiones extends AppCompatActivity {

    Button consumir = null;
    ListView lista = null;
    SimpleCursorAdapter adaptador = null;
    private String[] sistemas = {"Ubuntu", "Android", "iOS", "Windows", "Mac OSX",
            "Google Chrome OS", "Debian", "Mandriva", "Solaris", "Unix"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_peticiones);
        consumir = (Button) findViewById(R.id.buttonMostrarPet);

        lista = (ListView) findViewById(R.id.listView);


        //completarLista();
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sistemas);
        lista.setAdapter(adaptador);


        consumir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsumirDatos con = new ConsumirDatos();
                con.execute();
            }
        });
    }

    public void completarLista(){
        sistemas = new String[25];
        for (int i = 0; i <25; i++) {
            sistemas[i] = "->1"+String.valueOf(i);
        }
    }


    public class ConsumirDatos extends AsyncTask<String, Void, String> {

        private String resp;
        HttpURLConnection coneccion=null;
        BufferedReader reader = null;

        @Override
        protected String doInBackground(String... params) {

            try {
                //String dir = "http://192.168.1.90:8080/TaxiSpring/";
                URL url = new URL("http://192.168.1.90:8080/TaxiSpring/nuevoUsuarioGet.html?email=holaAqui@hotmil.com&pass=14141414");
                coneccion = (HttpURLConnection) url.openConnection();

                InputStream stream = coneccion.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));


                StringBuffer buffer = new StringBuffer();
                String line= "";


                while ((line = reader.readLine())!=null){
                    buffer.append(line);
                    //resp = resp + line.toString();
                }
                resp = coneccion.toString();

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
                }
            }

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {

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
        getMenuInflater().inflate(R.menu.menu_mostrar_peticiones, menu);
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
