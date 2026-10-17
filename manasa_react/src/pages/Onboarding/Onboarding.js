import React, { Component, createRef } from "react";

// CSS
import "./Onboarding.scss";

// Components
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import Select from '../../components/Field/Select';
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";

// Helper
import { apiFetch, isLoggedIn, getCurrentUser } from "../../Helper/Api";

// Properties
const properties = require("../../properties.json");

const STEPS = [
  { id: 1, title: "المراحل الدراسية" },
  { id: 2, title: "المواد" },
  { id: 3, title: "الفترات الدراسية" },
  { id: 4, title: "رابطك الخاص" },
];

class Onboarding extends Component {
  constructor(props) {
    super(props);
    this.toast = createRef(null);
    this.subdomainCheckTimer = null;
  }

  state = {
    loading: true,
    step: 1,

    schoolStages: [],
    materials: [],
    periods: [],

    newStageName: "",
    selectedStageId: null,
    newMaterialName: "",
    selectedMaterialId: null,
    newPeriodName: "",

    subdomain: "",
    subdomainAvailable: null,
    subdomainChecking: false,
    savingSubdomain: false,
    subdomainSaved: null,
  };

  componentDidMount() {
    document.title = properties.site_name + " | تفعيل الحساب";
    if (!isLoggedIn()) {
      window.location.href = "/";
      return;
    }
    this.loadAll();
  }

  loadAll = async () => {
    try {
      const user = getCurrentUser();
      const [status, schoolStages, materials, periods] = await Promise.all([
        apiFetch("/teachers/me/onboarding-status"),
        apiFetch("/lookup/school-stages/by-teacher", { query: { teacherUserId: user.userId } }),
        apiFetch("/lookup/materials", { query: { branchId: properties.branch_id, teacherUserId: user.userId } }),
        apiFetch("/periods"),
      ]);

      if (status.complete) {
        window.location.href = "/lessons";
        return;
      }

      let step = 1;
      if (schoolStages.length > 0) step = 2;
      if (materials.length > 0) step = 3;
      if (periods.length > 0) step = 4;

      this.setState({
        loading: false,
        step,
        schoolStages,
        materials,
        periods,
        subdomain: status.subdomain || "",
        subdomainSaved: status.subdomain || null,
        selectedStageId: schoolStages[0] ? schoolStages[0].id : null,
        selectedMaterialId: materials[0] ? materials[0].id : null,
      });
    } catch (error) {
      console.error("Error loading onboarding state:", error);
      this.setState({ loading: false });
    }
  };

  // ---- Step 1: school stages ----
  addStage = async () => {
    const name = this.state.newStageName.trim();
    if (!name) return;
    try {
      const stage = await apiFetch("/lookup/school-stages", {
        method: "POST",
        body: { name, branchId: properties.branch_id },
      });
      this.setState((s) => ({
        schoolStages: [...s.schoolStages, stage],
        newStageName: "",
        selectedStageId: s.selectedStageId || stage.id,
      }));
    } catch (error) {
      this.toast.current.show({ severity: "error", summary: "تعذرت الإضافة", detail: error.message });
    }
  };

  removeStage = async (id) => {
    try {
      await apiFetch(`/lookup/school-stages/${id}`, { method: "DELETE" });
      this.setState((s) => ({ schoolStages: s.schoolStages.filter((x) => x.id !== id) }));
    } catch (error) {
      this.toast.current.show({ severity: "error", summary: "تعذر الحذف", detail: error.message });
    }
  };

  // ---- Step 2: materials ----
  addMaterial = async () => {
    const name = this.state.newMaterialName.trim();
    if (!name || !this.state.selectedStageId) return;
    try {
      const material = await apiFetch("/lookup/materials", {
        method: "POST",
        body: { name, stageId: this.state.selectedStageId, branchId: properties.branch_id },
      });
      this.setState((s) => ({
        materials: [...s.materials, material],
        newMaterialName: "",
        selectedMaterialId: s.selectedMaterialId || material.id,
      }));
    } catch (error) {
      this.toast.current.show({ severity: "error", summary: "تعذرت الإضافة", detail: error.message });
    }
  };

  removeMaterial = async (id) => {
    try {
      await apiFetch(`/lookup/materials/${id}`, { method: "DELETE" });
      this.setState((s) => ({ materials: s.materials.filter((x) => x.id !== id) }));
    } catch (error) {
      this.toast.current.show({ severity: "error", summary: "تعذر الحذف", detail: error.message });
    }
  };

