import React, { Component, createRef } from "react";
import { Link, NavLink } from "react-router-dom";
// CSS
import "./Students.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import TableShell from "../../components/Table/TableShell";
import TableEmpty from "../../components/Table/TableEmpty";
import Select from "../../components/Field/Select";
import Modal from "../../components/Modal/Modal";
import Confirm from "../../components/Modal/Confirm";
import "../../components/Field/Form.scss";
import { Toast } from "primereact/toast";
import { Card } from "primereact/card";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { InputText } from "primereact/inputtext";
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

    // the rows and the period list are both fetched on mount
    tableLoading: true,
    periodsLoading: true,

    // A request is in flight. One flag each, so a slow reset cannot grey out
    // a delete, and neither can grey out the other's button.
    deleting: false,
    resetting: false,

    // The row waiting on a yes, or null -- one for each question this page
    // asks. The row itself rather than a boolean, so the question can name
    // the student it is about.
    confirmDelete: null,
    confirmReset: null,
  };
  getPeriods = () => {
    // inside the fetch, so opening the list reports the wait too
    this.setState({ periodsLoading: true });

    apiFetch("/periods")
      .then((fetchPeriods) => {
        this.setState({
          getPeriodsList: fetchPeriods,
          periodsLoading: false,
        });
      })
      .catch((error) => {
        // the wait ends on failure too, or the field spins for ever
        this.setState({ periodsLoading: false });
        console.error("Error:", error);
      });
  };

  getStudents = (periodId) => {
    // Every call announces itself, not just the first: this runs again after
    // a delete, and a silent refetch reads as though nothing happened. The
    // rows go with it so the waiting row has somewhere to appear -- the
    // empty state only renders while the table is empty.
    this.setState({ tableLoading: true, data: [] });

    apiFetch("/students/enrollments", { query: { periodId } })
      .then((fetchStudents) => {
        this.setState({
          data: fetchStudents,
          tableLoading: false,
        });
      })
      .catch((error) => {
        this.setState({ tableLoading: false });
        console.error("Error:", error);
      });
  };

  viewStudent = (R) => {};

  // The confirmation that used to be window.confirm is the Confirm modal in
  // the render below. The gate is the same -- this still only runs on a yes
  // -- and the request, the toasts and the refetch under it are unchanged.
  deleteStudent = (id, studentId, pId) => {
    var _this = this;

    this.setState({ deleting: true });

    apiFetch(`/students/enrollments/${id}`, { method: "DELETE" })
      .then(() => {
        this.setState({ deleting: false, confirmDelete: null });
        _this.toast.current.show({
          severity: "success",
          summary: "تمت العملية",
          detail: "تم الحذف بنجاح",
        });
        this.getStudents(pId);
      })
      .catch((error) => {
        // the question stays open on failure, so it can be retried
        this.setState({ deleting: false });
        console.error("Error:", error);

        _this.toast.current.show({
          severity: "error",
          summary: "لم تتم العملية",
          detail: "خطأ في الإتصال",
        });
      });
  };

  /** Passwords are bcrypt-hashed now (never shown in plaintext) -- this generates and shows a NEW one. */
  resetStudent = (studentProfileId) => {
    var _this = this;

    this.setState({ resetting: true });

    apiFetch(`/students/${studentProfileId}/reset-password`, { method: "POST" })
      .then((result) => {
        // the question closes and the new password takes its place
        this.setState({
          studentPassword: result.newPassword,
          visible: true,
          studentId: studentProfileId,
          resetting: false,
          confirmReset: null,
        });
        _this.toast.current.show({
          severity: "success",
          summary: "تمت العملية",
          detail: "تم إنشاء كلمة مرور جديدة",
        });
      })
      .catch((error) => {
        this.setState({ resetting: false });
        console.error("Error:", error);

        _this.toast.current.show({
          severity: "error",
          summary: "لم تتم العملية",
          detail: "خطأ في الإتصال",
        });
      });
  };

 

  componentDidMount() {
    // Not fetched here any more: the period list fetches when it is opened
    // (see the Select's onOpen), and doing both asked for it twice.
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
        <span className="p-input-icon-left m-tool-search">
          <i className="pi pi-search" />
          <InputText
            type="search"
            onInput={(e) => this.setState({ globalFilter: e.target.value })}
            placeholder="بحث في الطلاب"
          />
        </span>

        <div className="m-tool-stage">
          <Select
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
            showClear
            loading={this.state.periodsLoading}
            onOpen={this.getPeriods}
          />
        </div>

        {/* the count was loose text between the controls; it is a figure, so
            it is drawn as one */}
        <span className="m-tool-count">
          عدد الطلاب <b>{this.state.data.length}</b>
        </span>
      </div>
    );

    const actionBodyTemplate = (rowData) => {
      return (
        <React.Fragment>
          <Button
            icon="pi pi-refresh"
            className="p-button-rounded p-button-warning p-ml-2"
            onClick={() => this.setState({ confirmReset: rowData })}
          />
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() => this.setState({ confirmDelete: rowData })}
          />
        </React.Fragment>
      );
    };

    

    return (
      <>
        <Header pageName="طلابي" />

        <Toast ref={this.toast}></Toast>

        {/* the two questions that used to be the browser's */}
        <Confirm
          visible={this.state.confirmDelete !== null}
          onHide={() => this.setState({ confirmDelete: null })}
          onConfirm={() =>
            this.deleteStudent(
              this.state.confirmDelete.id,
              this.state.confirmDelete.studentProfileId,
              this.state.confirmDelete.periodId
            )
          }
          busy={this.state.deleting}
          title="حذف اشتراك طالب"
          message={`هل أنت متأكد من حذف «${this.state.confirmDelete ? this.state.confirmDelete.studentName : ""}» من هذه الفترة؟`}
          note="سيفقد الطالب وصوله إلى حصص هذه الفترة. لا يمكن التراجع عن هذه الخطوة."
          confirmLabel="نعم، احذف"
          busyLabel="جارٍ الحذف…"
        />

        <Confirm
          visible={this.state.confirmReset !== null}
          onHide={() => this.setState({ confirmReset: null })}
          onConfirm={() => this.resetStudent(this.state.confirmReset.studentProfileId)}
          busy={this.state.resetting}
          tone="brand"
          icon="pi pi-refresh"
          title="إعادة ضبط كلمة المرور"
          message={`إنشاء كلمة مرور جديدة لـ «${this.state.confirmReset ? this.state.confirmReset.studentName : ""}»؟`}
          note="كلمة المرور الحالية ستتوقف عن العمل فوراً، وستظهر الجديدة مرة واحدة فقط."
          confirmLabel="نعم، أنشئ كلمة مرور"
          busyLabel="جارٍ الإنشاء…"
        />

        <div className="content">
          <div className="container">
            <div className="card">
              {/* no title: the page header above already says it */}
              <Card>
                {/* presentation only -- see components/Table/TableShell.js */}
                <TableShell>
                <DataTable
                  header={header}
                  sortable
                  globalFilter={this.state.globalFilter}
                  className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                  value={this.state.data}
                  paginator
                  paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                  rows={10}
                  rowsPerPageOptions={[10, 20, 50]}
                  currentPageReportTemplate="عرض {first} – {last} من {totalRecords} طالب"
                  emptyMessage={
                    <TableEmpty
                      loading={this.state.tableLoading}
                      icon="pi pi-user"
                      title="لا يوجد طلاب"
                      hint="اختر فترة من القائمة أعلاه، أو فعّل اشتراك طالب جديد"
                    />
                  }
                >
                  {/* no column filters: the search above covers all three of
                      these columns and the period picker covers the third on
                      its own, and the empty boxes cost a whole row above every
                      table without saying what they filtered */}
                  <Column
                    sortable
                    field="studentName"
                    header="إسم الطالب"
                  ></Column>
                  <Column
                    sortable
                    field="studentMobile"
                    header="رقم التلفون"
                  ></Column>
                  <Column
                    sortable
                    field="periodName"
                    header="الفترة"
                  ></Column>

                  <Column
                    header="الإجراءات"
                    headerStyle={{ width: "124px" }}
                    bodyStyle={{ width: "124px" }}
                    body={actionBodyTemplate}
                  ></Column>
                </DataTable>
                </TableShell>
              </Card>
            </div>
          </div>
          {/* shown once, and only once: there is no second chance to read
              it, so it gets its own modal rather than a toast */}
          <Modal
            visible={this.state.visible}
            onHide={() => this.setState({ visible: false })}
            title="بيانات الطالب"
            subtitle="كلمة المرور الجديدة — انسخها الآن"
            actions={<Button label="تم" onClick={() => this.setState({ visible: false })} />}
          >
            <div className="m-form">
              <div className="m-grid">
                <div className="m-fld">
                  <label htmlFor="new-password">كلمة المرور الجديدة</label>
                  <InputText id="new-password" dir="ltr" readOnly tooltip="كلمة المرور الجديدة" value={this.state.studentPassword}></InputText>
                  <span className="m-note">يرجى إرسال كلمة المرور هذه للطالب -- لن تظهر مرة أخرى.</span>
                </div>
              </div>
            </div>
          </Modal>
        </div>
      </>
    );
  }
}

export default SubStudents;
