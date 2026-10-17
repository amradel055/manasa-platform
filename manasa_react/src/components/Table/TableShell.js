import React from "react";

// CSS
import "./TableShell.scss";

/**
 * Presentation only.
 *
 * It renders one <div> around whatever is put inside it and does nothing
 * else: it reads no props besides `children`, touches no data, and neither
 * wraps nor replaces any PrimeReact behaviour. Sorting, filtering, paging,
 * the global filter and every handler stay exactly where they were, on the
 * DataTable itself. Remove this element and the page still works -- it just
 * looks like it did before.
 *
 * What the element buys is a scope. TableShell.scss restyles PrimeReact's
 * table into rows that read as separate cards, and because every rule is
 * written inside `.m-table`, it reaches only the tables that are wrapped.
 * The other dashboard pages keep their current table until one is wrapped
 * on purpose.
 */
const TableShell = ({ children }) => <div className="m-table">{children}</div>;

export default TableShell;
