package com.example.myapplication.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    private Integer id;
    private String displayName;
    private String usuario;
    private String password;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    private String nombres;
    private String apellidos;


    public LoggedInUser(Integer id, String displayName,String usuario,String password) {
        this.id = id;
        this.displayName = displayName;
        this.setUsuario(usuario);
        this.setPassword(password);
    }

    public Integer getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String contrasena) {
        this.password = password;
    }
}
