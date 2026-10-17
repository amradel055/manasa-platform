import React, { Component, createRef } from "react";

// Helper
import { setCookie, checkCookie } from "../../Helper/Cookies";
import { apiFetch, getCurrentUser } from "../../Helper/Api";

// Components
import Header from "../../components/Header/Header";
import TableShell from "../../components/Table/TableShell";
import TableEmpty from "../../components/Table/TableEmpty";
import SlidePanel from "../../components/Panel/SlidePanel";
import Confirm from "../../components/Modal/Confirm";
import "../../components/Field/Form.scss";
import { Card } from "primereact/card";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import Select from '../../components/Field/Select';
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Calendar } from "primereact/calendar";
import { InputText } from "primereact/inputtext";
// Properties
const properties = require("../../properties.json");

class Schedule extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
  }

  state = {
    // Submit Button
    buttonText: "إضافة ميعاد",
    buttonColor: "",

    // Teacher Identifier
    branchID: properties.branch_id,
    companyID: properties.company_id,
    teacherId: null,
    periodName: null,
    globalFilter: null,
    userInput: null,
    // Get Data
    data: [],
    allGrades: [],
    dataId: [],
    getMaterialsList: [],
    selectedPeriod: null,
    selectedPeriodId: null,
    notes: null,
    post: null,
    type: null,
    day: null,
    active: null,
    schoolStageId: null,
    visible: false,
    editableData: null,
    formContent: {
      branchId: properties.branch_id,
      companyId: properties.company_id,
      thumb: null,
    },
    date1: null,
    date2: null,
    date3: null,
    date4: null,
    selectedMaterial: null,
    selectedDate1: null,
    selectedDate2: null,
    selectedDate3: null,
    selectedDate4: null,

    // whether the add panel is open. `visible` above is the edit panel's
    // own flag and keeps its meaning.
    addOpen: false,

    // True from the start: the list is fetched on mount, so there is a wait
    // to report before the first rows arrive. Only TableEmpty reads it, so
    // the waiting row appears where the answer will.
    tableLoading: true,

    // one flag per lookup list, raised inside its own fetch
    gradesLoading: false,
    placesLoading: false,

    // one flag per action -- never a shared one, or a slow save in one panel
    // greys out the buttons in the other
    adding: false,
    editing: false,
    deleting: false,

    // the row waiting on a yes
    confirmRow: null,
  };

  getMaterial = () => {
    // inside the fetch, so opening the list reports the wait too
    this.setState({ placesLoading: true });

    // Get Lesson Locations From API
    apiFetch("/lookup/lesson-locations", { query: { branchId: this.state.branchID } })
      .then((fetchMaterials) => {
        this.setState({
          getMaterialsList: fetchMaterials,
          placesLoading: false,
        });
      })
      .catch((error) => {
        this.setState({ placesLoading: false });
        console.error("Error:", error);
      });
  };

  // Lifted out of componentDidMount unchanged, so the stage list can be
  // asked for again when its dropdown is opened rather than staying the
  // snapshot it was at mount.
  getGrades = () => {
    this.setState({ gradesLoading: true });

    apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: getCurrentUser()?.userId } })
      .then((grades) => {
        this.setState({ allGrades: grades, gradesLoading: false });
      })
      .catch((error) => {
        this.setState({ gradesLoading: false });
        console.error("Error:", error);
      });
  };

  componentDidMount() {
    document.title = properties.site_name + " | المواعيد";

    this.allAppoinments();
    // this.getPeriods();
    // Redirect To Sign In Page If The User Is Already Logged Out & Remove The Data If It Exists
    if (!checkCookie("name")) {
      window.localStorage.removeItem("currentTeacher");
      window.location.href = `/`;
    }

    this.getMaterial();

    this.getGrades();

    // Get Periods From API
  }

  allAppoinments = () => {
    var _this = this;

    // Every call announces itself, not just the first: this runs again after
    // an add, an edit and a delete, and a silent refetch reads as though the
    // action never registered. The rows go with it so the waiting row has
    // somewhere to appear.
    this.setState({ tableLoading: true, data: [] });

    // Send the data to the server
    apiFetch("/appointments")
      .then((done) => {
        for (var i = 0; i < done.length; i++) {
          done[i].time =
            done[i].time != null
              ? done[i].time.toString().split("T")[1].split("+")[0]
              : null;
          done[i].time2 =
            done[i].time2 != null
              ? done[i].time2.toString().split("T")[1].split("+")[0]
              : null;
          done[i].time3 =
            done[i].time3 != null
              ? done[i].time3.toString().split("T")[1].split("+")[0]
              : null;
          done[i].time4 =
            done[i].time4 != null
              ? done[i].time4.toString().split("T")[1].split("+")[0]
              : null;
        }
        this.setState({ data: done, tableLoading: false });
      })
      .catch((error) => {
        // the wait ends on failure too, or the table waits for ever
        this.setState({ tableLoading: false });
        console.error("Error:", error);
        _this.toast.current.show({
          severity: "error",
          summary: "لم تتم العملية",
          detail: error.message || "خطأ في الإتصال",
        });
      });
  };

  edit = () => {
    var _this = this;

    this.setState({ editing: true });

    // Send the data to the server
    apiFetch(`/appointments/${this.state.editableData.id}`, {
      method: "PUT",
      body: {
        appointmentsType: this.state.editableData.appointmentType,
        day: this.state.editableData.day,
        time: this.state.editableData.time,
        time2: this.state.editableData.time2,
        time3: this.state.editableData.time3,
        time4: this.state.editableData.time4,
        lessonLocationId: this.state.editableData.materialsId,
        schoolStageId: this.state.editableData.schoolStageId,
        branchId: this.state.branchID,
        remarks: this.state.notes,
      },
    })
      .then(() => {
        _this.toast.current.show({
          severity: "success",
          summary: "تم بنجاح",
          detail: "تم التعديل بنجاح",
        });
        this.allAppoinments();
        this.setState({ visible: false, editing: false });
      })
      .catch((error) => {
        // the panel stays open with what was typed still in it, so it can be
        // sent again without retyping
        this.setState({ editing: false });
        console.error("Error:", error);
        _this.toast.current.show({
          severity: "error",
          summary: "لم تتم العملية",
          detail: error.message || "خطأ في الإتصال",
        });
      });
  };

  addSchedule = () => {
    var _this = this;

    this.setState({ adding: true });

    // Send the data to the server
    apiFetch("/appointments", {
      method: "POST",
      body: {
        appointmentsType: this.state.type,
        lessonLocationId: this.state.selectedMaterial,
        schoolStageId: this.state.schoolStageId,
        day: this.state.day,
        time: this.state.date1,
        time2: this.state.date2,
        time3: this.state.date3,
        time4: this.state.date4,
        branchId: this.state.branchID,
        remarks: this.state.notes,
      },
    })
      .then(() => {
        _this.toast.current.show({
          severity: "success",
          summary: "تم بنجاح",
          detail: "تمت إضافة الميعاد بنجاح",
        });
        this.allAppoinments();
        this.setState({ adding: false, addOpen: false });
      })
      .catch((error) => {
        // left open, with the typing still in it
        this.setState({ adding: false });
        console.error("Error:", error);
        _this.toast.current.show({
          severity: "error",
          summary: "لم تتم العملية",
          detail: error.message || "خطأ في الإتصال",
        });
      });
  };

  render() {
    const typeList = [
      { label: " ثابت", value: 0 },
      { label: " تعويض ", value: 1 },
      { label: "فاينال", value: 2 },
    ];
    const dayList = [
      { label: "السبت", value: 6 },
      { label: " ألاحد ", value: 7 },
      { label: "الاتنين", value: 1 },
      { label: "الثلاثاء", value: 2 },
      { label: "الاربعاء ", value: 3 },
      { label: "الخميس", value: 4 },
      { label: "الجمعه", value: 5 },
    ];

    const active = [
      { label: "مفعل", value: 1 },
      { label: "غير مفعل", value: 0 },
    ];
    const grades = this.state.allGrades.map((grade) => {
      return {
        label: grade.name,
        value: grade.id,
      };
    });

    let materialList = this.state.getMaterialsList.map((period) => {
      return { label: period.name, value: period.id };
    });

    var deletePeriod = (e) => {
      var _this = this;

      this.setState({ deleting: true });

      // Send the data to the server
      apiFetch(`/appointments/${e.id}`, { method: "DELETE" })
        .then(() => {
          _this.setState({ deleting: false, confirmRow: null });
          _this.allAppoinments();
          _this.toast.current.show({
            severity: "success",
            summary: "تم بنجاح",
            detail: "تم الحذف بنجاح",
          });
        })
        .catch((error) => {
          console.error("Error:", error);

          // the question stays open on failure, so it can be retried
          _this.setState({
            buttonText: "إضافة ميعاد",
            buttonColor: "",
            deleting: false,
          });

          _this.toast.current.show({
            severity: "error",
            summary: "لم تتم العملية",
            detail: error.message || "خطأ في الإتصال",
          });
        });
    };

    // The toolbar is a toolbar again. What used to be passed as the table's
    // `header` was the entire add form -- a Card, nine controls and a submit
    // button -- rendered inside the table's own toolbar slot.
    const header = (
      <div className="table-header">
        {/* no heading here: the page header above carries it */}
        <span className="p-input-icon-left m-tool-search">
          <i className="pi pi-search" />
          <InputText
            type="search"
            onInput={(e) => this.setState({ globalFilter: e.target.value })}
            placeholder="بحث في المواعيد"
          />
        </span>
      </div>
    );

    // The add form and the edit form ask for almost the same things, but
    // write to different places -- the add form to the page's own fields,
    // the edit form into `editableData` -- so every handler is passed in by
    // its caller and stays exactly what it was. Only the markup is shared.
    //
    // `hourFormat` is passed in too rather than fixed here: the add form has
    // always shown 12-hour time and the edit form 24-hour, and unifying them
    // would change what the page displays.
    const scheduleFields = ({ idPrefix, hourFormat, times, day, onDay, type, onType, stage, onStage, place, onPlace, notes, onNotes }) => (
      <>
        <section className="m-sec">
          <h3>
            <span className="m-sec-n">١</span>
            التوقيت
            <small>حتى أربعة مواعيد في اليوم</small>
          </h3>

          <div className="m-grid">
            {times.map((t, i) => (
              <div className="m-fld" key={i}>
                <label htmlFor={`${idPrefix}-time-${i}`}>{`التوقيت ${["الأول","الثاني","الثالث","الرابع"][i]}`}</label>
                {/* appendTo body: the overlay is absolutely positioned, and
                    the panel this form sits in clips what overflows it, so
                    left where it was the time picker was cut off. */}
                <Calendar
                  inputId={`${idPrefix}-time-${i}`}
                  timeOnly
                  hourFormat={hourFormat}
                  appendTo={document.body}
                  value={t.value}
                  onChange={t.onChange}
                ></Calendar>
              </div>
            ))}
          </div>
        </section>

        <section className="m-sec">
          <h3>
            <span className="m-sec-n">٢</span>
            تفاصيل الميعاد
            <small>اليوم ونوعه ومكانه</small>
          </h3>

          <div className="m-grid">
            <div className="m-fld">
              <label htmlFor={`${idPrefix}-day`}>اليوم</label>
              <Select
                inputId={`${idPrefix}-day`}
                value={day}
                options={dayList}
                onChange={onDay}
                name="type"
                placeholder="اليوم"
                optionLabel="label"
              />
            </div>

            <div className="m-fld">
              <label htmlFor={`${idPrefix}-type`}>نوع الميعاد</label>
              <Select
                inputId={`${idPrefix}-type`}
                value={type}
                options={typeList}
                onChange={onType}
                name="type"
                placeholder="نوع الميعاد"
                optionLabel="label"
              />
            </div>

            <div className="m-fld">
              <label htmlFor={`${idPrefix}-grade`}>المرحلة الدراسية</label>
              <Select
                inputId={`${idPrefix}-grade`}
                value={stage}
                options={grades}
                onChange={onStage}
                name="grade"
                placeholder="المرحلة الدراسية"
                optionLabel="label"
                loading={this.state.gradesLoading}
                onOpen={() => this.getGrades()}
              />
            </div>

            <div className="m-fld">
              <label htmlFor={`${idPrefix}-place`}>المكان</label>
              <Select
                inputId={`${idPrefix}-place`}
                value={place}
                options={materialList}
                placeholder="إختر المكان"
                onChange={onPlace}
                optionLabel="label"
                filter
                showClear
                filterBy="label"
                loading={this.state.placesLoading}
                onOpen={() => this.getMaterial()}
              />
            </div>

            {onNotes ? (
              <div className="m-fld m-wide">
                <label htmlFor={`${idPrefix}-notes`}>الملاحظات</label>
                <InputText
                  id={`${idPrefix}-notes`}
                  value={notes}
                  name="notes"
                  type="text"
                  placeholder="أي تفاصيل إضافية عن الميعاد"
                  onChange={onNotes}
                />
              </div>
            ) : null}
          </div>
        </section>
      </>
    );

    const actionBodyTemplate = (rowData) => {
      return (
        <React.Fragment>
          <Button
            icon="pi pi-pencil"
            title="تعديل الميعاد"
            className="p-button-rounded p-button-secondary p-ml-2"
            onClick={() =>
              this.setState({
                visible: true,
                editableData: rowData,
                selectedPeriod: null,
              })
            }
          />
          <Button
            icon="pi pi-trash"
            title="حذف الميعاد"
            className="p-button-rounded p-button-danger"
            onClick={() => this.setState({ confirmRow: rowData })}
          />
        </React.Fragment>
      );
    };

    return (
      <>
        <Header
          pageName="المواعيد"
          action={{ label: "إضافة ميعاد", icon: "pi pi-plus", onClick: () => this.setState({ addOpen: true }) }}
        />

        <Toast ref={this.toast}></Toast>

        {/* Nine controls including four time pickers: a slide-over, not a
            modal. A modal that has to scroll has stopped being a modal. */}
        <SlidePanel
          visible={this.state.addOpen}
          onHide={() => this.setState({ addOpen: false })}
          title="إضافة ميعاد جديد"
          subtitle="اختر التوقيت واليوم والمكان، ثم احفظ"
          actions={
            <Button
              className={this.state.buttonColor}
              icon={this.state.adding ? "pi pi-spin pi-spinner" : undefined}
              label={this.state.adding ? "جارٍ الحفظ…" : this.state.buttonText}
              disabled={this.state.adding}
              onClick={this.addSchedule}
            />
          }
        >
          <div className="m-form">
            {scheduleFields({
              idPrefix: "add-appt",
              hourFormat: "12",
              times: [
                { value: this.state.date1, onChange: (e) => { this.setState({ date1: e.value }); } },
                { value: this.state.date2, onChange: (e) => this.setState({ date2: e.value }) },
                { value: this.state.date3, onChange: (e) => this.setState({ date3: e.value }) },
                { value: this.state.date4, onChange: (e) => this.setState({ date4: e.value }) },
              ],
              day: this.state.day,
              onDay: (e) => this.setState({ day: e.value }),
              type: this.state.type,
              onType: (e) => this.setState({ type: e.value }),
              stage: this.state.schoolStageId,
              onStage: (e) => this.setState({ schoolStageId: e.value }),
              place: this.state.selectedMaterial,
              onPlace: (e) => this.setState({ selectedMaterial: e.value }),
              notes: this.state.notes,
              onNotes: (e) => { this.setState({ notes: e.target.value }); },
            })}
          </div>
        </SlidePanel>

        {/* the same fields, writing into the row being edited */}
        <SlidePanel
          visible={this.state.visible}
          onHide={() => this.setState({ visible: false })}
          title="تعديل الميعاد"
          subtitle="عدّل ما تريد، ثم احفظ التغييرات"
          actions={
            <Button
              icon={this.state.editing ? "pi pi-spin pi-spinner" : undefined}
              label={this.state.editing ? "جارٍ الحفظ…" : "تعديل"}
              disabled={this.state.editing}
              onClick={this.state.editableData !== null ? () => this.edit() : null}
            />
          }
        >
          {this.state.editableData !== null ? (
            <div className="m-form">
              {scheduleFields({
                idPrefix: "edit-appt",
                // left undefined, as it has always been here: PrimeReact then
                // falls back to 24-hour, which is what this form showed.
                hourFormat: undefined,
                times: [
                  { value: this.state.editableData["time"], onChange: (e) => this.setState({ editableData: { ...this.state.editableData, time: e.value } }) },
                  { value: this.state.editableData["time2"], onChange: (e) => this.setState({ editableData: { ...this.state.editableData, time2: e.value } }) },
                  { value: this.state.editableData["time3"], onChange: (e) => this.setState({ editableData: { ...this.state.editableData, time3: e.value } }) },
                  { value: this.state.editableData["time4"], onChange: (e) => this.setState({ editableData: { ...this.state.editableData, time4: e.value } }) },
                ],
                day: this.state.editableData["day"],
                onDay: (e) => this.setState({ editableData: { ...this.state.editableData, day: e.value } }),
                type: this.state.editableData["appointmentType"],
                onType: (e) => this.setState({ editableData: { ...this.state.editableData, appointmentType: e.value } }),
                stage: this.state.editableData["schoolStageId"],
                onStage: (e) => this.setState({ editableData: { ...this.state.editableData, schoolStageId: e.value } }),
                place: this.state.editableData["lessonLocationId"],
                onPlace: (e) => this.setState({ editableData: { ...this.state.editableData, materialsId: e.value } }),
                // No notes field here, exactly as before -- edit() sends
                // `remarks: this.state.notes`, which is the add form's box.
                notes: undefined,
                onNotes: undefined,
              })}
            </div>
          ) : null}
        </SlidePanel>

        {/* the question the delete button never used to ask */}
        <Confirm
          visible={this.state.confirmRow !== null}
          onHide={() => this.setState({ confirmRow: null })}
          onConfirm={() => deletePeriod(this.state.confirmRow)}
          busy={this.state.deleting}
          title="حذف ميعاد"
          message="هل أنت متأكد من حذف هذا الميعاد؟"
          note="لن يظهر بعدها في جدول طلابك. لا يمكن التراجع عن هذه الخطوة."
          confirmLabel="نعم، احذف"
          busyLabel="جارٍ الحذف…"
        />

        <div className="content">
          <div className="container">
            <div className="card">
              {/* no title: the page header above already says it, and what it
                  said -- "إضافة ميعاد جديد" -- was the title of the form, not
                  of this list */}
              <Card>
                {/* presentation only -- see components/Table/TableShell.js */}
                <TableShell>
                <DataTable
                  header={header}
                  sortable
                  globalFilter={this.state.globalFilter}
                  className="p-datatable-responsive-demo p-datatable-gridlines p-datatable-striped"
                  value={this.state.data}
                  paginator
                  paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                  rows={10}
                  rowsPerPageOptions={[10, 20, 50]}
                  currentPageReportTemplate="عرض {first} – {last} من {totalRecords} ميعاد"
                  emptyMessage={
                    <TableEmpty
                      loading={this.state.tableLoading}
                      icon="pi pi-calendar"
                      title="لا يوجد مواعيد"
                      hint="أضف أول ميعاد، وسيظهر هنا وفي جدول طلابك"
                      action={{ label: "إضافة ميعاد", onClick: () => this.setState({ addOpen: true }) }}
                    />
                  }
                >
                  <Column sortable field="dayText" header="اليوم"></Column>
                  {/* four columns headed "التوقيت" told you nothing about
                      which of the four each one was */}
                  <Column sortable field="time" header="التوقيت الأول"></Column>
                  <Column sortable field="time2" header="التوقيت الثاني"></Column>
                  <Column sortable field="time3" header="التوقيت الثالث"></Column>
                  <Column sortable field="time4" header="التوقيت الرابع"></Column>
                  <Column sortable field="lessonLocation.name" header="المكان"></Column>
                  <Column
                    header="الإجراءات"
                    headerStyle={{ width: "124px" }}
                    bodyStyle={{ width: "124px" }}
                    body={actionBodyTemplate}
                  ></Column>
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

export default Schedule;
