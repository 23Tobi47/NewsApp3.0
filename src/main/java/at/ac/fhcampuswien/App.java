package at.ac.fhcampuswien;

import at.ac.fhcampuswien.exception.NewsApiException;
import at.ac.fhcampuswien.ui.Menu;

public class App {
    public static void main(String[] args) throws NewsApiException {
        Menu menu = new Menu();
        menu.start();
    }
}
