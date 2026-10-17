import React, { Component } from "react";

// CSS
import "./Header.scss";

/**
 * The title strip at the top of a dashboard page.
 *
 * This used to carry the whole menu. The rail, the logout button and the
 * PrimeReact theme imports now live in pages/Dashboard/DashboardLayout.js,
 * which wraps every dashboard route -- so the menu is no longer rebuilt on
 * each page change. The component keeps its name and its `pageName` prop so
 * all 33 pages that render it are untouched.
 *
 * It now also takes an optional `action` for the
 * one thing a page is there to let you do, shown at the far end of its own
 * title. Optional on purpose: a page that passes nothing renders exactly the
 * strip it rendered before.
 */
class Header extends Component {
  render() {
    const { pageName, action } = this.props;

    return (
      <div className="page-head">
        <div className="ph-text">
          <h1>{pageName}</h1>
        </div>

        {/* `action` is { label, icon, url } to go somewhere, or
            { label, icon, onClick } to do something where you stand --
            a panel, for one. Whichever it is decides the element. */}
        {action ? (
          action.onClick ? (
            <button type="button" className="ph-action" onClick={action.onClick}>
              <i className={action.icon || "pi pi-plus"}></i>
              <span>{action.label}</span>
            </button>
          ) : (
            <a className="ph-action" href={action.url}>
              <i className={action.icon || "pi pi-plus"}></i>
              <span>{action.label}</span>
            </a>
          )
        ) : null}
      </div>
    );
  }
}

export default Header;
