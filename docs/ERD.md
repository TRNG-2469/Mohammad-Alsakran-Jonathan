erDiagram
    DEPARTMENTS ||--o{ USERS : "has"
    USERS ||--o{ REIMBURSEMENTS : "submits (author)"
    USERS ||--o{ REIMBURSEMENTS : "resolves (resolver)"

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
        boolean role
        int department_id FK
    }

    REIMBURSEMENTS {
        int reimbursements_id PK
        string status
        decimal amount
        string description
        string type
        int resolver_id FK
        int author_id FK
    }