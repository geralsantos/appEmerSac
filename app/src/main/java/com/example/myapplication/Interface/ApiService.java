package com.example.myapplication.Interface;

import com.example.myapplication.data.model.LoggedInUser;
import com.example.myapplication.data.model.clsIncidencia;
import com.example.myapplication.data.model.clsProyecto;
import com.example.myapplication.data.model.clsSolid;
import com.example.myapplication.data.model.clsTarea;
import com.example.myapplication.data.model.clsTareoActividad;
import com.example.myapplication.data.model.clsTrabajador;
import com.example.myapplication.data.model.clsEstadoProyecto;
import com.example.myapplication.data.model.clsSupervision;
import com.example.myapplication.data.model.clsActividad;
import com.example.myapplication.data.model.clsMontCronograma;
import com.example.myapplication.data.model.clsSuperVisionAdjuntos;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //declarar las rutas del api
    @GET("montaje/proyectos/ver/")
    Call<List<clsProyecto>> getProyectos();

    @GET("produccion/solid/ver/{proyecto_id}")
    Call<List<clsSolid>> getSolids(
            @Path("proyecto_id") Integer proyecto_id
    );

    @GET("montaje/estados/ver/")
    Call<List<clsEstadoProyecto>> getEstadosProyecto();

    @GET("montaje/incidencias/ver/")
    Call<List<clsIncidencia>> getIncidenciasProyecto();

    @GET("produccion/proyectos/ver/")
    Call<List<clsProyecto>> getProyectosProduccion();

    @GET("produccion/tareas/ver/")
    Call<List<clsTarea>> getTareasProyecto();

    @GET("produccion/trabajadores/ver/")
    Call<List<clsTrabajador>> getTrabajadores();

    @POST("produccion/tareo/guardar/")
    @FormUrlEncoded
    Call<clsTrabajador> guardarTareo(
            @Field("proyecto_id") Integer proyecto_id,
            @Field("tarea_id") Integer tarea_id,
            @Field("hora_inicio") String hora_inicio,
            @Field("hora_fin") String hora_fin,
            @Field("horas_trabajadas") String horas_trabajadas,
            @Field("empleados[]") Integer[] empleados,
            @Field("usuario_id") Integer usuario_id,
            @Field("solid_id") Integer solid_id
    );

    @Multipart
    @POST("montaje/supervision/guardar/")
    //@FormUrlEncoded
    //Call<ResponseBody> guardarSupervision(@Body RequestBody file);
    Call<clsSupervision> guardarSupervision(
            @Part("proyecto_id") Integer proyecto_id,
            @Part("geolocalizacion_estado_id") Integer geolocalizacion_estado_id,
            @Part("descripcion") String descripcion,
            @Part("montaje_cronograma_id") Integer montaje_cronograma_id,
            @Part("latitud") String latitud,
            @Part("logitud") String logitud,
            @Part("usuario_id") Integer usuario_id,
            @Part("incidencias[]") Integer[] incidencia_id,
            @Part List<MultipartBody.Part> file

            );

    @POST("login/supervisor/")
    @FormUrlEncoded
    Call<LoggedInUser> Login(
            @Field("usuario") String usuario,
            @Field("contrasena") String contrasena
    );
    @POST("montaje/supervision/actividades/")
    @FormUrlEncoded
    Call<List<clsActividad>> supervision_actividades(
            @Field("id") Integer usuario_id
    );
    @GET("montaje/hitos/ver/{proyecto_id}")
    Call<List<clsMontCronograma>> getCronograma(
            @Path("proyecto_id") Integer proyecto_id
    );
    @GET("produccion/tareo/listado/")
    Call<List<clsTareoActividad>> getTareoActividades();

    @POST("produccion/tareo/editar/")
    @FormUrlEncoded
    Call<List<clsActividad>> tareo_editar(
            @Field("id") Integer id,
            @Field("hora_fin") String hora_fin,
            @Field("horas_trabajadas") String horas_trabajadas,
            @Field("usuario_id") Integer usuario_id
    );
    /*@Multipart
    @Headers({"CONNECT_TIMEOUT:10000", "READ_TIMEOUT:10000", "WRITE_TIMEOUT:10000"})
    @POST("montaje/supervision/guardarAdjuntos/")
    Call<clsSuperVisionAdjuntos> guardarSupervisionAdjuntos(
            @Part("supervision_id") Integer supervision_id,
            @Part("usuario_id") Integer usuario_id,
            @Part MultipartBody.Part file
    );

    @POST("montaje/supervision/guardarAdjuntos/")
    @FormUrlEncoded
    Call<clsSuperVisionAdjuntos> pruebaAdjuntoVideo(
            @Field("nombre_adjunto") String nombre_adjunto,
            @Field("supervision_id") Integer supervision_id,
            @Field("usuario_id") Integer usuario_id,
            @Field("videobuffer") String videobuffer
    );*/

}
