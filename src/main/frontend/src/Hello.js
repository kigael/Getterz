import React from 'react';

function Hello({color,name, isSpecial}){
  return (
    <div style={{
      color: color
    }}>
      {isSpecial ? <b>*</b> : null}
      Hello world {name}
    </div>
  );
}

Hello.defaultProps={
  color: 'black',
  name: 'no-name'
};

export default Hello;
