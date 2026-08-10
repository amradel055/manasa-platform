import React, { Component, createRef } from "react";

// CSS
import "./Subscription.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import { Dropdown } from "primereact/dropdown";

// Properties
const properties = require("../../properties.json");

class AddSubscriptions extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    // Submit Button
    buttonText: "إضافة الإشتراك",
    buttonColor: "",

    // Teacher Identifier
    branchID: properties.branch_id,
    companyID: properties.company_id,

    // Get Data
    getStudentNames: [],
    getPeriodsList: [],
    getMaterialsList: [],
    getSchoolStages: [],
    getLessons: [],
    getPeriods: [],
    getMaterialsList: [],
    // Inputs Data
    selectedStudent: "",
    selectedStage: null,
    selectedPeriod: null,
    selectedMaterial: null,
    selectedPeriodId: null,
    cost: "",
    teacherId: null,
  };

  getLessonsByStage = (schoolStagesId, materialId, periodId) => {
    apiFetch("/lessons", { query: { materialId, schoolStageId: schoolStagesId, id: periodId } })
      .then((fetchLessons) => {
        this.setState({ getLessons: fetchLessons });
      })
      .catch((error) => console.error("Error:", error));
  };
  getPeriod(materialId) {
    apiFetch("/periods/by-material", { query: { materialId } })
      .then((grades) => {
        this.setState({ getPeriods: grades });
      })
      .catch((error) => console.error("Error:", error));
  }

  getMaterialByStage(stageId) {
    apiFetch("/lookup/materials/by-stage", { query: { stageId } })
      .then((grades) => {
        this.setState({ getMaterialsList: grades });
      })
      .catch((error) => console.error("Error:", error));
  }

  componentDidMount() {
    document.title = properties.site_name + " | دفع شهر جديد";

    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    // Get Grades From API
    apiFetch("/lookup/school-stages", { query: { branchId: this.state.branchID } })
      .then((grades) => {
        this.setState({ getSchoolStages: grades });
      })
      .catch((error) => console.error("Error:", error));
  }

  getStudentsByStageID = (e) => {
    this.setState({ selectedStage: e.value }, () => {
      // Get Students From API
      apiFetch("/students", {
        query: { branchId: this.state.branchID, schoolStagesId: this.state.selectedStage },
      })
        .then((fetchStudents) => {
          this.setState({ getStudentNames: fetchStudents });
        })
        .catch((error) => console.error("Error:", error));
    });
  };

  render() {
    let schoolStages = this.state.getSchoolStages.map((stage) => {
      return { label: stage.name, value: stage.id };
    });

    let studentNames = this.state.getStudentNames.map((student) => {
      return {
        label: `${student.firstName} ${student.fatherName} (${student.mobile})`,
        value: student.id,
      };
    });

    let periodList = this.state.getPeriodsList.map((period) => {
      return { label: period.name, value: period.id };
    });

    let materialList = this.state.getMaterialsList.map((period) => {
      return { label: period.name, value: period.id };
    });

    var addSubscription = (e) => {
      var _this = this;
      e.preventDefault();

      this.setState({
        buttonText: "جاري الإضافة ...",
        buttonColor: "loading",
      });

      // Send the data to the server
      apiFetch("/students/enrollments", {
        method: "POST",
        body: {
          studentProfileId: this.state.selectedStudent,
          periodId: this.state.selectedPeriod,
          materialId: this.state.selectedMaterial,
          value: this.state.cost,
        },
      })
        .then(() => {
          _this.setState({
            buttonText: "إضافة الإشتراك",
            buttonColor: "",
          });

          _this.toast.current.show({
            severity: "success",
            summary: "تم بنجاح",
            detail: "تم إضافة الإشتراك بنجاح",
          });
        })
        .catch((error) => {
          console.error("Error:", error);

          _this.setState({
            buttonText: "إضافة الإشتراك",
            buttonColor: "",
          });

          _this.toast.current.show({
            severity: "error",
            summary: "لم تتم العملية",
            detail: error.message || "خطأ في الإتصال",
          });
        });
    };
    let periods = this.state.getPeriods.map((period) => {
      return { label: period.name, value: period.id };
    });
    let gradesList = this.state.getSchoolStages.map((grade) => {
      return {
        label: grade.name,
        value: grade.id,
      };
    });
    return (
      <>
        <Header pageName="دفع فتره جديده" />

        <div className="content">
          <Toast ref={this.toast}></Toast>

          <div className="container">
            <Card title="دفع فتره جديده">
              <form onSubmit={addSubscription} action="#" method="POST">
                <div className="p-grid p-fluid">
                <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Dropdown
                          value={this.state.selectedStage}
                          options={gradesList}
                          placeholder="إختر المرحلة الدراسية"
                          onChange={(e) => {
                            this.setState(
                              { selectedStage: e.value },
                              this.getLessonsByStage(
                                e.value,
                                this.state.selectedMaterial,
                                this.state.selectedPeriod
                              ),
                              this.getMaterialByStage(e.value)
                            
                            );
                            this.getStudentsByStageID(e)
                          }}
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
                        <Dropdown
                          value={this.state.selectedMaterial}
                          options={materialList}
                          placeholder="إختر المادة"
                          onChange={(e) =>
                            this.setState({ selectedMaterial: e.value }
                              ,this.getPeriod(e.value), this.getLessonsByStage(
                                this.state.selectedStage,
                                e.value,
                                this.state.selectedPeriod
                              ),
                              )
                            
                           
                            
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
                        <Dropdown
                          value={this.state.selectedPeriod}
                          options={periods}
                          placeholder="  إختر الفترة "
                          onChange={(e) =>
                            this.setState({ selectedPeriod: e.value },
                              this.getLessonsByStage(
                                this.state.selectedStage,
                                this.state.selectedMaterial,
                                e.value
                              ),
                            //  this.state.selectedType==1? 
                            
                            //  :null
                              )
                              
                              

                             
                          }
                          optionLabel="label"
                          filter
                          showClear
                          filterBy="label"
                        />
                      </span>
                    </div>
                  </div>
                  {/* <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Dropdown
                          value={this.state.selectedStage}
                          options={schoolStages}
                          placeholder="إختر المرحلة الدراسية"
                          onChange={(e) => this.getStudentsByStageID(e)}
                          optionLabel="label"
                          filter
                          showClear
                          filterBy="label"
                        />
                      </span>
                    </div>
                  </div> */}
                  <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Dropdown
                          value={this.state.selectedStudent}
                          options={studentNames}
                          placeholder="إختر طالب"
                          onChange={(e) =>
                            this.setState({ selectedStudent: e.value })
                          }
                          optionLabel="label"
                          filter
                          showClear
                          filterBy="label"
                        />
                      </span>
                    </div>
                  </div>
                  {/* <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Dropdown
                          value={this.state.selectedPeriod}
                          options={periodList}
                          placeholder="إختر الفترة"
                          onChange={(e) =>
                            this.setState({ selectedPeriod: e.value })
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
                        <Dropdown
                          value={this.state.selectedMaterial}
                          options={materialList}
                          placeholder="إختر المادة"
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
                  </div> */}
                  <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <InputText
                          id="cost"
                          value={this.state.cost}
                          name="cost"
                          type="text"
                          onChange={(e) => {
                            this.setState({ cost: e.target.value });
                          }}
                        />
                        <label htmlFor="cost">السعر</label>
                      </span>
                    </div>
                  </div>
                  <div className="p-col-12 p-md-12 p-mt-2">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Button
                          className={`add-button ${this.state.buttonColor}`}
                          label={this.state.buttonText}
                        />
                      </span>
                    </div>
                  </div>
                </div>
              </form>
            </Card>
          </div>
        </div>
      </>
    );
  }
}

export default AddSubscriptions;
