package no.kristiania.http;

import no.kristiania.questions.Answer;
import no.kristiania.questions.AnswerDao;

import java.sql.SQLException;

public class AnswerOptionsController implements HttpController {
    private AnswerDao answerDao;

    public AnswerOptionsController(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String responseText = "";

        int value = 1;
        for (Answer answer : answerDao.listAll()) {
            responseText += "<option value=" + (value++) + ">" + answer + "</option>";
        }
        return new HttpMessage("Http/1.1 200 Ok", responseText);

    }
}
