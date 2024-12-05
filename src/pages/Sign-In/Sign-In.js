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
    var signIn = (e) => {
      var _this = this;
      e.preventDefault();
      console.log("sent");
      this.setState({
        buttonText: "جاري الدخول ...",
        buttonColor: "loading",
      });

      // Send the data to the server
      fetch(properties.api_path + "/authenticationManasa/login", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json; charset=utf-8",
          // 'Access-Control-Allow-Origin': '*' , 
          // 'Access-Control-Allow-Methods' : 'POST' ,
          // "Access-Control-Allow-Headers": "X-Requested-With"
        },
        body: JSON.stringify({
          userName: this.state.username,
          password: this.state.password,
        }),
      })
        .then((res) => {
          return res.json();
        })
        .then((done) => {
          _this.setState({
            buttonText: "تسجيل الدخول",
            buttonColor: "",
          });

          if (done.success === true) {
            _this.toast.current.show({
              severity: "success",
              summary: "تم بنجاح",
              detail: "تم تسجيل الدخول بنجاح",
            });
          } else {
            _this.toast.current.show({
              severity: "error",
              summary: "لم يتم تسجيل الدخول",
              detail: "اسم المستخدم او كلمة السر غير صحيحة",
            });
          }

          //  Redirection to Lessons Page & Registering a Session
          if (done.success) {
            console.log(done);
            setTimeout(function () {
              window.location.href = `/lessons`;
              window.localStorage.setItem(
                "currentTeacher",
                JSON.stringify(done)
              );
              setCookie("name", done.data);
            }, 1000);
          }
        })
        .catch((error) => {
          console.error("Error:", error);

          _this.setState({
            buttonText: "تسجيل الدخول",
            buttonColor: "",
          });

          _this.toast.current.show({
            severity: "error",
            summary: "لم تتم العملية",
            detail: "خطأ في الإتصال",
          });
        });
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
