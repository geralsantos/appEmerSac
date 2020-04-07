package com.example.myapplication.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.config;
import com.example.myapplication.data.model.galleryRowItemImagen;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    config conf = new config();
    private GalleryViewModel galleryViewModel;
    public String mostrarTipoArchivo="";
    ImageView id_img;
    Integer proyecto_id=null;
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };



    // Objetos.
    private ImagenAdapter cursorAdapter;
    private ListView listViewArchivos;
    List<galleryRowItemImagen> rowItems=null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        //verifyStoragePermissions(getActivity());
        Bundle args = getArguments();
        proyecto_id = args.getInt("proyecto_id", 0);
    Log.d("proyecto_id",proyecto_id+"awd");
        listViewArchivos = (ListView) getActivity().findViewById(R.id.listViewArchivos);
        this.recuperarListarArchivos();
        registerForContextMenu(listViewArchivos);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        android.view.MenuInflater inflater = getActivity().getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        inflater.inflate(R.menu.gallery_opciones_listview_images, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_contextual_eliminar_imagen:
                eliminarImagen((int)info.id);
                return true;
            default:
                return super.onContextItemSelected((android.view.MenuItem) item);
        }
    }
    private void eliminarImagen(int positionArrayImagen){
        // Objetos.
        final String ruta_imagen = rowItems.get(positionArrayImagen).getRuta_imagen();
        String nombre_imagen = rowItems.get(positionArrayImagen).getNombre();
        AlertDialog.Builder mensaje_dialogo = new AlertDialog.Builder(getActivity());

        // Variables.

        mensaje_dialogo.setTitle("Importante");
        mensaje_dialogo.setMessage("¿Está seguro de eliminar este archivo: "+nombre_imagen+" ?");
        mensaje_dialogo.setCancelable(false);
        mensaje_dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                try{
                    File fdelete = new File(ruta_imagen);
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            Toast.makeText(getActivity().getApplicationContext(), "Archivo eliminado.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "No se ha podido eliminar el archivo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    recuperarListarArchivos();

                }catch(Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), "Error al eliminar!!!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        mensaje_dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        mensaje_dialogo.show();
    }
    private void recuperarListarArchivos() {
        try{
            String root = conf.getRutaArchivos()+proyecto_id+"/";
            String carpeta="";
            Log.d("RUTAARCHIVOS ROOT",root);
            switch (mostrarTipoArchivo){
                case "mostrarFotos":
                    carpeta = root+"Fotos/" ;
                    break;
                case "mostrarVideos":
                    carpeta = root + "Videos/";
                    break;
                case "mostrarAudios":
                    carpeta = root + "Audios/";
                    break;
            }
            File myDir = new File(carpeta);
            File[] files= myDir.listFiles();
            if (files !=null){
                rowItems = new ArrayList<galleryRowItemImagen>();
                for (File f : files){
                    Log.d("GERAEL",f.getName());
                    galleryRowItemImagen item = new galleryRowItemImagen(f.getName(),f.getAbsolutePath());
                    rowItems.add(item);
                }
            }
            ImagenAdapter cursorAdapter = new ImagenAdapter(getActivity(), rowItems);
            listViewArchivos.setAdapter(cursorAdapter);
        }catch(Exception e){
            Log.d("Error", "El mensaje de error es: " + e.getMessage());
        }finally{
            // Se cierra la base de datos.
           // baseDatos.cerrar();
        }
    }

    /*public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }*/
}