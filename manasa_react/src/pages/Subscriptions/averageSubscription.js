import React, {Component, createRef} from 'react';

// CSS
import './Subscription.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

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
        apiFetch("/students/enrollments")
        .then(fetchStudents => {
            this.setState({
                data: fetchStudents
            });
        })
        .catch(error => console.error('Error:', error));
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
        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
        .then(grades => {
            this.setState({getSchoolStages: grades});
        })
        .catch(error => console.error('Error:', error));
    }

    getStudentsByStageID = (e) => {
        this.setState({selectedStage: e.value}, () => {
            // Get Students From API
            apiFetch("/students", { query: { branchId: this.state.branchID, schoolStagesId: this.state.selectedStage } })
            .then(fetchStudents => {
                this.setState({
                    getStudentNames: fetchStudents
                });
            })
            .catch(error => console.error('Error:', error));
        });
    }
    
    
    render() {
        let schoolStages = this.state.getSchoolStages.map(stage => {
            return {label: stage.name, value: stage.id};
        });
        
        let studentNames = this.state.getStudentNames.map(student => {
            return {label: `${student.fullName} (${student.mobile})`, value: student.id};
        });
        
        var addSubscription = (e) => {
            e.preventDefault();
            // The stage dropdown's onChange already fetches the matching students;
            // this just re-runs that search explicitly for the current selection.
            if (this.state.selectedStage) {
                this.getStudentsByStageID({ value: this.state.selectedStage });
            }
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