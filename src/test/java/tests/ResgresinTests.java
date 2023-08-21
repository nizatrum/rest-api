package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;
import static org.hamcrest.core.Is.is;

public class ResgresinTests {

    @Test
    public void successfulLogin() {
        /*
        request: https://reqres.in/api/login
        data:
        {
            "email": "eve.holt@reqres.in",
            "password": "cityslicka"
        }
        response:
        {
            "token": "QpwL5tke4Pnpja7X4"
        }
        */
        String authorizedData = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";
        given()
                .body(authorizedData)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void unsuccessfulLogin() {
        /*
        request: https://reqres.in/api/login
        data:
        {
            "email": "peter@klaven",
        }
        response:
        {
            "error": "Missing password"
        }
        */
        String authorizedData = "{ \"email\": \"peter@klaven\" }";
        given()
                .body(authorizedData)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }


}
