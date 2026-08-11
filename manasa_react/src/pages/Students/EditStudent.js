import React, {Component, createRef} from 'react';

// CSS
import './Students.scss';
import 'primeflex/primeflex.css';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Dropdown } from 'primereact/dropdown';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';

// Properties
const properties = require('../../properties.json');

class EditStudent extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        // Submit Button
        buttonText: "تعديل الطالب",
        buttonColor: "",

        // Form Select Boxes
        city: null,
        state: null,
        grade: null,
        gender: null,

        // Cities & States & Grades
        cities: [],
        states: [],
        grades: [],

        studentID: (this.props.match.params.id) ? parseInt(this.props.match.params.id) : 0,

        // Message
        msg: {
            msgText: null,
            msgType: null,
            msgIcon: null
        },

        // Lesson Data
        formContent: {
            fullName: null,
            city: null,
            cityState: null,
            studentPhone: null,
            parentPhone: null,
            parentJob: null,
            grade: null,
            gender: null,
            email: null,
            username: null,
            password: null,
            macId: null,
            companyID: properties.company_id,
            branchID: properties.branch_id,
        }
    }

    changeVideoType = (e) => {
        this.setState({
            setVideoType: e.value,
            formContent: {
                ...this.state.formContent,
                youtubeLink: null,
                videoLink: null
            }
        })
    }

    getStates = (governorateId) => {
        apiFetch("/lookup/states", { query: { governorateId } })
        .then(states => {
            this.setState({states: states});
        })
        .catch(error => {
            console.error('Error:', error)
        })
    }

    componentDidMount() {
        document.title = properties.site_name + " | تعديل الطالب جديدة";

        // Get Cities From API
        apiFetch("/lookup/governorates")
        .then(cities => {
            this.setState({cities: cities});
        })
        .catch(error => console.error('Error:', error));

        // Get Grades From API
        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
        .then(grades => {
            this.setState({grades: grades});
        })
        .catch(error => console.error('Error:', error));

        // Get Student From API
        apiFetch(`/students/${this.state.studentID}`)
        .then(student => {
            this.setState({
                state: student.stateId,
                gender: student.gender,
                grade: student.schoolStagesId,

                formContent: {
                    fullName: student.fullName,
                    cityState: student.stateId,
                    studentPhone: student.mobile,
                    parentPhone: student.fatherMobile,
                    parentJob: student.fatherJob,
                    grade: student.schoolStagesId,
                    gender: student.gender,
                    companyID: properties.company_id,
                    branchID: properties.branch_id
                }
            });
        })
        .catch(error => console.error('Error:', error));
    }

    render() {
        let cirtiesList = this.state.cities.map(city => {
            return {
                label: city.name,
                value: city.id
            };
        });

        let statesList = this.state.states.map(cityState => {
            return {
                label: cityState.name,
                value: cityState.id
            };
        });

        let gradesList = this.state.grades.map(grade => {
            return {
                label: grade.name,
                value: grade.id
            };
        });

        let genderList = [
            {label: "ذكر", value: 1},
            {label: "أنثى", value: 2}
        ]

        var updateAndGetState = (e) => {
            this.setState({
                city: parseInt(e.target.value),
                formContent: {
                    ...this.state.formContent,
                    city: parseInt(e.target.value)
                }
            }, () => {
                this.getStates(this.state.city);
            });
        }
        
        var updateStudent = (e) => {
            var _this = this;
            e.preventDefault();

            this.setState({
                buttonText: "جاري التعديل ...",
                buttonColor: "loading"
            });

            var studentDataEdit = {
                branchId: this.state.formContent.branchID,
                fullName: this.state.formContent.fullName,
                mobile: this.state.formContent.studentPhone,
                fatherMobile: this.state.formContent.parentPhone,
                fatherJob: this.state.formContent.parentJob,
                gender: this.state.formContent.gender,
                stateId: this.state.formContent.cityState,
                schoolStagesId: this.state.formContent.grade
            };

            apiFetch(`/students/${this.state.studentID}`, { method: "PUT", body: studentDataEdit })
            .then(() => {
                _this.setState({
                    buttonText: "تعديل الطالب",
                    buttonColor: ""
                });
                _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تم حفظ التعديلات'});
            })
            .catch(error => {
                console.error('Error:', error)

                _this.setState({
                    buttonText: "تعديل الطالب",
                    buttonColor: "",
                });

                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        }

        return (
            <div className="add-lesson-page">
                <Header pageName="تعديل الطالب" />

                <div className="content">
                    <Toast ref={this.toast}></Toast>

                    <div className="container">
                        <Card title="تعديل الطالب">
                            <form onSubmit={updateStudent} action="#" method="POST">
                                <div className="p-grid p-fluid">
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputText id="name" value={this.state.formContent.fullName} name="name" type="text" onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    fullName: e.target.value
                                                }})}} />
                                                
                                                <label htmlFor="name">الإسم</label>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.city} options={cirtiesList} onChange={(e) => updateAndGetState(e)} name="type" placeholder="المحافظة"/>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.state} options={statesList} onChange={(e) => this.setState({state: e.value,
                                                formContent: {
                                                    ...this.state.formContent,
                                                    cityState: e.value
                                                }
                                            })} name="states" placeholder="المراكز"/>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.grade} options={gradesList} onChange={(e) => this.setState({grade: e.value,
                                                formContent: {
                                                    ...this.state.formContent,
                                                    grade: e.value
                                                }
                                            })} name="grade" placeholder="المرحلة الدراسية"/>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.gender} options={genderList} onChange={(e) => this.setState({gender: e.value,
                                                formContent: {
                                                    ...this.state.formContent,
                                                    gender: e.value
                                                }
                                            })} name="gender" placeholder="النوع"/>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputText id="phone" type="tel" value={this.state.formContent.studentPhone} name="phone" onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    studentPhone: e.target.value
                                                }})}} />
                                                
                                                <label htmlFor="phone">تليفون الطالب</label>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputText id="parentPhone" type="tel" value={this.state.formContent.parentPhone} name="parentPhone" onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    parentPhone: e.target.value
                                                }})}} />
                                                
                                                <label htmlFor="parentPhone">تليفون ولي الأمر</label>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputText id="parentJob" value={this.state.formContent.parentJob} name="parentJob" type="text" onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    parentJob: e.target.value
                                                }})}} />
                                                
                                                <label htmlFor="parentJob">وظيفة ولي الأمر</label>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-12">
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
            </div>
        );
    }
}

export default EditStudent;