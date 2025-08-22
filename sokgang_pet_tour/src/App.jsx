import React from 'react'
import './App.css'
import { RouterProvider, createBrowserRouter } from 'react-router-dom'
import { ToastContainer } from 'react-toastify';
import AuthPage from './page/AuthPage';
import MainPage from './page/MainPage';
import 'bootstrap/dist/css/bootstrap.min.css';
import LoginProvider from './store/LoginProvider';
import ProtectedRoute from './store/ProtectedRoute';
import AdminPage from './page/AdminPage';
import TourPage from './page/TourPage';
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import ChatPage from './page/ChatPage';
import AIPage from './page/AiPage';
import MyPage from './page/MyPage';
import NotFoundPage from './page/NotFoundPage';
const router = createBrowserRouter([

  {
    path : "/auth",
    element: (
        <AuthPage />
    ),
  },
  {
    path : "/",
    element: (
        <MainPage />
    ),
  },
  {
    path : "/admin",
    element: (
      <ProtectedRoute>
        <AdminPage />
      </ProtectedRoute>
    ),
  },
  {
    path : "/chat",
    element: (
      <ProtectedRoute>
        <ChatPage />
      </ProtectedRoute>
    ),
  },
  {
    path : "/ai",
    element: (
      <ProtectedRoute>
        <AIPage/>
      </ProtectedRoute>
    ),
  },
  {
    path : "/my",
    element: (
      <ProtectedRoute>
        <MyPage/>
      </ProtectedRoute>
    ),
  },
  {
    path : "/tour",
    element: (
      <ProtectedRoute>
        <TourPage/>
      </ProtectedRoute>
    ),
  },
  {
    path: "*", 
    element: <NotFoundPage />,
  },
]);

function App() {
  
  return (
      <>
        <LoginProvider>
          <ToastContainer
            toastClassName="custom-toast-container"
            bodyClassName="custom-toast-body"
            />
          <RouterProvider router={router}/>
        </LoginProvider>
      </>
  )
}

export default App
