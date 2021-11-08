package no.kristiania.http;

import no.kristiania.questions.AnswerDao;
import no.kristiania.questions.Question;
import no.kristiania.questions.QuestionDao;

import java.sql.SQLException;
import java.util.Map;

public class QuestionWithAnswerController implements HttpController{

    private QuestionDao questionDao;
    private AnswerDao answerDao;

    public QuestionWithAnswerController(QuestionDao questionDao, AnswerDao answerDao){

        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }



    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);

        for (Question question: questionDao.listAll()) {

        }



        Question aQuestion = new Question();
        aQuestion.setQuestion(queryMap.get("questionInput"));
        questionDao.saveQuestion(aQuestion);




        return new HttpMessage("HTTP/1.1 200 Ok","Svar lagt til i db");
    }
}
