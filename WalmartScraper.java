import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//Jsoup is a java html parser. It is a java library that is used to parse HTML document.
//Jsoup provides api to extract and manipulate data from URL or HTML file. 
//It uses DOM, CSS and Jquery-like methods for extracting and manipulating file.
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WalmartScraper {

  public static void main(String[] args) {
    String url = "https://www.walmart.com/browse/electronics/dell-gaming-laptops/3944_3951_7052607_1849032_4519159";
    ArrayList<Product> products = new ArrayList<Product>();

    try {
      Document doc = Jsoup.connect(url).get();
      Elements productElements = doc.select(".search-result-gridview-items .search-result-gridview-item");

      for (Element productElement : productElements) {
        String productName = productElement.select(".search-result-product-title").text();
        String productUrl = "https://www.walmart.com" + productElement.select(".search-result-productimage a").attr("href");
        String productPrice = productElement.select(".search-result-productprice .price-group").text();

        products.add(new Product(productName, productUrl, productPrice));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Sort products by price
    products.sort((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));

    // Export top 10 products to CSV file
    try {
      FileWriter writer = new FileWriter("top_10_products.csv");
      writer.write("Name,URL,Price\n");

      for (int i = 0; i < 10 && i < products.size(); i++) {
        Product product = products.get(i);
        writer.write(product.getName() + "," + product.getUrl() + "," + product.getPrice() + "\n");
      }

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class Product {
    private String name;
    private String url;
    private double price;

    public Product(String name, String url, String price) {
      this.name = name;
      this.url = url;
      this.price = Double.parseDouble(price.replaceAll("[^\\d.]+", ""));
    }

    public String getName() {
      return name;
    }

    public String getUrl() {
      return url;
    }

    public double getPrice() {
      return price;
    }
  }

}

