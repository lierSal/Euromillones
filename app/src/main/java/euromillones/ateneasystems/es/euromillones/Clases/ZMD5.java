package euromillones.ateneasystems.es.euromillones.Clases;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cubel on 22/01/15.
 * Funcionamiento:
 * <p/>
 * -- Para login --
 * comprobarMD5 recibira dos string, la contraseña insertada por el usuario, y la contraseña recibida
 * desde la Base de Datos que ira codificada en MD5.
 * Retornara un true si coinciden, o un false si no coinciden
 * <p/>
 * -- Para Registro Nuevo --
 * generarMD5 recibira un string que sera la contraseña insertada por el usuario.
 * Retornara otro string donde estara el MD5 completo listo para guardar en la Base de Datos
 * <p/>
 * -- Funciones Automaticas --
 * codificadorMD5 recibira un string y se encarga de apicarle una encriptacion MD5
 * Retornara un string encriptado en MD5
 * <p/>
 * generarSALT recibira un String (creo que deberia recibir el pass del usuario)
 * Retornara un string en este caso un SALT
 * NOTA: Como segunda opcion, sera una funcion que no reciba nada pero si que devolvera el SALT
 */
public class ZMD5 {

    public Boolean comprobarMD5(String pass, String MD5) {
        //El MD5 tiene la siguiente estructura "caracteres:caracteres"
        //Por lo tanto deberemos indicar que solo queremos el segundo grupo de caracteres
        //para poder hacer la comprobacion.
        int inicioCadena = MD5.indexOf(":");// Indicamos que ":" sera donde empezar a leer
        //Ahora deberemos juntar, los segundos caracteres, con el "pass" y encriptarlo, asi sabremos
        //si la contraseña es la misma que MD5 completo
        //Indicamos que tiene que coger el siguiente caracter al ":"
        String codificador = MD5.substring(inicioCadena + 1); //De esta forma ya tenemos el SALT
        //Creamos una nueva variable para juntar el pass + el SALT
        String passYsalt = pass + codificador;
        //Una vez junto, lo encriptamos en MD5 (Crearemos y metermos el MD5 en passMD5)
        String passMD5 = codificadorMD5(passYsalt); // le pasamos los datos a la funcion codificadorMD5
        //Ahora comprobaremos si el pass del Usuario codificado es el mismo al MD5 traido de la DB
        if (MD5.equals(passMD5 + ":" + codificador)) {
            //El pass SI es correcto, devolvemos True
            return true;
        } else {
            //El pass NO es correcto, devolvemos False
            return false;
        }
    }

    public String generarMD5(String pass) {
        //Creamos una variable a la cual le metemos una cadena aleatoria generada por "generarSALT()"
        String nsalt = generarSALT();
        //mandamos al codificador MD5 la contraseña mas la cadena aleatoria
        String codificado = codificadorMD5(pass + nsalt);
        //Log.e("CODIFICADO GENERADO:",codificado);
        //En la base de datos hay que guardar lo codificado:llaveCodificadora (lo que retorna deberia ir a la base de datos)
        return codificado + ":" + nsalt;
    }

    public String codificadorMD5(String codificador) {
        //Creamos una variable donde ira a parar el resultado de la encriptacion en MD5
        String MD5Final = "";
        try {
            // Create MD5 Hash
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(codificador.getBytes());
            int size = b.length;
            StringBuffer h = new StringBuffer(size);
            //algoritmo y arreglo md5
            for (int i = 0; i < size; i++) {
                int u = b[i] & 255;
                if (u < 16) {
                    h.append("0" + Integer.toHexString(u));
                } else {
                    h.append(Integer.toHexString(u));
                }
            }
            //Metemos el resultado de la enciptacion en la variable de respuesta
            MD5Final = h.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //clave encriptada, la enviamos de vuelta al solicitante
        return MD5Final;
    }

    public String generarSALT() {
        //Añadimos todos los caracteres que queremos para que genere la cadena aleatoria, cuantos mas, mas dificil sera de saltar
        String caracteres[] = {"a", "b", "c", "d", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        //variable donde ira la cadena generada
        String salt = new String();
        //variable para el numero aleatorio
        int numero;
        //Esto se repetira 32 veces para sacar 32 caracteres aleatorios
        for (int i = 0; i < 32; i++) {
            //Generamos un numero aleatorio entre 0 y la longitud de la cadena -1
            numero = (int) (Math.random() * caracteres.length - 1);
            //el numero creado, sera el encargado de decir que caracter saldra,
            salt = salt + caracteres[numero];
            // Log.e("FOR",caracteres[numero]);
        }
        //Log.e("SALT GENERADO:",salt);
        return salt;
    }
}
