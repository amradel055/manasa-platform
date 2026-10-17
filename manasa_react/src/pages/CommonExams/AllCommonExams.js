import React, {Component, createRef} from 'react';

// CSS
import './Exams.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import TableShell from '../../components/Table/TableShell';
import TableEmpty from '../../components/Table/TableEmpty';
import Select from '../../components/Field/Select';
import SlidePanel from '../../components/Panel/SlidePanel';
import Confirm from '../../components/Modal/Confirm';
import AddCommonExam from './AddCommonExam';
import '../../components/Field/Form.scss';
import { Toast } from 'primereact/toast';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';

// Properties
const properties = require('../../properties.json');

class AllCommonExams extends Component {
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

        getSchoolStages: [],
        selectedSchoolStages:null,
        materialList:[],
        material:null,
        // Update Data
        resend: false,

        // whether the add panel is open. It mounts the very same
        // AddCommonExam the /commonQuestions/add route mounts; that route
        // still works.
        addOpen: false,

        // False to begin with: this table shows nothing until a material is
        // picked, so there is no wait to report before one is.
        tableLoading: false,

        // one flag per list, raised inside its own fetch
        stagesLoading: false,
        materialsLoading: false,

        // a delete is in flight, and the row waiting on a yes
        deleting: false,
        confirmRow: null,
    }

    getExams = (materialId) => {
        var _this = this;
        // Get Exams From API
        // Every call, not just the first: a silent refetch after a delete
        // reads as though nothing happened. The rows go with it so the
        // waiting row has somewhere to appear.
        this.setState({ tableLoading: true, data: [] });

        apiFetch("/common-questions", { query: { materialId: materialId } })
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

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });
    }

    // The confirmation that used to be window.confirm is the Confirm modal
    // in the render below. The gate is the same, and so is everything under it.
    deleteExam = (id) => {
        var _this = this;

        this.setState({ deleting: true });

        // Delete Subscription By ID
        apiFetch(`/common-questions/${id}`, { method: "DELETE" })
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

     getSchoolStages(){
        // inside the fetch, so opening the list reports the wait too
        this.setState({ stagesLoading: true });

        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
        .then(grades => {
            this.setState({getSchoolStages : grades, stagesLoading: false});
        })
        .catch(error => {
            this.setState({ stagesLoading: false });
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
        document.title = properties.site_name + " | كل الاسئله";
        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        this.getExams(this.state.material);
        this.getSchoolStages()
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState.resend !== this.state.resend) {
            this.getExams(this.state.material);

            this.setState({resend: false});
        }
    }

    render() {
        let  schoolStages = this.state.getSchoolStages.map(schoolStage => {
            return {label: schoolStage.name, value: schoolStage.id}});
            let  materials = this.state.materialList.map(material => {
                return {label: material.name, value: material.id}});
        // The two pickers were a row of their own above the table, built on
        // p-col widths meant for a grid they were not in. They are what
        // narrows this table, so they belong in its toolbar beside the search.
        const header = (
            <div className="table-header">
                <span className="p-input-icon-left m-tool-search">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الاسئله" />
                </span>

                <div className="m-tool-stage">
                    <Select
                        value={this.state.selectedSchoolStages}
                        options={schoolStages}
                        onChange={ (e) =>
                          this.setState({
                              selectedSchoolStages: e.value
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

                <div className="m-tool-stage">
                    <Select
                        value={this.state.material}
                        options={materials}
                        onChange={(e) =>
                          this.setState({
                            material: e.value
                          },
                          () => this.getExams(e.value)
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

                <span className="m-tool-count">
                    عدد الاسئله <b>{this.state.data.length}</b>
                </span>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" title="تعديل السؤال" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => window.location.href = `/commonQuestions/edit/${rowData.id}`} />
                    <Button icon="pi pi-trash" title="حذف السؤال" className="p-button-rounded p-button-danger" onClick={() => this.setState({ confirmRow: rowData })} />
                </React.Fragment>
            );
        }

        return (
            <>
                <Header
                    pageName=" كل الاسئله الشائعه"
                    action={{ label: "إضافة سؤال", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
                />

                <Toast ref={this.toast}></Toast>

                {/* A question with its answers is a long form, so a slide-over
                    rather than a modal, and wrapped rather than rewritten. */}
                <SlidePanel
                    visible={this.state.addOpen}
                    onHide={() => this.setState({ addOpen: false })}
                    title="إضافة سؤال شائع"
                    subtitle="اكتب السؤال وإجاباته، ثم احفظه"
                >
                    <div className="m-form m-form-legacy">
                        {this.state.addOpen ? <AddCommonExam /> : null}
                    </div>
                </SlidePanel>

                {/* the question that used to be the browser's */}
                <Confirm
                    visible={this.state.confirmRow !== null}
                    onHide={() => this.setState({ confirmRow: null })}
                    onConfirm={() => this.deleteExam(this.state.confirmRow.id)}
                    busy={this.state.deleting}
                    title="حذف سؤال"
                    message="هل أنت متأكد من حذف هذا السؤال؟"
                    note="ستُحذف إجاباته معه. لا يمكن التراجع عن هذه الخطوة."
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
                                rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="عرض {first} – {last} من {totalRecords} سؤال"
                                emptyMessage={
                                  <TableEmpty
                                    loading={this.state.tableLoading}
                                    icon="pi pi-question"
                                    title="لا يوجد اسئله"
                                    hint="اختر مرحلة ومادة من الأعلى لعرض أسئلتها، أو أضف سؤالاً جديداً"
                                    action={{ label: "إضافة سؤال", onClick: () => this.setState({ addOpen: true }) }}
                                  />
                                }>
                                    {/* no column filter: the search above covers it */}
                                    <Column sortable field="text" header=" السؤال"></Column>
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

export default AllCommonExams;
