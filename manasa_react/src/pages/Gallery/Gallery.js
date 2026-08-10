import React, {Component, createRef} from 'react';

// CSS
import './Gallery.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';
import { apiFetch } from '../../Helper/Api';

// Components
import Header from '../../components/Header/Header';
import { Toast } from 'primereact/toast';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';

// Properties
const properties = require('../../properties.json');

class AllLessons extends Component {
    constructor(props) {
        super(props);
        this.toast = createRef(null);
    }

    state = {
        globalFilter: null,

        // Teacher Identifier
        branchID: properties.branch_id,

        // Data
        data: [],

        // Update Data
        resend: false
    }

    getMedia = () => {
        var _this = this;

        // Get Media From API
        apiFetch("/media", { query: { branchId: this.state.branchID } })
        .then(allMedia => {
            this.setState({
                data: allMedia
            });
        })
        .catch(error => {
            console.error('Error:', error)

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });
    }

    deleteMedia = (id) => {
        var _this = this;

        // Confirm Deletion
        var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

        if (con) {
            // Delete Media By ID
            apiFetch(`/media/${id}`, { method: "DELETE" })
            .then(() => {
                this.setState({resend: true});
                _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الحذف بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)

                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
            });
        }
    }
    
    componentDidMount () {
        document.title = `${properties.site_name} | المعرض`;

        // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        this.getMedia();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState.resend !== this.state.resend) {
            this.getMedia();

            this.setState({resend: false});
        }
    }
    
    render() {
        const header = (
            <div className="table-header">
                المعرض
                <span className="p-input-icon-left">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في المعرض" />
                </span>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => this.deleteMedia(rowData.id)} />
                </React.Fragment>
            );
        }

        const imageBodyTemplate = (rowData) => {
            if (rowData.mediaType === 1) {
                return <a style={{backgroundImage: `url("${properties.uploads_path}/${rowData.thumb}")`}} className="play-video" href={`${properties.uploads_path}/${rowData.mediaLink}`}><i className="fas fa-play"></i></a>
            } else {
                return <img src={`${properties.uploads_path}/${rowData.mediaLink}`} alt={rowData.image} className="gallery-image" />
            }
        }

        const getMediaName = (rowData) => {
            var getFileFullName = rowData.mediaLink.split("/")[1];
            var getRealNameAndExt = getFileFullName.split("__");

            if (rowData.mediaType === 1) {
                return getRealNameAndExt[0] + getRealNameAndExt[2];
            } else {
                return getRealNameAndExt[0] + getRealNameAndExt[3];
            }

        }

        return (
            <>
                <Header pageName="المعرض" />

                <Toast ref={this.toast}></Toast>

                <div className="content">
                    <div className="container">
                        <div className="card">
                            <Card title="المعرض">
                                <DataTable header={header} sortable globalFilter={this.state.globalFilter} className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped" value={this.state.data} paginator rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"  emptyMessage="لا يوجد ملفات">
                                    <Column filter sortable  field="mediaLink" body={getMediaName} header="إسم الصورة/الفيديو"></Column>
                                    <Column filter sortable field="mediaType" header="معاينة" body={imageBodyTemplate}></Column>
                                    <Column header="حذف" body={actionBodyTemplate}></Column>
                                </DataTable>
                            </Card>
                        </div>
                    </div>
                </div>
            </>
        );
    }
}

export default AllLessons;