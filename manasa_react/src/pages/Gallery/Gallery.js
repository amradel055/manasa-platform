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
import TableShell from '../../components/Table/TableShell';
import TableEmpty from '../../components/Table/TableEmpty';
import Modal from '../../components/Modal/Modal';
import Confirm from '../../components/Modal/Confirm';
import AddItem from './AddItem';
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
        resend: false,

        // whether the add modal is open. The modal mounts the very same
        // AddItem component the /gallery/add route mounts, with its own
        // state and its own submit -- that route still works.
        addOpen: false,

        // the rows are fetched on mount
        tableLoading: true,

        // a delete is in flight, and the row waiting on a yes
        deleting: false,
        confirmRow: null,
    }

    getMedia = () => {
        var _this = this;

        // Every call, not just the first: this runs again after a delete,
        // and a silent refetch reads as though nothing happened. The rows go
        // with it so the waiting row has somewhere to appear.
        this.setState({ tableLoading: true, data: [] });

        // Get Media From API
        apiFetch("/media", { query: { branchId: this.state.branchID } })
        .then(allMedia => {
            this.setState({
                data: allMedia,
                tableLoading: false
            });
        })
        .catch(error => {
            console.error('Error:', error)

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });
    }

    // The confirmation that used to be window.confirm is the Confirm modal
    // in the render below. The gate is the same -- this still only runs on a
    // yes -- and the request, the toast and the `resend` flip under it are
    // unchanged.
    deleteMedia = (id) => {
        var _this = this;

        this.setState({ deleting: true });

        // Delete Media By ID
        apiFetch(`/media/${id}`, { method: "DELETE" })
        .then(() => {
            this.setState({resend: true, deleting: false, confirmRow: null});
            _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الحذف بنجاح'});
        })
        .catch(error => {
            // the question stays open on failure, so it can be retried
            this.setState({ deleting: false });
            console.error('Error:', error)

            _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: error.message || 'خطأ في الإتصال'});
        });
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
                <span className="p-input-icon-left m-tool-search">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في المعرض" />
                </span>

                <span className="m-tool-count">
                    عدد الملفات <b>{this.state.data.length}</b>
                </span>
            </div>
        );

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-trash" title="حذف الملف" className="p-button-rounded p-button-danger" onClick={() => this.setState({ confirmRow: rowData })} />
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
                <Header
                    pageName="المعرض"
                    action={{ label: "إضافة صورة/فيديو", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
                />

                <Toast ref={this.toast}></Toast>

                {/* The same AddItem the /gallery/add route mounts, in a modal
                    instead of a page of its own. Mounted only while open, so
                    each opening starts from a clean form. `slot` gives its
                    submit button somewhere to land in the footer. */}
                <Modal
                    visible={this.state.addOpen}
                    onHide={() => this.setState({ addOpen: false })}
                    title="إضافة صورة/فيديو"
                    subtitle="ارفع الملف، وسيظهر في المعرض بعد الحفظ"
                    width={680}
                    slot
                >
                    {/* .m-form-legacy, like the other mounted pages: it hides
                        the page's own title and card chrome and styles the
                        fields, without a line of AddItem being touched. */}
                    <div className="m-form m-form-legacy">
                        {this.state.addOpen ? <AddItem /> : null}
                    </div>
                </Modal>

                {/* the question that used to be the browser's */}
                <Confirm
                    visible={this.state.confirmRow !== null}
                    onHide={() => this.setState({ confirmRow: null })}
                    onConfirm={() => this.deleteMedia(this.state.confirmRow.id)}
                    busy={this.state.deleting}
                    title="حذف ملف"
                    message="هل أنت متأكد من حذف هذا الملف من المعرض؟"
                    note="سيُحذف الملف نهائياً. لا يمكن التراجع عن هذه الخطوة."
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
                                <DataTable header={header} sortable globalFilter={this.state.globalFilter} className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped" value={this.state.data} paginator
                                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                                rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="عرض {first} – {last} من {totalRecords} ملف"
                                emptyMessage={
                                  <TableEmpty
                                    loading={this.state.tableLoading}
                                    icon="pi pi-images"
                                    title="لا يوجد ملفات"
                                    hint="ارفع أول صورة أو فيديو، وسيظهر هنا"
                                    action={{ label: "إضافة صورة/فيديو", onClick: () => this.setState({ addOpen: true }) }}
                                  />
                                }>
                                    {/* no column filters: the search above covers these */}
                                    <Column sortable  field="mediaLink" body={getMediaName} header="إسم الصورة/الفيديو"></Column>
                                    <Column sortable field="mediaType" header="معاينة" body={imageBodyTemplate}></Column>
                                    <Column header="الإجراءات" headerStyle={{ width: '92px' }} bodyStyle={{ width: '92px' }} body={actionBodyTemplate}></Column>
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

export default AllLessons;