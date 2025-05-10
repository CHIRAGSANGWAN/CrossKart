//package org.example.controller;
//
//import org.example.model.Product;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//public class AmazonController {
//
//    @GetMapping("/search")
//    public String getProductList(@RequestParam String query, Model model) {
//        List<Product> products = new ArrayList<>();
//
//        try {
//            String url = "https://www.amazon.in/s?k=" + query;
//            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
//                    .timeout(10000)
//                    .get();
//            Elements productElements = doc.select(".s-main-slot .s-result-item");
//            System.out.println("hi");
//            System.out.println(productElements.size());
//
//            for (Element product : productElements) {
//                Product p = new Product();
//                p.setName(product.select(".a-text-normal").text());
//                p.setBrand(product.select(".a-size-base-plus").text());
//                p.setImageUrl(product.select(".s-image").attr("src"));
//                p.setPrice(product.select(".a-price-whole").text());
//                p.setOriginalPrice(product.select(".a-price-symbol").text());
//                p.setDiscount(product.select(".a-price-symbol + .a-price-whole").text());
//                p.setRating(product.select(".a-icon-alt").text());
//                p.setProductUrl("https://www.amazon.in" + product.select(".a-link-normal").attr("href"));
//                System.out.println(p.getName());
//
//
//                products.add(p);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        model.addAttribute("products", products);
//        return "productList";
//    }
//}

//package org.example.controller;
//
//import org.example.model.Product;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//public class AmazonController {
//
//    @GetMapping("/search")
//    public String getProductList(@RequestParam String query, Model model) {
//        List<Product> products = new ArrayList<>();
//
//        try {
//            // The URL you want to scrape
//            String url = "https://www.amazon.in/s?k=" + query;
//
//            // Connect to the URL and get the page content
//            Document doc = Jsoup.connect(url)
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
//                    .timeout(10000)
//                    .get();
//
//            // Select all the products in the search result
//            Elements productElements = doc.select("div.s-main-slot div[data-component-type=s-search-result]");
//
//            // Iterate over each product element
//            for (Element product : productElements) {
//                Product p = new Product();
//
//                // Name of the product
//                p.setName(product.select("h2 a span").text());
//
//                // Brand (fallback if not available)
//                String brand = product.select("h5 span").text();
//                if (brand.isEmpty()) {
//                    brand = product.select("span.a-size-base-plus").first() != null
//                            ? product.select("span.a-size-base-plus").first().text()
//                            : "";
//                }
//                p.setBrand(brand);
//
//                // Image URL
//                p.setImageUrl(product.select("img.s-image").attr("src"));
//
//                // Price
//                p.setPrice(product.select("span.a-price span.a-offscreen").text());
//
//                // Original Price and Discount
//                p.setOriginalPrice(product.select("span.a-text-price span.a-offscreen").text());
//                p.setDiscount(product.select("span.a-letter-space + span.a-color-base").text());
//
//                // Rating
//                p.setRating(product.select("span.a-icon-alt").text());
//
//                // Product URL
//                String productLink = product.select("h2 a").attr("href");
//                p.setProductUrl("https://www.amazon.in" + productLink);
//
//                products.add(p);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Add the list of products to the model
//        model.addAttribute("products", products);
//
//        // Return the view name for the product list
//        return "productList";
//    }
//}

package org.example.controller;

import org.example.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AmazonController {

    @GetMapping("/search")
    public String getProductList(@RequestParam String query, Model model) {
        List<Product> products = new ArrayList<>();

        try {
            for(int page=1;page<=10;page++) {
                // The URL you want to scrape
                String url = "https://www.amazon.in/s?k=" + query+ "&page=" + page;;
                System.out.println(page);

                // Connect to the URL and get the page content
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                        .timeout(10000)
                        .get();

                // Select all the products in the search result
                Elements productElements = doc.select("div.s-main-slot div[data-component-type=s-search-result]");

                // Iterate over each product element
                for (Element product : productElements) {
                    Product p = new Product();
                    String name = product.select("h2.a-size-base-plus.a-spacing-none.a-color-base.a-text-normal span").text();
                    p.setName(name);
                    //System.out.println(p.getName()+" "+"hello");

                    // Brand (some products have the brand in a different element, like h5 or span)
                    String brand = product.select("h5 span").text();
                    if (brand.isEmpty()) {
                        brand = product.select("span.a-size-base-plus").first() != null
                                ? product.select("span.a-size-base-plus").first().text()
                                : "";
                    }
                    p.setBrand(brand);

                    // Image URL
                    p.setImageUrl(product.select("img.s-image").attr("src"));

                    String price=product.select(".a-price-whole").text();
                    p.setPrice("\u20B9" + product.select(".a-price-whole").text());

                    if(price.trim().contains(" ")||price.isEmpty()){
                        continue;
                        //System.out.println(price+" abcjsbdiuebv"+" "+product.select("span.a-price span.a-offscreen").text());
                    }

                    // Original Price (often available in a different element if it's crossed out)
                    String originalPrice = product.select("span.a-text-price span.a-offscreen").text();
                    p.setOriginalPrice(originalPrice);
                    if(originalPrice.trim().contains(" ")||originalPrice.isEmpty()){
                        continue;
                    }
//                    System.out.println(p.getOriginalPrice().substring(1).replace(",", ""));
//                    Integer.parseInt(p.getPrice().substring(1).replace(",", ""));

                    // Discount (may be available in a sibling span or a different element)

                    try{
                    int discount = (Integer.parseInt(p.getOriginalPrice().substring(1).replace(",", "")) - Integer.parseInt(p.getPrice().substring(1).replace(",", ""))) * 100 / Integer.parseInt(p.getOriginalPrice().substring(1).replace(",", ""));
                    p.setDiscount(discount + "%");}catch (Exception e){
                        System.out.println(e);
                        continue;
                    }

                    // Rating (may not always be available)
                    p.setRating(product.select("span.a-icon-alt").text());

                    // Product URL (the relative link to the product page)
                    p.setProductUrl("https://www.amazon.in" + product.select(".a-link-normal").attr("href"));

                    // Add the product to the list
                    Element sponsored = product.selectFirst("span.a-color-base[aria-hidden=true]");
                    if (sponsored != null && "Sponsored".equals(sponsored.text())) {
                        System.out.println(p.getName());
                        System.out.println(p.getBrand());
                        continue;
                    }
                    products.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the list of products to the model
        model.addAttribute("products", products);

        // Return the view name for the product list
        return "productList";
    }
}

