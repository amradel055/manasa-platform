import React, {Component, createRef} from 'react';
import { Link, NavLink, withRouter } from 'react-router-dom';
import {Dialog} from 'primereact/dialog'
import Popup from 'reactjs-popup';
// CSS
import './Students.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import TableShell from '../../components/Table/TableShell';
import TableEmpty from '../../components/Table/TableEmpty';
import Confirm from '../../components/Modal/Confirm';
import { Toast } from 'primereact/toast';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';

// Properties
const properties = require('../../properties.json');

class AllStudents extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        globalFilter: null,

        // Teacher Identifier
        branchID: properties.branch_id,

        // Data
        data: [],

        // the requests are fetched on mount
        tableLoading: true,

        // A request is in flight. One flag each: activating one student must
        // not grey out the buttons on another row.
        activating: false,
        deleting: false,

        // the row waiting on a yes, or null -- one per question. The row
        // itself, so each question can name the student it is about.
        confirmActivate: null,
        confirmDelete: null,

        // Update Data
        resend: false ,
        visible : false
    }

    getStudents = () => {
        // Every call, not just the first: componentDidUpdate runs this again
        // after an activation or a rejection, and a silent refetch reads as
        // though nothing happened. The rows go with it so the waiting row
        // has somewhere to appear.
        this.setState({ tableLoading: true, data: [] });

        apiFetch("/students/period-orders", { query: { branchId: this.state.branchID } })
        .then(fetchStudents => {
            this.setState({
                data: fetchStudents,
                tableLoading: false
            });
        })
        .catch(error => {
            // the wait ends on failure too, or the table waits for ever
            this.setState({ tableLoading: false });
            console.error("Error:", error);
        });
    }

    viewStudent =(R)=>{


    }

    // The confirmation that used to be window.confirm is the Confirm modal
    // in the render below. The gate is the same -- this still only runs on a
    // yes -- and the request, the toasts and the `resend` flip that triggers
    // the refetch are unchanged.
    deleteStudent = (periodOrderId) => {
        var _this = this;

        this.setState({ deleting: true });

        apiFetch(`/students/period-orders/${periodOrderId}`, { method: "DELETE" })
        .then(() => {
            this.setState({resend: true, deleting: false, confirmDelete: null});
            _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الحذف بنجاح'});
        })
        .catch(error => {
            // the question stays open on failure, so it can be retried
            this.setState({ deleting: false });
            console.error('Error:', error)
            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
        });
    }

    // NOTE -- this one gained a confirmation it did not have. Activating a
    // request grants a student paid access to a whole period, and it was a
    // single click on a small round button beside a delete button of the
    // same size. The request itself is untouched.
    editStudent = (periodOrderId) => {
        var _this = this;

        this.setState({ activating: true });

        apiFetch(`/students/period-orders/${periodOrderId}/confirm`, { method: "POST" })
        .then(() => {
            this.setState({resend: true, activating: false, confirmActivate: null});
            _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الاشتراك بنجاح'});
        })
        .catch(error => {
            this.setState({ activating: false });
            console.error('Error:', error)
            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
        });
    }
    componentDidMount () {
        document.title = properties.site_name + " | كل الطلاب";

       // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        this.getStudents();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState.resend !== this.state.resend) {
            this.getStudents();

            this.setState({resend: false});
        }
    }

        render() {


        const header = (
            <div className="table-header">
                <span className="p-input-icon-left m-tool-search">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الطلاب" />
                </span>

                {/* the count was loose text beside the search; it is a figure,
                    so it is drawn as one */}
                <span className="m-tool-count">
                    عدد الطلبات <b>{this.state.data.length}</b>
                </span>
            </div>
        );


        const showResetPopUp = (rowData) => {
            this.setState({ visible: true, studentId: rowData.studentId });
          };

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    {/* Two things were wrong with this link. The route is
                        /exams/view/:studentId -- a STUDENT id -- and it was
                        being handed rowData.id, which is the period-order's.
                        And the path was relative, so where it landed depended
                        on the page it was clicked from. */}
                    <Button icon="pi pi-eye" title="امتحانات الطالب" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => this.props.history.push(`/exams/view/${rowData.studentId}`)} />

                    <Button
            icon="pi pi-check"
            title="تفعيل الاشتراك"
            className="p-button-rounded p-button-success p-ml-2"
            onClick={() => this.setState({ confirmActivate: rowData })}
          />
          <Button
            icon="pi pi-trash"
            title="رفض الطلب"
            className="p-button-rounded p-button-danger"
            onClick={() => this.setState({ confirmDelete: rowData })}
          />                </React.Fragment>
            );
        }
        return (
            <>
                <Header pageName="تفعيل الطلاب" />

                <Toast ref={this.toast}></Toast>

                {/* Activating grants access to a whole period, so it asks --
                    it did not before. Rejecting asked through the browser's
                    own dialog; now both ask the same way. */}
                <Confirm
                    visible={this.state.confirmActivate !== null}
                    onHide={() => this.setState({ confirmActivate: null })}
                    onConfirm={() => this.editStudent(this.state.confirmActivate.id)}
                    busy={this.state.activating}
                    tone="brand"
                    icon="pi pi-user-plus"
                    title="تفعيل اشتراك"
                    message={`تفعيل اشتراك «${this.state.confirmActivate ? this.state.confirmActivate.studentFullName : ''}» في فترة «${this.state.confirmActivate ? this.state.confirmActivate.periodName : ''}»؟`}
                    note="سيحصل الطالب على وصول كامل لحصص هذه الفترة."
                    confirmLabel="نعم، فعّل"
                    busyLabel="جارٍ التفعيل…"
                />

                <Confirm
                    visible={this.state.confirmDelete !== null}
                    onHide={() => this.setState({ confirmDelete: null })}
                    onConfirm={() => this.deleteStudent(this.state.confirmDelete.id)}
                    busy={this.state.deleting}
                    title="رفض طلب اشتراك"
                    message={`هل أنت متأكد من رفض طلب «${this.state.confirmDelete ? this.state.confirmDelete.studentFullName : ''}»؟`}
                    note="سيُحذف الطلب من القائمة. لا يمكن التراجع عن هذه الخطوة."
                    confirmLabel="نعم، ارفض"
                    busyLabel="جارٍ الحذف…"
                />

                <div className="content">
                    <div className="container">
                        <div className="card">
                            {/* no title: the page header above already says it */}
                            <Card>
                                {/* presentation only -- see components/Table/TableShell.js */}
                                <TableShell>
                                <DataTable header={header} sortable globalFilter={this.state.globalFilter}
                                className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                                value={this.state.data} paginator
                                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                                rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="عرض {first} – {last} من {totalRecords} طلب"
                                emptyMessage={
                                  <TableEmpty
                                    loading={this.state.tableLoading}
                                    icon="pi pi-user-plus"
                                    title="لا يوجد طلبات اشتراك"
                                    hint="عندما يسجّل طالب ويطلب فترة، سيظهر طلبه هنا لتفعّله"
                                  />
                                }>
                                    {/* no column filters: the search above covers all
                                        three of these columns on its own */}
                                    <Column sortable field="studentFullName" header="إسم الطالب"></Column>
                                    <Column sortable field="studentMobile" header="رقم التلفون"></Column>
                                    <Column sortable field="periodName" header="الفتره "></Column>
                                    <Column header="الإجراءات" headerStyle={{ width: '160px' }} bodyStyle={{ width: '160px' }} body={actionBodyTemplate}></Column>

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
export default withRouter(AllStudents);