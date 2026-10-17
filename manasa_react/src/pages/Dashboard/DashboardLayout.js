import React, { Component } from "react";

// CSS -- these three are what load PrimeReact's theme and icons for the whole
// dashboard. They used to live in Header.js; keep them here, since this layout
// now wraps every page that uses them.
import "./DashboardLayout.scss";
import "primereact/resources/themes/saga-blue/theme.css";
import "primereact/resources/primereact.min.css";
import "primeicons/primeicons.css";

// Helper
import { clearSession } from "../../Helper/Api";

// Properties
const properties = require("../../properties.json");

const LOGO = require(`../../assets/img/${properties.teacher.logo}`).default;

/**
 * The frame every dashboard page sits in: the platform's gradient as the
 * ground, one rounded sheet floating on it, and inside that sheet the rail on
 * one side and the page on the other.
 *
 * Mounted above the route switch (see connector.js) so moving between pages
 * swaps only the content -- the rail keeps its open sections and its scroll.
 */
class DashboardLayout extends Component {
  // which section is expanded in the rail; null = all closed
  state = { open: null };

  toggleSection = (i) => {
    this.setState((s) => ({ open: s.open === i ? null : i }));
  };

  // Whoever is signed in, as saved by the sign-in and signup flows. Read from
  // storage rather than refetched: the dashboard only renders after login, and
  // this is the same key every other page already reads.
  teacher = () => {
    try {
      return JSON.parse(window.localStorage.getItem("currentTeacher")) || {};
    } catch (e) {
      return {};
    }
  };

  // first letters of the first two words, for the avatar
  initials = (name) =>
    String(name || "")
      .trim()
      .split(/\s+/)
      .slice(0, 2)
      .map((w) => w.charAt(0))
      .join("");

  // Logout of The System
  logout = (e) => {
    e.preventDefault();

    document.cookie = "name =; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
    window.localStorage.removeItem("currentTeacher");
    clearSession();

    setTimeout(() => {
      window.location.href = `/`;
    }, 500);
  };

