import React from "react";
import { Sidebar } from "primereact/sidebar";

// CSS
import "./SlidePanel.scss";

/**
 * A slide-over panel.
 *
 * Presentation and nothing else: it owns no state and runs no logic.
 * `visible` and `onHide` belong to whoever opens it, and whatever is placed
 * inside keeps its own behaviour exactly as it was -- the point of this
 * element is that a form can be mounted in it unchanged.
 *
 * A click on the dimmed area closes it, as does Escape and the ×.
 *
 * The built-in close icon is off, so the × can live in the panel's own
 * header beside the title rather than floating over the content.
 *
 * The header's middle holds the panel's primary action. There are two ways
 * to put one there:
 *
 *  - pass `actions`, when whoever opens the panel also owns the handler
 *    (Materials does: adding a material is one click, not a form submit);
 *  - or portal into `.m-panel-slot` from inside, when the action belongs to
 *    a form that is mounted in the panel and must stay in its React tree
 *    (AddLesson does, so its button keeps the form's own state).
 *
 * blockScroll also matters for the look, not just the scrolling: it is what
 * puts the class on PrimeReact's mask that SlidePanel.scss needs in order to
 * find it. The mask is created imperatively on <body> with only the generic
 * .p-component-overlay class otherwise, which dialogs share.
 */
const SlidePanel = ({ visible, onHide, title, subtitle, actions, children }) => (
  <Sidebar
    visible={visible}
    onHide={onHide}
    position="left"
    className="m-panel"
    blockScroll
    dismissable
    showCloseIcon={false}
  >
    <div className="m-panel-head">
      <div className="m-panel-title">
        <b>{title}</b>
        {subtitle ? <small>{subtitle}</small> : null}
      </div>

      {/* whatever is inside puts its primary action here */}
      <div className="m-panel-slot">{actions}</div>

      <button
        type="button"
        className="m-panel-x"
        onClick={onHide}
        aria-label="إغلاق"
        title="إغلاق"
      >
        <i className="pi pi-times"></i>
      </button>
    </div>

    <div className="m-panel-body">{children}</div>
  </Sidebar>
);

export default SlidePanel;
