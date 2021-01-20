import produce from "immer";

const BUYER_LOGIN = "BUYER_LOGIN";
const BUYER_LOGOUT = "BUYER_LOGOUT";
const BUYER_LOADING = "BUYER_LOADING";
const BUYER_DONE = "BUYER_DONE";

export const buyerLogin = (header) => ({ type: BUYER_LOGIN, header });
export const buyerLogout = () => ({ type: BUYER_LOGOUT });
export const buyerLoading = () => ({ type: BUYER_LOADING });
export const buyerDone = () => ({ type: BUYER_DONE });

const initialState = {
  header: {
    transactionTime: null,
    resultCode: null,
    transactionType: null,
    description: null,
    data: {
      name: null,
      gender: null,
      dateOfBirth: null,
      emailAddress: null,
      cellNumber: null,
      latitude: null,
      longitude: null,
      address: null,
      job: [],
      cryptoWallet: null,
      purchases: [],
      reviews: [],
      profileImageName: null,
    },
    session: null,
  },
  loading: false,
};

export default function BuyerInfo(state = initialState, action) {
  console.log(action);
  switch (action.type) {
    case BUYER_LOGIN:
      return produce(state, (draft) => {
        draft.header.transactionTime = action.header.transactionTime;
        draft.header.resultCode = action.header.resultCode;
        draft.header.transactionType = action.header.transactionType;
        draft.header.description = action.header.description;
        draft.header.data.name = action.header.data.name;
        draft.header.data.gender = action.header.data.gender;
        draft.header.data.dateOfBirth = action.header.data.dateOfBirth;
        draft.header.data.emailAddress = action.header.data.emailAddress;
        draft.header.data.cellNumber = action.header.data.cellNumber;
        draft.header.data.latitude = action.header.data.latitude;
        draft.header.data.longitude = action.header.data.longitude;
        draft.header.data.address = action.header.data.address;
        draft.header.data.job = action.header.data.job;
        draft.header.data.cryptoWallet = action.header.data.cryptoWallet;
        draft.header.data.purchases = action.header.data.purchases;
        draft.header.data.reviews = action.header.data.reviews;
        draft.header.data.profileImageName =
          action.header.data.profileImageName;
        draft.header.session = action.header.session;
      });
    case BUYER_LOGOUT:
      return produce(state, (draft) => {
        draft.header.transactionTime = null;
        draft.header.resultCode = null;
        draft.header.transactionType = null;
        draft.header.description = null;
        draft.header.data.name = null;
        draft.header.data.gender = null;
        draft.header.data.dateOfBirth = null;
        draft.header.data.emailAddress = null;
        draft.header.data.cellNumber = null;
        draft.header.data.latitude = null;
        draft.header.data.longitude = null;
        draft.header.data.address = null;
        draft.header.data.job = [];
        draft.header.data.cryptoWallet = null;
        draft.header.data.purchases = [];
        draft.header.data.reviews = [];
        draft.header.data.profileImageName = null;
        draft.header.session = null;
      });
    case BUYER_LOADING:
      return produce(state, (draft) => {
        draft.loading = true;
      });
    case BUYER_DONE:
      return produce(state, (draft) => {
        draft.loading = false;
      });
    default:
      return state;
  }
}
