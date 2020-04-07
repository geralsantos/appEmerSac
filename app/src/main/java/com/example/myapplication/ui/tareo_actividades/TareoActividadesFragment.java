package com.example.myapplication.ui.tareo_actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.Config.Config;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.model.clsTareoActividad;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TareoActividadesFragment extends Fragment {
    public static TareoActividadesFragment newInstance() {
        return new TareoActividadesFragment();
    }
    ListView lvListado_tareo_actividades;
    ArrayList<clsTareoActividad> rowItems=null;
    clsTareoActividad item =null;
    SharedPreferences sharedPreferences = null;
    Integer usuario_id = null;
    private String m_Text = "";
    //Button btn_regresar_tareo;
    private ApiService servicio = Config.getRetrofit().create(ApiService.class);
    private TareoActividadesViewModel tareoActividadesViewModel;
    ProgressBar loadingProgressBar=null;
    ListadoTareoActividadAdapter cursorAdapter=null;
    Context contexto;
    View root;
    public void onResume(){
        super.onResume();
        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getString(R.string.menu_tareo_actividades));
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tareoActividadesViewModel = ViewModelProviders.of(this).get(TareoActividadesViewModel.class);
        root = inflater.inflate(R.layout.fragment_tareo_actividades, container, false);
        contexto = root.getContext();

        cursorAdapter=null;
        rowItems = new ArrayList<clsTareoActividad>();

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


        this.listViewActividades();
        //btn_regresar_tareo=(Button) getActivity().findViewById(R.id.btn_regresar_tareo);
    }
    public boolean listViewActividades(){
        rowItems = new ArrayList<clsTareoActividad>();
        Call<List<clsTareoActividad>> listadoActividades = servicio.getTareoActividades();
        listadoActividades.enqueue(new Callback<List<clsTareoActividad>>() {
            @Override
            public void onResponse(Call<List<clsTareoActividad>> call, Response<List<clsTareoActividad>> response) {
                if (response.isSuccessful()) {
                    for (clsTareoActividad p : response.body()) {
                        item = new clsTareoActividad(p.getId(),
                                p.getHora_inicio(),
                                p.getProyecto_nombre(),
                                p.getSolid_nombre(),p.getTarea_nombre());
                        rowItems.add(item);
                    }
                    lvListado_tareo_actividades =  root.findViewById(R.id.lvListado_tareo_actividades);
                    cursorAdapter = new ListadoTareoActividadAdapter(contexto, rowItems);
                    lvListado_tareo_actividades.setAdapter(cursorAdapter);
                    registerForContextMenu(lvListado_tareo_actividades);

                    //cursorAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<clsTareoActividad>> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
            }
        });
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
            super.onCreateContextMenu(menu, v, menuInfo);
            android.view.MenuInflater inflater = getActivity().getMenuInflater();
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            inflater.inflate(R.menu.tareo_actividad_opciones_listview, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.menu_contextual_editar_registro:
                    //eliminarImagen((int)info.id);
                    modificar((int)info.id);
                    return true;
                default:
                    return super.onContextItemSelected((android.view.MenuItem) item);
            }
    }
    private void modificar(int positionArrayImagen){
        final Integer id = rowItems.get(positionArrayImagen).getId();
        final String hora_inicio = rowItems.get(positionArrayImagen).getHora_inicio();

        CustomDialogClass cdd=new CustomDialogClass(contexto,id,hora_inicio,usuario_id,new TareoActividadesFragment());
        cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                listViewActividades();
            }
        });
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();



        //String nombre_imagen = rowItems.get(positionArrayImagen).getNombre();
/*
 final View view = getLayoutInflater().inflate(R.layout.tareo_dialog_editar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Modificar la hora fin"+id);

// Set up the input
        final EditText input = (EditText) view.findViewById(R.id.etComments);
        //final EditText input = createEditText();
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        if(view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view); // <- fix
        }
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();*/
    }
}