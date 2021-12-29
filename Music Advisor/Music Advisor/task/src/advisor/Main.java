package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.*;
import com.google.gson.*;

import java.util.*;

class spotify {
    private String ACCESS_POINT = "https://accounts.spotify.com";
    private String RESOURCE_POINT = "https://api.spotify.com";
    private String access_token;
    private String code = "";
    private HttpClient client = null;
    public boolean auth = false;

    final private Map<String, String> categories;
    private int pagesize = 5;
    final private List<String> viewList;
    private int currentpage = 1;

    spotify(){
        categories = new HashMap<>();
        client = HttpClient.newBuilder().build();
        viewList = new ArrayList<>();
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public boolean Authorization() throws IOException, InterruptedException {
        //configuring the server
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/",
                new HttpHandler() {
                    boolean got = false;
                    public void handle(HttpExchange exchange) throws IOException {
                        String notFound = "Authorization code not found. Try again.";
                        String succeed = "Got the code. Return back to your program.";
                        String query = exchange.getRequestURI().getQuery();
                        if (query != null && query.contains("code")){
                            got = true;
                            code = query;
                        }
                        String response = got ? succeed : notFound;
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes());
                        exchange.getResponseBody().close();
                    }
                }
        );

        //starting server and listening for the code
        server.start();
        System.out.println("use this link to request the access code:");
        System.out.println(ACCESS_POINT + "/authorize?client_id=53b07ec4d95147299c8d9d5f336ac096&redirect_uri=http://localhost:8080&response_type=code");
        System.out.println("waiting for code...");
        while(code.equals("")){
            Thread.sleep(1000);
        }
        System.out.println("code received");
        //code received, shut down server
        server.stop(0);

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(ACCESS_POINT + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code&" + code + "&redirect_uri=http://localhost:8080&client_id=d7a1319e65bf4295a0623eb3d97ef685&client_secret=ee361288c66243acbd7ee085811f5a5a"))
                .build();
        System.out.println("making http request for access_token...");
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println("raw token:" + response.body().toString());
        JsonObject jsonResponse = JsonParser.parseString(response.body().toString()).getAsJsonObject();
        access_token = jsonResponse.get("access_token").getAsString();
        //System.out.println("token: " + access_token);
        auth = true;

        System.out.println("---SUCCESS---");
        return true;
    }

    public void setACCESS_POINT(String ACCESS_POINT) {
        this.ACCESS_POINT = ACCESS_POINT;
    }

    private HttpRequest requestResource (String path){
       // System.out.println("token:" + access_token);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + access_token)
                .uri(URI.create(RESOURCE_POINT + path))
                .GET()
                .build();
        return httpRequest;
    }

    public void newReleases(){
        viewList.clear();
        int currentpage = 1;
        HttpRequest request = requestResource("/v1/browse/new-releases");
        try {
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body().toString()).getAsJsonObject();
            JsonArray albums = jsonResponse.get("albums").getAsJsonObject().get("items").getAsJsonArray();
            for (JsonElement item : albums) {
                String itemOfOutput = "";
                JsonObject release = item.getAsJsonObject();
                itemOfOutput += (release.get("name").getAsString() + '\n');
                itemOfOutput += ("[");
                JsonArray artists = release.get("artists").getAsJsonArray();
                int i = 0;
                for (var artist : artists) {
                    i++;
                    itemOfOutput += artist.getAsJsonObject().get("name").getAsString();
                    if (i < artists.size()) {
                        itemOfOutput += (", ");
                    }
                }
                itemOfOutput += ("]\n");
                itemOfOutput += (release.get("external_urls").getAsJsonObject().get("spotify").getAsString()) + '\n';
                viewList.add(itemOfOutput);
            }
        } catch (IOException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void featured(){
        this.viewList.clear();
        int currentpage = 1;
        HttpRequest request = requestResource("/v1/browse/featured-playlists");
        HttpResponse response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("featurelist raw:" + response.body().toString());
        JsonObject jsonResponse = JsonParser.parseString(response.body().toString()).getAsJsonObject();
        JsonArray albums = jsonResponse.get("playlists").getAsJsonObject().get("items").getAsJsonArray();

        for (JsonElement item : albums){
           // System.out.println(item.toString());

            JsonObject release = item.getAsJsonObject();
            viewList.add(release.get("name").getAsString().trim() + "\n" +
                release.get("external_urls").getAsJsonObject()
                .get("spotify").getAsString()  + "\n");
        }
        viewList.sort(String::compareTo);
        //viewList.sort((s1,s2) -> s2.compareTo(s1));
    }

    public void categories() throws IOException, InterruptedException {
        viewList.clear();
        int currentpage = 1;
        HttpRequest request = requestResource("/v1/browse/categories");
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonResponse = JsonParser.parseString(response.body().toString()).getAsJsonObject();
        JsonArray categories = jsonResponse.get("categories").getAsJsonObject().get("items").getAsJsonArray();


        for (var category : categories){
            viewList.add(category.getAsJsonObject().get("name").getAsString());
            this.categories.put(category.getAsJsonObject().get("name").getAsString(),
                    category.getAsJsonObject().get("id").getAsString());
        }
    }

    public boolean playlists(String C_NAME) throws IOException, InterruptedException {
        viewList.clear();
        int currentpage = 1;
        if (categories.isEmpty()){
            HttpRequest request = requestResource("/v1/browse/categories");
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body().toString()).getAsJsonObject();
            JsonArray categories = jsonResponse.get("categories").getAsJsonObject().get("items").getAsJsonArray();

            for (var category : categories){
                this.categories.put(category.getAsJsonObject().get("name").getAsString(),
                        category.getAsJsonObject().get("id").getAsString().trim());
            }
        }

        HttpRequest request = requestResource("/v1/browse/categories/"+categories.get(C_NAME)+"/playlists");
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonResponse = JsonParser.parseString(response.body().toString()).getAsJsonObject();
        if(response.body().toString().contains("error")){
            System.out.println(jsonResponse.get("error")
                    .getAsJsonObject().get("message").getAsString());
            return false;
        }
        JsonArray playlists = jsonResponse.getAsJsonObject("playlists").getAsJsonArray("items");
        for(var playlist : playlists){

            String itemOfOutput = "";
            itemOfOutput += (playlist.getAsJsonObject().get("name").getAsString() + '\n');
            itemOfOutput += (playlist.getAsJsonObject().get("external_urls")
                    .getAsJsonObject().get("spotify").getAsString());
            viewList.add(itemOfOutput);
        }

    return true;
    }

