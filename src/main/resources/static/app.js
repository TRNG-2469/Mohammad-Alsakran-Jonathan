/**
 * Shared frontend logic for the reimbursement app.
 * Talks to the Javalin backend at 'hardcoded'.
 * Stores the logged-in user in localStorage so pages can enforce
 * auth and the manager-only Approvals tab without server-side rendering.
 */

  //const API_BASE = 'removed hardcoded value';

// ---------- Auth helpers ----------

function getCurrentUser() {
    const raw = localStorage.getItem('currentUser');
    return raw ? JSON.parse(raw) : null;
}

function setCurrentUser(user) {
    // Backend already nulls the password on login; keep it out of storage.
    if (user && user.password) {
        user = { ...user, password: null };
    }
    localStorage.setItem('currentUser', JSON.stringify(user));
}

function clearCurrentUser() {
    localStorage.removeItem('currentUser');
}

function isManager() {
    const user = getCurrentUser();
    return !!(user && user.role === true);
}

/** Redirect to login if there is no session. Call at top of protected pages. */
function requireAuth() {
    if (!getCurrentUser()) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

/**
 * Hide the Approvals tab for non-managers.
 * Matches the requirement on reimbursements.html, my-requests.html, approvals.html.
 */
function updateApprovalsNav() {
    document.querySelectorAll('a.tab[href="approvals.html"]').forEach(function (link) {
        if (!isManager()) {
            link.style.display = 'none';
            link.setAttribute('aria-hidden', 'true');
        } else {
            link.style.display = '';
            link.removeAttribute('aria-hidden');
        }
    });
}

// ---------- Generic fetch helper ----------

async function api(path, options) {
    const opts = options || {};
    opts.headers = Object.assign(
        { 'Content-Type': 'application/json', Accept: 'application/json' },
        opts.headers || {}
    );
    const res = await fetch(API_BASE + path, opts);
    let body = null;
    const text = await res.text();
    if (text) {
        try {
            body = JSON.parse(text);
        } catch (_) {
            body = text;
        }
    }
    return { ok: res.ok, status: res.status, body: body };
}

// ---------- Receipt card renderer (used by my-requests & approvals) ----------

function formatAmount(n) {
    return Number(n).toFixed(2);
}

/**
 * Build a single receipt-card element from a reimbursement record.
 * Expects fields: reimbursement_id (or id), amount, description, type, status,
 * author_id, resolver_id (optional).
 * When showActions is true, Accept / Deny buttons are added (approvals page).
 */
function buildReceiptCard(reimb, showActions) {
    const id = reimb.reimbursement_id != null ? reimb.reimbursement_id : reimb.id;
    const status = (reimb.status || 'PENDING').toUpperCase();
    const type = (reimb.type || '').toUpperCase();
    const desc = reimb.description || '';

    const card = document.createElement('article');
    card.className = 'receipt-card';
    card.dataset.id = id;

    card.innerHTML =
        '<div class="receipt-perforation" aria-hidden="true"></div>' +
        '<div class="receipt-body">' +
        '  <div class="receipt-top">' +
        '    <span class="receipt-type">' + escapeHtml(type) + '</span>' +
        '    <span class="receipt-amount">$' + formatAmount(reimb.amount) + '</span>' +
        '  </div>' +
        '  <p class="receipt-description">' + escapeHtml(desc) + '</p>' +
        '  <div class="receipt-meta">' +
        '    <span>#' + id + '</span>' +
        '    <span>Author #' + (reimb.author_id != null ? reimb.author_id : '—') + '</span>' +
        '  </div>' +
        (showActions
            ? '<div class="receipt-actions">' +
            '  <button type="button" class="btn btn-approve" data-action="accept" data-id="' + id + '">Accept</button>' +
            '  <button type="button" class="btn btn-deny" data-action="deny" data-id="' + id + '">Deny</button>' +
            '</div>'
            : '') +
        '</div>' +
        '<span class="stamp" data-status="' + status + '">' + status + '</span>';

    return card;
}

function escapeHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function showEmptyState(container, message) {
    container.innerHTML = '';
    const empty = document.createElement('div');
    empty.className = 'empty-state';
    empty.textContent = message || 'Nothing here yet.';
    container.appendChild(empty);
}
