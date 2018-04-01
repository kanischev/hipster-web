import React, { Component } from 'react';
import {Route, Redirect} from "react-router-dom";
import publicRoutes from "./publicRoutes";

export const SecuredRoute = ({ component: Component, ...rest }) => (
    <Route {...rest} render={(props) => (
        false === true
            ? <Component {...props} />
            : <Redirect to={publicRoutes.login.path} />
    )} />
);