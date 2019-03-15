package ru.javaops.masterjava;

import com.google.common.io.Resources;
import org.xml.sax.SAXException;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;

public class MainStaX {
    private String element;

    public MainStaX(String element) throws JAXBException, IOException, SAXException {
        this.element = element;
    }

    public static void main(String[] args) throws Exception {
        MainStaX xx = new MainStaX("User");
     //   MainStaX xx2 = new MainStaX("masterjava");
        xx.readCities();
     //   xx2.print();
    }
    public void readCities() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    if (element.equals(reader.getLocalName())) {
                        System.out.println(reader.getAttributeValue(2));
                     }
                }
            }
        }
    }

}
