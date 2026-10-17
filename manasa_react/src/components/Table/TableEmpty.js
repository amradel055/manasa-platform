import React from "react";

/**
 * Presentation only, like TableShell.
 *
 * PrimeReact's `emptyMessage` renders whatever it is given inside the one
 * cell of the empty row, so this is handed to it as a node instead of a
 * string. It adds to what was there -- the original sentence stays, as the
 * title -- and reads no state, fetches nothing, and decides nothing about
 * when it appears. The DataTable still decides that, exactly as before.
 *
 * `icon` takes a PrimeIcons 4 name. That version is older than most docs
 * assume (`pi-file-edit`, for one, does not exist in it and renders as an
 * empty box), so anything passed here should be checked against the copy in
 * node_modules first.
 */
const TableEmpty = ({ title, hint, action, icon = "pi pi-inbox" }) => (
  <div className="m-empty">
    <span className="m-empty-ico" aria-hidden="true">
      <i className={icon}></i>
    </span>

    <b>{title}</b>
    {hint ? <span className="m-empty-hint">{hint}</span> : null}

    {/* same shape as the page header's action: a url goes somewhere, an
        onClick does something here */}
    {action ? (
      action.onClick ? (
        <button type="button" className="m-empty-act" onClick={action.onClick}>
          <i className="pi pi-plus"></i>
          <span>{action.label}</span>
        </button>
      ) : (
        <a className="m-empty-act" href={action.url}>
          <i className="pi pi-plus"></i>
          <span>{action.label}</span>
        </a>
      )
    ) : null}
  </div>
);

export default TableEmpty;
