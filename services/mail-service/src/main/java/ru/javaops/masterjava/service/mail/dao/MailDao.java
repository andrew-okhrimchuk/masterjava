package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.service.mail.model.Mail;

import java.util.Collection;
import java.util.List;


@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailDao implements AbstractDao {

    @SqlUpdate("TRUNCATE mail CASCADE ")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * FROM mail")
    public abstract List<Mail> getAll();

    @SqlUpdate("INSERT INTO mail (ref, name) VALUES (:ref, :name)")
    public abstract void insert(@BindBean Mail city);

    @SqlBatch("INSERT INTO mail (ref, name) VALUES (:ref, :name)")
    public abstract void insertBatch(@BindBean Collection<Mail> cities);
}
