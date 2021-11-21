import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.CreateNewUserModel;
import models.RegistrationModel;
import models.UserModelObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegresTestWithModelLombok {

    RegistrationModel registrationData = new RegistrationModel();

    public RegistrationModel getRegistrationData() {
        return registrationData;
    }

    public void setRegistrationData(RegistrationModel registrationData) {
        this.registrationData = registrationData;
    }

    CreateNewUserModel createNewUser = new CreateNewUserModel();

    public CreateNewUserModel getCreateNewUser() {
        return createNewUser;
    }

    public void setCreateNewUser(CreateNewUserModel createNewUser) {
        this.createNewUser = createNewUser;
    }

    @Test
    public void registerSuccessfulTest() {
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(registrationData)
                .when()
                .log().all()
                .post("https://reqres.in/api/register");

        System.out.println(response.asString());

        response.then()
                .log().all()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void registerUnsuccessfulTest() {
        registrationData.setEmail("eve.holt@reqres.in");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(registrationData)
                .when()
                .post("https://reqres.in/api/register");

        System.out.println(response.asString());

        response.then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    public void createNewUser() {
        createNewUser.setName("morpheus");
        createNewUser.setJob("leader");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(createNewUser)
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
    public void checkJobAfterCreatingNewUser() {
        String name = "morpheus";
        String job = "leader";
        createNewUser.setName("morpheus");
        createNewUser.setJob("leader");

        CreateNewUserModel checkJobAfterCreating = given()
                .contentType(ContentType.JSON)
                .body(createNewUser)
                .when()
                .log().all()
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .extract().as(CreateNewUserModel.class);

        assertEquals(name, checkJobAfterCreating.getName());
        assertEquals(job, checkJobAfterCreating.getJob());

    }

    @Test
    public void updateUserJob() {
        String job = "zion resident";
        createNewUser.setName("morpheus");
        createNewUser.setJob("zion resident");

        CreateNewUserModel updateUserJob = given()
                .contentType(ContentType.JSON)
                .body(createNewUser)
                .when()
                .log().all()
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .extract().as(CreateNewUserModel.class);

        assertEquals(job, updateUserJob.getJob());
    }

    @Test
    public void getSingleUser() {
        String text = "To keep ReqRes free, contributions towards server costs are appreciated!";
        UserModelObject userModelObject = given()
                .when()
                .log().all()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .log().all()
                .extract().as(UserModelObject.class);

        System.out.println(userModelObject.toString());
        assertEquals(text, userModelObject.getSupportModel().getText());
    }

    @Test
    public void checkListContainsFirstName() {
    List<String> list = given()
                .when()
                .log().all()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .path("data.findAll{it.first_name =~/say/}.first_name.flatten()");

        System.out.println(list);
    }

    @Test
    public void checkContainsFirstName() {
        given()
                .when()
                .log().all()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.findAll{it.first_name =~/say/}.first_name.flatten()",
                        hasItem("Lindsay"));
    }

    @Test
    public void checkContainsLastName() {
        given()
                .when()
                .log().all()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.findAll{it.last_name =~/ds/}.last_name.flatten()",
                        hasItems("Edwards" , "Fields"));
    }
}
