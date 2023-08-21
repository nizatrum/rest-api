package tests;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
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
}
