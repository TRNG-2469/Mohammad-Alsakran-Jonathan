erDiagram
DEPARTMENTS --o{ USERS : "has"
USERS --o{ REIMBURSEMENTS : "requests"

    DEPARTMENTS {
        int department_id PK
        string name
    }

    USERS {
        int user_id PK
        string username
        string password
        string first_name
        string last_name
        boolean isManager
        int department_id FK
    }

    REIMBURSEMENTS {
        int reimbursement_id PK
        int user_id FK
        decimal amount
        string description
        string type
        string status
        int resolver_id FK
    }
