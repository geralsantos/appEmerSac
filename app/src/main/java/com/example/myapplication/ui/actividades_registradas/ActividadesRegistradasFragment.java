package com.example.myapplication.ui.actividades_registradas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.data.model.clsActividad;
import com.example.myapplication.ui.tareoTrabajador.ListadoActividadAdapter;
import com.example.myapplication.ui.tareoTrabajador.TareoTrabajadorFragment;

import java.util.ArrayList;
import java.util.List;

public class ActividadesRegistradasFragment extends Fragment {

    private ActividadesRegistradasViewModel toolsViewModel;
    ListView lvListado_trabajadores;
    List<clsActividad> rowItems=null;
    clsActividad item =null;
    Button btn_regresar_tareo;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ActividadesRegistradasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_actividades_registradas, container, false);
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
        this.initApp();
        this.listViewActividades();
        btn_regresar_tareo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresarFragment();
            }
        });
    }

    public void initApp(){
        lvListado_trabajadores = (ListView) getActivity().findViewById(R.id.lvListado_actividades_registradas);
        btn_regresar_tareo=(Button) getActivity().findViewById(R.id.btn_regresar_tareo);
    }
    public void listViewActividades(){
        rowItems = new ArrayList<clsActividad>();
         item = new clsActividad(1,"GERAL POMA SANTOS","21:00","08:00","03:00","10:00","07:00","20:00");
        rowItems.add(item);
         item = new clsActividad(1,"LEON ESPINOZA, TOMAS ERNESTO","21:00","08:00","03:00","10:00","07:00","20:00");
        rowItems.add(item);
         item = new clsActividad(1,"TAPULLIMA TUANAMA, SOILANDA","21:00","08:00","03:00","10:00","07:00","20:00");
        rowItems.add(item);
         item = new clsActividad(1,"ANDRADE TITO, SUSANA","21:00","08:00","03:00","10:00","07:00","20:00");
        rowItems.add(item);
        item = new clsActividad(1,"ANDRADE TITO, SUSANA","21:00","08:00","03:00","10:00","07:00","20:00");
        rowItems.add(item);
        item = new clsActividad(1,"ANDRADE TITO, SUSANA","21:00","08:00","03:00","10:00","07:00","20:00");
        rowItems.add(item);
        item = new clsActividad(1,"ANDRADE TITO, SUSANA","21:00","08:00","03:00","10:00","07:00","20:00");
        rowItems.add(item);
        ListadoActividadAdapter cursorAdapter = new ListadoActividadAdapter(getActivity(), rowItems);
        lvListado_trabajadores.setAdapter(cursorAdapter);
    }
    public void regresarFragment(){
        Fragment fr = getFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (fr !=null){
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(fr);
            fragmentTransaction.commit();
        }
    }
}