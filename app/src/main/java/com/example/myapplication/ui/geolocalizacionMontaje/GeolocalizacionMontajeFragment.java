package com.example.myapplication.ui.geolocalizacionMontaje;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Config.Config;
import com.example.myapplication.InputFilterMinMax;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.R;
import com.example.myapplication.data.model.clsEstadoProyecto;
import com.example.myapplication.data.model.clsIncidencia;
import com.example.myapplication.data.model.clsProyecto;
import com.example.myapplication.data.model.clsSuperVisionAdjuntos;
import com.example.myapplication.data.model.clsSupervision;
import com.example.myapplication.data.model.clsTarea;
import com.example.myapplication.ui.audio_recorder.AudioRecorderFragment;
import com.example.myapplication.ui.gallery.GalleryFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.example.myapplication.config;
import com.example.myapplication.ui.gallery.GalleryViewModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeolocalizacionMontajeFragment extends Fragment {

    config conf = new config();
    private GeolocalizacionMontajeViewModel mViewModel;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private MediaRecorder recorder = null;


    ImageButton btn_geo_montaje_camara;
    ImageButton btn_geo_montaje_video;
    ImageButton btn_geo_montaje_audio;

    Button btn_geo_montaje_verfotos;
    Button btn_geo_montaje_vervideos;
    Button btn_geo_montaje_veraudios;

    ImageView id_img;
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static String actionActive = "";
    static Intent intentData;
    Bitmap imageBitmap;

    Spinner spnPorcentajeAvance;
    Spinner spinner_geo_montaje_listado_proyectos;
    Spinner spinner_geo_montaje_listado_estados_proyecto;
    //Spinner spinner_geo_montaje_listado_incidencia_proyecto;
    EditText edittext_geo_montaje_descripcion;
    SeekBar seekBar_porcentaje_avance;
    Button btn_grabar_geo_map;
    Button btn_abrir_dialog_seleccione_incidencias;
    private ApiService servicio = Config.getRetrofit().create(ApiService.class);
    clsProyecto item =null;

    List<clsProyecto> rowProyectos=null;
    List<String> spnArrProyectos = null;
    String[] proyectos_array = null;
    Integer[] id_proyectos_array = null;

    List<clsEstadoProyecto> rowEstadosProyecto=null;
    String[] estados_proyectos_array = null;
    Integer[] id_estado_proyectos_array = null;

    List<clsIncidencia> rowIncidencias=null;
    String[] incidencias = null;
    Integer[] incidencias_id = null;
    Integer[] newincidencias_id = null;

    boolean[] incidenciasSelected = null;
    LocationManager locationManager;
    LocationListener locationListener;
    private static  final int REQUEST_LOCATION=1;
    String latitude="",longitude="";

    boolean isVideoSaving=false;
    boolean isFotoSaving=false;
    boolean isAudioSaving=false;
    TextView nombreusuario;
    SharedPreferences sharedPreferences = null;

    public void openDialog(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seleccione a los trabajadores");
// add a checkbox list

        builder.setMultiChoiceItems(incidencias, incidenciasSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                incidenciasSelected[which] = isChecked;
            }
        });
// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                Log.i("q","awd"+which);

                //   addItem();
            }
        });
        // builder.setNegativeButton("Cancelar", null);
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        /*dialogSelectPersona dialogSelPer = new dialogSelectPersona();
        dialogSelPer.show(getFragmentManager(),"example dialog");*/
    }
    public void guardar(){
        verifyGPSON();

        Boolean[] arr = {spinner_geo_montaje_listado_proyectos.getSelectedItemPosition()==0,
                spinner_geo_montaje_listado_estados_proyecto.getSelectedItemPosition()==0,
                edittext_geo_montaje_descripcion.getText().toString().matches(""),longitude=="",latitude=="",isVideoSaving,isFotoSaving};
        String[] arrmsj = {"Seleccione un proyecto","Seleccione un estado del proyecto",
                "Ingrese una descripción","Habilite el gps y vuelva a entrar a la aplicación",
                "El video se está guardando, por favor espere...","La foto se está guardando, por favor espere..."};
        Log.d("guardarxd",""+arr.length);
        Log.d("guardarxd",""+arr.length);
        for (int i = 0;i<arr.length;i++){
            Log.d("foreach",""+i);
            Log.d("condicion",""+arr[i]);
            if (arr[i]){
                Toast.makeText(getActivity(),arrmsj[i],Toast.LENGTH_SHORT).show();
                return;
            }

        }

        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0;i<incidenciasSelected.length;i++){
            if (incidenciasSelected[i]){
                values.add(incidencias_id[i]);
            }
        }
        newincidencias_id = values.toArray(new Integer[values.size()]);

        Integer proyecto_id = id_proyectos_array[useLoop(proyectos_array,spinner_geo_montaje_listado_proyectos.getSelectedItem().toString())];
        Integer estado_proyecto_id = id_estado_proyectos_array[useLoop(estados_proyectos_array,spinner_geo_montaje_listado_estados_proyecto.getSelectedItem().toString())];
        String descripcion = edittext_geo_montaje_descripcion.getText().toString();
        Integer porcentaje = spnPorcentajeAvance.getSelectedItemPosition()+1;

        String root = conf.getRutaArchivos();
        String carpeta="";
        carpeta = root + "Fotos/";
        File myDir = new File(carpeta);
        final File[] filesFotos= myDir.listFiles();

        carpeta = root + "Videos/";
        myDir = new File(carpeta);
        final File[] filesVideos= myDir.listFiles();

        carpeta = root + "Audios/";
        myDir = new File(carpeta);
        final File[] filesAudios= myDir.listFiles();
        ArrayList<String> filePaths = new ArrayList<>();
        final List<MultipartBody.Part> parts = new ArrayList<>();


 if (filesFotos !=null){
                        for (File f : filesFotos){
                            //archivo = pruebaFilebase64(f);
                           // nombre_adjunto = f.getName();
                            //superVisionSubirArchivos(nombre_adjunto,response.body().getId(),1,archivo);
                            parts.add(prepareFilePart("adjuntos[]",f.getAbsolutePath()));
                        }
                    }if (filesVideos !=null) {

                        for (File f : filesVideos) {
                            //archivo = pruebaFilebase64(f);
                            //nombre_adjunto = f.getName();
                            //superVisionSubirArchivos(nombre_adjunto,response.body().getId(),1,archivo);
                            parts.add(prepareFilePart("adjuntos[]",f.getAbsolutePath()));

                        }
                    }if (filesAudios !=null){

                        for (File f : filesAudios){
                            //archivo = pruebaFilebase64(f);
                            //nombre_adjunto = f.getName();
                            //superVisionSubirArchivos(nombre_adjunto,response.body().getId(),1,archivo);
                            parts.add(prepareFilePart("adjuntos[]",f.getAbsolutePath()));

                        }
                    }


     /*   MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("proyecto_id", String.valueOf(proyecto_id));
        builder.addFormDataPart("geolocalizacion_estado_id", String.valueOf(estado_proyecto_id));
        builder.addFormDataPart("descripcion", descripcion);
        builder.addFormDataPart("porcentaje", String.valueOf(porcentaje));
        builder.addFormDataPart("latitud", latitude);
        builder.addFormDataPart("logitud", longitude);
        builder.addFormDataPart("usuario_id", "1");
        for (int i = 0; i < newincidencias_id.length; i++) {
            builder.addFormDataPart("incidencias[]", String.valueOf(newincidencias_id[i]));
        }
        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            builder.addFormDataPart("adjuntos[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-file"), file));
        }


        MultipartBody requestBody = builder.build();*/
        Integer usuario_id= sharedPreferences.getInt("usuario_id",10);
        Call<clsSupervision> call = servicio.guardarSupervision(proyecto_id,estado_proyecto_id,descripcion,porcentaje,latitude,longitude
        ,usuario_id,newincidencias_id,parts);
        btn_grabar_geo_map.setText("Procesando...");
        btn_grabar_geo_map.setEnabled(false);
        call.enqueue(new Callback<clsSupervision>() {
            @Override
            public void onResponse(Call<clsSupervision> call, Response<clsSupervision> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(), "El montaje ha sido registrado con exito. " + response.message(), Toast.LENGTH_LONG).show();
                    if (filesFotos !=null){
                        for (File f : filesFotos){
                            borrarArchivoSubido(f.getAbsolutePath());
                        }
                    }if (filesVideos !=null) {
                        for (File f : filesVideos) {
                            borrarArchivoSubido(f.getAbsolutePath());
                        }
                    }if (filesAudios !=null){
                        for (File f : filesAudios){
                            borrarArchivoSubido(f.getAbsolutePath());
                        }
                    }
                    /*for (int i = 0; i < parts.size(); i++) {
                        superVisionSubirArchivos(parts.get(i),(response.body().getId()),1);
                    }*/
                    //String archivo="";
                    //String nombre_adjunto = "";

                    /*if (filesFotos !=null){
                        btn_grabar_geo_map.setText("SUBIENDO FOTOS...");
                        for (File f : filesFotos){
                            archivo = pruebaFilebase64(f);
                            nombre_adjunto = f.getName();
                            superVisionSubirArchivos(nombre_adjunto,response.body().getId(),1,archivo);
                        }
                    }if (filesVideos !=null) {
                        btn_grabar_geo_map.setText("SUBIENDO VIDEOS...");

                        for (File f : filesVideos) {
                            archivo = pruebaFilebase64(f);
                            nombre_adjunto = f.getName();
                            superVisionSubirArchivos(nombre_adjunto,response.body().getId(),1,archivo);
                        }
                    }/*if (filesAudios !=null){
                        btn_grabar_geo_map.setText("SUBIENDO AUDIOS...");

                        for (File f : filesAudios){
                            archivo = pruebaFilebase64(f);
                            nombre_adjunto = f.getName();
                            superVisionSubirArchivos(nombre_adjunto,response.body().getId(),1,archivo);
                        }
                    }*/

                }else{
                    Toast.makeText(getActivity(), "El servidor ha retornado un error. " + response.message(), Toast.LENGTH_LONG).show();
                }
                btn_grabar_geo_map.setText("GUARDAR ACTIVIDAD");
                btn_grabar_geo_map.setEnabled(true);
                resetValues();
            }
            @Override
            public void onFailure(Call<clsSupervision> call, Throwable t) {
                Toast.makeText(getActivity(), "Ha ocurrido un error. " + t.getMessage(), Toast.LENGTH_LONG).show();
                btn_grabar_geo_map.setText("GUARDAR ACTIVIDAD");
                btn_grabar_geo_map.setEnabled(true);
            }
        });
    }
    private void borrarArchivoSubido(String ruta_imagen){
        try {
            File fdelete = new File(ruta_imagen);
            if (fdelete.exists()) {
                if (fdelete.delete()){

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /*private String pruebaFilebase64(File file){
        InputStream inputStream = null;//You can get an inputStream using any IO API
        ByteArrayOutputStream output=null;
        try {

            inputStream = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[8192];
            int bytesRead;
             output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output64.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            output64.close();

        }catch (Exception ex){
            Log.d("ERROR PRUEBA",ex.getMessage());
        }
        return output.toString();


    }*/
    private void superVisionSubirArchivos(String nombre_adjunto,Integer supervision_id, Integer usuario_id,String videobuffer){
       /*Call<clsSuperVisionAdjuntos> call = servicio.pruebaAdjuntoVideo(nombre_adjunto,supervision_id,usuario_id,videobuffer);
        call.enqueue(new Callback<clsSuperVisionAdjuntos>() {
            @Override
            public void onResponse(Call<clsSuperVisionAdjuntos> call, Response<clsSuperVisionAdjuntos> response) {
                Log.d("onResponse",""+response);

                if (response.isSuccessful()){
                    Log.d("RESPONSE","ITS OK");
                }else{
                    Log.d("onResponse ELSE","else error");

                }
            }
            @Override
            public void onFailure(Call<clsSuperVisionAdjuntos> call, Throwable t) {
                Log.d("ERROR REPONSE ADJUNTO","FAILED: "+t.getMessage());
            }
        });*/
/*
 Call<clsSuperVisionAdjuntos> call = servicio.guardarSupervisionAdjuntos(supervision_id,usuario_id,parts);
        call.enqueue(new Callback<clsSuperVisionAdjuntos>() {
            @Override
            public void onResponse(Call<clsSuperVisionAdjuntos> call, Response<clsSuperVisionAdjuntos> response) {
                Log.d("SUCCESS",""+response);
                Log.e("SUCCESS2",response.body()+"");
                Log.d("SUCCESS3",(""+(response.body().getId())));
                Log.d("SUCCESS3",( new Gson().toJson(response.body())));
                if (response.isSuccessful()){
                    Log.d("RESPONSE","ITS OK");

                }
            }
            @Override
            public void onFailure(Call<clsSuperVisionAdjuntos> call, Throwable t) {
                Log.d("ERROR REPONSE ADJUNTO","FAILED: "+t.getMessage());
            }
        });
*/

    }
    private MultipartBody.Part prepareFilePart(String partName, String url){

        File file = new File(url);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(partName, file.getName(),requestBody);
    }
    private void verifyGPSON(){
        locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Check gps is enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps
            OnGPS();
        }
        else
        {
            //GPS is already On then
            getLocation();
        }
    }
    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            //latitude="";
            //longitude="";
            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=lat+"";
                longitude=longi+"";
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=lat+"";
                longitude=longi+"";
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=""+lat;
                longitude=""+longi;
            }
            else
            {
                Toast.makeText(getActivity(), "Verifique que su gps funcione correctamente", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        builder.setMessage("Habilitar GPS").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private void llamarIntentAudio() {
        AudioRecorderFragment audioRecorderF = new AudioRecorderFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout_geomap, audioRecorderF);
        /*switch (actionActive){
            case "android.media.action.IMAGE_CAPTURE":
                audioRecorderF.mostrarTipoArchivo="mostrarFotos";
                break;
            case "android.media.action.VIDEO_CAPTURE":
                audioRecorderF.mostrarTipoArchivo="mostrarVideos";
                break;
        }*/
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static GeolocalizacionMontajeFragment newInstance() {
        return new GeolocalizacionMontajeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.geolocalizacion_montaje_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GeolocalizacionMontajeViewModel.class);
        // TODO: Use the ViewModel
        verifyStoragePermissions(getActivity());
        requestPermissionGPS(getActivity());

        this.listadoProyectos();
        this.listadoEstadosProyecto();
        this.listadoIncidenciasProyecto();
        this.listadoPorcentajes();
        sharedPreferences = getActivity().getSharedPreferences("datosusuario",getActivity().MODE_PRIVATE);
        //Log.d();
        btn_abrir_dialog_seleccione_incidencias = (Button) getActivity().findViewById(R.id.btn_abrir_dialog_seleccione_incidencias);
        btn_abrir_dialog_seleccione_incidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("q", "asd1");
                openDialog();
            }
        });
        id_img = (ImageView) getActivity().findViewById(R.id.id_img);
        edittext_geo_montaje_descripcion = (EditText) getActivity().findViewById(R.id.edittext_geo_montaje_descripcion);

        btn_geo_montaje_camara = (ImageButton) getActivity().findViewById(R.id.btn_geo_montaje_camara);
        btn_geo_montaje_verfotos=(Button) getActivity().findViewById(R.id.btn_geo_montaje_verfotos);
        btn_geo_montaje_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()){
                    llamarIntentCamara();
                }
            }
        });
        btn_geo_montaje_verfotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()){
                    actionActive = "android.media.action.IMAGE_CAPTURE";
                    llamarIntentVerArchivos();
                }
            }
        });
        btn_geo_montaje_video = (ImageButton) getActivity().findViewById(R.id.btn_geo_montaje_video);
        btn_geo_montaje_vervideos = (Button) getActivity().findViewById(R.id.btn_geo_montaje_vervideos);
        btn_geo_montaje_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()){
                    llamarIntentVideo();
                }
            }
        });
        btn_geo_montaje_vervideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()){
                    actionActive = "android.media.action.VIDEO_CAPTURE";
                    llamarIntentVerArchivos();
                }
            }
        });
        btn_geo_montaje_audio = (ImageButton) getActivity().findViewById(R.id.btn_geo_montaje_audio);
        btn_geo_montaje_veraudios= (Button) getActivity().findViewById(R.id.btn_geo_montaje_veraudios);
        btn_geo_montaje_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()){
                    actionActive = "android.media.action.IMAGE_CAPTURE";
                    llamarIntentAudio();
                }
            }
        });
        btn_geo_montaje_veraudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()){
                    actionActive = "android.media.action.AUDIO_CAPTURE";
                    llamarIntentVerArchivos();
                }
            }
        });

        btn_grabar_geo_map = (Button) getActivity().findViewById(R.id.btn_grabar_geo_map);
        btn_grabar_geo_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });



    }
    public void requestPermissionGPS(Activity activity){
        if (ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }

    }
    public void listadoPorcentajes(){
        List<Integer> spinnerArray =  new ArrayList<Integer>();
        for (int i = 1; i <= 100; i++) {
            spinnerArray.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>( getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        spnPorcentajeAvance = (Spinner) getActivity().findViewById(R.id.spnPorcentajeAvance);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPorcentajeAvance.setAdapter(adapter);
    }
    public void listadoEstadosProyecto(){
        Call<List<clsEstadoProyecto>> proyectos = servicio.getEstadosProyecto();
        proyectos.enqueue(new Callback<List<clsEstadoProyecto>>() {
              @Override
              public void onResponse(Call<List<clsEstadoProyecto>> call, Response<List<clsEstadoProyecto>> response) {
                  if (response.isSuccessful()) {
                      List<String> spinnerArray = new ArrayList<String>();
                      estados_proyectos_array = new String[response.body().size()];
                      id_estado_proyectos_array = new Integer[response.body().size()];
                      rowEstadosProyecto = new ArrayList<clsEstadoProyecto>();
                      spinnerArray.add("SELECCIONE");
                      Integer i = 0;
                      for (clsEstadoProyecto p : response.body()) {
                          spinnerArray.add(p.getNombre());
                          estados_proyectos_array[i] = (i+1)+". "+p.getNombre();
                          id_estado_proyectos_array[i] = p.getId();
                          rowEstadosProyecto.add(p);
                          i++;
                      }
                      ArrayAdapter<String> adapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

                      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      spinner_geo_montaje_listado_estados_proyecto = (Spinner) getView().findViewById(R.id.spinner_geo_montaje_listado_estados_proyecto);
                      spinner_geo_montaje_listado_estados_proyecto.setAdapter(adapter);
                  }
              }

              @Override
              public void onFailure(Call<List<clsEstadoProyecto>> call, Throwable t) {
                  Log.d("ERROR ESTADO PROYECTO",t.getMessage());
              }
        });

    }
    public void listadoProyectos(){
        Call<List<clsProyecto>> proyectos = servicio.getProyectos();
        proyectos.enqueue(new Callback<List<clsProyecto>>() {
            @Override
            public void onResponse(Call<List<clsProyecto>> call, Response<List<clsProyecto>> response) {
                if (response.isSuccessful()){
                    proyectos_array = new String[response.body().size()];
                    id_proyectos_array = new Integer[response.body().size()];
                    spnArrProyectos =  new ArrayList<String>();
                    rowProyectos = new ArrayList<clsProyecto>();
                    spnArrProyectos.add("SELECCIONE");
                    Integer i = 0;
                    for (clsProyecto p : response.body()) {
                        spnArrProyectos.add((i+1)+". "+p.getNombre());
                        proyectos_array[i] = (i+1)+". "+p.getNombre();
                        id_proyectos_array[i] = p.getId();
                        rowProyectos.add(p);
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_spinner_item, spnArrProyectos);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_geo_montaje_listado_proyectos = (Spinner) getView().findViewById(R.id.spinner_geo_montaje_listado_proyectos);
                    spinner_geo_montaje_listado_proyectos.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<clsProyecto>> call, Throwable t) {
                Log.d("ERROR PROYECTO",t.getMessage());
            }
        });

    }
    public static Integer useLoop(String[] arr, String targetValue) {
        Integer i = 0;
        for(String s: arr){
            if(s.equals(targetValue))
                return i;

            i++;

        }
        return 0;
    }
    public void listadoIncidenciasProyecto(){
        Call<List<clsIncidencia>> incidenciasProyecto = servicio.getIncidenciasProyecto();
        incidenciasProyecto.enqueue(new Callback<List<clsIncidencia>>() {
            @Override
            public void onResponse(Call<List<clsIncidencia>> call, Response<List<clsIncidencia>> response) {
                if (response.isSuccessful()){
                    incidencias = new String[response.body().size()];
                    incidencias_id = new Integer[response.body().size()];
                    incidenciasSelected = new boolean[response.body().size()];
                    rowIncidencias = new ArrayList<clsIncidencia>();
                    Integer i = 0;
                    for (clsIncidencia p : response.body()) {
                        incidencias[i] = p.getNombre();
                        incidencias_id[i] = p.getId();
                        incidenciasSelected[i] = false;
                        rowIncidencias.add(p);
                        i++;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<clsIncidencia>> call, Throwable t) {
                Log.d("ERROR INCI. PROYECTO",t.getMessage());
            }
        });

    }
    public void llamarIntentVerArchivos( ){
        GalleryFragment galleryF = new GalleryFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout_geomap, galleryF);
        switch (actionActive) {
            case "android.media.action.IMAGE_CAPTURE":
                galleryF.mostrarTipoArchivo = "mostrarFotos";
                break;
            case "android.media.action.VIDEO_CAPTURE":
                galleryF.mostrarTipoArchivo = "mostrarVideos";
                break;
            case "android.media.action.AUDIO_CAPTURE":
                galleryF.mostrarTipoArchivo = "mostrarAudios";
                break;
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                this.verificarCarpetaCreada();
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    private void saveToInternalStorage(){
        String root = conf.getRutaArchivos();
        File myDir,file;
        String fname="";
        Calendar c = Calendar.getInstance(); SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String strDate = sdf.format(c.getTime());
        switch (actionActive){
            case "android.media.action.IMAGE_CAPTURE":
                isFotoSaving=true;

                myDir = new File(root + "Fotos/");
                myDir.mkdirs();
                fname = "Image_"+strDate+".png";
                file = new File (myDir, fname);
                if (file.exists ()) file.delete ();
                try {

                    FileOutputStream out = new FileOutputStream(file);
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    //ByteArrayOutputStream  out = new ByteArrayOutputStream ();
                    out.flush();
                    out.close();
                    imageBitmap = ImageUtils.getInstant().getCompressedBitmap(myDir+"/"+fname);

                    isFotoSaving=false;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "android.media.action.VIDEO_CAPTURE":
                isVideoSaving=true;

                myDir = new File(root + "Videos/");
                myDir.mkdirs();
                fname = "Video_"+strDate+".mp4";
                file = new File (myDir, fname);
                if (file.exists ()) file.delete ();
                try {
                    AssetFileDescriptor videoAsset = getActivity().getContentResolver().openAssetFileDescriptor(intentData.getData(), "r");
                    InputStream  in = videoAsset.createInputStream();
                    OutputStream out = new FileOutputStream(file);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    in.close();
                    out.close();
                    isVideoSaving=false;
                }catch (Exception e) {
                    e.printStackTrace();
                }


                break;


     }

    }
    public void llamarIntentVideo() {
        actionActive = MediaStore.ACTION_VIDEO_CAPTURE;
        Intent takeVideoIntent = new Intent(actionActive);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    public void llamarIntentCamara(){
        actionActive = MediaStore.ACTION_IMAGE_CAPTURE;
        Intent takePictureIntent = new Intent(actionActive);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (actionActive){
            case "android.media.action.IMAGE_CAPTURE":
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode!=0 ) {
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    verifyStoragePermissions(getActivity());
                   /* if (isStoragePermissionGranted()){
                        */intentData = data;
                        saveToInternalStorage();
                        llamarIntentVerArchivos();/*
                    }*/
                }
                break;
            case "android.media.action.VIDEO_CAPTURE":
                if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode !=0) {
                    Uri videoUri = data.getData();
                    // videoView.setVideoURI(videoUri);
                    verifyStoragePermissions(getActivity());
                    /*if (isStoragePermissionGranted()){
                        */intentData = data;
                        saveToInternalStorage();
                        llamarIntentVerArchivos();/*
                    }*/
                }
                break;
        }

    }
    public void verificarCarpetaCreada(){
        String root = conf.getRutaArchivos();
        File myDir = new File(root + "Fotos/");
        myDir.mkdirs();
        File myDir1 = new File(root + "Videos/");
        myDir1.mkdirs();
        File myDir2 = new File(root + "Audios/");
        myDir2.mkdirs();
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRecordAudio = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 2);
        }


    }
    public void resetValues(){
        edittext_geo_montaje_descripcion.setText("");
        spinner_geo_montaje_listado_proyectos.setSelection(0);
        spinner_geo_montaje_listado_estados_proyecto.setSelection(0);
        for (int i = 0;i<incidencias.length;i++){
            incidenciasSelected[i] = false;
        }
        spnPorcentajeAvance.setSelection(0);
    }
}
