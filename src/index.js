import React from "react";
import ReactDOM from "react-dom";
import "./index.scss";
import Connector from "./connector";
import AllStudents from "./pages/Students/AllStudents";
import AddSubscriptions from "./pages/Subscriptions/AddSubscription";
import EditSubscriptions from "./pages/Subscriptions/EditSubscription";

ReactDOM.render(
  <React.Fragment>
    <Connector />
  </React.Fragment>,
  document.getElementById("root")
);
