package ru.javaops.masterjava;

import com.google.common.io.Resources;
import org.xml.sax.SAXException;
import ru.javaops.masterjava.xml.schema.Group;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);
    private String nameProject;

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public MainXml(String nameProject) throws JAXBException, IOException, SAXException {
        this.nameProject = nameProject;
       }

    public static void main(String[] args) throws JAXBException, IOException, SAXException {
        MainXml xx = new MainXml("topjava");
        MainXml xx2 = new MainXml("masterjava");
        xx.print();
        xx2.print();
    }

    public void print () throws IOException, JAXBException, SAXException {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        String strPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(strPayload);
        List<String> xx = payload.getProjects().getProject()
                .stream()
                .filter(project -> project.getName().equalsIgnoreCase(nameProject))
                .map(project -> project.getName())
                .map(name -> {System.out.println(name); return name;})
                .collect(toList());

    }
}
