import React from 'react';
import { useParams } from 'react-router-dom';

const BookReader = () => {
  const { id } = useParams();

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Book Reader</h1>
        <p className="mt-2 text-gray-600">Read your book content</p>
      </div>

      <div className="card">
        <div className="text-center py-12">
          <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
          </svg>
          <h3 className="mt-2 text-sm font-medium text-gray-900">Book Reader</h3>
          <p className="mt-1 text-sm text-gray-500">
            This feature is coming soon. You'll be able to read book content with a full-featured reader.
          </p>
          <p className="mt-1 text-sm text-gray-400">
            Book ID: {id}
          </p>
        </div>
      </div>
    </div>
  );
};

export default BookReader;
