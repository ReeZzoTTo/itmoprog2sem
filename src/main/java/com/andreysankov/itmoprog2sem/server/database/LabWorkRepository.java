package com.andreysankov.itmoprog2sem.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashSet;

import com.andreysankov.itmoprog2sem.common.models.Coordinates;
import com.andreysankov.itmoprog2sem.common.models.Difficulty;
import com.andreysankov.itmoprog2sem.common.models.Discipline;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class LabWorkRepository {
    private final DatabaseManager databaseManager;

    public LabWorkRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public LinkedHashSet<LabWork> loadCollection() throws SQLException {
        String sqlQuery = """
            SELECT * FROM labworks
            ORDER BY id
        """;

        try (
            Connection connection = databaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet querySet = statement.executeQuery()
        ) {
            LinkedHashSet<LabWork> collection = new LinkedHashSet<>();

            while (querySet.next()) {
                LabWork tempLabWork = getLabWorkFromTuple(querySet);
                collection.add(tempLabWork);
            }

            return collection;

        }
    }

    public long insertLabWork(LabWork labWork, String ownerLogin) throws SQLException {
        String sqlQuery = """
            INSERT INTO labworks (
                name,
                coordinates_x,
                coordinates_y,
                creation_date,
                minimal_point,
                personal_qualities_maximum,
                difficulty,
                discipline_name,
                discipline_lecture_hours,
                discipline_labs_count,
                owner_login
            ) VALUES (
                ?, ?, ?, ?, ?, ?, ?::difficulty_type, ?, ?, ?, ?
            ) RETURNING id
        """;

        try (
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            fillLabWorkToStatement(preparedStatement, labWork, ownerLogin);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }

                throw new SQLException("База данных не смогла вернуть id элемента");
            }
        } 
    }

    public boolean deleteElement(long elementId, String owner) throws SQLException {
        String sqlQuery = """
            DELETE FROM labworks 
            WHERE id = ?     
            AND owner_login = ?
        """;
        
        try (
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setLong(1, elementId);
            preparedStatement.setString(2, owner);

            return preparedStatement.executeUpdate() == 1;
        }
    }

    public int deleteLowerByOwner(int minimalPoint, String ownerLogin) throws SQLException {
        String sqlQuery = """
            DELETE FROM labworks
            WHERE owner_login = ?
            AND minimal_point < ?
        """;

        try (
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setString(1, ownerLogin);
            preparedStatement.setInt(2, minimalPoint);

            return preparedStatement.executeUpdate();
        }
    }

    public int deleteAllByOwner(String owner) throws SQLException {
        String sqlQuery = """
            DELETE FROM labworks
            WHERE owner_login = ?        
        """;
        try (
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setString(1, owner);

            return preparedStatement.executeUpdate();
        }
    }

    public boolean updateElement(LabWork element, long id, String owner) throws SQLException {
        String sqlQuery = """
            UPDATE labworks
            SET name = ?,
                coordinates_x = ?,
                coordinates_y = ?,
                creation_date = ?,
                minimal_point = ?,
                personal_qualities_maximum = ?,
                difficulty = ?::difficulty_type,
                discipline_name = ?,
                discipline_lecture_hours = ?,
                discipline_labs_count = ?
            WHERE owner_login = ? AND id = ?
        """;

        try (
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            fillLabWorkToStatement(preparedStatement, element, owner);

            preparedStatement.setLong(12, id);

            return preparedStatement.executeUpdate() == 1;
        }
    }

    private void fillLabWorkToStatement(
        PreparedStatement statement,
        LabWork labWork,
        String ownerLogin
    ) throws SQLException {
        statement.setString(1, labWork.getName());
        statement.setLong(2, labWork.getCoordinates().getX());
        statement.setInt(3, labWork.getCoordinates().getY());
        statement.setTimestamp(4, new Timestamp(labWork.getDate().getTime()));
        statement.setInt(5, labWork.getMinimalPoint());

        if (labWork.getPersonalQualitiesMaximum() == null) {
            statement.setNull(6, java.sql.Types.DOUBLE);
        } else {
            statement.setDouble(6, labWork.getPersonalQualitiesMaximum());
        }

        statement.setString(7, labWork.getDifficulty().name());
        
        if (labWork.getDiscipline() == null) {
            statement.setString(8, null);
            statement.setNull(9, java.sql.Types.BIGINT);
            statement.setNull(10, java.sql.Types.INTEGER);
        } else {
            statement.setString(8, labWork.getDiscipline().getName());
            statement.setLong(9, labWork.getDiscipline().getLectureHours());
            statement.setInt(10, labWork.getDiscipline().getLabsCount());
        }
        statement.setString(11, ownerLogin);
    }

    private LabWork getLabWorkFromTuple(ResultSet tableRow) throws SQLException {
        long id = tableRow.getLong("id");
        String name = tableRow.getString("name");
        
        Long coordinatesX = tableRow.getLong("coordinates_x");
        int coordinatesY = tableRow.getInt("coordinates_y");
        
        Coordinates coordinates = new Coordinates(
            coordinatesX,
            coordinatesY
        );

        Date creationDate = new Date(tableRow.getTimestamp("creation_date").getTime());
        int minimalPoint = tableRow.getInt("minimal_point");
        
        Double personalQualitiesMaximum = null;
        double rawPersonalQualitiesMaximum = tableRow.getDouble("personal_qualities_maximum");
        
        if (!tableRow.wasNull()) {
            personalQualitiesMaximum = rawPersonalQualitiesMaximum;
        }

        String difficultyStringType = tableRow.getString("difficulty");
        
        Difficulty difficulty = Difficulty.valueOf(difficultyStringType);

        Discipline discipline;

        String disciplineName = tableRow.getString("discipline_name");

        if (disciplineName == null) {
            discipline = null;
        } else {
            Long lectureHours = tableRow.getLong("discipline_lecture_hours");
            int labsCount = tableRow.getInt("discipline_labs_count");
        
            discipline = new Discipline(
                disciplineName,
                lectureHours,
                labsCount
            );
        }

        String ownerLogin = tableRow.getString("owner_login");

        LabWork resultLabWork = new LabWork(
            id,
            name,
            coordinates,
            creationDate,
            minimalPoint,
            personalQualitiesMaximum,
            difficulty,
            discipline
        );

        resultLabWork.setOwnerLogin(ownerLogin);

        return resultLabWork;
    }
}
