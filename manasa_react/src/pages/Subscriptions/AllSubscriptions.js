import React, { Component, createRef } from "react";

// CSS
import "./Subscription.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import { Toast } from "primereact/toast";
import { Card } from "primereact/card";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";

// Properties
const properties = require("../../properties.json");

class AllSubscriptions extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    globalFilter: null,

    // Teacher Identifier
    branchID: properties.branch_id,

    // Named Months
    namedMonths: [
      "يناير",
      "فبراير",
      "مارس",
      "إبريل",
      "مايو",
      "يونيو",
      "يوليو",
      "أغسطس",
      "سبتمبر",
      "أكتوبر",
      "نوفمبر",
      "ديسمبر",
    ],

    // Data
    data: [],

    // Update Data
    resend: false,
  };

  getSubscriptions = () => {
    apiFetch("/students/enrollments")
      .then((fetchStudents) => {
        this.setState({
          data: fetchStudents,
        });
      })
      .catch((error) => console.error("Error:", error));
  };

  deleteSubscription = (id) => {
    var _this = this;

    // Confirm Deletion
    var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

    if (con) {
      apiFetch(`/students/enrollments/${id}`, { method: "DELETE" })
        .then(() => {
          this.setState({ resend: true });
          _this.toast.current.show({
            severity: "success",
            summary: "تمت العملية",
            detail: "تم الحذف بنجاح",
          });
          this.getSubscriptions()
        })
        .catch((error) => {
          console.error("Error:", error);

          _this.toast.current.show({
            severity: "error",
            summary: "لم تتم العملية",
            detail: "خطأ في الإتصال",
          });
        });
    }
  };

  componentDidMount() {
    document.title = properties.site_name + " | كل إشتراكات الشهور";

    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    this.getSubscriptions();
  }

  // componentDidUpdate(prevProps, prevState, snapshot) {
  //     if (prevState.resend !== this.state.resend) {
  //         this.getSubscriptions();

  //         this.setState({resend: false});
  //     }
  // }

  render() {
    const header = (
      <div className="table-header">
        عدد الإشتراكات
        <span>{this.state.data.length}</span>
        <span className="p-input-icon-left">
          <i className="pi pi-search" />
          <InputText
            type="search"
            onInput={(e) => this.setState({ globalFilter: e.target.value })}
            placeholder="بحث في الإشتراكات"
          />
        </span>
      </div>
    );

    const actionBodyTemplate = (rowData) => {
      return (
        <React.Fragment>
          {/* <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => window.location.href = `subscriptions/edit/${rowData.id}`} /> */}
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() => this.deleteSubscription(rowData.id)}
          />
        </React.Fragment>
      );
    };

    return (
      <>
        <Header pageName="كل إشتراكات الشهور" />

        <Toast ref={this.toast}></Toast>

        <div className="content">
          <div className="container">
            <div className="card">
              <Card title="كل إشتراكات الشهور">
                <DataTable
                  header={header}
                  sortable
                  globalFilter={this.state.globalFilter}
                  className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                  value={this.state.data}
                  paginator
                  rows={10}
                  rowsPerPageOptions={[10, 20, 50]}
                  currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
                  emptyMessage="لا يوجد مدفوعات"
                >
                  <Column
                    filter
                    sortable
                    field="studentName"
                    header="إسم الطالب"
                  ></Column>
                  <Column
                    filter
                    sortable
                    field="studentMobile"
                    header="رقم الموبايل"
                  ></Column>
                  {/* <Column
                    filter
                    sortable
                    field="monthId"
                    body={(row) => this.state.namedMonths[row.monthId - 1]}
                    header="الشهر"
                  ></Column> */}
                  <Column
                    filter
                    sortable
                    field="value"
                    header="المبلغ"
                  ></Column>
                  <Column header="حذف" body={actionBodyTemplate}></Column>
                </DataTable>
              </Card>
            </div>
          </div>
        </div>
      </>
    );
  }
}

export default AllSubscriptions;
