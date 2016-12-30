/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author Tam
 */
@WebServlet(name = "ECommerce_GetMember", urlPatterns = {"/ECommerce_GetMember"})
public class ECommerce_GetMember extends HttpServlet {

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
            HttpSession session = request.getSession();
            String memberEmail = (String) session.getAttribute("memberEmail");           
            
            Client client = ClientBuilder.newClient();            
            WebTarget target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity")
                    .path("getUserOverview")
                    .queryParam("email", memberEmail);
            Invocation.Builder invocationBuilder = target.request();
            Response resp = invocationBuilder.get();

            if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
                String m = resp.readEntity(String.class);//The JsonObject returned from the Web Service

                //Create a Member object
                Member member = new Member();
                JsonObject jsonObject = Json.createReader(new StringReader(m)).readObject();
                String memberName = jsonObject.getString("name");
                member.setName(memberName);
                member.setEmail(memberEmail);
                member.setPhone(jsonObject.getString("phone"));
                member.setCity(jsonObject.getString("city"));
                member.setAddress(jsonObject.getString("address"));
                member.setSecurityQuestion(jsonObject.getInt("securityquestion"));
                member.setSecurityAnswer(jsonObject.getString("securityanswer"));
                
                //Create a session with a Member object
                session.setAttribute("member", member);
                session.setAttribute("memberName", memberName);

                response.sendRedirect("http://localhost:8080/IS3102_Project-war/B/SG/memberProfile.jsp");

            }else{
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
