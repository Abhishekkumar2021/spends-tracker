# Spends Tracker

Spends Tracker is a web application designed to help users track their spending habits. It provides features for managing categories, tracking expenses, and generating reports.

## Table of Contents

- [Spends Tracker](#spends-tracker)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
  - [Technologies Used](#technologies-used)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
  - [Backend](#backend)
    - [Key Files](#key-files)
  - [Frontend](#frontend)
    - [Key Files](#key-files-1)
  - [Middle Backend](#middle-backend)
    - [Key Files](#key-files-2)
  - [Contributing](#contributing)
  - [License](#license)

## Features

- User authentication and authorization
- Expense tracking and categorization
- Data visualization with charts
- Responsive design

## Technologies Used

- **Backend**: Spring Boot, WebFlux, JWT, MongoDB
- **Frontend**: Angular, Ngx-Charts
- **Middle Backend**: NestJS

## Getting Started

### Prerequisites

- Node.js
- npm or yarn
- Java 17+
- Maven
- MongoDB

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/spends-tracker.git
    cd spends-tracker
    ```

2. Install dependencies for the frontend:
    ```sh
    cd frontend
    npm install
    ```

3. Install dependencies for the middle backend:
    ```sh
    cd ../middle-backend
    npm install
    ```

4. Build and run the backend:
    ```sh
    cd ../backend
    ./mvnw spring-boot:run
    ```

5. Build and run the middle backend:
    ```sh
    cd ../middle-backend
    npm run start:dev
    ```

6. Build and run the frontend:
    ```sh
    cd ../frontend
    npm start
    ```

## Backend

The backend is built with Spring Boot and WebFlux. It handles user authentication, category management, and expense tracking.

### Key Files

- `BackendApplication.java`: Main entry point for the backend application.
- `SecurityConfig.java`: Security configuration for handling JWT authentication.
- `CategoryController.java`: REST controller for managing categories.
- `AuthController.java`: REST controller for user authentication.

## Frontend

The frontend is built with Angular and Ngx-Charts. It provides a user interface for tracking expenses and visualizing data.

### Key Files

- `home.component.ts`: Main component for displaying charts and data.
- `app.component.ts`: Root component of the Angular application.
- `angular.json`: Angular CLI configuration file.

## Middle Backend

The middle backend is built with NestJS. It acts as a middleware between the frontend and backend, handling additional business logic and API requests.

### Key Files

- `main.ts`: Main entry point for the NestJS application.
- `app.module.ts`: Root module of the NestJS application.
- `package.json`: Contains scripts and dependencies for the middle backend.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.