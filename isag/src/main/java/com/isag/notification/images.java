/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.notification;

import com.isag.businessservice.PathService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author AhmedEssam@ISAG.com
 */
@WebServlet(name = "images", urlPatterns = {"/images/*"})
public class images extends HttpServlet {

    @EJB
    private PathService pathService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getPathInfo().substring(1);
        String OS = System.getProperty("os.name").toLowerCase();
        String path = "";
        if ((OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)) {
            path = pathService.findPathbyTypeId(1, 1).concat("notification/");
        } else if ((OS.indexOf("win") >= 0)) {
            path = pathService.findPathbyTypeId(0, 1).concat("notification/");
        }
        File file = new File(path, filename);
        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

}
