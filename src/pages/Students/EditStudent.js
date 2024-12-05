import React, {Component, createRef} from 'react';

// CSS
import './Students.scss';
import 'primeflex/primeflex.css';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';

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

    getStates = (cityID) => {
        fetch(properties.api_path + "/lockup/statesList", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({id: cityID})
        })
        .then(res => {return res.json()})
        .then(states => {
            this.setState({states: states});
        })
        .catch(error => {
            console.error('Error:', error)
        })
    }

    componentDidMount() {
        document.title = properties.site_name + " | تعديل الطالب جديدة";

        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        // Get Cities From API
        fetch(properties.api_path + "/lockup/governorateList", {
            method: "POST"
        })
        .then(res => {return res.json()})
        .then(cities => {
            this.setState({cities: cities});
        })
        
        // Get Grades From API
        fetch(properties.api_path + "/lockup/levelList", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({branchId: this.state.formContent.branchID})
        })
        .then(res => {return res.json()})
        .then(grades => {
            this.setState({grades: grades});
        })

        // Get Students From API
        fetch(properties.api_path + "/student/studentList", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                branchId: this.state.formContent.branchID,
                companyId: this.state.formContent.branchID,
                id: this.state.studentID
            })
        })
        .then(res => {return res.json()})
        .then(fetchStudent => {
            this.setState({
                state: fetchStudent[0].stateId,
                gender: fetchStudent[0].gender,
                grade: fetchStudent[0].schoolStagesId,
                city: fetchStudent[0].governorateId,
                
                formContent: {
                    fullName: fetchStudent[0].fullName,
                    city: fetchStudent[0].governorateId,
                    cityState: fetchStudent[0].stateId,
                    studentPhone: fetchStudent[0].mobile,
                    parentPhone: fetchStudent[0].fatherMobile,
                    parentJob: fetchStudent[0].fatherJob,
                    grade: fetchStudent[0].schoolStagesId,
                    gender: fetchStudent[0].gender,
                    email: fetchStudent[0].email,
                    username: fetchStudent[0].userName,
                    password: fetchStudent[0].password,
                    macID: fetchStudent[0].macId,
                    companyID: properties.company_id,
                    branchID: properties.branch_id
                }
            }, () => {
                this.getStates(this.state.city);
            });
        });
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
                id: this.state.studentID,
                companyId: this.state.formContent.companyID,
                branchId: this.state.formContent.branchID,
                fullName: this.state.formContent.fullName,
                password: this.state.formContent.password,
                mobile: this.state.formContent.studentPhone,
                fatherMobile: this.state.formContent.parentPhone,
                fatherJob: this.state.formContent.parentJob,
                gender: this.state.formContent.gender,
                userName: this.state.formContent.username,
                stateId: this.state.formContent.cityState,
                email: this.state.formContent.email,
                schoolStagesId: this.state.formContent.grade
            };
            
            // Send the data to the server
            fetch(properties.api_path + "/student/studentregister", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json; charset=utf-8',
                },
                body: JSON.stringify(studentDataEdit)
            })
            .then(res => {return res.json()})
            .then(done => {
                _this.setState({
                    buttonText: "تعديل الطالب",
                    buttonColor: ""
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
                    buttonText: "إنشاء الحساب",
                    buttonColor: "",
                    msg: {
                        msgText: "",
                        msgType: "warning",
                        msgIcon: "fas fa-times-circle"
                    }});

                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
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
                                                <InputText id="e-mail" type="email" value={this.state.formContent.email} name="e-mail" onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    email: e.target.value
                                                }})}} />
                                                
                                                <label htmlFor="e-mail">البريد الإلكتروني</label>
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
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputText id="username" value={this.state.formContent.username} name="username" type="text" onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    username: e.target.value
                                                }})}} />
                                                
                                                <label htmlFor="username">إسم المستخدم</label>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputText id="password" value={this.state.formContent.password} name="password" type="password" onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    password: e.target.value
                                                }})}} />
                                                
                                                <label htmlFor="password">كلمة السر</label>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputText id="macAddress" value={this.state.formContent.macID} name="macAddress" type="text" onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    macID: e.target.value
                                                }})}} />
                                                
                                                <label htmlFor="macAddress">Mac ID</label>
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