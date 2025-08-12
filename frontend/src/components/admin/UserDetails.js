import React from 'react';

const UserDetails = () => {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">User Details</h1>
        <p className="mt-2 text-gray-600">View detailed information about a specific user</p>
      </div>

      <div className="card">
        <div className="text-center py-12">
          <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          <h3 className="mt-2 text-sm font-medium text-gray-900">User Details</h3>
          <p className="mt-1 text-sm text-gray-500">
            This feature is coming soon. You'll be able to view detailed user information and their books.
          </p>
        </div>
      </div>
    </div>
  );
};

export default UserDetails;
