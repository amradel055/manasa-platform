/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.config;

import com.isag.rest.AppointmentsRest;
import com.isag.rest.CommentsRest;
import com.isag.rest.DocumentsRest;
import com.isag.rest.ExamsRest;
import com.isag.rest.InboxMessageRest;
import com.isag.rest.InboxReplyRest;
import com.isag.rest.LessonDocumentsRest;
import com.isag.rest.LessonImageRest;
import com.isag.rest.LessonLocationRest;
import com.isag.rest.LessonRest;
import com.isag.rest.LessonVoiceRest;
import com.isag.rest.LockupRest;
import com.isag.rest.LoginManasa;
import com.isag.rest.LoginStudentRest;
import com.isag.rest.ManasaReportRest;
import com.isag.rest.MediaRest;
import com.isag.rest.NewRest;
import com.isag.rest.NewsRest;
import com.isag.rest.OtherAppointmentsRest;
import com.isag.rest.PeriodsRest;
import com.isag.rest.QuestionCommonRest;
import com.isag.rest.StudentMonthsRest;
import com.isag.rest.StudentPeriodsOrderRest;
import com.isag.rest.StudentPeriodsRest;
import com.isag.rest.StudentResetRest;
import com.isag.rest.StudentRest;
import com.isag.rest.StudentStatusRest;
import com.isag.rest.TeachersRest;
import com.isag.rest.ZoomLinkRest;
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
        resources.add(StudentPeriodsRest.class);
        resources.add(StudentResetRest.class);
        resources.add(AppointmentsRest.class);
        resources.add(NewRest.class);
        resources.add(NewsRest.class);
        resources.add(OtherAppointmentsRest.class);
        resources.add(ManasaReportRest.class);
        resources.add(LessonLocationRest.class);
        resources.add(StudentPeriodsOrderRest.class);
        resources.add(QuestionCommonRest.class);
        resources.add(InboxMessageRest.class);
        resources.add(InboxReplyRest.class);
        resources.add(ZoomLinkRest.class);
        resources.add(LessonDocumentsRest.class);
        resources.add(LessonVoiceRest.class);
        resources.add(LessonImageRest.class);
        resources.add(StudentStatusRest.class);

        return resources;
    }
}
