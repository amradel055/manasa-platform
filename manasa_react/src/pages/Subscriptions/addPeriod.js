    import React, {Component, createRef} from 'react';

// CSS
import './Subscription.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
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
import { InputTextarea } from 'primereact/inputtextarea';

// Properties
const properties = require('../../properties.json');

class AddPeriod extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        // Submit Button
        buttonText: "إضافة فترة",
        buttonColor: "",
        grade: null,
        // Teacher Identifier
        branchID: properties.branch_id,
        companyID: properties.company_id,
        teacherId : null ,
        periodName: null ,
        globalFilter: null,
        userInput : null ,
        // Get Data
        data:[],
        editableData : null ,
        dataId:[],
        selectedPeriod: null ,
        selectedPeriodId: null ,
        getSchoolStages: [],
        selectedSchoolStages:null,
        materialList:[],
        material:null,
        // getStudentNames: [],
        // getMonthsList: [],

        // Inputs Data
        // selectedStudent: null,
        // selectedStage: null,
        // selectedMonth: null,
        // cost: null
        visible : false ,

        // whether the add-a-period modal is open. addPeriod below is
        // unchanged and still reads the same four pieces of state -- this
        // only decides where those four fields are shown.
        addOpen: false,

        // the rows are fetched on mount
        tableLoading: true,

        // A request is in flight. One flag each, so a slow save in one modal
        // cannot grey out the other.
        adding: false,
        editing: false,
        deleting: false,

        // the row waiting on a yes, or null -- the row itself, so the
        // question can name the period it is about
        confirmRow: null,

        // one flag per list, raised inside its own fetch
        stagesLoading: false,
        materialsLoading: false,
    }

     getPeriods(){
        // Every call, not just the first: this runs again after an add, an
        // edit or a delete, and a silent refetch reads as though nothing
        // happened. The rows go with it so the waiting row has somewhere to
        // appear -- the empty state only renders while the table is empty.
        this.setState({ tableLoading: true, data: [] });

        apiFetch("/periods")
        .then(grades => {
            this.setState({data : grades, tableLoading: false});
        })
        .catch(error => {
            // the wait ends on failure too, or the table waits for ever
            this.setState({ tableLoading: false });
            console.error('Error:', error);
        });
     }


     getMaterialByStage(stageId){
        this.setState({ materialsLoading: true });

        apiFetch("/lookup/materials/by-stage", { query: { stageId } })
        .then(grades => {
            this.setState({materialList : grades, materialsLoading: false});
        })
        .catch(error => {
            this.setState({ materialsLoading: false });
            console.error('Error:', error);
        });
     }

    componentDidMount () {
       // Not fetched here any more: the stage list fetches when it is
       // opened (see the Select's onOpen). getPeriods below stays -- it
       // fills the table, not just a dropdown.
        document.title = properties.site_name + " | إضافة فترة جديدة";
        this.getPeriods();
       // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }
    }

    componentDidUpdate(){
        //  // Get Periods From API
        //  fetch(properties.api_path + "/periods/periodsByTeacher", {
        //     method: "POST",
        //     headers: {
        //         'Accept': 'application/json',
        //         'Content-Type': 'application/json',
        //     },
        //     body: JSON.stringify({
        //         teacherId: this.state.teacherId
        //     })
        // })
        // .then(res => {return res.json()})
        // .then(periods => {
        //     this.setState({data : grades});
        // })
    }

     addPeriod = () => {
         var _this = this ;

        this.setState({ adding: true });

        // Send the data to the server
        apiFetch("/periods", {
            method: "POST",
            body: {
                name: this.state.userInput,
                branchId: this.state.branchID,
                materialId: this.state.material,
                parentId: this.state.selectedPeriod,
            },
        })
        .then(() => {
            _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تمت الإضافة بنجاح'});
            // done: close the modal and refetch the list
            this.setState({ adding: false, addOpen: false });
            this.getPeriods();
        })
        .catch(error => {
            // stays open on failure, with what was chosen still in it
            this.setState({ adding: false });
            console.error('Error:', error)
            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });
    }

    getSchoolStages(){
        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
        .then(grades => {
            this.setState({getSchoolStages : grades});
        })
        .catch(error => console.error('Error:', error));
     }



    // getStudentsByStageID = (e) => {
    //     this.setState({selectedStage: e.value}, () => {
    //         // Get Students From API
    //         fetch(properties.api_path + "/student/studentList", {
    //             method: "POST",
    //             headers: {
    //                 'Accept': 'application/json',
    //                 'Content-Type': 'application/json',
    //             },
    //             body: JSON.stringify({
    //                 branchId: this.state.branchID,
    //                 schoolStagesId: this.state.selectedStage
    //             })
    //         })
    //         .then(res => {return res.json()})
    //         .then(fetchStudents => {
    //             this.setState({
    //                 getStudentNames: fetchStudents
    //             });
    //         });
    //     });
    // }


    render() {

        let  schoolStages = this.state.getSchoolStages.map(schoolStage => {
            return {label: schoolStage.name, value: schoolStage.id}});
        let  periods = this.state.data.map(period => {
            return {label: period.name, value: period.id}});
            let  materials = this.state.materialList.map(material => {
                return {label: material.name, value: material.id}});

        // NOTE -- this one gained a confirmation it did not have. Deleting
        // a period was a single click on a small round button, with nothing
        // between the click and the request. The request is untouched.
        var deletePeriod = (e) => {
            var _this = this;
            if (!e) return;

            this.setState({ deleting: true });

            // Send the data to the server
            apiFetch(`/periods/${e.id}`, { method: "DELETE" })
            .then(() => {
                this.setState({ deleting: false, confirmRow: null });
                this.getPeriods();
                _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تم الحذف بنجاح'});
            })
            .catch(error => {
                // the question stays open on failure, so it can be retried
                this.setState({ deleting: false });
                console.error('Error:', error)
                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        }

        // the toolbar keeps what narrows the table; the three pickers and
        // the name field are in the add modal now
        const header = (
            <div className="table-header">
                <span className="p-input-icon-left m-tool-search">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الفترات " />
                </span>

                <span className="m-tool-count">
                    عدد الفترات <b>{this.state.data.length}</b>
                </span>
            </div>
        );



        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" title="تعديل الفترة" className="p-button-rounded p-button-secondary p-ml-2" onClick={ () =>this.setState({visible: true , editableData : rowData , selectedPeriod : null})} />
                    <Button icon="pi pi-trash" title="حذف الفترة" className="p-button-rounded p-button-danger" onClick={() => this.setState({ confirmRow: rowData })} />
                </React.Fragment>
            );
        }

        const edit =(id) => {
            var _this = this ;

            this.setState({ editing: true });

            // Send the data to the server
            apiFetch(`/periods/${id}`, {
                method: "PUT",
                body: {
                    name: this.state.userInput,
                    branchId: this.state.branchID,
                    materialId: this.state.material,
                    parentId: this.state.selectedPeriod,
                },
            })
            .then(() => {
                _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تم التعديل بنجاح'});
                this.getPeriods();
                this.setState({visible : false});
            })
            .catch(error => {
                console.error('Error:', error)
                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        }

        return (
            <>
                <Header
                    pageName="إضافة فترة جديدة"
                    action={{ label: "إضافة فترة", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
                />
                <Toast ref={this.toast}></Toast>

                {/* The same four values addPeriod has always read, asked for
                    in the order it needs them: the stage, then the material
                    it belongs to, then the parent period, then the name. */}
                <Modal
                    visible={this.state.addOpen}
                    onHide={() => this.setState({ addOpen: false })}
                    title="إضافة فترة جديدة"
                    subtitle="اختر المرحلة والمادة والفترة الرئيسية، ثم اكتب الإسم"
                    width={700}
                    busy={this.state.adding}
                    actions={
                        <Button
                            icon={this.state.adding ? "pi pi-spin pi-spinner" : "pi pi-plus"}
                            label={this.state.adding ? "جارٍ الإضافة…" : "إضافة فترة"}
                            disabled={this.state.adding}
                            onClick={() => this.addPeriod()}
                        />
                    }
                >
                    <div className="m-form">
                        <div className="m-grid">
                            <div className="m-fld">
                                <label htmlFor="pr-stage">المرحلة الدراسية</label>
                                <Select
                                    inputId="pr-stage"
                                    value={this.state.selectedSchoolStages}
                                    options={schoolStages}
                                    // the material list fetches itself when
                                    // opened, so fetching it from here as well
                                    // asked for it twice
                                    onChange={ (e) =>
                                      this.setState({
                                          selectedSchoolStages: e.value,
                                        formContent: {
                                          ...this.state.formContent,
                                          schoolId: e.value,
                                        },

                                      })
                                    }
                                    name="grade"
                                    placeholder="المرحلة الدراسية"
                                    optionLabel="label"
                                    showClear
                                    loading={this.state.stagesLoading}
                                    onOpen={() => this.getSchoolStages()}
                                />
                            </div>

                            <div className="m-fld">
                                <label htmlFor="pr-material">المادة</label>
                                <Select
                                    inputId="pr-material"
                                    value={this.state.material}
                                    options={materials}
                                    onChange={(e) =>
                                      this.setState({
                                        material: e.value,
                                        formContent: {
                                          ...this.state.formContent,
                                          materialId: e.value,
                                        },
                                      },
                                      )
                                    }
                                    name="subject"
                                    placeholder="إختر المادة"
                                    optionLabel="label"
                                    showClear
                                    loading={this.state.materialsLoading}
                                    needs={this.state.selectedSchoolStages}
                                    needsLabel="اختر المرحلة الدراسية أولاً"
                                    onOpen={() => this.getMaterialByStage(this.state.selectedSchoolStages)}
                                />
                            </div>

                            <div className="m-fld">
                                <label htmlFor="pr-parent">الفترة الرئيسية</label>
                                <Select
                                    inputId="pr-parent"
                                    value={this.state.selectedPeriod}
                                    options={periods}
                                    placeholder="  إختر الفترة الرئيسية"
                                    onChange={(e) => this.setState({selectedPeriod : e.value})}
                                    optionLabel="label"
                                    showClear
                                />
                            </div>

                            <div className="m-fld">
                                <label htmlFor="pr-name">إسم الفترة</label>
                                {/* onChange, not onInput: the field is
                                    controlled by `value`, and React re-writes
                                    that value on every render -- so without
                                    the handler it expects, typing can be
                                    undone the moment anything else re-renders. */}
                                <InputText id="pr-name" value={this.state.userInput || ''} onChange={ (e) => this.setState({userInput : e.target.value})} placeholder="مثال: الفصل الأول" />
                            </div>
                        </div>
                    </div>
                </Modal>

                {/* a period had no confirmation at all before */}
                <Confirm
                    visible={this.state.confirmRow !== null}
                    onHide={() => this.setState({ confirmRow: null })}
                    onConfirm={() => deletePeriod(this.state.confirmRow)}
                    busy={this.state.deleting}
                    title="حذف فترة"
                    message={`هل أنت متأكد من حذف «${this.state.confirmRow ? this.state.confirmRow.name : ''}»؟`}
                    note="الحصص والإشتراكات المرتبطة بهذه الفترة قد تتأثر. لا يمكن التراجع عن هذه الخطوة."
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
                                  rows={10} rowsPerPageOptions={[10,20,50]}
                                   currentPageReportTemplate="عرض {first} – {last} من {totalRecords} فترة"
                                   emptyMessage={
                                     <TableEmpty
                                       loading={this.state.tableLoading}
                                       icon="pi pi-calendar"
                                       title="لا يوجد فترات"
                                       hint="أضف فتراتك أولاً، فالحصص والإشتراكات تُربط بها"
                                       action={{ label: "إضافة فترة", onClick: () => this.setState({ addOpen: true }) }}
                                     />
                                   }>
                                    <Column  field = "name"
                                    header= "اسم الفتره " >
                                    </Column>
                                    <Column header="الإجراءات"
                                    headerStyle={{ width: '124px' }} bodyStyle={{ width: '124px' }}
                                    body={actionBodyTemplate}
                                    ></Column>
                                </DataTable>
                                </TableShell>
                            </Card>
                        </div>
                    </div>
                    <Modal
                        visible={this.state.visible}
                        onHide={() => this.setState({visible : false} )}
                        title="تعديل الفترة"
                        busy={this.state.editing}
                        actions={
                            <Button
                                icon={this.state.editing ? "pi pi-spin pi-spinner" : "pi pi-check"}
                                label={this.state.editing ? "جارٍ الحفظ…" : "حفظ التعديل"}
                                disabled={this.state.editing}
                                onClick={ this.state.editableData!== null ?() => edit(this.state.editableData["id"]) : null }
                            />
                        }
                    >
                        <div className="m-form">
                            <div className="m-grid">
                                <div className="m-fld">
                                    <label htmlFor="ed-parent">الفترة الرئيسية</label>
                                    <Select inputId="ed-parent" value={this.state.selectedPeriod} options={periods} placeholder="  إختر الفترة الرئيسية" onChange={(e) => this.setState({selectedPeriod : e.value})} optionLabel="label" showClear />
                                </div>

                                <div className="m-fld">
                                    <label htmlFor="ed-name">إسم الفترة</label>
                                    <InputText id="ed-name" tooltip="اسم الفترة" onChange={(i)=> this.setState({userInput : i.target.value})}></InputText>
                                </div>
                            </div>
                        </div>
                    </Modal>
                </div>
            </>
        );
    }
}

export default AddPeriod;