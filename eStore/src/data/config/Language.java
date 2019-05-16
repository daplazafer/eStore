package data.config;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Esta clase permite obtener el texto de una etiqueta xml pasandole el nombre
 * de la etiqueta. La clase esta diseñada con un patron de diseño singleton.
 *
 * Los archivos xml de idioma deben encontrarse donde indique la variable
 * xmlpath, por defecto en //languages/strings/ Cada xml debe ser de la forma
 * (ejemplo): spanish.xml
 *
 * <?xml version="1.0" encoding="iso-8859-1"?>
 * <language>
 * <file>Archivo</file>
 * <edit>Edición</edit>
 * <error1>Error al introducir los datos</error1>
 * </language>
 *
 * Todos deben contener las mismas etiquetas para el correcto funcionamiento del
 * programa, aunque no es necesario que tengan el mismo orden.
 *
 * @author Daniel Plaza
 */
public class Language {
    private static Language instance;// unica instancia de la clase
    private Document doc;
    private final String language;
    private final String xmlpath; //ruta por defecto de los xml de idioma

    //CONSTRUCTOR (privado)
    private Language(String language,String xmlpath) {
        /*
         * El constructor tiene como parametro el idioma. 
         * El archivo.xml del idioma debe encontrarse en languages/strings/idioma.xml
         */
        this.xmlpath=xmlpath;
        this.language = language;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            if (!new File(xmlpath + this.language + ".xml").exists()) {
                throw new IllegalArgumentException(this.language + ".xml not found");
            }
            doc = dBuilder.parse(xmlpath + this.language + ".xml");
            doc.getDocumentElement().normalize();
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    //metodo que devuelve la instancia: "constructor" secundario
    public static Language getLanguage(String language,String xmlpath) {
        /*
         * Devuelve una instancia con el idioma pedido
         */
        instance = new Language(language,xmlpath);
        return instance;
    }

    /**
     *
     * Método que devuelve el texto de la etiqueta.
     *
     * Si hay más de una etiqueta con el mismo nombre devolverá la primera
     * mostrando un mensaje por consola advirtiendo del conflicto.
     *
     * Si no encuentra la etiqueta devolvera "#<nombreEtiqueta>" y mostrará un
     * mensaje por consola avisando del conflicto.
     *
     * @param markup nombre de la etiqueta
     * @return texto de la etiqueta
     */
    public String write(String markup) {
        NodeList nodeList = doc.getElementsByTagName(markup);
        try {
            if (nodeList.getLength() > 1) {
                System.err.println("err.found " + nodeList.getLength() + " <" + markup + "> items in " + language + ".xml");
            }
            return nodeList.item(0).getTextContent();
        } catch (NullPointerException e) {
            System.err.println("can't find any <" + markup + "> item in " + language + ".xml");
            return "#<" + markup + ">";
        }
    }

}
