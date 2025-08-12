# 📚 Online Library

A full-stack web application that allows users to register, manage their personal library of books, and read them online with modal-based book interactions.

## 🚀 Features

- **User Authentication**: JWT-based authentication with Spring Security
- **Book Management**: Add, edit, delete, and read books with modal interactions
- **Profile Management**: Edit profile information and upload profile photos
- **Admin Dashboard**: User management with ban/unban functionality
- **Responsive Design**: Works on desktop, tablet, and mobile
- **Modal Book Interactions**: Click books to view details in modal overlay

## 🛠 Tech Stack

### Backend
- Java 21
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- JWT Authentication
- PostgreSQL
- Swagger UI

### Frontend
- React.js 18
- TailwindCSS
- React Router
- Axios

## 📁 Project Structure

```
BookStorage/
├── backend/                 # Spring Boot application
│   ├── src/
│   ├── pom.xml
│   └── application.properties
├── frontend/               # React application
│   ├── src/
│   ├── package.json
│   └── tailwind.config.js
├── uploads/               # File storage (covers + books)
└── README.md
```

## 🚀 Quick Start

### Prerequisites
- Java 21
- Node.js 18+
- PostgreSQL
- Maven

### Backend Setup
1. Navigate to backend directory: `cd backend`
2. Update `application.properties` with your database credentials
3. Run: `mvn spring-boot:run`
4. Backend will start on `http://localhost:8080`
5. Swagger UI: `http://localhost:8080/api/swagger-ui.html`

### Frontend Setup
1. Navigate to frontend directory: `cd frontend`
2. Install dependencies: `npm install`
3. Run: `npm start`
4. Frontend will start on `http://localhost:3000`

## 🔐 User Roles

### USER
- Add, edit, delete own books
- View and search own books
- Open books in reading mode
- Edit profile information
- Modal book interactions

### ADMIN
- Manage users (view, ban/unban, add new)
- View all users and their books
- Cannot add/edit/delete books

## 📖 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Profile
- `GET /api/users/me` - Get current user profile
- `PUT /api/users/me` - Update profile
- `PUT /api/users/me/password` - Change password
- `PUT /api/users/me/photo` - Upload profile photo

### Books
- `GET /api/books` - Get user's books (with search & pagination)
- `POST /api/books` - Add new book
- `GET /api/books/{id}` - Get book details
- `PUT /api/books/{id}` - Update book
- `DELETE /api/books/{id}` - Delete book

### Admin
- `GET /api/admin/users` - Get all users
- `GET /api/admin/users/{id}` - Get user details
- `POST /api/admin/users` - Add new user
- `PATCH /api/admin/users/{id}/status` - Ban/unban user
- `GET /api/admin/users/{id}/books` - Get user's books

## 🎨 Modal Feature

When users click on a book in the list/grid:
- Modal appears centered with dimmed background
- Shows book cover, title, author, and description
- Provides "Read" and "Edit" (if owner) buttons
- Can be closed by clicking X, outside modal, or pressing Esc

## 📱 Responsive Design

The application is fully responsive and optimized for:
- Desktop (1200px+)
- Tablet (768px - 1199px)
- Mobile (320px - 767px)

## 🔒 Security

- JWT-based authentication
- Password hashing with BCrypt
- Role-based access control
- Secure file upload handling
- CORS configuration

## 📊 Database Schema

### Users Table
- id (Primary Key)
- fullName
- username (Gmail, unique)
- password (hashed)
- profilePhoto
- role (USER/ADMIN)
- status (ACTIVE/BANNED)

### Books Table
- id (Primary Key)
- title
- author
- description
- imageUrl
- contentUrl
- owner (Foreign Key to Users)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.
