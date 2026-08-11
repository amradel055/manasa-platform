import React, { Component, createRef } from 'react';

// CSS
import '../Subscriptions/Subscription.scss';

// Helper
import { checkCookie } from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Dialog } from 'primereact/dialog';

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
    }

    getStages() {
        const user = getCurrentUser();
        if (!user) return;
        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: user.userId } })
            .then(stages => {
                this.setState({ data: stages });
            })
            .catch(error => console.error('Error:', error));
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
        apiFetch("/lookup/school-stages", {
            method: "POST",
            body: {
                name: this.state.userInput,
                branchId: this.state.branchID,
            },
        })
            .then(() => {
                _this.toast.current.show({ severity: 'success', summary: 'تم بنجاح', detail: 'تمت الإضافة بنجاح' });
                this.setState({ userInput: null });
                this.getStages();
            })
            .catch(error => {
                console.error('Error:', error);
                _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
            });
    }

    render() {
        var deleteStage = (row) => {
            var _this = this;
            var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");
            if (con) {
                apiFetch(`/lookup/school-stages/${row.id}`, { method: "DELETE" })
                    .then(() => {
                        this.getStages();
                        _this.toast.current.show({ severity: 'success', summary: 'تم بنجاح', detail: 'تم الحذف بنجاح' });
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
                    });
            }
        }

        var editStage = () => {
            var _this = this;
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
                    this.setState({ visible: false });
                })
                .catch(error => {
                    console.error('Error:', error);
                    _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
                });
        }

        const header = (
            <div className="table-header p-col-12">
                <span className="p-input-icon-left p-col-12 p-md-3 p-mt-3">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({ globalFilter: e.target.value })} placeholder="بحث في المراحل الدراسية" />
                </span>
                <div className="p-col-12 p-md-3 p-mt-3">
                    <InputText value={this.state.userInput || ''} onChange={(e) => this.setState({ userInput: e.target.value })} placeholder="إسم المرحلة الدراسية الجديدة" />
                </div>
                <div>
                    <Button icon="pi pi-plus" className="p-button-rounded p-button-secondary p-mr-4 p-md-2 p-mt-3 p-ms-1" onClick={this.addStage} />
                </div>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => this.setState({ visible: true, editableData: { ...rowData } })} />
                    <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => deleteStage(rowData)} />
                </React.Fragment>
            );
        }

        return (
            <>
                <Header pageName="المراحل الدراسية" />
                <Toast ref={this.toast}></Toast>
                <div className="content">
                    <div className="container">
                        <div className="card">
                            <Card title="المراحل الدراسية">
                                <DataTable
                                    header={header} sortable
                                    globalFilter={this.state.globalFilter}
                                    className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                                    value={this.state.data} paginator rows={10} rowsPerPageOptions={[10, 20, 50]}
                                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
                                    emptyMessage="لا يوجد مراحل دراسية">
                                    <Column field="name" header="إسم المرحلة الدراسية"></Column>
                                    <Column header="تعديل / حذف" body={actionBodyTemplate}></Column>
                                </DataTable>
                            </Card>
                        </div>
                    </div>
                    <Dialog
                        header="تعديل المرحلة الدراسية"
                        visible={this.state.visible}
                        style={{ width: '50vw' }}
                        footer={<Button label="تعديل" className="p-button-secondary p-mr-1" onClick={editStage}></Button>}
                        onHide={() => this.setState({ visible: false })}>
                        {this.state.editableData !== null &&
                            <div className="p-col-10 p-m-5">
                                <div className="p-inputgroup">
                                    <span className="p-float-label">
                                        <InputText value={this.state.editableData.name} onChange={(e) => this.setState({ editableData: { ...this.state.editableData, name: e.target.value } })} />
                                        <label>إسم المرحلة الدراسية</label>
                                    </span>
                                </div>
                            </div>
                        }
                    </Dialog>
                </div>
            </>
        );
    }
}

export default SchoolStages;
