import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de tests para la clase PredictorPalabra.
 * Incluye tests de precondiciones, diferentes grados k, cobertura de ramas y ejemplos en castellano.
 */
class PredictorPalabraTest {

    private List<String> corpusEjemplo;
    private List<String> corpusMinimo;

    @BeforeEach
    public void setup() {
        // Corpus en castellano sobre un tema simple
        corpusEjemplo = Arrays.asList(
            "el", "gato", "gato", "negro", "el", "gato", "duerme",
            "el", "gato", "negro", "come", "pescado", "el", "perro",
            "negro", "corre", "rápido", "el", "perro", "es", "amistoso",
            "el", "gato", "es", "aventurero", "el", "gato", "negro", "es", "independiente"
        );

        // Corpus mínimo de 3 palabras
        corpusMinimo = Arrays.asList("hola", "mundo", "java");
    }

    @Test
    public void testConstructorConParametrosValidos() {
        // Arrange
        int gradoEsperado = 2;
        int tamanioCorpusEsperado = corpusEjemplo.size();

        // Act
        PredictorPalabra predictor = new PredictorPalabra(gradoEsperado, corpusEjemplo);

        // Assert
        assertNotNull(predictor, "El predictor no debe ser null");
    }

    @Test
    public void testConstructorConGradoInvalido() {
        // Arrange
        int gradoInvalido = 0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new PredictorPalabra(gradoInvalido, corpusEjemplo);
        }, "Debe lanzar excepción para grado <= 0");
    }

    @Test
    public void testConstructorConGradoNegativo() {
        // Arrange
        int gradoNegativo = -5;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new PredictorPalabra(gradoNegativo, corpusEjemplo);
        }, "Debe lanzar excepción para grado negativo");
    }

    @Test
    public void testConstructorConCorpusNull() {
        // Arrange
        int gradoValido = 1;
        List<String> corpusNull = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new PredictorPalabra(gradoValido, corpusNull);
        }, "Debe lanzar excepción para corpus null");
    }

    @Test
    public void testEntrenarConGradoUno() {
        // Arrange
        PredictorPalabra predictor = new PredictorPalabra(1, corpusEjemplo);

        // Act
        predictor.entrenar();
        // Verificar que después de entrenar podemos hacer predicciones
        List<String> textoPrueba = Arrays.asList("el");

        // Assert
        assertTrue(predictor.existePredictor(textoPrueba),
            "Debe existir predictor para 'el' después de entrenar");
    }

    @Test
    public void testEntrenarConGradoDos() {
        // Arrange
        PredictorPalabra predictor = new PredictorPalabra(2, corpusEjemplo);

        // Act
        predictor.entrenar();
        // Buscar un bigrama que existe: "el gato"
        List<String> textoConBigrama = Arrays.asList("el", "gato");

        // Assert
        assertTrue(predictor.existePredictor(textoConBigrama),
            "Debe existir predictor para el bigrama 'el gato'");
    }

}

