import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

import "./style/index.css";
import {AuthProvider} from "./context/AuthProvider";
import RequireAuth from "./components/RequireAuth";
import Layout from "./pages/Layout";
import ErrorPage from "./pages/ErrorPage";
import Home from "./pages/Home";
import QuestionsList from "./pages/QuestionsList";

import UserHome from "./pages/user/UserHome";
import AdminHome from "./pages/admin/AdminHome";
import NotFound from "./pages/NotFound";

const router = createBrowserRouter([
  /* public */
  {
    path: "/",
    element: <Layout />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "/",
        element: <Home />
      },
      {
        path: "/questions",
        element: <QuestionsList />
      }
    ]
  },
  /* restricted to users */
  {
    path: "/user",
    element: <RequireAuth allowedRoles={["User"]} />,
    errorElement: <ErrorPage />,
    children: [
      {
        element: <Layout />,
        children: [
          {
            path: "",
            element: <UserHome />
          }
        ]
      }
    ]
  },
  /* restricted to users */
  {
    path: "/admin",
    element: <RequireAuth allowedRoles={["User"]} />,
    errorElement: <ErrorPage />,
    children: [
      {
        element: <Layout />,
        children: [
          {
            path: "",
            element: <AdminHome />
          }
        ]
      }
    ]
  },
  {
    path: "/*",
    element: <NotFound />
  }
]);

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <AuthProvider>
    <RouterProvider router={router} />
  </AuthProvider>
);
