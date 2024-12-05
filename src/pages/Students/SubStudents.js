import React, { Component, createRef } from "react";
import { Link, NavLink } from "react-router-dom";
import { Dialog } from "primereact/dialog";
// CSS
import "./Students.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";

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
  getTeatcherId = async () => {
    var data = await JSON.parse(window.localStorage.getItem("currentTeacher"));
    this.setState({ teacherId: data["teacherId"] });
    console.log({ teacher: this.state.teacherId });
  };
  getPeriods = async () => {
    var data = await JSON.parse(window.localStorage.getItem("currentTeacher"));

    // Get Periods From API
    fetch(properties.api_path + "/periods/allPeriods", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        teacherId: data["teacherId"],
      }),
    })
      .then((res) => {
        return res.json();
      })
      .then((fetchStudents) => {
        this.setState({
          getPeriodsList: fetchStudents,
        });
      });
  };
  getStudentData = async () => {
    var data = await JSON.parse(window.localStorage.getItem("currentTeacher"));

    // Get Periods From API
    fetch(properties.api_path + "/student/findstudentlogindata", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        id:this.state.studentId,
      }),
    })
      .then((res) => {
        return res.json();
      })
      .then((fetchStudents) => {
        this.setState({
          studentEmail: fetchStudents.email,
          studentPassword: fetchStudents.password,
          studentCode:fetchStudents.code

        });
      });
  };
  getStudents = async (id) => {
    var data = await JSON.parse(window.localStorage.getItem("currentTeacher"));

    // Get Students From API
    await fetch(properties.api_path + "/studentPeriods/allStudentPeriods", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        teacherId: data["teacherId"],
        periodId: id,
      }),
    })
      .then((res) => {
        return res.json();
      })
      .then((fetchStudents) => {
        this.setState({
          data: fetchStudents,
        });
      });
  };

  viewStudent = (R) => {};

  deleteStudent = (id,studentId, pId) => {
    var _this = this;

    // Confirm Deletion
    var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

    if (con) {
      // Delete Subscription By ID
      fetch(properties.api_path + "/studentPeriods/deleteStudentPeriodsBy", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          id:id,
          studentId: studentId,
          teacherId: this.state.teacherId,
          periodId: pId,
        }),
      })
        .then((res) => {
          return res.json();
        })
        .then((deleteMediaByID) => {
          this.setState({ resend: true, data: deleteMediaByID });
          if (deleteMediaByID.msgType === "success") {
            _this.toast.current.show({
              severity: "success",
              summary: "تمت العملية",
              detail: "تم الحذف بنجاح",
            });
            this.getStudents(pId);
          } else {
            _this.toast.current.show({
              severity: "error",
              summary: "لم تتم العملية",
              detail: "لم يتم الحذف",
            });
          }
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
   resetStudent = (id) => {
  
    var _this = this;

    // Confirm Deletion
    var con = window.confirm("هل أنت متأكد من أنك تريد اعادة ظبط هذا العنصر");

    if (con) {
      // Delete Subscription By ID
      fetch(properties.api_path + "/studentPeriods/resetPassword", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          id: id,
          // password: this.state.studentPassword,
        }),
      })
        .then((res) => {
          return res.json();
        })
        .then((reset) => {
          if (reset.msgType === "success") {
            _this.toast.current.show({
              severity: "success",
              summary: "تمت العملية",
              detail: "تم اعاده الظبط بنجاح",
            });
            this.setState({ visible: false });
          } else {
            _this.toast.current.show({
              severity: "error",
              summary: "لم تتم العملية",
              detail: "الباسورد غلط",
            });
          }
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
    this.getTeatcherId();
    this.getStudents();

    document.title = properties.site_name + " | طلابي";

    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }
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

    const showResetPopUp = (rowData) => {
      this.setState({ visible: true, studentId: rowData.studentId });
     this. getStudentData();

    };

    const actionBodyTemplate = (rowData) => {
      return (
        <React.Fragment>
             <Button
            icon="pi pi-refresh"
            className="p-button-rounded p-button-warning  p-ml-2"
            onClick={() =>{
              this.resetStudent(rowData.id)
              // this.setState({
              //   selectedPeriod: e.value,
              //   globalFilter: e.lable,
              // });

              // this.getStudents(rowData.periodId);
            }
            

            }
          />
          <Button
            icon="pi pi-eye"
            className="p-button-rounded p-button-secondary p-ml-2"
            onClick={() => (window.location.href = `exams/view/${rowData.studentId}`)}
          />
          <Button
            icon="pi pi-pencil"
            className="p-button-rounded p-button-secondary p-ml-2"
            onClick={() => showResetPopUp(rowData)}
          />
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() =>{
              this.deleteStudent(rowData.id,rowData.studentId, rowData.periodId)
              // this.setState({
              //   selectedPeriod: e.value,
              //   globalFilter: e.lable,
              // });

              // this.getStudents(rowData.periodId);
            }
            

            }
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
                    field="schoolStagesName"
                    header="المرحلة الدراسية"
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
            // footer={
            //   <Button
            //     label="Reset "
            //     className="p-button-secondary p-mr-1"
            //     onClick={() => resetStudent()}
            //   ></Button>
            // }
            onHide={() => this.setState({ visible: false })}
          >
            <div  className="p-col-12 ">
            <label>
              البريد الالكتروني
            </label>
          <InputText
              tooltip=" البريد الالكتروني  "
              value={this.state.studentEmail}
              onChange={(i) =>
                this.setState({ studentEmail: i.target.value })
              }
            ></InputText>
              <label>
                    الباسورد  
                                </label>
            <InputText
              tooltip=" كلمة المرور "
              value={this.state.studentPassword}
              onChange={(i) =>
                this.setState({ studentPassword: i.target.value })
              }
            ></InputText>
              <div className="p-col-12 p-md-4">
              <label>
                    كود الطالب  
              </label>
            <InputText
              tooltip=" كود الطلب "
              value={this.state.studentCode}
              onChange={(i) =>
                this.setState({ studentCode: i.target.value })
              }
            ></InputText>
              </div> 
            </div>
        
          
          </Dialog>
        </div>
      </>
    );
  }
}

export default SubStudents;
