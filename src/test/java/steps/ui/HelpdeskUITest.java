package steps.ui;

import io.cucumber.java.ru.Затем;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import ui.elements.MainMenu;
import ui.models.Ticket;
import ui.pages.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HelpdeskUITest {

    private WebDriver driver;
    private Ticket ticket;

    @Когда("Откроем браузер chrome")
    public void setup() throws IOException {
        loadProperties();
        setupDriver();
    }

    @Step("Загрузить конфигурационные файлы")
    private void loadProperties() throws IOException {
        // Читаем конфигурационные файлы в System.properties
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("config.properties"));
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("user.properties"));
    }

    @Step("Создать экземпляр драйвера")
    private void setupDriver() {
        // Создание экземпляра драйвера
        driver = new ChromeDriver();
        // Устанавливаем размер окна браузера, как максимально возможный
        driver.manage().window().maximize();
        // Установим время ожидания для поиска элементов
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Установить созданный драйвер для поиска в веб-страницах
        AbstractPage.setDriver(driver);
    }

    @И("Переходим на сайт {string}")
    public void openHelpdeskMainPage(String url) {
        driver.get(url);
    }

    @И("Переходим на страницу авторизации")
    public void login() {
        ticket = buildNewTicket();
        MainMenu mainMenu = new MainMenu(driver);
        mainMenu.clickOnLogInButton();

        LoginPage loginPage = new LoginPage();
        loginPage.login(System.getProperty("user"), System.getProperty("password"));
        Assert.assertTrue(mainMenu.loginedUser().contains(ticket.getAssigned_to()), "Имя логина не совпадает");
    }

    @И("Создаем тикет")
    public void createTicket() {
        ticket = buildNewTicket();
        TicketsPage ticketsPage = new TicketsPage();
        ticketsPage.clickOnNewTicketButton();

        CreateTicketPage createTicketPage = new CreateTicketPage();
        createTicketPage.createTicket(ticket);
    }

    @И("Проверим созданный тикет")
    public void checkTicket() {
        ticket = buildNewTicket();
        TicketPage ticketPage = new TicketPage();
        ticketPage.checkTicket(ticket);
    }

    @Тогда("Обновим тикет")
    public void updateTicket() {
        ticket = buildNewTicket();
        TicketPage ticketPage = new TicketPage();
        ticketPage.updateTicket("C:\\CucumberTest.txt");
    }

    @И("Найдем созданный тикет")
    public void searchTicket() {
        MainMenu mainMenu = new MainMenu(driver);
        mainMenu.searchTicket(ticket);
    }

    @И("Сохраним историю поиска")
    public void saveQuery() {
        TicketsPage ticketsPage = new TicketsPage();
        ticketsPage.setQuery(ticket);
    }

    private Ticket buildNewTicket() {
        Ticket ticket = new Ticket();
        ticket.setDue_date("2022-07-09 12:00:00");
        ticket.setAssigned_to(System.getProperty("user"));
        ticket.setTitle("Cucumber UI test");
        ticket.setCreated("2022-07-09T11:18:34.467Z");
        ticket.setModified("2022-07-09T11:18:34.467Z");
        ticket.setStatus(2);
        ticket.setOn_hold(true);
        ticket.setDescription("someDescription");
        ticket.setResolution("someResolution");
        ticket.setPriority(1);
        ticket.setSecret_key("someSecretKey");
        ticket.setQueue(2);
        ticket.setKbitem(2);
        ticket.setMerged_to(2);
        return ticket;
    }

    @Затем("Закроем браузер chrome")
    public void close() {
        if (driver != null) {
            // Закрываем одно текущее окно браузера
            driver.close();
            // Закрываем все открытые окна браузера, завершаем работу браузера, освобождаем ресурсы
            driver.quit();
        }
    }
}
