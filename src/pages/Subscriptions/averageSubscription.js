import React, {Component, createRef} from 'react';

// CSS
import './Subscription.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';

// Components
import Header from '../../components/Header/Header';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';

// Properties
const properties = require('../../properties.json');

class AverageSubscription extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        // Submit Button
        buttonText: " بحث",
        buttonColor: "",

        // Teacher Identifier
        branchID: properties.branch_id,
        companyID: properties.company_id,

        // Get Data
        getSchoolStages: [],
        getStudentNames: [],
        getMonthsList: [],

        // Inputs Data
        selectedStudent: null,
        selectedStage: null,
        selectedMonth: null,
        cost: null,
        globalFilter: null,

       

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

        // Data
        data: [],

        // Update Data
        resend: false
    }
    getSubscriptions = () => {
        // Get Subscriptions From API
        fetch(properties.api_path + "/studentMonths/allStudentMonths", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                branchId: this.state.branchID
            })
        })
        .then(res => {return res.json()})
        .then(fetchStudents => {
            this.setState({
                data: fetchStudents
            });
        });
    }

    
    componentDidMount () {
        document.title = properties.site_name + " | عدد الطلاب المشتركين";

        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }
        this.getSubscriptions();
        // Get Grades From API
        fetch(properties.api_path + "/lockup/levelList", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                branchId: this.state.branchID
            })
        })
        .then(res => {return res.json()})
        .then(grades => {
            this.setState({getSchoolStages: grades});
        })
    }

    getStudentsByStageID = (e) => {
        this.setState({selectedStage: e.value}, () => {
            // Get Students From API
            fetch(properties.api_path + "/student/studentList", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    branchId: this.state.branchID,
                    schoolStagesId: this.state.selectedStage
                })
            })
            .then(res => {return res.json()})
            .then(fetchStudents => {
                this.setState({
                    getStudentNames: fetchStudents
                });
            });
        });
    }
    
    
    render() {
        let schoolStages = this.state.getSchoolStages.map(stage => {
            return {label: stage.name, value: stage.id};
        });
        
        let studentNames = this.state.getStudentNames.map(student => {
            return {label: `${student.fullName} (${student.mobile})`, value: student.id};
        });
        
        let monthsList = [
            {label: 'يناير', value: 1},
            {label: 'فبراير', value: 2},
            {label: 'مارس', value: 3},
            {label: 'إبريل', value: 4},
            {label: 'مايو', value: 5},
            {label: 'يونيو', value: 6},
            {label: 'يوليو', value: 7},
            {label: 'أغسطس', value: 8},
            {label: 'سبتمبر', value: 9},
            {label: 'أكتوبر', value: 10},
            {label: 'نوفمبر', value: 11},
            {label: 'ديسمبر', value: 12},
        ];

        var addSubscription = (e) => {
            var _this = this;
            e.preventDefault();

            this.setState({
                buttonText: "جاري البحث ...",
                buttonColor: "loading",
            });

            // Send the data to the server
            fetch(properties.api_path + "/studentMonths/saveStudentMonths", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json; charset=utf-8',
                },
                body: JSON.stringify({
                    companyId: this.state.companyID,
                    branchId: this.state.branchID,
                    monthId: this.state.selectedMonth,
                    value: this.state.cost,
                    studentId: this.state.selectedStudent
                })
            })
            .then(res => {return res.json()})
            .then(done => {
               _this.setState({
                   buttonText: "إضافة الإشتراك",
                   buttonColor: "",
               });

               if (done.msgType === "success") {
                   _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
                } else {
                   _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
               }
            })
            .catch(error => {
                console.error('Error:', error)

                _this.setState({
                   buttonText: "إضافة الإشتراك",
                   buttonColor: ""
               });

               _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
        }

        return (
            <>
                <Header pageName="عدد الطلاب المشتركين" />
                <div className="content">
                    <Toast ref={this.toast}></Toast>

                    <div className="container">
                        <Card title="عدد الطلاب المشتركين  ">
                        <h2>{this.state.getStudentNames.length}</h2>
                        <span>{this.state.data.length}</span>
                            <form onSubmit={addSubscription} action="#" method="POST">
                                <div className="p-grid p-fluid">
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.selectedStage} options={schoolStages} placeholder="إختر المرحلة الدراسية" onChange={(e) => this.getStudentsByStageID(e)} optionLabel="label" filter showClear filterBy="label" />   
                                         
                                            </span>
                                        </div>
                                    </div>
                                  
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.selectedMonth} options={monthsList} placeholder="إختر الشهر" onChange={(e) => this.setState({selectedMonth: e.value})} optionLabel="label" filter showClear filterBy="label" />                                            
                                            </span>
                                        </div>
                                    </div>
                                   
                                    <div className="p-col-12 p-md-12 p-mt-2">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Button className={`add-button ${this.state.buttonColor}`} label={this.state.buttonText}/>
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

export default AverageSubscription;