import React, {Component, createRef} from 'react';

// CSS
import './Lessons.scss';
import 'primeflex/primeflex.css';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';

// Components
import Header from '../../components/Header/Header';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Dropdown } from 'primereact/dropdown';
import { InputTextarea } from 'primereact/inputtextarea';
import { FileUpload } from 'primereact/fileupload';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { Fieldset } from 'primereact/fieldset';

// Properties
const properties = require('../../properties.json');

class EditLessons extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        // Teacher Identifier
        branchID: properties.branch_id,
        companyID: properties.company_id,

        // Submit Button
        buttonText: "حفظ التعديلات",
        buttonColor: "",

        // Get Current Teacher
        currentTeacher: JSON.parse(localStorage.getItem("currentTeacher")).data,
        setVideoType: null,

        // Form Select Boxes
        type: null,
        period: null,
        grade: null,
        getMaterialsList : [],
        // Lesson ID
        lessonID: (this.props.match.params.id) ? parseInt(this.props.match.params.id) : 0,

        // Set The Video Type
        setVideoType: null,

        // All Grades
        allGrades: [],
        reply: null,
        currentReplyID: null,
        
        material : null , 
 
        getPeriods: [],
        // Get Current Files
        currentDocument: [],

        // All Comments
        comments: [],

        resend: false,
        thumbs:[],
        images:[],
        imageString:null,

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
      voiceLink: null,
      lessonVoiceList:[],
      lessonVideosList:[],
      videoLinks:[],
      youtubeLinks: [],
      vimeoLinks: [],

      images: [],
      schoolId:null,
      // thumbs:[],
      documents: [],
      youtubeLink: null,
      vimeoLink: null,
      teacherId: null,
        }
    }

    getComments = () => {
        // Get Comments From API
        fetch(properties.api_path + "/comments/allComments", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                lessonId: this.state.lessonID
            })
        })
        .then(res => {return res.json()})
        .then(fetchComments => {
            this.setState({
                comments: [...fetchComments]
            });
        })
    }

    deleteComment = (id) => {

        var _this = this;

        // Confirm Deletion
        var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

        if (con) {
            // Delete Media By ID
            fetch(properties.api_path + "/comments/deleteComments", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    id: id
                })
            })
            .then(res => {return res.json()})
            .then(deleteCommentByID => {
                this.setState({resend: true});
                _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الحذف بنجاح'});
            });
        }
    }

    changeVideoType = (e) => {
        this.setState({
            setVideoType: e.value,
            formContent: {
                ...this.state.formContent,
                youtubeLink: null,
                vimeoLink: null,
                // voiceLink: null
            }
        })
    }

    sendReply = () => {
        var _this = this;
        
        fetch(properties.api_path + "/comments/saveComments", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json; charset=utf-8',
            },
            body: JSON.stringify({
                companyId: this.state.companyID,
                branchId: this.state.branchID,
                lessonId: this.state.lessonID,
                studentId: null,
                comment: this.state.reply,
                parent: this.state.currentReplyID
            })
        })
        .then(res => {return res.json()})
        .then(done => {
            if (done.msgType === "success") {
                this.setState({
                    resend: true,
                    currentReplyID: null,
                    reply: null
                });

                _this.toast.current.show({severity: 'success', summary: 'تم الحفظ بنجاح', detail: "تم إضافة التعليق"});
            } else {
                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: "لم يتم حفظ التعليق"});
            }
        })
        .catch(error => {
            console.error('Error:', error)

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
        });
    }
    getPeriod(materialId){
        var data =JSON.parse(window.localStorage.getItem('currentTeacher'));
        fetch(properties.api_path + "/periods/findPeriod", 
        {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                // teacherId: data["teacherId"]
                materialId: materialId
            })
        })
        .then(res => {return res.json()})
        .then(grades => {
            console.log(grades);
            this.setState({getPeriods : grades});
        })
     }
     getMaterial = (schoolStagesId) => {
        // Get Periods From API
        console.log(schoolStagesId);
        // var data = JSON.parse(window.localStorage.getItem("currentTeacher"));
        fetch(properties.api_path + "/lockup/materialListbystage", {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
          
            id: schoolStagesId,
          }),
        })
          .then((res) => {
            return res.json();
          })
          .then((fetchMaterials) => {
            this.setState({
              getMaterialsList: fetchMaterials,
            });
            
          });
      };
   
    //  getMaterial = () => {
    //     // Get Periods From API
    //     var data = JSON.parse(window.localStorage.getItem('currentTeacher'));
    //     fetch(properties.api_path + "/lockup/materialList", {
    //         method: "POST",
    //         headers: {
    //             'Accept': 'application/json',
    //             'Content-Type': 'application/json',
    //         },
    //         body: JSON.stringify({
    //             teacherId: data["teacherId"],
    //             // branchId: this.state.branchID
    //         })
    //     })
    //     .then(res => {return res.json()})
    //     .then(fetchMaterials => {
    //         this.setState({
    //             getMaterialsList : fetchMaterials
    //         });
    //     });

    // }

    componentDidMount() {
        document.title = properties.site_name + " | تعديل حصة";

        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        this.getComments();
        // this.getPeriod();
        // this.getMaterial();
        var data = JSON.parse(window.localStorage.getItem('currentTeacher'));

        // Get Grades From API
        fetch(properties.api_path + "/lockup/levelList", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({branchId: this.state.formContent.branchId})
        })
        .then(res => {return res.json()})
        .then(grades => {
            this.setState({allGrades: grades});
        })
        // Get Lesson From API
        fetch(properties.api_path + "/lesson/teacherLessonList", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                branchId: this.state.branchID,
                companyId: this.state.companyID,
                id: this.state.lessonID,
                teacherId : data["teacherId"]
            })
        })
        .then(res => {return res.json()})
        .then(singleLesson => {
          
            let vt = 1;
            console.log(singleLesson)
            this.getMaterial(singleLesson[0].schoolId)
            this.getPeriod(singleLesson[0].materialId)

            // if (singleLesson[0].youtubeLink) {
            //     vt = 0;
            // } else if (singleLesson[0].vimeoLink) {
            //     vt = 2;
            // } else {
            //     vt = 1;
            // }

            this.setState({
                formContent: {
      
                    id: parseInt(this.props.match.params.id),
                    branchId: properties.branch_id,
                    companyId: properties.company_id,
                    lessonName: singleLesson[0].lessonName,
                    lessonDescription: singleLesson[0].lessonDescription,
                    lessonType: singleLesson[0].lessonType,
                    lessonVoiceList:singleLesson[0].lessonVoiceList,
                    lessonVideosList:singleLesson[0].lessonVideosFList,

                    periodId: singleLesson[0].periodId,
                    materialId:singleLesson[0].materialId,
                    schoolId: singleLesson[0].schoolStagesGuideId.id,
                    // videoLink: singleLesson[0].videoLink,
                    thumb: singleLesson[0].thumb,

                    documents: singleLesson[0].documents,
                    videoLinks:singleLesson[0].videoLinks,
                    youtubeLinks: singleLesson[0].youtubeLinks,
                    vimeoLinks:singleLesson[0].vimeoLinks,
                  
                    images:singleLesson[0].images
                },
                type: singleLesson[0].lessonType,
                period: singleLesson[0].periodId,
                material:singleLesson[0].materialId,
                grade: singleLesson[0].schoolStagesGuideId.id,
                setVideoType: vt,
                thumbs:singleLesson[0].images,
                // setVideoType:singleLesson[0].youtubeLink !==""?0:singleLesson[0].vimeoLink!==""?2:1
                setVideoType:1

           
            }, () => {
                // Get Free Documents By Lessons From API On Load
                fetch(properties.api_path + "/documents/allDocuments", {
                    method: "POST",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        lessonId: this.state.lessonID
                    })
                })
                .then(res => {return res.json()})
                .then(fetchDocuments => {
                    var temp = [];
                    for (var i = 0; i < fetchDocuments.length; i++) {
                        temp.push(fetchDocuments[i].link);
                    }
                    
                    this.setState({
                        formContent: {
                            ...this.state.formContent,
                            documents: temp
                        }
                    });
                });
            });
        });
    }
    

    
    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState.resend !== this.state.resend) {
            this.getComments();

            this.setState({resend: false});
        }
    }

    render() {
        const typeList = [
            {label: 'مدفوع', value: 0},
            {label: 'مجاني', value: 1}
        ];
        
        const videoType = [
            {label: 'Youtube', value: 0},
            {label: 'سيرفر', value: 1},
            {label: 'Vimeo', value: 2}
        ];
        
        let  periods = this.state.getPeriods.map(period => {
            return {label: period.name, value: period.id}});

        const finalComments = this.state.comments.map(comment => {
            return {
                id: comment.id,
                name: comment.studentName,
                profile: comment.profilePicture,
                body: comment.comment,
                parent: (comment.parent) ? comment.parent : 0
            }
        })
        
        const grades = this.state.allGrades.map(grade => {
            return {
                label: grade.name,
                value: grade.id
            }
        });

        let materialList = this.state.getMaterialsList.map(period => {
            return {label: period.name, value: period.id};
        });

        let getReplys = (parentID) => {
            var allReplys = finalComments.map((parentComment, i) => {
                if (parentComment.parent !== 0 && parentComment.parent === parentID) {
                    return (
                        <div className="one" key={i}>
                            
                            <section>
                                {(parentComment.profile) ? <img src={(parentComment.profile) ? `${properties.uploads_path}/${parentComment.profile}` : (require(`../../assets/img/pp.jpg`).default)} alt={parentComment.name} /> : ""}
                                <div>
                                    <h3>{(parentComment.name) ? parentComment.name : this.state.currentTeacher.name} {(!parentComment.name) ? <span>المدرس</span> : ""}</h3>
                                    <p>{parentComment.body}</p>
                                </div>
                            </section>

                            <div className="control-btns">
                                <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => this.deleteComment(parentComment.id)} />
                            </div>
                        </div>
                    );
                }
            });

            return allReplys;
        }

        let getComments = finalComments.map(singleComment => {
             if (singleComment.parent === 0) {
                return (
                    <Fieldset className="p-mb-5" key={singleComment.id} legend={singleComment.name} toggleable>
                        <div className="comment-body">
                            <section>
                                <img src={(singleComment.profile) ? `${properties.uploads_path}/${singleComment.profile}` : (require(`../../assets/img/pp.jpg`).default)} alt={singleComment.name} />
                                <p>{singleComment.body}</p>
                            </section>
                            
                            <div className="control-btns">
                                <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => this.deleteComment(singleComment.id)} />
                                <Button icon="pi pi-reply" className="p-button-rounded p-button-secondary" onClick={(e) => {
                                    this.setState( prevState => ({
                                        currentReplyID: (prevState.currentReplyID) ? null : singleComment.id,
                                        reply: null
                                }))}} />
                            </div>
                        </div>

                        <div className="replys-comments">
                            {getReplys(singleComment.id)}
                        </div>

                        {(this.state.currentReplyID === singleComment.id) ? (
                            <div className="reply-form p-mt-5">
                                <div className="p-grid p-fluid">
                                    <div className="p-col-12 p-md-10">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputText id="reply" value={this.state.reply} name="reply" type="text" onChange={(e) => {this.setState({
                                                    reply: e.target.value
                                                })}} />
                                                
                                                <label htmlFor="reply">الرد</label>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-2">
                                        <Button label="إرسال" icon="pi pi-send" className="p-button-secondary" onClick={this.sendReply} />
                                    </div>
                                </div>
                            </div>
                        ): ""}
                    </Fieldset>
                );
             }   
        });

        const onUploadVoice = (e) => {
            let res = JSON.parse(e.xhr.response);
            this.setState({formContent: {
                // ...this.state.formContent,
                // voiceLink: res.video_url
                // formContent: {
                    ...this.state.formContent,
                 
                    lessonVoiceList:this.state.formContent.lessonVoiceList.concat(res.video_url.split('/').pop()),
                //   },
            }});
            this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الصوت'});
        }

        const onUploadThumb = (e) => {
            let res = JSON.parse(e.xhr.response);
            this.setState({
                thumbs: this.state.thumbs.concat(res.thumb_url.split('/').pop()),
                
            //     formContent: {
            //     ...this.state.formContent,
            //     images: this.state.thumbs.concat(res.thumb_url.split('/').pop()),
            //     // images: this.state.formContent.images.concat(res.thumb_url.split('/').pop())
            // }
        });
        //     this.setState({

        //     //     formContent: {
        //     //     ...this.state.formContent,
        //     //     thumb: res.thumb_url
        //     // }
        //     images: this.state.images.concat(res.thumb_url.split('/').pop()),
        // });
            this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الصورة المصغرة'});
        }
        
        const onUploadPDFs = (e) => {
            let res = JSON.parse(e.xhr.response);
            this.setState({formContent: {
                ...this.state.formContent,
                documents:  this.state.formContent.documents.concat(res.documents[0].split('/').pop()),
                // documents: this.state.formContent.documents.concat(res.documents)
            }});
            this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع المذكرات'});
        }
      
        
    
        var addLesson = (e) => {
            var _this = this;
            e.preventDefault();
            var _data = JSON.parse(window.localStorage.getItem('currentTeacher'));
            this.setState({
                buttonText: "جاري الإضافة ...",
                buttonColor: "loading"
            });
            
            this.setState({
                formContent: {
                    ...this.state.formContent,
                    youtubeLinks: (this.state.formContent.youtubeLinks) ,
                    vimeoLinks: (this.state.formContent.vimeoLinks), 
                    videoLinks: (this.state.formContent.videoLinks), 

                    teacherId : _data["teacherId"],
                    images: this.state.thumbs,
                }
            }, () => {
                // Send the data to the server
                fetch(properties.api_path + "/lesson/lessonregister", {
                    method: "POST",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json; charset=utf-8',
                    },
                    body: JSON.stringify(this.state.formContent)
                })
                .then(res => {return res.json()})
                .then(done => {
                    _this.setState({
                        buttonText: "إضافة حصة",
                        buttonColor: ""
                    });

                    if (done.msgType === "success") {
                        _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
                        } else {
                        _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
                    }
                })
                .catch(error => {
                    console.error('Error:', error)

                    _this.setState({
                        buttonText: "إنشاء الحساب",
                        buttonColor: "",
                        msg: {
                            msgText: "",
                            msgType: "warning",
                            msgIcon: "fas fa-times-circle"
                        }});

                    _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
                });
            });
        }
        var deletImage = (e,imag,index) => {
            var _this = this;

            // Get Periods From API
            // console.log(imageString);
            // var data = JSON.parse(window.localStorage.getItem("currentTeacher"));
            fetch(properties.api_path + "/lessonimage/deleteimage", {
              method: "POST",
              headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
              
                img: imag,
              }),
            })
              .then((res) => {
                return res.json();
              })
              .then(done => {
                // _this.setState({
                //     buttonText: "إضافة حصة",
                //     buttonColor: ""
                // });

                if (done.msgType === "success") {
                    _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
                    // var filtered = this.state.thumbs.filter(obj => {
                    //     // 👇️ returns truthy for all elements that
                    //     // don't have an id equal to 2
                    //     return obj ===imag;
                    //   });
                    // var array =      this.state.thumbs;
                    // var index = array.indexOf(e.target.value); // Let's say it's Bob.
                    // // delete array[index];
                    // array.splice(index, 1);
                    // window.location.reload(true)
                    // this.state.thumbs.removeItem(index)
                    } else {
                    _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
                }
            })
            .catch(error => {
                console.error('Error:', error)

                // _this.setState({
                //     buttonText: "إنشاء الحساب",
                //     buttonColor: "",
                //     msg: {
                //         msgText: "",
                //         msgType: "warning",
                //         msgIcon: "fas fa-times-circle"
                //     }});

                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
          };
          var deletDocument= (e,doc) => {
            var _this = this;

            // Get Periods From API
            // console.log(imageString);
            // var data = JSON.parse(window.localStorage.getItem("currentTeacher"));
            fetch(properties.api_path + "/lessondocuments/deletedocuments", {
              method: "POST",
              headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
              
                link: doc,
              }),
            })
              .then((res) => {
                return res.json();
              })
              .then(done => {
                // _this.setState({
                //     buttonText: "إضافة حصة",
                //     buttonColor: ""
                // });

                if (done.msgType === "success") {
                    _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
                    
                    } else {
                    _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
                }
            })
            .catch(error => {
                console.error('Error:', error)

                // _this.setState({
                //     buttonText: "إنشاء الحساب",
                //     buttonColor: "",
                //     msg: {
                //         msgText: "",
                //         msgType: "warning",
                //         msgIcon: "fas fa-times-circle"
                //     }});

                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
          };
          var deletVoice= (e,doc) => {
            var _this = this;

            // Get Periods From API
            // console.log(imageString);
            // var data = JSON.parse(window.localStorage.getItem("currentTeacher"));
            fetch(properties.api_path + "/lessonvoice/deletevoice", {
              method: "POST",
              headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
              
                voice: doc,
              }),
            })
              .then((res) => {
                return res.json();
              })
              .then(done => {
                // _this.setState({
                //     buttonText: "إضافة حصة",
                //     buttonColor: ""
                // });

                if (done.msgType === "success") {
                    _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
                    
                    } else {
                    _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
                }
            })
            .catch(error => {
                console.error('Error:', error)

                // _this.setState({
                //     buttonText: "إنشاء الحساب",
                //     buttonColor: "",
                //     msg: {
                //         msgText: "",
                //         msgType: "warning",
                //         msgIcon: "fas fa-times-circle"
                //     }});

                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
          };
          var deletVideo= (e,doc) => {
            var _this = this;

            // Get Periods From API
            // console.log(imageString);
            // var data = JSON.parse(window.localStorage.getItem("currentTeacher"));
            fetch(properties.api_path + "/lessonvedio/deletevideo", {
              method: "POST",
              headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
              
                video: doc,
              }),

            })
              .then((res) => {
                return res.json();
              })
              .then(done => {
                // _this.setState({
                //     buttonText: "إضافة حصة",
                //     buttonColor: ""
                // });

                if (done.msgType === "success") {
                    _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
                    
                    } else {
                    _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
                }
            })
            .catch(error => {
                console.error('Error:', error)

                // _this.setState({
                //     buttonText: "إنشاء الحساب",
                //     buttonColor: "",
                //     msg: {
                //         msgText: "",
                //         msgType: "warning",
                //         msgIcon: "fas fa-times-circle"
                //     }});

                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
          };
        // var getCurrentFiles = this.state.formContent.documents.map((file, Identifier) => {
        //     var getFileFullName = file.split("/")[1];
        //     var getRealNameAndExt = getFileFullName.split("__");
            
        //     return (
        //         <div className="single-file" key={Identifier}>
        //             <i className="fas fa-minus-circle delete" onClick={() => {
        //                 var tempFiles = [...this.state.formContent.documents];
        //                 tempFiles.splice(Identifier, 1);

        //                 this.setState(prevState => ({
        //                     formContent: {
        //                         ...this.state.formContent,
        //                         documents: [...tempFiles]
        //                     }
        //                 }))
        //             }}></i>
        //             <a target="_blank" rel="noreferrer" href={`${properties.uploads_path}/${file}`}><i className="fas fa-file-alt"></i> {getRealNameAndExt[0] + getRealNameAndExt[3]}</a>
        //         </div>
        //     );
        // });
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
        return (
            <div className="add-lesson-page">
                <Header pageName={`تعديل (${this.state.formContent.lessonName})`} />

                <div className="content">
                    <Toast ref={this.toast}></Toast>

                    <div className="container">
                        <Card title="تعديل حصة">
                            <form onSubmit={addLesson} action="#" method="POST">
                                <div className="p-grid p-fluid">
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputText id="lesson-name" value={this.state.formContent.lessonName} name="lesson-name" type="text" onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    lessonName: e.target.value
                                                }})}} />
                                                
                                                <label htmlFor="lesson-name">إسم الحصة</label>
                                            </span>
                                        </div>
                                    </div>
                                     <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.type} options={typeList} onChange={(e) => this.setState({type: e.value, formContent: {
                                                    ...this.state.formContent,
                                                    lessonType: e.value
                                                }})} name="type" placeholder="نوع الحصة"/>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.period} options={periods} onChange={(e) => this.setState({period: e.value, formContent: {
                                                    ...this.state.formContent,
                                                    periodId: e.value
                                                }})} name="periods" placeholder="الفترة"/>
                                            </span>
                                        </div>
                                    </div>
                                    
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.grade} options={grades} onChange={(e) => this.setState({grade: e.value,
                                                    formContent: {
                                                        ...this.state.formContent,
                                                        schoolId: e.value
                                                    }
                                                    
                                                },this.getMaterial(e.value ))}

                                                 name="grade" placeholder="المرحلة الدراسية"/>
                                            </span>
                                        </div>
                                    </div>
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.material} options={materialList} onChange={(e) => this.setState({material : e.value ,
                                                formContent: {
                                                    ...this.state.formContent,
                                                    materialId: e.value
                                                }
                                                
                                                }, this.getPeriod(e.value ))} name="subject" placeholder="إختر المادة"/>
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

                                                    <label htmlFor="video-link">لينك اليوتيوب</label>
                                                </span>
                                            </div>
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
                                    <div className="p-col-12 p-md-12 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <InputTextarea rows={5} value={this.state.formContent.lessonDescription} onChange={(e) => {this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    lessonDescription: e.target.value
                                                }})}} name="lesson-description" id="lesson-description" />

                                                <label htmlFor="lesson-description">وصف الحصة</label>
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

                                                   
                                                        // ...this.state.videos,
                                                        // lessonVideoList: this.state.videos.lessonVideoList.concat(res.video_url.split('/').pop()),

                                                        youtubeLink:e.target.value,
                                                       
                                                      }   )}} />
                                                      <Button    onClick={()=>uploadVideoYoutube(this.state.youtubeLink)}>اضافه</Button>

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
                    {/* {this.state.videos.lessonVideoList.map((object, i) =>              
                   
                   <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank">{`${object}`}</a>
 )}  */}
                  </div>
                                  ): ""} 
                  {(this.state.setVideoType === 2) ? (
                                        <div className="p-col-12 p-md-4 p-mt-3">
                                            <div className="p-inputgroup">
                                                <span className="p-float-label">
                                                <InputText id="video-link" value={this.state.vimeoLink} name="video-link" type="text" onChange={(e) => {this.setState({

                                                   
// ...this.state.videos,
// lessonVideoList: this.state.videos.lessonVideoList.concat(res.video_url.split('/').pop()),

vimeoLink:e.target.value,

}   )}} />
                                               <Button    onClick={()=>uploadVideoViemo(this.state.vimeoLink)}>اضافه</Button>


                                                    <label htmlFor="video-link">لينك Vimeo</label>
                                                </span>
                                            </div>
                                        </div>
                                    ): ""} 
                                    <div>
                      {this.state.formContent.videoLinks.map((object, i) =>              
                   <div>
                                       <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank" key={i}>{`${object}`}</a>

                                       <button type="button" class="btn btn-danger" onClick={(e) => deletVideo(e,object) }>حذف الفيديو</button>

                   </div>
                   
 )} 
     
        {this.state.formContent.youtubeLinks.map((object, i) =>      
        <div>                  
           <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank" key={i}>{`${object}`}</a>
           <button type="button" class="btn btn-danger" onClick={(e) => deletVideo(e,object) }>حذف الفيديو</button>

        </div>        
                   
 )} 
      
         
          {this.state.formContent.vimeoLinks.map((object, i) =>              
                   <div> 
                   <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank" key={i}>{`${object}`}</a>
                   <button type="button" class="btn btn-danger" onClick={(e) => deletVideo(e,object) }>حذف الفيديو</button>

                   </div>   )} 
     
       
                   </div>
                                    {(this.state.setVideoType === 1) ? (
                                        <div className="p-col-12 p-md-12">
                                            <div className="p-inputgroup">
                                                <span className="p-float-label">
                                                    <FileUpload name="LessonVideo" onUpload={uploadVideo} invalidFileSizeMessageSummary="حجم الفيديو كبير:" invalidFileSizeMessageDetail="أقصى حجم للفيديو هو (1 GB)" url={`${properties.uploads_path}/upload-video.php`} customUpload={false} multiple={false} accept=".mp3" maxFileSize={1000000000} chooseLabel="إختيار الصوت" uploadLabel="رفع الصوت" cancelLabel="إلغاء" emptyTemplate={<p className="p-m-0">إسحب الصوت هنا ليبدأ التحميل</p>} />
                                                </span>

                                                {/* <video className="preview-video" controls={true}>
                                                    <source src={`${properties.uploads_path}/video.php?videoSRC=${this.state.formContent.voiceLink}`}></source>
                                                </video> */}
                                            </div>
                                            {this.state.formContent.lessonVoiceList.map((object, i) =>     
                                            <div>         
                   <a href={`${properties.uploads_path}/videos/${object}`} download target="_blank">{`${object}`}</a>
                   <button type="button" class="btn btn-danger" onClick={(e) => deletVideo(e,object)
                   
                
                }>حذف الملف</button>
                   </div>
  )}
                                        
         
                                        </div>
                                    ): ""}
                                    <div className="p-col-12 p-md-12">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <FileUpload name="thumb" onUpload={onUploadThumb} invalidFileSizeMessageSummary="حجم الفيديو كبير:" invalidFileSizeMessageDetail="أقصى حجم للصورة هو (5 MB)" url={`${properties.uploads_path}/upload-thumb.php`} customUpload={false} multiple={false} accept="image/*" maxFileSize={50000000} chooseLabel="إختيار صورة مصغرة" uploadLabel="رفع الصورة" cancelLabel="إلغاء" emptyTemplate={<p className="p-m-0">إسحب الصورة هنا ليبدأ التحميل</p>} />
                                            </span>
                                           
                                            {/* <img className="preview-thumb" src={`${properties.uploads_path}/${this.state.formContent.thumb}`} alt={this.state.lessonName} /> */}
                                        </div>
                                        {this.state.thumbs.map((object, i) =>   
                                        <div>
                            <img className="preview-thumb image" src={`${properties.uploads_path}/thumb/${object}`} alt={object} onClick={(e)=>e.target.requestFullscreen()}/>
                            <button type="button" class="btn btn-danger" onClick={(e) => deletImage(e,object,i) }>حذف الصوره</button>

                                        </div>           
                              )}
                                    </div>
                                    <div className="p-col-12 p-md-12">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <FileUpload name="LessonPDFs[]" onUpload={onUploadPDFs} invalidFileSizeMessageSummary="حجم الفيديو كبير:" invalidFileSizeMessageDetail="أقصى حجم للمذكرة هو (10 MB)" url={`${properties.uploads_path}/upload-pdf.php`} customUpload={false} multiple={true} accept=".doc, .docx, .pdf, .ppt, .pptx" maxFileSize={10000000} chooseLabel="إختيار المذكرات" uploadLabel="رفع المذكرات" cancelLabel="إلغاء" emptyTemplate={<p className="p-m-0">إسحب المذكرات هنا ليبدأ التحميل</p>} />
                                                    {/* {(this.state.formContent.documents.length) ? (
                                                        <div className="current-files">
                                                            {getCurrentFiles}
                                                        </div>
                                                    ) : ""} */}
                                            </span>
                                        </div>
                                        {this.state.formContent.documents.map((object, i) =>       
                                        <div>

                                      <a href={`${properties.uploads_path}/documents/${object}`} download target="_blank">{`${object}`}</a>

                                        <button type="button" class="btn btn-danger" onClick={(e) => deletDocument(e,object) }>حذف الملف</button>

                                        </div>       
  )}
                                    </div>
                                    <div className="p-col-12 p-md-12">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Button className={`add-button ${this.state.buttonColor}`} label={this.state.buttonText}/>
                                            </span>
                                        </div>
                                    </div>
                                 </div>
                            </form>
                        </Card>

                        <Card title="التعليقات"className="p-mt-5">
                            {getComments}
                        </Card>
                    </div>
                </div>
            </div>
        );
    }
}

export default EditLessons;