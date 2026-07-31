import React, {Component, createRef} from 'react';
import {Link, NavLink} from 'react-router-dom';
import { Dropdown } from 'primereact/dropdown';
import {Dialog} from 'primereact/dialog'
import Popup from 'reactjs-popup';
// CSS
import './Students.scss';

// Helper
import {setCookie, checkCookie} from '../../Helper/Cookies';

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

class AllStudents extends Component {
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
        resend: false , 
        visible : false 
    }
    
    getStudents = () => {
        // Get Students From API
        fetch(properties.api_path + "/studentPeriodsOrders/studentactivation", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json', 
            },
            body: JSON.stringify({
                branchId: this.state.branchID
            })
        })
        .then(res => {return res.json()})
        .then(fetchStudents => {
            this.setState({
                data: fetchStudents
            });
        });
    }

    viewStudent =(R)=>{ 
        
          
    }

    deleteStudent = (periodOrderId) => {
        var _this = this;

        // Confirm Deletion
        var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

        if (con) {
            // Delete Subscription By ID
            fetch(properties.api_path + "/studentPeriodsOrders/deletestudentactivation", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    periodOrderId55: periodOrderId,
                })
            })
            .then(res => {return res.json()})
            .then(
                deleteMediaByID => {
                this.setState({resend: true});
                _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الحذف بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)
                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
        }
    }
    
    editStudent = (periodOrderId) => {
        var _this = this;

        // Confirm Deletion
        // var con = window.confirm("هل أنت متأكد من أنك تريد حذف هذا العنصر");

        // if (con) {
            // Delete Subscription By ID
            fetch(properties.api_path + "/studentPeriodsOrders/studenttransaction", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    periodOrderId55: periodOrderId,
                })
            })
            .then(res => {return res.json()})
            .then(
                deleteMediaByID => {
                this.setState({resend: true});
                _this.toast.current.show({severity: 'success', summary: 'تمت العملية', detail: 'تم الاشتراك بنجاح'});
            })
            .catch(error => {
                console.error('Error:', error)
                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
        // }
    }
    componentDidMount () {
        document.title = properties.site_name + " | كل الطلاب";

       // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
        if (!checkCookie('name')) {
            window.localStorage.removeItem('currentTeacher');
            window.location.href = `/`;
        }

        this.getStudents();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevState.resend !== this.state.resend) {
            this.getStudents();

            this.setState({resend: false});
        }
    }

        render() {
       
    
        const header = (
            <div className="table-header">
                عدد الطلاب
                <span>{this.state.data.length}</span>
                <span className="p-input-icon-left">
                    <i className="pi pi-search" />
                    <InputText type="search" onInput={(e) => this.setState({globalFilter: e.target.value})} placeholder="بحث في الطلاب" />
                </span>
            </div>
        );
        
       
        const showResetPopUp = (rowData) => {
            this.setState({ visible: true, studentId: rowData.studentId });
          };

        const actionBodyTemplate = (rowData) => {
            return (
                <React.Fragment>
                    <Button icon="pi pi-eye" className="p-button-rounded p-button-secondary p-ml-2" onClick={() => window.location.href = `exams/view/${rowData.id}`} />

                    <Button
            icon="pi pi-plus"
            className="p-button-rounded p-button-success p-ml-2"
            onClick={() =>
              this.editStudent(rowData.periodOrderId55)
            }
          />  
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() =>
              this.deleteStudent(rowData.periodOrderId55)
            }
          />                </React.Fragment>
            );
        }
        const edit =(id) => {
            var _this = this ;
            // Send the data to the server
            fetch(properties.api_path + "/periods/savePeriods", {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json; charset=utf-8',
                },
                body: JSON.stringify({
                    id: id,
                    branchId : this.state.branchID ,
                    companyId: this.state.companyID,
                    name: this.state.userInputName , 
                    teacherId: this.state.teacherId , 
                    parent: this.state.selectedPeriod
                })
            })
            .then(res => {return res.json()})
            .then(done => {
               
               if (done.msgType === "success") {
                _this.toast.current.show({severity: 'success', summary: 'تم بنجاح', detail: done.msg});
                this.getPeriods();
                this.setState({visible : false});
                } else {
                    _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: done.msg});
               }
            })
            .catch(error => {
                console.error('Error:', error) 
                _this.toast.current.show({severity: 'error', summary: 'لم تتم العملية', detail: 'خطأ في الإتصال'});
            });
        }
        return (
            <>
                <Header pageName="تفعيل الطلاب" />

                <Toast ref={this.toast}></Toast>

                <div className="content">
                    <div className="container">
                        <div className="card">
                            <Card title="تفعيل الطلاب">
                                <DataTable header={header} sortable globalFilter={this.state.globalFilter} 
                                className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped" 
                                value={this.state.data} paginator rows={10} rowsPerPageOptions={[10,20,50]} currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"  emptyMessage="لا يوجد طلاب">
                                    <Column filter sortable field="fullName" header="إسم الطالب"></Column>
                                    <Column filter sortable field="mobile" header="رقم التلفون"></Column>
                                    <Column filter sortable field="periodName" header="الفتره "></Column>
                                    <Column filter sortable field="schoolStagesName" header="المرحلة الدراسية"></Column>
                                    <Column header=" رؤيه / اضافه / حذف " body={actionBodyTemplate}></Column>
                                    
                                </DataTable>
                            </Card>
                        </div>
                    </div>
               
                </div>
            </>
        );
    }
}
 
export default AllStudents;