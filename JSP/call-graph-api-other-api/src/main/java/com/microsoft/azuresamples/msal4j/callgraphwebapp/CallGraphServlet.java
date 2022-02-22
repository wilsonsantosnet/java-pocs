// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.azuresamples.msal4j.callgraphwebapp;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.azure.core.http.HttpClient;
import com.azure.core.http.HttpMethod;
import com.azure.core.http.HttpRequest;
import com.microsoft.azuresamples.msal4j.helpers.AuthException;
import com.microsoft.azuresamples.msal4j.helpers.AuthHelper;
import com.microsoft.azuresamples.msal4j.helpers.GraphHelper;
import com.microsoft.azuresamples.msal4j.helpers.IdentityContextAdapterServlet;
import com.microsoft.azuresamples.msal4j.helpers.IdentityContextData;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.User;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



/**
 * This class defines the endpoint for showing the graph /me endpoint
 * This is here simply to demonstrate the graph call.
 */
@WebServlet(name = "CallGraphServlet", urlPatterns = "/call_graph")
public class CallGraphServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(CallGraphServlet.class.getName());

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        try {
            
            // re-auth (prefer silently) in case the access token is not valid anymore.
            IdentityContextAdapterServlet contextAdapter = new IdentityContextAdapterServlet(req, resp);
            AuthHelper.acquireTokenSilently(contextAdapter);
            User user = GraphHelper.getGraphClient(contextAdapter).me().buildRequest().get();
            if (user == null)
                throw new NullPointerException("user returned by Graph SDK was null");


            
            //Call API OAUTH 2.0 Bearer token
            AuthHelper.acquireTokenSilently(contextAdapter,"api://a3bc9b43-9309-473f-b4f5-cf2a5879baa7/AccessAPI");
            final IdentityContextData context = contextAdapter.getContext();
            String tk = context.getAccessToken();

            String result = canVisitUri("http://localhost:8083/webapiA",tk);

            req.setAttribute("tk", tk);
            req.setAttribute("user", graphUserProperties(user));
            req.setAttribute("bodyContent", "content/graph.jsp");
            final RequestDispatcher view = req.getRequestDispatcher("index.jsp");
            view.forward(req, resp);

        } catch (AuthException|ClientException|NullPointerException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            logger.log(Level.WARNING, Arrays.toString(ex.getStackTrace()));
            logger.log(Level.INFO, "redirecting to error page to display auth error to user.");
            resp.sendRedirect(resp.encodeRedirectURL(String.format(req.getContextPath() + "/auth_error_details?details=%s", ex.getMessage())));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String canVisitUri(String uri, String tkv) throws IOException, InterruptedException {
        

       

        OkHttpClient client = new OkHttpClient();

    
        Request request = new Request.Builder()
            .url(uri)
            .header("Authorization", "Bearer " + tkv)
            .build();
    

        Response response = client.newCall(request).execute();
        return response.body().string();



        // HttpRequest request = HttpRequest.newBuilder()
        //     .uri(URI.create(uri))
        //     .setHeader("Authorization", "Bearer " + tkv)
        //     .build();


        // var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        

        // // the response:
        
        // return response.body().toString();

       
       
    }



    /**
     * Take a few of the User properties obtained from the graph /me endpoint and put them into KV pairs for UI to display.
     * @param user User object (Graph SDK com.microsoft.graph.models.User)
     * @return HashMap<String,String> select Key-Values from User object
     */
    private HashMap<String,String> graphUserProperties(User user) {
        HashMap<String,String> userProperties = new HashMap<>();
        userProperties.put("Display Name", user.displayName);
        userProperties.put("Phone Number", user.mobilePhone);
        userProperties.put("City", user.city);
        userProperties.put("Given Name", user.givenName);
        return userProperties;

    }
}
