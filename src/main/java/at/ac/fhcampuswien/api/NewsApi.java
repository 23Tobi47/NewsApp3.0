package at.ac.fhcampuswien.api;

import at.ac.fhcampuswien.enums.*;
import at.ac.fhcampuswien.exception.NewsApiException;
import at.ac.fhcampuswien.models.NewsResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Objects;

public class NewsApi {
    public static final String DELIMITER = "&";
    private static final String URL = "https://newsapi.org/v2/%s?q=%s&apiKey=%s";
    private static final String API_KEY = Dotenv.load().get("API_TOKEN");   // read token from .env file -> add .env to .gitignore!!!
    private final OkHttpClient client;

    private Endpoint endpoint;
    private String q;
    private String qInTitle;
    private Country sourceCountry;
    private Category sourceCategory;
    private String domains;
    private String excludeDomains;
    private String from;
    private String to;
    private Language language;
    private SortBy sortBy;
    private String pageSize;
    private String page;


    public String getQ() {
        return q;
    }

    public String getqInTitle() {
        return qInTitle;
    }

    public Country getSourceCountry() {
        return sourceCountry;
    }

    public Category getSourceCategory() {
        return sourceCategory;
    }

    public String getDomains() {
        return domains;
    }

    public String getExcludeDomains() {
        return excludeDomains;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Language getLanguage() {
        return language;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getPage() {
        return page;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public NewsApi(String q, Endpoint endpoint){
        this.client = new OkHttpClient();
        this.q = q;
        this.endpoint = endpoint;
    }

    public NewsApi(String q, Country country, Endpoint endpoint){
        this.client = new OkHttpClient();
        this.q = q;
        this.sourceCountry = country;
        this.endpoint = endpoint;
    }

    public NewsApi(String q, String qInTitle, Country sourceCountry, Category sourceCategory, String domains, String excludeDomains, String from, String to, Language language, SortBy sortBy, String pageSize, String page, Endpoint endpoint) {
        this(q, endpoint);
        this.qInTitle = qInTitle;
        this.sourceCountry = sourceCountry;
        this.sourceCategory = sourceCategory;
        this.domains = domains;
        this.excludeDomains = excludeDomains;
        this.from = from;
        this.to = to;
        this.language = language;
        this.sortBy = sortBy;
        this.pageSize = pageSize;
        this.page = page;
    }

    private String buildUrl() throws NewsApiException {

        if (URL.equals("")) throw new NewsApiException ("Please provide a valid URL!");
        if (getEndpoint().getValue().equals("")) throw new NewsApiException ("Please provide an Endpoint!");
        if (getQ().equals("")) throw new NewsApiException ("Please provide a query!");
        if (API_KEY.equals("")) throw new NewsApiException ("Please provide an ApiKey, read the PDF on how to do it!");



        String baseurl = String.format(URL, getEndpoint().getValue(), getQ(), API_KEY);
        StringBuilder sb = new StringBuilder(baseurl);

        if(getFrom() != null){
            sb.append(DELIMITER).append("from=").append(getFrom());
        }
        if(getTo() != null){
            sb.append(DELIMITER).append("to=").append(getTo());
        }
        if(getPage() != null){
            sb.append(DELIMITER).append("page=").append(getPage());
        }
        if(getPageSize() != null){
            sb.append(DELIMITER).append("pageSize=").append(getPageSize());
        }
        if(getLanguage() != null){
            sb.append(DELIMITER).append("language=").append(getLanguage());
        }
        if(getSourceCountry() != null){
            sb.append(DELIMITER).append("country=").append(getSourceCountry());
        }
        if(getSourceCategory() != null){
            sb.append(DELIMITER).append("category=").append(getSourceCategory());
        }
        if(getDomains() != null){
            sb.append(DELIMITER).append("domains=").append(getDomains());
        }
        if(getExcludeDomains() != null){
            sb.append(DELIMITER).append("excludeDomains=").append(getExcludeDomains());
        }
        if(getqInTitle() != null){
            sb.append(DELIMITER).append("qInTitle=").append(getqInTitle());
        }
        if(getSortBy() != null){
            sb.append(DELIMITER).append("sortBy=").append(getSortBy());
        }
        return sb.toString();
    }

    public NewsResponse requestData() throws NewsApiException {
        String url = buildUrl();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {   // try with resources syntax
            Gson gson = new Gson();
            NewsResponse apiResponse = gson.fromJson(Objects.requireNonNull(response.body()).string(), NewsResponse.class); // parse the json response to NewsResponse
            if(apiResponse.getStatus().equals("ok")){   // http status code ok - 200
                return apiResponse;
            } else {
                System.err.println(this.getClass() + ": http status not ok");
                return null;
            }
        } catch (UnknownHostException ExceptionInternetConnection) {
            client.dispatcher().executorService().shutdown();
            System.out.println("Check your internet connection!");
            return null;
            // https://www.tabnine.com/code/java/methods/okhttp3.Dispatcher/executorService (29.05.2022)
            // When user has no internet connection
        } catch (JsonSyntaxException e){
            System.out.println("False Json Syntax");
            return null;
            // https://stackoverflow.com/questions/9455577/how-to-properly-handle-important-unchecked-exceptions
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
