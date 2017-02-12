/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author Shiro
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
                    HttpSession shoppingCartSession = request.getSession(false);
                    ArrayList<ShoppingCartLineItem> shoppingCart = (ArrayList<ShoppingCartLineItem>) (shoppingCartSession.getAttribute("shoppingCart"));
                    Map<String,Integer> valid = new HashMap();
                    
                    shoppingCart.clear();
                    shoppingCartSession.setAttribute("shoppingCart", shoppingCart);
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
