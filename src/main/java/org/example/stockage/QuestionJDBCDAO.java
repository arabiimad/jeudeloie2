package org.example.stockage;

import org.example.data.QuestionPOJO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC DAO for QuestionPOJO
 */
public class QuestionJDBCDAO implements DAO<QuestionPOJO> {

    @Override
    public Optional<QuestionPOJO> get(int id) throws DAOException {
        Optional<QuestionPOJO> QuestionPOJO = Optional.empty();
        try( Connection connection = DatabaseAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select question, answer from questions where id=?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                QuestionPOJO dto = new QuestionPOJO(resultSet.getString("question"), resultSet.getString("answer"));
                QuestionPOJO = Optional.of(dto);
            }
            resultSet.close();
        }
        catch (SQLException | DBAccessException e) {
            throw new DAOException("SELECT error", e);
        }
        return QuestionPOJO;
    }

    @Override
    public List<QuestionPOJO> getAll() throws DAOException {
        List<QuestionPOJO> questions = new ArrayList<>();
        try( Connection connection = DatabaseAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select id, question, answer from questions")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                QuestionPOJO dto = new QuestionPOJO(
                        resultSet.getString("question"),
                        resultSet.getString("answer"));
                questions.add(dto);
            }
            resultSet.close();
        }
        catch (SQLException | DBAccessException e) {
            throw new DAOException("SELECT error", e);
        }
        return questions;
    }

    @Override
    public int create(QuestionPOJO QuestionPOJO) throws DAOException {
        try( Connection connection = DatabaseAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into questions (question, answer) values(?,?)");
             PreparedStatement idRequest = connection.prepareStatement("call identity()")
             ) {
            preparedStatement.setString(1, QuestionPOJO.getQuestion());
            preparedStatement.setString(2, QuestionPOJO.getAnswer());
            preparedStatement.executeUpdate();

            ResultSet result = idRequest.executeQuery();
            result.next();
            return result.getInt(1);
        }
        catch (SQLException | DBAccessException e) {
            throw new DAOException("INSERT error", e);
        }
    }

    @Override
    public void delete(QuestionPOJO QuestionPOJO) throws DAOException {
        try( Connection connection = DatabaseAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("delete from questions where id=?")
        ) {
            preparedStatement.setLong(1, QuestionPOJO.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException | DBAccessException e) {
            throw new DAOException("DELETE error", e);
        }
    }

    @Override
    public void update(QuestionPOJO QuestionPOJO) throws DAOException {
        try( Connection connection = DatabaseAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("update questions set question=?, answer=? where id=?")
        ) {
            preparedStatement.setString(1, QuestionPOJO.getQuestion());
            preparedStatement.setString(2, QuestionPOJO.getAnswer());
            preparedStatement.setLong(3, QuestionPOJO.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException | DBAccessException e) {
            throw new DAOException("UPDATE error", e);
        }
    }
}
