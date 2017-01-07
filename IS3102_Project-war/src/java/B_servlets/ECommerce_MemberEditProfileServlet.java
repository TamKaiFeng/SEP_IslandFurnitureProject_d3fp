/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
@WebServlet(name = "ECommerce_MemberEditProfileServlet", urlPatterns = {"/ECommerce_MemberEditProfileServlet"})
public class ECommerce_MemberEditProfileServlet extends HttpServlet {

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
            //-----------------
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String country = request.getParameter("country");
            String address = request.getParameter("address");
            int securityQuestion = Integer.parseInt(request.getParameter("securityQuestion"));
            String securityAnswer = request.getParameter("securityAnswer");
            int age = Integer.parseInt(request.getParameter("age"));
            int income = Integer.parseInt(request.getParameter("income"));
            String password = request.getParameter("password");
            
            HttpSession session = request.getSession();
            String memberEmail = (String) session.getAttribute("memberEmail");
            
            
            JsonObjectBuilder memberBuilder = Json.createObjectBuilder();
            JsonObject memberJson = memberBuilder
                    .add("name", name)
                    .add("email", memberEmail)
                    .add("phone", phone)
                    .add("country", country)
                    .add("address", address)
                    .add("securityQuestion", securityQuestion)
                    .add("securityAnswer", securityAnswer)
                    .add("age", age)
                    .add("income", income)
                    .add("password", password != null ? password : "")
                    //if password is null replace it with an empty string
                    .build();
            out.println(memberJson);
            
            Client client = ClientBuilder.newClient();            
            WebTarget target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity")
                    .path("editMember")
                    .queryParam("member", memberJson);
            Invocation.Builder invocationBuilder = target.request();
            Response resp = invocationBuilder.get();

            if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
                String m = resp.readEntity(String.class);
                out.println(m);

                

                //response.sendRedirect("http://localhost:8080/IS3102_Project-war/B/SG/memberProfile.jsp");
                //response.sendRedirect("index.jsp");

            }else{
                out.print("fail");
            }
            //-----------------
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ECommerce_MemberEditProfileServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ECommerce_MemberEditProfileServlet at " + request.getContextPath() + "</h1>");
            out.println(name);
            out.println(phone);
            out.println(country);
            out.println(address);
            out.println(securityQuestion);
            out.println(securityAnswer);
            out.println(age);
            out.println(income);
            out.println(memberEmail);           
            out.println("</body>");
            out.println("</html>");
            
            
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
