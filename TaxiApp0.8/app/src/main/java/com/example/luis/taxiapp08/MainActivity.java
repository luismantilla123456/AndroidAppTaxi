package com.example.luis.taxiapp08;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import com.example.luis.taxiapp08.Login;

public class MainActivity extends Activity {

    Button Login = null;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String idUsuario = "usuarioId";
    public static final String nombre    = "usuarioNombre";
    public static final String Email     = "usuarioEmail";
    public static final String telefono  = "usuarioTelefono";
    public static final String pass      = "usuarioPassword";
    public static final String direccion = "usuarioDireccion";
    public static final String sexo      = "sexo";



    Button pedir     = null;
    Button reclamo   = null;
    Button cuentanos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login = (Button) findViewById(R.id.buttonInicioSession);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Login.class);
                startActivity(in);
            }
        });

        pedir = (Button) findViewById(R.id.buttonMainPedirTaxi);
        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,PedirTaxi.class);
                startActivity(in);
            }
        });

        reclamo = (Button)findViewById(R.id.buttonMainReclamo);
        reclamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,Reclamo.class);
                startActivity(in);
            }
        });

        cuentanos = (Button)findViewById(R.id.buttonMainCuentanos);

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.getString(MainActivity.nombre, "Anonimo").equals("Anonimo")){
            Intent in = new Intent(MainActivity.this, Login.class);
            startActivity(in);
        }else{
            this.Login.setText(sharedpreferences.getString(MainActivity.nombre,"Anonimo"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
