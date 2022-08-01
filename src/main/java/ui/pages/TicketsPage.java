package ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ui.models.Ticket;

import java.util.List;

/**
 * Страница с таблицей тикетов и фильтрами
 */
public class TicketsPage extends HelpdeskBasePage {

    // пример коллекции веб-элементов
    @FindBy(xpath = "//div[@class='tickettitle']/a")
    private List<WebElement> ticketsHref;

    @FindBy(xpath = "//span[contains(text(),'New Ticket')]")
    private WebElement newTicketButton;

    @FindBy(xpath = "//i[@class='fas fa-save']")
    private WebElement saveQueryField;

    @FindBy(id = "id_title")
    private WebElement queryName;

    @FindBy(xpath = "//input[@value='Save Query']")
    private WebElement saveQueryButton;

    public TicketsPage() {
        PageFactory.initElements(driver, this);
    }

    /**
     * Ищем строку с тикетом и нажимаем на нее
     *
     * @param ticket
     */
    @Step("Открыть тикет с id {ticket.id}")
    public void openTicket(Ticket ticket) {
        String id = String.valueOf(ticket.getId());
        ticketsHref.stream()
                .filter(WebElement::isDisplayed)
                .filter(ticketHref -> ticketHref.getText().startsWith(id))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Не найден тикет с id " + id))
                .click();
        saveScreenshot();
    }

    @Step("Нажать кнопку создания нового тикета")
    public void clickOnNewTicketButton() {
        newTicketButton.click();
        saveScreenshot();
    }

    @Step("Сохранение поиска")
    public void setQuery(Ticket ticket) {
        clickOnSaveQueryField();
        setQueryName(ticket.getTitle());
        clickOnSaveQueryButton();
    }

    @Step("Нажать кнопку поля сохранения поиска")
    private void clickOnSaveQueryField() {
        this.saveQueryField.click();
        saveScreenshot();
    }

    @Step("Ввести название сохранения поиска: {query}")
    private void setQueryName(String query) {
        this.queryName.sendKeys(query);
        saveScreenshot();
    }

    @Step("Нажать кнопку сохранения поиска")
    private void clickOnSaveQueryButton() {
        this.saveQueryButton.click();
        saveScreenshot();
    }
}
