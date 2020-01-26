package ru.javaops.masterjava.upload;

import org.slf4j.Logger;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@WebServlet(urlPatterns = "", loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10) //10 MB in memory limit
public class UploadServlet2 extends HttpServlet {
    private static final Logger log = getLogger(UploadServlet2.class);
    private final UserProcessor userProcessor = new UserProcessor();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        engine.process("upload2", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        int count = chekCount(req.getParameter("countUsers"));

        try {
            if(count >= 0) {
//            http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html
                Part filePart = req.getPart("fileToUpload");
                if (filePart.getSize() == 0) {
                    throw new IllegalStateException("Upload file have not been selected");
                }
                try (InputStream is = filePart.getInputStream()) {
                    List<User> users = userProcessor.process(is);
                    webContext.setVariable("messages", "");
                    webContext.setVariable("users", users);
                    engine.process("result", webContext, resp.getWriter());
                }
            }
            else {
                webContext.setVariable("messages", "please insert correct number");
                engine.process("result", webContext, resp.getWriter());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            webContext.setVariable("exception", e);
            engine.process("exception", webContext, resp.getWriter());

        }
    }
    private int chekCount(String count){
        int count1 = -1;
        try {
            count1 = Integer.parseInt(count);
        }
        catch (NumberFormatException e){
            count1 = -1;
            log.error(e.getMessage());
        }
        if(count1 >= 0 && count1 <= 20) {
            return count1;
        }
        return -1;
    };
}
