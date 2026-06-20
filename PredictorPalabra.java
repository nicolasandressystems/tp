import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Clase que representa un predictor de palabras, basado en k-gramas (k-grams). Permite sugerir
 * palabras basándose en las últimas k palabras de una frase o texto ingresado por el usuario.
 * El predictor utiliza un modelo de lenguaje que se ha entrenado previamente con un corpus de
 * texto para generar sugerencias de palabras relevantes.
 */
public class PredictorPalabra {

    /**
     * Grado (k) del predictor.
     */
    private int grado;

    /**
     * Corpus utilizado para el entrenamiento del modelo.
     */
    private List<String> corpus;

    /**
     * Transiciones resultantes del entrenamiento. El
     * map almacena las secuencias de k palabras (listas de strings) como claves,
     * y como valores, otro map que asocia palabras siguientes (strings) con su
     * frecuencia de aparición (enteros).
     */
    private Map<List<String>, Map<String, Integer>> transiciones;

    /**
     * Generador de números aleatorios, utilizado para elegir una palabra
     * al azar del corpus cuando no hay predicción disponible.
     */
    private final Random random = new Random();

    /**
     * Constructor de la clase PredictorPalabra. Crea un predictor de palabras con el grado
     * especificado y el corpus proporcionado.
     * @param grado el grado (k) del predictor, que determina cuántas palabras anteriores
     *              se consideran para generar sugerencias.
     * @param corpus el texto utilizado para entrenar el modelo de lenguaje, a partir del
     *               cual se generarán las sugerencias de palabras.
     */
    public PredictorPalabra(int grado, List<String> corpus) {
        if (grado < 1) throw new IllegalArgumentException("El grado debe ser positivo.");
        if (corpus == null) throw new IllegalArgumentException("El corpus debe ser no nulo.");
        this.grado = grado;
        this.corpus = corpus;
        this.transiciones = new HashMap<>();
    }

    /**
     * Método para entrenar el modelo de lenguaje utilizando el corpus proporcionado.
     * Este método procesa el corpus para construir las transiciones entre secuencias de k
     * palabras y las palabras siguientes, almacenando esta información en el mapa de
     * transiciones. El entrenamiento es esencial para que el predictor pueda generar
     * sugerencias de palabras relevantes basándose en las secuencias de palabras anteriores.
     */
    public void entrenar() {
        transiciones.clear();
        // Recorremos el corpus y, para cada posición que tenga al menos
        // "grado" palabras siguientes disponibles, registramos el k-grama
        // (las "grado" palabras a partir de esa posición) y la palabra que
        // lo sigue inmediatamente.
        for (int i = 0; i + grado < corpus.size(); i++) {
            List<String> kgrama = new ArrayList<>(corpus.subList(i, i + grado));
            String siguiente = corpus.get(i + grado);

            Map<String, Integer> frecuencias = transiciones.get(kgrama);
            if (frecuencias == null) {
                frecuencias = new HashMap<>();
                transiciones.put(kgrama, frecuencias);
            }
            int frecuenciaActual = frecuencias.getOrDefault(siguiente, 0);
            frecuencias.put(siguiente, frecuenciaActual + 1);
        }
    }

    /**
     * Predice la siguiente palabra basándose en un prefijo de al menos k palabras.
     * Este método toma las últimas k palabras del texto, y elige la palabra más probable
     * (de acuerdo a lo observado en el corpus de entrenamiento) que sigue al prefijo
     * provisto. Si ese prefijo no fue visto en entrenamiento, retorna una palabra aleatoria
     * del corpus.
     * @param texto es una lista de palabras que representa el texto ingresado por el usuario,
     *              del cual se tomarán las últimas k palabras para generar la predicción de
     *              la siguiente palabra.
     * @return la palabra siguiente más probable según el modelo de lenguaje entrenado,
     * o una palabra aleatoria del corpus si el prefijo no fue visto en entrenamiento.
     */
    public String predecirSiguientePalabra(List<String> texto) {
        List<String> ultimoKgrama = obtenerUltimoKgrama(texto);
        Map<String, Integer> frecuencias = transiciones.get(ultimoKgrama);

        if (frecuencias == null || frecuencias.isEmpty()) {
            return palabraAleatoriaDelCorpus();
        }
        return palabraMasFrecuente(frecuencias);
    }

    /**
     * Decide si existe un predictor de K palabras (K es el grado)
     * en las transiciones
     * @param texto el el texto del cual tomar las K últimas palabras
     * @return true ssi existe predictor
     */
    public boolean existePredictor(List<String> texto) {
        List<String> ultimoKgrama = obtenerUltimoKgrama(texto);
        return transiciones.containsKey(ultimoKgrama);
    }

    /**
     * Extrae el último k-grama (las últimas {@code grado} palabras) de un
     * texto. Si el texto tiene menos de {@code grado} palabras, se devuelven
     * todas las palabras disponibles (ese k-grama, al tener un tamaño
     * distinto a {@code grado}, nunca coincidirá con una clave registrada
     * en las transiciones).
     * @param texto el texto del cual extraer el k-grama final
     * @return una nueva lista con el k-grama final del texto
     */
    private List<String> obtenerUltimoKgrama(List<String> texto) {
        int cantidadPalabras = texto.size();
        int desde = Math.max(0, cantidadPalabras - grado);
        return new ArrayList<>(texto.subList(desde, cantidadPalabras));
    }

    /**
     * Dado un map de frecuencias de palabras, devuelve la palabra con
     * mayor frecuencia. Ante un empate, se queda con la primera encontrada.
     * @param frecuencias map de palabra a frecuencia, no nulo ni vacío
     * @return la palabra con mayor frecuencia
     */
    private String palabraMasFrecuente(Map<String, Integer> frecuencias) {
        String palabraGanadora = null;
        int mayorFrecuencia = -1;
        for (Map.Entry<String, Integer> entrada : frecuencias.entrySet()) {
            if (entrada.getValue() > mayorFrecuencia) {
                mayorFrecuencia = entrada.getValue();
                palabraGanadora = entrada.getKey();
            }
        }
        return palabraGanadora;
    }

    /**
     * Elige y devuelve una palabra al azar del corpus de entrenamiento.
     * @return una palabra aleatoria del corpus
     */
    private String palabraAleatoriaDelCorpus() {
        int indice = random.nextInt(corpus.size());
        return corpus.get(indice);
    }

}
