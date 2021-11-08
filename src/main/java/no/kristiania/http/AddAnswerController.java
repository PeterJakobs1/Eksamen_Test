package no.kristiania.http;

import no.kristiania.questions.Answer;
import no.kristiania.questions.AnswerDao;


import java.sql.SQLException;
import java.util.Map;

public class AddAnswerController implements HttpController {
    private final AnswerDao answerDao;

    public AddAnswerController(AnswerDao answerDao) {

        this.answerDao = answerDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Answer aAnswer = new Answer();
        aAnswer.setAnswerName(queryMap.get("answerOption"));
        answerDao.saveAnswer(aAnswer);

        return new HttpMessage("HTTP/1.1 200 Ok","Done");
    }
}
