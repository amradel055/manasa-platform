import React, {Component, createRef} from 'react';

// CSS
import '../../components/Field/Form.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import TableShell from '../../components/Table/TableShell';
import TableEmpty from '../../components/Table/TableEmpty';
import SlidePanel from '../../components/Panel/SlidePanel';
import Confirm from '../../components/Modal/Confirm';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import Select from '../../components/Field/Select';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
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
        },

        // whether the add panel is open. The edit panel is the existing
        // `visible` flag, which already governed the edit dialog.
        addOpen: false,

        // True from the start: the list is fetched on mount, so there is a
        // wait to report before the first rows arrive. Only TableEmpty reads
        // it, so the waiting row appears where the answer will.
        tableLoading: true,

        // one flag per lookup list, raised inside its own fetch
        gradesLoading: false,

        // one flag per action -- never a shared one, or a slow save in the
        // panel greys out the delete button in the table
        adding: false,
        editing: false,
        deleting: false,

        // the row waiting on a yes
        confirmRow: null
    }

    componentDidMount () {
        document.title = properties.site_name + " | الأخبار";
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

    // Lifted out of componentDidMount unchanged so the stage list can be
    // asked for again when its dropdown is opened. It is still fetched on
    // mount as well, because the table's stage column needs it before
    // anything is opened.
    getGrades = () => {
        this.setState({ gradesLoading: true });

        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
        .then(grades => {
            this.setState({allGrades: grades, gradesLoading: false});
        })
        .catch(error => {
            this.setState({ gradesLoading: false });
            console.error('Error:', error);
        });
    };

    allNews = () => {
        var _this = this ;

       // Every call announces itself, not just the first: this runs again
       // after an add, an edit and a delete, and a silent refetch reads as
       // though the action never registered. The rows go with it so the
       // waiting row has somewhere to appear.
       this.setState({ tableLoading: true, data: [] });

       // Send the data to the server
       apiFetch("/news", { query: { teacherUserId: getCurrentUser()?.userId } })
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


   edit = () => {
    var _this = this ;

    this.setState({ editing: true });

   // Send the data to the server
   apiFetch(`/news/${this.state.editableData.id}`, {
       method: "PUT",
       body: {
           type: this.state.editableData.newsType,
           post: this.state.editableData.post,
           img: this.state.editableData.img,
           address: this.state.editableData.address,
           schoolStageId: this.state.editableData.schoolStageId,
           branchId: this.state.branchID,
       },
   })
   .then(() => {
       _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تم التعديل بنجاح'});
       this.allNews();
       this.setState({visible: false, editing: false});
   })
   .catch(error => {
       // the panel stays open with what was typed still in it, so it can be
       // sent again without retyping
       this.setState({ editing: false });
       console.error('Error:', error)
       _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
   });
};

     addNews = () => {
        var _this = this ;

        this.setState({ adding: true });

       // Send the data to the server
       apiFetch("/news", {
           method: "POST",
           body: {
               type: this.state.type,
               post: this.state.post,
               img: this.state.formContent.thumb,
               address: this.state.address,
               schoolStageId: this.state.schoolStageId,
               branchId: this.state.branchID,
           },
       })
       .then(() => {
           _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تمت إضافة الخبر بنجاح'});
           this.allNews();
           this.setState({ adding: false, addOpen: false });
       })
       .catch(error => {
           // left open, with the typing still in it
           this.setState({ adding: false });
           console.error('Error:', error)
           _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
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

        // for the table: a row carries the stage's id, not its name
        const gradeNameById = {};
        this.state.allGrades.forEach(g => { gradeNameById[g.id] = g.name; });

        const labelOf = (list, value) => {
            const hit = list.filter(o => o.value === value)[0];
            return hit ? hit.label : "";
        };


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

            this.setState({ deleting: true });

            // Send the data to the server
            apiFetch(`/news/${e.id}`, { method: "DELETE" })
            .then(() => {
                _this.setState({ deleting: false, confirmRow: null });
                _this.allNews();
                _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تم الحذف بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)

                // the question stays open on failure, so it can be retried
                _this.setState({
                   buttonText: "إضافة الخبر",
                   buttonColor: "",
                   deleting: false
               });

               _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        };

        // The add form and the edit form ask for the same six things. They
        // write to different places -- the add form to the page's own fields,
        // the edit form into `editableData` -- so every handler is passed in
        // by its caller and stays exactly what it was. Only the markup is
        // shared.
        const newsFields = ({
            idPrefix,
            address, onAddress,
            post, onPost,
            type, onType,
            activeValue, onActive,
            stage, onStage,
            thumb, thumbSrc, onUpload, removeImage,
        }) => (
            <>
                <section className="m-sec">
                    <h3>
                        <span className="m-sec-n">١</span>
                        نص الخبر
                        <small>ما سيقرأه الطالب في التطبيق</small>
                    </h3>

                    <div className="m-grid">
                        <div className="m-fld m-wide">
                            <label htmlFor={`${idPrefix}-address`}>عنوان الخبر</label>
                            <InputText id={`${idPrefix}-address`} value={address} name="address" type="text" placeholder="مثال: إجازة نصف العام" onChange={onAddress} />
                        </div>

                        <div className="m-fld m-wide">
                            <label htmlFor={`${idPrefix}-post`}>الخبر</label>
                            <InputText id={`${idPrefix}-post`} value={post} name="post" type="text" placeholder="اكتب الخبر هنا" onChange={onPost} />
                            <small className="m-note">لا يتعدى ٢٥٠ حرفاً</small>
                        </div>
                    </div>
                </section>

                <section className="m-sec">
                    <h3>
                        <span className="m-sec-n">٢</span>
                        من يراه
                        <small>نوع الخبر وحالته والمرحلة الموجّه إليها</small>
                    </h3>

                    <div className="m-grid">
                        <div className="m-fld">
                            <label htmlFor={`${idPrefix}-type`}>نوع الخبر</label>
                            <Select inputId={`${idPrefix}-type`} value={type} options={typeList} onChange={onType} name="type" placeholder="نوع الخبر" optionLabel="label" />
                        </div>

                        <div className="m-fld">
                            <label htmlFor={`${idPrefix}-active`}>حالة الخبر</label>
                            <Select inputId={`${idPrefix}-active`} value={activeValue} options={active} onChange={onActive} name="type" placeholder="حالة الخبر" optionLabel="label" />
                        </div>

                        <div className="m-fld">
                            <label htmlFor={`${idPrefix}-grade`}>المرحلة الدراسية</label>
                            <Select
                                inputId={`${idPrefix}-grade`}
                                value={stage}
                                options={grades}
                                onChange={onStage}
                                name="grade"
                                placeholder="المرحلة الدراسية"
                                optionLabel="label"
                                loading={this.state.gradesLoading}
                                onOpen={() => this.getGrades()}
                            />
                        </div>
                    </div>
                </section>

                <section className="m-sec">
                    <h3>
                        <span className="m-sec-n">٣</span>
                        صورة الخبر
                        <small>اختياري</small>
                    </h3>

                    <div className="m-grid">
                        <div className="m-drop">
                            <FileUpload name="thumb" onUpload={onUpload} invalidFileSizeMessageSummary="حجم الفيديو كبير:" invalidFileSizeMessageDetail="أقصى حجم للصورة هو (100 MB)" url={`${properties.uploads_path}/upload-thumb.php`} customUpload={false} multiple={false} accept="image/*" maxFileSize={1000000000} chooseLabel="إختيار صورة مصغرة" uploadLabel="رفع الصورة" cancelLabel="إلغاء" emptyTemplate={<p className="p-m-0">إسحب الصورة هنا ليبدأ التحميل</p>} />
                        </div>

                        {/* Only once there is something to preview. It used to
                            render whatever happened -- on an empty form that
                            was a broken-image icon and a button beside it. */}
                        {thumb ? (
                            <div className="m-files">
                                <img className="preview-thumb image" src={thumbSrc} alt={thumb} onClick={(e)=>e.target.requestFullscreen()}/>
                                <button type="button" className="m-thumb-x" onClick={removeImage}>حذف الصوره</button>
                            </div>
                        ) : null}
                    </div>
                </section>
            </>
        );



        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-pencil" title="تعديل الخبر" className="p-button-rounded p-button-secondary p-ml-2" onClick={ () =>this.setState({visible: true , editableData : rowData , selectedPeriod : null})} />
                    <Button icon="pi pi-trash" title="حذف الخبر" className="p-button-rounded p-button-danger" onClick={() => this.setState({ confirmRow: rowData })} />
                </React.Fragment>
            );
        }

        const header = (
            <div className="table-header">
                {/* no heading here: the page header above carries it */}
                <span className="p-input-icon-left m-tool-search">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الأخبار" />
                </span>
            </div>
        );

        return (
            <>
                <Header
                    pageName="الاخبار"
                    action={{ label: "إضافة خبر", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
                />

                {/* One Toast for the page. There used to be three, all on the
                    same ref, so only the last one mounted could ever be the
                    one that spoke -- and that one lived inside the edit
                    dialog, which is closed most of the time. */}
                <Toast ref={this.toast}></Toast>

                {/* Six fields and an upload: long enough to earn a slide-over
                    rather than a modal. */}
                <SlidePanel
                    visible={this.state.addOpen}
                    onHide={() => this.setState({ addOpen: false })}
                    title="إضافة خبر جديد"
                    subtitle="اكتب الخبر واختر من يراه، ثم احفظه"
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
                    <div className="m-form">
                        {newsFields({
                            idPrefix: "add-news",
                            address: this.state.address,
                            onAddress: (e) => {this.setState({address : e.target.value})},
                            post: this.state.post,
                            onPost: (e) => {this.setState({post : e.target.value})},
                            type: this.state.type,
                            onType: (e) => this.setState({type: e.value}),
                            activeValue: this.state.active,
                            onActive: (e) => this.setState({active: e.value}),
                            stage: this.state.schoolStageId,
                            onStage: (e) => this.setState({schoolStageId: e.value}),
                            thumb: this.state.formContent.thumb,
                            thumbSrc: `${properties.uploads_path}/thumb/${this.state.formContent.thumb.split('/')}`,
                            onUpload: onUploadThumb,
                            // unchanged: this button has never had a handler on
                            // the add form, only on the edit form below
                            removeImage: undefined,
                        })}
                    </div>
                </SlidePanel>

                {/* the same six fields, writing into the row being edited */}
                <SlidePanel
                    visible={this.state.visible}
                    onHide={() => this.setState({visible : false})}
                    title="تعديل الخبر"
                    subtitle="عدّل ما تريد، ثم احفظ التغييرات"
                    actions={
                        <Button
                            icon={this.state.editing ? "pi pi-spin pi-spinner" : undefined}
                            label={this.state.editing ? "جارٍ الحفظ…" : "تعديل"}
                            disabled={this.state.editing}
                            onClick={ this.state.editableData!== null ?() => this.edit() : null }
                        />
                    }
                >
                    {this.state.editableData !== null ? (
                        <div className="m-form">
                            {newsFields({
                                idPrefix: "edit-news",
                                address: this.state.editableData["address"],
                                onAddress: (e) => {this.setState({editableData :{...this.state.editableData , address : e.target.value}})},
                                post: this.state.editableData["post"],
                                onPost: (e) => {this.setState({editableData :{...this.state.editableData , post : e.target.value}})},
                                type: this.state.editableData["newsType"],
                                onType: (e) => this.setState({editableData :{...this.state.editableData , newsType : e.value}}),
                                activeValue: this.state.editableData["active"],
                                onActive: (e) => this.setState({editableData :{...this.state.editableData , active : e.value}}),
                                stage: this.state.editableData["schoolStageId"],
                                onStage: (e) => this.setState({editableData :{...this.state.editableData , schoolStageId : e.value}}),
                                thumb: this.state.editableData["img"],
                                thumbSrc: `${properties.uploads_path}/thumb/${this.state.editableData["img"]}`,
                                onUpload: (e) => {
                                    let res = JSON.parse(e.xhr.response);
                                    this.setState({editableData: {
                                        ...this.state.editableData,
                                        img: res.thumb_url
                                    }});
                                    this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الصورة المصغرة'});
                                },
                                removeImage: ()=>{
                                    this.setState({editableData :{...this.state.editableData , img : ""},formContent: {
                                        ...this.state.formContent,
                                        thumb: ""
                                    }})},
                            })}
                        </div>
                    ) : null}
                </SlidePanel>

                {/* the question the delete button never used to ask */}
                <Confirm
                    visible={this.state.confirmRow !== null}
                    onHide={() => this.setState({ confirmRow: null })}
                    onConfirm={() => deletePeriod(this.state.confirmRow)}
                    busy={this.state.deleting}
                    title="حذف خبر"
                    message="هل أنت متأكد من حذف هذا الخبر؟"
                    note="لن يظهر بعدها لأي طالب. لا يمكن التراجع عن هذه الخطوة."
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
                                   currentPageReportTemplate="عرض {first} – {last} من {totalRecords} خبر"
                                   emptyMessage={
                                     <TableEmpty
                                       loading={this.state.tableLoading}
                                       icon="pi pi-bell"
                                       title="لا يوجد أخبار"
                                       hint="أضف أول خبر، وسيظهر هنا ولطلابك في التطبيق"
                                       action={{ label: "إضافة خبر", onClick: () => this.setState({ addOpen: true }) }}
                                     />
                                   }>
                                    <Column sortable field = "address"
                                    header= "عنوان الخبر " >
                                    </Column>
                                    {/* the row carries ids; these say what they mean */}
                                    <Column sortable field="newsType" body={(row) => labelOf(typeList, row.newsType)} header="نوع الخبر"></Column>
                                    <Column sortable field="schoolStageId" body={(row) => gradeNameById[row.schoolStageId] || ""} header="المرحلة الدراسية"></Column>
                                    <Column sortable field="active" body={(row) => labelOf(active, row.active)} header="الحالة"></Column>
                                    <Column header="الإجراءات"
                                    headerStyle={{ width: '124px' }} bodyStyle={{ width: '124px' }}
                                    body={actionBodyTemplate}
                                    ></Column>
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

export default AddNews;
