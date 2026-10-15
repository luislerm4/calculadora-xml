package mx.unison;

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

        System.out.println("Archivo XML a procesar: " + archivoXml);
        System.out.println("Variable a calcular: " + var1);
    }
}