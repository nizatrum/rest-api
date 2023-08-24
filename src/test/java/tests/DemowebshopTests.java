package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

public class DemowebshopTests {

    @Test
    public void addToCardAsNewUserTest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                .when()
                .post("https://demowebshop.tricentis.com/addproducttocart/details/72/1")
                .then().log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", containsString("The product has been added to your"))
                .body("updatetopcartsectionhtml", containsString("(1)"));
    }
    @Test
    public void addToCardTest() {
        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=1d6f4b5f-969a-41f7-aef2-ad0ecc284a46; " +
                        "ARRAffinity=d3bb6d387e9aac4a0990b4cb404955bda48daf72f33b52aeee5e3531e83248c7;")
                .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                .when()
                .post("https://demowebshop.tricentis.com/addproducttocart/details/72/1")
                .then().log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", containsString("The product has been added to your"))
                .extract().response();
//        Integer countCart = Integer.parseInt(response.path("updatetopcartsectionhtml")
//                .toString().replace("(",  "").replace(")", ""));
    }
}
