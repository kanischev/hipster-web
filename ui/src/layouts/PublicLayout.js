import React, { Component } from 'react';

class PublicLayout extends Component {
    render() {
        const Component = this.props.component;
        return (
            <Component/>
        );
    }
}

export default PublicLayout;