  // ---- Step 3: periods ----
  addPeriod = async () => {
    const name = this.state.newPeriodName.trim();
    if (!name || !this.state.selectedMaterialId) return;
    try {
      const period = await apiFetch("/periods", {
        method: "POST",
        body: { name, branchId: properties.branch_id, materialId: this.state.selectedMaterialId },
      });
      this.setState((s) => ({ periods: [...s.periods, period], newPeriodName: "" }));
    } catch (error) {
      this.toast.current.show({ severity: "error", summary: "تعذرت الإضافة", detail: error.message });
    }
  };

  removePeriod = async (id) => {
    try {
      await apiFetch(`/periods/${id}`, { method: "DELETE" });
      this.setState((s) => ({ periods: s.periods.filter((x) => x.id !== id) }));
    } catch (error) {
      this.toast.current.show({ severity: "error", summary: "تعذر الحذف", detail: error.message });
    }
  };

  // ---- Step 4: subdomain ----
  onSubdomainChange = (value) => {
    const clean = value.toLowerCase().replace(/[^a-z0-9-]/g, "");
    this.setState({ subdomain: clean, subdomainAvailable: null });
    clearTimeout(this.subdomainCheckTimer);
    if (clean.length < 3) return;
    this.subdomainCheckTimer = setTimeout(async () => {
      this.setState({ subdomainChecking: true });
      try {
        const res = await apiFetch("/teachers/subdomain-available", { query: { value: clean } });
        this.setState({ subdomainAvailable: res.available, subdomainChecking: false });
      } catch (error) {
        this.setState({ subdomainChecking: false });
      }
    }, 400);
  };

  saveSubdomain = async () => {
    const { subdomain } = this.state;
    if (!subdomain || subdomain.length < 3) {
      this.toast.current.show({ severity: "error", summary: "رابط غير صالح", detail: "3 أحرف على الأقل" });
      return;
    }
    this.setState({ savingSubdomain: true });
    try {
      await apiFetch("/teachers/me/subdomain", { method: "PUT", body: { subdomain } });
      this.toast.current.show({ severity: "success", summary: "تم بنجاح", detail: "تم تفعيل حسابك بالكامل!" });
      setTimeout(function () {
        window.location.href = `/lessons`;
      }, 1000);
    } catch (error) {
      this.setState({ savingSubdomain: false });
      this.toast.current.show({ severity: "error", summary: "تعذر الحفظ", detail: error.message });
    }
  };

  goToStep = (step) => this.setState({ step });

  render() {
    const { loading, step } = this.state;

    if (loading) {
      return <div className="onboarding-page loading">...جاري التحميل</div>;
    }

    return (
      <div className="onboarding-page">
        <Toast ref={this.toast}></Toast>
        <div className="container">
          <img
            className="logo"
            src={require(`../../assets/img/${properties.teacher.logo}`).default}
            alt={properties.site_name}
          />
          <h1>خطوات أخيرة لتفعيل حسابك</h1>

          <div className="steps-indicator">
            {STEPS.map((s) => (
              <div
                key={s.id}
                className={`step-dot ${step === s.id ? "active" : ""} ${step > s.id ? "done" : ""}`}
                onClick={() => (step > s.id ? this.goToStep(s.id) : null)}
              >
                <span className="dot">{step > s.id ? "✓" : s.id}</span>
                <span className="label">{s.title}</span>
              </div>
            ))}
          </div>

          <Card className="step-card">
            {step === 1 && this.renderStep1()}
            {step === 2 && this.renderStep2()}
            {step === 3 && this.renderStep3()}
            {step === 4 && this.renderStep4()}
          </Card>
        </div>
      </div>
    );
  }

  renderStep1() {
    const { schoolStages, newStageName } = this.state;
    return (
      <div className="step-content">
        <h2>1. أضف المراحل الدراسية اللي بتدرّسها</h2>
        <p className="step-desc">مثال: الصف الأول الثانوي، الصف الثاني الإعدادي...</p>
        <div className="add-row">
          <InputText
            value={newStageName}
            placeholder="اسم المرحلة الدراسية"
            onChange={(e) => this.setState({ newStageName: e.target.value })}
            onKeyDown={(e) => e.key === "Enter" && this.addStage()}
          />
          <Button label="إضافة" onClick={this.addStage} />
        </div>
        <ul className="item-list">
          {schoolStages.map((s) => (
            <li key={s.id}>
              <span>{s.name}</span>
              <i className="pi pi-times" onClick={() => this.removeStage(s.id)}></i>
            </li>
          ))}
          {schoolStages.length === 0 && <li className="empty">لسه مفيش مراحل دراسية مضافة</li>}
        </ul>
        <Button
          className="next-button"
          label="التالي"
          disabled={schoolStages.length === 0}
          onClick={() => this.goToStep(2)}
        />
      </div>
    );
  }

