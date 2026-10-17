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
import { Toast } from 'primereact/toast';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import Select from '../../components/Field/Select';

// Properties
const properties = require('../../properties.json');

class AllInbox extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        globalFilter: null,

        // Teacher Identifier
        branchID: properties.branch_id,
        companyID: properties.company_id,
        teacherId: null ,
        // Data
        data: [],

        getSchoolStages: [],
        selectedSchoolStages:null,
        materialList:[],
        material:null,
        selectedType:null,
        // Update Data
        resend: false,

        // True from the start: this list is fetched on mount, so there is a
        // wait to report before the first rows arrive. Only TableEmpty reads
        // it -- an empty table that is still loading says so rather than
        // claiming there is nothing here.
        tableLoading: true,

        // one flag per lookup list, raised inside its own fetch
        stagesLoading: false,
        materialsLoading: false
    }

    // NOTE: the legacy call also filtered by schoolId (schoolStagesId), but
    // GET /api/manasa/messages only accepts materialId and readDone -- there
    // is no school-stage filter on the messages endpoint. The school-stage
    // dropdown below is left in the UI (still drives the materials dropdown)
    // but no longer narrows this particular list.
    getInbox = (selectedSchoolStages,materialId,selectedType) => {
        var _this = this;

        // Every call announces itself, not just the first: this runs again on
        // every filter change, and a refetch that showed nothing looked as
        // though the filter had not registered. The rows are cleared with it
        // so the waiting row has somewhere to appear -- TableEmpty only
        // renders while the table is empty.
        this.setState({ tableLoading: true, data: [] });

        // Get Messages From API
        apiFetch("/messages", { query: { materialId, readDone: selectedType } })
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

    deleteExam = (id) => {
        var _this = this;

        // Confirm Deletion
        var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

        if (con) {
            // Delete Message By ID
            apiFetch(`/messages/${id}`, { method: "DELETE" })
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
        document.title = properties.site_name + " | كل الاسئله الوارده";
        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        this.getInbox(this.state.selectedSchoolStages,this.state.material,this.state.selectedType);
        this.getSchoolStages()
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState.resend !== this.state.resend) {
            this.getInbox();

            this.setState({resend: false});
        }
    }

    render() {
        let type = [
            { label: "تم الرد", value: 1 },
            { label: "لم يتم الرد", value: 0 },
          ];        let  schoolStages = this.state.getSchoolStages.map(schoolStage => {
            return {label: schoolStage.name, value: schoolStage.id}});
        let  periods = this.state.data.map(period => {
            return {label: period.name, value: period.id}});
            let  materials = this.state.materialList.map(material => {
                return {label: material.name, value: material.id}});

        // The three pickers were a row of their own above the table, built on
        // p-col widths meant for a grid they were not in. They are what
        // narrows this table, so they belong in its toolbar beside the search.
        const header = (
            <div className="table-header">
                {/* no heading here: the page header above carries it, and it
                    was the same four words twice, twenty pixels apart */}
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
                          selectedSchoolStages: e.value,
                        formContent: {
                          ...this.state.formContent,
                          schoolId: e.value,
                        }

                      },
                      // The materials fetch that used to sit here as well has
                      // moved to the materials list's own onOpen: it was
                      // fetching that list here AND again on opening it, so
                      // the same request went out twice.
                      this.getInbox(e.value,this.state.material,this.state.selectedType)
                      )
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
                        material: e.value,
                        formContent: {
                          ...this.state.formContent,
                          materialId: e.value,
                        },
                      },
                      this.getInbox(this.state.selectedSchoolStages,e.value,this.state.selectedType)
                      // this.getPeriod(e.value )
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

                <div className="m-tool-stage">
                  <Select
                    value={this.state.selectedType}
                    options={type}
                    placeholder="نوع السؤال"
                    onChange={(e) => {


                      this.setState({
                          selectedType: e.target.value,
                      });
                      this.getInbox(this.state.selectedSchoolStages,this.state.material,e.value)
                    }}
                    optionLabel="label"
                    showClear
                  />
                </div>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" title="عرض السؤال والرد عليه" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => this.props.history.push(`/allInbox/edit/${rowData.id}`)} />
                    {/* <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => this.deleteExam(rowData.id)} /> */}
                </React.Fragment>
            );
        }

        return (
            <>
                <Header pageName=" كل الاسئله الوارده" />

                <Toast ref={this.toast}></Toast>

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
                                    icon="pi pi-inbox"
                                    title="لا يوجد اسئله"
                                    hint="اختر مرحلة ومادة من الأعلى لعرض أسئلة طلابها، أو ابحث بعنوان السؤال"
                                  />
                                }>
                                    {/* no column filter: the search above already covers
                                        every column, and the four empty boxes it put
                                        inside the header row said nothing about what
                                        they filtered */}
                                    <Column sortable field="subject" header=" العنوان"></Column>
                                    <Column sortable field="materialName" header="الماده"></Column>
                                    <Column sortable field="body" header="السؤال"></Column>
                                    <Column sortable field="sentDate" header="تاريخ الارسال"></Column>
                                    {/* <Column filter sortable field="timer" header="الوقت"></Column> */}
                                    <Column header="الإجراءات" headerStyle={{ width: '84px' }} bodyStyle={{ width: '84px' }} body={actionBodyTemplate}></Column>
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
export default withRouter(AllInbox);
