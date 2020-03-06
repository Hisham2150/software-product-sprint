package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Comment;

@WebServlet("/list-comments")
public class ListCommentsServlet extends HttpServlet {

    private DatastoreService datastore;

    @Override
    public void init(){
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
   PreparedQuery commentsQuery = getComments("comment");
        List<Comment> commentsList = new ArrayList<Comment>();

        for(Entity commentEntity : commentsQuery.asIterable()){
            String body = (String) commentEntity.getProperty("body");
            long timestamp = (long) commentEntity.getProperty("timestamp");
            double sentimentScore = (double) commentEntity.getProperty("sentimentScore");
            Comment comment = new Comment(body,timestamp, sentimentScore);
            commentsList.add(comment);
        }

        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(commentsList));
    }

    public PreparedQuery getComments(String entityName){

        Query query = new Query(entityName).addSort("timestamp", SortDirection.DESCENDING);
        return datastore.prepare(query);
    }



}