import React from "react";
import ReactDOM from "react-dom";
import {Provider} from 'react-redux';
import AppRouter from "./routes/index";
import registerServiceWorker from "./registerServiceWorker";
import 'antd/dist/antd.min.css';
import { createStore } from 'redux';
import root from './reducers';

const store = createStore(
    root
);

ReactDOM.render(
    <Provider store={store}>
        <AppRouter/>
    </Provider>,
    document.getElementById("root")
);

registerServiceWorker();