import React, { Component, createRef } from "react";
import { Link, NavLink } from "react-router-dom";
import { Dialog } from "primereact/dialog";
// CSS
import "./Students.scss";

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
import { Dropdown } from "primereact/dropdown";
import { Button } from "primereact/button";

// Properties
const properties = require("../../properties.json");

class SubStudents extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    globalFilter: null,

    // Teacher Identifier
    branchID: properties.branch_id,

    // Data
    data: [],
    getPeriodsList: [],

    // Update Data
    resend: false,

    teacherId: null,
    selectedPeriod: null,
    selectedPeriodId: null,

    visible: false,
    studentId: null,
    studentPassword: null,
    studentEmail: null,
    studentCode: null,

  };
  getPeriods = () => {
    apiFetch("/periods")
      .then((fetchPeriods) => {
        this.setState({
          getPeriodsList: fetchPeriods,
        });
      })
      .catch((error) => console.error("Error:", error));
  };

  getStudents = (periodId) => {
    apiFetch("/students/enrollments", { query: { periodId } })
      .then((fetchStudents) => {
        this.setState({
          data: fetchStudents,
        });
      })
      .catch((error) => console.error("Error:", error));
  };

  viewStudent = (R) => {};

  deleteStudent = (id, studentId, pId) => {
    var _this = this;

    // Confirm Deletion
    var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

    if (con) {
      apiFetch(`/students/enrollments/${id}`, { method: "DELETE" })
        .then(() => {
          _this.toast.current.show({
            severity: "success",
            summary: "تمت العملية",
            detail: "تم الحذف بنجاح",
          });
          this.getStudents(pId);
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

  /** Passwords are bcrypt-hashed now (never shown in plaintext) -- this generates and shows a NEW one. */
  resetStudent = (studentProfileId) => {
    var _this = this;

    var con = window.confirm("هل أنت متأكد من أنك تريد اعادة ضبط كلمة المرور لهذا الطالب؟ سيتم إنشاء كلمة مرور جديدة");

    if (con) {
      apiFetch(`/students/${studentProfileId}/reset-password`, { method: "POST" })
        .then((result) => {
          this.setState({ studentPassword: result.newPassword, visible: true, studentId: studentProfileId });
          _this.toast.current.show({
            severity: "success",
            summary: "تمت العملية",
            detail: "تم إنشاء كلمة مرور جديدة",
          });
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
    this.getPeriods();
    this.getStudents();

    document.title = properties.site_name + " | طلابي";
  }

  //   componentDidUpdate(prevProps, prevState, snapshot) {
  //     if (prevState.resend !== this.state.resend) {
  //       this.getStudents();

  //       this.setState({ resend: false });
  //     }
  //   }

  render() {
    let periodList = this.state.getPeriodsList.map((period) => {
      return { label: period.name, value: period.id };
    });
    const header = (
      <div className="table-header">
        عدد الطلاب
        <span>{this.state.data.length}</span>
        <div className="p-col-12 p-md-4">
          <div className="p-inputgroup">
            <span className="p-float-label">
              <Dropdown
                value={this.state.selectedPeriod}
                options={periodList}
                placeholder="إختر الفترة"
                onChange={(e) => {
                  this.setState({
                    selectedPeriod: e.value,
                    globalFilter: e.lable,
                  });

                  this.getStudents(e.value);
                }}
                optionLabel="label"
                filter
                showClear
                filterBy="label"
              />
            </span>
          </div>
        </div>
        <span className="p-input-icon-left">
          <i className="pi pi-search" />
          <InputText
            type="search"
            onInput={(e) => this.setState({ globalFilter: e.target.value })}
            placeholder="بحث في الطلاب"
          />
        </span>
      </div>
    );

    const actionBodyTemplate = (rowData) => {
      return (
        <React.Fragment>
          <Button
            icon="pi pi-refresh"
            className="p-button-rounded p-button-warning p-ml-2"
            onClick={() => this.resetStudent(rowData.studentProfileId)}
          />
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() => this.deleteStudent(rowData.id, rowData.studentProfileId, rowData.periodId)}
          />
        </React.Fragment>
      );
    };

    

    return (
      <>
        <Header pageName="طلابي" />

        <Toast ref={this.toast}></Toast>

        <div className="content">
          <div className="container">
            <div className="card">
              <Card title="طلابي">
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
                  emptyMessage="لا يوجد طلاب"
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
                    header="رقم التلفون"
                  ></Column>
                  <Column
                    filter
                    sortable
                    field="periodName"
                    header="الفترة"
                  ></Column>

                  <Column
                    header=" اعاده الضبط/رؤيه / بياتات / حذف"
                    body={actionBodyTemplate}
                  ></Column>
                </DataTable>
              </Card>
            </div>
          </div>
          <Dialog

            header="بيانات الطالب"
            visible={this.state.visible}
            style={{ width: "50vw" }}
            onHide={() => this.setState({ visible: false })}
          >
            <div className="p-col-12">
              <label>كلمة المرور الجديدة</label>
              <InputText readOnly tooltip="كلمة المرور الجديدة" value={this.state.studentPassword}></InputText>
              <p>يرجى إرسال كلمة المرور هذه للطالب -- لن تظهر مرة أخرى.</p>
            </div>
          </Dialog>
        </div>
      </>
    );
  }
}

export default SubStudents;
