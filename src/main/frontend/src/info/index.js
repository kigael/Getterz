import { combineReducers } from "redux";
import AdminInfo from "./AdminInfo";
import SellerInfo from "./SellerInfo";
import BuyerInfo from "./BuyerInfo";

const rootReducer = combineReducers({
  AdminInfo,
  SellerInfo,
  BuyerInfo,
});

export default rootReducer;
