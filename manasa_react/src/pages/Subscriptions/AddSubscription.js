import React, { Component, createRef } from "react";
import ReactDOM from "react-dom";

// CSS
import "./Subscription.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch, getCurrentUser } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
// Our dropdown: a wrapper that passes every prop through to PrimeReact's,
// so the values, the options and the handlers below are unchanged.
import Select from "../../components/Field/Select";
import "../../components/Field/Form.scss";

// Properties
const properties = require("../../properties.json");

class AddSubscriptions extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    // The modal footer's slot, when this form is open inside one. Null on the
    // standalone route, where the button keeps its own bar.
    footerSlot: null,

    // One flag per list. Each is raised inside its own fetch, so opening a
    // list reports the wait as well as the first load does.
    stagesLoading: false,
    materialsLoading: false,
    periodsLoading: false,
    studentsLoading: false,

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
    this.setState({ periodsLoading: true });

    apiFetch("/periods/by-material", { query: { materialId } })
      .then((grades) => {
        this.setState({ getPeriods: grades, periodsLoading: false });
      })
      .catch((error) => {
        this.setState({ periodsLoading: false });
        console.error("Error:", error);
      });
  }

  getMaterialByStage(stageId) {
    this.setState({ materialsLoading: true });

    apiFetch("/lookup/materials/by-stage", { query: { stageId } })
      .then((grades) => {
        this.setState({ getMaterialsList: grades, materialsLoading: false });
      })
      .catch((error) => {
        this.setState({ materialsLoading: false });
        console.error("Error:", error);
      });
  }

  componentDidMount() {
    // Opened inside a modal? Then the save button belongs in its footer, not
    // in a bar of its own above the fields. It is moved there with a portal,
    // so it stays in THIS component's tree and its label and colour are still
    // driven from this state. The whole tree is in the DOM before any
    // componentDidMount runs, so the slot is already there if there is one.
    const slot = document.querySelector(".m-modal-slot");
    if (slot) this.setState({ footerSlot: slot });

    document.title = properties.site_name + " | دفع شهر جديد";

    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    // Not fetched here any more: the stage list fetches when it is opened
    // (see the Select's onOpen), and doing both asked for it twice.
  }

  // Lifted out of componentDidMount, where it was written inline and so could
  // only ever run once. Same request, same state write.
  getSchoolStages = () => {
    this.setState({ stagesLoading: true });

    apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
      .then((grades) => {
        this.setState({ getSchoolStages: grades, stagesLoading: false });
      })
      .catch((error) => {
        this.setState({ stagesLoading: false });
        console.error("Error:", error);
      });
  };

  getStudentsByStageID = (e) => {
    this.setState({ selectedStage: e.value, studentsLoading: true }, () => {
      // Get Students From API
      apiFetch("/students", {
        query: { branchId: this.state.branchID, schoolStagesId: this.state.selectedStage },
      })
        .then((fetchStudents) => {
          this.setState({ getStudentNames: fetchStudents, studentsLoading: false });
        })
        .catch((error) => {
          this.setState({ studentsLoading: false });
          console.error("Error:", error);
        });
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
    // One element, rendered in one of two places. `form` is what lets it
    // submit #add-subscription-form from wherever it ends up; PrimeReact
    // passes any prop it does not recognise straight on to the <button>.
    const saveButton = (
      <Button
        type="submit"
        form="add-subscription-form"
        className={`add-button ${this.state.buttonColor}`}
        label={this.state.buttonText}
      />
    );

    return (
      <div className="add-subscription-page m-form">
        <Header pageName="دفع فتره جديده" />

        <div className="content">
          <Toast ref={this.toast}></Toast>

          <div className="container">
            <div className="card">
              <form id="add-subscription-form" onSubmit={addSubscription} action="#" method="POST">

                {/* In a modal the button goes down into the footer; on the
                    standalone page it keeps its own bar. Same element either
                    way, with the same state behind it. */}
                {this.state.footerSlot ? (
                  ReactDOM.createPortal(saveButton, this.state.footerSlot)
                ) : (
                  <div className="m-actions">
                    <span className="m-actions-hint">لن يُسجَّل الإشتراك حتى تضغط الزر</span>
                    {saveButton}
                  </div>
                )}

                <div className="m-grid">
                  <div className="m-fld">
                    <label htmlFor="sub-stage">المرحلة الدراسية</label>
                    <Select
                      inputId="sub-stage"
                      value={this.state.selectedStage}
                      options={gradesList}
                      placeholder="إختر المرحلة الدراسية"
                      // The materials and the students used to be fetched
                      // from here as well. Both lists now fetch when they are
                      // opened, so doing it here too asked the server for the
                      // same thing twice -- once on the change and once on the
                      // open. getLessonsByStage stays: nothing opens a lesson
                      // list, so this is its only trigger.
                      onChange={(e) => {
                        this.setState(
                          { selectedStage: e.value },
                          this.getLessonsByStage(
                            e.value,
                            this.state.selectedMaterial,
                            this.state.selectedPeriod
                          )
                        );
                      }}
                      optionLabel="label"
                      showClear
                      loading={this.state.stagesLoading}
                      onOpen={this.getSchoolStages}
                    />
                  </div>

                  <div className="m-fld">
                    <label htmlFor="sub-material">المادة</label>
                    <Select
                      inputId="sub-material"
                      value={this.state.selectedMaterial}
                      options={materialList}
                      placeholder="إختر المادة"
                      // getPeriod used to run from here too; the period
                      // list fetches itself when opened.
                      onChange={(e) =>
                        this.setState({ selectedMaterial: e.value }
                          ,this.getLessonsByStage(
                            this.state.selectedStage,
                            e.value,
                            this.state.selectedPeriod
                          ),
                          )
                      }
                      optionLabel="label"
                      showClear
                      loading={this.state.materialsLoading}
                      needs={this.state.selectedStage}
                      needsLabel="اختر المرحلة الدراسية أولاً"
                      onOpen={() => this.getMaterialByStage(this.state.selectedStage)}
                    />
                  </div>

                  <div className="m-fld">
                    <label htmlFor="sub-period">الفترة</label>
                    <Select
                      inputId="sub-period"
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
                          )
                      }
                      optionLabel="label"
                      showClear
                      loading={this.state.periodsLoading}
                      needs={this.state.selectedMaterial}
                      needsLabel="اختر المادة أولاً"
                      onOpen={() => this.getPeriod(this.state.selectedMaterial)}
                    />
                  </div>

                  <div className="m-fld">
                    <label htmlFor="sub-student">الطالب</label>
                    <Select
                      inputId="sub-student"
                      value={this.state.selectedStudent}
                      options={studentNames}
                      placeholder="إختر طالب"
                      onChange={(e) =>
                        this.setState({ selectedStudent: e.value })
                      }
                      optionLabel="label"
                      showClear
                      loading={this.state.studentsLoading}
                      needs={this.state.selectedStage}
                      needsLabel="اختر المرحلة الدراسية أولاً"
                      // the same call the stage picker makes: it re-sets the
                      // stage to what it already is and refetches its students
                      onOpen={() => this.getStudentsByStageID({ value: this.state.selectedStage })}
                    />
                  </div>

                  <div className="m-fld">
                    <label htmlFor="cost">السعر</label>
                    <InputText
                      id="cost"
                      value={this.state.cost}
                      name="cost"
                      type="text"
                      placeholder="مثال: 150"
                      onChange={(e) => {
                        this.setState({ cost: e.target.value });
                      }}
                    />
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default AddSubscriptions;
