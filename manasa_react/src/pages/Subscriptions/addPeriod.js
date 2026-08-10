    import React, {Component, createRef} from 'react';

// CSS
import './Subscription.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import {Dialog} from 'primereact/dialog'
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

    }

     getPeriods(){
        apiFetch("/periods")
        .then(grades => {
            this.setState({data : grades});
        })
        .catch(error => console.error('Error:', error));
     }


     getMaterialByStage(stageId){
        apiFetch("/lookup/materials/by-stage", { query: { stageId } })
        .then(grades => {
            this.setState({materialList : grades});
        })
        .catch(error => console.error('Error:', error));
     }

    componentDidMount () {
       this.getSchoolStages()
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
            this.getPeriods();
        })
        .catch(error => {
            console.error('Error:', error)
            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });
    }

    getSchoolStages(){
        apiFetch("/lookup/school-stages", { query: { branchId: this.state.branchID } })
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
        
        var deletePeriod = (e) => {
            var _this = this;
            // Send the data to the server
            apiFetch(`/periods/${e.id}`, { method: "DELETE" })
            .then(() => {
                this.getPeriods();
                _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تم الحذف بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)
                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        }
        
        const header = (
            <>
               <span className="p-input-icon-left p-col-12 p-md-3 p-mt-3  ">
                      <i className="pi pi-search" />
                      <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الفترات " /> 
  
                  </span>
                  <div className="table-header p-col-12 ">
                  
                  <div className="p-col-12 p-md-3 p-mt-3 ">
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
                                },
                                
                              },
                              
                              this.getMaterialByStage(e.value )
                              ) 
                            }
                            
                            name="grade"
                            placeholder="المرحلة الدراسية"
                          />
                        </span>
                      </div>
                    </div>
                    <div className="p-col-12 p-md-3 p-mt-3">
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
                              // this.getPeriod(e.value )
                              )
                            }
                            name="subject"
                            placeholder="إختر المادة"
                          />
                        </span>
                      </div>
                    </div>
              
  
                   <div className="p-col-12 p-md-3  p-mt-3">
                                          <div className="p-inputgroup">
                                              <span className="p-float-label">
                                                  <Dropdown 
                                                  value={this.state.selectedPeriod} 
                                                  options={periods}
                                                   placeholder="  إختر الفترة الرئيسية"
                                                    onChange={(e) => this.setState({selectedPeriod : e.value})} optionLabel="label"
                                                     filter showClear filterBy="label" />                                            
                                              </span>
                                          </div>
                                      </div>  
                  <div className="p-col-12 p-md-2  p-mt-3">   
                  <InputText type="search" onInput ={ (e) => this.setState({userInput : e.target.value})} placeholder="إضافة فترة" />
  
                  </div>
                  <div >   
                  <Button icon="pi pi-plus" className="p-button-rounded p-button-secondary p-mr-4 p-md-2  p-mt-3 p-ms-1" onClick={() => this.addPeriod()} />
  
                  </div>
               
  
              
              </div>
            </>
         
            
        );

    

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={ () =>this.setState({visible: true , editableData : rowData , selectedPeriod : null})} />
                    <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => deletePeriod(rowData)} />
                </React.Fragment>
            );
        }

        const edit =(id) => {
            var _this = this ;
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
                <Header pageName="إضافة فترة جديدة" />
                <Toast ref={this.toast}></Toast>
                <div className="content">
                    <div className="container">
                        <div className="card">      
                            <Card title="الفترات">         
                                <DataTable 
                                header={header} sortable 
                                globalFilter={this.state.globalFilter}
                                 className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                                  value={this.state.data} paginator rows={10} rowsPerPageOptions={[10,20,50]}
                                   currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"  
                                   emptyMessage="لا يوجد فترات">
                                    <Column  field = "name" 
                                    header= "اسم الفتره " >
                                    </Column>
                                    <Column header="تعديل / حذف" 
                                    body={actionBodyTemplate}
                                    ></Column>
                                </DataTable>
                            </Card>
                        </div>
                    </div>
                    <Dialog 
                    header="تعديل"
                     visible={this.state.visible}
                      style={{ width: '50vw'}}
                       footer={ <Button label="تعديل "
                           
                        className="p-button-secondary p-mr-1" onClick={ this.state.editableData!== null ?() => edit(this.state.editableData["id"]) : null }></Button>} 
                        onHide={() => this.setState({visible : false} )}>
                            <>
                         <div className="p-col-10 p-m-5">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.selectedPeriod} options={periods} placeholder="  إختر الفترة الرئيسية" onChange={(e) => this.setState({selectedPeriod : e.value})} optionLabel="label" filter showClear filterBy="label" /> 
                                            </span>
                                            
                                            <span className="p-float-label  p-mr-4">
                                            <InputText tooltip="اسم الفترة" onChange={(i)=> this.setState({userInput : i.target.value})}></InputText>
                                            </span>
                                        </div>
                                    </div>   
                                   
                                             
                       
                                    </>  
                        
                    </Dialog>
                </div>
            </>
        );
    }
}

export default AddPeriod;