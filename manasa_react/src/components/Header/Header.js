import React, { Component } from "react";

// Components
import { Menubar } from "primereact/menubar";
import { Button } from "primereact/button";

// Helper
import { clearSession } from "../../Helper/Api";

// CSS
import "./Header.scss";
import "primereact/resources/themes/saga-blue/theme.css";
import "primereact/resources/primereact.min.css";
import "primeicons/primeicons.css";

// Properties
const properties = require("../../properties.json");

class Header extends Component {
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
    const logoImage = (
      <a href="/">
        <img
          className="p-mr-4"
          src={require(`../../assets/img/${properties.teacher.logo}`).default}
          alt={properties.teacher.name}
        />
      </a>
    );
    const logout = (
      <Button
        className="p-col-20 p-mr-1"
        label="تسجيل الخروج"
        icon="pi pi-power-off"
        onClick={this.logout}
      />
    );
    const items = [
      {
        label: "الحصص",
        icon: "pi pi-fw pi-video",
        items: [
          {
            label: "إضافة حصة",
            icon: "pi pi-fw pi-plus",
            url: `/lessons/add`,
          },
          {
            separator: true,
          },
          {
            label: "كل الحصص",
            icon: "pi pi-fw pi-list",
            url: `/lessons`,
          },
        ],
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
      <header>
        <div className="top-bar">
        <Menubar
            className="container no-background"
            start={logoImage}
            model={items}
            end={logout}
          />
          {/* <Menubar
            className="container no-background"
       
         
          /> */}
        </div>

        <div className="banner">
          <div className="container">
            <h3>{this.props.pageName}</h3>
        
          </div>
        </div>
      </header>
    );
  }
}

export default Header;
