// Shared API helper for talking to the new Hiilt gateway (user-service auth +
// manasa-service). Replaces the old flat fetch()-per-page pattern that called
// Payara directly: every manasa-service response is now an ApiResponse
// envelope ({success, payload, error}), and every request (except login)
// needs "Authorization: Bearer <token>".

const properties = require("../properties.json");

const TOKEN_KEY = "manasa_token";
const USER_KEY = "manasa_user";

export function getToken() {
  return window.localStorage.getItem(TOKEN_KEY);
}

export function getCurrentUser() {
  const raw = window.localStorage.getItem(USER_KEY);
  return raw ? JSON.parse(raw) : null;
}

export function isLoggedIn() {
  return !!getToken();
}

export function saveSession(token, user) {
  window.localStorage.setItem(TOKEN_KEY, token);
  window.localStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function clearSession() {
  window.localStorage.removeItem(TOKEN_KEY);
  window.localStorage.removeItem(USER_KEY);
}

function buildQuery(query) {
  if (!query) return "";
  const parts = Object.entries(query)
    .filter(([, v]) => v !== null && v !== undefined && v !== "")
    .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(v)}`);
  return parts.length ? `?${parts.join("&")}` : "";
}

/**
 * Calls manasa-service through the gateway. `path` is relative to
 * properties.api_path, e.g. "/lessons" or "/lookup/governorates".
 * Unwraps the ApiResponse envelope and returns just the payload; redirects
 * to the login page on 401 (expired/missing token).
 */
export async function apiFetch(path, { method = "GET", body, query } = {}) {
  const url = properties.api_path + path + buildQuery(query);
  const headers = { Accept: "application/json" };
  const token = getToken();
  if (token) headers.Authorization = `Bearer ${token}`;
  if (body !== undefined) headers["Content-Type"] = "application/json";

  const res = await fetch(url, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  if (res.status === 401) {
    clearSession();
    window.location.href = "/";
    throw new Error("Unauthorized");
  }

  if (res.status === 204) return null;

  const json = await res.json();
  if (!json.success) {
    throw new Error((json.error && json.error.message) || "حدث خطأ غير متوقع");
  }
  return json.payload;
}

/** Calls user-service's login endpoint (not gateway-routed under /api/manasa). */
export async function login(email, password) {
  const res = await fetch(properties.auth_path + "/login", {
    method: "POST",
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    body: JSON.stringify({ email, password }),
  });
  const json = await res.json();
  if (!res.ok || !json.success) {
    throw new Error((json.error && json.error.message) || "بيانات الدخول غير صحيحة");
  }
  return json.payload; // { token, userId, email, fullName, role }
}

/** Teachers still log in with their old short user_code; the migration gave
 *  every teacher a synthesized "<user_code>@manasa.local" email. */
export function toLoginEmail(usernameOrEmail) {
  const value = (usernameOrEmail || "").trim();
  return value.includes("@") ? value : `${value}@manasa.local`;
}
