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
import Select from '../../components/Field/Select';
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

class Materials extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        branchID: properties.branch_id,
        globalFilter: null,
        userInput: null,
        selectedStage: null,
        getSchoolStages: [],
        data: [],
        editableData: null,
        visible: false,

        // whether the add-a-material modal is open. Adding itself is still
        // addMaterial below, reading the same two pieces of state it always
        // read -- this only decides where those two fields are shown.
        addOpen: false,

        // The stages are fetched; until they arrive the field says so. Only
        // ever read by the Select's `loading` -- nothing else branches on it.
        stagesLoading: true,

        // Same idea for the rows. False to begin with: this table shows
        // nothing until a stage is picked, so there is no wait to report
        // before one is.
        tableLoading: false,

        // A request is in flight from one of the modals. Each one keeps its
        // own flag so a slow save in one cannot grey out the others.
        adding: false,
        editing: false,
        deleting: false,

        // the row waiting on a yes, or null. Holding the row rather than a
        // boolean is what lets the question name what is being deleted.
        confirmRow: null,
    }

    getSchoolStages() {
        const user = getCurrentUser();
        if (!user) { this.setState({ stagesLoading: false }); return; }
        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: user.userId } })
            .then(stages => {
                this.setState({ getSchoolStages: stages, stagesLoading: false });
            })
            .catch(error => {
                // the wait has to end on failure too, or the field spins forever
                this.setState({ stagesLoading: false });
                console.error('Error:', error);
            });
    }

    getMaterialsByStage = (stageId) => {
        this.setState({ selectedStage: stageId }, () => {
            if (!stageId) {
                this.setState({ data: [] });
                return;
            }
            // the rows go too, so the waiting row has somewhere to appear:
            // TableEmpty only renders while the table is empty
            this.setState({ tableLoading: true, data: [] });

            apiFetch("/lookup/materials/by-stage", { query: { stageId } })
                .then(materials => {
                    this.setState({ data: materials, tableLoading: false });
                })
                .catch(error => {
                    // the wait ends on failure too, or the table spins for ever
                    this.setState({ tableLoading: false });
                    console.error('Error:', error);
                });
        });
    }

    componentDidMount() {
        document.title = properties.site_name + " | المواد";
        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }
        // Not fetched here any more: the stage list fetches when it is
        // opened (see the Select's onOpen), and doing both meant every visit
        // asked for the same list twice.
    }

    addMaterial = () => {
        var _this = this;
        if (!this.state.userInput || !this.state.selectedStage) {
            _this.toast.current.show({ severity: 'error', summary: 'تنبيه', detail: 'إختر المرحلة الدراسية وإسم المادة' });
            return;
        }

        // after the check, not before: a rejected form never looked busy
        this.setState({ adding: true });

        apiFetch("/lookup/materials", {
            method: "POST",
            body: {
                name: this.state.userInput,
                stageId: this.state.selectedStage,
                branchId: this.state.branchID,
            },
        })
            .then(() => {
                _this.toast.current.show({ severity: 'success', summary: 'تم بنجاح', detail: 'تمت الإضافة بنجاح' });
                // done: clear the field, close the modal, and refetch the
                // list for the stage it was added to
                this.setState({ userInput: null, adding: false, addOpen: false });
                this.getMaterialsByStage(this.state.selectedStage);
            })
            .catch(error => {
                // stays open on failure, with what was typed still in it
                this.setState({ adding: false });
                console.error('Error:', error);
                _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
            });
    }

    render() {
        let schoolStages = this.state.getSchoolStages.map(stage => ({ label: stage.name, value: stage.id }));

        // The confirmation that used to be window.confirm is the Confirm
        // modal at the bottom of this render. The gate is the same -- this
        // still only runs on a yes -- and so is everything below it.
        var deleteMaterial = (row) => {
            var _this = this;
            if (!row) return;

            this.setState({ deleting: true });

            apiFetch(`/lookup/materials/${row.id}`, { method: "DELETE" })
                .then(() => {
                    this.setState({ deleting: false, confirmRow: null });
                    this.getMaterialsByStage(this.state.selectedStage);
                    _this.toast.current.show({ severity: 'success', summary: 'تم بنجاح', detail: 'تم الحذف بنجاح' });
                })
                .catch(error => {
                    // the question stays open on failure, so it can be retried
                    this.setState({ deleting: false });
                    console.error('Error:', error);
                    _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
                });
        }

        var editMaterial = () => {
            var _this = this;
            this.setState({ editing: true });

            apiFetch(`/lookup/materials/${this.state.editableData.id}`, {
                method: "PUT",
                body: {
                    name: this.state.editableData.name,
                    stageId: this.state.selectedStage,
                    branchId: this.state.branchID,
                },
            })
                .then(() => {
                    _this.toast.current.show({ severity: 'success', summary: 'تم بنجاح', detail: 'تم التعديل بنجاح' });
                    this.getMaterialsByStage(this.state.selectedStage);
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
                    <InputText type="search" onInput={(e) => this.setState({ globalFilter: e.target.value })} placeholder="بحث في المواد" />
                </span>

                <div className="m-tool-stage">
                    <Select value={this.state.selectedStage} options={schoolStages} placeholder="إختر المرحلة الدراسية" onChange={(e) => this.getMaterialsByStage(e.value)} optionLabel="label" showClear loading={this.state.stagesLoading} />
                </div>

                {/* the name field and its button are in the panel now; what
                    is left here is what filters the table */}
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
                    pageName="المواد"
                    action={{ label: "إضافة مادة", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
                />
                <Toast ref={this.toast}></Toast>

                {/* Two fields and one decision -- a modal, not a slide-over.
                    Same two values addMaterial has always read, asked for in
                    the order it needs them: the stage first, because a
                    material belongs to one, then the name. Picking the stage
                    here calls getMaterialsByStage exactly as the toolbar
                    filter does, so the list behind follows along -- which is
                    also how addMaterial knows where to put it. */}
                <Modal
                    visible={this.state.addOpen}
                    onHide={() => this.setState({ addOpen: false })}
                    title="إضافة مادة"
                    subtitle="اختر المرحلة الدراسية، ثم اكتب إسم المادة"
                    busy={this.state.adding}
                    actions={
                        <Button
                            icon={this.state.adding ? "pi pi-spin pi-spinner" : "pi pi-plus"}
                            label={this.state.adding ? "جارٍ الإضافة…" : "إضافة"}
                            disabled={this.state.adding}
                            onClick={this.addMaterial}
                        />
                    }
                >
                    <div className="m-form">
                        <div className="m-grid">
                            <div className="m-fld">
                                <label htmlFor="add-stage">المرحلة الدراسية <span className="req">*</span></label>
                                <Select inputId="add-stage" value={this.state.selectedStage} options={schoolStages} placeholder="إختر المرحلة الدراسية" onChange={(e) => this.getMaterialsByStage(e.value)} optionLabel="label" showClear loading={this.state.stagesLoading} onOpen={() => this.getSchoolStages()} />
                            </div>

                            <div className="m-fld">
                                <label htmlFor="add-name">إسم المادة <span className="req">*</span></label>
                                <InputText id="add-name" value={this.state.userInput || ''} onChange={(e) => this.setState({ userInput: e.target.value })} placeholder="مثال: رياضيات" />
                            </div>
                        </div>
                    </div>
                </Modal>

                {/* the question that used to be the browser's */}
                <Confirm
                    visible={this.state.confirmRow !== null}
                    onHide={() => this.setState({ confirmRow: null })}
                    onConfirm={() => deleteMaterial(this.state.confirmRow)}
                    busy={this.state.deleting}
                    title="حذف مادة"
                    message={`هل أنت متأكد من حذف «${this.state.confirmRow ? this.state.confirmRow.name : ''}»؟`}
                    note="لا يمكن التراجع عن هذه الخطوة."
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
                                        value={this.state.data}
                                        paginator
                                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                                        rows={10} rowsPerPageOptions={[10, 20, 50]}
                                        currentPageReportTemplate="عرض {first} – {last} من {totalRecords} مادة"
                                        emptyMessage={
                                          <TableEmpty
                                            loading={this.state.tableLoading}
                                            icon="pi pi-book"
                                            title="إختر مرحلة دراسية لعرض موادها"
                                            hint="المواد مرتبطة بالمراحل، فاختر مرحلة من القائمة أعلاه أولاً"
                                          />
                                        }>
                                        <Column field="name" header="إسم المادة"></Column>
                                        <Column header="الإجراءات" headerStyle={{ width: '124px' }} bodyStyle={{ width: '124px' }} body={actionBodyTemplate}></Column>
                                    </DataTable>
                                </TableShell>
                            </Card>
                        </div>
                    </div>
                    {/* One field: the same kind of thing as adding, so the
                        same element. The 50vw it used to be was a half-screen
                        box around a single input. */}
                    <Modal
                        visible={this.state.visible}
                        onHide={() => this.setState({ visible: false })}
                        title="تعديل المادة"
                        busy={this.state.editing}
                        actions={
                            <Button
                                icon={this.state.editing ? "pi pi-spin pi-spinner" : "pi pi-check"}
                                label={this.state.editing ? "جارٍ الحفظ…" : "حفظ التعديل"}
                                disabled={this.state.editing}
                                onClick={editMaterial}
                            />
                        }
                    >
                        {this.state.editableData !== null &&
                            <div className="m-form">
                                <div className="m-grid">
                                    <div className="m-fld">
                                        <label htmlFor="material-name">إسم المادة</label>
                                        <InputText id="material-name" value={this.state.editableData.name} onChange={(e) => this.setState({ editableData: { ...this.state.editableData, name: e.target.value } })} />
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

export default Materials;
