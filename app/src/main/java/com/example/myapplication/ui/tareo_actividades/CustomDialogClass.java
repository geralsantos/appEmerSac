package com.example.myapplication.ui.tareo_actividades;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Config.Config;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.R;
import com.example.myapplication.data.model.clsActividad;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    public Context c;
    public Dialog d;
    public Button yes, no;
    public EditText etHoraFin,etMinFin;
    public Integer id;
    public String hora_inicio;
    public Integer usuario_id;
    public TareoActividadesFragment frag;
    private ApiService servicio = Config.getRetrofit().create(ApiService.class);

    public CustomDialogClass(Context a, Integer id,String hora_inicio,Integer usuario_id,TareoActividadesFragment frag) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.id=id;
        this.hora_inicio=hora_inicio;
        this.usuario_id=usuario_id;
        this.frag = frag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tareo_dialog_editar);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        etHoraFin = (EditText) findViewById(R.id.edittext_horafin);
        etMinFin = (EditText) findViewById(R.id.edittext_minfin);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                modificar();
                //c.finish();
                break;
            case R.id.btn_no:
                cancel();
                break;
            default:
                break;
        }

    }

    public void modificar(){

        if(etHoraFin.getText().toString().matches("") || etMinFin.getText().toString().matches("")){
            Toast.makeText(getContext(),"Debe llenar Hora y el Minuto",Toast.LENGTH_SHORT).show();return;
        }
        Integer hora = Integer.parseInt(etHoraFin.getText().toString());
        Integer minuto = Integer.parseInt(etMinFin.getText().toString());

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
            Toast.makeText(getContext(),"La Hora Fin debe ser mayor a la Hora Inicio",Toast.LENGTH_SHORT).show();return;
        }
        int hours = (int)mills /(1000 * 60 * 60);
        int mins = ((int)mills/(1000*60)) % 60;

        String horas_trabajadas = String.valueOf((((double)mins/60)+hours));
        Call<List<clsActividad>> call = servicio.tareo_editar(id,hora_fin,horas_trabajadas,usuario_id);
        call.enqueue(new Callback<List<clsActividad>>() {
            @Override
            public void onResponse(Call<List<clsActividad>> call, Response<List<clsActividad>> response) {
                if (response.isSuccessful()){
                    //frag.listViewActividades();
                    //((TareoActividadesFragment) c).listViewActividades();
                    Config.Mensaje(getContext(),"El tareo ha sido modificado");
                    dismiss();
                }else{
                    Config.Mensaje(getContext(),"Ha ocurrido un error al modificar");
                }
            }

            @Override
            public void onFailure(Call<List<clsActividad>> call, Throwable t) {
                Log.d("onFailure",t.getMessage()+"asd");

            }
        });

    }
}