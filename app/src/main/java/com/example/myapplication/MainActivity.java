package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.ui.geolocalizacionMontaje.GeolocalizacionMontajeFragment;
import com.example.myapplication.ui.tareoTrabajador.TareoTrabajadorFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView nombreusuario;
    /*FrameLayout framegeo;
    MenuItem menu1;*/
    MenuItem menu1;
    NavigationView navigationView;
    DrawerLayout drawer;

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
         drawer = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu1 = (MenuItem) findViewById(R.id.nav_salir);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_geomap_montaje, R.id.nav_tareo_colab, R.id.nav_audio_recorder,
                R.id.nav_actividades_registradas, R.id.nav_share, R.id.nav_salir)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_tareo_colab:
                        fragment = new TareoTrabajadorFragment();
                        break;
                    case R.id.nav_geomap_montaje:
                        fragment = new GeolocalizacionMontajeFragment();
                        break;
                    case R.id.nav_salir:
                        final AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("¿ Desea cerrar sesión ?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        final AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                        break;
                }
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
                drawer.closeDrawer(GravityCompat.START);

                return true;
            };
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        nombreusuario = (TextView) findViewById(R.id.txtUsuario);
        SharedPreferences pref = getSharedPreferences("datosusuario",MODE_PRIVATE);
        nombreusuario.setText(pref.getString("empleado_apellidos","")+" "+pref.getString("empleado_nombres",""));
        //menu1 = menu.findItem(R.id.nav_geomap_montaje);
        getMenuInflater().inflate(R.menu.main, menu);
        //DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_geomap_montaje);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       /* Log.d("SELEC",item.getItemId()+"");
        switch (item.getItemId()) {
            case R.id.nav_salir:
                Log.d("XD","SALIR");
                return true;

            //finish();
               // return true;
            default:
        }*/
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*@Override
    public void onBackPressed() {
        SharedPreferences pref = getSharedPreferences("datosusuario",MODE_PRIVATE);
        Log.d("MENU",""+menu1.setVisible(true));
        GeolocalizacionMontajeFragment test = (GeolocalizacionMontajeFragment) getSupportFragmentManager().findFragmentByTag("fragment_layout_geomap");
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_geomap_montaje);*/
        /*if (drawer.isDrawerOpen(GravityCompat.START)) {
            if(pref.getString("usuario","")==""){
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }*/
}
