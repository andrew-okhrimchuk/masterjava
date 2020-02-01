package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;
import static ru.javaops.masterjava.persist.GroupsTestData.*;

import java.util.List;

public class ProjectTestData {
    public static Project project1;
    public static Project project2;
    public static Project project3;
    public static Project project4;
    public static Project project5;
    public static Project project6;
    public static List<Project> FIST5_projects;

    public static void init() {
        GroupsTestData.init();
        GroupsTestData.setUp();

        project1 = new Project("Project1", "description about Project1", groups1.getId() );
        project2 = new Project("Project2", "description about Project2", groups2.getId());
        project3 = new Project("Project3", "description about Project3", groups3.getId());
        project4 = new Project("Project4", "description about Project4", groups1.getId());
        project5 = new Project("Project5", "description about Project5", groups2.getId());
        project6 = new Project("Project6", "description about Project6", groups3.getId());
        FIST5_projects = ImmutableList.of(project1, project2,project3,project4,project5);
    }

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIST5_projects.forEach(dao::insert);
            dao.insert(project6);
        });
    }
}
