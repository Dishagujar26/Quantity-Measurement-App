package com.quantitymeasurementapp.repository;
import com.quantitymeasurementapp.entity.QuantityMeasurementEntity;
import com.quantitymeasurementapp.exception.DatabaseException;
import com.quantitymeasurementapp.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

	private static final String INSERT_SQL =
	        "INSERT INTO quantity_measurement (id, value, unit) VALUES (?, ?, ?)";

    private static final String FIND_SQL =
            "SELECT * FROM quantity_measurement WHERE id=?";

    @Override
    public void save(String key, QuantityMeasurementEntity entity) {

        Connection connection = null;

        try {

            connection = ConnectionPool.getConnection();

            PreparedStatement ps = connection.prepareStatement(INSERT_SQL);

            ps.setString(1, key);
            ps.setDouble(2, entity.getValue());
            ps.setString(3, entity.getUnit());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new DatabaseException("Error saving measurement", e);

        } finally {

            if (connection != null) {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    
    @Override
    public QuantityMeasurementEntity find(String key) {

        Connection connection = null;

        try {

            connection = ConnectionPool.getConnection();

            PreparedStatement ps = connection.prepareStatement(FIND_SQL);
            ps.setString(1, key);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                double value = rs.getDouble("value");
                String unit = rs.getString("unit");

                return new QuantityMeasurementEntity(value, unit);
            }

            return null;

        } catch (Exception e) {

            throw new DatabaseException("Error retrieving measurement", e);

        } finally {

            if (connection != null) {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
}
