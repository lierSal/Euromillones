package euromillones.ateneasystems.es.euromillones.Clases;

import android.app.Application;

/**
 * Created by cubel on 26/01/15.
 */
public class ZDatosTemporales extends Application {

    /**
     * Declaracion de Variables Temporales
     */
    private int idUser = 0;//ID del usuario
    private String nombreUser = new String();//Nombre del Usuario
    private String passUser = new String();//Pass del Usuario (No se si completo o solo la primera parte)
    private String nivelUser = new String();//Nivel del Usuario
    private String mailUser = new String();//Mail del Usuario

    /**
     * Funciones para recuperar los datos
     */
    public String getIdUser() {
        return String.valueOf(idUser);
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public String getPassUser() {
        return passUser;
    }

    public String getNivelUser() {
        return nivelUser;
    }

    public String getMailUser() {
        return mailUser;
    }

    /**
     * Funciones para escribir datos en esta clase
     */
    public void setIdUser(int dato) {
        this.idUser = dato;
    }

    public void setNombreUser(String dato) {
        this.nombreUser = dato;
    }

    public void setPassUser(String dato) {
        this.passUser = dato;
    }

    public void setNivelUser(String dato) {
        this.nivelUser = dato;
    }

    public void setMailUser(String dato) {
        this.mailUser = dato;
    }
}
