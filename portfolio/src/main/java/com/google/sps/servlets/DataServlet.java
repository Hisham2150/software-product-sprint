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

    private DatastoreService datastore;

    @Override
    public void init(){
    
        datastore =  DatastoreServiceFactory.getDatastoreService();
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
