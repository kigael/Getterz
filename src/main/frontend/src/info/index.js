import { combineReducers } from "redux";
import SellerInfo from "./SellerInfo";
import BuyerInfo from "./BuyerInfo";

const rootReducer = combineReducers({
  SellerInfo,
  BuyerInfo,
});

export default rootReducer;
