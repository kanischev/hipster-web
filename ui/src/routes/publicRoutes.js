import { LoginForm } from '../containers/public/Login';
import { RegisterForm } from '../containers/public/Register';
import { RestorePasswordForm } from '../containers/public/RestorePassword';

export default {
    login: {
        component: LoginForm,
        path: '/login',
        exact: true
    },
    signUp: {
        component: RegisterForm,
        path: '/signUp',
        exact: true
    },
    passwordRestore: {
        component: RestorePasswordForm,
        path: '/restorePassword',
        exact: true
    }
};