import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class VariablesHandler extends DefaultHandler {
    private String targetVariable;
    private List<Double> valores = new ArrayList<>(); // Uso de Java Collections API
    private boolean leyendoVariable = false;
    private StringBuilder textoActual = new StringBuilder();

    public VariablesHandler(String var) {
        super();
        this.targetVariable = var;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Lógica para identificar si qName coincide con la variable buscada
        if (qName.equals(targetVariable)) {
            leyendoVariable = true;
            textoActual.setLength(0); // Limpiar buffer
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (leyendoVariable) {
            textoActual.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals(targetVariable)) {
            try {
                double valor = Double.parseDouble(textoActual.toString().trim());
                valores.add(valor); 
            } catch (NumberFormatException e) {
                throw new SAXException("La variable seleccionada no es numerica");
            }
            leyendoVariable = false;
        }
    }

    public List<Double> getValores() {
        return valores;
    }
}