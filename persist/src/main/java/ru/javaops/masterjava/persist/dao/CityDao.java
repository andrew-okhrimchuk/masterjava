package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.IntStreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    public City insert(City city) {
        if (city.isNew()) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            insertWitId(city);
        }
        return city;
    }

    @SqlQuery("SELECT nextval('city_seq')")
    abstract int getNextVal();


    @SqlUpdate("INSERT INTO city (name) VALUES (:name) ")
    @GetGeneratedKeys(columnName = "id")
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO city (id, name) VALUES (:id, :name) ")
    abstract void insertWitId(@BindBean City city);

    @SqlQuery("SELECT * FROM city ORDER BY name LIMIT :it")
    public abstract List<City> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE city CASCADE")
    @Override
    public abstract void clean();

    //    https://habrahabr.ru/post/264281/
    @SqlBatch("INSERT INTO city (name) VALUES (:name)" +
            "ON CONFLICT DO NOTHING")
//            "ON CONFLICT (email) DO UPDATE SET full_name=:fullName, flag=CAST(:flag AS USER_FLAG)")
    public abstract int[] insertBatch(@BindBean List<City> city, @BatchChunkSize int chunkSize);

    public List<String> insertAndGetConflictEmails(List<City> city) {
        int[] result = insertBatch(city, city.size());
        return IntStreamEx.range(0, city.size())
                .filter(i -> result[i] == 0)
                .mapToObj(index -> city.get(index).getName())
                .toList();
    }
}
