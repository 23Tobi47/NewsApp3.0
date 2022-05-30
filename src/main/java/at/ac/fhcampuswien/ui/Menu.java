package at.ac.fhcampuswien.ui;

import at.ac.fhcampuswien.controllers.AppController;
import at.ac.fhcampuswien.downloader.ParallelDownloader;
import at.ac.fhcampuswien.downloader.SequentialDownloader;
import at.ac.fhcampuswien.exception.NewsApiException;
import at.ac.fhcampuswien.models.Article;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private static final String INVALID_INPUT_MESSAGE = "No valid input. Try again";
    private static final String EXIT_MESSAGE = "Bye bye!";
    private AppController controller;

    public void start() throws NewsApiException {
        String input;
        controller = new AppController();

        do{
            System.out.println(getMenuText());
            input = readLine();
            handleInput(input);
        } while(!input.equals("q"));

    }

    private void handleInput(String input) throws NewsApiException {
        switch (input) {
            case "a" -> getTopHeadlinesAustria(controller);
            case "b" -> getAllNewsBitcoin(controller);
            case "y" -> getArticleCount(controller);
            case "q" -> printExitMessage();
            case "h" -> downloadURLs();
            case "c" -> printSourceWithMostArticles(controller);
            case "d" -> printLongestAuthorName(controller);
            case "e" -> printAmountOfCNNArticles(controller);
            case "f" -> printHeadlinesUnder15(controller);
            case "g" -> printLongestDescription(controller);


            default -> printInvalidInputMessage();
        }
    }

    // Method is needed for exercise 4 - ignore for exercise 2 solution
    private void downloadURLs(){
        int resultSequential = controller.downloadURLs(new SequentialDownloader());
        // TODO print time in ms it took to download URLs sequentially

        // TODO implement the process() function in ParallelDownloader class
        int resultParallel = controller.downloadURLs(new ParallelDownloader());

        // TODO print time in ms it took to download URLs parallel
    }

    private void getArticleCount(AppController controller) {
        System.out.println("Number of articles: " + controller.getArticleCount());
    }

    private void printSourceWithMostArticles (AppController controller){
        try {
            System.out.println("Name of most used source: " + controller.printSourceWithMostArticles());
        } catch (NullPointerException exception) {
            System.out.println("Unable to find a source with more Articles than others!");
        }
    }

    private void printAmountOfCNNArticles(AppController controller){
        try {
            System.out.println("Number of articles: " + controller.printAmountOfCNNArticles());
        } catch (NullPointerException exception) {
            System.out.println("No Articles of CNN found!");
        }
    }

    public void printLongestAuthorName (AppController controller){
        try {
            System.out.println("Author with longest name: " + controller.printLongestAuthorName());
        } catch (NullPointerException exception) {
            System.out.println("Can not print the longest author name!");
        }
    }

    public void printHeadlinesUnder15(AppController controller){
        try {
            System.out.println("Headline with less than 15 characters: " + controller.printHeadlinesUnder15());
        } catch (NullPointerException exception) {
            System.out.println("No Title with less than 15 characters found!");
        }
    }

    public void printLongestDescription(AppController controller){
        try {
            System.out.println(controller.printLongestDescription());
        } catch (NullPointerException exception) {
            System.out.println("No Articles with descriptions found!");
        }
    }

    private void getTopHeadlinesAustria(AppController controller) throws NewsApiException {
        List<Article> articleList = controller.getTopHeadlinesAustria();

        for( Article a : articleList) {
            System.out.println(a);
        }

        try {
            System.out.println(controller.getTopHeadlinesAustria());
        } catch (NullPointerException e){
            System.out.println("There are no top headlines in Austria!");
        }
    }

    private void getAllNewsBitcoin(AppController controller) throws NewsApiException {

        try {
            System.out.println(controller.getAllNewsBitcoin());
        } catch (NullPointerException e){
            System.out.println("There are no news about bitcoin!");
        }
    }

    public static void printExitMessage(){
        System.out.println(EXIT_MESSAGE);
    }

    public static void printInvalidInputMessage(){
        System.out.println(INVALID_INPUT_MESSAGE);
    }
    
    private static String getMenuText(){
        return """
                *****************************
                *   Welcome to NewsApp   *
                *****************************
                Enter what you wanna do:
                a: Get top headlines austria
                b: Get all news about bitcoin
                y: Count articles
                q: Quit program
                c: Get provider with most articles
                d: Get longest author name
                e: Count articles from CNN
                f: Get articles with short title
                g: Sort articles by content length
                h: Download URLs
                """;
    }

    private static String readLine() {
        String value;
        Scanner scanner = new Scanner(System.in);
        value = scanner.nextLine();
        return value.trim();
    }

}
