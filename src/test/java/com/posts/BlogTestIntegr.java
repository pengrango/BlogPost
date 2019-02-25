package com.posts;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.client.HttpClient;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class BlogTestIntegr {

    private static final String POST_1 = "{\"id\":\"1\",\"title\":\"First title\",\"content\":\"First content\"}";
    private static final String POST_1_V2 = "{\"id\":\"1\",\"title\":\"First title\",\"content\":\"First content, version 2\"}";
    private static final String POST_2 = "{\"id\":\"2\",\"title\":\"Second title\",\"content\":\"Second content\"}";
    private static final String Invalid_POST = "{\"title\":\"First title\",\"content\":\"First content\"}";
    private static final String POSTS_URI = "http://localhost:8080/blog-web/posts/";


    public BlogTestIntegr() {
    }


    @Test
    public void testGetBlogWithNoPosts() throws IOException {
        HttpResponse httpResponse = get(POSTS_URI);
        assertEquals("[]", getResponseEntity(httpResponse));
    }

    @Test
    public void testAddPosts() throws IOException {

        HttpResponse response = post(POSTS_URI, POST_1);
        assertEquals(POSTS_URI + "1", getLocation(response));
        assertEquals(201, getStatusCode(response));

        response = post(POSTS_URI, POST_2);
        assertEquals(POSTS_URI + "2", getLocation(response));
        assertEquals(201, getStatusCode(response));

        //after
        delete(POSTS_URI + "1");
        delete(POSTS_URI + "2");
    }

    @Test
    public void testAddInvalidPosts() throws IOException {
        HttpResponse response = post(POSTS_URI, Invalid_POST);
        assertEquals(405, getStatusCode(response));
    }

    @Test
    public void testGetPost() throws IOException {
        //before
        post(POSTS_URI, POST_1);

        HttpResponse response = get(POSTS_URI + "1");
        assertEquals(200, getStatusCode(response));
        assertEquals(POST_1, getResponseEntity(response));

        //after
        delete(POSTS_URI + "1");
    }

    @Test
    public void testGetInvalidPost() throws IOException {
        HttpResponse response = get(POSTS_URI + "100");
        assertEquals(204, getStatusCode(response) );
    }

    @Test
    public void testGetAllPosts() throws IOException {
        //before
        post(POSTS_URI, POST_1);
        post(POSTS_URI, POST_2);

        HttpResponse response = get(POSTS_URI);
        assertEquals("[" + POST_1 + "," + POST_2 + "]", getResponseEntity(response));
        assertEquals(200, getStatusCode(response));

        //after
        delete(POSTS_URI + "1");
        delete(POSTS_URI + "2");
    }

    @Test
    public void testDeleteAPost() throws IOException {
        //before
        post(POSTS_URI, POST_1);

        HttpResponse response = delete(POSTS_URI + "1");
        assertEquals(200, getStatusCode(response));

        // Should now be gone
        response = get(POSTS_URI + "1");
        assertEquals(204, getStatusCode(response));
    }

    @Test
    public void testDeletePostNotExisted() throws IOException {
        HttpResponse response = delete(POSTS_URI + "100");
        assertEquals(404, getStatusCode(response));
    }

    @Test
    public void testUpdatePost() throws IOException {
        //before
        HttpResponse response = post(POSTS_URI, POST_1);
        assertEquals(201, getStatusCode(response));

        response = put(POSTS_URI, POST_1_V2);
        assertEquals(201, getStatusCode(response));

        //after
        delete(POSTS_URI + 1);
    }

    @Test
    public void testUpdateNonExistingPost() throws IOException {


        HttpResponse response = put(POSTS_URI, POST_2);
        assertEquals(404, getStatusCode(response));

    }

    @Test
    public void testUpdateInvalidPost() throws IOException {
        //before
        HttpResponse response = post(POSTS_URI, POST_1);
        assertEquals(201, getStatusCode(response));

        response = put(POSTS_URI, Invalid_POST);
        assertEquals(405, getStatusCode(response));

        //after
        response = delete(POSTS_URI + 1);
        assertEquals(200, getStatusCode(response));
    }



    private HttpResponse post(String url, String entity) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(entity, "utf-8"));
        HttpResponse response = client.execute(request);
        return response;

    }

    private HttpResponse get(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");
        HttpResponse response = client.execute(request);
        return response;

    }

    private HttpResponse delete(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(url);
        request.setHeader("Accept", "application/json");
        HttpResponse response = client.execute(request);
        return response;
    }

    private HttpResponse put(String url, String entity) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(url);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(entity, "utf-8"));
        HttpResponse response = client.execute(request);
        return response;
    }

    private int getStatusCode(HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    private String getResponseEntity(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    private String getLocation(HttpResponse response) {
        return response.getLastHeader("Location").getValue();
    }

}
