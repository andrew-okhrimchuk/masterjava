package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Groups;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupsDao implements AbstractDao {

    public Groups insert(Groups groups) {
        if (groups.isNew()) {
            int id = insertGeneratedId(groups);
            groups.setId(id);
        } else {
            insertWitId(groups);
        }
        return groups;
    }

    @SqlQuery("SELECT nextval('groups_seq')")
    abstract int getNextVal();


    @SqlUpdate("INSERT INTO groups (name, flag, project_id) VALUES (:name, CAST(:flag AS groups_flag), :project_id) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Groups groups);

    @SqlUpdate("INSERT INTO groups (id, name, flag, project_id) VALUES (:id, :name, CAST(:flag AS groups_flag), :project_id) ")
    abstract void insertWitId(@BindBean Groups groups);

    @SqlQuery("SELECT * FROM groups ORDER BY name LIMIT :it")
    public abstract List<Groups> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE groups CASCADE")
    @Override
    public abstract void clean();

    //    https://habrahabr.ru/post/264281/
    @SqlBatch("INSERT INTO groups (id, name, flag, project_id) VALUES (:id, :name, CAST(:flag AS groups_flag), :project_id)" +
            "ON CONFLICT DO NOTHING")
//            "ON CONFLICT (email) DO UPDATE SET full_name=:fullName, flag=CAST(:flag AS USER_FLAG)")
    public abstract int[] insertBatch(@BindBean List<Groups> groups, @BatchChunkSize int chunkSize);

}
