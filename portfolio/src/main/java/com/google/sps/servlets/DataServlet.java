// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private List<String> comments;
    private DatastoreService datastore;

    @Override
    public void init(){
        
        comments = new ArrayList<>();
        datastore =  DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
     
        PreparedQuery commentsQuery = getComments("comment");
        List<Comment> commentsList = new ArrayList<Comment>();

        for(Entity commentEntity : commentsQuery.asIterable()){
            String body = (String) commentEntity.getProperty("body");
            long timestamp = (long) commentEntity.getProperty("timestamp");
            Comment comment = new Comment(body,timestamp);
            commentsList.add(comment);
        }

        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(commentsList));
    }

   
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
       
        String commentBody = getParameter(request, "comment-input");
        long timestamp = System.currentTimeMillis();
        Entity commentEntity = createCommentEntity("comment", commentBody, timestamp);
        storeComment(commentEntity);

        response.setContentType("html/text");
        response.sendRedirect("/data");
    }

    public PreparedQuery getComments(String entityName){

        Query query = new Query(entityName).addSort("timestamp", SortDirection.DESCENDING);
        return datastore.prepare(query);
    }

    public String getParameter(HttpServletRequest request, String name){
       
        String value = request.getParameter(name);
        
        if(value == null){
          return "";
        }

        return value;
    }

    public Entity createCommentEntity(String entityName, String body, long timestamp){

        Entity commentEntity = new Entity(entityName);
        commentEntity.setProperty("body", body);
        commentEntity.setProperty("timestamp", timestamp);
        return commentEntity;
    }


    public void storeComment(Entity comment){
    
        datastore.put(comment);
    }

    //Adding to array
    public void addCommentArray(String text){
        if(text != ""){
          comments.add(text);
        }
    }

    //Printing comments from array
    public void printCommentsArray(HttpServletResponse response) throws IOException{
        for(String comment: comments){
            response.getWriter().println(comment);
        }
    }
}
