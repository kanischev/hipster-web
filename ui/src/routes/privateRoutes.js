import UserManagement from '../containers/private/UserManagement';
import UserProfile from '../containers/private/UserProfile';
import { menuGroups } from '../utils/Const'

export default {
    profile: {
        component: UserProfile,
        path: '/profile',
        group: menuGroups.sideMenuGroup,
        exact: true
    },
    manageUsers: {
        component: UserManagement,
        path: '/userManagement',
        group: menuGroups.sideMenuGroup,
        roles: ['Admin'],
        exact: true
    },
};