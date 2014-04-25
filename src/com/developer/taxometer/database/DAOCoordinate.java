package com.developer.taxometer.database;

import com.developer.taxometer.models.Coordinate;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by developer on 26.03.14.
 */
public class DAOCoordinate extends BaseDaoImpl<Coordinate, Integer> {

    protected DAOCoordinate(ConnectionSource connectionSource,
                            Class<Coordinate> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Coordinate> getAllCoordinates() throws SQLException {
        return this.queryForAll();
    }
}
