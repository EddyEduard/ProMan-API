# ProMan
ProMan is a project management application. It allows users to manage their projects, sprints and tasks.Tasks can be scheduled and prioritized according to the level of difficulty or time. All the roots from the REST API are described below.

## Technologies

Below is a list of all the technologies used to develop the app. They are structured by categories.
     
**Frontend**
   - [https://github.com/EddyEduard/ProMan](https://github.com/EddyEduard/ProMan.git)
     
**Backend**
   - Java
   - Spring
     
**Database**
   - MySQL 

## REST API routes

Below is a list of all REST API routes.

##### Auth

1. Register for company 

*Method: POST*

> /api/auth/register

```json
{
	"username": "Your username...",
	"password: "Your password...",
	"roles: ["ADMIN", "DIRECTOR", "MANAGER"],
	"company": {
		"name": "Compnay name...",
        "email": "Company e-mail...",
        "phone": "Company phone",
        "industry": "Company industry...",
        "country": "Company country...",
        "city": "Company city...",
        "address": "Company address..."
	}
}
```

2. Register for user 

*Method: PATCH*
<br/>
*Authorization role: ADMIN*

> /api/auth/register

```json
{
    "username": "Your uesrname...",
    "email": "Your e-mail...",
    "password": "Your password...",
    "phone": "Your phone...",
    "roles": ["SPECIALIST", "ACCOUNTANT"]
}
```

3. Authentication 

*Method: POST*

> /api/auth/login

```json
{
    "email": "Your e-mail...",
    "password": "Your password..."
}
```

4. Profile company 

*Method: GET*
<br/>
*Authorization role: ADMIN*

> /api/auth/company_profile

5. Profile user 

*Method: GET*
<br/>
*Authorization role: ADMIN*

> /api/auth/account_profile

6. Edit profile company 

*Method: PUT*
<br/>
*Authorization role: ADMIN*

> /api/auth/company_profile

```json
{
    "name": "Compnay name...",
    "email": "Company e-mail...",
    "phone": "Company phone",
    "industry": "Company industry...",
    "country": "Company country...",
    "city": "Company city...",
    "address": "Company address..."
}
```

7. Edit profile user 

*Method: PUT*
<br/>
*Authorization role: ADMIN*

> /api/auth/account_profile

```json
{
    "username": "Your uesrname...",
    "email": "Your e-mail...",
    "password": "Your password...",
    "phone": "Your phone...",
    "roles": ["SPECIALIST", "ACCOUNTANT"]
}
```

8. Remove account 

*Method: DELETE*
<br/>
*Authorization role: ADMIN*

> /api/auth/accounts

##### Project

1. Get projects

*Method: GET*

> /api/project/get

2. Get project

*Method: GET*

> /api/project/get/{id}

3. Create project

*Method: POST*
<br/>
*Authorization role: ADMIN, DIRECTOR, MANAGER*

> /api/project/create

```json
{
    "name": "Project name...",
    "description": "Project description..."
}
```

4. Update project

*Method: PUT*
<br/>
*Authorization role: ADMIN, DIRECTOR, MANAGER*

> /api/project/update/{id}

```json
{
    "name": "Project name...",
    "description": "Project description..."
}
```

5. Delete project

*Method: DELETE*
<br/>
*Authorization role: ADMIN, DIRECTOR, MANAGER*

> /api/project/delete/{id}

##### Sprint

1. Get sprints

*Method: GET*

> /api/sprint/get

2. Get sprint

*Method: GET*

> /api/sprint/get/{id}

3. Create sprint

*Method: POST*
<br/>
*Authorization role: ADMIN, DIRECTOR, MANAGER*

> /api/sprint/create

```json
{
	"projectId": 0
    "name": "Sprint name...",
    "description": "Sprint description...",
	"priority": "LOW | MIDDLE | HIGH"
}
```

4. Update sprint

*Method: PUT*
<br/>
*Authorization role: ADMIN, DIRECTOR, MANAGER*

> /api/sprint/update/{id}

```json
{
	"projectId": 0
    "name": "Sprint name...",
    "description": "Sprint description...",
	"priority": "LOW | MIDDLE | HIGH"
}
```

5. Delete sprint

*Method: DELETE*
<br/>
*Authorization role: ADMIN, DIRECTOR, MANAGER*

> /api/sprint/delete/{id}

##### Task

1. Get tasks

*Method: GET*

> /api/task/get

2. Get task

*Method: GET*

> /api/task/get/{id}

3. Create task

*Method: POST*
<br/>
*Authorization role: ADMIN, DIRECTOR, MANAGER*

> /api/task/create

```json
{
	"projectId": 0,
	"sprintId": 0,
    "name": "Task name...",
    "description": "Task description...",
	"priority": "LOW | MIDDLE | HIGH",
	"status: "CREATED | OPENED | CLOSED"
}
```

4. Update task

*Method: PUT*
<br/>
*Authorization role: ADMIN, DIRECTOR, MANAGER, SPECIALIST*

> /api/task/update/{id}

```json
{
	"projectId": 0,
	"sprintId": 0,
    "name": "Task name...",
    "description": "Task description...",
	"priority": "LOW | MIDDLE | HIGH",
	"status: "CREATED | OPENED | CLOSED"
}
```

5. Delete task

*Method: DELETE*
<br/>
*Authorization role: ADMIN, DIRECTOR, MANAGER*

> /api/task/delete/{id}

## System Diagram

### Entity Relationship Diagram

```mermaid
erDiagram
    COMPANY {
        long id PK
        srring name
        string email
        string(10) phone
        string industry
	string country
	string city
	string address
    }
    ACCOUNT {
        long id PK
        long company_id FK
        string username
        string email
        string(10) phone
        string(6) password
    }
    ACCOUNT_ROLE {
        long id PK
        long account_id FK
	long role_id FK
    }
    ROLE {
        long id PK
        string name
	string description
    }
    PROJECT {
        long id PK
        long company_id FK
        string name
	string description
	date created_date
	date updated_date
    }
    SPRINT {
        long id PK
        long company_id FK
        long project_id FK
        string name
	string description
	enum priority
	date created_date
	date updated_date
    }
    TASK {
        long id PK
        long company_id FK
        long project_id FK
        long sprint_id FK
        string name
	string description
	enum priority
	enum status
	date created_date
	date updated_date
    }

    COMPANY ||--o{ ACCOUNT : "has accounts"
    ACCOUNT ||--|{ ACCOUNT_ROLE : "has roles"
    ACCOUNT_ROLE }|--|| ROLE : "has accounts"
    COMPANY ||--o{ PROJECT : "has projects"
    COMPANY ||--o{ SPRINT : "has sprints"
    COMPANY ||--o{ TASK : "has tasks"
    PROJECT ||--o{ SPRINT : "has sprints"
    PROJECT ||--o{ TASK : "has tasks"
    SPRINT ||--o{ TASK : "has tasks"
```

### Use case diagrams

```mermaid
flowchart LR
    admin((ADMIN))
    director((DIRECTOR))
    manager((MANAGER))
    specialist((SPECIALIST))

    caseOne([User login])
    caseTwo([User registration])
    caseThree([Managing company and user accounts])
    caseFour([Managing projects])
    caseFive([Managing sprints])
    caseSix([Managing tasks])
    caseSeven([Update task])

    admin --> caseOne
    admin --> caseTwo
    admin --> caseThree
    admin --> caseFour
    admin --> caseFive
    admin --> caseSix

    director --> caseOne
    director --> caseFour
    director --> caseFive
    director --> caseSix

    manager --> caseOne
    manager --> caseFour
    manager --> caseFive
    manager --> caseSix

    specialist --> caseOne
    specialist --> caseSeven

    style admin fill:#ff0000,color:white
    style director fill:#3498db,color:white
    style manager fill:#f39c12,color:white 
    style specialist fill:#1abc9c,color:white
```

## License
Distributed under the MIT License. See [MIT](https://github.com/EddyEduard/ProMan-API/blob/master/LICENSE) for more information.

## Contact
Eduard-Nicolae - [eduard_nicolae@yahoo.com](mailTo:eduard_nicolae@yahoo.com)
\
Project link - [https://github.com/EddyEduard/ProMan-API](https://github.com/EddyEduard/ProMan-API.git)
