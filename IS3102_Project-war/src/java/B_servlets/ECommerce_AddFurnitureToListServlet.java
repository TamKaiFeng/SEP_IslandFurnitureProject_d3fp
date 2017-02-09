/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tam
 */
@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})
public class ECommerce_AddFurnitureToListServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet ECommerce_AddFurnitureToListServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet ECommerce_AddFurnitureToListServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");

            String id = request.getParameter("id");
            String SKU = request.getParameter("SKU");
            double price = Double.parseDouble(request.getParameter("price"));
            String name = request.getParameter("name");
            String imageURL = request.getParameter("imageURL");


            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.storeentity")
                    .path("getQuantityFromAll")
                    .queryParam("SKU", SKU);
            Invocation.Builder invocationBuilder = target.request();
            Response resp = invocationBuilder.get();

if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
                String m = resp.readEntity(String.class);
                
                HttpSession shoppingCartSession = request.getSession();//true (if no session create one)
                ArrayList<ShoppingCartLineItem> shoppingCart = (ArrayList<ShoppingCartLineItem>) (shoppingCartSession.getAttribute("shoppingCart"));
                
                if (shoppingCart == null) {
                    ArrayList<ShoppingCartLineItem> newShoppingCart = new ArrayList();
                    
                    ShoppingCartLineItem shoppingCartLineItem = new ShoppingCartLineItem();
                    shoppingCartLineItem.setId(id);
                    shoppingCartLineItem.setSKU(SKU);
                    shoppingCartLineItem.setPrice(price);
                    shoppingCartLineItem.setName(name);
                    shoppingCartLineItem.setImageURL(imageURL);
                    shoppingCartLineItem.setQuantity(1);
                    
                    newShoppingCart.add(shoppingCartLineItem);
                    shoppingCartSession.setAttribute("shoppingCart", newShoppingCart);
                } else {
                    for (ShoppingCartLineItem item : shoppingCart) {
                        if (item.getId().equals(id)) {
                            item.setQuantity(item.getQuantity() + 1);
                            break;
                        }
                        else{
                            ShoppingCartLineItem shoppingCartLineItem = new ShoppingCartLineItem();
                            shoppingCartLineItem.setId(id);
                            shoppingCartLineItem.setSKU(SKU);
                            shoppingCartLineItem.setPrice(price);
                            shoppingCartLineItem.setName(name);
                            shoppingCartLineItem.setImageURL(imageURL);
                            shoppingCartLineItem.setQuantity(1);
                            
                            shoppingCart.add(shoppingCartLineItem);
                            shoppingCartSession.setAttribute("shoppingCart", shoppingCart);
                            break;
                        }
                    }
                }
                
                response.sendRedirect("http://localhost:8080/IS3102_Project-war/B/SG/shoppingCart.jsp");
                
            } else {
                out.print("fail");
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
