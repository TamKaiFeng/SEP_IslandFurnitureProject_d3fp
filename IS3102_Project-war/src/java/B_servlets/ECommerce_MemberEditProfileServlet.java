/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
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
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
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

            Member member = new Member();
            member.setName(name);
            member.setPhone(phone);
            member.setCity(country);
            member.setAddress(address);
            member.setSecurityQuestion(securityQuestion);
            member.setSecurityAnswer(securityAnswer);
            member.setAge(age);
            member.setIncome(income);

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity")
                    .path("editMember")
                    .queryParam("name", name)
                    .queryParam("phone", phone)
                    .queryParam("country", country)
                    .queryParam("address", address)
                    .queryParam("securityQuestion", securityQuestion)
                    .queryParam("securityAnswer", securityAnswer)
                    .queryParam("age", age)
                    .queryParam("income", income)
                    .queryParam("password", password)
                    .queryParam("email", memberEmail);

            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response resp = invocationBuilder.put(Entity.entity(member, MediaType.APPLICATION_JSON));
            out.println("status: " + resp.getStatus());

            if (resp.getStatus() == Response.Status.CREATED.getStatusCode()) {
                String m = resp.readEntity(String.class);
                out.println(m);
                out.println("status: " + resp.getStatus());

                response.sendRedirect("http://localhost:8080/IS3102_Project-war/ECommerce_GetMember");

            } else {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet ECommerce_MemberEditProfileServlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet ECommerce_MemberEditProfileServlet at " + request.getContextPath() + "</h1>");
                out.println("</body>");
                out.println("</html>");
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
