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
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        try (InputStream is = file.getInputStream()) {
            try {
                processByStax(is);
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
    }


    private static void processByStax(InputStream is) throws XMLStreamException {



            StaxStreamProcessor processor = new StaxStreamProcessor(is);

            while (processor.startElement("User", "Users")) {
                String name;
                String flag;
                String email;

                flag = processor.getAttribute("flag");
                email = processor.getAttribute("email");
                name = processor.getText();
                System.out.println(name + " " + flag + " " +  email);

            }


    }
    public static void main(String[] args) throws Exception {
        URL payloadUrl = Resources.getResource("payload.xml");
        InputStream xxxx = payloadUrl.openStream();
        processByStax(xxxx);
    }


}
