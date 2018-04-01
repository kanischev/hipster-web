import React, {Component} from 'react';
import { Form, Icon, Input, Button, Checkbox } from 'antd';
import { Link } from 'react-router-dom';
import publicRoutes from '../../routes/publicRoutes';
import './public.css';

const FormItem = Form.Item;

class AppRestorePasswordForm extends Component {
    handleSubmit = (e) => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
            }
        });
    };
    render() {
        const { getFieldDecorator } = this.props.form;
        return (
            <div className="public-form-container restore-password-form">
                <div className="logo">
                    <img alt="logo" src="images/hipster-logo.svg" />
                    <span className="app-logo-label">Hipster App</span>
                </div>
                <Form onSubmit={this.handleSubmit} >
                    <FormItem>
                        {getFieldDecorator('userEmail', {
                            rules: [{ required: true, message: 'Please input your user\'s e-mail!' }],
                        })(
                            <Input prefix={<Icon type="mail" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="User e-mail" />
                        )}
                    </FormItem>
                    <FormItem>
                        <Button type="primary" htmlType="submit" className="login-form-button">
                            Restore password
                        </Button>
                        <div className="login-bottom-nav">
                            <Link to={publicRoutes.login.path}>Log in</Link>  |  <Link to={publicRoutes.signUp.path}>Register</Link>
                        </div>
                    </FormItem>
                </Form>
            </div>
        );
    }
}

export const RestorePasswordForm = Form.create()(AppRestorePasswordForm);
