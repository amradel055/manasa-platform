import React, {Component, createRef} from 'react';

// CSS
import './Exams.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';

// Components
import Header from '../../components/Header/Header';
import { Toast } from 'primereact/toast';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';

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
        resend: false
    }

    getInbox = (selectedSchoolStages,materialId,selectedType) => {
        var _this = this;
        var data = JSON.parse(window.localStorage.getItem('currentTeacher'));
        // Get Exams From API
        fetch(properties.api_path + "/inboxmessage/findmessages", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
            teacherId :data["teacherId"] ,
            schoolId:selectedSchoolStages,
            materialId:materialId,
            readDone:selectedType

          
            })
        })
        .then(res => {return res.json()})
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
            fetch(properties.api_path + "/commonquestions/deleteanswer", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    id: id
                })
            })
            .then(res => {return res.json()})
            .then(deleteMediaByID => {
                this.setState({resend: true});
                _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الحذف بنجاح'});
            });
        }
    }
    getTeacherId =async()  => {
        var data =await JSON.parse(window.localStorage.getItem('currentTeacher'));
        this.setState({teacherId : data["teacherId"]});
        
     };
     getSchoolStages(){
        var data =JSON.parse(window.localStorage.getItem('currentTeacher'));
        fetch(properties.api_path + "/lockup/schoolStagesGuideByTeacher", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                teacherId: data["teacherId"]
            })
        })
        .then(res => {return res.json()})
        .then(grades => {
            console.log(grades);
       
            this.setState({getSchoolStages : grades});
        })
     }
     getMaterialByStage(stageId){
        var data =JSON.parse(window.localStorage.getItem('currentTeacher'));
        fetch(properties.api_path + "/lockup/materialListbystage", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: stageId
            })
        })
        .then(res => {return res.json()})
        .then(grades => {
            console.log(grades);
            this.setState({materialList : grades});
        })
     }
    componentDidMount () {
        document.title = properties.site_name + " | كل الاسئله الوارده";
        this.getTeacherId();
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
        const header = (
            <div className="table-header">
                 كل الاسئله الوارده
                <span className="p-input-icon-left">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الاسئله" />
                </span>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => window.location.href = `/allInbox/edit/${rowData.id}`} />
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
                            <Card title="كل الاسئله الوارده">
                            <div className="table-header p-col-12 ">     
                            <div className="p-col-12 p-md-4 p-mt-3 ">
                      <div className="p-inputgroup">
                        <span className="p-float-label">
                          <Dropdown
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
                              
                              this.getMaterialByStage(e.value ),
                              this.getInbox(e.value,this.state.material,this.state.selectedType)
                              ) 
                            }
                            
                            name="grade"
                            placeholder="المرحلة الدراسية"
                          />
                        </span>
                      </div>
                    </div>
                    <div className="p-col-12 p-md-4 p-mt-3">
                      <div className="p-inputgroup">
                        <span className="p-float-label">
                          <Dropdown
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
                          />
                        </span>
                      </div>
                    </div>
                    <div className="p-col-12 p-md-4  p-mt-3">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <Dropdown
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
              </span>
            </div>
          </div>
                    </div>
                                <DataTable header={header} sortable globalFilter={this.state.globalFilter} className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped" value={this.state.data} paginator rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"  emptyMessage="لا يوجد اسئله">
                                    <Column filter sortable field="subject" header=" العنوان"></Column>
                                    <Column filter sortable field="materialName" header="الماده"></Column>
                                    <Column filter sortable field="body" header="السؤال"></Column>
                                    <Column filter sortable field="sentDate" header="تاريخ الارسال"></Column>
                                    {/* <Column filter sortable field="timer" header="الوقت"></Column> */}
                                    <Column header="رؤيه " body={actionBodyTemplate}></Column>
                                </DataTable>
                            </Card>
                        </div>
                    </div>
                </div>
            </>
        );
    }
}

export default AllInbox;