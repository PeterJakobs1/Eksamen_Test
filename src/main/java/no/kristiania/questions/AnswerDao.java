package no.kristiania.questions;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDao {

    private final DataSource dataSource;

    public AnswerDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveAnswer(Answer answer) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into answers (answer_name) values (?)",
                    Statement.RETURN_GENERATED_KEYS

            )) {
                statement.setString(1,answer.getAnswerName());
                statement.executeUpdate();

                try(ResultSet resultSet = statement.getGeneratedKeys()){
                    resultSet.next();
                    answer.setAnswerId(resultSet.getLong("answer_id"));
                }

            }
        }
    }

    public List<Answer> listAll() throws SQLException {
        try (Connection dataSourceConnection = dataSource.getConnection()) {
            try (PreparedStatement statement = dataSourceConnection.prepareStatement("select * from answers")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ArrayList<Answer> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(fromResultSet(resultSet));

                    }
                    return result;
                }
            }
        }
    }
    private Answer fromResultSet(ResultSet resultSet) throws SQLException {
        Answer answer = new Answer();
        answer.setAnswerId(resultSet.getLong("answer_id"));
        answer.setAnswerName((resultSet.getString("answer_name")));
        return answer;
    }

}
