package ru.javaops.masterjava;

import com.google.common.io.Resources;
import org.xml.sax.SAXException;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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
     //   MainXml xx2 = new MainXml("masterjava");
        xx.print();
       // xx2.print();
    }

    public void print () throws IOException, JAXBException, SAXException {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        String strPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(strPayload);

        Project xx = payload.getProjects().getProject()
                .stream()
                .filter(project -> project.getName().equalsIgnoreCase(nameProject))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid project name '" + "payload.xml" + '\''));

        final Set<Project.Group> groupName = new HashSet<>(xx.getGroup());

        Set<User> xx2 = payload.getUsers().getUser().stream()
                .filter(u -> !Collections.disjoint(groupName, u.getGroupRefs())) //Более формально, две коллекции не пересекаются, если у них нет общих элементов.
                .collect(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User::getValue).thenComparing(User::getEmail))));
        for (User us: xx2
             ) {
            System.out.println(us.getValue());
        }
    }
}
