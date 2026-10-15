package mx.unison;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
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

        try {
            List<Double> valores1 = procesarVariable(archivoXml, var1);

            if (valores1.isEmpty()) {
                System.out.println("Error: La variable '" + var1 + "' no existe en el documento o no contiene valores.");
                System.exit(3);
            }

            System.out.println("¡Lectura exitosa!");
            System.out.println("Valores encontrados para " + var1 + ": " + valores1);

        } catch (IOException e) {
            System.out.println("Error: No se pudo abrir o leer el documento XML (" + archivoXml + ").");
            System.exit(4);
        } catch (SAXException e) {
            System.out.println("Error de análisis: " + e.getMessage());
            System.exit(5);
        } catch (ParserConfigurationException e) {
            System.out.println("Error de configuración del parser: " + e.getMessage());
            System.exit(6);
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
}