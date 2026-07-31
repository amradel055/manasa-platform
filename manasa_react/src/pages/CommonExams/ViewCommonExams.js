import React, {Component, createRef} from 'react';
import { Toast } from 'primereact/toast';
// Components
import Header from '../../components/Header/Header';
//import PageHeader from '../../components/Page-Header/PageHeader';
import Footer from '../../components/Footer/Footer';
import './view.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';

// Properties
var properties = require('../../properties.json');

class ViewCommonExams extends Component {
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

        // Get Data
        data: [],

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
        fetch(properties.api_path + "/exams/findExamsForStudent", {
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

    componentDidMount() {
        document.title = properties.site_name + " - الاسئله";

        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
     

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

        return (
            <>
                <Header className="head" PageName="aa" />
                {/* <PageHeader pageName="الإمتحانات" /> */}

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
                                    <th scope="col">إسم السؤال</th>
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
            </>
        );
    }
} 

export default ViewCommonExams;