import React, { Component, createRef } from "react";

// CSS
import "./Lessons.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch, getCurrentUser } from "../../Helper/Api";

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

class AllLessons extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    globalFilter: null,

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
    // teacherUserId is resolved from the JWT on the server -- no need to send it.
    apiFetch("/lessons")
      .then((fetchLessons) => {
        this.setState({
          data: fetchLessons,
        });
      })
      .catch((error) => {
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
        كل الدروس
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

    const actionBodyTemplate = (rowData) => {
      return (
        <React.Fragment>
          <Button
            icon="pi pi-pencil"
            className="p-button-rounded p-button-secondary p-ml-2"
            onClick={() =>
              (window.location.href = `lessons/edit/${rowData.id}`)
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
        <Header pageName="كل الدروس" />

        <Toast ref={this.toast}></Toast>

        <div className="content">
          <div className="container">
            <div className="card">
              <Card title="كل الدروس">
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
                  emptyMessage="لا يوجد دروس"
                >
                  <Column
                    filter
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
                    filter
                    sortable
                    field="lessonType"
                    body={(row) => this.state.lessonTypes[row.lessonType]}
                    header="نوع الدرس"
                  ></Column>
                  <Column
                    header="تعديل / حذف"
                    body={actionBodyTemplate}
                  ></Column>
                </DataTable>
              </Card>
            </div>
          </div>
        </div>
      </>
    );
  }
}

export default AllLessons;
