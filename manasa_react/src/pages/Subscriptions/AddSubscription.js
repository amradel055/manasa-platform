import React, { Component, createRef } from "react";

// CSS
import "./Subscription.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";

// Components
import Header from "../../components/Header/Header";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import { Dropdown } from "primereact/dropdown";
import { getDefaultNormalizer } from "@testing-library/react";

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

  getData = async () => {
    var data = await JSON.parse(window.localStorage.getItem("currentTeacher"));
    this.setState({ teacherId: data["teacherId"] });
    console.log({ teacher: this.state.teacherId });
  };
  
  getLessonsByStage = (schoolStagesId,materialId , periodId) => {
    // Get Lessons From API
    var _data = JSON.parse(window.localStorage.getItem("currentTeacher"));
    fetch(properties.api_path + "/lesson/teacherLessonList", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        branchId: this.state.branchID,
        companyId: this.state.companyID,
        materialId: materialId,
        schoolStagesId: schoolStagesId,
        schoolId: schoolStagesId,
        periodId: periodId,
        teacherId: _data["teacherId"],
      }),
    })
      .then((res) => {
        return res.json();
      })
      .then((fetchLessons) => {
        this.setState({ getLessons: fetchLessons });
      });
  };
  getPeriod(materialId) {
    fetch(properties.api_path + "/periods/findPeriod", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
      
        materialId: materialId
      }),
    })
      .then((res) => {
        return res.json();
      })
      .then((grades) => {
        console.log(grades);
        this.setState({ getPeriods: grades });
      });
  }

  // getPeriods() {
  //   var data = JSON.parse(window.localStorage.getItem("currentTeacher"));
  //   fetch(properties.api_path + "/periods/allPeriods", {
  //     method: "POST",
  //     headers: {
  //       Accept: "application/json",
  //       "Content-Type": "application/json",
  //     },
  //     body: JSON.stringify({
  //       branchId: this.state.branchID,
  //     }),
  //   })
  //     .then((res) => {
  //       return res.json();
  //     })
  //     .then((grades) => {
  //       console.log(grades);
  //       this.setState({ getPeriods: grades });
  //     });
  // }
  getMaterialByStage(stageId){
    var data =JSON.parse(window.localStorage.getItem('currentTeacher'));
    fetch(properties.api_path + "/lockup/materialListbystage", {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: stageId
        })
    })
    .then(res => {return res.json()})
    .then(grades => {
        console.log(grades);
        this.setState({getMaterialsList : grades});
    })
 }

  // getPeriods = () => {
  //   // Get Periods From API
  //   fetch(properties.api_path + "/periods/allPeriods", {
  //     method: "POST",
  //     headers: {
  //       Accept: "application/json",
  //       "Content-Type": "application/json",
  //     },
  //     body: JSON.stringify({
  //       branchId: this.state.branchID,
  //     }),
  //   })
  //     .then((res) => {
  //       return res.json();
  //     })
  //     .then((fetchStudents) => {
  //       this.setState({
  //         getPeriodsList: fetchStudents,
  //       });
  //     });
  // };

  // //getMaterialList
  // getMaterial = () => {
  //   // Get Periods From API
  //   var data = JSON.parse(window.localStorage.getItem("currentTeacher"));
  //   fetch(properties.api_path + "/lockup/materialList", {
  //     method: "POST",
  //     headers: {
  //       Accept: "application/json",
  //       "Content-Type": "application/json",
  //     },
  //     body: JSON.stringify({
  //       teacherId: data["teacherId"],
  //       branchId: this.state.branchID,
  //     }),
  //   })
  //     .then((res) => {
  //       return res.json();
  //     })
  //     .then((fetchMaterials) => {
  //       this.setState({
  //         getMaterialsList: fetchMaterials,
  //       });
  //     });
  // };

  componentDidMount() {
    this.getData();
    document.title = properties.site_name + " | دفع شهر جديد";
    // this.getPeriods();
    // this.getMaterial();
    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    // Get Grades From API
    fetch(properties.api_path + "/lockup/levelList", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        branchId: this.state.branchID,
      }),
    })
      .then((res) => {
        return res.json();
      })
      .then((grades) => {
        this.setState({ getSchoolStages: grades });
      });
  }

  getStudentsByStageID = (e) => {
    this.setState({ selectedStage: e.value }, () => {
      // Get Students From API
      fetch(properties.api_path + "/student/studentList", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          branchId: this.state.branchID,
          schoolStagesId: this.state.selectedStage,
        }),
      })
        .then((res) => {
          return res.json();
        })
        .then((fetchStudents) => {
          this.setState({
            getStudentNames: fetchStudents,
          });
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
      fetch(properties.api_path + "/studentPeriods/saveStudentPeriods", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json; charset=utf-8",
        },
        body: JSON.stringify({
          companyId: this.state.companyID,
          branchId: this.state.branchID,
          periodId: this.state.selectedPeriod,
          value: this.state.cost,
          studentId: this.state.selectedStudent,
          materialId: this.state.selectedMaterial,
          teacherId: this.state.teacherId,
        }),
      })
        .then((res) => {
          return res.json();
        })
        .then((done) => {
          _this.setState({
            buttonText: "إضافة الإشتراك",
            buttonColor: "",
          });

          if (done.msgType === "success") {
            _this.toast.current.show({
              severity: "success",
              summary: "تم بنجاح",
              detail: done.msg,
            });
          } else {
            _this.toast.current.show({
              severity: "error",
              summary: "لم تتم العملية",
              detail: done.msg,
            });
          }
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
            detail: "خطأ في الإتصال",
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
