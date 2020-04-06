package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.dao.MailDao;
import ru.javaops.masterjava.service.mail.model.Mail;

import java.io.*;
import java.util.List;
import java.util.Properties;

@Slf4j
public class MailSender {
    private static final MailDao cityDao = DBIProvider.getDao(MailDao.class);

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        Email err = new SimpleEmail();
        try {
            Email email = cratemail();
            email.setSubject(subject);

        if(to != null) {
            to.forEach(s -> {
                try {
                    email.addTo(s.getEmail(), s.getName());
                } catch (EmailException e) {
                    e.printStackTrace();
                }
            });
        }
        if(cc != null) {
            cc.forEach(s -> {
                try {
                    email.addCc(s.getEmail(), s.getName());
                } catch (EmailException e) {
                    e.printStackTrace();
                }
            });
        }
            email.setMsg(body);
            String mass = email.send();
            err = email;
        } catch (EmailException e) {
            e.printStackTrace();
            cityDao.insert(new Mail(err.getToAddresses(), e.toString()));

        }
    }

    private static Email cratemail () throws EmailException{

        Email email = new SimpleEmail();
        email.setHostName(System.getProperty("mail.host"));
        System.out.println(email.getHostName());
        email.setSmtpPort(Integer.valueOf(System.getProperty("mail.port")));
        email.setAuthenticator(new DefaultAuthenticator(System.getProperty("mail.username"), System.getProperty("mail.password")));
        email.setSSLOnConnect(Boolean.valueOf(System.getProperty("mail.useSSL")));
        email.setTLS(Boolean.valueOf(System.getProperty("mail.useTLS")));
        email.setFrom(System.getProperty("mail.fromName"));
        return email;
    }

    static {
        Properties p = new Properties();
        String pathToFile = "D:\\Stajirovka\\masterjava\\services\\mail-service\\src\\test\\java\\ru\\javaops\\masterjava\\service\\mail\\mail.conf";
        InputStream is = null;
        try {
            is = new FileInputStream(pathToFile);
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String name : p.stringPropertyNames()) {
            String value = p.getProperty(name);
            System.setProperty(name, value);
        }

        String propFile = "mail.conf";
        File file = new File(pathToFile);

        String path = file.getAbsolutePath();
        System.setProperty(propFile, path);

    }
}
