package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.Interface.myDbAdapter;
import com.example.myapplication.ui.actividades_registradas.ActividadesRegistradasFragment;
import com.example.myapplication.ui.geolocalizacionMontaje.GeolocalizacionMontajeFragment;
import com.example.myapplication.ui.tareoTrabajador.TareoTrabajadorFragment;
import com.example.myapplication.ui.tareo_actividades.TareoActividadesFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView nombreusuario;
    /*FrameLayout framegeo;
    MenuItem menu1;*/
    MenuItem menu1;
    NavigationView navigationView;
    DrawerLayout drawer;
    myDbAdapter helper;
    SharedPreferences pref = null;
    private AppBarConfiguration mAppBarConfiguration;
    MenuItem menuItemAnterior = null;
    Boolean x = false;

    private LocationManager locationManager;
    private LocationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helper = new myDbAdapter(this);

        pref = getSharedPreferences("datosusuario", MODE_PRIVATE);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu1 = (MenuItem) findViewById(R.id.nav_salir);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_geomap_montaje, R.id.nav_tareo_colab, R.id.nav_actividades_registradas,
                R.id.nav_tareo_actividades, R.id.nav_tareo_actividades, R.id.nav_salir)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                if (menuItemAnterior == null ? true : (menuItemAnterior.getItemId() != menuItem.getItemId())) {
                    menuItemAnterior = menuItem;
                } else {
                    // x=true;

                    return false;
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.nav_tareo_colab:
                        fragment = new TareoTrabajadorFragment();
                        break;
                    case R.id.nav_geomap_montaje:
                        fragment = new GeolocalizacionMontajeFragment();
                        break;
                    case R.id.nav_actividades_registradas:
                        fragment = new ActividadesRegistradasFragment();
                        break;
                    case R.id.nav_tareo_actividades:
                        fragment = new TareoActividadesFragment();

                        break;
                    case R.id.nav_salir:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("¿ Desea cerrar sesión ?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete(pref.getString("usuario", ""));
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                }
                if (fragment != null) {
                    ft.add(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }

            ;
        });

    }



    public void delete(String uname)
    {
        int a= helper.delete(uname);
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        nombreusuario = (TextView) findViewById(R.id.txtUsuario);

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
      //  return super.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null && fragments.size()>=2){
            Fragment nombreFragment = fragments.get(fragments.size()-2);
            if (nombreFragment instanceof GeolocalizacionMontajeFragment || nombreFragment instanceof NavHostFragment){
                this.setActionBarTitle(getString(R.string.menu_geolocalizacion_montaje));
                navigationView.getMenu().getItem(1).setChecked(true);
            }
            if (nombreFragment instanceof TareoTrabajadorFragment){
                this.setActionBarTitle(getString(R.string.menu_tareo_trabajador));
                navigationView.getMenu().getItem(0).setChecked(true);
            }
            if (nombreFragment instanceof ActividadesRegistradasFragment){
                this.setActionBarTitle(getString(R.string.menu_actividades_registradas));
                navigationView.getMenu().getItem(2).setChecked(true);
            }
            if (nombreFragment instanceof TareoActividadesFragment){
                this.setActionBarTitle(getString(R.string.menu_tareo_actividades));
                navigationView.getMenu().getItem(3).setChecked(true);
            }
        }else{
            Log.d("else",(fragments != null)+"-"+(fragments.size()>=2));
        }
        menuItemAnterior = null;
        super.onBackPressed();

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            if (status == LocationProvider.OUT_OF_SERVICE) {
            } else {
            }
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
