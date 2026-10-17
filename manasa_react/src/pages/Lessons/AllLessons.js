import React, { Component, createRef } from "react";
import { withRouter } from 'react-router-dom';

// CSS
import "./Lessons.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch, getCurrentUser } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import TableShell from "../../components/Table/TableShell";
import TableEmpty from "../../components/Table/TableEmpty";
import SlidePanel from "../../components/Panel/SlidePanel";
import AddLesson from "./AddLesson";
import { Toast } from "primereact/toast";
import { Card } from "primereact/card";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";

// Properties
const properties = require("../../properties.json");

class AllLessons extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    globalFilter: null,

    // whether the add-a-lesson panel is open. Nothing else about adding a
    // lesson lives here: the panel mounts the very same AddLesson component
    // the /lessons/add route mounts, with its own state and its own submit.
    addOpen: false,

    // The rows are fetched; until they arrive the table says so rather than
    // claiming there are none. Only the DataTable's `loading` reads it.
    tableLoading: true,

    // Teacher Identifier
    branchID: properties.branch_id,
    companyID: properties.company_id,

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

    // Lesson Type
    lessonTypes: ["مدفوع", "مجاني"],

    // Data
    data: [],
    stagesById: {},

    // Update Data
    resend: false,
  };

  getLessons = () => {
    // Every call announces itself, not just the first: this runs again after
    // an add or a delete, and a refetch that showed nothing looked as though
    // the action had not registered. The rows are cleared with it so the
    // waiting row has somewhere to appear -- TableEmpty only renders when
    // the table is empty.
    this.setState({ tableLoading: true, data: [] });

    // teacherUserId is resolved from the JWT on the server -- no need to send it.
    apiFetch("/lessons")
      .then((fetchLessons) => {
        this.setState({
          data: fetchLessons,
          tableLoading: false,
        });
      })
      .catch((error) => {
        // the wait ends on failure too, or the table spins for ever
        this.setState({ tableLoading: false });
        console.error("Error:", error);
      });
  };

  getStages = () => {
    apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
      .then((stages) => {
        const stagesById = {};
        stages.forEach((s) => {
          stagesById[s.id] = s.name;
        });
        this.setState({ stagesById });
      })
      .catch((error) => console.error("Error:", error));
  };

  deleteMedia = (id) => {
    var _this = this;

    // Confirm Deletion
    var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

    if (con) {
      // Delete Lesson By ID
      apiFetch(`/lessons/${id}`, { method: "DELETE" })
        .then(() => {
          _this.toast.current.show({
            severity: "success",
            summary: "تمت العملية",
            detail: "تم الحذف بنجاح",
          });
          this.setState({ resend: true });
          this.getLessons();
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
    document.title = properties.site_name + " | كل الدروس";

    this.getStages();
    this.getLessons();
  }

  // componentDidUpdate(prevProps, prevState, snapshot) {
  //     if (prevState.resend !== this.state.resend) {
  //         this.getLessons();

  //         this.setState({resend: false});
  //     }
  // }

  render() {
    const header = (
      <div className="table-header">
        {/* no heading here: the page header above carries it, and it was the
            same four words twice, twenty pixels apart */}
        <span className="p-input-icon-left">
          <i className="pi pi-search" />
          <InputText
            type="search"
            onInput={(e) => this.setState({ globalFilter: e.target.value })}
            placeholder="بحث في الدروس"
          />
        </span>
      </div>
    );

    // const editProduct = (product) => {
    //     setProduct({...product});
    //     setProductDialog(true);
    // }

    // const confirmDeleteProduct = (product) => {
    //     setProduct(product);
    //     setDeleteProductDialog(true);
    // }

    // Mounted only while the panel is open, so each opening starts from a
    // clean form and its lookups are fetched then -- exactly as arriving at
    // /lessons/add does. That route still works and is untouched.
    const openAdd = () => this.setState({ addOpen: true });
    const closeAdd = () => this.setState({ addOpen: false });

    const actionBodyTemplate = (rowData) => {
      return (
        <React.Fragment>
          <Button
            icon="pi pi-pencil"
            className="p-button-rounded p-button-secondary p-ml-2"
            onClick={() =>
              this.props.history.push(`/lessons/edit/${rowData.id}`)
            }
          />
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() => this.deleteMedia(rowData.id)}
          />
        </React.Fragment>
      );
    };

    return (
      <>
        <Header
          pageName="كل الدروس"
          action={{ label: "إضافة حصة", icon: "pi pi-plus", onClick: openAdd }}
        />

        <Toast ref={this.toast}></Toast>

        <SlidePanel
          visible={this.state.addOpen}
          onHide={closeAdd}
          title="إضافة حصة"
          subtitle="املأ بيانات الحصة، وستظهر في الجدول بعد الحفظ"
        >
          {this.state.addOpen ? <AddLesson /> : null}
        </SlidePanel>

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
                    // The report was already configured below but never shown:
                    // PrimeReact's default template leaves CurrentPageReport
                    // out, so the count has been silently dropped all along.
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                    rows={10}
                    rowsPerPageOptions={[10, 20, 50]}
                    currentPageReportTemplate="عرض {first} – {last} من {totalRecords} درس"
                    emptyMessage={
                      <TableEmpty
                        loading={this.state.tableLoading}
                        icon="pi pi-video"
                        title="لا يوجد دروس"
                        hint="ابدأ بإضافة أول حصة، وستظهر هنا لك ولطلابك"
                        action={{ label: "إضافة حصة", onClick: openAdd }}
                      />
                    }
                  >
                    {/* no column filter: the search above already covers all
                        three columns, and the two empty boxes it put inside the
                        header row said nothing about what they filtered */}
                    <Column
                      sortable
                      field="lessonName"
                      header="إسم الدرس"
                    ></Column>
                    <Column
                      sortable
                      field="schoolStagesGuideId"
                      body={(row) => this.state.stagesById[row.schoolStagesGuideId] || ""}
                      header="المرحلة الدراسية"
                    ></Column>
                    {/* <Column filter sortable field="monthId" body={(row) => this.state.namedMonths[row.monthId - 1]} header="الفترة"></Column> */}
                    <Column
                      sortable
                      field="lessonType"
                      body={(row) => this.state.lessonTypes[row.lessonType]}
                      header="نوع الدرس"
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
        </div>
      </>
    );
  }
}

// withRouter: this page is mounted as a Route's child, so it is handed
// no router props of its own and cannot reach history without it.
export default withRouter(AllLessons);