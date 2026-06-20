import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee el contenido de un archivo de textos, lo procesa y almacena
 * como lista de palabras.
 */
public class LectorArchivo {

    /**
     * Lista que almacena las palabras leídas del archivo de
     * textos.
     */
    private List<String> palabras;

    /**
     * Constructor de la clase LectorArchivo. Recibe la ruta a un
     * archivo de texto, lo lee y produce internamente
     * una lista de palabras.
     * @param ruta es la ruta al archivo de texto que se desea leer
     *             y procesar. El constructor debe manejar la lectura
     *             del archivo, la separación del texto en palabras,
     *             y el almacenamiento de estas palabras.
     */
    public LectorArchivo(String ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta debe ser no nula.");
        }
        palabras = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            String linea = br.readLine();
            while (linea != null) {
                String[] palabrasLinea = linea.split("\\s+");
                for (String palabra: palabrasLinea) {
                    if (!palabra.trim().isEmpty()) {
                        palabras.add(palabra);
                    }
                }
                linea = br.readLine();
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + e.getMessage(), e);
        }
    }


    /**
     * Devuelve la lista de palabras obtenida del archivo de texto.
     * Esta lista es el resultado del procesamiento realizado en el
     * constructor, donde se leyó el archivo, se separó el texto en
     * palabras y se almacenaron en la lista.
     * @return la lista de palabras obtenida del archivo de texto
     */
    public List<String> obtenerListaPalabras() {
        return palabras;
    }


}
