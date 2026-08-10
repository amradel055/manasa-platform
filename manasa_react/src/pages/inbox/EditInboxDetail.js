/* eslint-disable no-loop-func */
import React, {Component, createRef} from 'react';

// CSS
import './Exams.scss';

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
import { RadioButton } from 'primereact/radiobutton';
import AddExam from '../Exams/AddExam';
import { FileUpload } from 'primereact/fileupload';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';

// Properties
const properties = require('../../properties.json');

class EditInboxDetail extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        // Submit Button
        buttonText: "حفظ الرد",
        buttonColor: "",

        // Teacher Identifier
        branchID: properties.branch_id,
        companyID: properties.company_id,

        // Exam ID
        examID: (this.props.match.params.id) ? parseInt(this.props.match.params.id) : 0,
        
        // Exam
        questions: [
            {
                id: 1,
                qID: null,
                question: null,
                type: null,
                score: 0,
                answers: [
                    {
                        answerID: 1,
                        id: null,
                        text: null,
                        note: null,
                        correct: null,
                        parent: 1
                    }
                ]
            }
        ],
        numQuestions: 1,
        numAnswers: 1,

        // Get Data
        getSchoolStages: [],
        getLessons: [],
        getPeriods: [],
        getMaterialsList: [],
        // Get Current Answers
        getAnswers: 1,

        // Inputs Data
        selectedLesson: null,
        selectedStage: null,
        selectedStatus: null,
        selectedMonth: null,
        selectedType: null,
        selectedMaterial:null,
        selectedPeriod:null,
        subject:null,
        body:null,
        materialName:null,
        // sendDate:null,
        image:null,
        sendDate:null,
        answer:null,
        imageAnswer:null,
        inboxReplies:[],

        youtubeLink: null,
        vimeoLink: null,
        voiceLink: null,
        lessonVoiceList:[],
        setVideoType: null,

        examName: "",
        timer:0
    }
    // getMaterialByStage(stageId){
    //     var data =JSON.parse(window.localStorage.getItem('currentTeacher'));
    //     fetch(properties.api_path + "/lockup/materialListbystage", {
    //         method: "POST",
    //         headers: {
    //             'Accept': 'application/json',
    //             'Content-Type': 'application/json',
    //         },
    //         body: JSON.stringify({
    //             id: stageId
    //         })
    //     })
    //     .then(res => {return res.json()})
    //     .then(grades => {
    //         console.log(grades);
    //         this.setState({getMaterialsList : grades});
    //     })
    //  }
    //  getPeriod(materialId) {
    //     fetch(properties.api_path + "/periods/findPeriod", {
    //       method: "POST",
    //       headers: {
    //         Accept: "application/json",
    //         "Content-Type": "application/json",
    //       },
    //       body: JSON.stringify({
          
    //         materialId: materialId
    //       }),
    //     })
    //       .then((res) => {
    //         return res.json();
    //       })
    //       .then((grades) => {
    //         console.log(grades);
    //         this.setState({ getPeriods: grades });
    //       });
    //   }
    
    //   getLessonsByStage = (schoolStagesId,materialId , periodId) => {
    //     // Get Lessons From API
    //     var _data = JSON.parse(window.localStorage.getItem("currentTeacher"));
    //     fetch(properties.api_path + "/lesson/teacherLessonList", {
    //       method: "POST",
    //       headers: {
    //         Accept: "application/json",
    //         "Content-Type": "application/json",
    //       },
    //       body: JSON.stringify({
    //         branchId: this.state.branchID,
    //         companyId: this.state.companyID,
    //         materialId: materialId,
    //         schoolStagesId: schoolStagesId,
    //         schoolId: schoolStagesId,
    //         periodId: periodId,
    //         teacherId: _data["teacherId"],
    //       }),
    //     })
    //       .then((res) => {
    //         return res.json();
    //       })
    //       .then((fetchLessons) => {
    //         this.setState({ getLessons: fetchLessons });
    //       });
    //   };

    // deleteAnswer = (id) => {
    //     var _this = this;

    //     // Confirm Deletion
    //     var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

    //     if (con) {
    //         // Delete Subscription By ID
    //         fetch(properties.api_path + "/exams/deleteAnswer", {
    //             method: "POST",
    //             headers: {
    //                 'Accept': 'application/json',
    //                 'Content-Type': 'application/json',
    //             },
    //             body: JSON.stringify({
    //                 id: id
    //             })
    //         })
    //         .then(res => {return res.json()})
    //         .then(done => {
    //             this.setState({resend: true});
    //             _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم حذف الإجابة'});
    //         });
    //     }
    // }
    
    // deleteQuestion = (id) => {
    //     var _this = this;

    //     // Confirm Deletion
    //     var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

    //     if (con) {
    //         // Delete Subscription By ID
    //         fetch(properties.api_path + "/exams/deleteQuestion", {
    //             method: "POST",
    //             headers: {
    //                 'Accept': 'application/json',
    //                 'Content-Type': 'application/json',
    //             },
    //             body: JSON.stringify({
    //                 id: id
    //             })
    //         })
    //         .then(res => {return res.json()}).catch( (e)=>
    //         _this.toast.current.show({severity: 'error', summary: 'خطأ في  العملية', detail: 'تم حذف السؤال'}))
    //         .then(done => {
    //             this.setState({resend: true});
    //             _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم حذف السؤال'});
    //         });
    //     }
    // }
  

    
    componentDidMount () {
        var _this = this;

        document.title = properties.site_name + " | تعديل إمتحان";

        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        this.loadMessage();
    }

    // NOTE: the legacy response embedded a "materialName" field (looked up
    // server-side); InboxMessageResponse only returns materialId, so the
    // header's material name is left blank here -- no lookup-by-id endpoint
    // exists to resolve it client-side.
    loadMessage = () => {
        var _this = this;

        // Get Message From API
        apiFetch(`/messages/${this.state.examID}`)
        .then(fetchExam => {
            this.setState({
                subject: fetchExam.subject,
                body: fetchExam.body,
                sendDate: fetchExam.sentDate,
                image: fetchExam.image,
            });
        })
        .catch(error => {
            console.error('Error:', error)

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });

        // Get Replies From API
        apiFetch(`/messages/${this.state.examID}/replies`)
        .then(inboxReplies => {
            this.setState({ inboxReplies });
        })
        .catch(error => {
            console.error('Error:', error)
        });
    }
    
    render() {
        const videoType = [
            {label: 'Youtube', value: 0},
            {label: 'سيرفر', value: 1},
            {label: 'Vimeo', value: 2}
        ];
        const onUploadVoice = (e) => {
            let res = JSON.parse(e.xhr.response);
            console.log(res);
            this.setState({
              // voices:this.state.voices.concat(res.video_url.split('/')),
              formContent: {
                ...this.state.formContent,
             
                lessonVoiceList:this.state.formContent.lessonVoiceList.concat(res.video_url.split('/').pop()),
              },
            });
            // console.log(this.state.voices)
            console.log(this.state.formContent.lessonVoiceList)
      
            this.toast.current.show({
              severity: "info",
              summary: "تم بنجاح",
              detail: "تم رفع الصوت",
            });
          };
        let uploadVideo = (uploadedFiles) => {
            let _this = this;
            let videosArray = uploadedFiles.files;
    
            if (videosArray.length === 1) {
    
                let formData = new FormData();
                formData.append('LessonVideo', videosArray[0]);
    
                fetch(properties.uploads_path + '/upload-video.php', {
                    method: "POST",
                    body: formData
                }).then((response) => {
                    return response.json();
                }).then(res => {
                    _this.setState({formContent: {
                        ...this.state.formContent,
                        voiceLink: res.video_url
                    }});
                    _this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الفيديو'});
                }).catch(error => {
                    console.error('Error:', error)
                });
    
            } else {
                _this.toast.current.show({severity: 'error', summary: 'تحذير', detail: 'لا يمكنك رفع أكثر من فيديو واحد'});
            }
        }
    
        var addReply = (e) => {

            var _this = this;

            this.setState({
                buttonText: "جاري الإضافة ...",
                buttonColor: "loading",
            });

            // video/vimeo link boxes are mutually exclusive via setVideoType (0 = Youtube, 2 = Vimeo)
            let video = "";
            if (this.state.setVideoType === 0 && this.state.youtubeLink) {
                video = `https://www.youtube.com/embed/${this.state.youtubeLink
                    .split("/")
                    .pop()
                    .replace("watch?v=", "")}`;
            } else if (this.state.setVideoType === 2 && this.state.vimeoLink) {
                video = `https://player.vimeo.com/video/${this.state.vimeoLink
                    .split("/")
                    .pop()}`;
            }

            // Send the data to the server
            apiFetch(`/messages/${this.state.examID}/replies`, {
                method: "POST",
                body: {
                    body: this.state.answer,
                    image: this.state.imageAnswer ? this.state.imageAnswer.split('/').pop() : null,
                    video: video,
                },
            })
            .then(() => {
               _this.setState({
                   buttonText: "إضافة الرد",
                   buttonColor: "",
               });

               _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تمت إضافة الرد بنجاح'});
               this.loadMessage();
            })
            .catch(error => {
                console.error('Error:', error)

                _this.setState({
                    buttonText: "إضافة الرد",
                    buttonColor: "",
                });

                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        }

        let lastOrders = this.state.inboxReplies.map((qn, index) => {            
            return (
                <div className="p-grid p-fluid question p-mb-3" key={index}>
                <Card title={ qn.subject}>
                            <form onSubmit={(e) => e.preventDefault()} action="#" method="POST">
                                <div className="p-grid p-fluid">
                                
                                    <div className="p-col-12 p-md-12">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                 {/* <InputText id="exam-name" disabled value={this.state.materialName} onChange={(e) => this.setState({examName: e.target.value})} name="exam-name" type="text" />     */}
                                                 <h1 > {qn.body}  </h1>                                      

                                                 {/* <label htmlFor="exam-name"> السؤال </label>                                       */}
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                 {/* <InputText id="exam-name" disabled value={this.state.materialName} onChange={(e) => this.setState({examName: e.target.value})} name="exam-name" type="text" />     */}
                                                 <h1> {qn.sendDate}  </h1>                                      

                                                 {/* <label htmlFor="exam-name"> التاريخ </label>                                       */}
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                 {/* <InputText id="exam-name" disabled value={this.state.materialName} onChange={(e) => this.setState({examName: e.target.value})} name="exam-name" type="text" />     */}
                                                <img src={properties.uploads_path+'/thumb/'+qn.image} width={1000} height={200}></img>                               

                                                 {/* <label htmlFor="exam-name"> التاريخ </label>                                       */}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </Card>
            </div>
            );
        });

        return (
            <>
                <Header pageName={`الماده "${this.state.materialName}"`} />

                <div className="content">
                    <Toast ref={this.toast}></Toast>

                    <div className="container">
                        <Card title={ this.state.subject??""}>
                            <form onSubmit={(e) => e.preventDefault()} action="#" method="POST">
                                <div className="p-grid p-fluid">
                                
                                    <div className="p-col-12 p-md-12">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                 {/* <InputText id="exam-name" disabled value={this.state.materialName} onChange={(e) => this.setState({examName: e.target.value})} name="exam-name" type="text" />     */}
                                                 <h1 > {this.state.body}  </h1>                                      

                                                 {/* <label htmlFor="exam-name"> السؤال </label>                                       */}
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                 {/* <InputText id="exam-name" disabled value={this.state.materialName} onChange={(e) => this.setState({examName: e.target.value})} name="exam-name" type="text" />     */}
                                                 <h1> {this.state.sendDate}  </h1>                                      

                                                 {/* <label htmlFor="exam-name"> التاريخ </label>                                       */}
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                 {/* <InputText id="exam-name" disabled value={this.state.materialName} onChange={(e) => this.setState({examName: e.target.value})} name="exam-name" type="text" />     */}
                                                <img src={properties.uploads_path+'/thumb/'+this.state.image} width={1000} height={200}></img>                               

                                                 {/* <label htmlFor="exam-name"> التاريخ </label>                                       */}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </Card>
                        <Card title="الردود السابقه" className="p-mt-5 questions p-grid p-fluid p-col-12">
                    {this.state.inboxReplies!=[]?lastOrders:<div className='p-fluid p-col-12'>
                           <div className="p-col-4 p-md-4"></div>
                           
                        <div className='p-m-5 p-col-8 p-md-12'><h3 className=''>لا يوجد ردود سابقه</h3></div>
                        </div>}

                           
                        </Card>

                        <Card title="الرد" className="p-mt-5 questions">
                        <InputText type="text" onInput={(e) => this.setState({answer: e.target.value})} placeholder="اكتب ردك هنا.." />

                           
                        </Card>
                        
                        <div className="p-col-12 p-md-12">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <Toast ref={this.toast}></Toast>
                <FileUpload
                  name="thumb"
                  onUpload={(e) => {
                    let res = JSON.parse(e.xhr.response);
                    
                    this.setState({   imageAnswer: res.thumb_url });
                    this.toast.current.show({
                        severity: "info",
                        summary: "تم بنجاح",
                        detail: "تم رفع الصورة/الفيديو",
                      });
                  }}
                  invalidFileSizeMessageSummary="حجم الصورة كبير:"
                  invalidFileSizeMessageDetail="أقصى حجم للصورة هو (100 MB)"
                  url={`${properties.uploads_path}/upload-thumb.php`}
                  customUpload={false}
                  multiple={false}
                  accept={`${"image/*"}`}
                  maxFileSize={2000000000}
                  chooseLabel="إختيار صوره"
                  uploadLabel="رفع صوره"
                  cancelLabel="إلغاء"
                  emptyTemplate={
                    <p className="p-m-0">إسحب الصورة هنا ليبدأ التحميل</p>
                  }
                />
              </span>
            </div>
          </div>
          <div className="p-col-12 p-md-4 p-mt-3">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Dropdown
                          value={this.state.setVideoType}
                          options={videoType}
                          onChange={(e) => this.changeVideoType(e)
                          //   this.setState({
                          //     setVideoType: e.value,
                          //     formContent: {
                          //       ...this.state.formContent,
                          //       setVideoType: e.value,
                          //     },
                          //   })
                          }
                          name="vedioes"
                          placeholder="نوع الفيديو"
                        />
                      </span>
                    </div>
                  </div>
                   {(this.state.setVideoType === 0) ? (
                                        <div className="p-col-12 p-md-4 p-mt-3">
                                            <div className="p-inputgroup">
                                                <span className="p-float-label">
                                                    <InputText id="video-link" value={this.state.youtubeLink} name="video-link" type="text" onChange={(e) => {this.setState({
                                                        ...this.state.formContent,
                                                        youtubeLink: e.target.value
                                                    })}} />

                                                    <label htmlFor="video-link">لينك Youtube</label>
                                                </span>
                                            </div>
                                        </div>
                                    ): ""} 
                                    {(this.state.setVideoType === 1) ? (
                                       <div className="p-col-12 p-md-12">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        {
                          // (this.state.setVideoType === 1) ?
                          <FileUpload
                            name="LessonVideo"
                            onUpload={uploadVideo}
                            invalidFileSizeMessageSummary="حجم الفيديو  كبير:"
                            invalidFileSizeMessageDetail="أقصى حجم للصوت هو (2 GB)"
                            url={`${properties.uploads_path}/upload-video.php`}
                            customUpload={false}
                            multiple={true}
                            accept=".mp3"
                            maxFileSize={50000000000}
                            chooseLabel="إختيار الفيديو"
                            uploadLabel="رفع الفيديو"
                            cancelLabel="إلغاء"
                            emptyTemplate={
                              <p className="p-m-0">
                                إسحب الفيديو هنا ليبدأ التحميل
                              </p>
                            }
                          />
                          //  : ""
                        }
                      </span>
                    </div>
                    {this.state.lessonVoiceList.map((object, i) =>              
                   
                   <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank">{`${object}`}</a>
)}
                  </div>
                                  ): ""} 
                  {(this.state.setVideoType === 2) ? (
                                        <div className="p-col-12 p-md-4 p-mt-3">
                                            <div className="p-inputgroup">
                                                <span className="p-float-label">
                                                    <InputText id="video-link" value={this.state.vimeoLink} name="video-link" type="text" onChange={(e) => {this.setState({
                                                        
                                                        vimeoLink: e.target.value
                                                    })}} />

                                                    <label htmlFor="video-link">لينك Vimeo</label>
                                                </span>
                                            </div>
                                        </div>
                                    ): ""} 
          <div className="p-col-12 p-md-12">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        {
                          // (this.state.setVideoType === 1) ?
                          <FileUpload
                            name="LessonVideo"
                            onUpload={onUploadVoice}
                            invalidFileSizeMessageSummary="حجم الصوت  كبير:"
                            invalidFileSizeMessageDetail="أقصى حجم للصوت هو (2 GB)"
                            url={`${properties.uploads_path}/upload-video.php`}
                            customUpload={false}
                            multiple={true}
                            accept=".mp3"
                            maxFileSize={50000000000}
                            chooseLabel="إختيار الصوت"
                            uploadLabel="رفع الصوت"
                            cancelLabel="إلغاء"
                            emptyTemplate={
                              <p className="p-m-0">
                                إسحب الصوت هنا ليبدأ التحميل
                              </p>
                            }
                          />
                          //  : ""
                        }
                      </span>
                    </div>
                    {this.state.lessonVoiceList.map((object, i) =>              
                   
                   <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank">{`${object}`}</a>
)}
                  </div>
                        <Card className="p-mt-5">
                             <div className="p-col-12 p-md-12 p-mt-2">
                                <div className="p-inputgroup">
                                    <span className="p-float-label">
                                        <Button onClick={addReply}  className={`add-button ${this.state.buttonColor}`} label={this.state.buttonText}/>
                                    </span>
                                </div>
                            </div>
                        </Card>
                    </div>
                </div>
            </>
        );
    }
}

export default EditInboxDetail;