import produce from "immer";

const SELLER_LOGIN = "SELLER_LOGIN";
const SELLER_LOGOUT = "SELLER_LOGOUT";
const SELLER_LOADING = "SELLER_LOADING";
const SELLER_DONE = "SELLER_DONE";

export const sellerLogin = (header) => ({ type: SELLER_LOGIN, header });
export const sellerLogout = () => ({ type: SELLER_LOGOUT });
export const sellerLoading = () => ({ type: SELLER_LOADING });
export const sellerDone = () => ({ type: SELLER_DONE });

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
      products: [],
      soldAmount: null,
      orders: [],
      profileImageName: null,
    },
    session: null,
  },
  loading: false,
};

export default function SellerInfo(state = initialState, action) {
  switch (action.type) {
    case SELLER_LOGIN:
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
        draft.header.data.products = action.header.data.products;
        draft.header.data.soldAmount = action.header.data.soldAmount;
        draft.header.data.orders = action.header.data.orders;
        draft.header.data.profileImageName =
          action.header.data.profileImageName;
        draft.header.session = action.header.session;
      });
    case SELLER_LOGOUT:
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
        draft.header.data.products = [];
        draft.header.data.soldAmount = null;
        draft.header.data.orders = [];
        draft.header.data.profileImageName = null;
        draft.header.session = null;
      });
    case SELLER_LOADING:
      return produce(state, (draft) => {
        draft.loading = true;
      });
    case SELLER_DONE:
      return produce(state, (draft) => {
        draft.loading = false;
      });
    default:
      return state;
  }
}
