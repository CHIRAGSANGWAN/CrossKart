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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class FlipKartController {
    ////<div class="gqcSqV YGE0gZ" style="padding-top:120.00%"><img class="_53J4C-" alt="" src="https://rukminim2.flixcart.com/image/612/612/xif0q/shoe/c/h/t/-original-imah4j6wpqs7cfma.jpeg?q=70"/></div></div></div></div><div class="jt22zr" style="bottom:4px"></div><div class="oUss6M D05kjV gREhVj"><div class="+7E521"><svg xmlns="http://www.w3.org/2000/svg" class="N1bADF" width="28" height="28" viewBox="0 0 20 16"><path d="M8.695 16.682C4.06 12.382 1 9.536 1 6.065 1 3.219 3.178 1 5.95 1c1.566 0 3.069.746 4.05 1.915C10.981 1.745 12.484 1 14.05 1 16.822 1 19 3.22 19 6.065c0 3.471-3.06 6.316-7.695 10.617L10 17.897l-1.305-1.215z" fill="#2874F0" class="x1UMqG" stroke="#FFF" fill-rule="evenodd" opacity=".9"></path></svg></div></div></a><div style="transform:translate3d(0, 0, 0)" class="hCKiGj"><div class="syl9yP">NIKE</div><a class="WKTcLC BwBZTg" title="SB Heritage Vulc Skateboard Shoes For Men" target="_blank" rel="noopener noreferrer" href="/nike-sb-heritage-vulc-skateboard-shoes-men/p/itmc984df4edd5fa?pid=SHOGZ4G4XHTAS2DK&amp;lid=LSTSHOGZ4G4XHTAS2DKQ8LFUV&amp;marketplace=FLIPKART&amp;q=nike+shoes&amp;store=osp&amp;srno=s_1_1&amp;otracker=search&amp;otracker1=search&amp;fm=organic&amp;iid=d59d19eb-7f30-4a3e-8753-e9293fa223e5.SHOGZ4G4XHTAS2DK.SEARCH&amp;ppt=None&amp;ppn=None&amp;ssid=gkktej9neo0000001748777482991&amp;qH=2d7d99166bc4a50f">SB Heritage Vulc Skateboard Shoes For Men</a><a class="+tlBoD" target="_blank" rel="noopener noreferrer" href="/nike-sb-heritage-vulc-skateboard-shoes-men/p/itmc984df4edd5fa?pid=SHOGZ4G4XHTAS2DK&amp;lid=LSTSHOGZ4G4XHTAS2DKQ8LFUV&amp;marketplace=FLIPKART&amp;q=nike+shoes&amp;store=osp&amp;srno=s_1_1&amp;otracker=search&amp;otracker1=search&amp;fm=organic&amp;iid=d59d19eb-7f30-4a3e-8753-e9293fa223e5.SHOGZ4G4XHTAS2DK.SEARCH&amp;ppt=None&amp;ppn=None&amp;ssid=gkktej9neo0000001748777482991&amp;qH=2d7d99166bc4a50f"><div class="hl05eU"><div class="Nx9bqj">₹2,747</div><div class="yRaY8j">₹<!-- -->5,495</div><div class="UkUFwK"><span>50% off</span></div></div><div class="k6cAZE gX6QF5"><div><div class="yiggsN" style="color:#000000;font-size:12px;font-weight:400">Free delivery</div></div></div></a><div class="M4DNwV"><div class="n5vj9c" style="padding-top:4px;padding-right:4px;padding-bottom:4px;padding-left:4px;border-radius:2px;background-color:#E9DEFF"><div class="yiggsN O5Fpg8" style="color:#7048c6;font-size:12px;font-style:normal;font-weight:500">Top Discount of the Sale</div></div></div></div></div></div><div data-id="SHOGZ4HYZZQHKBAQ" style="width:25%"><div class="_1sdMkc LFEi7Z"><a class="rPDeLR" target="_blank" rel="noopener noreferrer" href="/nike-sb-heritage-vulc-skateboard-shoes-men/p/itmf3bf0aec1205c?pid=SHOGZ4HYZZQHKBAQ&amp;lid=LSTSHOGZ4HYZZQHKBAQNKBA7J&amp;marketplace=FLIPKART&amp;q=nike+shoes&amp;store=osp&amp;srno=s_1_2&amp;otracker=search&amp;otracker1=search&amp;fm=organic&amp;iid=d59d19eb-7f30-4a3e-8753-e9293fa223e5.SHOGZ4HYZZQHKBAQ.SEARCH&amp;ppt=None&amp;ppn=None&amp;ssid=gkktej9neo0000001748777482991&amp;qH=2d7d99166bc4a50f"><div><div><div class="wvIX4U" style="padding-top:120.00%">
    @GetMapping("/search2")
    public String getProductList(@RequestParam String query, Model model) {
        List<Product> products = new ArrayList<>();

        try {
            // Construct the Flipkart search URL
            String url = "https://www.flipkart.com/search?q=" + query;

            // Connect to the URL and fetch the HTML content
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                    .timeout(10000)
                    .get();
            extractImagesAndText(doc.toString(),products);
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("products", products);
        return "productList";
    }
    public static void extractImagesAndText(String html,List<Product> products) {
        // Split by the image indicator
        String[] parts = html.split("alt=\"\" src=\"https://");

        // Loop through each image section
        for (int i = 1; i < parts.length; i++) {
            Product product=new Product();// skip the first part (before first match)
            String part = parts[i];

            // Extract the image URL (until the next quote)
            String imageUrl = "https://" + part.split("\"")[0];
            product.setImageUrl(imageUrl);

            Pattern pattern = Pattern.compile("href=\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(parts[i]);

            while (matcher.find() && !matcher.group(1).startsWith("//")) {
                product.setProductUrl("https://www.flipkart.com"+matcher.group(1).trim());
                break;
            }

            // Extract all non-empty text between > and <
            matcher = Pattern.compile(">([^<>]+)<").matcher(part);
            System.out.println("Text between > and <:");
            int count=0;
            while (matcher.find()) {
                String text = matcher.group(1).trim();

                if (!text.isEmpty()) {
                    if(count==1){
                        product.setName(text);
                    }else if(count==0){
                        product.setBrand(text);
                    }else if(count==2){
                        product.setPrice(text.substring(1));
                    }else if(count==4){
                        product.setOriginalPrice(text);
                    }else if(count==5){
                        product.setDiscount(text.split(" ")[0]);
                    }
                    count++;
                }
            }
            products.add(product);
        }
    }
}
