package no.kristiania.questions;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QuestionAndAnswerDao {
    private final DataSource dataSource;

    public QuestionAndAnswerDao(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    public static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("pgr203.properties")) {
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty(
                "dataSource.url",
                "jdbc:postgresql://localhost:5432/product_db"));
        dataSource.setUser(properties.getProperty("dataSource.user","person_dbuser"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;

    }

    public void saveQuestionWithAnswer(QuestionAndAnswer questionAndAnswer) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into questionsAndAnswers(question,answer) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, questionAndAnswer.getAnsweredQuestion());
                statement.setString(2, questionAndAnswer.getQuestionAnswer());

                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    questionAndAnswer.setId(resultSet.getLong("qa_id"));
                }

            }
        }

    }


    public QuestionAndAnswer retrieveQuestionAndAnswerId(Long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questionsandanswers where qa_id  = ?"
            )) {
                statement.setLong(1,id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();

                    return fromResultSet(resultSet);
                }
            }
        }
    }


    public List<QuestionAndAnswer> listAllQuestionsWithAnswers() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questionsandanswers")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ArrayList<QuestionAndAnswer> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(fromResultSet(resultSet));
                    }
                    return result;
                }
            }
        }
    }

    private QuestionAndAnswer fromResultSet(ResultSet resultSet) throws SQLException {
        QuestionAndAnswer questionAndAnswer = new QuestionAndAnswer();
        questionAndAnswer.setId(resultSet.getLong("qa_id"));
        questionAndAnswer.setAnsweredQuestion((resultSet.getString("question")));
        questionAndAnswer.setQuestionAnswer((resultSet.getString("answer")));
        return questionAndAnswer;
    }


}
