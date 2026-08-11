import React, { Component, createRef } from "react";

// CSS
import "./Join.scss";

// Components
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { InputTextarea } from "primereact/inputtextarea";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";

// Helper
import { setCookie } from "../../Helper/Cookies";
import { apiFetch, login, saveSession } from "../../Helper/Api";

// Properties
const properties = require("../../properties.json");

const SERVICES = [
  { icon: "pi-video", title: "دروس تفاعلية", desc: "ارفع فيديو وصوت لكل درس، متاحة لطلابك في أي وقت وأي مكان" },
  { icon: "pi-file-edit", title: "امتحانات إلكترونية", desc: "أنشئ امتحاناتك وخلي التصحيح فوري وتلقائي بمجرد التسليم" },
  { icon: "pi-chart-line", title: "متابعة أداء الطلاب", desc: "أرشيف كامل لكل امتحانات ودرجات طلابك" },
  { icon: "pi-comments", title: "تواصل مباشر", desc: "راسل طلابك واستقبل استفساراتهم في مكان واحد" },
  { icon: "pi-calendar", title: "جدول الحصص", desc: "نظّم مواعيد حصصك ومحاضراتك في مكان واحد" },
  { icon: "pi-credit-card", title: "إدارة الاشتراكات", desc: "تابع اشتراكات طلابك بالفترات الدراسية بسهولة" },
];

class Join extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
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

      setTimeout(function () {
        window.location.href = `/onboarding`;
      }, 800);
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
    const { submitting } = this.state;

    return (
      <div className="join-page">
        <Toast ref={this.toast}></Toast>

        <section className="hero">
          <div className="container">
            <img
              className="logo"
              src={require(`../../assets/img/${properties.teacher.logo}`).default}
              alt={properties.site_name}
            />
            <h1>{properties.site_name}</h1>
            <p className="tagline">
              منصتك التعليمية المتكاملة كمعلم — إدارة دروسك وامتحاناتك
              وطلابك في مكان واحد
            </p>
            <a href="#signup" className="hero-cta">
              اشترك الآن مجانًا
            </a>
          </div>
        </section>

        <section className="services">
          <div className="container">
            <h2>خدمات المنصة</h2>
            <div className="services-grid">
              {SERVICES.map((s, i) => (
                <div className="service-card" key={i}>
                  <i className={`pi ${s.icon}`}></i>
                  <h3>{s.title}</h3>
                  <p>{s.desc}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        <section className="signup" id="signup">
          <div className="container">
            <Card className="signup-card">
              <h2>اشترك كمعلم</h2>
              <p className="signup-subtitle">
                أنشئ حسابك مجانًا وابدأ في استخدام لوحة التحكم فورًا
              </p>
              <form onSubmit={this.handleSubmit}>
                <div className="p-grid p-fluid">
                  <div className="p-col-12 p-mt-3">
                    <span className="p-float-label">
                      <InputText
                        id="fullName"
                        value={this.state.fullName}
                        onChange={(e) => this.setState({ fullName: e.target.value })}
                      />
                      <label htmlFor="fullName">الاسم الكامل</label>
                    </span>
                  </div>
                  <div className="p-col-12 p-mt-4">
                    <span className="p-float-label">
                      <InputText
                        id="email"
                        type="email"
                        value={this.state.email}
                        onChange={(e) => this.setState({ email: e.target.value })}
                      />
                      <label htmlFor="email">البريد الإلكتروني</label>
                    </span>
                  </div>
                  <div className="p-col-12 p-mt-4">
                    <span className="p-float-label">
                      <InputText
                        id="password"
                        type="password"
                        value={this.state.password}
                        onChange={(e) => this.setState({ password: e.target.value })}
                      />
                      <label htmlFor="password">كلمة السر (8 أحرف على الأقل)</label>
                    </span>
                  </div>
                  <div className="p-col-12 p-mt-4">
                    <span className="p-float-label">
                      <InputText
                        id="mobile"
                        value={this.state.mobile}
                        onChange={(e) => this.setState({ mobile: e.target.value })}
                      />
                      <label htmlFor="mobile">رقم الموبايل</label>
                    </span>
                  </div>
                  <div className="p-col-12 p-mt-4">
                    <span className="p-float-label">
                      <InputTextarea
                        id="description"
                        rows={3}
                        value={this.state.description}
                        onChange={(e) => this.setState({ description: e.target.value })}
                      />
                      <label htmlFor="description">نبذة عن تخصصك (اختياري)</label>
                    </span>
                  </div>
                  <div className="p-col-12 p-mt-4">
                    <Button
                      type="submit"
                      className="signup-button"
                      label={submitting ? "جاري إنشاء الحساب..." : "إنشاء الحساب"}
                      disabled={submitting}
                    />
                  </div>
                </div>
              </form>
            </Card>
          </div>
        </section>
      </div>
    );
  }
}

export default Join;
