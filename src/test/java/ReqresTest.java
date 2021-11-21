import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

public class ReqresTest {

    @Test
    public void getSingleUserTest() {
        get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void getSingleUserIdTest() {
        Integer userId = get("https://reqres.in/api/users/2")
                .then()
                .extract()
                .path("data.id");

        System.out.println(userId.toString());

        assertThat(userId).isEqualTo(2);
    }

    @Test
    public void getSingleUserEmailTest() {
        String email = get("https://reqres.in/api/users/2")
                .then()
                .extract()
                .path("data.email");

        System.out.println(email.toString());

        assertThat(email).isEqualTo("janet.weaver@reqres.in");
    }

    @Test
    public void getSingleUserNotFound() {
        Response response = get("https://reqres.in/api/users/23");
        response.then().statusCode(404);
    }

    @Test
    public void createNewUser() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"morpheus\"," +
                        " \"job\": \"leader\" }") //bad practise to send body of request like this
                .when()
                .post("https://reqres.in/api/users");

        System.out.println(response.asString());

        response
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    public void deleteUserTest() {
        delete("https://reqres.in/api/users/{userId}", 9)
                .then()
                .statusCode(204);
    }

    @Test
    public void registerSuccessfulTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"email\": \"eve.holt@reqres.in\", " +
                        "\"password\": \"pistol\" }") //bad practise to send body of request like this
                .when()
                .post("https://reqres.in/api/register");

        System.out.println(response.asString());

        response.then()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void registerUnsuccessfulTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"sydney@fife\" }") //bad practise to send body of request like this
                .when()
                .post("https://reqres.in/api/register");

        System.out.println(response.asString());

        response.then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    public void getSingleResourceNotFound() {
        get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404);
    }

}
