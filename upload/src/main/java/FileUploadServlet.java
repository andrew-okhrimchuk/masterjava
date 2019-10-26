import com.google.common.base.Splitter;
import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.base.Strings.nullToEmpty;

@WebServlet("/upload")
@MultipartConfig//(location = "/tmp")
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/resources/templates/upload.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Part file = req.getPart("file");
        processByStax(file);
        /*try (StaxStreamProcessor processor = new StaxStreamProcessor(file.getInputStream())) {
            XMLStreamReader reader = processor.getReader();

            while (reader.hasNext()) {
                if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                    if ("User".equals(reader.getLocalName())) {

                        System.out.println(reader.getElementText());
                       // System.out.println(reader.getAttributeCount());
                        // System.out.println(reader.next());


                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }*/


    }


    private static void processByStax(Part payload)  {

        try (InputStream is = payload.getInputStream()) {
            StaxStreamProcessor processor = new StaxStreamProcessor(is);

            while (processor.startElement("User", "Users")) {
                System.out.println (processor.getText());
                System.out.print (" " + processor.getAttribute("flag"));
                System.out.print (" " + processor.getAttribute("email"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

}
