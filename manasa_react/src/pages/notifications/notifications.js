import React, {Component, createRef} from 'react';

// CSS


// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import Select from '../../components/Field/Select';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import {Dialog} from 'primereact/dialog' ;
import { FileUpload } from 'primereact/fileupload';

import { InputTextarea } from 'primereact/inputtextarea';

// Properties
const properties = require('../../properties.json');

class Notifications extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        // Submit Button
        buttonText: "إضافة اشعار",
        buttonColor: "",
          
        // Teacher Identifier
        branchID: properties.branch_id,
        companyID: properties.company_id,
        teacherId : null ,
        periodName: null ,
        globalFilter: null,
        userInput : null ,
        grade: null,
        material: null,
        // Set The Video Type
        setVideoType: null,
    
    
        getMaterialsList: [],
        // All Grades
        allGrades: [],
        // Get Data
        data:[],
        link:"",
     
        editableData : null ,
        dataId:[],
        selectedPeriod: null ,
        selectedPeriodId: null ,
        address : null ,
        post : null ,
        type : null ,
        active : null ,
        schoolStageId : null , 
        visible : false ,
        formContent: {
            branchId: properties.branch_id,
            companyId: properties.company_id,
            thumb: "",
        }

    }

     getMaterialByStage = (schoolStagesId) => {
        // Get Materials From API
        apiFetch("/lookup/materials/by-stage", { query: { stageId: schoolStagesId } })
          .then((fetchMaterials) => {
            this.setState({
              getMaterialsList: fetchMaterials,
            });
          })
          .catch((error) => console.error("Error:", error));
      };


    componentDidMount () {
        document.title = properties.site_name + " | إضافة فترة جديدة";
        this.allNews();
        // this.getPeriods();
       // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        };

        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
          .then((grades) => {
            this.setState({ allGrades: grades });
          })
          .catch((error) => console.error("Error:", error));

        // Get Periods From API

    }
    // NOTE: same as links.js -- the legacy "notifications" page already calls
    // the zoomlink endpoints (there's no dedicated legacy notifications
    // resource), so this is ported to the same /zoom-links resource. Worth a
    // product/backend follow-up if notifications are meant to be distinct
    // from links going forward.
    allNews = () => {
        var _this = this ;
       // Send the data to the server
       apiFetch("/zoom-links")
       .then(done => {
        this.setState({data : done});

       })
       .catch(error => {
           console.error('Error:', error)
           _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
       });
   };
    

   // NOT PORTED: unreachable dead code -- the pencil/edit button that would
   // set editableData and open this Dialog is commented out below (only the
   // trash/delete button is live), and the fields posted here (newsType,
   // address, post) are leftover copy-paste from AddNews.js, unrelated to
   // zoom links. Left untouched since there's no live UI path that calls it.
   edit = () => {
    var _this = this ;
   // Send the data to the server
   var data =JSON.parse(window.localStorage.getItem('currentTeacher'));
//    this.setState(
//        {editableData : { ...this.state.editableData , companyId : this.state.companyID}}
//    )
   fetch(properties.api_path + "/newrest/saveNewss",
        {
       method: "POST",
       headers: {
           'Accept': 'application/json',
           'Content-Type': 'application/json; charset=utf-8',
       },
       body: JSON.stringify(
            {  img:this.state.thumb,
               active : this.state.editableData.active ,
               newsType: this.state.editableData.newsType,
               post: this.state.editableData.post , 
               img: this.state.editableData.img.split('/') , 
               address: this.state.editableData.address , 
               schoolStageId : this.state.editableData.schoolStageId  ,
               teacherId : this.state.editableData.teacherId ,
               companyId: this.state.companyID,
               branchId: this.state.branchID,
            }
            )
   })
   .then(res => {return res.json()})
   .then(done => {
      
      if (done.msgType === "success") {
       _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
       this.allNews();
       this.setState({visible :     false});
       } else {
           _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
      }
   })
   .catch(error => {
       console.error('Error:', error) 
       _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
   });
};

     addNews = () => {
        var _this = this ;
       // Send the data to the server
       apiFetch("/zoom-links", {
           method: "POST",
           body: {
               link: this.state.link,
               schoolStageId: this.state.schoolStageId,
               materialId: this.state.material,
               branchId: this.state.branchID,
           },
       })
       .then(() => {
           _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تمت إضافة الاشعار بنجاح'});
           this.allNews();
       })
       .catch(error => {
           console.error('Error:', error)
           _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
       });
   };

    
    
    render() {
        let materialList = this.state.getMaterialsList.map((period) => {
            return { label: period.name, value: period.id };
          });
      
   
      
          const grades = this.state.allGrades.map((grade) => {
            return {
              label: grade.name,
              value: grade.id,
            };
          });
      
        
        const typeList = [
            {label: 'عام', value: 0},
            {label: 'دفعه معينه', value: 1}
        ];

        const active = [
            {label: 'مفعل', value: 1},
            {label: 'غير مفعل', value: 0}
        ];
  

            const onUploadThumb = (e) => {
                let res = JSON.parse(e.xhr.response);
                this.setState({formContent: {
                    ...this.state.formContent,
                    thumb:res.thumb_url.split('/').pop()
                }});

                // console
                this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الصورة المصغرة'});
            };

        var deletePeriod = (e) => {
            var _this = this;
            // Send the data to the server
            apiFetch(`/zoom-links/${e.id}`, { method: "DELETE" })
            .then(() => {
                _this.allNews();
                _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تم الحذف بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)

                _this.setState({
                   buttonText: "إضافة الاشعار",
                   buttonColor: ""
               });

               _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        };

        
        
        const header = (
            <div className="container">
            <Card>
                    <div className="p-grid p-fluid">
                        {/* <div className="p-col-12 p-md-4">
                            <div className="p-inputgroup">
                                <span className="p-float-label">
                                    <InputText id="address" value={this.state.address} name="address" type="text" onChange={(e) => {this.setState({address : e.target.value})}} />
                                    <label htmlFor="address">عنوان الخبر </label>
                                </span>
                            </div>
                        </div>
                        <div className="p-col-12 p-md-20 p-mt-4">
                            <div className="p-inputgroup">
                                <span className="p-float-label">
                                    <InputText id="post" value={this.state.post} name="post" type="text" onChange={(e) => {this.setState({post : e.target.value})}} />
                                    <label htmlFor="post">  الخبر   لا يتعدي 250 حرف    </label>
                                </span>
                            </div>
                        </div>
                        <span className="p-col-12 p-md-2">
                                                <Select value={this.state.type} options={typeList} onChange={(e) => this.setState({type: e.value})} name="type" placeholder="نوع الخبر"/>
                                            </span>
                                            <span className="p-col-12 p-md-2   ">
                                                <Select value={this.state.active} options={active} onChange={(e) => this.setState({active: e.value})} name="type" placeholder="حالة الخبر"/>
                        </span> */}
                        {/* <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Select value={this.state.schoolStageId} options={grades} onChange={(e) => this.setState({schoolStageId: e.value})} name="grade" placeholder="المرحلة الدراسية"/>
                                            </span>
                                        </div>
                                    </div> */}
                                      <div className="p-col-12 p-md-4 p-mt-3">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Select
                          value={this.state.grade}
                          options={grades}
                          onChange={ (e) =>
                            this.setState({
                                schoolStageId: e.value,
                                grade:e.value
                             
                              
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
                  <div className="p-col-12 p-md-4 p-mt-3">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Select
                          value={this.state.material}
                          options={materialList}
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
                        />
                      </span>
                    </div>
                  </div>
                  {/* <div className="p-col-12 p-md-4 p-mt-3">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                      <Select value={this.state.type} options={typeList} onChange={(e) => this.setState({type: e.value})} name="type" placeholder="نوع التعويض"/>

                      </span>
                    </div>
                  </div>
                  
                */}
                        {/* <div className="p-col-12 p-md-12">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Toast ref={this.toast}></Toast>
                                                <FileUpload name="thumb" onUpload={onUploadThumb} invalidFileSizeMessageSummary="حجم الفيديو كبير:" invalidFileSizeMessageDetail="أقصى حجم للصورة هو (100 MB)" url={`${properties.uploads_path}/upload-thumb.php`} customUpload={false} multiple={false} accept="image/*" maxFileSize={1000000000} chooseLabel="إختيار صورة مصغرة" uploadLabel="رفع الصورة" cancelLabel="إلغاء" emptyTemplate={<p className="p-m-0">إسحب الصورة هنا ليبدأ التحميل</p>} />
                                            </span>
                                        </div>
                                         <div>
                            <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${this.state.formContent.thumb.split('/')}`} alt={this.state.formContent.thumb.split('/')} onClick={(e)=>e.target.requestFullscreen()}/>
                            <button type="button" class="btn btn-danger" 
                            // onClick={(e) => deletImage(e,object,i)  }
                            >حذف الصوره</button>

                                        </div>
                                    </div> */}
                                       <div className="p-col-12 p-md-12 p-mt-2">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <InputText
                          id="lesson-name"
                          value={this.state.link}
                          name="lesson-name"
                          type="text"
                          onChange={(e) => {
                            this.setState({
                              
                                link: e.target.value,
                             
                            });
                          }}
                        />

                        <label htmlFor="lesson-name">الاشعار</label>
                      </span>
                    </div>
                  </div>
                        <div className="p-col-12 p-md-12 p-mt-2">
                            <div className="p-inputgroup">
                                <span className="p-float-label">
                                    <Button className={`add-button ${this.state.buttonColor}`} label={this.state.buttonText} onClick={this.addNews}/>
                                </span>
                            </div>
                        </div>
                    </div>
            </Card>
        </div>
        );

    

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    {/* <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={ () =>this.setState({visible: true , editableData : rowData , selectedPeriod : null})} /> */}
                    <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => deletePeriod(rowData)} />
                </React.Fragment>
            );
        }
        const imageTable = (rowData) => {
            return (
                <React.Fragment>
                    {/* <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={ () =>this.setState({visible: true , editableData : rowData , selectedPeriod : null})} /> */}
                    <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${rowData['img']}`} alt={rowData['img']} onClick={(e)=>e.target.requestFullscreen()}/>
                </React.Fragment>
            );
        }

        return (
            <>
                <Header pageName="الاشعارات" />
                <Toast ref={this.toast}></Toast>
                <div className="content">
                    <div className="container">
                        <div className="card">      
                            <Card title="إضافة اشعار جديد">         
                                <DataTable 
                                header={header} sortable 
                                globalFilter={this.state.globalFilter}
                                 className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                                //   value={this.state.data} paginator rows={10} rowsPerPageOptions={[10,20,50]}
                                //    currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"  
                                   emptyMessage="لا يوجد روابط">
                                        {/* <Column  field = "schoolStageName" 
                                    header= " السنه الدراسيه " >
                                    </Column>
                                    <Column  field = "materialName" 
                                    header= " الماده " >
                                    </Column>
                                    <Column 

                                   field='link'
//                                       value =  { <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${this.state.data['img']}`} alt={this.state.formContent.thumb.split('/')} onClick={(e)=>e.target.requestFullscreen()}/>

// }
                                    header= " الرابط " >
                                    </Column>
                                    <Column header="  حذف" 
                                    body={actionBodyTemplate}
                                    ></Column> */}
                                </DataTable>
                            </Card>
                        </div>
                    </div>
                    <Dialog 
                    header="تعديل"
                     visible={this.state.visible}
                      style={{ width: '50vw'}}
                       footer={ 
                       <Button label="تعديل "
                           
                        className="p-button-secondary p-mr-1" onClick={ this.state.editableData!== null ?() => this.edit() : null }></Button>} 
                        onHide={() => this.setState({visible : false})}>
                            <>
                         {
                             this.state.editableData !== null ?
                             <div className="container">
                             <Card>
                                     <div className="p-grid p-fluid">
                                         <div className="p-col-12 p-md-4">
                                             <div className="p-inputgroup">
                                                 <span className="p-float-label">
                                                     <InputText id="cost" value={this.state.editableData["address"]} name="cost" type="text" onChange={(e) =>
                                                         {this.setState({editableData :{...this.state.editableData , address : e.target.value}})} }/>
                                                     <label htmlFor="cost">عنوان الخبر </label>
                                                 </span>
                                             </div>
                                         </div>
                                         <div className="p-col-12 p-md-20 p-mt-4">
                                             <div className="p-inputgroup">
                                                 <span className="p-float-label">
                                                     <InputText id="cost" value={this.state.editableData["post"]} name="post" type="text" onChange={(e) => {this.setState({editableData :{...this.state.editableData , post : e.target.value}})}} />
                                                     <label htmlFor="cost"> الخبر </label>
                                                 </span>
                                             </div>
                                         </div>
                                         <span className="p-col-12 p-md-4">
                                                                 <Select value={this.state.editableData["newsType"]} options={typeList} onChange={(e) =>  this.setState({editableData :{...this.state.editableData , newsType : e.value}})} name="type" placeholder="نوع الخبر"/>
                                                             </span>
                                                             <span className="p-col-12 p-md-4   ">
                                                                 <Select value={this.state.editableData["active"]} options={active} onChange={(e) =>  this.setState({editableData :{...this.state.editableData , active : e.value}})} name="type" placeholder="حالة الخبر"/>
                                         </span>
                                         <div className="p-col-12 p-md-4">
                                                         <div className="p-inputgroup">
                                                             <span className="p-float-label">
                                                                 <Select value={this.state.editableData["schoolStageId"]} options={grades} onChange={(e) =>  this.setState({editableData :{...this.state.editableData , schoolStageId : e.value}})} name="grade" placeholder="المرحلة الدراسية"/>
                                                             </span>
                                                         </div>
                                                     </div>
                                         <div className="p-col-12 p-md-12">
                                                         <div className="p-inputgroup">
                                                             <span className="p-float-label">
                                                                 <Toast ref={this.toast}></Toast>
                                                                 <FileUpload name="thumb" onUpload={(e) => {
                                                                     let res = JSON.parse(e.xhr.response);
                                                                     this.setState({editableData: {
                                                                         ...this.state.editableData,
                                                                         img: res.thumb_url
                                                                     }});
                                                                     this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الصورة المصغرة'});
                                                                 }
                                                                 } invalidFileSizeMessageSummary="حجم الفيديو كبير:" invalidFileSizeMessageDetail="أقصى حجم للصورة هو (100 MB)" url={`${properties.uploads_path}/upload-thumb.php`} customUpload={false} multiple={false} accept="image/*" maxFileSize={1000000000} chooseLabel="إختيار صورة مصغرة" uploadLabel="رفع الصورة" cancelLabel="إلغاء" emptyTemplate={<p className="p-m-0">إسحب الصورة هنا ليبدأ التحميل</p>} />
                                                             </span>
                                                         </div>
                                        <div>
                            <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${this.state.editableData["img"]}`} alt={this.state.editableData["img"]} onClick={(e)=>e.target.requestFullscreen()}/>
                            <button type="button" class="btn btn-danger" 
                            onClick={()=>{ 
                                this.setState({editableData :{...this.state.editableData , img : ""},formContent: {
                                    ...this.state.formContent,
                                    thumb: ""
                                }})}
                                  }
                            >حذف الصوره</button>

                                        </div>           
                              
                                                     </div>
                                         <div className="p-col-12 p-md-12 p-mt-2">
                                             <div className="p-inputgroup">
                
                                             </div>
                                         </div>
                                     </div>
                             </Card>
                         </div>
                          : null
                                    
                                    }
                                   
                                             
                       
                                    </>  
                        
                    </Dialog>
                </div>
            </>
        );
    }
}

export default Notifications;