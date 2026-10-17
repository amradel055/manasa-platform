import React, { Component, createRef } from "react";
import ReactDOM from "react-dom";

// CSS
import "./Lessons.scss";
import "primeflex/primeflex.css";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch, getCurrentUser } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
// Our dropdown: a wrapper that passes every prop through to PrimeReact's,
// so the value, the options and the handler below are unchanged.
import Select from "../../components/Field/Select";
import "../../components/Field/Form.scss";
import { InputTextarea } from "primereact/inputtextarea";
import { FileUpload } from "primereact/fileupload";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import { Link } from "react-router-dom/cjs/react-router-dom.min";

// Properties
const properties = require("../../properties.json");

class AddLessons extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    // Submit Button
    buttonText: "إضافة حصة",
    buttonColor: "",

    // The panel header's slot, when this form is open inside one. Null on
    // the standalone route, where the button stays in its own bar.
    headerSlot: null,

    // Form Select Boxes
    type: null,
    period: null,
    grade: null,
    material: null,
    // Set The Video Type
    setVideoType: null,

    getPeriods: [],
    getMaterialsList: [],
    // All Grades
    allGrades: [],
    reply: null,
    currentReplyID: null,
    thumbs: [],
    voices: [],
    // Lesson Data
    formContent: {
      branchId: properties.branch_id,
      companyId: properties.company_id,
      lessonName: null,
      lessonDescription: null,
      lessonType: null,
      schoolId: null,
      periodId: null,
      materialId: null,
  
      images: [],
      schoolId:null,
      // thumbs:[],
      documents: [],
    
      voiceLink: null,
      lessonVoiceList:[],
      videoLinks:[],
      youtubeLinks: [],
      vimeoLinks: [],
      videoLink:null,

      teacherId: null,
    },
    
    videos:{
      lessonVideoList:[],
      youtubeLink: [],
      vimeoLink: [],
    },
    youtubeLink: null,
      vimeoLink: null,
  };

  changeVideoType = (e) => {
    this.setState({
      setVideoType: e.value,
      
        // ...this.state.videos,
        // youtubeLink: [],
        // vimeoLink: [],
        // lessonVideoList:[]
      
    });
  };

  getPeriod(materialId) {
    apiFetch("/periods/by-material", { query: { materialId } })
      .then((grades) => {
        this.setState({ getPeriods: grades });
      })
      .catch((error) => console.error("Error:", error));
  }

  getMaterialByStage = (schoolStagesId) => {
    apiFetch("/lookup/materials/by-stage", { query: { stageId: schoolStagesId } })
      .then((fetchMaterials) => {
        this.setState({
          getMaterialsList: fetchMaterials,
        });
      })
      .catch((error) => console.error("Error:", error));
  };

  componentDidMount() {
    document.title = properties.site_name + " | إضافة حصة جديدة";

    // Opened inside a slide-over? Then the save button belongs on the
    // panel's header row, not in a second bar underneath it. It is moved
    // there with a portal, so it stays in THIS component's tree and its
    // label and colour are still driven from this state -- and the `form`
    // attribute on it is what keeps a native submit, and therefore the
    // browser's own `required` checking, working from outside the <form>.
    // The whole tree is in the DOM before any componentDidMount runs, so
    // the slot is already there if there is one.
    const slot = document.querySelector(".m-panel-slot");
    if (slot) this.setState({ headerSlot: slot });

    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
      .then((grades) => {
        this.setState({ allGrades: grades });
      })
      .catch((error) => console.error("Error:", error));
  }

  render() {
    const typeList = [
      { label: "مدفوع", value: 0 },
      { label: "مجاني", value: 1 },
    ];

    const videoType = [
        {label: 'Youtube', value: 0},
        {label: 'سيرفر', value: 1},
        {label: 'Vimeo', value: 2}
    ];
    let materialList = this.state.getMaterialsList.map((period) => {
      return { label: period.name, value: period.id };
    });

    let periods = this.state.getPeriods.map((period) => {
      return { label: period.name, value: period.id };
    });

    const grades = this.state.allGrades.map((grade) => {
      return {
        label: grade.name,
        value: grade.id,
      };
    });

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

    const onUploadThumb = (e) => {
      let res = JSON.parse(e.xhr.response);
      console.log(res)
      this.setState({
        // formContent: {
        //   ...this.state.formContent,
        //   thumbs: res.thumb_url,
        // },
        thumbs: this.state.thumbs.concat(res.thumb_url.split('/').pop()),
      });
      console.log(this.state.thumbs);
      this.toast.current.show({
        severity: "info",
        summary: "تم بنجاح",
        detail: "تم رفع الصورة المصغرة",
      });
    };

    // const onUploadThumb = (e) => {
    //   let res = JSON.parse(e.xhr.response);
    //   let thumbUrls = this.state.thumbs.concat(res.thumb_urls);
    //   this.setState({
    //     thumbs: thumbUrls,
    //   });
    //   console.log(this.state.thumbs);
    //   this.toast.current.show({
    //     severity: "info",
    //     summary: "تم بنجاح",
    //     detail: "تم رفع الصور المصغرة",
    //   });
    // };

    const onUploadPDFs = (e) => {
      let res = JSON.parse(e.xhr.response);
      console.log(res)
      this.setState({
        formContent: {
          ...this.state.formContent,
          documents:  this.state.formContent.documents.concat(res.documents[0].split('/').pop()),
        },
      });
      console.log( this.state.formContent.documents)
      this.toast.current.show({
        severity: "info",
        summary: "تم بنجاح",
        detail: "تم رفع المذكرات",
      });
    };

    /**
     * Custom Upload Handler
     * @param null
     */
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
                _this.setState(
                  {
                    // lessonVideoList:this.state.formContent.lessonVoiceList.concat(res.video_url.split('/').pop()),

                    formContent: {
                    ...this.state.formContent,
                    // ...this.state.videos,
                    videoLinks: this.state.formContent.videoLinks.concat(res.video_url.split('/').pop()),
                }});
                _this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الفيديو'});
            }).catch(error => {
                console.error('Error:', error)
            });

        } else {
            _this.toast.current.show({severity: 'error', summary: 'تحذير', detail: 'لا يمكنك رفع أكثر من فيديو واحد'});
        }
    }
    let uploadVideoYoutube = (link) => {
      let _this = this;
      // let videosArray = uploadedFiles.files;

      if (link !== "") 

         {
              _this.setState({formContent: {
                  // ...this.state.formContent,
                  ...this.state.formContent,
                  youtubeLinks: this.state.formContent.youtubeLinks.concat(link),
              }});
              // this.state.videos.youtubeLink=null
              console.log( this.state.formContent)
              _this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم اضافه الفيديو'});
          
          

      } else {
          _this.toast.current.show({severity: 'error', summary: 'تحذير', detail: 'لا يمكنك الرفع '});
      }
  }
  let uploadVideoViemo = (link) => {
    let _this = this;
    // let videosArray = uploadedFiles.files;

    if (link !== "") 

       {
            _this.setState({formContent: {
                // ...this.state.formContent,
                ...this.state.formContent,
                vimeoLinks: this.state.formContent.vimeoLinks.concat(link),
            }});
            // this.state.videos.youtubeLink=null
            console.log( this.state.videos)
            _this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم اضافه الفيديو'});
        
        

    } else {
        _this.toast.current.show({severity: 'error', summary: 'تحذير', detail: 'لا يمكنك الرفع '});
    }
}
    var addLesson = (e) => {
      var _this = this;
      e.preventDefault();
      var _data = JSON.parse(window.localStorage.getItem("currentTeacher"));
     
      this.setState({
        buttonText: "جاري الإضافة ...",
        buttonColor: "loading",
      });

      this.setState({
        formContent: {
          ...this.state.formContent,
          images: this.state.thumbs,
        },
      }, () => {
        apiFetch("/lessons", {
          method: "POST",
          body: {
            lessonName: this.state.formContent.lessonName,
            lessonType: this.state.formContent.lessonType,
            lessonDescription: this.state.formContent.lessonDescription,
            branchId: this.state.formContent.branchId,
            schoolStagesGuideId: this.state.formContent.schoolId,
            periodId: this.state.formContent.periodId,
            materialId: this.state.formContent.materialId,
            images: this.state.formContent.images,
            voices: this.state.formContent.lessonVoiceList,
            videoLinks: this.state.formContent.videoLinks,
            vimeoLinks: this.state.formContent.vimeoLinks,
            youtubeLinks: this.state.formContent.youtubeLinks,
          },
        })
          .then(() => {
            _this.setState({
              buttonText: "إضافة حصة",
              buttonColor: "",
            });
            _this.toast.current.show({
              severity: "success",
              summary: "تم بنجاح",
              detail: "تم الحفظ بنجاح",
            });
          })
          .catch((error) => {
            console.error("Error:", error);

            _this.setState({
              buttonText: "إضافة حصة",
              buttonColor: "",
            });

            _this.toast.current.show({
              severity: "error",
              summary: "لم تتم العملية",
              detail: error.message || "خطأ في الإتصال",
            });
          });
      });
    };

    // One element, rendered in one of two places. `form` is what lets it
    // submit #add-lesson-form from wherever it ends up; PrimeReact passes
    // any prop it does not recognise straight on to the <button>.
    const saveButton = (
      <Button
        type="submit"
        form="add-lesson-form"
        className={`add-button ${this.state.buttonColor}`}
        label={this.state.buttonText}
      />
    );

    return (
      <div className="add-lesson-page m-form">
        <Header pageName="إضافة حصة" />

        <div className="content">
          <Toast ref={this.toast}></Toast>

          <div className="container">
            <div className="card">
              <form id="add-lesson-form" onSubmit={addLesson} action="#" method="POST">

                {/* In a panel the button goes up into the header row; on the
                    standalone page it keeps its own bar. Same element either
                    way, with the same state behind it. */}
                {this.state.headerSlot ? (
                  ReactDOM.createPortal(saveButton, this.state.headerSlot)
                ) : (
                  <div className="m-actions">
                    <span className="m-actions-hint">لن تُحفظ الحصة حتى تضغط الزر</span>
                    {saveButton}
                  </div>
                )}

                {/* ---------------- 1. the lesson itself ---------------- */}
                <section className="m-sec">
                  <h3>
                    <span className="m-sec-n">١</span>
                    معلومات الحصة
                    <small>الحقول المطلوبة معلّمة بـ *</small>
                  </h3>

                  <div className="m-grid">
                    <div className="m-fld">
                      <label htmlFor="lesson-name">
                        إسم الحصة <span className="req">*</span>
                      </label>
                      <InputText
                        id="lesson-name"
                        required
                        value={this.state.formContent.lessonName}
                        name="lesson-name"
                        type="text"
                        placeholder="مثال: الوحدة الثالثة — المعادلات"
                        onChange={(e) => {
                          this.setState({
                            formContent: {
                              ...this.state.formContent,
                              lessonName: e.target.value,
                            },
                          });
                        }}
                      />
                    </div>

                    <div className="m-fld">
                      <label htmlFor="type">
                        نوع الحصة <span className="req">*</span>
                      </label>
                      <Select
                        inputId="type"
                        value={this.state.type}
                        options={typeList}
                        required
                        onChange={(e) =>
                          this.setState({
                            type: e.value,
                            formContent: {
                              ...this.state.formContent,
                              lessonType: e.value,
                            },
                          })
                        }
                        name="type"
                        placeholder="نوع الحصة"
                      />
                    </div>

                    <div className="m-fld">
                      <label htmlFor="grade">
                        المرحلة الدراسية <span className="req">*</span>
                      </label>
                      <Select
                        inputId="grade"
                        value={this.state.grade}
                        options={grades}
                        required
                        onChange={ (e) =>
                          this.setState({
                            grade: e.value,
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
                    </div>

                    <div className="m-fld">
                      <label htmlFor="subject">
                        إختر المادة <span className="req">*</span>
                      </label>
                      <Select
                        inputId="subject"
                        value={this.state.material}
                        options={materialList}
                        required
                        onChange={(e) =>
                          this.setState({
                            material: e.value,
                            formContent: {
                              ...this.state.formContent,
                              materialId: e.value,
                            },
                          },
                          this.getPeriod(e.value )
                          )
                        }
                        name="subject"
                        placeholder="إختر المادة"
                      />
                    </div>

                    <div className="m-fld">
                      <label htmlFor="months">
                        الفتره <span className="req">*</span>
                      </label>
                      <Select
                        inputId="months"
                        value={this.state.period}
                        options={periods}
                        required
                        onChange={(e) =>
                          this.setState({
                            period: e.value,
                            formContent: {
                              ...this.state.formContent,
                              periodId: e.value,
                            },
                          })
                        }
                        name="months"
                        placeholder="الفتره"
                      />
                    </div>

                    <div className="m-fld m-wide">
                      <label htmlFor="lesson-description">
                        وصف الحصة <span className="req">*</span>
                      </label>
                      <InputTextarea
                        rows={5}
                        required
                        value={this.state.formContent.lessonDescription}
                        placeholder="اكتب شرحاً مختصراً يظهر لطلابك قبل فتح الحصة"
                        onChange={(e) => {
                          this.setState({
                            formContent: {
                              ...this.state.formContent,
                              lessonDescription: e.target.value,
                            },
                          });
                        }}
                        name="lesson-description"
                        id="lesson-description"
                      />
                    </div>
                  </div>
                </section>

                {/* ---------------- 2. the video ---------------- */}
                <section className="m-sec">
                  <h3>
                    <span className="m-sec-n">٢</span>
                    الفيديو
                    <small>اختر المصدر ثم أضف الرابط أو ارفع الملف</small>
                  </h3>

                  <div className="m-grid">
                    <div className="m-fld">
                      <label htmlFor="vedioes">نوع الفيديو</label>
                      <Select
                        inputId="vedioes"
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
                    </div>

                    {/* Left exactly as it was found: an external link to
                        bobbyhadz.com wrapped around a button with no handler.
                        It looks like development debris rather than anything
                        a teacher should be sent to -- worth deleting, but
                        that is a call to make deliberately, not in a restyle. */}
                    <div className="m-fld">
                      <label>&nbsp;</label>
                      <a href="https://bobbyhadz.com" target="_blank" rel="noopener noreferrer">
                        <Button
                          className="p-col-20 p-mr-1 m-3"
                          label="فيديو الشرح"
                          // icon="pi pi-power-off"
                          // onClick={}
                        />
                      </a>
                    </div>

                    {(this.state.setVideoType === 0) ? (
                      <div className="m-fld m-wide">
                        <label htmlFor="video-link">لينك Youtube</label>
                        <div className="m-inline">
                          <InputText id="video-link" value={this.state.youtubeLink} name="video-link" type="text" placeholder="https://youtube.com/watch?v=..." onChange={(e) => {this.setState({


                              // ...this.state.videos,
                              // lessonVideoList: this.state.videos.lessonVideoList.concat(res.video_url.split('/').pop()),

                              youtubeLink:e.target.value,

                            }   )}} />
                          <Button    onClick={()=>uploadVideoYoutube(this.state.youtubeLink)}>اضافه</Button>
                        </div>
                      </div>
                    ): ""}

                    {(this.state.setVideoType === 1) ? (
                      <div className="m-drop">
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
                      </div>
                    ): ""}

                    {(this.state.setVideoType === 2) ? (
                      <div className="m-fld m-wide">
                        <label htmlFor="video-link">لينك Vimeo</label>
                        <div className="m-inline">
                          <InputText id="video-link" value={this.state.vimeoLink} name="video-link" type="text" placeholder="https://vimeo.com/..." onChange={(e) => {this.setState({


                              // ...this.state.videos,
                              // lessonVideoList: this.state.videos.lessonVideoList.concat(res.video_url.split('/').pop()),

                              vimeoLink:e.target.value,

                            }   )}} />
                          <Button    onClick={()=>uploadVideoViemo(this.state.vimeoLink)}>اضافه</Button>
                        </div>
                      </div>
                    ): ""}

                    <div className="m-files">
                      {this.state.formContent.videoLinks.map((object, i) =>

                        <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank" key={i}>{`${object}`}</a>
                      )}

                      {this.state.formContent.youtubeLinks.map((object, i) =>
                        <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank" key={i}>{`${object}`}</a>
                      )}

                      {this.state.formContent.vimeoLinks.map((object, i) =>
                        <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank" key={i}>{`${object}`}</a>
                      )}
                    </div>
                  </div>
                </section>

                {/* ---------------- 3. everything else ---------------- */}
                <section className="m-sec">
                  <h3>
                    <span className="m-sec-n">٣</span>
                    الصوت والصور والمذكرات
                    <small>اختياري</small>
                  </h3>

                  <div className="m-grid">
                    <div className="m-drop">
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
                    </div>

                    <div className="m-files">
                      {this.state.formContent.lessonVoiceList.map((object, i) =>

                        <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank" key={i}>{`${object}`}</a>
                      )}
                    </div>

                    <div className="m-drop">
                      <Toast ref={this.toast}></Toast>
                      <FileUpload
                        name="thumb"
                        onUpload={onUploadThumb}
                        invalidFileSizeMessageSummary="حجم الفيديو كبير:"
                        invalidFileSizeMessageDetail="أقصى حجم للصورة هو (50 MB)"
                        url={`${properties.uploads_path}/upload-thumb.php`}
                        customUpload={false}
                        multiple={true}
                        accept="image/*"
                        maxFileSize={500000000}
                        chooseLabel="إختيار صورة مصغرة"
                        uploadLabel="رفع الصورة"
                        cancelLabel="إلغاء"
                        emptyTemplate={
                          <p className="p-m-0">
                            إسحب الصورة هنا ليبدأ التحميل
                          </p>
                        }
                      />
                    </div>

                    <div className="m-files">
                      {this.state.thumbs.map((object, i) =>
                        <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${object}`} alt={object} key={i} onClick={(e)=>e.target.requestFullscreen()}/>
                      )}
                    </div>

                    <div className="m-drop">
                      <Toast ref={this.toast}></Toast>
                      <FileUpload
                        name="LessonPDFs[]"
                        onUpload={onUploadPDFs}
                        invalidFileSizeMessageSummary="حجم الفيديو كبير:"
                        invalidFileSizeMessageDetail="أقصى حجم للمذكرة هو (100 MB)"
                        url={`${properties.uploads_path}/upload-pdf.php`}
                        customUpload={false}
                        multiple={true}
                        accept=".doc, .docx, .pdf, .ppt, .pptx"
                        maxFileSize={100000000}
                        chooseLabel="إختيار المذكرات"
                        uploadLabel="رفع المذكرات"
                        cancelLabel="إلغاء"
                        emptyTemplate={
                          <p className="p-m-0">
                            إسحب المذكرات هنا ليبدأ التحميل
                          </p>
                        }
                      />
                    </div>

                    <div className="m-files">
                      {this.state.formContent.documents.map((object, i) =>

                        <a href={`${properties.uploads_path}/documents/${object}`} download target="_blank" key={i}>{`${object}`}</a>
                      )}
                    </div>
                  </div>
                </section>

              </form>
            </div>
          </div>
        </div>

      </div>
    );
  }
}

export default AddLessons;
