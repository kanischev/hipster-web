import React, { Component } from 'react';
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import App from '../containers/App';
import { SecuredRoute } from './SecuredRoute';
import publicRoutes from  './publicRoutes';
import privateRoutes from  './privateRoutes';
import PublicLayout from '../layouts/PublicLayout';
import _ from 'lodash';
import AdminLayout from "../layouts/AdminLayout";


export default class AppRouter extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <SecuredRoute exact path='/' render = {(route) => <AdminLayout component={App} route={route}/>}/>
                    { _.map(publicRoutes, (route, key) => {
                        const { component, path } = route;
                        return (
                            route.exact ?
                                <Route
                                    exact
                                    path={path}
                                    key={key}
                                    render={ (route) => <PublicLayout component={component} route={route}/>}
                                /> :
                                <Route
                                    path={path}
                                    key={key}
                                    render={ (route) => <PublicLayout component={component} route={route}/>}
                                />
                        )
                    }) }
                    { _.map(privateRoutes, (route, key) => {
                        const { component, path } = route;
                        return (
                            route.exact ?
                                <SecuredRoute
                                    exact
                                    path={path}
                                    key={key}
                                    render={ (route) => <AdminLayout component={component} route={route}/>}
                                /> :
                                <SecuredRoute
                                    path={path}
                                    key={key}
                                    render={ (route) => <AdminLayout component={component} route={route}/>}
                                />
                        )
                    }) }
                 </Switch>
            </Router>
        );
    }
}
