import React, { Component, createRef } from 'react';

// CSS
import '../Subscriptions/Subscription.scss';

// Helper
import { checkCookie } from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import TableShell from '../../components/Table/TableShell';
import TableEmpty from '../../components/Table/TableEmpty';
import Modal from '../../components/Modal/Modal';
import Confirm from '../../components/Modal/Confirm';
import '../../components/Field/Form.scss';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';

// Properties
const properties = require('../../properties.json');

class SchoolStages extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        branchID: properties.branch_id,
        globalFilter: null,
        userInput: null,
        data: [],
        editableData: null,
        visible: false,

        // whether the add-a-stage modal is open. addStage below is unchanged
        // and still reads the same userInput -- this only decides where that
        // one field is shown.
        addOpen: false,

        // the rows are fetched on mount, so the table waits from the start
        tableLoading: true,

        // A request is in flight from one of the modals. One flag each, so a
        // slow save in one cannot grey out the others.
        adding: false,
        editing: false,
        deleting: false,

        // the row waiting on a yes, or null. The row itself rather than a
        // boolean, so the question can name what is being deleted.
        confirmRow: null,
    }

    getStages() {
        const user = getCurrentUser();
        if (!user) { this.setState({ tableLoading: false }); return; }

        // every call, not just the first: this runs again after an add, an
        // edit or a delete, and the rows go with it so the waiting row has
        // somewhere to appear
        this.setState({ tableLoading: true, data: [] });

        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: user.userId } })
            .then(stages => {
                this.setState({ data: stages, tableLoading: false });
            })
            .catch(error => {
                // the wait ends on failure too, or the table waits for ever
                this.setState({ tableLoading: false });
                console.error('Error:', error);
            });
    }

    componentDidMount() {
        document.title = properties.site_name + " | المراحل الدراسية";
        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }
        this.getStages();
    }

    addStage = () => {
        var _this = this;
        if (!this.state.userInput) return;

        // after the guard, not before: a click that does nothing never
        // looked as though it had started
        this.setState({ adding: true });

        apiFetch("/lookup/school-stages", {
            method: "POST",
            body: {
                name: this.state.userInput,
                branchId: this.state.branchID,
            },
        })
            .then(() => {
                _this.toast.current.show({ severity: 'success', summary: 'تم بنجاح', detail: 'تمت الإضافة بنجاح' });
                // done: clear the field, close the modal, refetch the list
                this.setState({ userInput: null, adding: false, addOpen: false });
                this.getStages();
            })
            .catch(error => {
                // stays open on failure, with what was typed still in it
                this.setState({ adding: false });
                console.error('Error:', error);
                _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
            });
    }

    render() {
        // The confirmation that used to be window.confirm is the Confirm
        // modal at the foot of this render. The gate is the same -- this
        // still only runs on a yes -- and so is everything below it.
        var deleteStage = (row) => {
            var _this = this;
            if (!row) return;

            this.setState({ deleting: true });

            apiFetch(`/lookup/school-stages/${row.id}`, { method: "DELETE" })
                .then(() => {
                    this.setState({ deleting: false, confirmRow: null });
                    this.getStages();
                    _this.toast.current.show({ severity: 'success', summary: 'تم بنجاح', detail: 'تم الحذف بنجاح' });
                })
                .catch(error => {
                    // the question stays open on failure, so it can be retried
                    this.setState({ deleting: false });
                    console.error('Error:', error);
                    _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
                });
        }

        var editStage = () => {
            var _this = this;
            this.setState({ editing: true });

            apiFetch(`/lookup/school-stages/${this.state.editableData.id}`, {
                method: "PUT",
                body: {
                    name: this.state.editableData.name,
                    branchId: this.state.branchID,
                },
            })
                .then(() => {
                    _this.toast.current.show({ severity: 'success', summary: 'تم بنجاح', detail: 'تم التعديل بنجاح' });
                    this.getStages();
                    this.setState({ visible: false, editing: false });
                })
                .catch(error => {
                    this.setState({ editing: false });
                    console.error('Error:', error);
                    _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
                });
        }

        const header = (
            <div className="table-header">
                <span className="p-input-icon-left m-tool-search">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({ globalFilter: e.target.value })} placeholder="بحث في المراحل الدراسية" />
                </span>
                {/* the name field and its button are in the modal now */}
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => this.setState({ visible: true, editableData: { ...rowData } })} />
                    <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => this.setState({ confirmRow: rowData })} />
                </React.Fragment>
            );
        }

        return (
            <>
                <Header
                    pageName="المراحل الدراسية"
                    action={{ label: "إضافة مرحلة", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
                />
                <Toast ref={this.toast}></Toast>

                {/* One field and one decision, so a modal rather than a
                    slide-over. The button is live whether or not the field has
                    anything in it: addStage already returns early when it is
                    empty, and a button that greys itself out is worse than a
                    click that does nothing -- it looks broken. */}
                <Modal
                    visible={this.state.addOpen}
                    onHide={() => this.setState({ addOpen: false })}
                    title="إضافة مرحلة دراسية"
                    subtitle="اكتب إسم المرحلة كما تريد أن تظهر لطلابك"
                    busy={this.state.adding}
                    actions={
                        <Button
                            icon={this.state.adding ? "pi pi-spin pi-spinner" : "pi pi-plus"}
                            label={this.state.adding ? "جارٍ الإضافة…" : "إضافة"}
                            disabled={this.state.adding}
                            onClick={this.addStage}
                        />
                    }
                >
                    <div className="m-form">
                        <div className="m-grid">
                            <div className="m-fld">
                                <label htmlFor="add-stage-name">إسم المرحلة الدراسية <span className="req">*</span></label>
                                <InputText id="add-stage-name" value={this.state.userInput || ''} onChange={(e) => this.setState({ userInput: e.target.value })} placeholder="مثال: الصف التاسع" />
                            </div>
                        </div>
                    </div>
                </Modal>

                {/* the question that used to be the browser's */}
                <Confirm
                    visible={this.state.confirmRow !== null}
                    onHide={() => this.setState({ confirmRow: null })}
                    onConfirm={() => deleteStage(this.state.confirmRow)}
                    busy={this.state.deleting}
                    title="حذف مرحلة دراسية"
                    message={`هل أنت متأكد من حذف «${this.state.confirmRow ? this.state.confirmRow.name : ''}»؟`}
                    note="المواد المرتبطة بهذه المرحلة قد تتأثر. لا يمكن التراجع عن هذه الخطوة."
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
                                    <DataTable
                                        header={header} sortable
                                        globalFilter={this.state.globalFilter}
                                        className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                                        value={this.state.data} paginator
                                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                                        rows={10} rowsPerPageOptions={[10, 20, 50]}
                                        currentPageReportTemplate="عرض {first} – {last} من {totalRecords} مرحلة"
                                        emptyMessage={
                                          <TableEmpty
                                            loading={this.state.tableLoading}
                                            icon="pi pi-sitemap"
                                            title="لا يوجد مراحل دراسية"
                                            hint="أضف مراحلك أولاً، فالمواد والحصص تُربط بها"
                                            action={{ label: "إضافة مرحلة", onClick: () => this.setState({ addOpen: true }) }}
                                          />
                                        }>
                                        <Column field="name" header="إسم المرحلة الدراسية"></Column>
                                        <Column header="الإجراءات" headerStyle={{ width: '124px' }} bodyStyle={{ width: '124px' }} body={actionBodyTemplate}></Column>
                                    </DataTable>
                                </TableShell>
                            </Card>
                        </div>
                    </div>
                    {/* one field, same kind of thing as adding */}
                    <Modal
                        visible={this.state.visible}
                        onHide={() => this.setState({ visible: false })}
                        title="تعديل المرحلة الدراسية"
                        busy={this.state.editing}
                        actions={
                            <Button
                                icon={this.state.editing ? "pi pi-spin pi-spinner" : "pi pi-check"}
                                label={this.state.editing ? "جارٍ الحفظ…" : "حفظ التعديل"}
                                disabled={this.state.editing}
                                onClick={editStage}
                            />
                        }
                    >
                        {this.state.editableData !== null &&
                            <div className="m-form">
                                <div className="m-grid">
                                    <div className="m-fld">
                                        <label htmlFor="stage-name">إسم المرحلة الدراسية</label>
                                        <InputText id="stage-name" value={this.state.editableData.name} onChange={(e) => this.setState({ editableData: { ...this.state.editableData, name: e.target.value } })} />
                                    </div>
                                </div>
                            </div>
                        }
                    </Modal>
                </div>
            </>
        );
    }
}

export default SchoolStages;
