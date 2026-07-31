import React, {Component, createRef} from 'react';

// CSS


// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';

// Components
import Header from '../../components/Header/Header';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import {Dialog} from 'primereact/dialog' ;
import { FileUpload } from 'primereact/fileupload';

import { InputTextarea } from 'primereact/inputtextarea';

// Properties
const properties = require('../../properties.json');

class AddNews extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        // Submit Button
        buttonText: "إضافة خبر",
        buttonColor: "",
          
        // Teacher Identifier
        branchID: properties.branch_id,
        companyID: properties.company_id,
        teacherId : null ,
        periodName: null ,
        globalFilter: null,
        userInput : null ,
        // Get Data
        data:[],
        allGrades : [],
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

    getTeacherId =async()  => {
        var data =await JSON.parse(window.localStorage.getItem('currentTeacher'));
        this.setState({teacherId : data["teacherId"]});
        console.log({teacher : this.state.teacherId});
        
     };
     

    componentDidMount () {
        document.title = properties.site_name + " | إضافة فترة جديدة";
        this.getTeacherId();
        this.allNews();
        // this.getPeriods();
       // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        };

        fetch(properties.api_path + "/lockup/levelList", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({branchId: this.state.branchID})
        })
        .then(res => {return res.json()})
        .then(grades => {
            this.setState({allGrades: grades});
        })

        // Get Periods From API
        
    }
    allNews = () => {
        var _this = this ;
       // Send the data to the server
       var data =JSON.parse(window.localStorage.getItem('currentTeacher'));
       fetch(properties.api_path + "/newrest/allNews",
        {
           method: "POST",
           headers: {
               'Accept': 'application/json',
               'Content-Type': 'application/json; charset=utf-8',
           },
           body: JSON.stringify({
               teacherId : data["teacherId"] ,
               companyId: this.state.companyID,
               branchId: this.state.branchID,
           })
       })
       .then(res => {return res.json()})
       .then(done => {
        this.setState({data : done});
        
       })
       .catch(error => {
           console.error('Error:', error) 
           _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
       });
   };
    

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
            {  
               id : this.state.editableData.id ,
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
       var data =JSON.parse(window.localStorage.getItem('currentTeacher'));
       fetch(properties.api_path + "/newrest/saveNewss",
        {
           method: "POST",
           headers: {
               'Accept': 'application/json',
               'Content-Type': 'application/json; charset=utf-8',
           },
           body: JSON.stringify({
               active : this.state.active ,
               newsType:    this.state.type,
               post:    this.state.post , 
               img:     this.state.formContent.thumb.split('/') , 
               address: this.state.address , 
               schoolStageId : this.state.schoolStageId  ,
               teacherId : data["teacherId"] ,
               companyId: this.state.companyID,
               branchId: this.state.branchID,
           })
       })
       .then(res => {return res.json()})
       .then(done => {
          
          if (done.msgType === "success") {
           _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
           // this.getPeriods();
           this.allNews();
           } else {
               _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
          }
       })
       .catch(error => {
           console.error('Error:', error) 
           _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
       });
   };

    
    
    render() {
        
        const typeList = [
            {label: 'عام', value: 0},
            {label: 'دفعه معينه', value: 1}
        ];

        const active = [
            {label: 'مفعل', value: 1},
            {label: 'غير مفعل', value: 0}
        ];
        const grades = this.state.allGrades.map(grade => {
            return {
                label: grade.name,
                value: grade.id
            }
        });


            const onUploadThumb = (e) => {
                let res = JSON.parse(e.xhr.response);
                this.setState({formContent: {
                    ...this.state.formContent,
                    thumb: res.thumb_url.split('/').pop()
                }});
                this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الصورة المصغرة'});
            };

        var deletePeriod = (e) => {
            var _this = this;
            // Send the data to the server
            fetch(properties.api_path + "/newrest/deleteNews", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json; charset=utf-8',
                },
                body: JSON.stringify({
                    id: e.id , 
                })
            })
            .then(res => {return res.json()})
            .then(done => {
               if (done.msgType === "success") {
                    _this.allNews();
                   _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
                } else {
                   _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
               }
            })
            .catch(error => {
                console.error('Error:', error)

                _this.setState({
                   buttonText: "إضافة الخبر",
                   buttonColor: ""
               });

               _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
        };

        
        
        const header = (
            <div className="container">
            <Card>
                    <div className="p-grid p-fluid">
                        <div className="p-col-12 p-md-4">
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
                                                <Dropdown value={this.state.type} options={typeList} onChange={(e) => this.setState({type: e.value})} name="type" placeholder="نوع الخبر"/>
                                            </span>
                                            <span className="p-col-12 p-md-2   ">
                                                <Dropdown value={this.state.active} options={active} onChange={(e) => this.setState({active: e.value})} name="type" placeholder="حالة الخبر"/>
                        </span>
                        <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.schoolStageId} options={grades} onChange={(e) => this.setState({schoolStageId: e.value})} name="grade" placeholder="المرحلة الدراسية"/>
                                            </span>
                                        </div>
                                    </div>
                        <div className="p-col-12 p-md-12">
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
                    <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={ () =>this.setState({visible: true , editableData : rowData , selectedPeriod : null})} />
                    <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => deletePeriod(rowData)} />
                </React.Fragment>
            );
        }

        const edit =(id) => {
            var _this = this ;
            // Send the data to the server
            fetch(properties.api_path + "/periods/savePeriods", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json; charset=utf-8',
                },
                body: JSON.stringify({
                    id: id,
                    branchId : this.state.branchID ,
                    companyId: this.state.companyID,
                    name: this.state.userInput , 
                    teacherId: this.state.teacherId , 
                    parent: this.state.selectedPeriod
                })
            })
            .then(res => {return res.json()})
            .then(done => {
               
               if (done.msgType === "success") {
                _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
                this.getPeriods();
                this.setState({visible : false});
                } else {
                    _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
               }
            })
            .catch(error => {
                console.error('Error:', error) 
                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
        }

        return (
            <>
                <Header pageName="الاخبار" />
                <Toast ref={this.toast}></Toast>
                <div className="content">
                    <div className="container">
                        <div className="card">      
                            <Card title="إضافة خبر جديد">         
                                <DataTable 
                                header={header} sortable 
                                globalFilter={this.state.globalFilter}
                                 className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                                  value={this.state.data} paginator rows={10} rowsPerPageOptions={[10,20,50]}
                                   currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"  
                                   emptyMessage="لا يوجد أخبار">
                                    <Column  field = "address" 
                                    header= "عنوان الخبر " >
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
                                                                 <Dropdown value={this.state.editableData["newsType"]} options={typeList} onChange={(e) =>  this.setState({editableData :{...this.state.editableData , newsType : e.value}})} name="type" placeholder="نوع الخبر"/>
                                                             </span>
                                                             <span className="p-col-12 p-md-4   ">
                                                                 <Dropdown value={this.state.editableData["active"]} options={active} onChange={(e) =>  this.setState({editableData :{...this.state.editableData , active : e.value}})} name="type" placeholder="حالة الخبر"/>
                                         </span>
                                         <div className="p-col-12 p-md-4">
                                                         <div className="p-inputgroup">
                                                             <span className="p-float-label">
                                                                 <Dropdown value={this.state.editableData["schoolStageId"]} options={grades} onChange={(e) =>  this.setState({editableData :{...this.state.editableData , schoolStageId : e.value}})} name="grade" placeholder="المرحلة الدراسية"/>
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

export default AddNews;