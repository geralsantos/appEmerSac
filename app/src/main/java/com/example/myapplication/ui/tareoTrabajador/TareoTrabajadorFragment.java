package com.example.myapplication.ui.tareoTrabajador;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Config.Config;
import com.example.myapplication.InputFilterMinMax;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.R;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.myapplication.data.model.clsActividad;
import com.example.myapplication.ui.actividades_registradas.ActividadesRegistradasFragment;
import com.example.myapplication.data.model.clsProyecto;
import com.example.myapplication.data.model.clsTarea;
import com.example.myapplication.data.model.clsTrabajador;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TareoTrabajadorFragment extends Fragment {

    private TareoTrabajadorViewModel mViewModel;
    Button btnAbrirDialogSelectPerson;
    //Button btn_ver_actividades_registradas;
    Button btnGuardar;
    Spinner spinner_tareo_trabajador_listado_actividades;
    Spinner spinner_tareo_trabajador_listado_proyectos;

    EditText etHorainicio ,etMinInicio , etHoraFin ,etMinFin ;

    public static TareoTrabajadorFragment newInstance() {
        return new TareoTrabajadorFragment();
    }

    List<clsActividad> rowItems = new ArrayList<clsActividad>();

    private ApiService servicio = Config.getRetrofit().create(ApiService.class);
    clsProyecto item = null;
    List<clsProyecto> rowProyectos = null;
    String[] proyectos_array = null;
    Integer[] id_proyectos_array = null;
    String[] actividades_array = null;
    Integer[] id_actividades_array = null;

    List<clsTarea> rowTareasProyecto = null;
    List<clsTrabajador> rowTrabajadores = null;
    String[] personas = null;
    Integer[] personas_id = null;
    Integer[] newpersonas_id = null;
    boolean[] personasSelected = null;

    List<String> spnArrProyectos = null;
    SharedPreferences sharedPreferences = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.tareo_trabajador_fragment,container,false);

        return inflater.inflate(R.layout.tareo_trabajador_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TareoTrabajadorViewModel.class);
        // TODO: Use the ViewModel
        this.configinit();
        this.listadoProyectos();
        this.listadoActividades();
        this.listadoTrabajadores();
        sharedPreferences = getActivity().getSharedPreferences("datosusuario",getActivity().MODE_PRIVATE);
    }

    public void loadEventListener() {
        btnAbrirDialogSelectPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("q", "asd1");
                openDialog();
            }
        });
        /*btn_ver_actividades_registradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("q", "asd2");

                llamarIntentActividadesRegistradas();
            }
        });*/
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });
    }

    public void guardar(){
        Boolean[] arr = {spinner_tareo_trabajador_listado_proyectos.getSelectedItemPosition()==0,
                spinner_tareo_trabajador_listado_actividades.getSelectedItemPosition()==0,
                etHorainicio.getText().toString().matches(""),etMinInicio.getText().toString().matches(""),
                etHoraFin.getText().toString().matches(""),etMinFin.getText().toString().matches("")};
        String[] arrmsj = {"Seleccione un proyecto","Seleccione una actividad","Ingrese la hora y minuto de Inicio","Ingrese la hora y minuto de Inicio","Ingrese la hora y minuto de Fin","Ingrese la hora y minuto de Fin"};
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
        boolean continue_ = false;

        for (int i = 0;i<personasSelected.length;i++){
            if (personasSelected[i]){
                continue_ = true;
                break;
            }
        }
        if (!continue_){Toast.makeText(getActivity(),"Debe seleccionar uno o más trabajadores",Toast.LENGTH_SHORT).show();return;}

        btnGuardar.setText("Procesando...");
        btnGuardar.setEnabled(false);
        List<Integer> values = new ArrayList<Integer>();

        for (int i = 0;i<personasSelected.length;i++){
            if (personasSelected[i]){
                values.add(personas_id[i]);
            }
        }
        newpersonas_id = values.toArray(new Integer[values.size()]);

        Integer proyecto_id = id_proyectos_array[useLoop(proyectos_array,spinner_tareo_trabajador_listado_proyectos.getSelectedItem().toString())];
        Integer tarea_id = id_actividades_array[useLoop(actividades_array,spinner_tareo_trabajador_listado_actividades.getSelectedItem().toString())];
        Integer hora = Integer.parseInt(etHorainicio.getText().toString());
        Integer minuto = Integer.parseInt(etMinInicio.getText().toString());
        String hora_inicio = (hora<=9 ? "0"+hora:hora)+":"+(minuto<=9 ? "0"+minuto:minuto);
        Log.d("hora_inicio",hora_inicio);
        hora = Integer.parseInt(etHoraFin.getText().toString());
        minuto = Integer.parseInt(etMinFin.getText().toString());
        String hora_fin = (hora<=9 ? "0"+hora:hora)+":"+(minuto<=9 ? "0"+minuto:minuto);
        Log.d("hora_fin",hora_fin);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        ParsePosition pp1 = new ParsePosition(0);
        Date date1 = format.parse(hora_fin,pp1);
        Log.d("date1",date1+"");

        ParsePosition pp2 = new ParsePosition(0);
        Date date2 = format.parse(hora_inicio,pp2);
        Log.d("date2",date2+"");

        long mills = date1.getTime() - date2.getTime();
        if (mills<0){
            Toast.makeText(getActivity(),"La Hora Fin debe ser mayor a la Hora Inicio",Toast.LENGTH_SHORT).show();return;
        }
        int hours = (int)mills /(1000 * 60 * 60);
        int mins = ((int)mills/(1000*60)) % 60;
        String diff = hours + ":" + mins;

        double horas_trabajadas = ((double)mins/60)+hours;
        Integer usuario_id= sharedPreferences.getInt("usuario_id",10);
        Call<clsTrabajador> call = servicio.guardarTareo(
                proyecto_id,tarea_id,hora_inicio,hora_fin,horas_trabajadas,newpersonas_id,usuario_id
        );
        call.enqueue(new Callback<clsTrabajador>() {
            @Override
            public void onResponse(Call<clsTrabajador> call, Response<clsTrabajador> response) {
                Log.d("RESPONSE",""+response);
                if (response.isSuccessful()){
                    Config.Mensaje(getActivity(),"El tareo ha sido registrado");

                    resetValues();
                }else{
                    Config.Mensaje(getActivity(),"Ha ocurrido un error al registrar");

                }
                btnGuardar.setText("GUARDAR");
                btnGuardar.setEnabled(true);
            }

            @Override
            public void onFailure(Call<clsTrabajador> call, Throwable t) {
                Config.Mensaje(getActivity(),"Ha ocurrido un error en el servidor. "+t.getMessage());
                btnGuardar.setText("GUARDAR");
                btnGuardar.setEnabled(true);
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
    public void openDialog(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seleccione a los trabajadores");
// add a checkbox list

        builder.setMultiChoiceItems(personas, personasSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                personasSelected[which] = isChecked;
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
    public void configinit(){
        etHorainicio = (EditText) getView().findViewById(R.id.edittext_horainicio);
        etMinInicio = (EditText) getView().findViewById(R.id.edittext_mininicio);
        etHoraFin = (EditText) getView().findViewById(R.id.edittext_horafin);
        etMinFin = (EditText) getView().findViewById(R.id.edittext_minfin);
        etHorainicio.setTransformationMethod(null);
        etHorainicio.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        etMinInicio.setTransformationMethod(null);
        etMinInicio.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
        etHoraFin.setTransformationMethod(null);
        etHoraFin.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        etMinFin.setTransformationMethod(null);
        etMinFin.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
        spinner_tareo_trabajador_listado_actividades = (Spinner) getView().findViewById(R.id.spinner_tareo_trabajador_listado_actividades);
        spinner_tareo_trabajador_listado_proyectos = (Spinner) getView().findViewById(R.id.spinner_tareo_trabajador_listado_proyectos);
        //btn_ver_actividades_registradas = (Button) getActivity().findViewById(R.id.btn_ver_actividades_registradas);
        btnAbrirDialogSelectPerson = (Button) getActivity().findViewById(R.id.btn_abrir_dialog_seleccione_trabajador);
        btnGuardar = (Button) getActivity().findViewById(R.id.btn_guardar_actividad);

    }

    public void addItem(){
        clsActividad item = new clsActividad(1,"GERAL POMA SANTOS","21:00","08:00","03:00","10:00","07:00","20:00");
        rowItems.add(item);
    }

    public void llamarIntentActividadesRegistradas(){
        ActividadesRegistradasFragment actividadesRegF = new ActividadesRegistradasFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, actividadesRegF);
        fragmentTransaction.commit();
    }

    public void listadoTrabajadores(){
        Call<List<clsTrabajador>> Trabajadores = servicio.getTrabajadores();
        Trabajadores.enqueue(new Callback<List<clsTrabajador>>() {
            @Override
            public void onResponse(Call<List<clsTrabajador>> call, Response<List<clsTrabajador>> response) {

                if (response.isSuccessful()){
                    personas = new String[response.body().size()];
                    personas_id = new Integer[response.body().size()];
                    personasSelected = new boolean[response.body().size()];
                    rowTrabajadores = new ArrayList<clsTrabajador>();
                    Integer i = 0;
                    for (clsTrabajador p : response.body()) {
                        personas[i] = p.getNombres()+" "+p.getApellidos();
                        personas_id[i] = p.getId();
                        personasSelected[i] = false;
                        rowTrabajadores.add(p);
                        i++;
                    }
                    Log.d("FOREACH",""+personas);
                    /*Log.d("FOREACH",""+personas[0]);*/
                    loadEventListener();
                }
            }

            @Override
            public void onFailure(Call<List<clsTrabajador>> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
            }
        });

    }
    public void listadoActividades(){
        Call<List<clsTarea>> tareasProyecto = servicio.getTareasProyecto();
        tareasProyecto.enqueue(new Callback<List<clsTarea>>() {
            @Override
            public void onResponse(Call<List<clsTarea>> call, Response<List<clsTarea>> response) {
                if (response.isSuccessful()){
                    actividades_array = new String[response.body().size()];
                    id_actividades_array = new Integer[response.body().size()];

                    List<String> spinnerArray =  new ArrayList<String>();
                    rowTareasProyecto = new ArrayList<clsTarea>();
                    spinnerArray.add("SELECCIONE ACTIVIDAD");
                    Integer i = 0;

                    for (clsTarea p : response.body()) {
                        spinnerArray.add((i+1)+". "+p.getNombre());
                        actividades_array[i] = (i+1)+". "+p.getNombre();
                        id_actividades_array[i] = p.getId();
                        rowTareasProyecto.add(p);
                        i++;
                    }
                    /*List <String> listClone = new ArrayList<String>();
                    for (String string : spinnerArray) {
                        if(string.matches("(?i)(bea).*")){
                            listClone.add(string);
                        }
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_tareo_trabajador_listado_actividades.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<clsTarea>> call, Throwable t) {

            }
        });

    }
    public void listadoProyectos(){
        final Call<List<clsProyecto>> proyectos = servicio.getProyectosProduccion();
        proyectos.enqueue(new Callback<List<clsProyecto>>() {
            @Override
            public void onResponse(Call<List<clsProyecto>> call, Response<List<clsProyecto>> response) {
                if (response.isSuccessful()){
                    proyectos_array = new String[response.body().size()];
                    id_proyectos_array = new Integer[response.body().size()];
                    spnArrProyectos =  new ArrayList<String>();
                    rowProyectos = new ArrayList<clsProyecto>();
                    spnArrProyectos.add("SELECCIONE PROYECTO");

                    Integer i = 0;
                    for (clsProyecto p : response.body()) {
                        spnArrProyectos.add((i+1)+". "+p.getNombre());
                        proyectos_array[i] = (i+1)+". "+p.getNombre();
                        id_proyectos_array[i] = p.getId();
                        rowProyectos.add(p);
                        i++;
                    }
                    /*List <String> listClone = new ArrayList<String>();
                    for (String string : spinnerArray) {
                        if(string.matches("(?i)(bea).*")){
                            listClone.add(string);
                        }
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_spinner_item, spnArrProyectos);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_tareo_trabajador_listado_proyectos.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<clsProyecto>> call, Throwable t) {
                Log.d("ERROR PROD PROYECTO",t.getMessage());
            }
        });



    }
    public void resetValues(){
        etHorainicio.setText("");
        etMinInicio.setText("");
        etHoraFin.setText("");
        etMinFin.setText("");
        spinner_tareo_trabajador_listado_proyectos.setSelection(0);
        spinner_tareo_trabajador_listado_actividades.setSelection(0);
        for (int i = 0;i<personas.length;i++){
            personasSelected[i] = false;
        }
    }

}