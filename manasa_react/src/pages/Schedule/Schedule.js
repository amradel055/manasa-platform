import React, { Component, createRef } from "react";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch, getCurrentUser } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import { Card } from "primereact/card";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import Select from '../../components/Field/Select';
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Dialog } from "primereact/dialog";
import { Calendar } from "primereact/calendar";
import { InputText } from "primereact/inputtext";
// Properties
const properties = require("../../properties.json");

class Schedule extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    // Submit Button
    buttonText: "إضافة ميعاد",
    buttonColor: "",

    // Teacher Identifier
    branchID: properties.branch_id,
    companyID: properties.company_id,
    teacherId: null,
    periodName: null,
    globalFilter: null,
    userInput: null,
    // Get Data
    data: [],
    allGrades: [],
    dataId: [],
    getMaterialsList: [],
    selectedPeriod: null,
    selectedPeriodId: null,
    notes: null,
    post: null,
    type: null,
    day: null,
    active: null,
    schoolStageId: null,
    visible: false,
    editableData: null,
    formContent: {
      branchId: properties.branch_id,
      companyId: properties.company_id,
      thumb: null,
    },
    date1: null,
    date2: null,
    date3: null,
    date4: null,
    selectedMaterial: null,
    selectedDate1: null,
    selectedDate2: null,
    selectedDate3: null,
    selectedDate4: null,
  };

  getMaterial = () => {
    // Get Lesson Locations From API
    apiFetch("/lookup/lesson-locations", { query: { branchId: this.state.branchID } })
      .then((fetchMaterials) => {
        this.setState({
          getMaterialsList: fetchMaterials,
        });
      })
      .catch((error) => console.error("Error:", error));
  };

  componentDidMount() {
    document.title = properties.site_name + " | إضافة ميعاد جديدة";

    this.allAppoinments();
    // this.getPeriods();
    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    this.getMaterial();

    apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
      .then((grades) => {
        this.setState({ allGrades: grades });
      })
      .catch((error) => console.error("Error:", error));

    // Get Periods From API
  }

  allAppoinments = () => {
    var _this = this;
    // Send the data to the server
    apiFetch("/appointments")
      .then((done) => {
        for (var i = 0; i < done.length; i++) {
          done[i].time =
            done[i].time != null
              ? done[i].time.toString().split("T")[1].split("+")[0]
              : null;
          done[i].time2 =
            done[i].time2 != null
              ? done[i].time2.toString().split("T")[1].split("+")[0]
              : null;
          done[i].time3 =
            done[i].time3 != null
              ? done[i].time3.toString().split("T")[1].split("+")[0]
              : null;
          done[i].time4 =
            done[i].time4 != null
              ? done[i].time4.toString().split("T")[1].split("+")[0]
              : null;
        }
        this.setState({ data: done });
      })
      .catch((error) => {
        console.error("Error:", error);
        _this.toast.current.show({
          severity: "error",
          summary: "لم تتم العملية",
          detail: error.message || "خطأ في الإتصال",
        });
      });
  };

  edit = () => {
    var _this = this;
    // Send the data to the server
    apiFetch(`/appointments/${this.state.editableData.id}`, {
      method: "PUT",
      body: {
        appointmentsType: this.state.editableData.appointmentType,
        day: this.state.editableData.day,
        time: this.state.editableData.time,
        time2: this.state.editableData.time2,
        time3: this.state.editableData.time3,
        time4: this.state.editableData.time4,
        lessonLocationId: this.state.editableData.materialsId,
        schoolStageId: this.state.editableData.schoolStageId,
        branchId: this.state.branchID,
        remarks: this.state.notes,
      },
    })
      .then(() => {
        _this.toast.current.show({
          severity: "success",
          summary: "تم بنجاح",
          detail: "تم التعديل بنجاح",
        });
        this.allAppoinments();
        this.setState({ visible: false });
      })
      .catch((error) => {
        console.error("Error:", error);
        _this.toast.current.show({
          severity: "error",
          summary: "لم تتم العملية",
          detail: error.message || "خطأ في الإتصال",
        });
      });
  };

  addSchedule = () => {
    var _this = this;
    // Send the data to the server
    apiFetch("/appointments", {
      method: "POST",
      body: {
        appointmentsType: this.state.type,
        lessonLocationId: this.state.selectedMaterial,
        schoolStageId: this.state.schoolStageId,
        day: this.state.day,
        time: this.state.date1,
        time2: this.state.date2,
        time3: this.state.date3,
        time4: this.state.date4,
        branchId: this.state.branchID,
        remarks: this.state.notes,
      },
    })
      .then(() => {
        _this.toast.current.show({
          severity: "success",
          summary: "تم بنجاح",
          detail: "تمت إضافة الميعاد بنجاح",
        });
        this.allAppoinments();
      })
      .catch((error) => {
        console.error("Error:", error);
        _this.toast.current.show({
          severity: "error",
          summary: "لم تتم العملية",
          detail: error.message || "خطأ في الإتصال",
        });
      });
  };

  render() {
    const typeList = [
      { label: " ثابت", value: 0 },
      { label: " تعويض ", value: 1 },
      { label: "فاينال", value: 2 },
    ];
    const dayList = [
      { label: "السبت", value: 6 },
      { label: " ألاحد ", value: 7 },
      { label: "الاتنين", value: 1 },
      { label: "الثلاثاء", value: 2 },
      { label: "الاربعاء ", value: 3 },
      { label: "الخميس", value: 4 },
      { label: "الجمعه", value: 5 },
    ];

    const active = [
      { label: "مفعل", value: 1 },
      { label: "غير مفعل", value: 0 },
    ];
    const grades = this.state.allGrades.map((grade) => {
      return {
        label: grade.name,
        value: grade.id,
      };
    });

    let materialList = this.state.getMaterialsList.map((period) => {
      return { label: period.name, value: period.id };
    });

    var deletePeriod = (e) => {
      var _this = this;
      // Send the data to the server
      apiFetch(`/appointments/${e.id}`, { method: "DELETE" })
        .then(() => {
          _this.allAppoinments();
          _this.toast.current.show({
            severity: "success",
            summary: "تم بنجاح",
            detail: "تم الحذف بنجاح",
          });
        })
        .catch((error) => {
          console.error("Error:", error);

          _this.setState({
            buttonText: "إضافة ميعاد",
            buttonColor: "",
          });

          _this.toast.current.show({
            severity: "error",
            summary: "لم تتم العملية",
            detail: error.message || "خطأ في الإتصال",
          });
        });
    };

    const header = (
      <div className="container">
        <Card>
          <div className="p-grid p-fluid">
            <div className=" p-md-4">
              <div className="p-inputgroup">
                <span className="p-float-label">
                  <Calendar
                    timeOnly
                    hourFormat="12"
                    value={this.state.date1}
                    onChange={(e) => {
                      this.setState({
                        date1: e.value,
                      });
                    }}
                  ></Calendar>{" "}
                  <label htmlFor="time"> التوقيت </label>
                </span>
              </div>
            </div>
            <div className=" p-md-4">
              <div className="p-inputgroup">
                <span className="p-float-label">
                  <Calendar
                    timeOnly
                    hourFormat="12"
                    value={this.state.date2}
                    onChange={(e) =>
                      this.setState({
                        date2: e.value,
                        // selectedDate2: this.state.date2.toLocaleTimeString(),
                      })
                    }
                  ></Calendar>{" "}
                  <label htmlFor="time"> التوقيت </label>
                </span>
              </div>
            </div>
            <div className=" p-md-4">
              <div className="p-inputgroup">
                <span className="p-float-label">
                  <Calendar
                    timeOnly
                    hourFormat="12"
                    value={this.state.date3}
                    onChange={(e) =>
                      this.setState({
                        date3: e.value,
                        // selectedDate3: this.state.date3.toLocaleTimeString(),
                      })
                    }
                  ></Calendar>{" "}
                  <label htmlFor="time"> التوقيت </label>
                </span>
              </div>
            </div>
            <div className=" p-md-4">
              <div className="p-inputgroup">
                <span className="p-float-label">
                  <Calendar
                    timeOnly
                    hourFormat="12"
                    value={this.state.date4}
                    onChange={(e) =>
                      this.setState({
                        date4: e.value,
                        // selectedDate4: this.state.date4.toLocaleTimeString(),
                      })
                    }
                  ></Calendar>{" "}
                  <label htmlFor="time"> التوقيت </label>
                </span>
              </div>
            </div>

            <div className="p-col-12 p-md-4">
              <div className="p-inputgroup">
                <Select
                  value={this.state.day}
                  options={dayList}
                  onChange={(e) => this.setState({ day: e.value })}
                  name="type"
                  placeholder="اليوم"
                />
              </div>
            </div>

            <span className="p-col-12 p-md-2">
              <Select
                value={this.state.type}
                options={typeList}
                onChange={(e) => this.setState({ type: e.value })}
                name="type"
                placeholder="نوع الميعاد"
              />
            </span>

            <div className="p-col-12 p-md-4">
              <div className="p-inputgroup">
                <span className="p-float-label">
                  <Select
                    value={this.state.schoolStageId}
                    options={grades}
                    onChange={(e) => this.setState({ schoolStageId: e.value })}
                    name="grade"
                    placeholder="المرحلة الدراسية"
                  />
                </span>
              </div>
            </div>
            <div className="p-col-12 p-md-4">
              <div className="p-inputgroup">
                <span className="p-float-label">
                  <Select
                    value={this.state.selectedMaterial}
                    options={materialList}
                    placeholder="إختر المكان"
                    onChange={(e) =>
                      this.setState({ selectedMaterial: e.value })
                    }
                    optionLabel="label"
                    filter
                    showClear
                    filterBy="label"
                  />
                </span>
              </div>
            </div>
            <div className="p-col-12 p-md-4">
              <div className="p-inputgroup">
                <span className="p-float-label">
                  <InputText
                    id="notes"
                    value={this.state.notes}
                    name="notes"
                    type="text"
                    onChange={(e) => {
                      this.setState({ notes: e.target.value });
                    }}
                  />
                  <label htmlFor="notes">الملاحظات </label>
                </span>
              </div>
            </div>
            <div className="p-col-12 p-md-12 p-mt-2">
              <div className="p-inputgroup">
                <span className="p-float-label">
                  <Button
                    className={`add-button ${this.state.buttonColor}`}
                    label={this.state.buttonText}
                    onClick={this.addSchedule}
                  />
                </span>
              </div>
            </div>
          </div>
        </Card>
      </div>
    );

    const actionBodyTemplate = (rowData) => {
      return (
        <React.Fragment>
          <Button
            icon="pi pi-pencil"
            className="p-button-rounded p-button-secondary p-ml-2"
            onClick={() =>
              this.setState({
                visible: true,
                editableData: rowData,
                selectedPeriod: null,
              })
            }
          />
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() => deletePeriod(rowData)}
          />
        </React.Fragment>
      );
    };

    return (
      <>
        <Header pageName="المواعيد" />
        <Toast ref={this.toast}></Toast>
        <div className="content">
          <div className="container">
            <div className="card">
              <Card title="إضافة ميعاد جديد">
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
                  emptyMessage="لا يوجد مواعيد"
                >
                  <Column field="dayText" header="اليوم"></Column>
                  <Column field="time" header="التوقيت"></Column>
                  <Column field="time2" header="التوقيت"></Column>
                  <Column field="time3" header="التوقيت"></Column>
                  <Column field="time4" header="التوقيت"></Column>
                  <Column field="lessonLocation.name" header="المكان"></Column>
                  <Column
                    header="تعديل / حذف"
                    body={actionBodyTemplate}
                  ></Column>
                </DataTable>
              </Card>
            </div>
          </div>
          <Dialog
            header="تعديل"
            visible={this.state.visible}
            style={{ width: "60vw" }}
            footer={
              <Button
                label="تعديل "
                className="p-button-secondary p-mr-1"
                onClick={
                  this.state.editableData !== null ? () => this.edit() : null
                }
              ></Button>
            }
            onHide={() => this.setState({ visible: false })}
          >
            <>
              {this.state.editableData !== null ? (
                <div className="container">
                  <Card>
                    <div className="p-grid p-fluid">
                      <div className=" p-md-4">
                        <div className="p-inputgroup">
                          <span className="p-float-label">
                            <Calendar
                              timeOnly
                              hourForm
                              at="24"
                              value={this.state.editableData["time"]}
                              onChange={(e) =>
                                this.setState({
                                  editableData: {
                                    ...this.state.editableData,
                                    time: e.value,
                                  },
                                })
                              }
                            ></Calendar>{" "}
                            <label htmlFor="time"> التوقيت </label>
                          </span>
                        </div>
                      </div>
                      <div className=" p-md-4">
                        <div className="p-inputgroup">
                          <span className="p-float-label">
                            <Calendar
                              timeOnly
                              hourForm
                              at="24"
                              value={this.state.editableData["time2"]}
                              onChange={(e) =>
                                this.setState({
                                  editableData: {
                                    ...this.state.editableData,
                                    time2: e.value,
                                  },
                                })
                              }
                            ></Calendar>{" "}
                            <label htmlFor="time"> التوقيت </label>
                          </span>
                        </div>
                      </div>
                      <div className=" p-md-4">
                        <div className="p-inputgroup">
                          <span className="p-float-label">
                            <Calendar
                              timeOnly
                              hourForm
                              at="24"
                              value={this.state.editableData["time3"]}
                              onChange={(e) =>
                                this.setState({
                                  editableData: {
                                    ...this.state.editableData,
                                    time3: e.value,
                                  },
                                })
                              }
                            ></Calendar>{" "}
                            <label htmlFor="time"> التوقيت </label>
                          </span>
                        </div>
                      </div>
                      <div className=" p-md-4">
                        <div className="p-inputgroup">
                          <span className="p-float-label">
                            <Calendar
                              timeOnly
                              hourForm
                              at="24"
                              value={this.state.editableData["time4"]}
                              onChange={(e) =>
                                this.setState({
                                  editableData: {
                                    ...this.state.editableData,
                                    time4: e.value,
                                  },
                                })
                              }
                            ></Calendar>{" "}
                            <label htmlFor="time"> التوقيت </label>
                          </span>
                        </div>
                      </div>

                      <div className="p-col-12 p-md-4">
                        <div className="p-inputgroup">
                          <Select
                            value={this.state.editableData["day"]}
                            options={dayList}
                            onChange={(e) =>
                              this.setState({
                                editableData: {
                                  ...this.state.editableData,
                                  day: e.value,
                                },
                              })
                            }
                            name="type"
                            placeholder="اليوم"
                          />
                        </div>
                      </div>

                      <span className="p-col-12 p-md-2">
                        <Select
                          value={this.state.editableData["appointmentType"]}
                          options={typeList}
                          onChange={(e) =>
                            this.setState({
                              editableData: {
                                ...this.state.editableData,
                                appointmentType: e.value,
                              },
                            })
                          }
                          name="type"
                          placeholder="نوع الميعاد"
                        />
                      </span>

                      <div className="p-col-12 p-md-4">
                        <div className="p-inputgroup">
                          <span className="p-float-label">
                            <Select
                              value={this.state.editableData["schoolStageId"]}
                              options={grades}
                              onChange={(e) =>
                                this.setState({
                                  editableData: {
                                    ...this.state.editableData,
                                    schoolStageId: e.value,
                                  },
                                })
                              }
                              name="grade"
                              placeholder="المرحلة الدراسية"
                            />
                          </span>
                        </div>
                      </div>
                      <div className="p-col-12 p-md-4">
                        <div className="p-inputgroup">
                          <span className="p-float-label">
                            <Select
                              value={
                                this.state.editableData["lessonLocationId"]
                              }
                              options={materialList}
                              placeholder="إختر المكان"
                              onChange={(e) =>
                                this.setState({
                                  editableData: {
                                    ...this.state.editableData,
                                    materialsId: e.value,
                                  },
                                })
                              }
                              optionLabel="label"
                              filter
                              showClear
                              filterBy="label"
                            />
                          </span>
                        </div>
                      </div>
                    </div>
                  </Card>
                </div>
              ) : null}
            </>
          </Dialog>
        </div>
      </>
    );
  }
}

export default Schedule;
