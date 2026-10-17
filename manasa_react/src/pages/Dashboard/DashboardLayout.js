import React, { Component } from "react";
import { Link, withRouter } from "react-router-dom";

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
  // Which section is expanded in the rail.
  //
  //   undefined -- nobody has touched the rail yet, so the section holding
  //                the current page opens itself (see openIndex in render)
  //   a number  -- that section, chosen by a click
  //   null      -- all closed, also by a click
  //
  // The three states are distinct on purpose: without the first one there is
  // no way to tell "closed because you closed it" from "closed because you
  // have not touched it", and the rail would re-open itself every render.
  state = { open: undefined };

  /**
   * Which single rail entry leads to the page being looked at.
   *
   * A prefix match on its own is not enough, because rail entries are
   * prefixes of each other: standing on /subscriptions/addPeriod, both it
   * and /subscriptions match, and the rail lit up twice. So every entry that
   * matches is collected and the LONGEST one wins -- the most specific route
   * that still contains the current page.
   *
   * The trailing slash in the prefix test is what stops /lessons matching
   * /lessonsomething. The prefix part itself is still wanted: /lessons/add
   * has no entry of its own, and should light "الحصص".
   */
  activeUrl = (items) => {
    const here = this.props.location.pathname;

    const urls = [];
    items.forEach((item) => {
      if (item.url) urls.push(item.url);
      (item.items || []).forEach((k) => {
        if (k.url) urls.push(k.url);
      });
    });

    return urls
      .filter((url) => here === url || here.startsWith(url + "/"))
      .sort((a, b) => b.length - a.length)[0];
  };

  // `current` is the section open on screen, which is not always
  // this.state.open -- it may be the one that opened itself.
  toggleSection = (i, current) => {
    this.setState({ open: current === i ? null : i });
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
          // "إشتراك فتره جديد" is not here any more: that form opens as a
          // modal from the button in the subscriptions page header, the way
          // adding a lesson opens from the lessons page. The route still
          // works; it is just not a second entry in a nine-item menu.
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
      // One destination, so a link rather than a drawer: adding to the
      // gallery is an action taken on the gallery page, not a second entry
      // in a menu that already has nine of them. /gallery/add still works.
      {
          label:'المعرض',
          icon:'pi pi-fw pi-images',
          url: `/gallery`
      },
      // One destination: adding an exam is an action on the exams page.
      // /exams/add still works.
      {
        label: "الإمتحانات",
        icon: "pi pi-fw pi-list",
        url: `/exams`,
      },
     // One destination, same reason.
     { label: "الاسئله الشائعه",
      icon: "pi pi-fw pi-list",
      url: `/commonQuestions`,
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
    // One child, so a drawer that opens onto a single row -- two clicks to
    // reach one page. A link goes straight there.
    {
      label: "التقارير",
      icon: "pi pi-fw pi-chart-bar",
      url: `/examsReports`,
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

    // Worked out once for the whole rail, so there is exactly one answer.
    const activeUrl = this.activeUrl(items);
    const isHere = (url) => !!url && url === activeUrl;

    // The section holding the page being looked at, so arriving at a child
    // route does not land on a rail that gives no sign of where it is.
    const autoOpen = items.findIndex((item) =>
      (item.items || []).some((k) => isHere(k.url))
    );

    // state wins once the teacher has touched it; before that, the page does
    const open =
      this.state.open === undefined
        ? autoOpen === -1
          ? null
          : autoOpen
        : this.state.open;

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
                <Link className="rail-brand" to="/" aria-label={properties.teacher.name}>
                  <span
                    className="bm-mark"
                    style={{ backgroundImage: `url(${LOGO})` }}
                  ></span>
                  <span
                    className="bm-name"
                    style={{ backgroundImage: `url(${LOGO})` }}
                  ></span>
                </Link>
              </div>

              <div className="rail-list">
                {items.map((item, i) => {
                  const kids = (item.items || []).filter((k) => !k.separator);

                  // a section counts as current when one of its children is
                  const here =
                    isHere(item.url) || kids.some((k) => isHere(k.url));

                  // Nothing underneath it: it goes somewhere instead of
                  // opening. No chevron either -- a chevron is a promise of
                  // something below, and there is nothing below.
                  if (!kids.length && item.url) {
                    return (
                      <div className="rail-sec" key={i}>
                        {/* Link, not <a href>. An anchor hands the URL back
                            to the browser, which throws the whole app away
                            and boots it again -- every fetch re-run, every
                            bit of state lost, a white flash between pages.
                            This is a single-page app; the router can swap
                            the page without any of that. */}
                        <Link
                          className={`rail-top${here ? " here" : ""}`}
                          to={item.url}
                          aria-current={here ? "page" : undefined}
                        >
                          <i className={item.icon}></i>
                          <span className="lbl">{item.label}</span>
                        </Link>
                      </div>
                    );
                  }

                  return (
                    <div
                      className={`rail-sec${open === i ? " open" : ""}${here ? " here" : ""}`}
                      key={i}
                    >
                      <button
                        type="button"
                        className={`rail-top${here ? " here" : ""}`}
                        onClick={() => this.toggleSection(i, open)}
                        aria-expanded={open === i}
                      >
                        <i className={item.icon}></i>
                        <span className="lbl">{item.label}</span>
                        <i className="pi pi-chevron-down cr"></i>
                      </button>
                      <div className="rail-kids">
                        {/* One wrapper, not the links themselves: the 0fr-to-1fr
                            accordion in the stylesheet collapses the rows the
                            grid is TOLD about, and a grid told about one row
                            gives every child after the first an implicit row of
                            its own -- which is auto-height, and stays open. */}
                        <div>
                        {/* a dot rather than a glyph: the children repeat the
                            section's own icon, so the marks added noise without
                            telling the reader anything */}
                        {kids.map((k, j) => (
                          <Link
                            to={k.url}
                            key={j}
                            className={isHere(k.url) ? "here" : undefined}
                            aria-current={isHere(k.url) ? "page" : undefined}
                          >
                            <span className="dot" aria-hidden="true"></span>
                            <span>{k.label}</span>
                          </Link>
                        ))}
                        </div>
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

// withRouter, so the rail re-reads the path on every navigation rather than
// only on the full page loads its own <a href> links happen to cause.
export default withRouter(DashboardLayout);
