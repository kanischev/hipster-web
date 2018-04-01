import React, {Component} from 'react';
import { Form, Icon, Input, Button, Checkbox } from 'antd';
import { Link } from 'react-router-dom';
import publicRoutes from '../../routes/publicRoutes';
import './public.css';

const FormItem = Form.Item;

class AppRegisterForm extends Component {
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

        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 8 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
        };

        return (
            <div className="public-form-container register-form">
                <div className="logo">
                    <img alt="logo" src="images/hipster-logo.svg" />
                    <span className="app-logo-label">Hipster App</span>
                </div>
                <Form onSubmit={this.handleSubmit} >
                    <FormItem {...formItemLayout} label="E-mail">
                        {getFieldDecorator('email', {
                            rules: [{ required: true, message: 'Please input your user\'s e-mail!' }],
                        })(
                            <Input />
                        )}
                    </FormItem>
                    <FormItem {...formItemLayout} label="First name">
                        {getFieldDecorator('firstName', {
                            rules: [{ required: true, message: 'Please input your first name!' }],
                        })(
                            <Input />
                        )}
                    </FormItem>
                    <FormItem {...formItemLayout} label="Last Name">
                        {getFieldDecorator('lastName', {
                            rules: [{ required: true, message: 'Please input your last name!' }],
                        })(
                            <Input />
                        )}
                    </FormItem>
                    <FormItem {...formItemLayout} label="Password">
                        {getFieldDecorator('password', {
                            rules: [{ required: true, message: 'Please input your password!' }],
                        })(
                            <Input type="password" />
                        )}
                    </FormItem>
                    <FormItem  {...formItemLayout} label="Repeat password">
                        {getFieldDecorator('passwordRepeat', {
                            rules: [{ required: true, message: 'Please repeat your password!' }],
                        })(
                            <Input type="password" />
                        )}
                    </FormItem>
                    <FormItem>
                        <Button type="primary" htmlType="submit" className="login-form-button">
                            Register
                        </Button>
                        <div className="login-bottom-nav">
                            <Link to={publicRoutes.login.path}>Log in</Link>  |  <Link to={publicRoutes.passwordRestore.path}>Restore password</Link>
                        </div>
                    </FormItem>
                </Form>
            </div>
        );
    }
}

export const RegisterForm = Form.create()(AppRegisterForm);
