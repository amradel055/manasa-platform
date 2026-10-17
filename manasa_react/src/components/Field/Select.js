import React, { useState, useEffect } from "react";
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
 * It adds a class name and three props of its own:
 *
 *  - `onOpen` runs when the list is opened, for fetching its options then
 *    rather than on mount. A list fetched once at mount is a snapshot that
 *    goes stale the moment anything is added somewhere else.
 *  - `needs` is the value this list depends on -- the stage a material
 *    belongs to, the material a period belongs to. While it is empty the
 *    field is disabled and says what to choose first (`needsLabel`), rather
 *    than opening onto an empty list and leaving the reader to work out why.
 *    Disabled also means it cannot be opened, so `onOpen` cannot fire with
 *    nothing to ask the server for -- the guard and the explanation are the
 *    same thing.
 *  - `searchable` puts the search box back inside the list. It is off by
 *    default, and `filter` passed from a page is ignored -- see the note
 *    beside it below.
 *  - `loading` says the options are on their way: the trigger turns into a
 *    spinner, the field reads "جارٍ التحميل…", and the open list says the
 *    same where "no options" would go.
 *
 * `loading` deliberately does NOT disable the field: with `onOpen` the fetch
 * starts BECAUSE it was opened, so disabling it would close the list that
 * triggered the request.
 *
 * ---- the flash this component exists to stop ----
 *
 * A page raises its loading flag inside its fetch, which is one render away:
 * the list opens, React paints it with no options and the flag still false,
 * and for that one frame it reads "no options" at a list that is about to be
 * full. So the open is remembered here, synchronously, and counts as waiting
 * until the page's own flag takes over.
 */
const Select = ({
  className,
  panelClassName,
  loading,
  onOpen,
  onShow,
  onHide,
  placeholder,
  emptyMessage,
  emptyFilterMessage,
  needs,
  needsLabel,
  disabled,

  // Swallowed on purpose. Pages all over this project pass `filter` to
  // PrimeReact, which puts a search box inside the open list -- a second
  // input on top of a list of a handful of items, and one more thing to
  // look at. The list itself already responds to typing: PrimeReact
  // matches as you type with the list open, which is what the search box
  // was doing anyway.
  //
  // A list long enough to genuinely need searching can opt back in with
  // `searchable`; nothing is lost, it just stops being the default.
  filter,
  filterBy,
  filterPlaceholder,
  searchable,

  ...rest
}) => {
  // true from the moment the list is opened until the page's own flag is up
  const [opening, setOpening] = useState(false);

  useEffect(() => {
    if (loading) setOpening(false);
  }, [loading]);

  // `needs` is only honoured when the caller passed it at all, so a list with
  // no parent is never accidentally blocked
  const blocked = needs !== undefined && !needs;
  const waiting = Boolean(loading || opening);

  const classes = ["m-select"];
  if (waiting) classes.push("is-loading");
  if (blocked) classes.push("is-blocked");
  if (className) classes.push(className);

  const panel = ["m-select-panel"];
  if (waiting) panel.push("is-loading");
  if (panelClassName) panel.push(panelClassName);

  return (
    <Dropdown
      {...rest}
      filter={searchable ? true : undefined}
      filterBy={searchable ? filterBy : undefined}
      filterPlaceholder={searchable ? filterPlaceholder : undefined}
      disabled={blocked || disabled}
      placeholder={
        waiting
          ? "جارٍ التحميل…"
          : blocked
          ? needsLabel || "اختر ما قبله أولاً"
          : placeholder
      }
      emptyMessage={waiting ? "جارٍ التحميل…" : emptyMessage || "لا توجد خيارات بعد"}
      emptyFilterMessage={
        waiting ? "جارٍ التحميل…" : emptyFilterMessage || "لا نتائج تطابق بحثك"
      }
      onShow={(e) => {
        if (onOpen) {
          // set before the fetch, so the first paint of the open list
          // already knows something is coming
          setOpening(true);
          onOpen();
        }
        // a page that wanted onShow for its own reasons still gets it
        if (onShow) onShow(e);
      }}
      onHide={(e) => {
        setOpening(false);
        if (onHide) onHide(e);
      }}
      className={classes.join(" ")}
      panelClassName={panel.join(" ")}
    />
  );
};

export default Select;
