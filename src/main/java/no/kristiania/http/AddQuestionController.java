package no.kristiania.http;

import no.kristiania.questions.Question;
import no.kristiania.questions.QuestionDao;

import java.sql.SQLException;
import java.util.Map;

public class AddQuestionController implements HttpController {
    private final QuestionDao questionDao;

    public AddQuestionController(QuestionDao questionDao) {

        this.questionDao = questionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Question aQuestion = new Question();
        aQuestion.setQuestion(queryMap.get("questionInput"));
        questionDao.saveQuestion(aQuestion);

        return new HttpMessage("HTTP/1.1 200 Ok","Done");
    }
}
