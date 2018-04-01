import UserManagement from '../containers/private/UserManagement';
import UserProfile from '../containers/private/UserProfile';

export default {
    Login: {
        component: UserProfile,
        path: '/profile',
        exact: true
    },
    signUp: {
        component: UserManagement,
        path: '/userManagement',
        exact: true
    },
};