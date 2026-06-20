import java.util.List;
import java.util.ArrayList;

public class ControlKGramStories {

    /**
     * Lista de palabras, que constituye el corpus de entrenamiento.
     * La lectura de archivo de texto deja en este campo las palabras
     * del corpus
     */
    private static List<String> corpus;


    /**
     * Arreglo de predictores basados en k-gramas, para k que van desde
     * 1 hasta K, para un K elegido por el usuario.
     */
    private PredictorPalabra[] predictores;

    /**
     * Carga el corpus desde un archivo de texto.
     * @param rutaArchivo ruta al archivo de corpus
     */
    public void cargarCorpus(String rutaArchivo) {
        LectorArchivo lector = new LectorArchivo(rutaArchivo);
        corpus = lector.obtenerListaPalabras();
    }

    /**
     * Devuelve la cantidad de palabras cargadas en el corpus.
     * Precondición: debe haber cargado un corpus previamente.
     * @return tamaño del corpus.
     */
    public int tamanhoCorpus() {
        if (corpus == null) {
            throw new IllegalStateException("Debe cargar un corpus antes de obtener su tamaño.");
        }
        return corpus.size();
    }

    /**
     * Entrena predictores para grados desde 1 hasta {@code gradoMaximo}.
     * @param gradoMaximo grado máximo a entrenar
     */
    public void entrenarPredictores(int gradoMaximo) {
        if (gradoMaximo < 1) {
            throw new IllegalArgumentException("El grado debe ser positivo.");
        }
        if (corpus == null || corpus.isEmpty()) {
            throw new IllegalStateException("Debe cargar un corpus antes de entrenar.");
        }

        int maximoEntrenable = Math.min(gradoMaximo, corpus.size() - 1);
        if (maximoEntrenable < 1) {
            throw new IllegalStateException("El corpus no tiene suficientes palabras para entrenar.");
        }

        predictores = new PredictorPalabra[maximoEntrenable];
        for (int i = 1; i <= maximoEntrenable; i++) {
            PredictorPalabra predictor = new PredictorPalabra(i, corpus);
            predictor.entrenar();
            predictores[i - 1] = predictor;
        }
    }

    /**
     * Genera texto a partir de una frase semilla y una cantidad de palabras.
     * @param frase frase inicial
     * @param numeroPalabras cantidad de palabras a generar
     * @return texto generado incluyendo la frase inicial
     */
    public String generarTexto(String frase, int numeroPalabras) {
        if (frase == null) {
            throw new IllegalArgumentException("La frase debe ser no nula.");
        }
        if (numeroPalabras < 1) {
            throw new IllegalArgumentException("La cantidad de palabras debe ser positiva.");
        }
        if (predictores == null || predictores.length == 0) {
            throw new IllegalStateException("Debe entrenar predictores antes de generar texto.");
        }

        List<String> texto = tokenizar(frase);
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("La frase debe contener al menos una palabra.");
        }

        for (int i = 0; i < numeroPalabras; i++) {
            String siguiente = predecirSiguiente(texto);
            texto.add(siguiente);
        }

        return String.join(" ", texto);
    }

    /**
     * Predice la siguiente palabra usando Backoff en base a los predictores
     * basados en k-gramas entrenados. Comienza con el predictor de mayor grado (k más grande)
     * y si no encuentra una predicción válida, retrocede al siguiente predictor
     * de menor grado, hasta llegar al predictor de unigramas. Si ningún
     * predictor tiene una predicción válida, retorna una palabra aleatoria
     * del corpus.
     * Precondición: debe haber entrenado predictores previamente.
     *              El texto provisto debe contener al menos una palabra.
     * @param texto es el texto a partir del cual predecir la siguiente palabra.
     *              debe ser no nulo.
     * @return la siguiente palabra más probable al texto.
     */
    private String predecirSiguiente(List<String> texto) {
        if (predictores == null || predictores.length == 0) {
            throw new IllegalStateException("Debe entrenar predictores antes de predecir la siguiente palabra.");
        }
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("El texto debe ser no nulo y contener al menos una palabra.");
        }
        // Backoff: empezamos con el predictor de mayor grado (último del
        // arreglo) y vamos retrocediendo hacia grados menores hasta
        // encontrar uno que tenga una predicción para el texto dado.
        for (int i = predictores.length - 1; i >= 0; i--) {
            PredictorPalabra predictor = predictores[i];
            if (predictor.existePredictor(texto)) {
                return predictor.predecirSiguientePalabra(texto);
            }
        }

        // Ningún predictor (ni siquiera el de unigramas) tiene una
        // predicción registrada: el predictor de grado 1 ya se encarga de
        // devolver una palabra aleatoria del corpus en este caso.
        return predictores[0].predecirSiguientePalabra(texto);
    }

    /**
     * Tokeniza (genera lista de palabras) a partir de un texto.
     * @param texto es el texto a tokenizar
     * @return lista de palabras del texto
     */
    private List<String> tokenizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("La frase debe ser no nula y no vacía.");
        }
        String[] partes = texto.trim().split("\\s+");
        List<String> palabras = new ArrayList<>();
        for (String parte : partes) {
            if (!parte.isEmpty()) {
                palabras.add(parte);
            }
        }
        return palabras;
    }
}
