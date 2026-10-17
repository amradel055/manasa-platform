import React, {Component, createRef} from 'react';
import { withRouter } from 'react-router-dom';

// CSS
import './Exams.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import TableShell from '../../components/Table/TableShell';
import TableEmpty from '../../components/Table/TableEmpty';
import SlidePanel from '../../components/Panel/SlidePanel';
import Confirm from '../../components/Modal/Confirm';
import AddExam from './AddExam';
import '../../components/Field/Form.scss';
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
        resend: false,

        // whether the add panel is open. It mounts the very same AddExam the
        // /exams/add route mounts; that route still works.
        addOpen: false,

        // the rows are fetched on mount
        tableLoading: true,

        // a delete is in flight, and the row waiting on a yes
        deleting: false,
        confirmRow: null,
    }

    getExams = () => {
        var _this = this;
        // Get Exams From API
        // Every call, not just the first: a silent refetch after a delete
        // reads as though nothing happened. The rows go with it so the
        // waiting row has somewhere to appear.
        this.setState({ tableLoading: true, data: [] });

        apiFetch("/exams")
        .then(fetchExams => {
            this.setState({
                data: fetchExams,
                tableLoading: false
            });
        })
        .catch(error => {
            // the wait ends on failure too, or the table waits for ever
            this.setState({ tableLoading: false });
            console.error('Error:', error)

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
        });
    }

    // The confirmation that used to be window.confirm is the Confirm modal
    // in the render below. The gate is the same, and so is everything under it.
    deleteExam = (id) => {
        var _this = this;

        this.setState({ deleting: true });

        // Delete Subscription By ID
        apiFetch(`/exams/${id}`, { method: "DELETE" })
        .then(() => {
            this.setState({resend: true, deleting: false, confirmRow: null});
            _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الحذف بنجاح'});
        })
        .catch(error => {
            // the question stays open on failure, so it can be retried
            this.setState({ deleting: false });
            console.error('Error:', error)
            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });
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
                <span className="p-input-icon-left m-tool-search">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الإمتحانات" />
                </span>

                <span className="m-tool-count">
                    عدد الإمتحانات <b>{this.state.data.length}</b>
                </span>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" title="تعديل الإمتحان" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => this.props.history.push(`/exams/edit/${rowData.id}`)} />
                    <Button icon="pi pi-trash" title="حذف الإمتحان" className="p-button-rounded p-button-danger" onClick={() => this.setState({ confirmRow: rowData })} />
                </React.Fragment>
            );
        }

        return (
            <>
                <Header
                    pageName="كل الإمتحانات"
                    action={{ label: "إضافة إمتحان", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
                />

                <Toast ref={this.toast}></Toast>

                {/* A question builder is a long form, so a slide-over rather
                    than a modal. It is wrapped in .m-form-legacy rather than
                    rewritten: 840 lines of p-inputgroup scaffolding, every
                    one of which is a handler that does not need touching to
                    look right. */}
                <SlidePanel
                    visible={this.state.addOpen}
                    onHide={() => this.setState({ addOpen: false })}
                    title="إضافة إمتحان"
                    subtitle="املأ بيانات الإمتحان وأسئلته، ثم احفظه"
                >
                    <div className="m-form m-form-legacy">
                        {this.state.addOpen ? <AddExam /> : null}
                    </div>
                </SlidePanel>

                {/* the question that used to be the browser's */}
                <Confirm
                    visible={this.state.confirmRow !== null}
                    onHide={() => this.setState({ confirmRow: null })}
                    onConfirm={() => this.deleteExam(this.state.confirmRow.id)}
                    busy={this.state.deleting}
                    title="حذف إمتحان"
                    message={`هل أنت متأكد من حذف «${this.state.confirmRow ? this.state.confirmRow.examName : ''}»؟`}
                    note="ستُحذف أسئلته ونتائج طلابه معه. لا يمكن التراجع عن هذه الخطوة."
                    confirmLabel="نعم، احذف"
                    busyLabel="جارٍ الحذف…"
                />

                <div className="content">
                    <div className="container">
                        <div className="card">
                            {/* no title: the page header above already says it */}
                            <Card>
                                {/* presentation only -- see components/Table/TableShell.js */}
                                <TableShell>
                                <DataTable header={header} sortable globalFilter={this.state.globalFilter} className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped" value={this.state.data} paginator
                                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                                rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="عرض {first} – {last} من {totalRecords} إمتحان"
                                emptyMessage={
                                  <TableEmpty
                                    loading={this.state.tableLoading}
                                    icon="pi pi-check-square"
                                    title="لا يوجد إمتحانات"
                                    hint="أنشئ أول إمتحان، وسيظهر هنا لطلابك"
                                    action={{ label: "إضافة إمتحان", onClick: () => this.setState({ addOpen: true }) }}
                                  />
                                }>
                                    {/* no column filters: the search above covers these */}
                                    <Column sortable field="examName" header="إسم الإمتحان"></Column>
                                    <Column sortable body={(row) => this.state.stagesById[row.schoolStagesId] || ''} header="المرحلة الدراسية"></Column>
                                    <Column sortable body={(row) => this.state.lessonsById[row.lessonId] || ''} header="الدرس"></Column>
                                    <Column sortable field="degree" header="درجه الامتحان"></Column>
                                    <Column sortable field="timer" header="الوقت"></Column>
                                    <Column header="الإجراءات" headerStyle={{ width: '124px' }} bodyStyle={{ width: '124px' }} body={actionBodyTemplate}></Column>
                                </DataTable>
                                </TableShell>
                            </Card>
                        </div>
                    </div>
                </div>
            </>
        );
    }
}

// withRouter: this page is mounted as a Route's child, so it is handed
// no router props of its own and cannot reach history without it.
export default withRouter(AllExams);