# Prompt Log — Ledgerline Expense Reimbursement System

A record of the prompts given in this conversation, in order, with a short note on what each produced.
 
---

### 1. Initial build request
> "you are a frontend developer for a java project. Write Html and css for the application, use javascript only if necessary. the application is an expense reimbursement system. The home page should contain a way for employees and managers to create a new reimbursement requests, and the ability to approve or deny reimbursement requests if the user is a manager. the way to tell if a user is a manager is saved in the database as boolean role... [Postgres schema for Department, Users, Reimbursements provided]"

**Output:** First version of the app — `index.html`, `styles.css`, `app.js`. A single-page interface (Fraunces/IBM Plex ledger-and-receipt visual design) with a new-request form, a "my requests" list, and a manager-only "approvals" list, wired up with `fetch`-based JavaScript against assumed REST endpoints.
 
---

### 2. Remove JavaScript, restructure as multi-page, add comments
> "some additional context. This is a local project. no reimbursement will affect real bank accounts. This is meant as a project, javascript isn't something that has been learned, so do not add javascript for now, instead add comments where and how it would be relevant. Add comments throughout the code explaining its purpose."

**Output:** Dropped `app.js` entirely. Rebuilt as three plain server-rendered-style HTML pages — `index.html` (new request), `my-requests.html`, `approvals.html` — sharing `styles.css`. Tabs became plain `<a>` links; approve/deny became real `<form method="post">` submissions. Extensive comments added throughout explaining what a Java/JSP backend (servlets, JSTL loops, session-based role checks) would need to do to make each part dynamic.
 
---

### 3. Add login/register, keep excluding JavaScript
> "lets implement login/register an account functionality. We're still going to exclude the correlated javascript for now. implement the style sheet you just produced. all attributes labeled 'not null' in the Users table should be instantiated in the register logic."

**Output:** Added `login.html` and `register.html`, generalized `.request-form` into a reusable `.form-card` CSS class, and added a logout `<form>` to the header of the three signed-in pages. `register.html`'s comments map every `NOT NULL` column on `Users` (`username`, `password`, `role`, `first_name`, `last_name`, `department_id`) to exactly how the servlet's `INSERT` should populate it — notably, `role` is deliberately **not** a form field and is set explicitly to `false` server-side to avoid a self-registration privilege-escalation bug.

Ended by flagging an open question: nothing yet stops an unauthenticated user from loading the signed-in pages directly by URL, since that has to be enforced server-side.
 
---

## 4. Initial context (do not respond)

**Prompt:**
> Context: these are the front end of my project. Prompt: do not respond yet

*(Attached: approvals.html, login.html, my-requests.html, register.html, reimbursements.html, styles.css)*

**Outcome:** Acknowledged only. No analysis or implementation performed.

---

## 5. Implement frontend against backend

**Prompt:**
> This is part of the backend of my project. I need to implement my front end so it will connect to my backend and subsequently the database. the html files I have given have comments at the bottom of the files that have specific instructions for that file. I need to implement the changes described in those files. change ONLY the html files. if you want, you can create a new file for the implementation. add comments where you make changes describing the change. This backend content (MainClass, ConnectionFactory, etc) are for context. DO NOT EDIT styles.css

*(Attached: MainClass.java, ConnectionFactory.java, UsersServiceImpl.java, UsersDAOImpl.java, UsersController.java)*

**Outcome:** Frontend implemented to call the existing Javalin API:
- Created shared `app.js` (session helpers, `api()` wrapper, receipt-card renderer)
- Updated `login.html`, `register.html`, `reimbursements.html`, `my-requests.html`, `approvals.html` per the instructions in each file’s trailing comments
- Approvals tab hidden unless `role === true`
- Login / register / create reimbursement / my-requests list / pending approvals + Accept/Deny wired to `/api/*` endpoints
- Change sites marked with `<!-- CHANGE: ... -->`
- `styles.css` left untouched

---

## 6. Initial bug audit

**Prompt:**
> You are a Java full-stack developer. Assume that all intended functionality is implemented but not running correctly. Go through these files and report what needs to be fixed. Do not implement the fixes yet, list them in order of how critical the errors are.

