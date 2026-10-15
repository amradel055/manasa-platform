// Lets a developer point the app at a different gateway without editing (and
// accidentally committing) properties.json. The values in properties.json stay
// the defaults, so leaving these unset changes nothing for anyone else.
//
// Set them per machine in manasa-platform/.env (git-ignored), e.g. on a Windows
// box where "localhost" resolves to ::1 and reaches the wrong local server:
//   REACT_APP_API_PATH=http://127.0.0.1:8080/api/manasa
//   REACT_APP_AUTH_PATH=http://127.0.0.1:8080/api/auth
//
// Imported first in index.js: every caller reads properties.api_path inside a
// function at request time, so overwriting the object here is enough.
const properties = require("./properties.json");

if (process.env.REACT_APP_API_PATH) {
  properties.api_path = process.env.REACT_APP_API_PATH;
}

if (process.env.REACT_APP_AUTH_PATH) {
  properties.auth_path = process.env.REACT_APP_AUTH_PATH;
}

export default properties;
