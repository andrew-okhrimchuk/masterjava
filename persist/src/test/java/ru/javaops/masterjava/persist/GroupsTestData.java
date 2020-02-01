package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupsDao;
import ru.javaops.masterjava.persist.model.Groups;

import java.util.List;

import static ru.javaops.masterjava.persist.model.GroupFlag.*;
import static ru.javaops.masterjava.persist.ProjectTestData.*;


public class GroupsTestData {
    public static Groups topjava1;
    public static Groups topjava2;
    public static Groups topjava3;
    public static Groups groups1;
    public static Groups groups2;
    public static Groups groups3;
    public static List<Groups> FIST5_groups;

    public static void init() {
        ProjectTestData.init();
        ProjectTestData.setUp();

        topjava1 = new Groups("topjava1", registering, project1.getId());
        topjava2 = new Groups("topjava2", current, project2.getId());
        topjava3 = new Groups("topjava3", finished, project3.getId());
        groups1 = new Groups("Groups1",registering, project4.getId());
        groups2 = new Groups("Groups2",current, project5.getId());
        groups3 = new Groups("Groups3",finished, project6.getId());
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
