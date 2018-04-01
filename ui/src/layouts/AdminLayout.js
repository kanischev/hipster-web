import React, { Component } from 'react';

class AdminLayout extends Component {
    render() {
        const Component = this.props.component;
        return (
            <Component/>
        );
    }
}

export default AdminLayout;