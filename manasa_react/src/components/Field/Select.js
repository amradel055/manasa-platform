import React from "react";
import { Dropdown } from "primereact/dropdown";

/**
 * The project's dropdown.
 *
 * It is a wrapper around PrimeReact's Dropdown, not a replacement for it.
 * Every prop is passed straight through -- `value`, `options`, `onChange`,
 * `placeholder`, `optionLabel`, `filter`, `disabled`, everything -- so a page
 * that swaps `Dropdown` for `Select` keeps the same value in the same state
 * through the same handler, and keeps the keyboard navigation, the type-ahead
 * and the ARIA wiring that come with it. Writing a listbox from scratch would
 * have meant rebuilding all of that, and that is where behaviour quietly goes
 * missing.
 *
 * All this adds is a class name. The look lives in Field/Form.scss under
 * `.m-select`, so every dropdown in the dashboard can be restyled in one
 * place instead of page by page.
 *
 * An explicit className still works: it is kept and ours is added to it.
 */
const Select = ({ className, ...rest }) => (
  <Dropdown {...rest} className={className ? `m-select ${className}` : "m-select"} />
);

export default Select;
