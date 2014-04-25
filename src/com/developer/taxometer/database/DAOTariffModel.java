package com.developer.taxometer.database;


import com.developer.taxometer.models.TariffModel;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by developer on 26.03.14.
 */
public class DAOTariffModel extends BaseDaoImpl<TariffModel, Integer> {
    protected DAOTariffModel(ConnectionSource connectionSource, Class<TariffModel> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<TariffModel> getAllTariffModels() throws SQLException {
        return this.queryForAll();
    }

}
