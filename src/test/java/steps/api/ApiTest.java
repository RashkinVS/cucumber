package steps.api;

import api.AuthToken;
import api.Ticket;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class ApiTest {

    private String baseUrl;
    private String user;
    private String password;

    @Дано("Загрузим предварительные данные")
    public void prepare() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("config.properties"));
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("user.properties"));
        baseUrl = System.getProperty("site.url");
        user = System.getProperty("user");
        password = System.getProperty("password");
    }

    @Step("Зададим начальные данные для создания тикета")
    private Ticket buildNewTicket(int status) {
        Ticket newTicket = new Ticket();
        newTicket.setDue_date("2022-07-09");
        newTicket.setAssigned_to(System.getProperty("user"));
        newTicket.setTitle("Cucumber API test");
        newTicket.setCreated("2022-07-09T11:18:34.467Z");
        newTicket.setModified("2022-07-09T11:18:34.467Z");
        newTicket.setSubmitter_email("user@example.com");
        newTicket.setStatus(status);
        newTicket.setOn_hold(true);
        newTicket.setDescription("someDescription");
        newTicket.setResolution("someResolution");
        newTicket.setPriority(1);
        newTicket.setSecret_key("someSecretKey");
        newTicket.setQueue(2);
        newTicket.setKbitem(2);
        newTicket.setMerged_to(2);
        return newTicket;
    }

    @Step("Создадим тикет")
    private Ticket createTicket(Ticket ticket) {
        return given()
                .baseUri(baseUrl)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(ticket)
                .log().all()
                .when()
                .post("/api/tickets")
                .then()
                .statusCode(201)
                .extract().body().as(Ticket.class);
    }

    @И("Получим токен авторизации")
    public AuthToken login() {
        AuthToken login = new AuthToken();
        login.setUsername(user);
        login.setPassword(password);
        return given()
                .baseUri(baseUrl)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(login)
                .log().all()
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().body().as(AuthToken.class);
    }

    @И("Создадим тикет с высоким приоритетом и статусом открыт")
    public void createOpenTicket() {
        createTicket(buildNewTicket(1));
    }

    @И("Создадим тикет с высоким приоритетом и статусом закрыт и обновим статус тикета на статус открыт")
    public void createCloseTicket() {
        Ticket closeTicket = createTicket(buildNewTicket(4));
        updateTicketNegative(closeTicket);
    }

    @Step("Обновим статус тикета")
    private void updateTicketNegative(Ticket ticket) {
        Ticket newTicket = new Ticket();
        newTicket.setStatus(1);
        given()
                .baseUri(baseUrl)
                .header("Authorization", "Token " + login().getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newTicket)
                .log().all()
                .pathParam("id", ticket.getId())
                .when()
                .patch("/api/tickets/{id}")
                .then()
                .statusCode(422);
    }
}