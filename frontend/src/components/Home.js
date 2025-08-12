import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { bookAPI, fileAPI } from '../services/api';
import BookModal from './books/BookModal';

const Home = () => {
  const { user } = useAuth();
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [selectedBook, setSelectedBook] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');

  const fetchBooks = async (page = 0, search = '') => {
    try {
      setLoading(true);
      const response = await bookAPI.getUserBooks({
        page,
        size: 12,
        search: search || undefined,
        sortBy: 'createdAt',
        sortDir: 'desc'
      });
      
      setBooks(response.data.content);
      setTotalPages(response.data.totalPages);
      setCurrentPage(page);
    } catch (error) {
      console.error('Error fetching books:', error);
      setError('Failed to load books');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBooks();
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    fetchBooks(0, searchTerm);
  };

  const handleBookClick = (book) => {
    setSelectedBook(book);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedBook(null);
  };

  const handleDeleteBook = async (bookId) => {
    try {
      await bookAPI.deleteBook(bookId);
      setBooks(books.filter(book => book.id !== bookId));
      handleCloseModal();
    } catch (error) {
      console.error('Error deleting book:', error);
      setError('Failed to delete book');
    }
  };

  const handlePageChange = (page) => {
    fetchBooks(page, searchTerm);
  };

  if (loading && books.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">My Library</h1>
          <p className="mt-2 text-gray-600">Welcome back, {user?.fullName}!</p>
        </div>
        <Link
          to="/books/add"
          className="mt-4 sm:mt-0 btn-primary inline-flex items-center"
        >
          <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
          Add Book
        </Link>
      </div>

      {/* Search */}
      <div className="card">
        <form onSubmit={handleSearch} className="flex gap-4">
          <div className="flex-1">
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Search books by title or author..."
              className="input-field"
            />
          </div>
          <button type="submit" className="btn-primary">
            Search
          </button>
        </form>
      </div>

      {/* Error Message */}
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
          {error}
        </div>
      )}

      {/* Books Grid */}
      {books.length === 0 ? (
        <div className="card text-center py-12">
          <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
          </svg>
          <h3 className="mt-2 text-sm font-medium text-gray-900">No books found</h3>
          <p className="mt-1 text-sm text-gray-500">
            {searchTerm ? 'Try adjusting your search terms.' : 'Get started by adding your first book.'}
          </p>
          {!searchTerm && (
            <div className="mt-6">
              <Link to="/books/add" className="btn-primary">
                Add Book
              </Link>
            </div>
          )}
        </div>
      ) : (
        <>
          <div className="book-grid">
            {books.map((book) => (
              <div
                key={book.id}
                className="book-card card cursor-pointer"
                onClick={() => handleBookClick(book)}
              >
                <div className="aspect-w-3 aspect-h-4 mb-4">
                  <img
                    src={book.imageUrl ? fileAPI.getFileUrl(book.imageUrl) : fileAPI.getDefaultBookCover()}
                    alt={book.title}
                    className="w-full h-48 object-cover rounded-lg"
                  />
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-1 line-clamp-2">
                    {book.title}
                  </h3>
                  {book.author && (
                    <p className="text-sm text-gray-600 mb-2">by {book.author}</p>
                  )}
                  {book.description && (
                    <p className="text-sm text-gray-500 line-clamp-3">
                      {book.description}
                    </p>
                  )}
                </div>
              </div>
            ))}
          </div>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex justify-center mt-8">
              <nav className="flex items-center space-x-2">
                <button
                  onClick={() => handlePageChange(currentPage - 1)}
                  disabled={currentPage === 0}
                  className="btn-secondary disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Previous
                </button>
                
                {Array.from({ length: totalPages }, (_, i) => (
                  <button
                    key={i}
                    onClick={() => handlePageChange(i)}
                    className={`px-3 py-2 rounded-md text-sm font-medium ${
                      currentPage === i
                        ? 'bg-primary-600 text-white'
                        : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                    }`}
                  >
                    {i + 1}
                  </button>
                ))}
                
                <button
                  onClick={() => handlePageChange(currentPage + 1)}
                  disabled={currentPage === totalPages - 1}
                  className="btn-secondary disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Next
                </button>
              </nav>
            </div>
          )}
        </>
      )}

      {/* Book Modal */}
      {showModal && selectedBook && (
        <BookModal
          book={selectedBook}
          onClose={handleCloseModal}
          onDelete={handleDeleteBook}
          isOwner={selectedBook.ownerId === user?.userId}
        />
      )}
    </div>
  );
};

export default Home;
