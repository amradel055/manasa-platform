import React, { Component, createRef } from "react";

// Components
import { InputText } from "primereact/inputtext";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import { Link } from "react-router-dom";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { login, saveSession, toLoginEmail } from "../../Helper/Api";

// Properties
const properties = require("../../properties.json");

/**
 * Only the sign-in panel. Everything around it -- the gradient, the sheet, the
 * headline, the service cards -- lives in AuthLayout, which stays mounted when
 * the viewer moves between this and the signup form.
 */
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

    const busy = this.state.buttonColor === "loading";

    return (
      <div className="sheet-panel">
        <Toast ref={this.toast}></Toast>

        <div className="panel-face">
          <h2>تسجيل الدخول</h2>
          <p className="signup-subtitle">أدخل بياناتك للوصول للوحة التحكم</p>
          <form action="/lessons" method="POST" onSubmit={signIn}>
            <div className="fields">
              {/* placeholder=" " keeps :not(:placeholder-shown) working, so the
                  floating label never sits on top of a typed value. */}
              <span className="p-float-label" style={{ "--d": "320ms" }}>
                <InputText
                  id="username"
                  value={this.state.username}
                  name="username"
                  type="text"
                  placeholder=" "
                  onChange={(e) => {
                    this.setState({ username: e.target.value });
                  }}
                />
                <label htmlFor="username">إسم المستخدم</label>
              </span>
              <span className="p-float-label" style={{ "--d": "380ms" }}>
                <InputText
                  id="password"
                  type="password"
                  value={this.state.password}
                  name="password"
                  placeholder=" "
                  onChange={(e) => {
                    this.setState({ password: e.target.value });
                  }}
                />
                <label htmlFor="password">كلمة السر</label>
              </span>
              <Button
                className={`signup-button add-button ${this.state.buttonColor}`}
                style={{ "--d": "440ms" }}
                icon={busy ? "join-spin" : null}
                label={this.state.buttonText}
                onClick={signIn}
              />
            </div>
          </form>
          <p className="signin">ما عندك حساب؟ <Link to="/join">اشترك مجانًا</Link></p>
        </div>
      </div>
    );
  }
}

export default SignIn;
