import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;


public class DemoWebShopTest {

    String authCookie;

    @BeforeEach
    public void beforeEach() {
        authCookie = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("Email", "sakovetsdmytryi@gmail.com")
                .formParam("Password", "qwerty")
                .when()
                .post("http://demowebshop.tricentis.com/login")
                .then()
                .statusCode(302)
                .extract().cookie("NOPCOMMERCE.AUTH");

        System.out.println(authCookie.toString());


        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(
                new Cookie("NOPCOMMERCE.AUTH", authCookie));
    }

    @Test
    public void addPhoneCoverToCardTest() {

        Response addPhoneCoverToCard = given()
                .cookie(authCookie)
                .body("product_attribute_80_2_37=112&" +
                        "product_attribute_80_1_38=114&" +
                        "addtocart_80.EnteredQuantity=1")
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/31/1");

        addPhoneCoverToCard.then()
                .statusCode(200)
                .body("success", is(true));
        System.out.println(addPhoneCoverToCard.asString());
    }

    @Test
    public void subscribeWithEmailTest() {
        Response subscribeWithEmail =
                given()
                        .cookie(authCookie)
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("email=asd%40gmail.com")
                        .when()
                        .post("http://demowebshop.tricentis.com/subscribenewsletter");

        subscribeWithEmail.then()
                .statusCode(200)
                .body("Success", is(true))
                .body("Result", is("Thank you for signing up! A verification email has been sent. We appreciate your interest."));

        System.out.println(subscribeWithEmail.asString());
    }

    @Test
    public void emailFriendAboutItemTest() {
        Response emailFriendAboutItem = given()
                .cookie(authCookie)
                .body("FriendEmail=wq%40gmail.com&" +
                        "YourEmailAddress=qw%40gmail.com&" +
                        "PersonalMessage=hello+world&" +
                        "send-email=Send+email")
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post("http://demowebshop.tricentis.com/productemailafriend/72");

        emailFriendAboutItem.then()
                .statusCode(200);

        System.out.println(emailFriendAboutItem.asString());
    }

}
