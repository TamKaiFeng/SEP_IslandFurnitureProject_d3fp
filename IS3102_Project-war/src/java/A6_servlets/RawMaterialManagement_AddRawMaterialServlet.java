package A6_servlets;

import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.regex.*;

public class RawMaterialManagement_AddRawMaterialServlet extends HttpServlet {

    @EJB
    private ItemManagementBeanLocal itemManagementBean;
    String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String SKU = request.getParameter("SKU");
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            String description = request.getParameter("description");
            Integer _length = Integer.parseInt(request.getParameter("length"));
            Integer width = Integer.parseInt(request.getParameter("width"));
            Integer height = Integer.parseInt(request.getParameter("height"));
            String source = request.getParameter("source");
            System.out.println("source is " + source);
            
            //Set the url pattern string
            Pattern p = Pattern.compile("RM[1-9][0-9]*");
            //Match the given string with the pattern
            Matcher m = p.matcher(SKU);
            //Check whether match is found
            boolean matchFound = m.matches();
            if(matchFound){
                //SKU is valid
                //------------
                //Check length/width/height
                if(_length > 0){
                    //length is valid
                    //---------------
                    if(width > 0){
                        //width is valid
                        if(height > 0){
                            //height is valid
                            //-----------------
                            //all values are valid
                            if (!itemManagementBean.checkSKUExists(SKU)) {
                                itemManagementBean.addRawMaterial(SKU, name, category, description, _length, width, height);
                                result = "?goodMsg=Raw Material with SKU: " + SKU + " has been created successfully.";
                                response.sendRedirect("RawMaterialManagement_RawMaterialServlet" + result);
                            } else {
                                result = "?errMsg=Failed to add raw material, SKU: " + SKU + " already exist.";
                                response.sendRedirect(source + result);
                            }
                        }else{
                            //height is invalid
                            result = "?errMsg=Failed to add raw material, height: " + height + " is not valid.";
                            response.sendRedirect(source + result);
                        }
                    }else{
                        //width is invalid
                        result = "?errMsg=Failed to add raw material, width: " + width + " is not valid.";
                        response.sendRedirect(source + result);
                    }
                }else{
                    //length is invalid
                    result = "?errMsg=Failed to add raw material, length: " + _length + " is not valid.";
                    response.sendRedirect(source + result);
                }
                
                
                
            }else{
                //SKU is not valid
                result = "?errMsg=Failed to add raw material, SKU: " + SKU + " is not valid.";
                response.sendRedirect(source + result);
            }
            
            
            
            
            
            
            
            
        } catch (Exception ex) {
            out.println(ex);
        } finally {
            out.close();
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
