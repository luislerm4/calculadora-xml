package mx.unison;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Calculadora {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No se proporcionaron argumentos requeridos");
            System.exit(1);
        }

        if (args.length == 1) {
            System.out.println("Debes incluir al menos el nombre de una variable");
            System.exit(2);
        }

        String archivoXml = args[0];
        String var1 = args[1];
        String var2 = (args.length > 2) ? args[2] : null;

        try {
            List<Double> valores1 = procesarVariable(archivoXml, var1);
            if (valores1.isEmpty()) {
                System.out.println("Error: La variable '" + var1 + "' no existe en el documento o no contiene valores.");
                System.exit(3);
            }
            mostrarEstadisticas(var1, valores1);

            if (var2 != null) {
                List<Double> valores2 = procesarVariable(archivoXml, var2);
                if (valores2.isEmpty()) {
                    System.out.println("Error: La variable '" + var2 + "' no existe en el documento o no contiene valores.");
                    System.exit(4);
                }
                mostrarEstadisticas(var2, valores2);

                if (valores1.size() != valores2.size()) {
                    System.out.println("\nAdvertencia: Las variables tienen diferente cantidad de datos, la correlación podría verse afectada.");
                }

                double correlacion = calcularCorrelacion(valores1, valores2);
                System.out.println("\n--- Coeficiente de Correlación ---");
                System.out.println("Correlación entre " + var1 + " y " + var2 + ": " + correlacion);
            }

        } catch (IOException e) {
            System.out.println("Error: No se pudo abrir o leer el documento XML (" + archivoXml + ").");
            System.exit(5);
        } catch (SAXException e) {
            System.out.println("Error de análisis: " + e.getMessage());
            System.exit(6);
        } catch (ParserConfigurationException e) {
            System.out.println("Error de configuración del parser: " + e.getMessage());
            System.exit(7);
        }
    }

    private static List<Double> procesarVariable(String archivoXml, String variable)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        VariablesHandler handler = new VariablesHandler(variable);
        saxParser.parse(new File(archivoXml), handler);
        return handler.getValores();
    }

    private static void mostrarEstadisticas(String nombreVar, List<Double> valores) {
        double suma = 0;
        for (double v : valores) {
            suma += v;
        }
        double media = suma / valores.size();

        double sumaVarianza = 0;
        for (double v : valores) {
            sumaVarianza += Math.pow(v - media, 2);
        }
        double varianza = (valores.size() > 1) ? sumaVarianza / (valores.size() - 1) : 0;
        double desviacionEstantar = Math.sqrt(varianza);

        double max = Collections.max(valores);
        double min = Collections.min(valores);

        System.out.println("\n----------------------------------------");
        System.out.println(" Estadisticas para la variable: " + nombreVar);
        System.out.println("----------------------------------------");
        System.out.println("Media: " + media);
        System.out.println("Variancia: " + varianza);
        System.out.println("Desviacion Estandar: " + desviacionEstantar);
        System.out.println("Valor maximo: " + max);
        System.out.println("Valor minimo: " + min);
    }

    private static double calcularCorrelacion(List<Double> x, List<Double> y) {
        int n = Math.min(x.size(), y.size());
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;

        for (int i = 0; i < n; i++) {
            double xi = x.get(i);
            double yi = y.get(i);
            sumX += xi;
            sumY += yi;
            sumXY += xi * yi;
            sumX2 += xi * xi;
            sumY2 += yi * yi;
        }

        double numerador = (n * sumXY) - (sumX * sumY);
        double denominador = Math.sqrt(((n * sumX2) - (sumX * sumX)) * ((n * sumY2) - (sumY * sumY)));

        if (denominador == 0) {
            return 0;
        }
        return numerador / denominador;
    }
}