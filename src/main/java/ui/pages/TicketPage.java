package ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import ui.models.Ticket;

import static ui.models.Dictionaries.getPriority;
import static ui.models.Dictionaries.getQueue;

/**
 * Страница отдельного тикета (авторизированный пользователь)
 */
public class TicketPage extends HelpdeskBasePage {

    /* Верстка страницы может измениться, поэтому для таблиц вместо индексов строк и столбцов лучше использовать
       более универсальные локаторы, например поиск по тексту + parent, following-sibling и другие.

       Текст тоже может измениться, но в этом случае элемент не будет найден и тест упадет,
       а ошибку можно будет легко локализовать и исправить.
       В случае изменений ячеек таблицы, локатор будет продолжать работать, но будет указывать на другой элемент,
       поведение теста при этом изменится непредсказуемым образом и ошибку будет сложно найти. */
    private final WebElement dueDate = driver.findElement(By.xpath("//th[text()='Due Date']/following-sibling::td[1]"));
    private final WebElement title = driver.findElement(By.xpath("//h3"));
    private final WebElement queue = driver.findElement(By.xpath("//th[contains(text(), 'Queue:')]"));
    private final WebElement email = driver.findElement(By.xpath("//th[text()='Submitter E-Mail']/following-sibling::td[1]"));
    private final WebElement priority = driver.findElement(By.xpath("//th[text()='Priority']/following-sibling::td[1]"));
    private final WebElement description = driver.findElement(By.xpath("//h4[text()='Description']/following-sibling::p[1]"));

    private final WebElement attachFileButton = driver.findElement(By.xpath("//button[@id='ShowFileUpload']"));
    private final WebElement browseButton = driver.findElement(By.xpath("//input[@type='file']"));
    private final WebElement fileName = driver.findElement(By.xpath("//span[@id='selectedfilename0']"));
    private final WebElement updateThisTicketButton = driver.findElement(By.xpath("//button[@class='btn btn-primary float-right']"));

    JavascriptExecutor javascriptExecutor = ((JavascriptExecutor) driver);

    @Step("Проверить значение полей на странице тикета")
    public void checkTicket(Ticket ticket) {
        Assert.assertTrue(dueDate.getText().contains("July 9, 2022"), "Дата создания тикета не соответствует");
        Assert.assertTrue(title.getText().contains(ticket.getTitle()), "Имя тикета не соответствует");
        Assert.assertTrue(queue.getText().contains(getQueue(ticket.getQueue())), "Имя очереди не соответствует");
        Assert.assertTrue(email.getText().contains("admin@example.org"), "E-mail не соответствует");
        Assert.assertTrue(priority.getText().contains(getPriority(ticket.getPriority())), "Приоритет не соответствует");
        Assert.assertTrue(description.getText().contains(ticket.getDescription()), "Описание не соответствует");
        saveScreenshot();
    }

    @Step("Прикрепить файл к созданному тикету и обновить его")
    public void updateTicket(String path) {
        scroll();
        clickOnAttachFileButton();
        clickOnBrowseButton(path);
        Assert.assertTrue(fileName.getText().contains("CucumberTest.txt"), "Имя файла не соответствует");
        clickOnUpdateThisTicketButton();
    }

    @Step("Прокрутка страницы вниз")
    private void scroll() {
        javascriptExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    @Step("Нажать кнопку добавления файла")
    private void clickOnAttachFileButton() {
        this.attachFileButton.click();
        saveScreenshot();
    }

    @Step("Нажать кнопку выбора файла")
    private void clickOnBrowseButton(String path) {
        this.browseButton.sendKeys(path);
        saveScreenshot();
    }

    @Step("Нажать кнопку обновления тикета")
    private void clickOnUpdateThisTicketButton() {
        this.updateThisTicketButton.click();
        saveScreenshot();
    }
}
