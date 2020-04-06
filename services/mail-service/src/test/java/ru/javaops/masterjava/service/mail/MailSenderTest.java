package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import org.junit.Test;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.persist.DBIProvider;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MailSenderTest {
    public static void initDBI() {
        Config db = Configs.getConfig("persist.conf","db");
        initDBI(db.getString("url"), db.getString("user"), db.getString("password"));
    }

    public static void initDBI(String dbUrl, String dbUser, String dbPassword) {
        DBIProvider.init(() -> {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("PostgreSQL driver not found", e);
            }
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        });
    }

    @Test
    public void sendMail() throws Exception {
        initDBI();
        List<Addressee> mails = new ArrayList<>();
        mails.add(new Addressee("andrew456@yandex.ru", "andrew1"));
        mails.add(new Addressee("a0972103356@gmail.com", "andrew2"));
        MailSender.sendMail(mails, null, "why", "test1");

    }

}