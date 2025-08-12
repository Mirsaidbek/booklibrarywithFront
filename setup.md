# 🚀 Online Library Setup Guide

This guide will help you set up and run the Online Library application.

## Prerequisites

Before you begin, make sure you have the following installed:

- **Java 21** - [Download here](https://adoptium.net/)
- **Node.js 18+** - [Download here](https://nodejs.org/)
- **PostgreSQL** - [Download here](https://www.postgresql.org/download/)
- **Maven** - [Download here](https://maven.apache.org/download.cgi)

## Database Setup

1. **Create PostgreSQL Database**
   ```sql
   CREATE DATABASE bookstorage;
   CREATE USER postgres WITH PASSWORD 'password';
   GRANT ALL PRIVILEGES ON DATABASE bookstorage TO postgres;
   ```

2. **Update Database Configuration**
   - Open `backend/src/main/resources/application.properties`
   - Update the database URL, username, and password if needed:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/bookstorage
   spring.datasource.username=postgres
   spring.datasource.password=password
   ```

## Backend Setup

1. **Navigate to backend directory**
   ```bash
   cd backend
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Verify backend is running**
   - Open http://localhost:8080/api/auth/register
   - You should see a JSON response (even if it's an error, it means the server is running)

5. **Access Swagger UI**
   - Open http://localhost:8080/swagger-ui.html
   - This provides interactive API documentation

## Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

4. **Verify frontend is running**
   - Open http://localhost:3000
   - You should see the login page

## Default Admin Account

The application automatically creates a default admin account on first run:

- **Email**: admin@library.com
- **Password**: admin123

## First Steps

1. **Register a new user account**
   - Go to http://localhost:3000/register
   - Create a new account with your email

2. **Login and start using the application**
   - Add books to your library
   - Upload cover images and book files
   - Use the modal feature by clicking on books

3. **Admin Features** (if you're an admin)
   - Access admin dashboard at /admin
   - Manage users and view system statistics

## File Uploads

The application stores uploaded files in the `uploads/` directory:
- `uploads/covers/` - Book cover images
- `uploads/books/` - Book content files
- `uploads/profiles/` - User profile photos
- `uploads/defaults/` - Default images

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Make sure PostgreSQL is running
   - Verify database credentials in `application.properties`
   - Check if the database exists

2. **Port Already in Use**
   - Backend: Change port in `application.properties` (server.port)
   - Frontend: Change port in `package.json` (scripts.start)

3. **CORS Issues**
   - Backend CORS is configured for http://localhost:3000
   - Update `cors.allowed-origins` in `application.properties` if needed

4. **File Upload Issues**
   - Check if the `uploads/` directory exists
   - Verify file permissions
   - Check file size limits (10MB default)

### Logs

- **Backend logs**: Check console output when running `mvn spring-boot:run`
- **Frontend logs**: Check browser console (F12) for any JavaScript errors

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Books
- `GET /api/books` - Get user's books
- `POST /api/books` - Add new book
- `GET /api/books/{id}` - Get book details
- `PUT /api/books/{id}` - Update book
- `DELETE /api/books/{id}` - Delete book

### Users
- `GET /api/users/me` - Get current user profile
- `PUT /api/users/me` - Update profile
- `PUT /api/users/me/password` - Change password
- `PUT /api/users/me/photo` - Upload profile photo

### Admin (Admin only)
- `GET /api/admin/users` - Get all users
- `GET /api/admin/users/{id}` - Get user details
- `POST /api/admin/users` - Create new user
- `PATCH /api/admin/users/{id}/status` - Ban/unban user

## Development

### Backend Development
- The application uses Spring Boot 3.x with Java 21
- JPA/Hibernate for database operations
- Spring Security with JWT for authentication
- File uploads are handled with MultipartFile

### Frontend Development
- React 18 with functional components and hooks
- TailwindCSS for styling
- React Router for navigation
- Axios for API calls

### Modal Feature
The modal feature is implemented in `BookModal.js` and includes:
- Click outside to close
- ESC key to close
- Book details display
- Read/Edit/Delete actions
- Responsive design

## Production Deployment

For production deployment, consider:
- Using a production database (AWS RDS, etc.)
- Setting up proper file storage (AWS S3, etc.)
- Configuring HTTPS
- Setting up proper logging
- Using environment variables for sensitive data
- Setting up CI/CD pipelines

## Support

If you encounter any issues:
1. Check the troubleshooting section above
2. Review the logs for error messages
3. Verify all prerequisites are installed correctly
4. Ensure database and file permissions are set correctly
