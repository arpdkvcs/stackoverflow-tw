import React from "react";
import ReactDOM from "react-dom/client";
import {createBrowserRouter, RouterProvider} from "react-router-dom";

import "./style/index.css";
import {AuthProvider} from "./context/AuthProvider";
import RequireAuth from "./components/RequireAuth";
import Layout from "./pages/Layout";
import ErrorPage from "./pages/ErrorPage";
import QuestionsList from "./pages/QuestionsList";
import AdminPage from "./pages/admin/AdminPage";
import NotFound from "./pages/NotFound";
import UserSignIn from "./pages/UserSignIn";
import UserSignUp from "./pages/UserSignUp";
import QuestionDetail from "./pages/QuestionDetail";
import UserLayout from "./pages/user/UserLayout";
import AddAnswer from "./pages/AddAnswer";
import AddQuestion from "./pages/AddQuestion";
import EditAnswer from "./pages/EditAnswer";
import EditQuestion from "./pages/EditQuestion";

const router = createBrowserRouter([
  /* public */
  {
    path: "/",
    element: <Layout/>,
    errorElement: <ErrorPage/>,
    children: [
      {
        path: "/",
        element: <QuestionsList/>
      },
      {
        path: "/questions/:id",
        element: <QuestionDetail />
      },
      {
        path:"/login",
        element:<UserSignIn/>
      },
      {
        path: "/register",
        element: <UserSignUp/>
      }
    ]
  },
  /* restricted to users */
  {
    path: "/user",
    element: <RequireAuth allowedRoles={["USER"]}/>,
    errorElement: <ErrorPage/>,
    children: [
      {
        element: <UserLayout/>,
        children: [
          {
            path: "",
            element: <QuestionsList/>
          },
          {
            path: "/user/myquestions",
            element: <QuestionsList/>
          },
          {
            path: "/user/questions/add",
            element: <AddQuestion/>
          },
          {
            path: "/user/questions/edit/:id",
            element: <EditQuestion/>
          },
          {
            path: "/user/questions/addanswer/:id",
            element: <AddAnswer/>
          },
          {
            path: "/user/questions/editanswer/:id",
            element: <EditAnswer/>
          },
          {
            path: "/user/questions/:id",
            element: <QuestionDetail/>
          }
        ]
      }
    ]
  },
  /* restricted to admins */
  {
    path: "/admin",
    element: <RequireAuth allowedRoles={["User"]}/>,
    errorElement: <ErrorPage/>,
    children: [
      {
        element: <Layout/>,
        children: [
          {
            path: "",
            element: <AdminPage/>
          }
        ]
      }
    ]
  },
  {
    path: "/*",
    element: <NotFound/>
  }
]);

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <AuthProvider>
    <RouterProvider router={router}/>
  </AuthProvider>
);
