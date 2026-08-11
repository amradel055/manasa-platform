import React, {Component} from 'react';
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

// Pages
import AddLesson from './pages/Lessons/AddLesson.js';
import EditLessons from './pages/Lessons/EditLesson.js';
import AllLessons from './pages/Lessons/AllLessons.js';

import AddSubscriptions from './pages/Subscriptions/AddSubscription.js';
import AverageSubscription from './pages/Subscriptions/averageSubscription';

import AllSubscriptions from './pages/Subscriptions/AllSubscriptions.js';
// import EditSubscriptions from './pages/Subscriptions/EditSubscription.js';

import AddExam from './pages/Exams/AddExam.js';
import EditExam from './pages/Exams/EditExam.js';
import AllExams from './pages/Exams/AllExams.js';
import Schedule from './pages/Schedule/Schedule.js';
import AllStudents from './pages/Students/AllStudents.js';
import EditStudent from './pages/Students/EditStudent.js';
import SubStudents from './pages/Students/SubStudents.js';

import SignIn from './pages/Sign-In/Sign-In.js';

import AddItemGallery from './pages/Gallery/AddItem.js';
import Gallery from './pages/Gallery/Gallery.js';
import ViewExams from './pages/Exams/ViewExams';
import AddPeriod from './pages/Subscriptions/addPeriod.js';
import AddNews from './pages/News/AddNews.js';
import ExamsReports from './pages/Exams/ExamsReports.js';
import EditCommonExam from './pages/CommonExams/EditCommonExam.js';
import AddCommonExam from './pages/CommonExams/AddCommonExam.js';
import AllCommonExams from './pages/CommonExams/AllCommonExams.js';
import AllInbox from './pages/inbox/allInbox.js';
import EditInboxDetail from './pages/inbox/EditInboxDetail.js';
import Reparation from './pages/Reparation/Reparation.js';
import Links from './pages/links/links.js';
import Notifications from './pages/notifications/notifications.js';
import SchoolStages from './pages/Settings/SchoolStages.js';
import Materials from './pages/Settings/Materials.js';
import Join from './pages/Join/Join.js';
import Onboarding from './pages/Onboarding/Onboarding.js';
class Connector extends Component {
  render() {
    return (
      <Router>
        <Switch >
          <Route path="/" exact>
            <SignIn />
          </Route>
          <Route path="/join" exact>
            <Join />
          </Route>
          <Route path="/onboarding" exact>
            <Onboarding />
          </Route>
          <Route path="/lessons" exact>
            <AllLessons />
          </Route>
          <Route path="/lessons/add">
            <AddLesson />
          </Route>
          <Route path="/lessons/edit/:id" component={EditLessons} />
          <Route path="/subscriptions" exact>
            <AllSubscriptions />
          </Route>
          <Route path="/subscriptions/add">
            <AddSubscriptions />
          </Route>
          {/* <Route path="/subscriptions/edit/:id" component={EditSubscriptions} /> */}
          <Route path="/sign-in">
          </Route>
          <Route path="/students" exact>
            <AllStudents />
          </Route>
          <Route path="/myStudents" exact>
            <SubStudents />
          </Route>
          <Route path="/exams/view/:studentId" component={ViewExams} />
          <Route path="/students/edit/:id" component={EditStudent} />
          <Route path="/subscriptions/average/" >
            <AverageSubscription/>
          </Route>
          <Route path="/subscriptions/addPeriod/" >
            <AddPeriod/>
          </Route>
          <Route path="/gallery" exact>
            <Gallery />
          </Route>
          <Route path="/gallery/add">
            <AddItemGallery />
          </Route>
          <Route path="/exams/add">
            <AddExam />
          </Route>
          <Route path="/exams/edit/:id" component={EditExam} />
          <Route path="/exams/" exact>
            <AllExams />
          </Route>
          <Route path="/commonQuestions/add">
            <AddCommonExam />
          </Route>
          <Route path="/commonQuestions/edit/:id" component={EditCommonExam} />
          <Route path="/commonQuestions/" exact>
            <AllCommonExams />
          </Route>
          <Route path="/allInbox/add">
            <AllInbox />
          </Route>
          <Route path="/allInbox/edit/:id" component={EditInboxDetail} />
          <Route path="/allInbox/" exact>
            <AllInbox />
          </Route>
          <Route path="/news/add" exact>
            <AddNews />
          </Route>
          <Route path="/reparation/add" exact>
            <Reparation />
          </Route>
          <Route path="/links/add" exact>
            <Links />
          </Route>
          <Route path="/notifications/add" exact>
            <Notifications />
          </Route>
          <Route path="/schedule" exact>
            <Schedule />
          </Route>
          <Route path="/examsReports" exact>
            <ExamsReports />
          </Route>
          <Route path="/settings/school-stages" exact>
            <SchoolStages />
          </Route>
          <Route path="/settings/materials" exact>
            <Materials />
          </Route>
        </Switch>
      </Router>
    );
  }
}

export default Connector;
