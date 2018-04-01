import { combineReducers } from 'redux'

import ContactReducer from './ContactReducer';

const reducers = {
    contactStore: ContactReducer
};

const rootReducer = combineReducers(reducers);

export default rootReducer;