  renderStep2() {
    const { schoolStages, materials, selectedStageId, newMaterialName } = this.state;
    const stageOptions = schoolStages.map((s) => ({ label: s.name, value: s.id }));
    return (
      <div className="step-content">
        <h2>2. أضف المواد لكل مرحلة</h2>
        <p className="step-desc">اختر المرحلة الدراسية، وسمّي المادة اللي هتدرّسها فيها</p>
        <Select
          value={selectedStageId}
          options={stageOptions}
          onChange={(e) => this.setState({ selectedStageId: e.value })}
          placeholder="اختر المرحلة الدراسية"
        />
        <div className="add-row">
          <InputText
            value={newMaterialName}
            placeholder="اسم المادة"
            onChange={(e) => this.setState({ newMaterialName: e.target.value })}
            onKeyDown={(e) => e.key === "Enter" && this.addMaterial()}
          />
          <Button label="إضافة" onClick={this.addMaterial} disabled={!selectedStageId} />
        </div>
        <ul className="item-list">
          {materials.map((m) => {
            const stage = schoolStages.find((s) => s.id === m.stageId);
            return (
              <li key={m.id}>
                <span>
                  {m.name} <small>({stage ? stage.name : ""})</small>
                </span>
                <i className="pi pi-times" onClick={() => this.removeMaterial(m.id)}></i>
              </li>
            );
          })}
          {materials.length === 0 && <li className="empty">لسه مفيش مواد مضافة</li>}
        </ul>
        <div className="nav-row">
          <Button className="p-button-text" label="السابق" onClick={() => this.goToStep(1)} />
          <Button
            className="next-button"
            label="التالي"
            disabled={materials.length === 0}
            onClick={() => this.goToStep(3)}
          />
        </div>
      </div>
    );
  }

  renderStep3() {
    const { materials, periods, selectedMaterialId, newPeriodName } = this.state;
    const materialOptions = materials.map((m) => ({ label: m.name, value: m.id }));
    return (
      <div className="step-content">
        <h2>3. أنشئ الفترات الدراسية</h2>
        <p className="step-desc">الفترة هي اللي هيشترك فيها الطالب لمادة معيّنة (زي "الترم الأول")</p>
        <Select
          value={selectedMaterialId}
          options={materialOptions}
          onChange={(e) => this.setState({ selectedMaterialId: e.value })}
          placeholder="اختر المادة"
        />
        <div className="add-row">
          <InputText
            value={newPeriodName}
            placeholder="اسم الفترة الدراسية"
            onChange={(e) => this.setState({ newPeriodName: e.target.value })}
            onKeyDown={(e) => e.key === "Enter" && this.addPeriod()}
          />
          <Button label="إضافة" onClick={this.addPeriod} disabled={!selectedMaterialId} />
        </div>
        <ul className="item-list">
          {periods.map((p) => (
            <li key={p.id}>
              <span>{p.name}</span>
              <i className="pi pi-times" onClick={() => this.removePeriod(p.id)}></i>
            </li>
          ))}
          {periods.length === 0 && <li className="empty">لسه مفيش فترات دراسية مضافة</li>}
        </ul>
        <div className="nav-row">
          <Button className="p-button-text" label="السابق" onClick={() => this.goToStep(2)} />
          <Button
            className="next-button"
            label="التالي"
            disabled={periods.length === 0}
            onClick={() => this.goToStep(4)}
          />
        </div>
      </div>
    );
  }

  renderStep4() {
    const { subdomain, subdomainAvailable, subdomainChecking, savingSubdomain } = this.state;
    return (
      <div className="step-content">
        <h2>4. اختر رابطك الخاص</h2>
        <p className="step-desc">
          ده الرابط اللي طلابك هيدخلوا بيه على صفحتك الخاصة. اختر اسم مميز ومش
          هيتكرر مع حد تاني.
        </p>
        <div className="subdomain-input">
          <span className="suffix">.igma-it.com</span>
          <InputText
            value={subdomain}
            placeholder="ahmed-math"
            onChange={(e) => this.onSubdomainChange(e.target.value)}
          />
        </div>
        <div className="availability-msg">
          {subdomainChecking && <span className="checking">...جاري التأكد من الإتاحة</span>}
          {!subdomainChecking && subdomainAvailable === true && (
            <span className="available">✓ متاح</span>
          )}
          {!subdomainChecking && subdomainAvailable === false && (
            <span className="unavailable">✗ محجوز، جرّب اسم تاني</span>
          )}
        </div>
        <div className="nav-row">
          <Button className="p-button-text" label="السابق" onClick={() => this.goToStep(3)} />
          <Button
            className="next-button"
            label={savingSubdomain ? "جاري التفعيل..." : "تفعيل الحساب"}
            disabled={savingSubdomain || subdomain.length < 3 || subdomainAvailable === false}
            onClick={this.saveSubdomain}
          />
        </div>
      </div>
    );
  }
}

export default Onboarding;
