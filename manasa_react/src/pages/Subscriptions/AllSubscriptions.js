import React, { Component, createRef } from "react";

// CSS
import "./Subscription.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import TableShell from "../../components/Table/TableShell";
import TableEmpty from "../../components/Table/TableEmpty";
import Modal from "../../components/Modal/Modal";
import Confirm from "../../components/Modal/Confirm";
import AddSubscription from "./AddSubscription";
import "../../components/Field/Form.scss";
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

    // whether the add-a-subscription modal is open. The modal mounts the very
    // same AddSubscription component the /subscriptions/add route mounts,
    // with its own state and its own submit -- that route still works.
    addOpen: false,

    // the rows are fetched on mount
    tableLoading: true,

    // a delete is in flight, and the row waiting on a yes
    deleting: false,
    confirmRow: null,

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
    // Every call, not just the first: this runs again after a delete, and a
    // silent refetch reads as though nothing happened. The rows go with it
    // so the waiting row has somewhere to appear.
    this.setState({ tableLoading: true, data: [] });

    apiFetch("/students/enrollments")
      .then((fetchStudents) => {
        this.setState({
          data: fetchStudents,
          tableLoading: false,
        });
      })
      .catch((error) => {
        // the wait ends on failure too, or the table waits for ever
        this.setState({ tableLoading: false });
        console.error("Error:", error);
      });
  };

  // The confirmation that used to be window.confirm is the Confirm modal in
  // the render below. The gate is the same -- this still only runs on a yes
  // -- and the request, the toasts and the refetch under it are unchanged.
  deleteSubscription = (id) => {
    var _this = this;

    this.setState({ deleting: true });

    apiFetch(`/students/enrollments/${id}`, { method: "DELETE" })
      .then(() => {
        this.setState({ resend: true, deleting: false, confirmRow: null });
        _this.toast.current.show({
          severity: "success",
          summary: "تمت العملية",
          detail: "تم الحذف بنجاح",
        });
        this.getSubscriptions()
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
        <span className="p-input-icon-left m-tool-search">
          <i className="pi pi-search" />
          <InputText
            type="search"
            onInput={(e) => this.setState({ globalFilter: e.target.value })}
            placeholder="بحث في الإشتراكات"
          />
        </span>

        <span className="m-tool-count">
          عدد الإشتراكات <b>{this.state.data.length}</b>
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
            title="حذف الإشتراك"
            onClick={() => this.setState({ confirmRow: rowData })}
          />
        </React.Fragment>
      );
    };

    return (
      <>
        <Header
          pageName="كل إشتراكات الشهور"
          action={{ label: "إشتراك فترة جديد", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
        />

        <Toast ref={this.toast}></Toast>

        {/* The same AddSubscription the /subscriptions/add route mounts, in
            a modal instead of a page of its own. It is mounted only while the
            modal is open, so each opening starts from a clean form and its
            lookups are fetched then -- exactly as arriving at the route does.
            `slot` gives its submit button somewhere to land in the footer. */}
        <Modal
          visible={this.state.addOpen}
          onHide={() => this.setState({ addOpen: false })}
          title="إشتراك فترة جديد"
          subtitle="اختر المرحلة والمادة والفترة، ثم الطالب والسعر"
          width={780}
          slot
        >
          {this.state.addOpen ? <AddSubscription /> : null}
        </Modal>

        {/* the question that used to be the browser's */}
        <Confirm
          visible={this.state.confirmRow !== null}
          onHide={() => this.setState({ confirmRow: null })}
          onConfirm={() => this.deleteSubscription(this.state.confirmRow.id)}
          busy={this.state.deleting}
          title="حذف إشتراك"
          message={`هل أنت متأكد من حذف إشتراك «${this.state.confirmRow ? this.state.confirmRow.studentName : ""}»؟`}
          note="سيفقد الطالب وصوله إلى حصص هذه الفترة. لا يمكن التراجع عن هذه الخطوة."
          confirmLabel="نعم، احذف"
          busyLabel="جارٍ الحذف…"
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
                  currentPageReportTemplate="عرض {first} – {last} من {totalRecords} إشتراك"
                  emptyMessage={
                    <TableEmpty
                      loading={this.state.tableLoading}
                      icon="pi pi-calendar"
                      title="لا يوجد مدفوعات"
                      hint="سجّل أول إشتراك فترة لطالب، وسيظهر هنا"
                      action={{ label: "إشتراك فترة جديد", onClick: () => this.setState({ addOpen: true }) }}
                    />
                  }
                >
                  {/* no column filters: the search above covers these */}
                  <Column
                    sortable
                    field="studentName"
                    header="إسم الطالب"
                  ></Column>
                  <Column
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
                    sortable
                    field="value"
                    header="المبلغ"
                  ></Column>
                  <Column
                    header="الإجراءات"
                    headerStyle={{ width: "92px" }}
                    bodyStyle={{ width: "92px" }}
                    body={actionBodyTemplate}
                  ></Column>
                </DataTable>
                </TableShell>
              </Card>
            </div>
          </div>
        </div>
      </>
    );
  }
}

export default AllSubscriptions;
