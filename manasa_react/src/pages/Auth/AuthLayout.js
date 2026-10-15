import React, { Component, createRef } from "react";
import { Link, withRouter } from "react-router-dom";

// CSS
import "../Join/Join.scss";

// Properties
const properties = require("../../properties.json");

// "logo copy.png", not properties.teacher.logo: that one's wordmark is white
// and vanishes on this white panel -- this is the same lockup in dark ink.
// Resolved here rather than with url() in the stylesheet, because the space in
// the filename stops css-loader resolving it and the image silently disappears.
const LOGO = require("../../assets/img/logo copy.png").default;

// Inline strokes rather than PrimeIcons, so each glyph can draw itself in.
const ICONS = {
  video: (
    <>
      <rect x="2" y="5.5" width="13" height="13" rx="2.6" />
      <path d="m15 11.5 6-3.6v8.2l-6-3.6z" />
    </>
  ),
  exam: (
    <>
      <rect x="3.5" y="3.5" width="17" height="17" rx="3" />
      <path d="m8 12 2.6 2.6L16.5 9" />
    </>
  ),
  chart: (
    <>
      <path d="M4 20V4" />
      <path d="M4 20h16" />
      <path d="m7.5 15 3.5-4 3 2.4 4.5-6" />
    </>
  ),
  chat: <path d="M20 13.5a2.5 2.5 0 0 1-2.5 2.5H9l-5 4V6.5A2.5 2.5 0 0 1 6.5 4h11A2.5 2.5 0 0 1 20 6.5z" />,
  cal: (
    <>
      <rect x="3.5" y="5" width="17" height="15.5" rx="2.4" />
      <path d="M8 3v4M16 3v4M3.5 10h17" />
    </>
  ),
  card: (
    <>
      <rect x="2.5" y="5.5" width="19" height="13" rx="2.4" />
      <path d="M2.5 10h19" />
      <path d="M6 15h4" />
    </>
  ),
};

// Each service carries its own accent and its own icon motion, so the six read
// as six things rather than one block repeated. `tint` is the same hue at card
// strength -- kept as a literal because a CSS variable cannot be fed to rgba().
// Descriptions are kept within 44-50 characters of each other so the six cards
// wrap to the same number of lines and the grid reads as one block.
const SERVICES = [
  { icon: "video", anim: "a-pulse", color: "#198baf", tint: "#e4f2f7",
    title: "دروس تفاعلية", desc: "ارفع فيديو وصوت لكل درس، متاح لطلابك بأي وقت ومكان" },
  { icon: "exam", anim: "a-stamp", color: "#2a9d52", tint: "#e4f4e9",
    title: "امتحانات إلكترونية", desc: "أنشئ امتحاناتك وخلي التصحيح فوري بمجرد التسليم" },
  { icon: "chart", anim: "a-climb", color: "#6d5bd0", tint: "#ece9fa",
    title: "متابعة أداء الطلاب", desc: "أرشيف كامل لكل امتحانات طلابك ودرجاتهم بمكان واحد" },
  { icon: "chat", anim: "a-wiggle", color: "#d9682f", tint: "#fbeae0",
    title: "تواصل مباشر", desc: "راسل طلابك واستقبل استفساراتهم وردّ عليهم فوراً" },
  { icon: "cal", anim: "a-flip", color: "#2f6fd0", tint: "#e6eefb",
    title: "جدول الحصص", desc: "نظّم مواعيد حصصك ومحاضراتك وشوف جدولك كاملاً" },
  { icon: "card", anim: "a-swipe", color: "#c9960a", tint: "#fbf1d8",
    title: "إدارة الاشتراكات", desc: "تابع اشتراكات طلابك بالفترات الدراسية بسهولة" },
];

// The whole headline types itself out, erases, then moves to the next line.
// The original headline stays first so the page still opens on it; the rest
// are drawn from the tagline and the service list, nothing invented.
const TYPED = [
  "منصتك التعليمية المتكاملة كمعلم",
  "دروسك وامتحاناتك وطلابك في مكان واحد",
  "دروس تفاعلية لطلابك في أي وقت",
  "امتحانات إلكترونية بتصحيح فوري",
  "جدول حصصك ومتابعة أداء طلابك",
];

const TYPE_SPEED = 70; // ms per character while writing
const ERASE_SPEED = 32; // ms per character while erasing
const HOLD_FULL = 2200; // pause on a finished line
const HOLD_EMPTY = 360; // pause before the next line starts
const TYPE_START = 500; // a beat after the sheet lands

const TILT = 1.6; // degrees the sheet leans towards the pointer

/**
 * The half of /join and / that never changes: the gradient ground, the sheet,
 * the brand, the typing headline and the six service cards.
 *
 * It is mounted ABOVE the route switch (see connector.js), so moving between
 * the sign-in form and the signup form swaps only the panel on the left --
 * the headline keeps typing where it was instead of restarting, and the cards
 * do not replay their entrance.
 */
class AuthLayout extends Component {
  constructor(props) {
    super(props);
    this.sheet = createRef(null);
    this.typeTimer = null;
    this.revealTimer = null;
    this.observer = null;
    this.stillPlease = false;
  }

  state = {
    typedText: "",
    typedIndex: 0,
    erasing: false,
  };

  componentDidMount() {
    this.stillPlease =
      window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;

    // Anyone who asked the OS for less motion gets the first line, standing
    // still -- no typing loop at all.
    if (this.stillPlease) {
      this.setState({ typedText: TYPED[0] });
    } else {
      this.typeTimer = setTimeout(this.tickType, TYPE_START);
    }

    this.watchCards();
  }

