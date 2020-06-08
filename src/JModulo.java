/*
 * JModulo.java
 *
 * Created on 05 de marzo de 2008, 05:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author José Francisco
 */
public class JModulo {
    
    /** Creates a new instance of JModulo */
    public JModulo() {
    }
    
    public static String Quitar_Caracter(String texto, String caracter)
    {
        // El texto puede ser una palabra, parrafo o lectura completa.
        // El caracter es el que vamos a eliminar de la palabra, parrafo o lectura completa.
        
        StringBuffer s = new StringBuffer(texto);
        int posicion = s.indexOf(caracter);
        
        while(posicion != -1)
        {
            s = s.delete(posicion, posicion + 1);
            posicion = s.indexOf(caracter);
        }
        
        texto = String.valueOf(s);
        
        return texto;
    }
}
