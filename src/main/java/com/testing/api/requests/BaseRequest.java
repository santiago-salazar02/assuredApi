package com.testing.api.requests;

import com.testing.api.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {
    /**
     * This is a function to read elements using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     *
     * @return Response
     */
    protected Response requestGet(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                          .contentType(Constants.VALUE_CONTENT_TYPE)
                          .headers(headers)
                          .when()
                          .get(endpoint);
    }

    /**
     * This is a function to create a new element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @param body     model object
     *
     * @return Response
     */
    protected Response requestPost(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                          .contentType(Constants.VALUE_CONTENT_TYPE)
                          .headers(headers)
                          .body(body)
                          .when()
                          .post(endpoint);
    }

    /**
     * This is a function to update an element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @param body     model object
     *
     * @return Response
     */
    protected Response requestPut(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                          .contentType(Constants.VALUE_CONTENT_TYPE)
                          .headers(headers)
                          .body(body)
                          .when()
                          .put(endpoint);
    }

    /**
     * This is a function to delete an element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     *
     * @return Response
     */
    protected Response requestDelete(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                          .contentType(Constants.VALUE_CONTENT_TYPE)
                          .headers(headers)
                          .when()
                          .delete(endpoint);
    }

    /**
     * This is a function to create a default header map object
     *
     * @return default headers
     */
    protected Map<String, String> createBaseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
        return headers;
    }
}
