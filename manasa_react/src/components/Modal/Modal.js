import React from "react";
import { Dialog } from "primereact/dialog";

// CSS
import "./Modal.scss";

/**
 * A centred dialog, for the short forms that do not earn a slide-over.
 *
 * The rule of thumb: a couple of fields and one decision belong here; a long
 * form with sections and uploads belongs in SlidePanel, because a modal that
 * has to scroll stops being a modal.
 *
 * Presentation only, like SlidePanel. It owns no state and runs no logic --
 * `visible`, `onHide` and whatever is in `actions` belong to the page that
 * opens it, and the content inside keeps its own behaviour untouched.
 *
 * `slot` renders an empty element in the footer for a form mounted inside
 * to portal its submit button into.
 *
 * `busy` is for while its action is in flight: the mask stops dismissing,
 * Escape stops closing and the × is disabled, so a request cannot be
 * abandoned halfway by a stray click on the dim area.
 *
 * `draggable` and `resizable` are off because PrimeReact turns both on by
 * default, which lets a form be dragged half off the screen by its title.
 * The built-in header is off too, so the title, the subtitle and the × can
 * sit in one row of our own rather than PrimeReact's.
 */
const Modal = ({ visible, onHide, title, subtitle, actions, slot, busy, width = 520, children }) => (
  <Dialog
    visible={visible}
    onHide={onHide}
    className="m-modal"
    style={{ width: `min(${width}px, 94vw)` }}
    modal
    draggable={false}
    resizable={false}
    dismissableMask={!busy}
    closeOnEscape={!busy}
    blockScroll
    showHeader={false}
  >
    <div className="m-modal-head">
      <div className="m-modal-title">
        <b>{title}</b>
        {subtitle ? <small>{subtitle}</small> : null}
      </div>

      <button
        type="button"
        className="m-modal-x"
        onClick={onHide}
        disabled={busy}
        aria-label="إغلاق"
        title="إغلاق"
      >
        <i className="pi pi-times"></i>
      </button>
    </div>

    <div className="m-modal-body">{children}</div>

    {/* `actions` is for a button the opener owns; `slot` is for one a form
        inside portals out to, so it can stay in that form's React tree and
        keep its state -- see AddSubscription. */}
    {actions || slot ? (
      <div className="m-modal-foot">
        {actions}
        {slot ? <div className="m-modal-slot"></div> : null}
      </div>
    ) : null}
  </Dialog>
);

export default Modal;
