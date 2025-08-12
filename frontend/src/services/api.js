import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
};

// User API
export const userAPI = {
  getCurrentUser: () => api.get('/users/me'),
  updateProfile: (data) => api.put('/users/me', data),
  updatePassword: (data) => api.put('/users/me/password', data),
  uploadPhoto: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.put('/users/me/photo', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
};

// Book API
export const bookAPI = {
  getUserBooks: (params = {}) => api.get('/books', { params }),
  getBookById: (id) => api.get(`/books/${id}`),
  createBook: (data) => {
    const formData = new FormData();
    Object.keys(data).forEach(key => {
      if (data[key] !== null && data[key] !== undefined) {
        if (key === 'coverImage' || key === 'bookFile') {
          if (data[key]) {
            formData.append(key, data[key]);
          }
        } else {
          formData.append(key, data[key]);
        }
      }
    });
    return api.post('/books', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  updateBook: (id, data) => {
    const formData = new FormData();
    Object.keys(data).forEach(key => {
      if (data[key] !== null && data[key] !== undefined) {
        if (key === 'coverImage' || key === 'bookFile') {
          if (data[key]) {
            formData.append(key, data[key]);
          }
        } else {
          formData.append(key, data[key]);
        }
      }
    });
    return api.put(`/books/${id}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  deleteBook: (id) => api.delete(`/books/${id}`),
};

// Admin API
export const adminAPI = {
  getAllUsers: (params = {}) => api.get('/admin/users', { params }),
  getUserById: (id) => api.get(`/admin/users/${id}`),
  createUser: (data) => api.post('/admin/users', data),
  updateUserStatus: (id, status) => api.patch(`/admin/users/${id}/status`, null, { params: { status } }),
  getUserBooks: (id, params = {}) => api.get(`/admin/users/${id}/books`, { params }),
  getAllBooks: (params = {}) => api.get('/admin/books', { params }),
};

// File API
export const fileAPI = {
  getFileUrl: (path) => `${API_BASE_URL}/files/${path}`,
  getDefaultBookCover: () => `${API_BASE_URL}/files/default-book-cover.jpg`,
  getDefaultProfilePhoto: () => `${API_BASE_URL}/files/default-profile.jpg`,
};

export default api;
