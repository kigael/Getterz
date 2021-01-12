const SELLER_LOGIN = "SELLER_LOGIN";
const SELLER_LOGOUT = "SELLER_LOGOUT";
const SELLER_LOADING = "SELLER_LOADING";
const SELLER_DONE = "SELLER_DONE";

export const sellerLogin = (header) => ({ type: SELLER_LOGIN, header });
export const sellerLogout = () => ({ type: SELLER_LOGOUT });

const initialState = {
  header: {
    transactionTime: null,
    resultCode: null,
    transactionType: null,
    description: null,
    data: null,
    session: null,
  },
};

export default function SellerInfo(state = initialState, action) {
  switch (action.type) {
    case SELLER_LOGIN:
      return {
        //immer
      };
    case SELLER_LOGOUT:
      return {
        //immer
      };
    default:
      return state;
  }
}
