import React from 'react';
import { shallow } from 'enzyme';
import Sidedrawer from './Sidedrawer';

describe('AppBar', () => {
  it('should match snapshot', () => {
    let eut = shallow(<Sidedrawer />);

    expect(eut).toMatchSnapshot();
  });
});
