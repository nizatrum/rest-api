package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

public class SelenoidTests {
    // make to request to https://selenoid.autotests.cloud/status
    // total is 20
    @Test
    public void checkTotal() {
        given() // if this configured is empty can be removed
                .when()// if this configured is empty can be removed
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    public void checkTotalWithoutGiven() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    public void checkChromeVersion() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    public void checkTotalBadPractice() {
        String response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response().asString();
        System.out.println("Response " + response);
        String expectedResponse = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0,\"" +
                "browsers\":{\"android\":{\"8.1\":{}},\"chrome\":{\"100.0\":{},\"99.0\":{}},\"" +
                "chrome-mobile\":{\"86.0\":{}},\"" +
                "firefox\":{\"97.0\":{},\"98.0\":{}},\"opera\":{\"84.0\":{},\"85.0\":{}}}}\n";
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void checkTotalGoodPractice() {
        int response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract()
                .path("total");
        System.out.println("Response " + response);
        int expectedResponse = 20;
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void responseExamples() {
        Response response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response();
        System.out.println(response);
        System.out.println(response.toString());
        System.out.println(response.asString());
        System.out.println(response.path("total").toString());
        System.out.println(response.path("browsers.chrome").toString());
    }

    @Test
    public void checkStatus401() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);
    }

    @Test
    public void checkStatus200() {
        get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    public void checkStatus200WithAuth() {
        given()
                .auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }

}
