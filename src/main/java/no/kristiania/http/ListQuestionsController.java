package no.kristiania.http;

import no.kristiania.questions.Answer;
import no.kristiania.questions.AnswerDao;
import no.kristiania.questions.Question;
import no.kristiania.questions.QuestionDao;

import java.sql.SQLException;

public class ListQuestionsController implements HttpController {
    private QuestionDao questionDao;
    private AnswerDao answerDao;

    public ListQuestionsController(QuestionDao questionDao) {
        this.questionDao = questionDao;

    }
    public ListQuestionsController(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String response = "";
        String answers = "";


        for (Question question : questionDao.listAll()) {
            int value = 1;
            response += "<div id=" + question.getId() + ">" + question.getQuestion() + "<select name='category' id='category'>" + answers +"</select>" + "</div>";
            answers = "";
            for (Answer answer : answerDao.listAll()) {

                answers +=  "<option value=" + (value++) + ">" + answer.getAnswerName() + "</option>" ;
            }
        }
        return new HttpMessage("HTTP/1.1 200 Ok",response);
    }
}
