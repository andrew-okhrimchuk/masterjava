package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {
    public static City KIEV;
    public static City MOSCOV;
    public static City LVIV;
    public static City CITY1;
    public static City CITY2;
    public static City CITY3;
    public static List<City> FIST5_CITY;

    public static void init() {
        KIEV = new City("Kiev");
        MOSCOV = new City("Moscov");
        LVIV = new City("Lviv");
        CITY1 = new City("City1");
        CITY2 = new City("City2");
        CITY3 = new City("City3");
        FIST5_CITY = ImmutableList.of(CITY1, CITY2, CITY3, KIEV, LVIV);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIST5_CITY.forEach(dao::insert);
            dao.insert(MOSCOV);
        });
    }
}
