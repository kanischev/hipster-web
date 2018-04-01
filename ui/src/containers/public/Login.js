import React, {Component} from 'react';
import { Form, Icon, Input, Button, Checkbox } from 'antd';
import { Link } from 'react-router-dom';
import publicRoutes from '../../routes/publicRoutes';
import './public.css';

const FormItem = Form.Item;

class AppLoginForm extends Component {
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
            <div className="public-form-container login-form">
                <div className="logo">
                    <img alt="logo" src="images/hipster-logo.svg" />
                    <span className="app-logo-label">Hipster App</span>
                </div>
                <Form onSubmit={this.handleSubmit} >
                    <FormItem>
                        {getFieldDecorator('userName', {
                            rules: [{ required: true, message: 'Please input your username!' }],
                        })(
                            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Username" />
                        )}
                    </FormItem>
                    <FormItem>
                        {getFieldDecorator('password', {
                            rules: [{ required: true, message: 'Please input your Password!' }],
                        })(
                            <Input prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password" placeholder="Password" />
                        )}
                    </FormItem>
                    <FormItem>
                        {getFieldDecorator('remember', {
                            valuePropName: 'checked',
                            initialValue: true,
                        })(
                            <Checkbox>Remember me</Checkbox>
                        )}
                        <Button type="primary" htmlType="submit" className="login-form-button">
                            Log in
                        </Button>
                        <div className="login-bottom-nav">
                            <Link to={publicRoutes.passwordRestore.path}>Restore password</Link>  |  <Link to={publicRoutes.signUp.path}>Register</Link>
                        </div>
                    </FormItem>
                </Form>
            </div>
        );
    }
}

export const LoginForm = Form.create()(AppLoginForm);