    public void setRESOURCE_POINT(String RESOURCE_POINT) {
        this.RESOURCE_POINT = RESOURCE_POINT;
    }

    void view(int pagechange){
        //System.out.println("size: " + viewList.size() + ", pagesize:" + this.pagesize);
        //viewList.sort(String::compareTo);
        if (pagechange < 0 && currentpage == 1 || pagechange > 0 && (currentpage >= (viewList.size() + pagesize - 1) / pagesize)) {
            System.out.println("No more pages.");
        } else {
            currentpage += pagechange;
           // System.out.printf("---PAGE %d OF %d---\n", currentpage, (viewList.size() + pagesize - 1) / pagesize);
            for (int i = pagesize * (currentpage - 1);
                 i < Math.min(pagesize * currentpage, viewList.size()); i++) {
                System.out.print(viewList.get(i));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("error");
                    e.printStackTrace();
                }
            }
            System.out.printf("---PAGE %d OF %d---\n", currentpage, (viewList.size() + pagesize - 1) / pagesize);
            System.out.println();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }
    }
}

public class Main {


    public static void main(String[] args) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("error");
            e.printStackTrace();
        }
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        List<String> argsList = List.of(args);

        spotify myspotify = new spotify();
        String input;
        if (argsList.contains("-access")){
            int index;
            if ((index = (argsList.indexOf("-access") + 1)) != argsList.size()){
                if (!argsList.get(index).startsWith("-")){
                    myspotify.setACCESS_POINT(argsList.get(index));
                }
            }
        }

        if (argsList.contains("-resource")){
            int index;
            if ((index = (argsList.indexOf("-resource") + 1)) != argsList.size()){
                if (!argsList.get(index).startsWith("-")){
                    myspotify.setRESOURCE_POINT(argsList.get(index));
                }
            }
        }
        if (argsList.contains("-page")){
            int index;
            if ((index = (argsList.indexOf("-page") + 1)) != argsList.size()){
                if (!argsList.get(index).startsWith("-")){
                    myspotify.setPagesize(Integer.parseInt(argsList.get(index)));
                }
            }
        }


        boolean loggedin = false;

        while (true) {
            input = scanner.nextLine();
            //System.out.println(input);
            if ("exit".equals(input)) {
                System.out.println("---GOODBYE!---");
                //break;
            } else if ("auth".equals(input)) {
                try {
                    loggedin = myspotify.Authorization();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else if ("featured".equals(input)) {
                if (loggedin) {
                    //System.out.println("---FEATURED---");
                    myspotify.featured();
                    myspotify.view(0);
                } else {
                    System.out.println("Please, provide access for application.");
                }
            } else if ("categories".equals(input)) {
                if (loggedin) {
                    System.out.println("---CATEGORIES---");
                    try {
                        myspotify.categories();
                        myspotify.view(0);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Please, provide access for application.");
                }
            } else if ("new".equals(input)) {
                if (loggedin) {
                    System.out.println("---NEW RELEASES---");
                    myspotify.newReleases();
                    myspotify.view(0);
                } else {
                    System.out.println("Please, provide access for application.");
                }
            } else if (input.startsWith("playlists ")) {
                if (loggedin) {
                    String selected = input.replaceAll("\\s+"," ")
                            .replaceAll("playlists ","");
                    System.out.println("---" + selected + " PLAYLISTS---");
                    try {
                        myspotify.playlists(selected);
                        myspotify.view(0);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Please, provide access for application.");
                }
            } else if (input.toLowerCase().startsWith("prev")) {
                myspotify.view(-1);
            } else if (input.toLowerCase().startsWith("next")) {
                myspotify.view(1);
            }

        }
/*
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("error");
            e.printStackTrace();
        }*/
    }
}
