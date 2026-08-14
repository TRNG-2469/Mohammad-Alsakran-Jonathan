# Prompt Log — ERS Iteration 1

---

**Prompt:** "Give me the pattern for a layered Javalin CRUD flow — Model, DAO, Service, Controller."
**Response:** Provided the general shape and an example method, told me to write the rest and bring it back.
**Result:** I wrote `UsersDAOImpl.create()`. Asked Claude to validate — it caught that I was manually inserting `user_id` into a `serial` column. I fixed it using `RETURNING user_id`.

---

**Prompt:** "Is scoping Department to just create/findById/findAll reasonable, or should it be full CRUD?"
**Response:** Confirmed the scoped-down approach matched the spec (no update/delete requirement listed).
**Result:** I wrote the full Model/DAO/Service/Controller stack myself; Claude reviewed each file as I finished it.

---

**Prompt:** "What type should I use for `amount` and `resolver_id` on the Reimbursement model?"
**Response:** Recommended `BigDecimal` for money and a nullable `Integer` for resolver_id, explained why.
**Result:** I built the model and DAO. Asked Claude to validate the immutability logic I wrote in `update`/`resolve` — confirmed it was correct.

---

**Prompt:** "Walk me through how Basic Auth and sessions work in Javalin."
**Response:** Explained `ctx.basicAuthCredentials()` and `ctx.sessionAttribute()`.
**Result:** I implemented login, logout, and the `app.before` manager-only middleware. Claude reviewed the final code and I tested all four session states myself (no session, wrong role, correct manager, post-logout).

---

**Prompt:** "How does BCrypt hashing work, walk me through it."
**Response:** Explained hash/checkpw and where each call belongs.
**Result:** I integrated it into registration and login, then verified the hash directly in the database myself.

---

**Prompt:** "What are reasonable business rules to add here — amount limits, description length, etc.?"
**Response:** Discussed options and reasoning for each.
**Result:** I implemented the amount ceiling, description length cap, and resolver-must-be-manager check. Validated each with Claude line by line.

---

**Prompt:** "Explain constructor injection and mocking, then help me get unit tests started given I'm short on time."
**Response:** Explained the pattern, then provided starter test classes for all three services given the time constraint.
**Result:** I placed the files, ran them, and confirmed all 33 tests passed myself.

---

**Prompt:** "Review the frontend my partner built and tell me what's actually broken."
**Response:** Flagged three DOM id mismatches and a type-dropdown mismatch with backend validation, explained the fixes.
**Result:** I applied the fixes and tested each one live in the browser before confirming with Claude.

---
