package ui.pages;

import ui.elements.MainMenu;

/** Элементы общие для системы Helpdesk */
public class HelpdeskBasePage extends AbstractPage {

    /** Доступ к элементам главного меню */
    public MainMenu mainMenu() {
        return new MainMenu(driver);
    }
}
