# seed_users.md

Users can't be seeded with plain SQL anymore, since passwords are hashed
in the service layer (`UsersServiceImpl.create`), not in the database.
Run these five requests through Postman (or curl) against a running app,
in order, before running the Reimbursements section of seed.md.

## Step 0: wipe and reset ids first

If the `users` table has any existing rows (old test data, previous
seed attempts, etc.), delete them and reset the id sequence before
registering. `DELETE FROM users;` alone does NOT reset the sequence,
Postgres keeps counting up from wherever it left off, so new
registrations will land on unpredictable ids (learned this the hard
way, first reseed attempt landed on ids 9-13 instead of 1-5).

Run both of these before registering anyone:

```sql
DELETE FROM users;
SELECT setval('users_user_id_seq', 1, false);
```

Confirm afterward:

```sql
SELECT user_id, username FROM users ORDER BY user_id;
```

Should return zero rows before you start registering.

## Step 1: register the five seed users

Each one is a POST to:

```
http://localhost:7700/api/Users
Content-Type: application/json
```

Bodies, run in this order:

```json
{"username": "JohnVT", "password": "password", "role": true, "first_name": "John", "last_name": "Vermont", "department_id": 1}
```

```json
{"username": "Sakran2", "password": "password", "role": false, "first_name": "Mohammad", "last_name": "Alsakran", "department_id": 1}
```

```json
{"username": "BobK", "password": "password", "role": false, "first_name": "Bob", "last_name": "Kim", "department_id": 2}
```

```json
{"username": "DanaR", "password": "password", "role": true, "first_name": "Dana", "last_name": "Reyes", "department_id": 2}
```

```json
{"username": "SamP", "password": "password", "role": false, "first_name": "Sam", "last_name": "Patel", "department_id": 3}
```

After running all five, confirm with:

```sql
SELECT user_id, username, password FROM users;
```

`password` should show a real bcrypt hash (starts with `$2a$` or `$2b$`),
not the literal word "password". If it doesn't, something's wrong with
the hashing in `create()`, not with this seed process.

Note: the Reimbursements insert in seed.md assumes ids landed exactly
as JohnVT=1, Sakran2=2, BobK=3, DanaR=4, SamP=5. This only holds if
Step 0 was actually done first. Skipping the sequence reset (or running
these out of order) means the author_id/resolver_id values in the
Reimbursements seed will silently point at the wrong people, no error,
just wrong data.
