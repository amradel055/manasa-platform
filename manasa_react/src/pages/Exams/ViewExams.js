import React, {Component, createRef} from 'react';
import { Toast } from 'primereact/toast';
// Components
import Header from '../../components/Header/Header';
//import PageHeader from '../../components/Page-Header/PageHeader';
import Footer from '../../components/Footer/Footer';
import './view.scss';
import { Card } from "primereact/card";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';

// Properties
var properties = require('../../properties.json');

class ViewExams extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        studentID:  (this.props.match.params.studentId) ? parseInt(this.props.match.params.studentId) : 0,

        // Input Values
        username: null,
        password: null,
        companyID: properties.company_id,
        branchID: properties.branch_id,

        // Sign Up Process Indicator
        buttonText: "تسجيل الدخول",
        buttonColor: "",
        resend: false,
        fullName:null,
        mobile:null,
        email:null,
        image:null,schoolStagesName:null,stateName:null,
        governorateName:null,

        // Get Data
        data: [],
        dataActive:[],
        dataActive2:[],


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
            "ديسمبر"
        ],

        getLessons: [],

        // Current User
        currentUser: localStorage.getItem('currentUser') ? JSON.parse(localStorage.getItem('currentUser')).studentData : {},

        // Message
        msg: {
            msgText: null,
            msgType: null,
            msgIcon: null
        },
        selectedMonth: null,
        selectedLesson: null

    }

    getExams = (formData) => {

        // Get Exams From API
        fetch(properties.api_path + "/exams/findSolvedExamsForStudent", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({formData,  studentId: this.state.studentID})
        })
        .then(res => {return res.json()})
        .then(fetchExams => {
            this.setState({
                data: fetchExams
            });
        })
        .catch(error => {
            console.error('Error:', error)
        });
    }
    getSubscriptions = async () => {
        // Get Subscriptions From API
        var data = await JSON.parse(window.localStorage.getItem("currentTeacher"));
        fetch(properties.api_path + "/studentPeriodsOrders/studentActivationOrder", {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            id: this.state.studentID,
            // branchId: this.state.branchID,
            // teacherId: data["teacherId"],
          }),
        })
          .then((res) => {
            return res.json();
          })
          .then((fetchStudents) => {
            this.setState({
                dataActive: fetchStudents,
            });
          });
      };

      getSubscriptionsTwo = async () => {
        // Get Subscriptions From API
        var data = await JSON.parse(window.localStorage.getItem("currentTeacher"));
        fetch(properties.api_path + "/studentPeriods/studentActivation", {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            id: this.state.studentID,
            // branchId: this.state.branchID,
            // teacherId: data["teacherId"],
          }),
        })
          .then((res) => {
            return res.json();
          })
          .then((fetchStudents) => {
            this.setState({
                dataActive2: fetchStudents,
            });
          });
      };

      getStudentData = async () => {
        // Get Subscriptions From API
        var data = await JSON.parse(window.localStorage.getItem("currentTeacher"));
        fetch(properties.api_path + "/student/findstudentdata", {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            id: this.state.studentID,
            // branchId: this.state.branchID,
            // teacherId: data["teacherId"],
          }),
        })
          .then((res) => {
            return res.json();
          })
          .then((fetchStudents) => {
            this.setState({
                fullName:fetchStudents.fullName,
                mobile:fetchStudents.mobile,
                email:fetchStudents.email,
                image:fetchStudents.image,schoolStagesName:fetchStudents.schoolStagesName
                ,stateName:fetchStudents.stateName,
                governorateName:fetchStudents.governorateName,
            });
          });
      };
      deleteSubscription = (id) => {
        var _this = this;
    
        // Confirm Deletion
        var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");
    
        if (con) {
          // Delete Subscription By ID
          fetch(properties.api_path + "/studentPeriodsOrders/deletestudentactivation", {
            method: "POST",
            headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
                periodOrderId55: id,
            }),
          })
            .then((res) => {
              return res.json();
            })
            .then((deleteMediaByID) => {
                if (deleteMediaByID.msgType === "success") {
                    this.setState({ resend: true, dataActive: deleteMediaByID });

                //   _this.toast.current.show({
                //     severity: "success",
                //     summary: "تمت العملية",
                //     detail: "تم الحذف بنجاح",
                //   });
                  this.getSubscriptions();
                } else {

                //   _this.toast.current.show({
                //     severity: "error",
                //     summary: "لم تتم العملية",
                //     detail: "لم يتم الحذف",
                //   });
                }
              })
      
              .catch((error) => {
                console.error("Error:", error);
      
                // _this.toast.current.show({
                //   severity: "error",
                //   summary: "لم تتم العملية",
                //   detail: "خطأ في الإتصال",
                // });
              });
        }
      };
      deleteSubscription2 = (id) => {
        var _this = this;
    
        // Confirm Deletion
        var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");
    
        if (con) {
          // Delete Subscription By ID
          fetch(properties.api_path + "/studentPeriods/deleteStudentPeriods", {
            method: "POST",
            headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
                id: id,
            }),
          })
            .then((res) => {
              return res.json();
            })
            .then((deleteMediaByID) => {
                if (deleteMediaByID.msgType === "success") {
                    this.setState({ resend: true, dataActive: deleteMediaByID });

                //   _this.toast.current.show({
                //     severity: "success",
                //     summary: "تمت العملية",
                //     detail: "تم الحذف بنجاح",
                //   });
                  this.getSubscriptions();
                } else {

                //   _this.toast.current.show({
                //     severity: "error",
                //     summary: "لم تتم العملية",
                //     detail: "لم يتم الحذف",
                //   });
                }
              })
      
              .catch((error) => {
                console.error("Error:", error);
      
                // _this.toast.current.show({
                //   severity: "error",
                //   summary: "لم تتم العملية",
                //   detail: "خطأ في الإتصال",
                // });
              });
        }
      };
      editStudent = (periodOrderId) => {
        var _this = this;

        // Confirm Deletion
        // var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

        // if (con) {
            // Delete Subscription By ID
            fetch(properties.api_path + "/studentPeriodsOrders/studenttransaction", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    periodOrderId55: periodOrderId,
                })
            })
            .then(res => {return res.json()})
            .then((deleteMediaByID) => {
                this.setState({ resend: true, dataActive: deleteMediaByID });

                if (deleteMediaByID.msgType === "success") {
                //   _this.toast.current.show({
                //     severity: "success",
                //     summary: "تمت العملية",
                //     detail: "تم الحذف بنجاح",
                //   });
                  this.getSubscriptions();
                                } else {

                //   _this.toast.current.show({
                //     severity: "error",
                //     summary: "لم تتم العملية",
                //     detail: "لم يتم الحذف",
                //   });
                }
              })
      
              .catch((error) => {
                console.error("Error:", error);
      
                // _this.toast.current.show({
                //   severity: "error",
                //   summary: "لم تتم العملية",
                //   detail: "خطأ في الإتصال",
                // });
              });
    }
        componentDidMount() {
        document.title = properties.site_name + " - الإمتحانات";

        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        this.getSubscriptions();
        this.getSubscriptionsTwo();
        this.getStudentData();
        this.getExams({
            branchId: this.state.branchID,
            companyId: this.state.companyID,
            studentId: this.state.currentUser.id,
            schoolStagesId: this.state.currentUser.schoolStagesId
        });

        // Get Lessons From API
        fetch(properties.api_path + "/lesson/lessonList", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                branchId: this.state.branchID,
                companyId: this.state.companyID,
                schoolId: this.state.currentUser.schoolStagesId
            })
        })
        .then(res => {return res.json()})
        .then(fetchLessons => {
            this.setState({getLessons: fetchLessons});
        })
    }

    render() {

        // Import CSS
        const header = (
            <div className="table-header">
              عدد الإشتراكات
              <span>{this.state.dataActive2.length}</span>
              <span className="p-input-icon-left">
                <i className="pi pi-search" />
                <InputText
                  type="search"
                  onInput={(e) => this.setState({ globalFilter: e.target.value })}
                  placeholder="بحث في الإشتراكات"
                />
              </span>
            </div>
          );
          const header2 = (
            <div className="table-header">
              عدد الطلبات
              <span>{this.state.dataActive.length}</span>
              <span className="p-input-icon-left">
                <i className="pi pi-search" />
                <InputText
                  type="search"
                  onInput={(e) => this.setState({ globalFilter: e.target.value })}
                  placeholder="بحث في الطلبات"
                />
              </span>
            </div>
          );
        let allExams = this.state.data.map((exam, i) => 
            <tr>
                <th scope="row">{i+1}</th>
                <td>{exam.examName}</td>
                <td>{(exam.monthId) ? this.state.namedMonths[exam.monthId - 1] : "------"}</td>
                <td>{(exam.lessonName) ? exam.lessonName : "------"}</td>
                <td>{(exam.score !== undefined) ? `${exam.score}/${exam.degree}` : "------"}</td>
            </tr>
        );

        let allLessons = this.state.getLessons.map(lesson => 
            <option value={lesson.id}>{lesson.lessonName}</option>    
        );
        // const actionBodyTemplate = (rowData) => {
        //     return (
        //       <React.Fragment>
        //         {/* <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => window.location.href = `subscriptions/edit/${rowData.id}`} /> */}
        //         <Button
        //           icon="pi pi-trash"
        //           className="p-button-rounded p-button-danger"
        //           onClick={() => this.deleteSubscription(rowData.id)}
        //         />
        //       </React.Fragment>
        //     );
        //   };
        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    {/* <Button icon="pi pi-eye" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => window.location.href = `exams/view/${rowData.id}`} /> */}

                    <Button
            icon="pi pi-plus"
            className="p-button-rounded p-button-success p-ml-2"
            onClick={() =>
              this.editStudent(rowData.periodOrderId55)
            }
          />  
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() =>
              this.deleteSubscription(rowData.periodOrderId55)
            }
          />                </React.Fragment>
            );
        }

        const actionBodyTemplateSubscription = (rowData) => {
            return (
                <React.Fragment>
                    {/* <Button icon="pi pi-eye" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => window.location.href = `exams/view/${rowData.id}`} /> */}

         
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() =>
              this.deleteSubscription2(rowData.periodOrderId55)
            }
          />                </React.Fragment>
            );
        }
        return (
            <>
            
                <Header className="head" PageName="aa" />
                {/* <PageHeader pageName="الإمتحانات" /> */}
                <div className="content">
          <div className="container">
            <div className="card">
              <Card title="بيانات الطالب ">
               <div className="container my-5">
                    
                        <div className="row">

                        <div className="col-md-4">
               <label>اسم الطالب:</label>
                <label>{this.state.fullName}</label>
            
              
               </div>
               <div className="col-md-4">
               <label>بريد الطالب:</label>
                <label>{this.state.email}</label>
              
               </div>
               <div className="col-md-4">
               <label> الفرقه:</label>
            <label>{this.state.schoolStagesName}</label>
              
               </div>
               </div>
               </div>
               <div className="container my-5">
                    
                    <div className="row">

                    <div className="col-md-4">
               <label> الهاتف:</label>
            <label>{this.state.mobile}</label>
              
               </div>   <div className="col-md-4">
               <label> المحافظه:</label>
            <label>{this.state.governorateName}</label>
              
               </div>
               <div className="col-md-4">
               <label> البلد:</label>
            <label>{this.state.stateName}</label>
              
               </div>
               </div>
               </div>
              </Card>
            </div>
          </div>
        </div>
                <div className="container my-5">
                    <div className="filter">
                        <div className="row">
                            <div className="col-md-3">
                                <select className="months" onChange={(e) => {
                                    this.setState({
                                        selectedMonth: e.target.value
                                    }, () => {
                                        this.getExams({
                                            branchId: this.state.branchID,
                                            companyId: this.state.companyID,
                                            studentId: this.state.currentUser.id,
                                            schoolStagesId: this.state.currentUser.schoolStagesId,
                                            monthId: this.state.selectedMonth,
                                            lessonId: this.state.selectedLesson
                                        });
                                    });
                                }}>
                                    <option value={null}>إختر الشهر</option>
                                    <option value={1}>يناير</option>
                                    <option value={2}>فبراير</option>
                                    <option value={3}>مارس</option>
                                    <option value={4}>إبريل</option>
                                    <option value={5}>مايو</option>
                                    <option value={6}>يونيو</option>
                                    <option value={7}>يوليو</option>
                                    <option value={8}>أغسطس</option>
                                    <option value={9}>سبتمبر</option>
                                    <option value={10}>أكتوبر</option>
                                    <option value={11}>نوفمبر</option>
                                    <option value={12}>ديسمبر</option>
                                </select>
                            </div>
                            <div className="col-md-3">
                                <select className="months" onChange={(e) => {
                                    this.setState({
                                        selectedLesson: e.target.value
                                    }, () => {
                                        this.getExams({
                                            branchId: this.state.branchID,
                                            companyId: this.state.companyID,
                                            studentId: this.state.currentUser.id,
                                            schoolStagesId: this.state.currentUser.schoolStagesId,
                                            monthId: this.state.selectedMonth,
                                            lessonId: this.state.selectedLesson
                                        });
                                    });
                                }}>
                                    <option value={null}>إختر الدرس</option>
                                    {allLessons}
                                </select>
                            </div>
                        </div>
                    </div>
                    <div className="table-responsive">
                        <table className="table">
                            <thead className="thead-dark">
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">إسم الإمتحان</th>
                                    <th scope="col">الشهر</th>
                                    <th scope="col">الدرس</th>
                              
                                    <th scope="col">النتيجة</th>
                                </tr>
                            </thead>
                            <tbody>
                               {allExams}
                            </tbody>
                        </table>
                    </div>
                </div>

                <Footer />
                {/* <Header pageName="كل إشتراكات الشهور" /> */}

        {/* <Toast ref={this.toast}></Toast> */}
        <div className="content">
          <div className="container">
            <div className="card">
              <Card title="طلبات التفعيل ">
                <DataTable
                  header={header2}
                  sortable
                  globalFilter={this.state.globalFilter}
                  className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                  value={this.state.dataActive}
                  paginator
                  rows={10}
                  rowsPerPageOptions={[10, 20, 50]}
                  currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
                  emptyMessage="لا يوجد مدفوعات"
                >
              
                  <Column
                    filter
                    sortable
                    field="schoolStagesName"
                    header="المرحلة الدراسية"
                  ></Column>
                  <Column
                    filter
                    sortable
                    field="periodName"
                    header="الفتره"
                  ></Column>
                  {/* <Column
                    filter
                    sortable
                    field="monthId"
                    body={(row) => this.state.namedMonths[row.monthId - 1]}
                    header="الشهر"
                  ></Column> */}
               
               <Column header=" رؤيه / اضافه / حذف " body={actionBodyTemplate}></Column>
                </DataTable>
              </Card>
            </div>
          </div>
        </div>
        <div className="content">
          <div className="container">
            <div className="card">
              <Card title="كل إشتراكات ">
                <DataTable
                  header={header}
                  sortable
                  globalFilter={this.state.globalFilter}
                  className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                  value={this.state.dataActive2}
                  paginator
                  rows={10}
                  rowsPerPageOptions={[10, 20, 50]}
                  currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
                  emptyMessage="لا يوجد مدفوعات"
                >
              
                  <Column
                    filter
                    sortable
                    field="schoolStagesName"
                    header="المرحلة الدراسية"
                  ></Column>
                  <Column
                    filter
                    sortable
                    field="periodName"
                    header="الفتره"
                  ></Column>
                  {/* <Column
                    filter
                    sortable
                    field="monthId"
                    body={(row) => this.state.namedMonths[row.monthId - 1]}
                    header="الشهر"
                  ></Column> */}
               
               <Column header="   حذف " body={actionBodyTemplateSubscription}></Column>
                </DataTable>
              </Card>
            </div>
          </div>
        </div>
            </>
        );
    }
} 

export default ViewExams;