  render() {
    const { open } = this.state;
    const me = this.teacher();

    const items = [
      // One destination, so a link rather than a drawer: adding a lesson is
      // an action taken on the lessons page (see its header), not a second
      // entry in a menu that already has nine of them.
      {
        label: "الحصص",
        icon: "pi pi-fw pi-video",
        url: `/lessons`,
      },
      {
        label: "الطلاب",
        icon: "pi pi-fw pi-user",
        items: [
          {
            label: "تفعيل الطلاب ",
            icon: "pi pi-fw pi-user",
            url: `/students`,
          },
          {
            label: " طلابي ",
            icon: "pi pi-fw pi-user",
            url: `/myStudents`,
          },
        ],
      },
      {
        label: "الفترات ",
        icon: "pi pi-fw pi-calendar",
        items: [
          {
            label: "إشتراك فتره جديد",
            icon: "pi pi-fw pi-plus",
            url: `/subscriptions/add`,
          },
          {
            separator: true,
          },
          {
            label: "اضافة فتره جديده",
            icon: "pi pi-fw pi-plus",
            url: `/subscriptions/addPeriod`,
          },
          {
            separator: true,
          },
          {
            label: "كل الإشتراكات",
            icon: "pi pi-fw pi-calendar",
            url: `/subscriptions`,
          },
        ],
      },
      {
          label:'المعرض',
          icon:'pi pi-fw pi-images',
          items: [
              {
                  label: 'إضافة صورة/فيديو',
                  icon: 'pi pi-fw pi-plus',
                  url: `/gallery/add`
              },
              {
                  separator:true
              },
              {
                  label: 'كل المعرض',
                  icon: 'pi pi-fw pi-images',
                  url: `/gallery`
              }
          ]
      },
      {
        label: "الإمتحانات",
        icon: "pi pi-fw pi-list",
        items: [
          {
            label: "إضافة إمتحان",
            icon: "pi pi-fw pi-plus",
            url: `/exams/add`,
          },
          {
            separator: true,
          },
          {
            label: "كل الإمتحانات",
            icon: "pi pi-fw pi-list",
            url: `/exams`,
          },
        ],
      },
     { label: "الاسئله الشائعه",
      icon: "pi pi-fw pi-list",
      items: [
        {
          label: "إضافة سؤال ",
          icon: "pi pi-fw pi-plus",
          url: `/commonQuestions/add`,
        },
        {
          separator: true,
        },
        {
          label: "كل الاسئله",
          icon: "pi pi-fw pi-list",
          url: `/commonQuestions`,
        },
      ],
    },
    // { label: "البريد",
    //   icon: "pi pi-fw pi-list",
    //   items: [
    //     // {
    //     //   label: "إضافة سؤال ",
    //     //   icon: "pi pi-fw pi-plus",
    //     //   url: `/commonExams/add`,
    //     // },
    //     // {
    //     //   separator: true,
    //     // },
       
    //   ],
    // },
    {
      label: "التقارير",
      icon: "pi pi-fw pi-chart-bar",
      items: [
        {
          label: "تقارير الامتحانات",
          icon: "pi pi-fw pi-chart-line",
          url: `/examsReports`,
        },
      ],
    },
    { label: "الإعدادات",
      icon: "pi pi-fw pi-cog",
      items: [
        {
          label: "المراحل الدراسية",
          icon: "pi pi-fw pi-sitemap",
          url: `/settings/school-stages`,
        },
        {
          label: "المواد",
          icon: "pi pi-fw pi-book",
          url: `/settings/materials`,
        },
      ],
    },
    { label: "المزيد",
    icon: "pi pi-fw pi-list",
    items: [
      // {
      //   label: "إضافة سؤال ",
      //   icon: "pi pi-fw pi-plus",
      //   url: `/commonExams/add`,
      // },
      // {
      //   separator: true,
      // },
      {
        label: "البريد الوارد",
        icon: "pi pi-fw pi-list",
        url: `/allInbox`,
      },
      {
        label: "الأخبار",
        icon: "pi pi-fw pi-calendar",
        url: `/news/add`,
      },
      {
        label: "التعويضات",
        icon: "pi pi-fw pi-calendar",
        url: `/reparation/add`,
      },
      {
        label: "الروابط",
        icon: "pi pi-fw pi-calendar",
        url: `/links/add`,
      },
      {
        label: "الاشعارات",
        icon: "pi pi-fw pi-calendar",
        url: `/notifications/add`,
      },
      {
        label: "المواعيد",
        icon: "pi pi-fw pi-calendar",
        url: `/schedule`,
      },
    ],
  },
    
     
    ];

    return (
      <div className="dash">
        {/* Only the three pixels of padding on this wrapper ever show of
            the halo behind it, so the light reads as travelling around the
            sheet's edge rather than as a border sitting on it -- the same
            frame the sign-in screen wears. */}
        <div className="dash-wrap">
          <div className="dash-sheet">

            <nav className="side-rail" aria-label="القائمة الرئيسية">
              <div className="rail-head">
                {/* The drawing and the wordmark live in one PNG with the
                    drawing to the LEFT of the name, so shown whole it puts the
                    name on the right -- the start of an RTL row. Each half is
                    drawn as a cropped background of that same file instead, so
                    the drawing leads and the name sits at the end. */}
                <a className="rail-brand" href="/" aria-label={properties.teacher.name}>
                  <span
                    className="bm-mark"
                    style={{ backgroundImage: `url(${LOGO})` }}
                  ></span>
                  <span
                    className="bm-name"
                    style={{ backgroundImage: `url(${LOGO})` }}
                  ></span>
                </a>
              </div>

              <div className="rail-list">
                {items.map((item, i) => {
                  const kids = (item.items || []).filter((k) => !k.separator);

                  // Nothing underneath it: it goes somewhere instead of
                  // opening. No chevron either -- a chevron is a promise of
                  // something below, and there is nothing below.
                  if (!kids.length && item.url) {
                    return (
                      <div className="rail-sec" key={i}>
                        <a className="rail-top" href={item.url}>
                          <i className={item.icon}></i>
                          <span className="lbl">{item.label}</span>
                        </a>
                      </div>
                    );
                  }

                  return (
                    <div className={`rail-sec${open === i ? " open" : ""}`} key={i}>
                      <button
                        type="button"
                        className="rail-top"
                        onClick={() => this.toggleSection(i)}
                        aria-expanded={open === i}
                      >
                        <i className={item.icon}></i>
                        <span className="lbl">{item.label}</span>
                        <i className="pi pi-chevron-down cr"></i>
                      </button>
                      <div className="rail-kids">
                        {/* a dot rather than a glyph: the children repeat the
                            section's own icon, so the marks added noise without
                            telling the reader anything */}
                        {kids.map((k, j) => (
                          <a href={k.url} key={j}>
                            <span className="dot" aria-hidden="true"></span>
                            <span>{k.label}</span>
                          </a>
                        ))}
                      </div>
                    </div>
                  );
                })}
              </div>

              {/* pinned to the foot of the rail: who is signed in, and the way
                  out. The list above it is the only part that scrolls. */}
              <div className="rail-profile">
                <span className="pf-avatar" aria-hidden="true">
                  {this.initials(me.fullName) || "؟"}
                  <i className="pf-live"></i>
                </span>
                <span className="pf-who">
                  <b>{me.fullName || "المعلم"}</b>
                  <small dir="ltr">{me.email || ""}</small>
                </span>
                <button
                  type="button"
                  className="pf-out"
                  onClick={this.logout}
                  aria-label="تسجيل الخروج"
                  title="تسجيل الخروج"
                >
                  <i className="pi pi-power-off"></i>
                </button>
              </div>
            </nav>

            <main className="dash-main">{this.props.children}</main>

          </div>
        </div>
      </div>
    );
  }
}

export default DashboardLayout;
