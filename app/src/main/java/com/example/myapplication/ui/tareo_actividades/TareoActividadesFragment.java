package com.example.myapplication.ui.tareo_actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Config.Config;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.model.clsTareoActividad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class TareoActividadesFragment extends Fragment {
    public static TareoActividadesFragment newInstance() {
        return new TareoActividadesFragment();
    }
    //ListView lvListado_tareo_actividades;
    ArrayList<clsTareoActividad> rowItems=null;
    clsTareoActividad item =null;
    SharedPreferences sharedPreferences = null;
    SharedPreferences preferences = null;
    SharedPreferences.Editor editor = null;
    Integer usuario_id = null;
    private String m_Text = "";
    //Button btn_regresar_tareo;
    private ApiService servicio = Config.getRetrofit().create(ApiService.class);
    private TareoActividadesViewModel tareoActividadesViewModel;
    ProgressBar loadingProgressBar=null;
    //ListadoTareoActividadAdapter cursorAdapter=null;
    AdapterDatos  cursorAdapter;
    Context contexto;
    View root;

    ArrayList<String> listDatos;
    RecyclerView lvListado_tareo_actividades;
    public void onResume(){
        super.onResume();
        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getString(R.string.menu_tareo_actividades));
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tareoActividadesViewModel = ViewModelProviders.of(this).get(TareoActividadesViewModel.class);
        root = inflater.inflate(R.layout.fragment_tareo_actividades2, container, false);
        contexto = root.getContext();

        preferences= getActivity().getSharedPreferences("prueba",getActivity().MODE_PRIVATE);
        editor = preferences.edit();
        sharedPreferences = getActivity().getSharedPreferences("datosusuario",getActivity().MODE_PRIVATE);
        usuario_id= sharedPreferences.getInt("usuario_id",10);
        this.initApp();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tareoActividadesViewModel = ViewModelProviders.of(this).get(TareoActividadesViewModel.class);


        /*btn_regresar_tareo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresarFragment();
            }
        });*/
    }
    public void initApp(){

        this.iniciarObjetos();
        this.listViewActividades();
        //btn_regresar_tareo=(Button) getActivity().findViewById(R.id.btn_regresar_tareo);
    }

    private void iniciarObjetos() {
        /*recycler  = (RecyclerView) root.findViewById(R.id.recyclerId);

        recycler.setLayoutManager(new LinearLayoutManager(contexto));
        listDatos= new ArrayList<String>();
        for (int i = 0;i<=50;i++){
            listDatos.add("Dato # "+i);
        }
        AdapterDatos  adapter = new AdapterDatos(listDatos);
        recycler.setAdapter(adapter);*/

        rowItems = new ArrayList<clsTareoActividad>();
        //lvListado_tareo_actividades =  root.findViewById(R.id.lvListado_tareo_actividades);
        lvListado_tareo_actividades  = (RecyclerView) root.findViewById(R.id.recyclerId);
        lvListado_tareo_actividades.setLayoutManager(new LinearLayoutManager(contexto));
        //cursorAdapter = new ListadoTareoActividadAdapter(contexto, rowItems);
        cursorAdapter = new AdapterDatos(rowItems);
        lvListado_tareo_actividades.setAdapter(cursorAdapter);
        //registerForContextMenu(lvListado_tareo_actividades);
    }

    public void listViewActividades(){
        rowItems.clear();
        Call<List<clsTareoActividad>> listadoActividades = servicio.getTareoActividades(usuario_id);
        listadoActividades.enqueue(new Callback<List<clsTareoActividad>>() {
            @Override
            public void onResponse(Call<List<clsTareoActividad>> call, Response<List<clsTareoActividad>> response) {
                if (response.isSuccessful()) {
                    for (clsTareoActividad p : response.body()) {
                        item = new clsTareoActividad(p.getId(),
                                p.getHora_inicio(),
                                p.getProyecto_nombre(),
                                p.getSolid_nombre(),p.getTarea_nombre(),p.getEmpleados());
                        rowItems.add(item);
                    }
                    // lvListado_tareo_actividades =  root.findViewById(R.id.lvListado_tareo_actividades);
                    // cursorAdapter = new ListadoTareoActividadAdapter(contexto, rowItems);
                    cursorAdapter.notifyDataSetChanged();
                    //cursorAdapter.notify();
                    saveArray("id");
                    saveArray("horainicio");
                    // registerForContextMenu(lvListado_tareo_actividades);
                }
            }

            @Override
            public void onFailure(Call<List<clsTareoActividad>> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
            }
        });
    }

    public void modifyViewActividades(int position){
        rowItems.clear();
        final int pos = position;
        Call<List<clsTareoActividad>> listadoActividades = servicio.getTareoActividades(usuario_id);
        listadoActividades.enqueue(new Callback<List<clsTareoActividad>>() {
            @Override
            public void onResponse(Call<List<clsTareoActividad>> call, Response<List<clsTareoActividad>> response) {
                if (response.isSuccessful()) {
                    for (clsTareoActividad p : response.body()) {
                        item = new clsTareoActividad(p.getId(),
                                p.getHora_inicio(),
                                p.getProyecto_nombre(),
                                p.getSolid_nombre(),p.getTarea_nombre(),p.getEmpleados());
                        rowItems.add(item);
                    }
                    // lvListado_tareo_actividades =  root.findViewById(R.id.lvListado_tareo_actividades);
                    // cursorAdapter = new ListadoTareoActividadAdapter(contexto, rowItems);
                    //lvListado_tareo_actividades.removeViewAt(pos);
                    cursorAdapter.notifyItemRemoved(pos);
                    cursorAdapter.notifyItemRangeChanged(pos, rowItems.size());
                    cursorAdapter.notifyDataSetChanged();
                    //cursorAdapter.notify();
                    saveArray("id");
                    saveArray("horainicio");
                    // registerForContextMenu(lvListado_tareo_actividades);
                }
            }

            @Override
            public void onFailure(Call<List<clsTareoActividad>> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
            }
        });
    }


    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        android.view.MenuInflater inflater = getActivity().getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        inflater.inflate(R.menu.tareo_actividad_opciones_listview, menu);
    }*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                //cursorAdapter.eliminar(item.getGroupId());
                modificar(item.getGroupId());
                return true;
            default:
                return super.onContextItemSelected((android.view.MenuItem) item);
        }
    }
    /*public class Peticion extends AsyncTask<Void, Void, ArrayList<clsTareoActividad>> {
        private ApiService servicio = Config.getRetrofit().create(ApiService.class);
        ArrayList<clsTareoActividad> rowItems = new ArrayList<clsTareoActividad>();
        clsTareoActividad item = null;
        @Override
        protected ArrayList<clsTareoActividad> doInBackground(Void... voids) {
            Call<List<clsTareoActividad>> listadoActividades = servicio.getTareoActividades(usuario_id);
            try {
                for (clsTareoActividad p : listadoActividades.execute().body()) {
                    item = new clsTareoActividad(p.getId(),
                            p.getHora_inicio(),
                            p.getProyecto_nombre(),
                            p.getSolid_nombre(),p.getTarea_nombre());
                    rowItems.add(item);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return rowItems;

        }
    }*/
    private void modificar(int positionArrayImagen){

                /*final Integer id = rowItems.get(positionArrayImagen).getId();
                final String hora_inicio = rowItems.get(positionArrayImagen).getHora_inicio();*/
                Integer id = Integer.valueOf(loadArray(positionArrayImagen,"id"));
                String hora_inicio =loadArray(positionArrayImagen,"horainicio");
                CustomDialogClass cdd=new CustomDialogClass(contexto,id,hora_inicio,usuario_id,new TareoActividadesFragment());
                final int pos = positionArrayImagen;
                cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.d("rowitems",rowItems.size()+"");
                        modifyViewActividades(pos);
                    }
                });
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
    }
    public boolean saveArray(String prefijo)
    {
        /* sKey is an array */
        editor.putInt("rowItems_size", rowItems.size());
        for(int i=0;i<rowItems.size();i++)
        {
            editor.remove((prefijo+"_") + i);
            if (prefijo=="id"){
                editor.putString((prefijo+"_") + i, String.valueOf(rowItems.get(i).getId()));
            }else{
                editor.putString((prefijo+"_") + i, rowItems.get(i).getHora_inicio());
            }
        }

        return editor.commit();
    }
    public String loadArray(Integer index,String prefijo)
    {
        String res = "";
        //rowItems.clear();
        int size = preferences.getInt("rowItems_size", 0);
        for(int i=0;i<size;i++)
        {
            if (index == i){
                res = preferences.getString((prefijo+"_") + i, null);
                break;
            }
        }
        return res;
    }
}