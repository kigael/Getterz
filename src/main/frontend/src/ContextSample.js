import React, {createContext, useContext, useState} from 'react';

const MyContext = createContext('defaultValue');

function Child(){
  const text = useContext(MyContext);
  return <div>hello {text}</div>
}

function Parent({text}){
  return <Child />
}

function GrandParent(){
  return <Parent />
}

function ContextSample(){
  const [value, setValue] = useState(true);
  return (
      <MyContext.Provider value={value ? 'Good' : 'Bad'}>
        <GrandParent />
        <button onClick={() => setValue(!value)}>CLICK ME</button>
      </MyContext.Provider>
  );
}

export default ContextSample;
