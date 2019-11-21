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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;


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
        Set<User> user = new TreeSet<>() ;
        try (InputStream is = file.getInputStream()) {
            try {
                user = processByStax(is);
            } catch (XMLStreamException e) {
                e.printStackTrace();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        req.setAttribute("users", user);
        res.sendRedirect("/result");
    }


    private Set<User> processByStax(InputStream is) throws XMLStreamException, JAXBException {
         final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);
        StaxStreamProcessor processor = new StaxStreamProcessor(is);
        Set<User> users = new TreeSet<>(USER_COMPARATOR);
        JaxbParser parser = new JaxbParser(User.class);
            while (processor.startElement("User", "Users")) {
                User user = parser.unmarshal(processor.getReader(), User.class);
                users.add(user);
            }
            return users;
    }
    public static void main(String[] args) throws Exception {
        URL payloadUrl = Resources.getResource("payload.xml");
        InputStream is = payloadUrl.openStream();
        FileUploadServlet xxx = new FileUploadServlet();
        xxx.processByStax(is);
    }


}
