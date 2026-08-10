import React, {Component, createRef} from 'react';

// CSS
import './Gallery.scss';
import 'primeflex/primeflex.css';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import { Card } from 'primereact/card';
import { FileUpload } from 'primereact/fileupload';
import { Toast } from 'primereact/toast';
import { Dropdown } from 'primereact/dropdown';
import { Button } from 'primereact/button';

// Properties
const properties = require('../../properties.json');

class AddItem extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        // Submit Button
        buttonText: "إضافة صورة/فيديو",
        buttonColor: "",

        // Lesson Data
        formContent: {
            branchId: properties.branch_id,
            mediaType: 0,
            mediaLink: null,
            thumb: null
        }
    }

    componentDidMount() {
        document.title = `${properties.site_name} | إضافة صورة/فيديو جديد`;

        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }
    }

    render() {
        const mediaType = [
            {label: 'فيديو', value: 1},
            {label: 'صورة', value: 0}
        ];

        const onUploadMedia = (e) => {
            let res = JSON.parse(e.xhr.response);
            this.setState({formContent: {
                ...this.state.formContent,
                mediaLink: res.media_url
            }});
            this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الصورة/الفيديو'});
        }
        
        const onUploadMediaThumb = (e) => {
            let res = JSON.parse(e.xhr.response);
            this.setState({formContent: {
                ...this.state.formContent,
                thumb: res.thumb_url
            }});
            this.toast.current.show({severity: 'info', summary: 'تم بنجاح', detail: 'تم رفع الصورة المصغرة'});
        }
        
        var addMedia = (e) => {
            var _this = this;
            e.preventDefault();

            this.setState({
                buttonText: "جاري الإضافة ...",
                buttonColor: "loading",
            });

            // Send the data to the server
            apiFetch("/media", {
                method: "POST",
                body: {
                    mediaLink: this.state.formContent.mediaLink,
                    mediaType: this.state.formContent.mediaType,
                    thumb: this.state.formContent.thumb,
                    branchId: this.state.formContent.branchId
                }
            })
            .then(() => {
               _this.setState({
                   buttonText: "إضافة صورة/فيديو",
                   buttonColor: ""
               });

               _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: 'تمت إضافة الصورة/الفيديو بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)

                _this.setState({
                   buttonText: "إضافة صورة/فيديو",
                   buttonColor: ""
               });

               _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        }

        return (
            <div className="add-lesson-page">
                <Header pageName="إضافة صورة/فيديو" />

                <div className="content">
                    <Toast ref={this.toast}></Toast>

                    <div className="container">
                        <Card title="إدخال صورة/فيديو جديد">
                            <form onSubmit={addMedia} action="#" method="POST">
                                <div className="p-grid p-fluid">
                                    <div className="p-col-12 p-md-4 p-mt-3">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Dropdown value={this.state.formContent.mediaType} options={mediaType} onChange={(e) => this.setState({formContent: {
                                                    ...this.state.formContent,
                                                    mediaType: e.value
                                                }})} name="videoType" placeholder="نوع الملف"/>
                                            </span>
                                        </div>
                                    </div>
                                    {(this.state.formContent.mediaType === 1) ? (
                                        <div className="p-col-12 p-md-12">
                                            <div className="p-inputgroup">
                                                <span className="p-float-label">
                                                    <FileUpload name="thumb" onUpload={onUploadMediaThumb} invalidFileSizeMessageSummary="حجم الصورة المصغرة كبير:" invalidFileSizeMessageDetail="أقصى حجم للصورة المصغرة هو (100 MB)" url={`${properties.uploads_path}/upload-thumb.php`} customUpload={false} multiple={false} accept="image/*" maxFileSize={1000000000} chooseLabel="إختيار صورة مصغرة" uploadLabel="رفع صورة مصغرة" cancelLabel="إلغاء" emptyTemplate={<p className="p-m-0">إسحب الصورة المصغرة هنا ليبدأ التحميل</p>} />
                                                </span>
                                            </div>
                                        </div>
                                    ) : ""}
                                    <div className="p-col-12 p-md-12">
                                        <div className="p-inputgroup">
                                            <span className="p-float-label">
                                                <Toast ref={this.toast}></Toast>
                                                <FileUpload name="media" onUpload={onUploadMedia} invalidFileSizeMessageSummary="حجم الصورة/الفيديو كبير:" invalidFileSizeMessageDetail="أقصى حجم للصورة/فيديو هو (200 MB)" url={`${properties.uploads_path}/upload-media.php`} customUpload={false} multiple={false} accept={`${(this.state.formContent.mediaType === 1) ? 'video/*': 'image/*'}`} maxFileSize={2000000000} chooseLabel="إختيار صورة/فيديو" uploadLabel="رفع صورة/فيديو" cancelLabel="إلغاء" emptyTemplate={<p className="p-m-0">إسحب الصورة/الفيديو هنا ليبدأ التحميل</p>} />
                                            </span>
                                        </div>
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
                    </div>
                </div>
            </div>
        );
    }
}

export default AddItem;