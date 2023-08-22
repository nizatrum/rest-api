package tests;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegresinTestsHomeWork {

    private final String URL = "https://reqres.in";

    @Test
    @DisplayName("checked all id as not null")
    public void getListResourcesTest() {
        Response resources = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL+"/api/unknown")
                .then()
                .extract().response();
        JsonPath jsonPath = resources.jsonPath();
        List<Integer> ids = jsonPath.getList("data.id");
        for (int i = 0; i < ids.size(); i++) {
            assertNotNull(ids.get(i));
        }
    }

    @Test
    @DisplayName("checked new user")
    public void postCreateUserTest() {
        String newUser = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post(URL+"/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("user update check")
    public void putUpdateUserTest() {
        String userForUpdate = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        given()
                .contentType(ContentType.JSON)
                .body(userForUpdate)
                .when()
                .put(URL+"/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("success user register")
    public void postSuccessfulUserRegisterTest() {
        String userForRegistration = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
        given()
                .contentType(ContentType.JSON)
                .body(userForRegistration)
                .when()
                .post(URL+"/api/register")
                .then()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("unsuccessful user register")
    public void postUnsuccessfulUserRegisterTest() {
        String userForRegistration = "{ \"email\": \"eve.holt@reqres.in\" }";
        given()
                .contentType(ContentType.JSON)
                .body(userForRegistration)
                .when()
                .post(URL+"/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
