package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.Credentials;
import models.GenerateTokenResponse;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static listeners.CustomAllureListener.withCustomTemplates;
import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;


public class BookstoreTests {
    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI ="https://demoqa.com";
    }

    @Test
    public void getBooksTest() {
        get("/BookStore/v1/Books")
                .then()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    public void getBooksWithAllLogsTest() {
        given()
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .log().all()
                .body("books", hasSize(greaterThan(0)));
    }
    @Test
    public void generateTokenTest() {
        String data = "{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }";
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }

    @Test
    public void generateTokenWithAllureListenerTest() {
        String data = "{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }";
        //RestAssured.filters(new AllureRestAssured()); move to @BeforeAll
        given()
                .filter(new AllureRestAssured()) //or this
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }

    @Test
    public void generateTokenWithCustomAllureListenerTest() {
        String data = "{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }";
        given()
                .filter(withCustomTemplates()) //or this
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }

    @Test
    public void generateTokenJsonSchemeCheckTest() {
        String data = "{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }";
        given()
                .filter(withCustomTemplates()) //or this
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/generate_token_response_scheme.json"))
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }

    @Test
    public void generateTokenWithModelTest() {
        given()
                .filter(withCustomTemplates()) //or this
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(new Credentials("alex", "asdsad#frew_DFS2")) // this model
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/generate_token_response_scheme.json"))
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }

    @Test
    public void generateTokenWithAllModelTest() {
        GenerateTokenResponse response = given()
                .filter(withCustomTemplates()) //or this
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(new Credentials("alex", "asdsad#frew_DFS2")) // this model
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/generate_token_response_scheme.json"))
                .extract().as(GenerateTokenResponse.class);
        Assertions.assertEquals(169, response.getToken().length());
        Assertions.assertNotNull(response.getExpires());
        Assertions.assertEquals("Success", response.getStatus());
        Assertions.assertEquals("User authorized successfully.", response.getResult());
    }
}
