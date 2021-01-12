import React, {
  useRef,
  useReducer,
  useMemo,
  useCallback,
  createContext,
} from "react";
import produce from "immer";
import UserList from "./UserList";
import CreateUser from "./CreateUser";
import useInputs from "./useInputs";

const initialState = {
  users: [
    {
      id: 1,
      username: "cgs",
      email: "cgs@gamil.com",
      active: true,
    },
  ],
};

function countActiveUsers(a) {
  return a.filter((e) => e.active).length;
}

function reducer(state, action) {
  switch (action.type) {
    case "CREATE_USER":
      return produce(state, (draft) => {
        draft.users.push(action.user);
      });
    case "TOGGLE_USER":
      return produce(state, (draft) => {
        const user = draft.users.find((user) => user.id === action.id);
        user.active = !user.active;
      });
    case "REMOVE_USER":
      return produce(state, (draft) => {
        const index = draft.users.findIndex((user) => user.id === action.id);
        draft.users.splice(index, 1);
      });
    default:
      throw new Error("Unhandled action");
  }
}

export const UserDispatch = createContext(null);

function App() {
  const [state, dispatch] = useReducer(reducer, initialState);
  const [form, onChange, reset] = useInputs({
    username: "",
    email: "",
  });
  const { username, email } = form;
  const nextId = useRef(2);
  const { users } = state;

  const onCreate = useCallback(
    (e) => {
      dispatch({
        type: "CREATE_USER",
        user: {
          id: nextId.current,
          username,
          email,
        },
      });
      nextId.current += 1;
      reset();
    },
    [username, email, reset]
  );

  const count = useMemo(() => countActiveUsers(users), [users]);

  return (
    <UserDispatch.Provider value={dispatch}>
      <CreateUser
        username={username}
        email={email}
        onChange={onChange}
        onCreate={onCreate}
      />
      <UserList users={users} />
      <div>active users: {count}</div>
    </UserDispatch.Provider>
  );
}
export default App;
