import React, {Component, createRef} from 'react';

// CSS
import './Exams.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import { Toast } from 'primereact/toast';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import Select from '../../components/Field/Select';

// Properties
const properties = require('../../properties.json');

class ExamsReports extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        globalFilter: null,

        // Teacher Identifier
        branchID: properties.branch_id,
        companyID: properties.company_id,
        selectedExam : null ,
        examStudentNumber : 0 ,
        // Data
        data: [],
        mistakesData: [],
        allExams : [],
        // Update Data
        resend: false
    }

    getExams = () => {
        var _this = this;
        // Get Exams From API
        apiFetch("/exams")
        .then(fetchExams => {
            this.setState({
                allExams: fetchExams
            });
        })
        .catch(error => {
            console.error('Error:', error)

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });
    }

    deleteExam = (id) => {
        var _this = this;

        // Confirm Deletion
        var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

        if (con) {
            // Delete Subscription By ID
            apiFetch(`/exams/${id}`, { method: "DELETE" })
            .then(() => {
                this.setState({resend: true});
                _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الحذف بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)
                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        }
    }

     getData = (examId) => {
        // Single report endpoint replaces the old examStudentNumber /
        // examStudentbestNumbers / mostMistakes trio. It only returns
        // attemptCount + a best-students list of {attemptId, studentProfileId,
        // score} -- there's no studentName/schoolStagesName/lessonName/degree/
        // solutionTime, and no "most mistakes" data at all in the new API.
        apiFetch(`/exams/${examId}/report`)
        .then(report => {
            this.setState({
                examStudentNumber: report.attemptCount,
                data: report.bestStudents,
            });
        })
        .catch(error => console.error('Error:', error));
     }

    componentDidMount () {
        document.title = properties.site_name + " | تقارير الإمتحانات";
        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        this.getExams();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState.resend !== this.state.resend) {
            this.getExams();

            this.setState({resend: false});
        }
    }
    
    render() {
        const allExams = this.state.allExams.map(exam => {
            return {
                label: exam.examName,
                value: exam.id
            }
        });

        const header = (
            <div className="table-header">
                درجات الطلاب
                <span className="p-input-icon-left">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الإمتحانات" />
                </span>
            </div>
        );
        const header2 = (
            <div className="table-header">
                اكثر الاسئله التي تم الاجابه عنها بطريقه خاطئه
                <span className="p-input-icon-left">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الإمتحانات" />
                </span>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => window.location.href = `exams/edit/${rowData.id}`} />
                    <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => this.deleteExam(rowData.id)} />
                </React.Fragment>
            );
        }

        return (
            <>
                <Header pageName="تقارير الإمتحانات" />

                <Toast ref={this.toast}></Toast>

                <div className="content">
                    <div className="container">
                    <div className="p-my-4 ">
                            
                            <span className="p-px-4">
                                <Select className="p-dropDown-lg p-px-6 p-text-center" value={this.state.selectedExam} options={allExams} onChange={(e) => this.setState({selectedExam: e.value} , this.getData(e.value))} name="type" placeholder="اختر الامتحان"/>
                               </span>
                        
                        </div>
                        <div className="card">
                            <Card title={<> عدد الطلبه اللذين أدو الامتحان  : {this.state.examStudentNumber} </>}>
                                <DataTable header={header} sortable globalFilter={this.state.globalFilter} className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped" value={this.state.data} paginator rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"  emptyMessage="لا يوجد بيانات">
                                    <Column  field="studentProfileId" header="رقم الطالب"></Column>
                                    <Column sortable field="score" header="درجه الطالب"></Column>
                                </DataTable>
                            </Card>
                        </div>

                        <div className="card p-my-4">
                            <Card >
                                <DataTable header={header2} sortable globalFilter={this.state.globalFilter} className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped" value={this.state.mistakesData} paginator rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"  emptyMessage="لا يوجد بيانات">
                                    <Column   field="questionName" header="السؤال"></Column>
                                    <Column sortable  field="questionDegree" header="عدد المرات"></Column>
                                    
                                </DataTable>
                            </Card>
                        </div>
                    </div>
                </div>
            </>
        );
    }
}

export default ExamsReports;