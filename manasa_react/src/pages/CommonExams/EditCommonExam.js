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
// Required component 
// import Lightbox from "react-awesome-lightbox";
// // Required stylesheet
// import "react-awesome-lightbox/build/style.css";
// // Properties
const properties = require("../../properties.json");

class EditCommonExam extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    // Submit Button
    buttonText: "تعديل السؤال",
    buttonColor: "",

    // Teacher Identifier
    branchID: properties.branch_id,
    companyID: properties.company_id,
    examID: (this.props.match.params.id) ? parseInt(this.props.match.params.id) : 0,


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
      id:null

    },   
     img: "",
    question:"",
    questionId:null,
    questionAnswerImage:"",
    questionImage:"",

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
    },
    qustionReply:null,
    materialId:null,
    levelId:null,
    qusetionImg:null,
    answer:null,
    answerImg:null,



    selectedPeriod: null,
  };
  getMaterialByStage(stageId){
    apiFetch("/lookup/materials/by-stage", { query: { stageId } })
      .then(grades => {
        this.setState({getMaterialsList : grades});
      })
      .catch(error => console.error('Error:', error));
 }

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
//   getMaterialByStage(stageId){
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
    var _this = this;
    // let lightbox = new PhotoSwipeLightbox({
    //     gallery: '#' + props.galleryID,
    //     children: 'a',
    //     pswpModule: () => import('photoswipe'),
    //   });
    //   lightbox.init();
  
    //   return () => {
    //     lightbox.destroy();
    //     lightbox = null;
    //   };
    document.title = properties.site_name + " | تعديل السؤال ";
    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    apiFetch("/lookup/school-stages", { query: { branchId: this.state.branchID } })
      .then((grades) => {
        this.setState({ getSchoolStages: grades });
      })
      .catch((error) => console.error("Error:", error));

    // Get Grades From API
    apiFetch(`/common-questions/${this.state.examID}`)
    .then(fetchExam => {
        this.setState({
            qustionReply:fetchExam.text,
            materialId:fetchExam.materialId,
            qusetionImg:fetchExam.image,
            questionId:fetchExam.id,
           answerContent:{
                text: fetchExam.answer ? fetchExam.answer.text : "",
                image: fetchExam.answer ? fetchExam.answer.image : "",
                id: fetchExam.answer ? fetchExam.answer.id : null
            },
            questionAnswerImage: fetchExam.answer ? fetchExam.answer.image : "",
            questionImage:fetchExam.image
            // Note: the backend no longer returns a school stage for a question (materials
            // are already stage-scoped, and QuestionCommon has no schoolStage field), so the
            // "levelId" dropdown can't be prefilled here -- the teacher must reselect the
            // stage to repopulate the material dropdown with this materialId.
        });
    })
    .catch(error => {
        console.error('Error:', error)

        _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
    });
  }

  openFullscreen  () {
    document.getElementById('image')?.requestFullscreen()
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
                    // console.log(res.thumb_url.split('/'))
                    this.state.questionAnswerImage = res.thumb_url.split(',')[0].split('/')[1];
                    this.setState({
                      answerContent: {
                        ...this.state.answerContent,
                        image:this.state.questionAnswerImage,
                        
                      },
                    
                    });
                    
                    console.log(this.state.questionAnswerImage)

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
              <img className="preview-thumb" onClick={(e)=>{e.target.requestFullscreen()
            console.log(
                `${properties.uploads_path}/thumb/${this.state.questionAnswerImage}`)
        
        }
            } src={`${properties.uploads_path}/thumb/${this.state.questionAnswerImage}`} alt={this.state.answer} />

            </div>
          </div>
        </div>
      )
    

    let questionsInputs =  (
        <div className="p-grid p-fluid question p-mb-3">
          <div className="p-col-12 p-md-6 p-p-2">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <InputText
                  value={this.state.qustionReply}
                  // id={`question-${index}`}
                  // name={`question-${index}`}
                  type="text"
                  onChange={(e) => {
                    // let temp = this.state.questions;
                    // var q = temp.findIndex((ques) => ques.id === qn.id);
                    // temp[q].question = e.target.value;
                       this.setState({
                        qustionReply: e.target.value,
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
                    this.state.questionImage = res.thumb_url.split(',')[0].split('/')[1];
                    this.setState({
                      img: this.state.questionImage,
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
              {/* <ImageGallary
      items={[`${properties.uploads_path}/${this.state.qusetionImg}`]}
      showThumbnails={true}
      showFullscreenButton={true}
    //   showPlayButton={false}
    //   showBullets={true}
    //   ref={imageGalleryRef}
    //   onClick={onClickHandler}
    /> */}
    {/* <Lightbox image={`${properties.uploads_path}/${this.state.qusetionImg}`} title="Image Description" /> */}
              <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${this.state.questionImage}`} alt={this.state.question} onClick={(e)=>e.target.requestFullscreen()}/>

            </div>
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
      apiFetch(`/common-questions/${this.state.questionId}`, {
        method: "PUT",
        body: {
          text: this.state.qustionReply,
          image: this.state.questionImage,
          branchId: this.state.branchID,
          materialId: this.state.materialId,
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
            detail: "تم تعديل السؤال بنجاح",
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
        <Header pageName="تعديل سؤال شائع " />

        <div className="content">
          <Toast ref={this.toast}></Toast>

          <div className="container">
            <Card title="تعديل سؤال شائع ">
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
                          value={this.state.levelId}
                          options={gradesList}
                          placeholder="إختر المرحلة الدراسية"
                          onChange={(e) => {
                            this.setState(
                              { levelId: e.value },
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
                          value={this.state.materialId}
                          options={materialList}
                          placeholder="إختر المادة"
                          onChange={(e) =>
                            this.setState({ materialId: e.value }
                              ,this.getPeriod(e.value), this.getLessonsByStage(
                                this.state.levelId,
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

export default EditCommonExam;
