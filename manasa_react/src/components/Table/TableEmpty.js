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
 * With `loading` it says so instead, in the same shape and the same place.
 * That is deliberate: PrimeReact's own `loading` draws an overlay ON TOP of
 * the rows, which over a blank table is a spinner floating in space. The row
 * PrimeReact gives an empty table is exactly where the answer will appear,
 * so the wait belongs there too -- and the table does not change shape when
 * it ends.
 *
 * `icon` takes a PrimeIcons 4 name. That version is older than most docs
 * assume (`pi-file-edit`, for one, does not exist in it and renders as an
 * empty box), so anything passed here should be checked against the copy in
 * node_modules first.
 */
const TableEmpty = ({ title, hint, action, loading, icon = "pi pi-inbox" }) => (
  <div className={loading ? "m-empty is-loading" : "m-empty"}>
    <span className="m-empty-ico" aria-hidden="true">
      {loading ? <i className="m-empty-spin"></i> : <i className={icon}></i>}
    </span>

    <b>{loading ? "جارٍ التحميل…" : title}</b>
    {loading ? (
      <span className="m-empty-hint">لحظة واحدة، يتم جلب البيانات</span>
    ) : hint ? (
      <span className="m-empty-hint">{hint}</span>
    ) : null}

    {/* same shape as the page header's action: a url goes somewhere, an
        onClick does something here */}
    {!loading && action ? (
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