**Outcome:** Full-project review across HTML/JS/Java/SQL. Findings ranked critical → low, including:
- `login.html` — believed auth-header/JSON mismatch (later retracted as a false positive)
- `reimbursements.html` — missing `id="reimb-status"` causing the submit handler to throw
- `login.html` / `register.html` — missing `id="login-error"` / `id="register-error"`
- Reimbursement type mismatch between frontend dropdown and backend validation (later retracted as a false positive)
- No CORS configuration in `MainClass.java`
- `app.js`'s `api()` helper missing `credentials: 'include'`
- `/Reimbursements/filter` endpoint requiring `department_id` with no null check
- `resolveReimbursement()` overriding `resolver_id` from session instead of trusting the request body
- Register page mislabeling all failures as "username already taken"
- Minor: unused imports (`NotNull`, unused `MainClass.java` imports)
---

## 7. Walkthrough of one fix

**Prompt:**
> 2. `reimbursements.html` references a DOM id that doesn't exist... show me how you would fix this

**Outcome:** Provided the exact one-line HTML fix (`id="reimb-status"`), explained why editing the JS instead was riskier (shared `.form-status` class across pages), and extended the same fix pattern to `login-error` and `register-error`.
 
---

## 8. Re-audit after first round of fixes

**Prompt:**
> Ive just uploaded the changed files. Go through these files and report what needs to be fixed... reference the previous issues you found and report if any have been resolved

**Outcome:** No files had actually been uploaded — flagged the empty uploads folder and asked for a re-upload.
 
---

## 9. Re-audit via project files

**Prompt:**
> the files you saw originally were deleted from the project and I uploaded the replacements. Look in the project for the files. reexamine them and see if any errors have been fixed

**Outcome:** Re-read all files directly from `/mnt/project/`. Confirmed the three DOM-id fixes landed. Retracted the two false-positive findings (login auth format, reimbursement type mismatch — both were already correct in the original). Confirmed CORS, credentials, resolve-endpoint, and filter-endpoint issues were still unresolved. Re-ranked remaining issues by severity.
 
---

## 10. Second re-audit

**Prompt:**
> the files in project have been updated again. repeat the process you have done before

**Outcome:** Re-checked every file against the prior version; found all files were byte-identical re-uploads (no substantive edits). Reconfirmed the same outstanding issue list and ranking.
 
---

## 11. Sort-order bug report and fix

**Prompt:**
> the text in my-requests.html says that the requests would be ordered from newest to oldest but it is currently the opposite. can you help me fix that

**Outcome:** Root-caused a field-name typo (`a.reimbursement_id` vs. actual `reimbursements_id`) causing the sort comparator to always return `0`, leaving results in default (oldest-first) order. Provided the corrected comparator and noted the identical bug existed in `approvals.html` as well.

**Follow-up confirmation:** "that change has been applied."
 
---

## 12. Feature request: status/department filters

**Prompt:**
> I want to add the ability to filter the my-requests.html page contents by status. On approvals.html I want to add another filter this one filterable by status and department

**Outcome:** Designed and delivered:
- `my-requests.html`: client-side status filter (All/Pending/Approved/Denied), reusing already-fetched data, with the sort fix folded in.
- `approvals.html`: status + department filters. Explained the backend's `findAllByStatusAndDepartment` can't express "all departments" or "all statuses," so those cases fall back to fetching everything and filtering client-side using an `author_id → department_id` map built from `/api/Users`. Accept/Deny buttons scoped to only show on `PENDING` cards.
- `styles.css`: new `.filter-bar` layout rule.
---

## 13. Merge conflict resolution (round 1)

**Prompt (with pasted "incoming" version):**
> Im having a merge conflict on approvals.html. I want to keep both implementations because they are doing seperate tasks. how should I go about resolving this

**Outcome:** Identified that the pasted "conflicting" version was just the earlier sort-typo fix (`reimbursement_id` → `reimbursements_id`), which was already fully subsumed by the filter-feature rewrite — nothing to actually merge, just take the filter-feature version wholesale. Provided the full resolved file and flagged that `styles.css`'s `.filter-bar` addition also needed to land separately.
 
---

## 14. Merge conflict resolution (round 2 — corrected)

**Prompt (with both true left/right versions):**
> I dont think I showed you the correct approvals.html. review the merge conflict again. The first pasted implementation is from the github (right). the second pasted implementation is from my machine (left)

**Outcome:** Identified this as a genuine two-feature merge:
- GitHub (right): Pending/History toggle + new "Promote to Manager" section (`PUT /api/Users/{id}/role`)
- Local (left): status + department filter dropdowns
  Flagged the one real overlap (the History toggle and the status dropdown both gate the same list) and proposed folding "History" into the status dropdown as a combined Approved+Denied option rather than running two competing controls, while keeping the Promote-to-Manager section untouched since it was fully independent. Delivered the merged file and flagged that the `/api/Users/{id}/role` backend route hadn't been confirmed to exist yet in the reviewed Java files.