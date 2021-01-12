import React, {useContext} from 'react';
import {UserDispatch} from './App'

const User = React.memo(function User({user}){
  const {username, email, id, active} = user;
  const dispatch = useContext(UserDispatch);
  return (
    <div>
      <b
        style={{
          color: user.active ? 'green' : 'black',
          cursor: 'pointer'
        }}
        onClick={() => dispatch({
          type: 'TOGGLE_USER',
          id
        })}
      >
        {user.username}
      </b>
        &nbsp;
        <span>({user.email})</span>
      <button onClick={() => dispatch({
          type: 'REMOVE_USER',
          id
        })}>remove</button>
    </div>
  );
})

function UserList({users}){
  return (
    <div>
      {
        users.map(
          user => (
                    <User
                        user={user}
                        key={user.id}
                    />
                  )
        )
      }
    </div>
  );
}

export default React.memo(UserList, (prevProps, nextProps) => prevProps.users === nextProps.users);
