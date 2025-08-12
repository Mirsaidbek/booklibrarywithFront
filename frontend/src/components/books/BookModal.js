import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { fileAPI } from '../../services/api';

const BookModal = ({ book, onClose, onDelete, isOwner }) => {
  // Handle ESC key press to close modal
  useEffect(() => {
    const handleEscKey = (event) => {
      if (event.key === 'Escape') {
        onClose();
      }
    };

    document.addEventListener('keydown', handleEscKey);
    return () => {
      document.removeEventListener('keydown', handleEscKey);
    };
  }, [onClose]);

  // Handle click outside modal to close
  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  const handleDelete = () => {
    if (window.confirm('Are you sure you want to delete this book?')) {
      onDelete(book.id);
    }
  };

  return (
    <div className="modal-overlay" onClick={handleOverlayClick}>
      <div className="modal-content animate-scale-in">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-xl font-semibold text-gray-900">Book Details</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 focus:outline-none"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* Content */}
        <div className="p-6">
          <div className="flex flex-col md:flex-row gap-6">
            {/* Book Cover */}
            <div className="flex-shrink-0">
              <img
                src={book.imageUrl ? fileAPI.getFileUrl(book.imageUrl) : fileAPI.getDefaultBookCover()}
                alt={book.title}
                className="w-32 h-48 object-cover rounded-lg shadow-md"
              />
            </div>

            {/* Book Information */}
            <div className="flex-1 space-y-4">
              <div>
                <h3 className="text-2xl font-bold text-gray-900 mb-2">{book.title}</h3>
                {book.author && (
                  <p className="text-lg text-gray-600 mb-3">by {book.author}</p>
                )}
              </div>

              {book.description && (
                <div>
                  <h4 className="text-sm font-medium text-gray-700 mb-2">Description</h4>
                  <p className="text-gray-600 text-sm leading-relaxed">{book.description}</p>
                </div>
              )}

              <div className="text-sm text-gray-500">
                <p>Added on {new Date(book.createdAt).toLocaleDateString()}</p>
                {book.updatedAt && book.updatedAt !== book.createdAt && (
                  <p>Updated on {new Date(book.updatedAt).toLocaleDateString()}</p>
                )}
              </div>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex flex-col sm:flex-row gap-3 mt-6 pt-6 border-t border-gray-200">
            {book.contentUrl && (
              <Link
                to={`/books/${book.id}/read`}
                className="btn-primary flex-1 flex items-center justify-center"
                onClick={onClose}
              >
                <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                </svg>
                Read Book
              </Link>
            )}

            {isOwner && (
              <>
                <Link
                  to={`/books/${book.id}/edit`}
                  className="btn-secondary flex-1 flex items-center justify-center"
                  onClick={onClose}
                >
                  <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                  Edit Book
                </Link>

                <button
                  onClick={handleDelete}
                  className="btn-danger flex-1 flex items-center justify-center"
                >
                  <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                  Delete Book
                </button>
              </>
            )}
          </div>

          {/* Close Button */}
          <div className="mt-4 text-center">
            <button
              onClick={onClose}
              className="text-gray-500 hover:text-gray-700 text-sm font-medium"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookModal;
