/* eslint-disable no-loop-func */
import React, { Component, createRef } from "react";

// CSS
import "./Exams.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import { Dropdown } from "primereact/dropdown";
import { RadioButton } from "primereact/radiobutton";
import { FileUpload } from "primereact/fileupload";

// Properties
const properties = require("../../properties.json");

class AddCommonExam extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    // Submit Button
    buttonText: "إضافة سؤال",
    buttonColor: "",

    // Teacher Identifier
    branchID: properties.branch_id,
    companyID: properties.company_id,

    // Exam
    // questions: [
    //   {
    //     id: 1,
    //     question: null,
    //     type: null,
    //     img: "",
    //     score: 0,
    //     answers: [
    //       {
    //         answerID: 1,
    //         text: null,
    //         note: null,
    //         correct: null,
    //         parent: 1,
    //         img: null,
    //       },
    //     ],
    //   },
    // ],
    answerContent: {
      image: "",
      text:"",
      youtubeLink: null,
      vimeoLink: null,
      voiceLink: null,
      lessonVoiceList:[],

    },   
     img: "",
    question:"",


    numQuestions: 1,
    numAnswers: 1,

    // Get Data
    getSchoolStages: [],
    getLessons: [],
    getPeriods: [],
    getMaterialsList: [],
    // Get Current Answers
    getAnswers: 1,
    setVideoType: null,
    setVideoTypeQuestion: null,

    // Inputs Data
    selectedLesson: null,
    selectedStage: null,
    selectedStatus: null,
    selectedMonth: null,
    selectedType: null,
    selectedMaterial: null,
    examName: null,
    Timer: 0,
    formContent: {
      type: null,
      id: null,
      branchId: properties.branch_id,
      companyId: properties.company_id,
      mediaType: 0,
      mediaLink: null,
      thumb: null,
      youtubeLink: null,
      vimeoLink: null,
      voiceLink: null,
      lessonVoiceList:[],

    },

    selectedPeriod: null,
  };

  changeVideoType = (e) => {
    this.setState({
      setVideoType: e.value,
      answerContent: {
        ...this.state.answerContent,
        youtubeLink: null,
        voiceLink: null,
      },
    });
  };
  changeVideoTypeQuestion = (e) => {
    this.setState({
      setVideoTypeQuestion: e.value,
      formContent: {
        ...this.state.formContent,
        youtubeLink: null,
        voiceLink: null,
      },
    });
  };

  getLessonsByStage = (schoolStagesId,materialId , periodId) => {
    // Get Lessons From API
    apiFetch("/lessons", { query: { materialId, schoolStageId: schoolStagesId, id: periodId } })
      .then((fetchLessons) => {
        this.setState({ getLessons: fetchLessons });
      })
      .catch((error) => console.error("Error:", error));
  };
  getPeriod(materialId) {
    apiFetch("/periods/by-material", { query: { materialId } })
      .then((grades) => {
        this.setState({ getPeriods: grades });
      })
      .catch((error) => console.error("Error:", error));
  }

  // getPeriods() {
  //   var data = JSON.parse(window.localStorage.getItem("currentTeacher"));
  //   fetch(properties.api_path + "/periods/allPeriods", {
  //     method: "POST",
  //     headers: {
  //       Accept: "application/json",
  //       "Content-Type": "application/json",
  //     },
  //     body: JSON.stringify({
  //       branchId: this.state.branchID,
  //     }),
  //   })
  //     .then((res) => {
  //       return res.json();
  //     })
  //     .then((grades) => {
  //       console.log(grades);
  //       this.setState({ getPeriods: grades });
  //     });
  // }
  getMaterialByStage(stageId){
    apiFetch("/lookup/materials/by-stage", { query: { stageId } })
      .then(grades => {
        this.setState({getMaterialsList : grades});
      })
      .catch(error => console.error('Error:', error));
 }
  //getMaterialList
  // getMaterial = () => {
  //   // Get Periods From API
  //   var data = JSON.parse(window.localStorage.getItem("currentTeacher"));
  //   fetch(properties.api_path + "/lockup/materialList", {
  //     method: "POST",
  //     headers: {
  //       Accept: "application/json",
  //       "Content-Type": "application/json",
  //     },
  //     body: JSON.stringify({
  //       teacherId: data["teacherId"],
  //       branchId: this.state.branchID,
  //     }),
  //   })
  //     .then((res) => {
  //       return res.json();
  //     })
  //     .then((fetchMaterials) => {
  //       this.setState({
  //         getMaterialsList: fetchMaterials,
  //       });
  //     });
  // };

  componentDidMount() {
    document.title = properties.site_name + " | إضافة سؤال جديد";
    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    // Get Grades From API
    apiFetch("/lookup/school-stages", { query: { branchId: this.state.branchID } })
      .then((grades) => {
        this.setState({ getSchoolStages: grades });
      })
      .catch((error) => console.error("Error:", error));
  }
  // addAnswerImage (url ,index) {
  //  this.setState({questions : {...this.state.questions[this.state.getAnswers -1].answers[index] ,
  //   img : url,
  // }}) ;
  // console.log(this.state.questions[this.state.getAnswers -1].answers[index]);
  // }

  render() {
    let materialList = this.state.getMaterialsList.map((period) => {
      return { label: period.name, value: period.id };
    });

    let periods = this.state.getPeriods.map((period) => {
      return { label: period.name, value: period.id };
    });

    let questionType = [
      { label: "إختياري", value: 0 },
      { label: "أكمل", value: 1 },
    ];

    let correct = [
      { label: "إجابة خاطئة", value: 0 },
      { label: "إجابة صحيحة", value: 1 },
    ];

    let status = [
      { label: "مفعّل", value: 1 },
      { label: "غير مفعّل", value: 0 },
    ];
    let type = [
      { label: "يدوي", value: 1 },
      { label: "ألي", value: 0 },
    ];
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
      answerContent: {
        ...this.state.answerContent,
     
        lessonVoiceList:this.state.answerContent.lessonVoiceList.concat(res.video_url.split('/').pop()),
      },
    });
    // console.log(this.state.voices)
    console.log(this.state.answerContent.lessonVoiceList)

    this.toast.current.show({
      severity: "info",
      summary: "تم بنجاح",
      detail: "تم رفع الصوت",
    });
  };
  // * Custom Upload Handler
  // @param null
  
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
 const onUploadVoiceQuestion = (e) => {
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
let uploadVideoQuestion = (uploadedFiles) => {
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
    // let getQuestionIndex = this.state.questions.findIndex(
    //   (ques) => ques.id === this.state.getAnswers
    // );
    let answersInputs =  (
        <div className="p-grid p-fluid answer p-mb-6" >
          <div className="p-col-12 p-md-5 p-p-2">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <InputText
                  value={this.state.answerContent.text}
                  // id={`answer-${answer.answerID}`}
                  // name={`answer-${answer.answerID}`}
                  type="text"
                  onChange={(e) => {
                    // let temp = this.state.questions;
                    // var a = temp[getQuestionIndex].answers.findIndex(
                    //   (ans) => ans.answerID === answer.answerID
                    // );
                    // temp[getQuestionIndex].answers[a].text = e.target.value;
                    this.setState({
                      answerContent: {
                        ...this.state.answerContent,
                        text: e.target.value,
                      },
                    });
                
                  }}
                />

                {/* <label htmlFor={`answer-${answer.answerID}`}>
                  إجابة {i + 1}
                </label> */}
              </span>
            </div>
          </div>
          {/* <div className="p-col-12 p-md-2 p-d-flex p-ai-center p-jc-center">
            <div className="p-field-radiobutton p-mb-0">
              <RadioButton
                inputId={`correct-${answer.answerID}`}
                value={1}
                name="correct"
                id={`correct-${answer.answerID}`}
                checked={answer.correct === 1}
                onChange={(e) => {
                  let temp = this.state.questions;
                  var a = temp[getQuestionIndex].answers.findIndex(
                    (ans) => ans.answerID === answer.answerID
                  );
                  for (
                    var c = 0;
                    c < temp[getQuestionIndex].answers.length;
                    c++
                  ) {
                    temp[getQuestionIndex].answers[c].correct = 0;
                  }

                  temp[getQuestionIndex].answers[a].correct = e.value;

                  this.setState({
                    questions: temp,
                  });
                }}
              />
              <label
                className="right-answer"
                htmlFor={`correct-${answer.answerID}`}
              >
                الإجابة الصحيحة
              </label>
            </div>
          </div>
          <div className="p-col-12 p-md-3 p-p-2">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <InputText
                  value={answer.note}
                  id={`note-${answer.answerID}`}
                  name={`note-${answer.answerID}`}
                  type="text"
                  onChange={(e) => {
                    let temp = this.state.questions;
                    var a = temp[getQuestionIndex].answers.findIndex(
                      (ans) => ans.answerID === answer.answerID
                    );
                    temp[getQuestionIndex].answers[a].note = e.target.value;

                    this.setState({
                      questions: temp,
                    });
                  }}
                />

                <label htmlFor={`note-${answer.answerID}`}>ملحوظة</label>
              </span>
            </div>
          </div>
          <div className="p-col-12 p-md-2 p-p-2">
            <Button
              icon="pi pi-trash"
              className="p-button-danger p-button-rounded"
              onClick={() => {
                var temp = this.state.questions;
                temp[getQuestionIndex].answers.splice(i, 1);

                this.setState({
                  questions: temp,
                });
              }}
            />
          </div> */}
          <div className="p-col-12 p-md-12">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <Toast ref={this.toast}></Toast>
                <FileUpload
                  name="thumb"
                  onUpload={(e) => {
                    let res = JSON.parse(e.xhr.response);
                    // let temp = this.state.answerContent;
                    // var q = getQuestionIndex;
                    // var answer = temp[q].answers[i];
                    // temp.image = res.media_url;
                    this.setState({
                      answerContent: {
                        ...this.state.answerContent,
                        image: res.thumb_url.split('/').pop(),
                      },
                    });
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
                                                    <InputText id="video-link" value={this.state.answerContent.youtubeLink} name="video-link" type="text" onChange={(e) => {this.setState({formContent: {
                                                        ...this.state.answerContent,
                                                        youtubeLink: e.target.value
                                                    }})}} />

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
                    {this.state.answerContent.lessonVoiceList.map((object, i) =>              
                   
                   <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank">{`${object}`}</a>
)}
                  </div>
                                  ): ""} 
                  {(this.state.setVideoType === 2) ? (
                                        <div className="p-col-12 p-md-4 p-mt-3">
                                            <div className="p-inputgroup">
                                                <span className="p-float-label">
                                                    <InputText id="video-link" value={this.state.answerContent.vimeoLink} name="video-link" type="text" onChange={(e) => {this.setState({formContent: {
                                                        ...this.state.answerContent,
                                                        vimeoLink: e.target.value
                                                    }})}} />

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
                    {this.state.answerContent.lessonVoiceList.map((object, i) =>              
                   
                   <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank">{`${object}`}</a>
)}
                  </div>
        </div>
      )
    

    let questionsInputs =  (
        <div className="p-grid p-fluid question p-mb-3">
          <div className="p-col-12 p-md-6 p-p-2">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <InputText
                  value={this.state.question}
                  // id={`question-${index}`}
                  // name={`question-${index}`}
                  type="text"
                  onChange={(e) => {
                    // let temp = this.state.questions;
                    // var q = temp.findIndex((ques) => ques.id === qn.id);
                    // temp[q].question = e.target.value;
                       this.setState({
                        question: e.target.value,
                    });
                    
                  }}
                />

                {/* <label htmlFor={`question-${qn.id}`}>سؤال {index + 1}</label> */}
              </span>
            </div>
          </div>
          {/* <div className="p-col-12 p-md-2">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <Dropdown
                  value={qn.type}
                  options={questionType}
                  placeholder="نوع السؤال"
                  onChange={(e) => {
                    let temp = this.state.questions;
                    var q = temp.findIndex((ques) => ques.id === qn.id);
                    temp[q].type = e.value;

                    this.setState({
                      questions: temp,
                    });
                  }}
                  optionLabel="label"
                  showClear
                />
              </span>
            </div>
          </div> */}

          {/* <div className="p-col-12 p-md-2">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <InputText
                  id={`score-${index}`}
                  value={qn.score}
                  name={`score-${index}`}
                  type="number"
                  onChange={(e) => {
                    let temp = this.state.questions;
                    var q = temp.findIndex((ques) => ques.id === qn.id);
                    temp[q].score = e.target.value;

                    this.setState({
                      questions: temp,
                    });
                  }}
                />

                <label htmlFor={`score-${index}`}>الدرجة</label>
              </span>
            </div>
          </div>
          <div className="p-col-12 p-md-2 p-p-2">
            <Button
              icon="pi pi-trash"
              className="p-button-danger p-button-rounded"
              onClick={() => {
                var temp = this.state.questions;
                temp.splice(index, 1);

                this.setState({
                  questions: temp,
                });
              }}
            />
            <Button
              icon="pi pi-list"
              className="p-button-rounded p-button-success p-mr-2"
              onClick={() => {
                this.setState({
                  getAnswers: qn.id,
                });
              }}
            />
          </div> */}
          <div className="p-col-12 p-md-12">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <Toast ref={this.toast}></Toast>
                <FileUpload
                  name="thumb"
                  onUpload={(e) => {
                    let res = JSON.parse(e.xhr.response);
                    console.log(res)
                    // let temp = this.state.questions;
                    // var q = temp.findIndex((ques) => ques.id === qn.id);
                    // temp[q].img = res.media_url;
                    // console.log(res.media_url);
                    // this.setState({ questions: temp });
                    this.setState({
                      img: res.thumb_url,
                  });
                    this.toast.current.show({
                      severity: "info",
                      summary: "تم بنجاح",
                      detail: "تم رفع الصورة/الفيديو",
                    });
                    // console.log(this.state.questions);
                  }}
                  invalidFileSizeMessageSummary="حجم الصورة/الفيديو كبير:"
                  invalidFileSizeMessageDetail="أقصى حجم للصورة/فيديو هو (200 MB)"
                  url={`${properties.uploads_path}/upload-thumb.php`}
                  customUpload={false}
                  multiple={false}
                  accept={`${
                    this.state.formContent.mediaType === 1
                      ? "video/*"
                      : "image/*"
                  }`}
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
                          value={this.state.setVideoTypeQuestion}
                          options={videoType}
                          onChange={(e) => this.changeVideoTypeQuestion(e)
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
                                                    <InputText id="video-link" value={this.state.formContent.youtubeLink} name="video-link" type="text" onChange={(e) => {this.setState({formContent: {
                                                        ...this.state.formContent,
                                                        youtubeLink: e.target.value
                                                    }})}} />

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
                            onUpload={uploadVideoQuestion}
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
                    {this.state.formContent.lessonVoiceList.map((object, i) =>              
                   
                   <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank">{`${object}`}</a>
)}
                  </div>
                                  ): ""} 
                  {(this.state.setVideoType === 2) ? (
                                        <div className="p-col-12 p-md-4 p-mt-3">
                                            <div className="p-inputgroup">
                                                <span className="p-float-label">
                                                    <InputText id="video-link" value={this.state.formContent.vimeoLink} name="video-link" type="text" onChange={(e) => {this.setState({formContent: {
                                                        ...this.state.formContent,
                                                        vimeoLink: e.target.value
                                                    }})}} />

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
                            onUpload={onUploadVoiceQuestion}
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
                    {this.state.formContent.lessonVoiceList.map((object, i) =>              
                   
                   <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank">{`${object}`}</a>
)}
                  </div>
        </div>
      );
    

    let gradesList = this.state.getSchoolStages.map((grade) => {
      return {
        label: grade.name,
        value: grade.id,
      };
    });

    let lessonsList = this.state.getLessons.map((lessons) => {
      return {
        label: lessons.lessonName,
        value: lessons.id,
      };
    });

    var addExam = (e) => {
      var _this = this;

      // let total = 0;

      // for (var i = 0; i < this.state.questions.length; i++) {
      //   total += parseFloat(this.state.questions[i].score);
      // }

      // console.log(total);

      this.setState({
        buttonText: "جاري الإضافة ...",
        buttonColor: "loading",
      });

      // var finalQuestionsForm = [];

      // for (var q = 0; q < this.state.questions.length; q++) {
      //   var finalAnswers = [];

      //   for (var a = 0; a < this.state.questions[q].answers.length; a++) {
      //     finalAnswers.push({
      //       companyId: this.state.companyID,
      //       branchId: this.state.branchID,
      //       answer: this.state.questions[q].answers[a].text,
      //       img: this.state.questions[q].answers[a].img,
      //       // correct: this.state.questions[q].answers[a].correct,
      //       // reason: this.state.questions[q].answers[a].note,
      //     });
      //   }

      //   finalQuestionsForm.push({
      //     companyId: this.state.companyID,
      //     branchId: this.state.branchID,
      //     questionName: this.state.questions[q].question,
      //     // questionType: this.state.questions[q].type,
      //     img: this.state.questions[q].img,
      //     // questionDegree: this.state.questions[q].score,
      //     answersdtoList: finalAnswers,
      //   });
      // }

      // console.log(finalQuestionsForm);

      // Send the data to the server
      apiFetch("/common-questions", {
        method: "POST",
        body: {
          text: this.state.question,
          image: this.state.img,
          branchId: this.state.branchID,
          materialId: this.state.selectedMaterial,
          answer: {
            text: this.state.answerContent.text,
            image: this.state.answerContent.image,
          },
        },
      })
        .then(() => {
          _this.setState({
            buttonText: "إضافة سؤال",
            buttonColor: "",
          });

          _this.toast.current.show({
            severity: "success",
            summary: "تم بنجاح",
            detail: "تمت إضافة السؤال بنجاح",
          });
          window.location.reload();
        })
        .catch((error) => {
          console.error("Error:", error);

          _this.setState({
            buttonText: "إضافة سؤال",
            buttonColor: "",
          });

          _this.toast.current.show({
            severity: "error",
            summary: "لم تتم العملية",
            detail: error.message || "خطأ في الإتصال",
          });
        });
    };

    return (
      <>
        <Header pageName="إضافة سؤال شائع جديد" />

        <div className="content">
          <Toast ref={this.toast}></Toast>

          <div className="container">
            <Card title="إضافة سؤال شائع جديد">
              <form
                onSubmit={(e) => e.preventDefault()}
                action="#"
                method="POST"
              >
                <div className="p-grid p-fluid">
               
                  <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Dropdown
                          value={this.state.selectedStage}
                          options={gradesList}
                          placeholder="إختر المرحلة الدراسية"
                          onChange={(e) => {
                            this.setState(
                              { selectedStage: e.value },
                              this.getLessonsByStage(
                                e.value,
                                this.state.selectedMaterial,
                                this.state.selectedPeriod
                              ),
                              this.getMaterialByStage(e.value)
                            );
                          }}
                          optionLabel="label"
                          filter
                          showClear
                          filterBy="label"
                        />
                      </span>
                    </div>
                  </div>
                  <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Dropdown
                          value={this.state.selectedMaterial}
                          options={materialList}
                          placeholder="إختر المادة"
                          onChange={(e) =>
                            this.setState({ selectedMaterial: e.value }
                              ,this.getPeriod(e.value), this.getLessonsByStage(
                                this.state.selectedStage,
                                e.value,
                                this.state.selectedPeriod
                              ),
                              )
                            
                           
                            
                          }
                          optionLabel="label"
                          filter
                          showClear
                          filterBy="label"
                        />
                      </span>
                    </div>
                  </div>
                 
                
                </div>
              </form>
            </Card>

            <Card title="السؤال" className="p-mt-5 questions">
             

              {questionsInputs}
            </Card>

            <Card title="الاجابه" className="p-mt-5 questions">
              {/* <Button
                label="إضافة إجابة"
                icon="pi pi-plus"
                className="p-mb-5 add-question"
                onClick={(e) => {
                  this.setState(
                    {
                      numAnswers: this.state.numAnswers + 1,
                    },
                    () => {
                      let temp = this.state.questions;

                      temp[getQuestionIndex].answers.push({
                        answerID: this.state.numAnswers,
                        text: null,
                        note: null,
                        parent: this.state.getAnswers,
                      });

                      this.setState({
                        questions: temp,
                      });
                    }
                  );
                }}
              /> */}

              {answersInputs}
            </Card>

            <Card className="p-mt-5">
              <div className="p-col-12 p-md-12 p-mt-2">
                <div className="p-inputgroup">
                  <span className="p-float-label">
                    <Button
                      onClick={addExam}
                      className={`add-button ${this.state.buttonColor}`}
                      label={this.state.buttonText}
                    />
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

export default AddCommonExam;
