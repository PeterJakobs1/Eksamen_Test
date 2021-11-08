package no.kristiania.http;

import no.kristiania.questions.AnswerDao;
import no.kristiania.questions.QuestionDao;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class QuestionServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        DataSource dataSource = createDataSource();
        AnswerDao answerDao = new AnswerDao(dataSource);
        QuestionDao questionDao = new QuestionDao(dataSource);
        HttpServer httpServer = new HttpServer(1984);
        httpServer.addController("/api/newAnswer",new AddAnswerController(answerDao));
        httpServer.addController("/api/answerOptions",new AnswerOptionsController(answerDao));
        httpServer.addController("/api/newQuestion",new AddQuestionController(questionDao));
        httpServer.addController("/api/questions",new ListQuestionsController(questionDao));
        httpServer.addController("/api/questions",new ListQuestionsController(questionDao,answerDao));
        httpServer.addController("/api/newQuestionsWithAnswers",new QuestionWithAnswerController(questionDao,answerDao));
        logger.info("Starting http://localhost:{}/index.html",httpServer.getPort());
    }

    private static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("pgr203.properties")) {
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty(
                "dataSource.url",
                "jdbc:postgresql://localhost:5432/question_db"));
        dataSource.setUser(properties.getProperty("dataSource.user","person_dbuser"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
