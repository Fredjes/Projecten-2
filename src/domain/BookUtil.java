package domain;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class BookUtil {

    private static List<Book> books = new ArrayList<>();

    public static List<Book> searchIsbn(String isbn) {
        books.clear();
        if (!isbn.trim().isEmpty()) {
            String s = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn.trim().replace(" ", "");

            try {
                URL url = new URL(s);
                convertToBook(url);
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong" + e);
            }
        }
        return books;
    }

    public static List<Book> searchTitle(String title) {
        books.clear();
        if (!title.trim().isEmpty()) {
            String s = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + title.trim().replace(" ", "+");
            try {
                URL url = new URL(s);
                convertToBook(url);
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong" + e);
            }
        }
        return books;
    }

    private static void convertToBook(URL url) {
        try (InputStream in = url.openStream(); JsonReader reader = Json.createReader(in)) {
            JsonObject response = reader.readObject();
            JsonArray items = response.getJsonArray("items");

            if (items != null) {
                items.stream().filter(i -> i instanceof JsonObject).map(i -> (JsonObject) i).forEach(i -> {
                    JsonObject info = i.getJsonObject("volumeInfo");
                    List<String> genres = new ArrayList<>();
                    String authors = "";

                    if (info.getJsonArray("categories") != null) {
                        genres = info.getJsonArray("categories").stream().map(JsonValue::toString).collect(Collectors.toList());
                        for (int j = 0; j < genres.size(); j++) {
                            genres.set(j, genres.get(j).replaceAll("\"", "").trim());
                        }

                    } else if (info.getJsonArray("authors") != null) {
                        authors = info.getJsonArray("authors").stream().map(JsonValue::toString).reduce("", (e1, e2) -> e1 + " " + e2).trim().replace("\"", "");
                    }

                    Book b = new Book(genres, "", info.getString("title", ""), info.getString("description", ""), authors, info.getString("publisher", ""));
                    if (info.getJsonObject("imageLinks") != null) {
                        String img = info.getJsonObject("imageLinks").getString("thumbnail", "");
                        Image ii = new Image(img);
                        WritableImage img2 = new WritableImage(ii.getPixelReader(), (int) ii.getWidth(), (int) ii.getHeight());
                        b.setFXImage(img2);
                    }
                    books.add(b);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
