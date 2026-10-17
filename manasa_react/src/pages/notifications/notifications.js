import React, {Component, createRef} from 'react';

// CSS


// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import TableShell from '../../components/Table/TableShell';
import TableEmpty from '../../components/Table/TableEmpty';
import Modal from '../../components/Modal/Modal';
import Confirm from '../../components/Modal/Confirm';
import '../../components/Field/Form.scss';
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
        },

        // whether the add modal is open. `visible` below is the edit
        // dialog's own flag and is left alone -- nothing opens it.
        addOpen: false,

        // True from the start: the list is fetched on mount, so there is a
        // wait to report before the first rows arrive. Only TableEmpty reads
        // it, so the waiting row appears where the answer will.
        tableLoading: true,

        // one flag per lookup list, raised inside its own fetch
        gradesLoading: false,
        materialsLoading: false,

        // one flag per action -- never a shared one, or a slow save in the
        // modal greys out the delete button in the table
        adding: false,
        deleting: false,

        // the row waiting on a yes
        confirmRow: null

    }

     getMaterialByStage = (schoolStagesId) => {
        // inside the fetch, so opening the list reports the wait too
        this.setState({ materialsLoading: true });

        // Get Materials From API
        apiFetch("/lookup/materials/by-stage", { query: { stageId: schoolStagesId } })
          .then((fetchMaterials) => {
            this.setState({
              getMaterialsList: fetchMaterials,
              materialsLoading: false,
            });
          })
          .catch((error) => {
            this.setState({ materialsLoading: false });
            console.error("Error:", error);
          });
      };

     // Lifted out of componentDidMount unchanged, so the stage list can be
     // asked for again when its dropdown is opened rather than staying the
     // snapshot it was at mount.
     getGrades = () => {
        this.setState({ gradesLoading: true });

        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
          .then((grades) => {
            this.setState({ allGrades: grades, gradesLoading: false });
          })
          .catch((error) => {
            this.setState({ gradesLoading: false });
            console.error("Error:", error);
          });
      };


    componentDidMount () {
        document.title = properties.site_name + " | الاشعارات";
        this.allNews();
        // this.getPeriods();
       // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        };

        this.getGrades();

        // Get Periods From API

    }
    // NOTE: same as links.js -- the legacy "notifications" page already calls
    // the zoomlink endpoints (there's no dedicated legacy notifications
    // resource), so this is ported to the same /zoom-links resource. Worth a
    // product/backend follow-up if notifications are meant to be distinct
    // from links going forward.
    allNews = () => {
        var _this = this ;

       // Every call announces itself, not just the first: this runs again
       // after an add and after a delete, and a silent refetch reads as
       // though the action never registered. The rows go with it so the
       // waiting row has somewhere to appear.
       this.setState({ tableLoading: true, data: [] });

       // Send the data to the server
       apiFetch("/zoom-links")
       .then(done => {
        this.setState({data : done, tableLoading: false});

       })
       .catch(error => {
           // the wait ends on failure too, or the table waits for ever
           this.setState({ tableLoading: false });
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

        this.setState({ adding: true });

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
           this.setState({ adding: false, addOpen: false });
       })
       .catch(error => {
           // left open, with what was typed still in it
           this.setState({ adding: false });
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

            this.setState({ deleting: true });

            // Send the data to the server
            apiFetch(`/zoom-links/${e.id}`, { method: "DELETE" })
            .then(() => {
                _this.setState({ deleting: false, confirmRow: null });
                _this.allNews();
                _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تم الحذف بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)

                // the question stays open on failure, so it can be retried
                _this.setState({
                   buttonText: "إضافة الاشعار",
                   buttonColor: "",
                   deleting: false
               });

               _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        };

        
        
        // The toolbar is a toolbar again. What used to be passed as the
        // table's `header` was the entire add form -- a Card, a grid and a
        // submit button -- rendered inside the table's own toolbar slot.
        const header = (
            <div className="table-header">
                {/* no heading here: the page header above carries it */}
                <span className="p-input-icon-left m-tool-search">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الاشعارات" />
                </span>
            </div>
        );

        // Three fields and one decision, so a modal. The commented-out
        // fields that used to sit here -- address, post, type, active and an
        // image upload, all copy-paste from AddNews -- are in the history;
        // they were never rendered.
        const notificationsForm = (
            <div className="m-form">
                <section className="m-sec">
                    <h3>
                        <span className="m-sec-n">١</span>
                        الاشعار
                        <small>اختر المرحلة أولاً، ثم مادتها</small>
                    </h3>

                    <div className="m-grid">
                        <div className="m-fld">
                            <label htmlFor="notif-grade">المرحلة الدراسية</label>
                            <Select
                              inputId="notif-grade"
                              value={this.state.grade}
                              options={grades}
                              onChange={ (e) =>
                                this.setState({
                                    schoolStageId: e.value,
                                    grade:e.value
                                })
                              }
                              name="grade"
                              placeholder="المرحلة الدراسية"
                              optionLabel="label"
                              loading={this.state.gradesLoading}
                              onOpen={() => this.getGrades()}
                            />
                        </div>

                        <div className="m-fld">
                            <label htmlFor="notif-subject">المادة</label>
                            <Select
                              inputId="notif-subject"
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
                              optionLabel="label"
                              loading={this.state.materialsLoading}
                              needs={this.state.grade}
                              needsLabel="اختر المرحلة الدراسية أولاً"
                              onOpen={() => this.getMaterialByStage(this.state.grade)}
                            />
                        </div>

                        <div className="m-fld m-wide">
                            <label htmlFor="lesson-name">الاشعار</label>
                            <InputText
                              id="lesson-name"
                              value={this.state.link}
                              name="lesson-name"
                              type="text"
                              placeholder="اكتب نص الاشعار"
                              onChange={(e) => {
                                this.setState({

                                    link: e.target.value,

                                });
                              }}
                            />
                        </div>
                    </div>
                </section>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    {/* <Button icon="pi pi-pencil" className="p-button-rounded p-button-secondary p-ml-2" onClick={ () =>this.setState({visible: true , editableData : rowData , selectedPeriod : null})} /> */}
                    <Button icon="pi pi-trash" title="حذف الاشعار" className="p-button-rounded p-button-danger" onClick={() => this.setState({ confirmRow: rowData })} />
                </React.Fragment>
            );
        }
        // The field is free text here -- a notification is a sentence, not a
        // URL. It is still stored in `link`, because this page posts to the
        // same resource /links/add does. When a teacher has pasted a link
        // into it anyway, it opens; otherwise it reads as the text it is.
        const linkTable = (rowData) => {
            const href = rowData['link'];
            const isUrl = typeof href === 'string' && /^https?:\/\//i.test(href);

            return isUrl ? (
                <a className="m-row-link" href={href} target="_blank" rel="noreferrer" title={href}>
                    <i className="pi pi-external-link"></i>
                    <span>{href}</span>
                </a>
            ) : (
                <span title={href}>{href}</span>
            );
        }

        return (
            <>
                <Header
                    pageName="الاشعارات"
                    action={{ label: "إضافة اشعار", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
                />

                <Toast ref={this.toast}></Toast>

                <Modal
                    visible={this.state.addOpen}
                    onHide={() => this.setState({ addOpen: false })}
                    title="إضافة اشعار جديد"
                    subtitle="اختر المرحلة والمادة، ثم اكتب الاشعار"
                    busy={this.state.adding}
                    width={620}
                    actions={
                        <Button
                            className={this.state.buttonColor}
                            icon={this.state.adding ? "pi pi-spin pi-spinner" : undefined}
                            label={this.state.adding ? "جارٍ الحفظ…" : this.state.buttonText}
                            disabled={this.state.adding}
                            onClick={this.addNews}
                        />
                    }
                >
                    {notificationsForm}
                </Modal>

                {/* the question the delete button never used to ask */}
                <Confirm
                    visible={this.state.confirmRow !== null}
                    onHide={() => this.setState({ confirmRow: null })}
                    onConfirm={() => deletePeriod(this.state.confirmRow)}
                    busy={this.state.deleting}
                    title="حذف اشعار"
                    message="هل أنت متأكد من حذف هذا الاشعار؟"
                    note="لن يصل الطلاب إليه بعد الآن. لا يمكن التراجع عن هذه الخطوة."
                    confirmLabel="نعم، احذف"
                    busyLabel="جارٍ الحذف…"
                />
                <div className="content">
                    <div className="container">
                        <div className="card">      
                            {/* no title: the page header above already says it,
                                and what it said -- "إضافة اشعار جديد" -- was the
                                title of the form, not of this list */}
                            <Card>
                                {/* presentation only -- see components/Table/TableShell.js */}
                                <TableShell>
                                {/* The list was switched off in the source: no
                                    `value`, and every Column commented out, so
                                    this table showed an empty row reading "no
                                    links" on a notifications page. Both are
                                    restored. */}
                                <DataTable
                                header={header} sortable
                                globalFilter={this.state.globalFilter}
                                 className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                                  value={this.state.data} paginator
                                   paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                                   rows={10} rowsPerPageOptions={[10,20,50]}
                                   currentPageReportTemplate="عرض {first} – {last} من {totalRecords} اشعار"
                                   emptyMessage={
                                     <TableEmpty
                                       loading={this.state.tableLoading}
                                       icon="pi pi-bell"
                                       title="لا يوجد اشعارات"
                                       hint="أضف أول اشعار، وسيصل إلى طلابك في التطبيق"
                                       action={{ label: "إضافة اشعار", onClick: () => this.setState({ addOpen: true }) }}
                                     />
                                   }>
                                    <Column sortable field = "schoolStageName"
                                    header= " السنه الدراسيه " >
                                    </Column>
                                    <Column sortable field = "materialName"
                                    header= " الماده " >
                                    </Column>
                                    <Column
                                   field='link'
                                   body={linkTable}
                                    header= " الاشعار " >
                                    </Column>
                                    <Column header="الإجراءات"
                                    headerStyle={{ width: '84px' }} bodyStyle={{ width: '84px' }}
                                    body={actionBodyTemplate}
                                    ></Column>
                                </DataTable>
                                </TableShell>
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