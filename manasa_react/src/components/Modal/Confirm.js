import React from "react";
import { Button } from "primereact/button";

import Modal from "./Modal";

/**
 * A confirmation, built on Modal.
 *
 * It exists to replace `window.confirm`, which cannot be styled, cannot be
 * read in Arabic by the page's own type, and blocks the browser while it is
 * open. The decision it asks for is the same one: nothing happens until
 * `onConfirm` is called.
 *
 * `tone` is "danger" by default because that is what a confirmation is
 * usually for; pass "brand" for one that is not destructive.
 *
 * While `busy` is true the buttons report the work and the modal refuses to
 * close, so a delete cannot be abandoned between the click and the answer.
 */
const Confirm = ({
  visible,
  onHide,
  onConfirm,
  busy,
  tone = "danger",
  title = "تأكيد",
  message,
  note,
  confirmLabel = "تأكيد",
  busyLabel = "جارٍ التنفيذ…",
  cancelLabel = "إلغاء",
  icon = "pi pi-exclamation-triangle",
}) => (
  <Modal
    visible={visible}
    onHide={onHide}
    title={title}
    busy={busy}
    width={440}
    actions={
      <>
        <Button
          className="p-button-secondary"
          label={cancelLabel}
          disabled={busy}
          onClick={onHide}
        />
        <Button
          className={`m-confirm-go is-${tone}`}
          icon={busy ? "pi pi-spin pi-spinner" : undefined}
          label={busy ? busyLabel : confirmLabel}
          disabled={busy}
          onClick={onConfirm}
        />
      </>
    }
  >
    <div className={`m-confirm is-${tone}`}>
      <span className="m-confirm-ico" aria-hidden="true">
        <i className={icon}></i>
      </span>

      <p className="m-confirm-msg">{message}</p>
      {note ? <p className="m-confirm-note">{note}</p> : null}
    </div>
  </Modal>
);

export default Confirm;
