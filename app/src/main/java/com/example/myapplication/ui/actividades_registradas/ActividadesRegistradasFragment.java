package com.example.myapplication.ui.actividades_registradas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.Config.Config;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.model.clsActividad;
import com.example.myapplication.ui.tareoTrabajador.ListadoActividadAdapter;
import com.example.myapplication.ui.tareo_actividades.ListadoTareoActividadAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadesRegistradasFragment extends Fragment {

    private ActividadesRegistradasViewModel toolsViewModel;
    ListView lvListado_trabajadores;
    List<clsActividad> rowItems=null;
    clsActividad item =null;
    SharedPreferences sharedPreferences = null;
    Integer usuario_id = null;
    //Button btn_regresar_tareo;
    private ApiService servicio = Config.getRetrofit().create(ApiService.class);
    Context contexto;
    ListadoActividadAdapter cursorAdapter=null;
    public void onResume(){
        super.onResume();
        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getString(R.string.menu_geomap_actividades_registradas));
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel = ViewModelProviders.of(this).get(ActividadesRegistradasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_actividades_registradas, container, false);
        sharedPreferences = getActivity().getSharedPreferences("datosusuario",getActivity().MODE_PRIVATE);
        usuario_id= sharedPreferences.getInt("usuario_id",10);
        contexto = root.getContext();
        lvListado_trabajadores = root.findViewById(R.id.lvListado_actividades_registradas);
        cursorAdapter = null;
        rowItems = new ArrayList<clsActividad>();

        this.initApp();
        this.listViewActividades();
       // final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        /*btn_regresar_tareo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresarFragment();
            }
        });*/
    }

    public void initApp(){
        //btn_regresar_tareo=(Button) getActivity().findViewById(R.id.btn_regresar_tareo);
    }
    public void listViewActividades(){
        Log.i("q", "listViewActividades");
        Call<List<clsActividad>> listadoActividades = servicio.supervision_actividades(usuario_id);
        Log.i("usuario_id", usuario_id+"");

        listadoActividades.enqueue(new Callback<List<clsActividad>>() {
            @Override
            public void onResponse(Call<List<clsActividad>> call, Response<List<clsActividad>> response) {

                if (response.isSuccessful()) {
                    Log.i("response.isSuccessful", response.body()+"");
                    for (clsActividad p : response.body()) {
                        item = new clsActividad(p.getId(),
                                p.getNombre_proyecto(),
                                p.getEstado_proyecto(),
                                p.getHito(),p.getCantidad_adjuntos());
                        rowItems.add(item);
                    }
                }
                cursorAdapter = new ListadoActividadAdapter(contexto, rowItems);
                lvListado_trabajadores.setAdapter(cursorAdapter);
            }

            @Override
            public void onFailure(Call<List<clsActividad>> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
            }
        });
    }

}