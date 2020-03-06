package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Comment;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

@WebServlet("/new-comment")
public class NewCommentServlet extends HttpServlet {

    private DatastoreService datastore;

    @Override
    public void init(){

        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String commentBody = request.getParameter("comment-input");
        long timestamp = System.currentTimeMillis();
        double sentimentScore = calculateSentimentScore(commentBody);
        Entity commentEntity = createCommentEntity("comment", commentBody, timestamp, sentimentScore);
        storeComment(commentEntity);
        response.sendRedirect("/index.html");
    }

    public Entity createCommentEntity(String entityName, String body, long timestamp, double sentimentScore){

        if(body == null || body.trim().length() == 0){
            return null;
        }
        
        Entity commentEntity = new Entity(entityName);
        commentEntity.setProperty("body", body);
        commentEntity.setProperty("timestamp", timestamp);
        commentEntity.setProperty("sentimentScore", sentimentScore);
        return commentEntity;
    }

    public void storeComment(Entity comment){
       
        if(comment != null){
            datastore.put(comment);
        }
    }


    public double calculateSentimentScore(String body) throws IOException{

        Document doc = Document.newBuilder().setContent(body).setType(Document.Type.PLAIN_TEXT).build();
        LanguageServiceClient languageService = LanguageServiceClient.create();
        Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
        double score = sentiment.getScore();
        languageService.close();
        return score;
    }


    
}