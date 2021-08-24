/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.config;

import com.isag.rest.CommentsRest;
import com.isag.rest.DocumentsRest;
import com.isag.rest.ExamsRest;
import com.isag.rest.LessonRest;
import com.isag.rest.LockupRest;
import com.isag.rest.LoginManasa;
import com.isag.rest.LoginStudentRest;
import com.isag.rest.MediaRest;
import com.isag.rest.PeriodsRest;
import com.isag.rest.StudentMonthsRest;
import com.isag.rest.StudentRest;
import com.isag.rest.TeachersRest;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author AHMEDESSAM
 */
@ApplicationPath("/rest/*")
public class MyApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(LoginManasa.class);
        resources.add(AuthenticationFilter.class);
        resources.add(CorsFilter.class);
        resources.add(StudentRest.class);
        resources.add(LockupRest.class);
        resources.add(LoginStudentRest.class);
        resources.add(LessonRest.class);
        resources.add(StudentMonthsRest.class);
        resources.add(DocumentsRest.class);
        resources.add(CommentsRest.class);
        resources.add(MediaRest.class);
        resources.add(ExamsRest.class);
        resources.add(PeriodsRest.class);
        resources.add(TeachersRest.class);
        return resources;
    }

}
