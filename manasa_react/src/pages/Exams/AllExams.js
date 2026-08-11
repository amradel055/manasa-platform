import React, {Component, createRef} from 'react';

// CSS
import './Exams.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import { Toast } from 'primereact/toast';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';

// Properties
const properties = require('../../properties.json');

class AllExams extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        globalFilter: null,

        // Teacher Identifier
        branchID: properties.branch_id,
        companyID: properties.company_id,
        // Data
        data: [],
        stagesById: {},
        lessonsById: {},

        // Update Data
        resend: false
    }

    getExams = () => {
        var _this = this;
        // Get Exams From API
        apiFetch("/exams")
        .then(fetchExams => {
            this.setState({
                data: fetchExams
            });
        })
        .catch(error => {
            console.error('Error:', error)

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
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

    componentDidMount () {
        document.title = properties.site_name + " | كل الإمتحانات";
        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        this.getExams();

        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
        .then(stages => {
            const stagesById = {};
            stages.forEach(s => { stagesById[s.id] = s.name; });
            this.setState({ stagesById });
        })
        .catch(error => console.error('Error:', error));

        apiFetch("/lessons")
        .then(lessons => {
            const lessonsById = {};
            lessons.forEach(l => { lessonsById[l.id] = l.lessonName; });
            this.setState({ lessonsById });
        })
        .catch(error => console.error('Error:', error));
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState.resend !== this.state.resend) {
            this.getExams();

            this.setState({resend: false});
        }
    }
    
    render() {
        const header = (
            <div className="table-header">
                كل الإمتحانات
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
                <Header pageName="كل الإمتحانات" />

                <Toast ref={this.toast}></Toast>

                <div className="content">
                    <div className="container">
                        <div className="card">
                            <Card title="كل الإمتحانات">
                                <DataTable header={header} sortable globalFilter={this.state.globalFilter} className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped" value={this.state.data} paginator rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"  emptyMessage="لا يوجد إمتحانات">
                                    <Column filter sortable field="examName" header="إسم الإمتحان"></Column>
                                    <Column filter sortable body={(row) => this.state.stagesById[row.schoolStagesId] || ''} header="المرحلة الدراسية"></Column>
                                    <Column filter sortable body={(row) => this.state.lessonsById[row.lessonId] || ''} header="الدرس"></Column>
                                    <Column filter sortable field="degree" header="درجه الامتحان"></Column>
                                    <Column filter sortable field="timer" header="الوقت"></Column>
                                    <Column header="تعديل / حذف" body={actionBodyTemplate}></Column>
                                </DataTable>
                            </Card>
                        </div>
                    </div>
                </div>
            </>
        );
    }
}

export default AllExams;