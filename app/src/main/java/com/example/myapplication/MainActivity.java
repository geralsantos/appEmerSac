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

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
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


    Handler handler = new Handler(); // declared before onCreate
    Runnable myRunnable = null;
    private LocationManager locationManager;
    private LocationListener listener;
    String latitude=null, longitude=null;
    private static  final int REQUEST_LOCATION=1;
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
                //Fragment fragment = null;

                if (menuItemAnterior == null ? true : (menuItemAnterior.getItemId() != menuItem.getItemId())) {
                    menuItemAnterior = menuItem;
                } else {
                    return false;
                }
                //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.nav_tareo_colab:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, new TareoTrabajadorFragment())
                                .addToBackStack(null)
                                .commit();
                        //fragment = new TareoTrabajadorFragment();
                        break;
                    case R.id.nav_geomap_montaje:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, new GeolocalizacionMontajeFragment())
                                .addToBackStack(null)
                                .commit();
                        //fragment = new GeolocalizacionMontajeFragment();
                        break;
                    case R.id.nav_actividades_registradas:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, new ActividadesRegistradasFragment())
                                .addToBackStack(null)
                                .commit();
                        //fragment = new ActividadesRegistradasFragment();
                        break;
                    case R.id.nav_tareo_actividades:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, new TareoActividadesFragment())
                                .addToBackStack(null)
                                .commit();
                        //fragment = new TareoActividadesFragment();

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
                /*if (fragment != null) {
                    ft.add(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }*/
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }

            ;
        });
        verifyGPSON();
    }

    private void verifyGPSON(){
        Log.d("verifyGPSON1","verifyGPSON");
        locationManager=(LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        //Check gps is enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps
            onGPS();
        }
        else
        {
            //GPS is already On then
            getLocation();
        }
    }

    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
            handler =  new Handler();
            handler.removeCallbacks(myRunnable);
            myRunnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "No se ha podido identificar su gps", Toast.LENGTH_LONG).show();
                    locationManager.removeUpdates(listener);
                }
            };
            handler.postDelayed(myRunnable,10000);
            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat=location.getLatitude();
                    double longi=location.getLongitude();
                    handler.removeCallbacks(myRunnable);
                    latitude=lat+"";
                    longitude=longi+"";
                    locationManager.removeUpdates(listener);
                }
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            };
            locationManager.requestLocationUpdates("gps", 2000, 0, listener);
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();
                handler.removeCallbacks(myRunnable);
                latitude=lat+"";
                longitude=longi+"";
                locationManager.removeUpdates(listener);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();
                handler.removeCallbacks(myRunnable);
                latitude=lat+"";
                longitude=longi+"";
                locationManager.removeUpdates(listener);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();
                handler.removeCallbacks(myRunnable);
                latitude=""+lat;
                longitude=""+longi;
                locationManager.removeUpdates(listener);
            }else{
                Toast.makeText(getApplicationContext(), "Verifique que su gps funcione correctamente", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void onGPS(){
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Habilitar GPS");
        builder.setMessage("Esta aplicación funciona con gps, favor de habilitarlo.").setCancelable(false).setPositiveButton("Habilitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
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

}
