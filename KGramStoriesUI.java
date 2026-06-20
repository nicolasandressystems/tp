import java.util.Scanner;

/**
 * Clase que implementa interfaz de usuario de la aplicación
 * KGram Stories. Permite las opciones de:
 * - Cargar un corpus de un archivo de texto.
 * - Entrenar el modelo de lenguaje con el corpus cargado, para un
 * grado (k) provisto por el usuario.
 * - Ingresar una frase o texto por parte del usuario, y un tamaño o
 * número N de palabras a generar, y obtener un texto generado de N
 * palabras, basado en las últimas k palabras de la frase ingresada,
 * utilizando el modelo entrenado.
 */
public class KGramStoriesUI {

    private static ControlKGramStories control;
    private static final Scanner SCANNER = new Scanner(System.in);


    /**
     * Método principal de la UI de la aplicación KGram Stories.
     * @param args es ignorado por esta aplicación
     */
    public static void main(String[] args) {
        mostrarBienvenida();
        control = new ControlKGramStories();
        boolean finalizado = false;
        boolean corpusCargado = false;
        boolean modeloEntrenado = false;
        while (!finalizado) {
            mostrarMenuOpciones();
            int opcion = leerOpcionUsuario();
            switch (opcion) {
                case 1:
                    // cargar corpus desde archivo de texto
                    String rutaArchivo = leerRutaArchivo();
                    try {
                        control.cargarCorpus(rutaArchivo);
                        corpusCargado = true;
                        modeloEntrenado = false;
                        System.out.println("Corpus cargado con éxito. Número de palabras: " + control.tamanhoCorpus());
                    } catch (RuntimeException e) {
                        System.out.println("No se pudo cargar el corpus: " + e.getMessage());
                    }
                    break;
                case 2:
                    // entrenar modelo de lenguaje
                    if (!corpusCargado) {
                        System.out.println("Primero debe cargar un corpus (opción 1).");
                    }
                    else {
                        int grado = leerGrado();
                        control.entrenarPredictores(grado);
                        modeloEntrenado = true;
                        System.out.println("Modelo de lenguaje entrenado con éxito para grado k = " + grado);
                    }
                    break;
                case 3:
                    // generar texto
                    if (!modeloEntrenado) {
                        System.out.println("Primero debe entrenar el modelo (opción 2).");
                    }
                    else {
                        String frase = leerFrase();
                        int numPalabras = leerNumeroPalabras();
                        String textoGenerado = control.generarTexto(frase, numPalabras);
                        System.out.println(textoGenerado);
                    }
                    break;
                case 4:
                    // salir
                    finalizado = true;
                    break;
            }
        }
        mostrarDespedida();
    }

    /**
     * Imprime mensaje de bienvenida al sistema de predicción.
     */
    public static void mostrarBienvenida() {
        System.out.println("Bienvenido a KGramStories");
    }

    /**
     * Muestra por consola las opciones disponibles para el usuario.
     */
    public static void mostrarMenuOpciones() {
        System.out.println();
        System.out.println("Seleccione una opción:");
        System.out.println("1) Cargar corpus desde archivo");
        System.out.println("2) Entrenar modelo");
        System.out.println("3) Generar texto");
        System.out.println("4) Salir");
    }

    /**
     * Lee la opción del menú principal y valida que esté en el rango [1, 4].
     * @return la opción seleccionada por el usuario
     */
    public static int leerOpcionUsuario() {
        while (true) {
            System.out.print("Ingrese opción (1-4): ");
            String entrada = SCANNER.nextLine().trim();
            try {
                int opcion = Integer.parseInt(entrada);
                if (opcion >= 1 && opcion <= 4) {
                    return opcion;
                }
            } catch (NumberFormatException ignored) {
                // Se informa el error con el mismo mensaje de abajo.
            }
            System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    /**
     * Solicita y lee la ruta del archivo de corpus por consola.
     * @return la ruta ingresada por el usuario
     */
    public static String leerRutaArchivo() {
        while (true) {
            System.out.print("Ingrese la ruta del archivo de corpus: ");
            String ruta = SCANNER.nextLine().trim();
            if (!ruta.isEmpty()) {
                return ruta;
            }
            System.out.println("La ruta no puede estar vacía.");
        }
    }

    /**
     * Solicita y lee el grado k del modelo de lenguaje.
     * @return el grado k ingresado por el usuario
     */
    public static int leerGrado() {
        while (true) {
            System.out.print("Ingrese el grado k (entero positivo): ");
            String entrada = SCANNER.nextLine().trim();
            try {
                int grado = Integer.parseInt(entrada);
                if (grado > 0) {
                    return grado;
                }
            } catch (NumberFormatException ignored) {
                // Se informa el error con el mismo mensaje de abajo.
            }
            System.out.println("Valor inválido. Debe ingresar un entero positivo.");
        }
    }

    /**
     * Solicita y lee una frase inicial para la generación de texto.
     * @return la frase ingresada por el usuario
     */
    public static String leerFrase() {
        while (true) {
            System.out.print("Ingrese la frase inicial: ");
            String frase = SCANNER.nextLine().trim();
            if (!frase.isEmpty()) {
                return frase;
            }
            System.out.println("La frase no puede estar vacía.");
        }
    }

    /**
     * Solicita y lee cuántas palabras se deben generar.
     * @return la cantidad de palabras a generar
     */
    public static int leerNumeroPalabras() {
        while (true) {
            System.out.print("Ingrese cuántas palabras generar (entero positivo): ");
            String entrada = SCANNER.nextLine().trim();
            try {
                int cantidad = Integer.parseInt(entrada);
                if (cantidad > 0) {
                    return cantidad;
                }
            } catch (NumberFormatException ignored) {
                // Se informa el error con el mismo mensaje de abajo.
            }
            System.out.println("Valor inválido. Debe ingresar un entero positivo.");
        }
    }

    /**
     * Imprime mensaje de despedida al cerrar la aplicación.
     */
    public static void mostrarDespedida() {
        System.out.println("Gracias por usar KGramStories. ¡Hasta luego!");
    }


}
