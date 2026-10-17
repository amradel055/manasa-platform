/* eslint-disable no-loop-func */
import React, { Component, createRef } from "react";
import ReactDOM from "react-dom";

// CSS
import "./Exams.scss";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch, getCurrentUser } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import Select from '../../components/Field/Select';
import { RadioButton } from "primereact/radiobutton";
import { FileUpload } from "primereact/fileupload";

// Properties
const properties = require("../../properties.json");

class AddExam extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {

    // The panel header's slot, when this form is open inside one. Null on

    // the standalone route, where the button keeps its own card.

    headerSlot: null,

    // Submit Button
    buttonText: "إضافة الإمتحان",
    buttonColor: "",

    // Teacher Identifier
    branchID: properties.branch_id,
    companyID: properties.company_id,

    // Exam
    questions: [
      {
        id: 1,
        question: null,
        type: null,
        img: "",
        questionImage:null,

        score: 0,
        answers: [
          {
            answerID: 1,
            text: null,
            note: null,
            correct: null,
            parent: 1,
            img: null,
            answerImage:null,

          },
        ],
      },
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
    rowIndex:null,

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

    selectedPeriod: null,
    teacherId: null,
  };

  getLessonsByStage = (schoolStagesId,materialId , periodId) => {
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

  getMaterialByStage(stageId){
    apiFetch("/lookup/materials/by-stage", { query: { stageId } })
      .then(grades => {
        this.setState({getMaterialsList : grades});
      })
      .catch(error => console.error('Error:', error));
  }

  componentDidMount() {
    // Opened inside a slide-over? Then the save button belongs on the
    // panel's header row, not at the far end of a form this long. It is
    // moved there with a portal, so it stays in THIS component's tree and
    // its label and colour are still driven from this state. The whole tree
    // is in the DOM before any componentDidMount runs, so the slot is
    // already there if there is one.
    const slot = document.querySelector(".m-panel-slot");
    if (slot) this.setState({ headerSlot: slot });
    document.title = properties.site_name + " | إضافة إمتحان جديد";
    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    // Get Grades From API
    apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
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

    let getQuestionIndex = this.state.questions.findIndex(
      (ques) => ques.id === this.state.getAnswers
    );
    let answersInputs = this.state.questions[getQuestionIndex].answers.map(
      (answer, i) => (
        <div className="p-grid p-fluid answer p-mb-3" key={answer.answerID}>
          <div className="p-col-12 p-md-5 p-p-2">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <InputText
                  value={answer.text}
                  id={`answer-${answer.answerID}`}
                  name={`answer-${answer.answerID}`}
                  type="text"
                  onChange={(e) => {
                    let temp = this.state.questions;
                    var a = temp[getQuestionIndex].answers.findIndex(
                      (ans) => ans.answerID === answer.answerID
                    );
                    temp[getQuestionIndex].answers[a].text = e.target.value;

                    this.setState({
                      questions: temp,
                    });
                  }}
                />

                <label htmlFor={`answer-${answer.answerID}`}>
                  إجابة {i + 1}
                </label>
              </span>
            </div>
          </div>
          <div className="p-col-12 p-md-2 p-d-flex p-ai-center p-jc-center">
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
          </div>
          <div className="p-col-12 p-md-12">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <Toast ref={this.toast}></Toast>
                <FileUpload
                  name="thumb"
                  onUpload={(e) => {
                    let res = JSON.parse(e.xhr.response);
                    let temp = this.state.questions;
                    var q = getQuestionIndex;
                    var answer = temp[q].answers[i];
                    // answer = res.thumb_url.split(',')[0].split('/')[1];
                    answer.answerImage =  res.thumb_url.split(',')[0].split('/')[1];
                    this.setState({ questions: temp });
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
              <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${ answer.answerImage}`} alt={this.state.answer} onClick={(e)=>e.target.requestFullscreen()}/>

            </div>
          </div>
        </div>
      )
    );

    let questionsInputs = this.state.questions.map((qn, index) => 
    { 
      return (
        <div className={ this.state.getAnswers=== qn.id ? "p-grid p-fluid question p-mb-3 bg-success" : "p-grid p-fluid question p-mb-3"} key={index}>
          <div className="p-col-12 p-md-6 p-p-2">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <InputText
                  value={qn.question}
                  id={`question-${index}`}
                  name={`question-${index}`}
                  type="text"
                  onChange={(e) => {
                    let temp = this.state.questions;
                    var q = temp.findIndex((ques) => ques.id === qn.id);
                    temp[q].question = e.target.value;
                   
                    this.setState({
                      questions: temp,
                    });
                  }}
                />

                <label htmlFor={`question-${qn.id}`}>سؤال {index + 1}</label>
              </span>
            </div>
          </div>
          <div className="p-col-12 p-md-2">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <Select
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
          </div>

          <div className="p-col-12 p-md-2">
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
             
              className={ this.state.getAnswers=== qn.id ? "p-button-rounded p-button-danger p-mr-2 " : "p-button-rounded p-button-success p-mr-2 "}
              onClick={() => {
                this.setState({
                  getAnswers: qn.id,

                });
              }}
            />
          </div>
          <div className="p-col-12 p-md-12">
            <div className="p-inputgroup">
              <span className="p-float-label">
                <Toast ref={this.toast}></Toast>
                <FileUpload
                  name="thumb"
                  onUpload={(e) => {
                    let res = JSON.parse(e.xhr.response);
                    let temp = this.state.questions;
                    var q = temp.findIndex((ques) => ques.id === qn.id);
                    temp[q].questionImage = res.thumb_url.split(',')[0].split('/')[1];
                    // temp[q].img = res.media_url;
                    console.log(res);
                    this.setState({ questions: temp });
                    this.toast.current.show({
                      severity: "info",
                      summary: "تم بنجاح",
                      detail: "تم رفع الصورة/الفيديو",
                    });
                    console.log(this.state.questions);
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
             
              <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${ qn.questionImage}`} alt={this.state.question} onClick={(e)=>e.target.requestFullscreen()}/>

            </div>
          </div>
          
        </div>
      );
    });

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

      let total = 0;

      for (var i = 0; i < this.state.questions.length; i++) {
        total += parseFloat(this.state.questions[i].score);
      }

      console.log(total);

      this.setState({
        buttonText: "جاري الإضافة ...",
        buttonColor: "loading",
      });

      var finalQuestionsForm = [];

      for (var q = 0; q < this.state.questions.length; q++) {
        var finalAnswers = [];

        for (var a = 0; a < this.state.questions[q].answers.length; a++) {
          finalAnswers.push({
            answer: this.state.questions[q].answers[a].text,
            img: this.state.questions[q].answers[a].answerImage,
            correct: this.state.questions[q].answers[a].correct === 1,
            reason: this.state.questions[q].answers[a].note,
          });
        }

        finalQuestionsForm.push({
          questionName: this.state.questions[q].question,
          questionType: this.state.questions[q].type,
          img: this.state.questions[q].questionImage,
          questionDegree: this.state.questions[q].score,
          answers: finalAnswers,
        });
      }

      // Send the data to the server
      apiFetch("/exams", {
        method: "POST",
        body: {
          examName: this.state.examName,
          degree: total,
          active: this.state.selectedStatus,
          schoolStagesId: this.state.selectedStage,
          lessonId: this.state.selectedLesson,
          timer: this.state.Timer,
          periodId: this.state.selectedPeriod,
          examType: this.state.selectedType,
          branchId: this.state.branchID,
          materialId: this.state.selectedMaterial,
          questions: finalQuestionsForm,
        },
      })
        .then(() => {
          _this.setState({
            buttonText: "إضافة الإمتحان",
            buttonColor: "",
          });

          _this.toast.current.show({
            severity: "success",
            summary: "تم بنجاح",
            detail: "تمت إضافة الإمتحان بنجاح",
          });
          window.location.reload();
        })
        .catch((error) => {
          console.error("Error:", error);

          _this.setState({
            buttonText: "إضافة الإمتحان",
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
        <Header pageName="إضافة إمتحان جديد" />

        <div className="content">
          <Toast ref={this.toast}></Toast>

          <div className="container">
            <Card title="إضافة إمتحان جديد">
              <form
                onSubmit={(e) => e.preventDefault()}
                action="#"
                method="POST"
              >
                <div className="p-grid p-fluid">
                <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Select
                          value={this.state.selectedType}
                          options={type}
                          placeholder="نوع الإمتحان"
                          onChange={(e) =>
                            this.setState({ selectedType: e.value })
                          }
                          optionLabel="label"
                        />
                      </span>
                    </div>
                  </div>
                  <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <InputText
                          id="exam-name"
                          value={this.state.examName}
                          required
                          onChange={(e) =>
                            this.setState({ examName: e.target.value })
                          }
                          name="exam-name"
                          type="text"
                        />
                        <label htmlFor="exam-name">إسم الإمتحان</label>
                      </span>
                    </div>
                  </div>
                  <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Select
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
                        <Select
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
                 
                  <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Select
                          value={this.state.selectedPeriod}
                          options={periods}
                          placeholder="  إختر الفترة "
                          onChange={(e) =>
                            this.setState({ selectedPeriod: e.value },
                              this.getLessonsByStage(
                                this.state.selectedStage,
                                this.state.selectedMaterial,
                                e.value
                              )
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
                { this.state.selectedType===1? <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Select
                          value={this.state.selectedLesson}
                          options={lessonsList}
                          placeholder="إختر الدرس"
                          onChange={(e) =>
                            this.setState({ selectedLesson: e.value })

                          }
                          optionLabel="label"
                          filter
                          showClear
                          filterBy="label"
                        />
                      </span>
                    </div>
                  </div>:null}
                  <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Select
                          value={this.state.selectedStatus}
                          options={status}
                          placeholder="حالة الإمتحان"
                          onChange={(e) =>
                            this.setState({ selectedStatus: e.value })
                          }
                          optionLabel="label"
                        />
                      </span>
                    </div>
                  </div>
                  <div className="p-col-12 p-md-4 p-my-2">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <InputText
                          id="exam-timer"
                          value={this.state.Timer}
                          onChange={(e) =>
                            this.setState({ Timer: e.target.value })
                          }
                          name="Timer"
                          type="number"
                        />

                        <label htmlFor="exam-timer">
                          مدة الامتحان بالدقايق
                        </label>
                      </span>
                    </div>
                  </div>
                  
                </div>
              </form>
            </Card>

            <Card title="الأسئلة" className="p-mt-5 questions">
              <Button
                label="إضافة سؤال"
                icon="pi pi-plus"
                className="p-mb-5 add-question"
                onClick={() => {
                  this.setState(
                    {
                      numQuestions: this.state.numQuestions + 1,
                    },
                    () => {
                      let temp = this.state.questions;

                      temp.push({
                        id: this.state.numQuestions,
                        question: null,
                        type: null,
                        score: 0,
                        answers: [
                          {
                            answerID: 1,
                            text: "",
                            note: "",
                            correct: null,
                            parent: this.state.numQuestions,
                          },
                        ],
                      });

                      this.setState({
                        questions: temp,
                      });
                    }
                  );
                }}
              />

              {questionsInputs}
            </Card>

            <Card title="الإجابات" className="p-mt-5 questions">
              <div>الإجابة للسؤال رقم{getQuestionIndex+1}</div>
              <Button
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
              />

              {answersInputs}
            </Card>

            {/* In a panel the save button goes up into the header row; on
                the standalone page it keeps its card at the foot of the form.
                It is the same element either way, with the same state behind
                it -- and it submits through onClick, not a native submit, so
                moving it out of the <form> changes nothing. */}
            {this.state.headerSlot ? (
              ReactDOM.createPortal(
                <Button
                  onClick={addExam}
                  className={`add-button ${this.state.buttonColor}`}
                  label={this.state.buttonText}
                />,
                this.state.headerSlot
              )
            ) : (
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
            )}
          </div>
        </div>
      </>
    );
  }
}

export default AddExam;
