import produce from "immer";

const ADMIN_LOGIN = "ADMIN_LOGIN";
const ADMIN_LOGOUT = "ADMIN_LOGOUT";
const ADMIN_LOADING = "ADMIN_LOADING";
const ADMIN_DONE = "ADMIN_DONE";

export const adminLogin = (header) => ({ type: ADMIN_LOGIN, header });
export const adminLogout = () => ({ type: ADMIN_LOGOUT });
export const adminLoading = () => ({ type: ADMIN_LOADING });
export const adminDone = () => ({ type: ADMIN_DONE });

const initialState = {
  header: {
    transactionTime: null,
    resultCode: null,
    transactionType: null,
    description: null,
    data: {
      id: null,
      password: null,
      emailAddress: null,
      cellNumber: null,
    },
    session: null,
  },
  loading: false,
};

export default function AdminInfo(state = initialState, action) {
  console.log(action);
  switch (action.type) {
    case ADMIN_LOGIN:
      return produce(state, (draft) => {
        draft.header.transactionTime = action.header.transactionTime;
        draft.header.resultCode = action.header.resultCode;
        draft.header.transactionType = action.header.transactionType;
        draft.header.description = action.header.description;
        draft.header.data.id = action.header.data.id;
        draft.header.data.password = action.header.data.password;
        draft.header.data.emailAddress = action.header.data.emailAddress;
        draft.header.data.cellNumber = action.header.data.cellNumber;
        draft.header.session = action.header.session;
      });
    case ADMIN_LOGOUT:
      return produce(state, (draft) => {
        draft.header.transactionTime = null;
        draft.header.resultCode = null;
        draft.header.transactionType = null;
        draft.header.description = null;
        draft.header.data.id = null;
        draft.header.data.password = null;
        draft.header.data.emailAddress = null;
        draft.header.data.cellNumber = null;
        draft.header.session = null;
      });
    case ADMIN_LOADING:
      return produce(state, (draft) => {
        draft.loading = true;
      });
    case ADMIN_DONE:
      return produce(state, (draft) => {
        draft.loading = false;
      });
    default:
      return state;
  }
}
