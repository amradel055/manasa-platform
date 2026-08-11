/* eslint-disable no-loop-func */
import React, {Component, createRef} from 'react';

// CSS
import './Exams.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch, getCurrentUser } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { RadioButton } from 'primereact/radiobutton';
import { FileUpload } from 'primereact/fileupload';

// Properties
const properties = require('../../properties.json');

class EditExam extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        // Submit Button
        buttonText: "حفظ التعديلات",
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
                questionImage:null,
                answers: [
                    {
                        answerID: 1,
                        id: null,
                        text: null,
                        note: null,
                        correct: null,
                        parent: 1,
                        answerImage:null,
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

        examName: "",
        timer:0,
        formContent: {
            type: null,
            id: null,
            branchId: properties.branch_id,
            companyId: properties.company_id,
            mediaType: 0,
            mediaLink: null,
            thumb: null,
          },
    }
    getMaterialByStage(stageId){
        apiFetch("/lookup/materials/by-stage", { query: { stageId } })
          .then(grades => {
            this.setState({getMaterialsList : grades});
          })
          .catch(error => console.error('Error:', error));
     }
     getPeriod(materialId) {
        apiFetch("/periods/by-material", { query: { materialId } })
          .then((grades) => {
            this.setState({ getPeriods: grades });
          })
          .catch((error) => console.error("Error:", error));
      }

      getLessonsByStage = (schoolStagesId,materialId , periodId) => {
        // Get Lessons From API
        apiFetch("/lessons", { query: { materialId, schoolStageId: schoolStagesId, id: periodId } })
          .then((fetchLessons) => {
            this.setState({ getLessons: fetchLessons });
          })
          .catch((error) => console.error("Error:", error));
      };

    componentDidMount () {
        var _this = this;

        document.title = properties.site_name + " | تعديل إمتحان";

        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        // Get Grades From API
        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
        .then(grades => {
            this.setState({getSchoolStages: grades});
        })
        .catch(error => console.error('Error:', error));

        // Get Exam From API
        apiFetch(`/exams/${this.state.examID}`)
        .then(fetchExam => {
            var temp = [];

            for (var q = 0; q < fetchExam.questions.length; q++) {
                var getAnswers = [];

                for (var a = 0; a < fetchExam.questions[q].answers.length; a++) {
                    getAnswers.push({
                        answerID: fetchExam.questions[q].answers[a].id,
                        id: fetchExam.questions[q].answers[a].id,
                        correct: fetchExam.questions[q].answers[a].correct ? 1 : 0,
                        note: fetchExam.questions[q].answers[a].reason,
                        text: fetchExam.questions[q].answers[a].answer,
                        parent: fetchExam.questions[q].id,
                        answerImage:fetchExam.questions[q].answers[a].img
                    });
                }

                temp.push(
                    {
                        id: fetchExam.questions[q].id,
                        qID: fetchExam.questions[q].id,
                        question: fetchExam.questions[q].questionName,
                        type: fetchExam.questions[q].questionType,
                        score: fetchExam.questions[q].questionDegree,
                        questionImage:fetchExam.questions[q].img,
                        answers: getAnswers
                    }
                );
            }
            this.getMaterialByStage(fetchExam.schoolStagesId)
            this.getPeriod(fetchExam.materialId)
            this.getLessonsByStage(fetchExam.schoolStagesId,fetchExam.materialId ,fetchExam. periodId );
            this.setState({
                examName: fetchExam.examName,
                timer:fetchExam.timer,
                selectedStage: (fetchExam.schoolStagesId) ? fetchExam.schoolStagesId : null,
                selectedStatus: fetchExam.active,
                selectedLesson: (fetchExam.lessonId) ? fetchExam.lessonId : null,
                selectedMonth: (fetchExam.monthId) ? fetchExam.monthId : null,
                selectedMaterial: (fetchExam.materialId) ? fetchExam.materialId : null,
                selectedPeriod: (fetchExam.periodId) ? fetchExam.periodId : null,
                selectedType: fetchExam.examType,
                getAnswers: (fetchExam.questions.length) ? fetchExam.questions[0].id : 1,
                questions: (temp.length) ? temp : this.state.questions,

            });
        })
        .catch(error => {
            console.error('Error:', error)

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });
    }
    
    render() {
        let materialList = this.state.getMaterialsList.map((period) => {
            return { label: period.name, value: period.id };
          });
      
          let periods = this.state.getPeriods.map((period) => {
            return { label: period.name, value: period.id };
          });
      
        const months = [
            {label: 'يناير', value: 1},
            {label: 'فبراير', value: 2},
            {label: 'مارس', value: 3},
            {label: 'إبريل', value: 4},
            {label: 'مايو', value: 5},
            {label: 'يونيو', value: 6},
            {label: 'يوليو', value: 7},
            {label: 'أغسطس', value: 8},
            {label: 'سبتمبر', value: 9},
            {label: 'أكتوبر', value: 10},
            {label: 'نوفمبر', value: 11},
            {label: 'ديسمبر', value: 12}
        ];
        let type = [
            { label: "يدوي", value: 1 },
            { label: "ألي", value: 0 },
          ];
        
        let questionType = [
            {label: 'إختياري', value: 0},
            {label: 'أكمل', value: 1}
        ];
        
        let status = [
            {label: 'مفعّل', value: 1},
            {label: 'غير مفعّل', value: 0}
        ];

        let getQuestionIndex = this.state.questions.findIndex((ques => ques.id === this.state.getAnswers));
        let answersInputs = this.state.questions[getQuestionIndex].answers.map((answer, i) => 
            <div className="p-grid p-fluid answer p-mb-3" key={answer.answerID}>
                <div className="p-col-12 p-md-5 p-p-2">
                    <div className="p-inputgroup">
                        <span className="p-float-label">
                            <InputText value={answer.text} id={`answer-${answer.answerID}`} name={`answer-${answer.answerID}`} type="text" onChange={(e) => {
                                let temp = this.state.questions;
                                var a = temp[getQuestionIndex].answers.findIndex((ans => ans.answerID === answer.answerID));
                                temp[getQuestionIndex].answers[a].text = e.target.value;

                                this.setState({
                                    questions: temp
                                })
                            }} />
                            
                            <label htmlFor={`answer-${answer.answerID}`}>إجابة {i+1}</label>
                        </span>
                    </div>
                </div>
                <div className="p-col-12 p-md-2 p-d-flex p-ai-center p-jc-center">
                    <div className="p-field-radiobutton p-mb-0">
                        <RadioButton inputId={`correct-${answer.answerID}`} value={1} name="correct" id={`correct-${answer.answerID}`} checked={answer.correct === 1} onChange={(e) => {
                            let temp = this.state.questions;
                            var a = temp[getQuestionIndex].answers.findIndex((ans => ans.answerID === answer.answerID));
                            for (var c = 0; c < temp[getQuestionIndex].answers.length; c++) {
                                temp[getQuestionIndex].answers[c].correct = 0;
                            }

                            temp[getQuestionIndex].answers[a].correct = e.value;

                            this.setState({
                                questions: temp
                            })
                        }} />
                        <label className="right-answer" htmlFor={`correct-${answer.answerID}`}>الإجابة الصحيحة</label>
                    </div>
                </div>
                <div className="p-col-12 p-md-3 p-p-2">
                    <div className="p-inputgroup">
                        <span className="p-float-label">
                            <InputText value={answer.note} id={`note-${answer.answerID}`} name={`note-${answer.answerID}`} type="text" onChange={(e) => {
                                let temp = this.state.questions;
                                var a = temp[getQuestionIndex].answers.findIndex((ans => ans.answerID === answer.answerID));
                                temp[getQuestionIndex].answers[a].note = e.target.value;

                                this.setState({
                                    questions: temp
                                })
                            }} />
                            
                            <label htmlFor={`note-${answer.answerID}`}>ملحوظة</label>
                        </span>
                    </div>
                </div>
                <div className="p-col-12 p-md-2 p-p-2">
                    <Button icon="pi pi-trash" className="p-button-danger p-button-rounded"


                    onClick={() => {
                        var temp = this.state.questions;
                        temp[getQuestionIndex].answers.splice(i, 1);

                        this.setState({
                            questions: temp
                        })

                    }} />
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
        );

        let questionsInputs = this.state.questions.map((qn, index) => {            
            return (
                <div className="p-grid p-fluid question p-mb-3" key={index}>
                <div className="p-col-12 p-md-6 p-p-2">
                    <div className="p-inputgroup">
                        <span className="p-float-label">
                            <InputText value={qn.question} id={`question-${index}`} name={`question-${index}`} type="text" onChange={(e) => {
                                let temp = this.state.questions;
                                var q = temp.findIndex((ques => ques.id === qn.id));
                                temp[q].question = e.target.value;

                                this.setState({
                                    questions: temp
                                })
                            }} />
                            
                            <label htmlFor={`question-${qn.id}`}>سؤال {index+1}</label>
                        </span>
                    </div>
                </div>
                <div className="p-col-12 p-md-2">
                    <div className="p-inputgroup">
                        <span className="p-float-label">
                            <Dropdown value={qn.type} options={questionType} placeholder="نوع السؤال" onChange={(e) => {
                                let temp = this.state.questions
                                var q = temp.findIndex((ques => ques.id === qn.id));
                                temp[q].type = e.value;

                                this.setState({
                                    questions: temp
                                })
                            }} optionLabel="label" showClear />                                            
                        </span>
                    </div>
                </div>

                <div className="p-col-12 p-md-2">
                    <div className="p-inputgroup">
                        <span className="p-float-label">
                            <InputText id={`score-${index}`} value={qn.score} name={`score-${index}`} type="number" onChange={(e) => {
                                let temp = this.state.questions;
                                var q = temp.findIndex((ques => ques.id === qn.id));
                                temp[q].score = e.target.value;

                                this.setState({
                                    questions: temp
                                })
                            }} />
                            
                            <label htmlFor={`score-${index}`}>الدرجة</label>
                        </span>
                    </div>
                </div>
                <div className="p-col-12 p-md-2 p-p-2">
                    <Button icon="pi pi-trash" className="p-button-danger p-button-rounded"
                   disabled={this.state.questions.answers==[]}
                    onClick={() => {
                        var temp = this.state.questions;
                        temp.splice(index, 1);

                        this.setState({
                            questions: temp
                        })
                    }} />
                    <Button icon="pi pi-list" className="p-button-rounded p-button-success p-mr-2" onClick={() => {
                        this.setState({
                            getAnswers: qn.id
                        });
                    }} />
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
             
              <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${ qn.questionImage}`} alt={qn.text} onClick={(e)=>e.target.requestFullscreen()}/>

            </div>
          </div>
            </div>
            );
        });

        let gradesList = this.state.getSchoolStages.map(grade => {
            return {
                label: grade.name,
                value: grade.id
            };
        });
        
        let lessonsList = this.state.getLessons.map(lessons => {
            return {
                label: lessons.lessonName,
                value: lessons.id
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
                        correct: this.state.questions[q].answers[a].correct === 1,
                        reason: this.state.questions[q].answers[a].note,
                        img: this.state.questions[q].answers[a].answerImage
                    });
                }

                finalQuestionsForm.push(
                    {
                        questionName: this.state.questions[q].question,
                        questionType: this.state.questions[q].type,
                        questionDegree: this.state.questions[q].score,
                        img: this.state.questions[q].questionImage,
                        answers: finalAnswers
                    }
                );
            }

            // Send the data to the server
            apiFetch(`/exams/${this.state.examID}`, {
                method: "PUT",
                body: {
                    examName: this.state.examName,
                    degree: total,
                    active: this.state.selectedStatus,
                    schoolStagesId: this.state.selectedStage,
                    lessonId: this.state.selectedLesson,
                    monthId: this.state.selectedMonth,
                    timer: this.state.timer,
                    periodId: this.state.selectedPeriod,
                    examType: this.state.selectedType,
                    branchId: this.state.branchID,
                    materialId: this.state.selectedMaterial,
                    questions: finalQuestionsForm,
                },
            })
            .then(() => {
               _this.setState({
                   buttonText: "حفظ التعديلات",
                   buttonColor: "",
               });

               _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تم حفظ التعديلات بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)

                _this.setState({
                   buttonText: "حفظ التعديلات",
                   buttonColor: ""
               });

               _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        }

        return (
            <>
                <Header pageName={`تعديل "${this.state.examName}"`} />

                <div className="content">
                    <Toast ref={this.toast}></Toast>

                    <div className="container">
                        <Card title={`تعديل إمتحان`}>
                            <form onSubmit={(e) => e.preventDefault()} action="#" method="POST">
                                <div className="p-grid p-fluid">
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                 <InputText id="exam-name" value={this.state.examName} onChange={(e) => this.setState({examName: e.target.value})} name="exam-name" type="text" />    

                                                 <label htmlFor="exam-name">إسم الإمتحان</label>                                      
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.selectedStage} options={gradesList} placeholder="إختر المرحلة الدراسية"
                                                 onChange={(e) => {this.setState({selectedStage: e.value});  this.getLessonsByStage(
                                                    e.value,
                                                    this.state.selectedMaterial,
                                                    this.state.selectedPeriod
                                                  ); this.getMaterialByStage(e.value)}}
                                                  optionLabel="label" filter showClear filterBy="label" />                                            
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
                              ,this.getPeriod(e.value), 
                              
                              this.getLessonsByStage(
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
                        <Dropdown
                          value={this.state.selectedPeriod}
                          options={periods}
                          placeholder="  إختر الفترة "
                          onChange={(e) =>
                            this.setState({ selectedPeriod: e.value },
                              this.getLessonsByStage(
                                this.state.selectedStage,
                                this.state.selectedMaterial,
                                e.value
                              ),)
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
                                                <Dropdown value={this.state.selectedLesson} options={lessonsList} placeholder="إختر الدرس" onChange={(e) => this.setState({selectedLesson: e.value})} optionLabel="label" filter showClear filterBy="label" />                                            
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4 p-mt-2">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.selectedStatus} options={status} placeholder="حالة الإمتحان" onChange={(e) => this.setState({selectedStatus: e.value})} optionLabel="label" />                                            
                                            </span>
                                        </div>
                                    </div>
                                    {/* <div className="p-col-12 p-md-4 p-mt-2">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.selectedMonth} options={months} placeholder="الشهر" onChange={(e) => this.setState({selectedMonth: e.value})} optionLabel="label" />                                            
                                            </span>
                                        </div>
                                    </div> */}
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                            <InputText id="exam-name" value={this.state.timer} onChange={(e) => this.setState({timer: e.target.value})} name="Timer" type="number" />    

                                             <label htmlFor="exam-name">مدة الامتحان بالدقايق</label>                                      
                                            </span>
                                        </div>
                                    </div>
                             
                  <div className="p-col-12 p-md-4">
                    <div className="p-inputgroup">
                      <span className="p-float-label">
                        <Dropdown
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
                                   
                                 
                                </div>
                            </form>
                        </Card>

                        <Card title="الأسئلة" className="p-mt-5 questions">
                            <Button label="إضافة سؤال" icon="pi pi-plus" className="p-mb-5 add-question" onClick={() => {

                                this.setState({
                                    numQuestions: this.state.numQuestions + 1,
                                }, () => {
                                    let temp = this.state.questions;

                                    temp.push({
                                        id: this.state.numQuestions, 
                                        question: null,
                                        type: null,
                                        score: 0,
                                        answers: [
                                            {
                                                answerID: 1,
                                                id: null,
                                                text: "",
                                                note: "",
                                                correct: null,
                                                parent: this.state.numQuestions
                                            }
                                        ]
                                    })

                                    this.setState({
                                        questions: temp
                                    });
                                }) 
                            }} />

                            {questionsInputs}
                        </Card>
                        
                        <Card title="الإجابات" className="p-mt-5 questions">
                            <Button label="إضافة إجابة" icon="pi pi-plus" className="p-mb-5 add-question" onClick={(e) => {
                                this.setState({
                                    numAnswers: this.state.numAnswers + 1
                                }, () => {
                                    let temp = this.state.questions;

                                    temp[getQuestionIndex].answers.push({
                                        answerID: this.state.numAnswers,
                                        text: null,
                                        note: null,
                                        parent: this.state.getAnswers
                                    })

                                    this.setState({
                                        questions: temp
                                    });
                                })
                            }} />
                            
                            {answersInputs}
                        </Card>

                        <Card className="p-mt-5">
                             <div className="p-col-12 p-md-12 p-mt-2">
                                <div className="p-inputgroup">
                                    <span className="p-float-label">
                                        <Button onClick={addExam} className={`add-button ${this.state.buttonColor}`} label={this.state.buttonText}/>
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

export default EditExam;