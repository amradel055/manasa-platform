import React, { Component, createRef } from "react";

// Components
import { InputText } from "primereact/inputtext";
import { InputTextarea } from "primereact/inputtextarea";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import { Link } from "react-router-dom";

// Helper
import { setCookie } from "../../Helper/Cookies";
import { apiFetch, login, saveSession } from "../../Helper/Api";

// Properties
const properties = require("../../properties.json");

const SUCCESS_HOLD = 1600; // ms the "done" panel shows before /onboarding

/**
 * Only the signup panel. Everything around it -- the gradient, the sheet, the
 * headline, the service cards -- lives in AuthLayout, which stays mounted when
 * the viewer moves between this and the sign-in form.
 */
class Join extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    succeeded: false,
    fullName: "",
    email: "",
    password: "",
    mobile: "",
    description: "",
    submitting: false,
  };

  componentDidMount() {
    document.title = properties.site_name + " | اشترك الآن";
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const { fullName, email, password, mobile, description } = this.state;

    if (!fullName || !email || !password) {
      this.toast.current.show({
        severity: "error",
        summary: "بيانات ناقصة",
        detail: "برجاء ملء الاسم والبريد الإلكتروني وكلمة السر",
      });
      return;
    }

    if (password.length < 8) {
      this.toast.current.show({
        severity: "error",
        summary: "كلمة السر قصيرة",
        detail: "كلمة السر لازم تكون 8 أحرف على الأقل",
      });
      return;
    }

    this.setState({ submitting: true });
    try {
      await apiFetch("/teachers/register", {
        method: "POST",
        body: {
          fullName,
          email,
          password,
          mobile,
          description,
          branchId: properties.branch_id,
        },
      });

      // Account is a real TEACHER identity -- log straight into this same
      // dashboard instead of just showing a confirmation message.
      const auth = await login(email, password);
      saveSession(auth.token, auth);
      window.localStorage.setItem(
        "currentTeacher",
        JSON.stringify({ ...auth, teacherId: auth.userId })
      );
      setCookie("name", auth.fullName);

      this.toast.current.show({
        severity: "success",
        summary: "تم بنجاح",
        detail: "تم إنشاء حسابك، خطوات بسيطة كمان وتبقى جاهز...",
      });

      // the panel turns over to its "done" face, then moves on
      this.setState({ succeeded: true });

      setTimeout(function () {
        window.location.href = `/onboarding`;
      }, SUCCESS_HOLD);
    } catch (error) {
      console.error("Error:", error);
      this.setState({ submitting: false });
      this.toast.current.show({
        severity: "error",
        summary: "تعذر إنشاء الحساب",
        detail: error.message || "حدث خطأ غير متوقع",
      });
    }
  };

  render() {
    const { submitting, succeeded } = this.state;

    return (
      <div className={`sheet-panel${succeeded ? " done" : ""}`} id="signup">
        <Toast ref={this.toast}></Toast>

        <div className="panel-face">
          <h2>اشترك كمعلم</h2>
          <p className="signup-subtitle">
            أنشئ حسابك مجانًا وابدأ في استخدام لوحة التحكم فورًا
          </p>
          <form onSubmit={this.handleSubmit} autoComplete="off">
            <div className="fields">
              {/* placeholder=" " keeps :not(:placeholder-shown) working, so the
                  floating label never sits on top of a typed value.
                  autoComplete is off so the browser does not drop a saved
                  address and password into the fields on load. */}
              <span className="p-float-label" style={{ "--d": "320ms" }}>
                <InputText
                  id="fullName"
                  placeholder=" "
                  value={this.state.fullName}
                  onChange={(e) => this.setState({ fullName: e.target.value })}
                />
                <label htmlFor="fullName">الاسم الكامل</label>
              </span>
              <span className="p-float-label" style={{ "--d": "380ms" }}>
                <InputText
                  id="email"
                  type="email"
                  dir="ltr"
                  placeholder=" "
                  autoComplete="off"
                  value={this.state.email}
                  onChange={(e) => this.setState({ email: e.target.value })}
                />
                <label htmlFor="email">البريد الإلكتروني</label>
              </span>
              <span className="p-float-label" style={{ "--d": "440ms" }}>
                <InputText
                  id="password"
                  type="password"
                  placeholder=" "
                  autoComplete="new-password"
                  value={this.state.password}
                  onChange={(e) => this.setState({ password: e.target.value })}
                />
                <label htmlFor="password">كلمة السر (8 أحرف على الأقل)</label>
              </span>
              <span className="p-float-label" style={{ "--d": "500ms" }}>
                <InputText
                  id="mobile"
                  dir="ltr"
                  placeholder=" "
                  value={this.state.mobile}
                  onChange={(e) => this.setState({ mobile: e.target.value })}
                />
                <label htmlFor="mobile">رقم الموبايل</label>
              </span>
              <span className="p-float-label" style={{ "--d": "560ms" }}>
                <InputTextarea
                  id="description"
                  rows={2}
                  placeholder=" "
                  value={this.state.description}
                  onChange={(e) => this.setState({ description: e.target.value })}
                />
                <label htmlFor="description">نبذة عن تخصصك (اختياري)</label>
              </span>
              <Button
                type="submit"
                className="signup-button"
                style={{ "--d": "630ms" }}
                icon={submitting ? "join-spin" : null}
                label={submitting ? "جاري إنشاء الحساب..." : "إنشاء الحساب"}
                disabled={submitting}
              />
            </div>
          </form>
          <p className="fine">بالتسجيل بتوافق على شروط استخدام المنصة.</p>
          <p className="signin">عندك حساب؟ <Link to="/">سجّل دخولك</Link></p>
        </div>

        {/* shown for a beat on success, before /onboarding takes over */}
        <div className="panel-done" aria-hidden={!succeeded}>
          <span className="seal">
            <svg
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="3"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="m4.5 12.5 5 5 10-11" />
            </svg>
          </span>
          <b>تم إنشاء حسابك</b>
          <span className="note">خطوات بسيطة كمان وتبقى جاهز...</span>
        </div>
      </div>
    );
  }
}

export default Join;
