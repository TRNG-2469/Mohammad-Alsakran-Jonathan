/**
 * Shared frontend logic for the reimbursement app.
 * Talks to the Javalin backend at API_BASE.
 * Stores the logged-in user in localStorage as a fast local cache, but
 * requireAuth() always re-verifies against the real server session via
 * GET /api/me before trusting it.
 */

// Base URL for all backend API calls
const API_BASE = 'http://localhost:7700/api';

// ---------- Auth helpers ----------

// Read the cached logged-in user out of localStorage
function getCurrentUser() {
    const raw = localStorage.getItem('currentUser');
    return raw ? JSON.parse(raw) : null;
}

// Cache the logged-in user in localStorage (never store the password)
function setCurrentUser(user) {
    // Backend already nulls the password on login; keep it out of storage.
    if (user && user.password) {
        user = { ...user, password: null };
    }
    localStorage.setItem('currentUser', JSON.stringify(user));
}

// Remove the cached user from localStorage
function clearCurrentUser() {
    localStorage.removeItem('currentUser');
}

// Check whether the cached current user has manager role
function isManager() {
    const user = getCurrentUser();
    return !!(user && user.role === true);
}

// Redirect to login if there is no real server session. Call at top of protected pages.
async function requireAuth() {
    // Ask the backend who the current session belongs to
    const result = await api('/me');
    if (!result.ok) {
        // No valid session: clear local cache and bounce to login
        clearCurrentUser();
        window.location.href = 'login.html';
        return false;
    }
    // Valid session: refresh local cache with server's copy
    setCurrentUser(result.body);
    return true;
}

// Log the user out server-side and locally, then redirect to login
async function logOut() {
    await api('/logout', { method: 'POST' });
    clearCurrentUser();
    window.location.href = 'login.html';
}

// Fill in the "who am I" name/role display in the nav bar
function renderWhoAmI() {
    const user = getCurrentUser();
    const nameEl = document.getElementById('whoami-name');
    const deptEl = document.getElementById('whoami-dept');
    if (user && nameEl) {
        nameEl.textContent = user.first_name + ' ' + user.last_name;
    }
    if (user && deptEl) {
        deptEl.textContent = user.role ? 'Manager' : 'Employee';
    }
}

/**
 * Hide the Approvals tab for non-managers.
 * Matches the requirement on reimbursements.html, my-requests.html, approvals.html.
 */
