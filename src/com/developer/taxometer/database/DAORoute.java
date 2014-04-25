package com.developer.taxometer.database;

import com.developer.taxometer.models.Route;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by developer on 26.03.14.
 */
public class DAORoute extends BaseDaoImpl<Route, Integer> {

    protected DAORoute(ConnectionSource connectionSource,
                       Class<Route> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Route> getAllRoutes() throws SQLException {
        return this.queryForAll();
    }
}