  componentWillUnmount() {
    clearTimeout(this.typeTimer);
    clearTimeout(this.revealTimer);
    if (this.observer) this.observer.disconnect();
  }

  // ---- the headline types itself ----
  tickType = () => {
    const { typedText, typedIndex, erasing } = this.state;
    const word = TYPED[typedIndex];

    if (!erasing) {
      if (typedText.length < word.length) {
        this.setState({ typedText: word.slice(0, typedText.length + 1) });
        this.typeTimer = setTimeout(this.tickType, TYPE_SPEED);
      } else {
        this.setState({ erasing: true });
        this.typeTimer = setTimeout(this.tickType, HOLD_FULL);
      }
      return;
    }

    if (typedText.length > 0) {
      this.setState({ typedText: word.slice(0, typedText.length - 1) });
      this.typeTimer = setTimeout(this.tickType, ERASE_SPEED);
    } else {
      this.setState({
        erasing: false,
        typedIndex: (typedIndex + 1) % TYPED.length,
      });
      this.typeTimer = setTimeout(this.tickType, HOLD_EMPTY);
    }
  };

  // ---- cards appear as they come into view ----
  watchCards = () => {
    const cards = document.querySelectorAll(".join-page .svc");
    const showAll = () => cards.forEach((c) => c.classList.add("in"));

    if (this.stillPlease || !("IntersectionObserver" in window)) {
      showAll();
      return;
    }

    this.observer = new IntersectionObserver(
      (entries, obs) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("in");
            obs.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.2 }
    );
    cards.forEach((c) => this.observer.observe(c));

    // safety net: if the observer never fires, nothing stays invisible
    this.revealTimer = setTimeout(showAll, 1800);
  };

  // ---- the sheet leans towards the pointer ----
  handleTilt = (e) => {
    if (this.stillPlease || !this.sheet.current || e.pointerType === "touch") return;
    const el = this.sheet.current;
    const r = el.getBoundingClientRect();
    const px = (e.clientX - r.left) / r.width - 0.5;
    const py = (e.clientY - r.top) / r.height - 0.5;
    el.style.transform = `rotateY(${(px * TILT).toFixed(2)}deg) rotateX(${(-py * TILT).toFixed(2)}deg)`;
  };

  resetTilt = () => {
    if (this.sheet.current) this.sheet.current.style.transform = "";
  };

  // ---- a glow follows the pointer inside a card ----
  handleGlow = (e) => {
    const card = e.currentTarget;
    const r = card.getBoundingClientRect();
    card.style.setProperty("--mx", `${e.clientX - r.left}px`);
    card.style.setProperty("--my", `${e.clientY - r.top}px`);
  };

  render() {
    // site_name carries both halves ("المعلم | لوحة التحكم"); show them as a
    // name and its subtitle instead of printing the pipe inside a heading.
    const [brandName, brandSub] = String(properties.site_name)
      .split("|")
      .map((part) => part.trim());

    // the call to action always points at the form the viewer is NOT on
    const onJoin = this.props.location.pathname === "/join";
    const ctaTo = onJoin ? "/" : "/join";
    const ctaLabel = onJoin ? "عندك حساب؟ سجّل دخولك" : "ما عندك حساب؟ اشترك مجانًا";

    return (
      <div className="join-page">

        {/* slow-drifting shapes behind the sheet */}
        <div className="orbs" aria-hidden="true">
          <span className="o1"></span>
          <span className="o2"></span>
          <span className="o3"></span>
        </div>

        {/* the wrapper carries the gradient halo that turns around the sheet */}
        <div
          className="sheet-wrap"
          onPointerMove={this.handleTilt}
          onPointerLeave={this.resetTilt}
        >
          <div className="sheet" ref={this.sheet}>

            <div className="sheet-left">
              {/* The artwork and the wordmark live in one PNG with the drawing
                  on the left of it, which puts the drawing last in an RTL row.
                  Both halves are therefore drawn as cropped backgrounds (see
                  Join.scss) so the drawing can lead and the wordmark can sit
                  above the subtitle. */}
              <div className="bar">
                <span
                  className="mark"
                  role="img"
                  aria-label={properties.site_name}
                  style={{ backgroundImage: `url(${LOGO})` }}
                ></span>
                <div className="wm">
                  <span
                    className="name"
                    aria-hidden="true"
                    style={{ backgroundImage: `url(${LOGO})` }}
                  ></span>
                  <small>{brandSub || brandName}</small>
                </div>
              </div>

              {/* the whole line types itself, erases, and moves on to the next.
                  The box is a fixed height so nothing below it shifts. */}
              <h1>
                <span className="sr-only">منصتك التعليمية المتكاملة كمعلم</span>
                <span className="typed grad" aria-hidden="true">
                  {this.state.typedText}
                  <b className="caret"></b>
                </span>
              </h1>
              <p className="lede">إدارة دروسك وامتحاناتك وطلابك في مكان واحد</p>

              <Link to={ctaTo} className="hero-cta">{ctaLabel}</Link>

              <div className="svch">خدمات المنصة</div>

              <div className="svcs">
                {SERVICES.map((s, i) => (
                  <div
                    className="svc"
                    key={i}
                    onPointerMove={this.handleGlow}
                    style={{ "--d": `${i * 70}ms`, "--c": s.color, "--c-tint": s.tint }}
                  >
                    <span className={`ic ${s.anim}`}>
                      <svg
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="1.8"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        aria-hidden="true"
                      >
                        {ICONS[s.icon]}
                      </svg>
                    </span>
                    <div className="tx">
                      <b>{s.title}</b>
                      <span>{s.desc}</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* the only part that changes between / and /join */}
            {this.props.children}

          </div>
        </div>
      </div>
    );
  }
}

export default withRouter(AuthLayout);
