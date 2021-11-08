package no.kristiania.http;

import no.kristiania.questions.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {

    private final HttpServer server = new HttpServer(0);

    HttpServerTest() throws IOException {

    }

    @Test
    void shouldCreateAndServeFile() throws IOException {
        HttpServer server = new HttpServer(10004);
        Paths.get("target/test-classes");

        String fileContent = "A file was created by @TEST shouldCreateAndServeFile";
        Files.write(Paths.get("target/test-classes/test-file.txt"),fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/test-file.txt");
        assertEquals(fileContent, client.getMessageBody());
    }

    @Test
    void shouldReturnAnswerFromServer() throws IOException, SQLException {
        AnswerDao answerDao = new AnswerDao((TestData.testDataSource()));
        Answer firstAnswer = AnswerDaoTest.exampleAnswer();
        answerDao.saveAnswer(firstAnswer);
        Answer secondAnswer = AnswerDaoTest.exampleAnswer();
        answerDao.saveAnswer(secondAnswer);

        server.addController("/api/answerOptions",new AnswerOptionsController(answerDao));

        HttpClient client = new HttpClient("localhost", server.getPort(),"/api/answerOptions");
        assertThat(client.getMessageBody())
                .contains(firstAnswer.getAnswerName())
                .contains(secondAnswer.getAnswerName());
    }

    @Test
    void shouldListQuestionsFromDatabase() throws SQLException, IOException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());

        Question firstQuestion = QuestionDaoTest.exampleQuestion();
        questionDao.saveQuestion(firstQuestion);
        Question secondQuestion = QuestionDaoTest.exampleQuestion();
        questionDao.saveQuestion(secondQuestion);

        server.addController("/api/questions", new ListQuestionsController(questionDao));

        HttpClient client = new HttpClient("localhost",server.getPort(),"/api/questions");
        assertThat(client.getMessageBody())
                .contains(firstQuestion.getQuestion())
                .contains(secondQuestion.getQuestion());

    }

    @Test
    void shouldCreateNewItem() throws IOException, SQLException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        server.addController("/api/newQuestion",new AddQuestionController(questionDao));


        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestion",
                "questionInput=goldfish"
        );
        assertEquals(200, postClient.getStatusCode());
        assertThat(questionDao.listAll())
                .anySatisfy(p -> {
                    assertThat(p.getQuestion()).isEqualTo("goldfish");
                });
        Question question = questionDao.listAll().get(0);
        assertEquals("goldfish", question.getQuestion());
    }

}