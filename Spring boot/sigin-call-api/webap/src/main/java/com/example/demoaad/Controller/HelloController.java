package com.example.demoaad.Controller;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.reactive.function.client.WebClient;





@RestController
public class HelloController {
     
   
   //  @Autowired
   //  private WebClient webClient;

     @GetMapping("Admin")
     @ResponseBody
     @PreAuthorize("hasAuthority('APPROLE_Admin')")
     public String Admin() {
         
        return "Admin message";
     }

      @GetMapping("/graph")
      @ResponseBody
      public String graph(
         @RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graphClient
      ) {
         // toJsonString() is just a demo.
         // oAuth2AuthorizedClient contains access_token. We can use this access_token to access the resource server.
         OAuth2AccessToken tk = graphClient.getAccessToken();
         return tk.getTokenValue();
      }

      @GetMapping("/back")
      @PreAuthorize("hasAuthority('APPROLE_Admin')")
      @ResponseBody
      public String Back(
         @RegisteredOAuth2AuthorizedClient("back") OAuth2AuthorizedClient BackClient
      ) throws IOException, InterruptedException {
         // toJsonString() is just a demo.
         // oAuth2AuthorizedClient contains access_token. We can use this access_token to access the resource server.
         
         OAuth2AccessToken tk = BackClient.getAccessToken();
         String tkv = tk.getTokenValue();


         var result = canVisitUri("http://localhost:8083/webapiA",tkv);


         return result;
      }

      private String canVisitUri(String uri, String tkv) throws IOException, InterruptedException {
        

         HttpClient client = HttpClient.newHttpClient();
         HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(uri))
             .setHeader("Authorization", "Bearer " + tkv)
             .build();


         var response = client.send(request, HttpResponse.BodyHandlers.ofString());
         

         // the response:
         
         return response.body().toString();

        
     }
}


