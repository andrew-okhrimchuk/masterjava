package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.GroupsTestData;
import ru.javaops.masterjava.persist.model.Groups;

import java.util.List;

import static ru.javaops.masterjava.persist.GroupsTestData.FIST5_groups;

public class GroupsDaoTest extends AbstractDaoTest<GroupsDao> {

    public GroupsDaoTest() {
        super(GroupsDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        GroupsTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        GroupsTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<Groups> groups = dao.getWithLimit(2);
        Assert.assertEquals(FIST5_groups, groups);
    }

    @Test
    public void insertBatch() throws Exception {
        dao.clean();
        dao.insertBatch(FIST5_groups, 2);
        Assert.assertEquals(2, dao.getWithLimit(100).size());
    }
}