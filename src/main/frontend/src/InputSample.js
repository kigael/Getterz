import React, {useState, useRef} from 'react';

function InputSample(){
  const [inputs, setInputs] = useState({
    name:'',
    profileName:''
  });
  const nameInput = useRef();
  const { name, profileName } = inputs;
  const onChange = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: e.target.value
    });
  };
  const onReset = () => {
    setInputs({
      name: '',
      profileName: ''
    });
    nameInput.current.focus();
  };
  return(
    <div>
      <input
        name="name"
        placeholder="name"
        onChange={onChange}
        value={name}
        ref={nameInput}
      />
      <input
        name="profileName"
        placeholder="profileName"
        onChange={onChange}
        value={profileName}
      />
      <button onClick={onReset}>clear</button>
      {name} {profileName}
    </div>
  );
}
export default InputSample;
