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
import { Dropdown } from 'primereact/dropdown';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Dialog } from 'primereact/dialog';

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
    }

    getSchoolStages() {
        const user = getCurrentUser();
        if (!user) return;
        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: user.userId } })
            .then(stages => {
                this.setState({ getSchoolStages: stages });
            })
            .catch(error => console.error('Error:', error));
    }

    getMaterialsByStage = (stageId) => {
        this.setState({ selectedStage: stageId }, () => {
            if (!stageId) {
                this.setState({ data: [] });
                return;
            }
            apiFetch("/lookup/materials/by-stage", { query: { stageId } })
                .then(materials => {
                    this.setState({ data: materials });
                })
                .catch(error => console.error('Error:', error));
        });
    }

    componentDidMount() {
        document.title = properties.site_name + " | المواد";
        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }
        this.getSchoolStages();
    }

    addMaterial = () => {
        var _this = this;
        if (!this.state.userInput || !this.state.selectedStage) {
            _this.toast.current.show({ severity: 'error', summary: 'تنبيه', detail: 'إختر المرحلة الدراسية وإسم المادة' });
            return;
        }
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
                this.setState({ userInput: null });
                this.getMaterialsByStage(this.state.selectedStage);
            })
            .catch(error => {
                console.error('Error:', error);
                _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
            });
    }

    render() {
        let schoolStages = this.state.getSchoolStages.map(stage => ({ label: stage.name, value: stage.id }));

        var deleteMaterial = (row) => {
            var _this = this;
            var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");
            if (con) {
                apiFetch(`/lookup/materials/${row.id}`, { method: "DELETE" })
                    .then(() => {
                        this.getMaterialsByStage(this.state.selectedStage);
                        _this.toast.current.show({ severity: 'success', summary: 'تم بنجاح', detail: 'تم الحذف بنجاح' });
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        _this.toast.current.show({ severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال' });
                    });
            }
        }

        var editMaterial = () => {
            var _this = this;
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
                    <InputText type="search" onInput={(e) => this.setState({ globalFilter: e.target.value })} placeholder="بحث في المواد" />
                </span>
                <div className="p-col-12 p-md-3 p-mt-3">
                    <Dropdown value={this.state.selectedStage} options={schoolStages} placeholder="إختر المرحلة الدراسية" onChange={(e) => this.getMaterialsByStage(e.value)} optionLabel="label" filter showClear filterBy="label" />
                </div>
                <div className="p-col-12 p-md-3 p-mt-3">
                    <InputText value={this.state.userInput || ''} onChange={(e) => this.setState({ userInput: e.target.value })} placeholder="إسم المادة الجديدة" />
                </div>
                <div>
                    <Button icon="pi pi-plus" className="p-button-rounded p-button-secondary p-mr-4 p-md-2 p-mt-3 p-ms-1" onClick={this.addMaterial} />
                </div>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => this.setState({ visible: true, editableData: { ...rowData } })} />
                    <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => deleteMaterial(rowData)} />
                </React.Fragment>
            );
        }

        return (
            <>
                <Header pageName="المواد" />
                <Toast ref={this.toast}></Toast>
                <div className="content">
                    <div className="container">
                        <div className="card">
                            <Card title="المواد">
                                <DataTable
                                    header={header} sortable
                                    globalFilter={this.state.globalFilter}
                                    className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                                    value={this.state.data} paginator rows={10} rowsPerPageOptions={[10, 20, 50]}
                                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
                                    emptyMessage="إختر مرحلة دراسية لعرض موادها">
                                    <Column field="name" header="إسم المادة"></Column>
                                    <Column header="تعديل / حذف" body={actionBodyTemplate}></Column>
                                </DataTable>
                            </Card>
                        </div>
                    </div>
                    <Dialog
                        header="تعديل المادة"
                        visible={this.state.visible}
                        style={{ width: '50vw' }}
                        footer={<Button label="تعديل" className="p-button-secondary p-mr-1" onClick={editMaterial}></Button>}
                        onHide={() => this.setState({ visible: false })}>
                        {this.state.editableData !== null &&
                            <div className="p-col-10 p-m-5">
                                <div className="p-inputgroup">
                                    <span className="p-float-label">
                                        <InputText value={this.state.editableData.name} onChange={(e) => this.setState({ editableData: { ...this.state.editableData, name: e.target.value } })} />
                                        <label>إسم المادة</label>
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

export default Materials;
