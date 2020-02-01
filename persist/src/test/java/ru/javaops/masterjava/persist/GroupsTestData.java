package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupsDao;
import ru.javaops.masterjava.persist.model.Groups;

import java.util.List;

public class GroupsTestData {
    public static Groups REGISTERING;
    public static Groups CURRENT;
    public static Groups FINISHED;
    public static Groups groups1;
    public static Groups groups2;
    public static Groups groups3;
    public static List<Groups> FIST5_groups;

    public static void init() {
        REGISTERING = new Groups("REGISTERING");
        CURRENT = new Groups("CURRENT");
        FINISHED = new Groups("FINISHED");
        groups1 = new Groups("Groups1");
        groups2 = new Groups("Groups2");
        groups3 = new Groups("Groups3");
        FIST5_groups = ImmutableList.of(groups1, groups2);
    }

    public static void setUp() {
        GroupsDao dao = DBIProvider.getDao(GroupsDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIST5_groups.forEach(dao::insert);
            dao.insert(groups3);
        });
    }
}