function updateApprovalsNav() {
    // Show/hide every Approvals nav link based on manager status
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

// Wrapper around fetch() that prefixes API_BASE, sets JSON headers,
// and safely parses the response body (JSON or plain text)
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

// Format a number as a 2-decimal money string
function formatAmount(n) {
    return Number(n).toFixed(2);
}

/**
 * Build a single receipt-card element from a reimbursement record.
 * Expects fields: reimbursement_id (or id), amount, description, type, status,
 * author_id, resolver_id (optional).
 *
 * showActions  - true on approvals.html, adds Accept / Deny buttons.
 * showEdit     - true on my-requests.html, adds an Edit button (only when
 *                status is PENDING) that reveals an inline edit form for
 *                amount / type / description.
 */
function buildReceiptCard(reimb, showActions, showEdit) {
    // Pull out and normalize the fields we need to render
    const id = reimb.reimbursements_id;
    const status = (reimb.status || 'PENDING').toUpperCase();
    const type = (reimb.type || '').toUpperCase();
    const desc = reimb.description || '';
    const canEdit = !!showEdit && status === 'PENDING';

    // Create the card container
    const card = document.createElement('article');
    card.className = 'receipt-card';
    card.dataset.id = id;

    // Build the card markup: header/amount, description, meta row,
    // optional accept/deny actions, and optional inline edit form
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
        (canEdit
            ? '<div class="receipt-actions">' +
            '  <button type="button" class="btn btn-edit" data-action="edit" data-id="' + id + '">Edit</button>' +
            '</div>' +
            '<div class="edit-form" data-id="' + id + '" hidden>' +
            '  <div class="field"><label>Amount</label><input type="number" step="0.01" min="0.01" class="edit-amount" value="' + reimb.amount + '"></div>' +
            '  <div class="field"><label>Type</label>' +
            '    <select class="edit-type">' +
            '      <option value="TRAVEL"' + (type === 'TRAVEL' ? ' selected' : '') + '>Travel</option>' +
            '      <option value="FOOD"' + (type === 'FOOD' ? ' selected' : '') + '>Food</option>' +
            '      <option value="LODGING"' + (type === 'LODGING' ? ' selected' : '') + '>Lodging</option>' +
            '      <option value="OTHER"' + (type === 'OTHER' ? ' selected' : '') + '>Other</option>' +
            '    </select>' +
            '  </div>' +
            '  <div class="field"><label>Description</label><textarea class="edit-description" rows="3">' + escapeHtml(desc) + '</textarea></div>' +
            '  <p class="edit-error" hidden></p>' +
            '  <div class="edit-actions">' +
            '    <button type="button" class="btn btn-primary edit-save" data-id="' + id + '">Save</button>' +
            '    <button type="button" class="btn edit-cancel" data-id="' + id + '">Cancel</button>' +
            '  </div>' +
            '</div>'
            : '') +
        '</div>' +
        '<span class="stamp" data-status="' + status + '">' + status + '</span>';

    return card;
}

/**
 * Wire up Edit / Save / Cancel clicks for a list of cards built with
 * showEdit = true. Call once on the container after rendering.
 * Delegated, so it works even as cards get added/removed.
 */
function wireEditHandlers(listEl, user) {
    // Single delegated click listener handles edit/cancel/save for every card
    listEl.addEventListener('click', async function (e) {
        const editBtn = e.target.closest('button[data-action="edit"]');
        const cancelBtn = e.target.closest('.edit-cancel');
        const saveBtn = e.target.closest('.edit-save');

        // Reveal the inline edit form for this card
        if (editBtn) {
            const card = editBtn.closest('.receipt-card');
            card.querySelector('.edit-form').hidden = false;
            card.querySelector('.receipt-actions').hidden = true;
            return;
        }

        // Hide the edit form and restore the normal action buttons
        if (cancelBtn) {
            const card = cancelBtn.closest('.receipt-card');
            card.querySelector('.edit-form').hidden = true;
            card.querySelector('.receipt-actions').hidden = false;
            return;
        }

        // Validate and submit the edited reimbursement
        if (saveBtn) {
            const id = saveBtn.dataset.id;
            const card = saveBtn.closest('.receipt-card');
            const form = card.querySelector('.edit-form');
            const errorEl = form.querySelector('.edit-error');
            errorEl.hidden = true;

            // Read the edited values out of the form
            const amount = parseFloat(form.querySelector('.edit-amount').value);
            const type = form.querySelector('.edit-type').value;
            const description = form.querySelector('.edit-description').value.trim();

            // Basic client-side validation before hitting the API
            if (!amount || amount < 0.01 || !type) {
                errorEl.textContent = 'Amount and type are required.';
                errorEl.hidden = false;
                return;
            }

            saveBtn.disabled = true;

            // Send the update, then reload on success or show an error
            try {
                const result = await api('/Reimbursements/' + id, {
                    method: 'PUT',
                    body: JSON.stringify({
                        amount: amount,
                        type: type,
                        description: description,
                        author_id: user.user_id
                    })
                });

                if (result.ok) {
                    window.location.reload();
                } else {
                    errorEl.textContent = 'Could not save changes.';
                    errorEl.hidden = false;
                    saveBtn.disabled = false;
                }
            } catch (err) {
                console.error(err);
                errorEl.textContent = 'Could not save changes.';
                errorEl.hidden = false;
                saveBtn.disabled = false;
            }
        }
    });
}

// Escape special HTML characters to safely inject text into innerHTML
function escapeHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

// Replace a list container's contents with a placeholder "nothing here" message
function showEmptyState(container, message) {
    container.innerHTML = '';
    const empty = document.createElement('div');
    empty.className = 'empty-state';
    empty.textContent = message || 'Nothing here yet.';
    container.appendChild(empty);
}