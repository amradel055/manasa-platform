import React, { Component, createRef } from "react";

// CSS
import "./Sign-In.scss";

// Components
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { login, saveSession, toLoginEmail } from "../../Helper/Api";

// Properties
const properties = require("../../properties.json");

class SignIn extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    // Form Data
    username: null,
    password: null,

    // Submit Button
    buttonText: "تسجيل الدخول",
    buttonColor: "",
  };

  componentDidMount() {
    document.title = properties.site_name + " | تسجيل الدخول";
    // Redirect To Lessons Page If The User Is Already Logged In
    // if (checkCookie("name")) {
    //   window.location.href = `/lessons`;
    // }
  }

  render() {
    var signIn = async (e) => {
      var _this = this;
      e.preventDefault();
      this.setState({
        buttonText: "جاري الدخول ...",
        buttonColor: "loading",
      });

      try {
        const auth = await login(toLoginEmail(this.state.username), this.state.password);

        if (auth.role !== "TEACHER") {
          throw new Error("هذه اللوحة لحسابات المعلمين فقط");
        }

        saveSession(auth.token, auth);
        // Pages ported to the new API derive identity from the JWT itself (no
        // need to pass teacherId around). "teacherId" is kept here as an alias
        // of the new userId purely so any page not yet migrated to Api.js still
        // finds a value at the same localStorage key it always used.
        window.localStorage.setItem(
          "currentTeacher",
          JSON.stringify({ ...auth, teacherId: auth.userId })
        );

        _this.toast.current.show({
          severity: "success",
          summary: "تم بنجاح",
          detail: "تم تسجيل الدخول بنجاح",
        });

        setCookie("name", auth.fullName);
        setTimeout(function () {
          window.location.href = `/lessons`;
        }, 800);
      } catch (error) {
        console.error("Error:", error);
        _this.setState({
          buttonText: "تسجيل الدخول",
          buttonColor: "",
        });
        _this.toast.current.show({
          severity: "error",
          summary: "لم يتم تسجيل الدخول",
          detail: error.message || "اسم المستخدم او كلمة السر غير صحيحة",
        });
      }
    };

    return (
      <div className="sign-in-page">
        <Toast ref={this.toast}></Toast>

        <div className="container">
          <div className="row align-items-center justify-content-center">
            <div className="col-md-6">
              <img
                className="logo"
                src={
                  require(`../../assets/img/${properties.teacher.logo}`).default
                }
                alt={properties.teacher.name}
              />
              <div className="sign-form">
                <Card>
                  <form action="/lessons" method="POST" onSubmit={signIn}>
                    <div className="p-grid p-fluid">
                      <div className="p-col-12 p-md-12 p-mt-2">
                        <div className="p-inputgroup">
                          <span className="p-float-label">
                            <InputText
                              id="username"
                              value={this.state.username}
                              name="username"
                              type="text"
                              onChange={(e) => {
                                this.setState({ username: e.target.value });
                              }}
                            />

                            <label htmlFor="username">إسم المستخدم</label>
                          </span>
                        </div>
                      </div>
                      <div className="p-col-12 p-md-12 p-mt-2">
                        <div className="p-inputgroup">
                          <span className="p-float-label">
                            <InputText
                              id="password"
                              type="password"
                              value={this.state.password}
                              name="password"
                              onChange={(e) => {
                                this.setState({ password: e.target.value });
                              }}
                            />

                            <label htmlFor="password">كلمة السر</label>
                          </span>
                        </div>
                      </div>
                      <div className="p-col-12 p-md-12">
                        <div className="p-inputgroup">
                          <span className="p-float-label">
                            <Button
                              className={`add-button ${this.state.buttonColor}`}
                              label={this.state.buttonText}
                              onClick={signIn}
                            />
                          </span>
                        </div>
                      </div>
                    </div>
                  </form>
                </Card>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default SignIn;
