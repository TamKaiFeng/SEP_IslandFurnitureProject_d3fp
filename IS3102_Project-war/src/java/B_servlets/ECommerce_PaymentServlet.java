/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tam
 */
@WebServlet(name = "ECommerce_PaymentServlet", urlPatterns = {"/ECommerce_PaymentServlet"})
public class ECommerce_PaymentServlet extends HttpServlet {

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
                //Get values from form
                String name = request.getParameter("name");
                String cardNo = request.getParameter("cardno");
                String secCode = request.getParameter("seccode");
                String expMonth = request.getParameter("expmonth");
                String expMonthNo = "";
                switch(expMonth){
                    case "January":
                        expMonthNo = "01";
                        break;
                    
                    case "February":
                        expMonthNo = "02";
                        break;
                    
                    case "March":
                        expMonthNo = "03";
                        break;
                    
                    case "April":
                        expMonthNo = "04";
                        break;
                        
                    case "May":
                        expMonthNo = "05";
                        break;
                    
                    case "June":
                        expMonthNo = "06";
                        break;
                        
                    case "July":
                        expMonthNo = "07";
                        break;
                       
                    case "August":
                        expMonthNo = "08";
                        break;
                        
                    case "September":
                        expMonthNo = "09";
                        break;
                     
                    case "October":
                        expMonthNo = "10";
                        break;
                        
                    case "November":
                        expMonthNo = "11";
                        break;
                        
                    case "December":
                        expMonthNo = "12";
                        break;
                    
                    default:
                        break;
                }
                
                String expYear = request.getParameter("expyr");
                String expYearMonth = expYear + "-" + expMonthNo;
                String result;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                Date currentDate = new Date();
                Date expDate = new Date();
                 try {
                expDate = dateFormat.parse(expYearMonth);
                 } catch (ParseException ex) {
                Logger.getLogger(ECommerce_PaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
                 }
                if(name.equals("")){
                    result = "No Name. Try again.";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+result);
                }else if(cardNo.equals("")){
                    result = "No Card Number. Try again.";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+result);
                }else if(cardNo.length()<16 || cardNo.length()>19){
                    result = "Invalid Card Number Length. Try again.";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+result);
                }else if(!cardNo.matches("^((4\\d{3})|(5[1-5]\\d{2}))(-?|\\040?)(\\d{4}(-?|\\040?)){3}|^(3[4,7]\\d{2})(-?|\\040?)\\d{6}(-?|\\040?)\\d{5}")){
                    result = "Invalid Card Number. Try again.";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+result);
                }else if(expYear.equals("")){
                    result = "No Expiry Year. Try again.";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+result);
                }else if(secCode.equals("")){
                    result = "No Security Code. Try again.";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+result);
                }else if(!secCode.matches("^[0-9]+$") || secCode.length() != 3){
                    result = "Invalid Security code. Try again.";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+result);
                }else if(currentDate.compareTo(expDate) > 0){
                    result = "Card is expired";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+result);
                }else{
                    HttpSession currSessions = request.getSession(false);
                    ArrayList<ShoppingCartLineItem> shoppingCart = (ArrayList<ShoppingCartLineItem>) (currSessions.getAttribute("shoppingCart"));
                    String memberEmail = (String) currSessions.getAttribute("memberEmail");
                    Member member = new Member();
                   
                    Client client = ClientBuilder.newClient();
                   
                    WebTarget target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity")
                    .path("getUserOverview")
                    .queryParam("email", memberEmail);
                    
                    Invocation.Builder invocationBuilder = target.request();
                    Response resp = invocationBuilder.get();
                    
                    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
                    String m = resp.readEntity(String.class);//The JsonObject returned from the Web Service
                    JsonObject jsonObject = Json.createReader(new StringReader(m)).readObject();
                    member.setId((long)jsonObject.getInt("id"));
                    double amountDue = 0.0;
                    
                    for(ShoppingCartLineItem item : shoppingCart){
                        amountDue+= item.getPrice()*item.getQuantity();
                    }
                    System.out.println("Passed line 176");
                    target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce")
                    .path("createSalesRecord")
                    .queryParam("memberId", member.getId())
                    .queryParam("amountDue",amountDue);
                    
                    invocationBuilder = target.request();
                    resp = invocationBuilder.post(Entity.entity(member,MediaType.APPLICATION_JSON));
                    System.out.println("Passed line 184" + resp);
                    
                    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
                        for(ShoppingCartLineItem item : shoppingCart){
                            target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce")
                            .path("updateStorage")
                            .queryParam("itemId", item.getId())
                            .queryParam("quantity",item.getQuantity());
                    invocationBuilder = target.request();
                    resp = invocationBuilder.put(Entity.entity(item,MediaType.APPLICATION_JSON));
                        }
                        if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
                        System.out.println("Line 196" + resp);
                        shoppingCart.clear();
                        currSessions.setAttribute("shoppingCart", shoppingCart);
                        response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=Transaction successful");
                        }
                    }
                }
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
