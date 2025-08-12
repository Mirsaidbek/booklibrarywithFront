import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { bookAPI } from '../../services/api';

const EditBook = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    description: ''
  });
  const [coverImage, setCoverImage] = useState(null);
  const [bookFile, setBookFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchBook = async () => {
      try {
        const response = await bookAPI.getBookById(id);
        const book = response.data;
        setFormData({
          title: book.title || '',
          author: book.author || '',
          description: book.description || ''
        });
      } catch (error) {
        console.error('Error fetching book:', error);
        setError('Failed to load book');
      } finally {
        setLoading(false);
      }
    };

    fetchBook();
  }, [id]);

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleFileChange = (e, type) => {
    const file = e.target.files[0];
    if (file) {
      if (type === 'cover') {
        setCoverImage(file);
      } else {
        setBookFile(file);
      }
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSaving(true);

    if (!formData.title.trim()) {
      setError('Title is required');
      setSaving(false);
      return;
    }

    try {
      const submitData = {
        ...formData,
        coverImage,
        bookFile
      };

      await bookAPI.updateBook(id, submitData);
      navigate('/');
    } catch (error) {
      console.error('Error updating book:', error);
      setError(error.response?.data?.message || 'Failed to update book');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Edit Book</h1>
        <p className="mt-2 text-gray-600">Update your book information</p>
      </div>

      <div className="card">
        <form onSubmit={handleSubmit} className="space-y-6">
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
              {error}
            </div>
          )}

          <div>
            <label htmlFor="title" className="block text-sm font-medium text-gray-700">
              Title *
            </label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              className="input-field mt-1"
              placeholder="Enter book title"
              required
            />
          </div>

          <div>
            <label htmlFor="author" className="block text-sm font-medium text-gray-700">
              Author
            </label>
            <input
              type="text"
              id="author"
              name="author"
              value={formData.author}
              onChange={handleInputChange}
              className="input-field mt-1"
              placeholder="Enter author name"
            />
          </div>

          <div>
            <label htmlFor="description" className="block text-sm font-medium text-gray-700">
              Description
            </label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleInputChange}
              rows={4}
              className="input-field mt-1"
              placeholder="Enter book description"
            />
          </div>

          <div>
            <label htmlFor="coverImage" className="block text-sm font-medium text-gray-700">
              Cover Image
            </label>
            <input
              type="file"
              id="coverImage"
              accept="image/*"
              onChange={(e) => handleFileChange(e, 'cover')}
              className="mt-1 block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-primary-50 file:text-primary-700 hover:file:bg-primary-100"
            />
            <p className="mt-1 text-sm text-gray-500">
              Upload a new cover image (JPG, PNG, GIF). Max size: 10MB
            </p>
          </div>

          <div>
            <label htmlFor="bookFile" className="block text-sm font-medium text-gray-700">
              Book File
            </label>
            <input
              type="file"
              id="bookFile"
              accept=".pdf,.txt,.epub,.mobi"
              onChange={(e) => handleFileChange(e, 'book')}
              className="mt-1 block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-primary-50 file:text-primary-700 hover:file:bg-primary-100"
            />
            <p className="mt-1 text-sm text-gray-500">
              Upload a new book file (PDF, TXT, EPUB, MOBI). Max size: 10MB
            </p>
          </div>

          <div className="flex gap-4">
            <button
              type="submit"
              disabled={saving}
              className="btn-primary flex-1"
            >
              {saving ? (
                <div className="flex items-center justify-center">
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                  Updating Book...
                </div>
              ) : (
                'Update Book'
              )}
            </button>
            <button
              type="button"
              onClick={() => navigate('/')}
              className="btn-secondary flex-1"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditBook;
