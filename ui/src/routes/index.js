import React, { Component } from 'react';
import {BrowserRouter as Router, IndexRoute, Route, Switch} from "react-router-dom";
import publicRoutes from  './publicRoutes';
import PublicLayout from '../layouts/PublicLayout';
import _ from 'lodash';


export default class AppRouter extends Component {
    render() {
        return (
            <Router>
                <Switch>
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
                 </Switch>
            </Router>
        );
    }
}
