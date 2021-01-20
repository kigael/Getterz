import React from "react";
import { Route, Switch } from "react-router-dom";
import AboutGetterz from "./AboutGetterz";
import Gate from "./Gate";
import * as B from "./Buyer";
import * as S from "./Seller";
import * as A from "./Admin";
import { ThemeProvider, createMuiTheme } from "@material-ui/core/styles";

const theme = createMuiTheme({
  typography: {
    fontFamily: ["Anton", "sans-serif"].join(","),
  },
});

function Routing() {
  return (
    <ThemeProvider theme={theme}>
      <Switch>
        <Route path="/" component={Gate} exact />
        <Route path="/about" component={AboutGetterz} exact />
        <Route path="/admin" component={A.Gate} exact />
        <Route path="/admin/signup" component={A.Signup} exact />
        <Route path="/admin/login" component={A.Login} exact />
        <Route path="/admin/buyer_verify" component={A.BuyerVerify} exact />
        <Route
          path="/admin/buyer_verify/:buyerId"
          component={A.BuyerVerifyCertify}
          exact
        />
        <Route path="/admin/seller_verify" component={A.SellerVerify} exact />
        <Route
          path="/admin/seller_verify/:sellerId"
          component={A.SellerVerifyCertify}
          exact
        />
        <Route path="/admin/buyer_refund" component={A.BuyerRefund} exact />
        <Route
          path="/admin/seller_withdraw"
          component={A.SellerWithdraw}
          exact
        />
        <Route path="/seller" component={S.Gate} exact />
        <Route path="/seller/signup" component={S.Signup} exact />
        <Route path="/seller/verify_email" component={S.VerifyEmail} exact />
        <Route path="/seller/login" component={S.Login} exact />
        <Route path="/seller/product" component={S.Product} exact />
        <Route
          path="/seller/product/register"
          component={S.ProductRegister}
          exact
        />
        <Route path="/seller/order" component={S.Order} exact />
        <Route path="/seller/withdraw" component={S.Withdraw} exact />
        <Route path="/seller/manage" component={S.Manage} exact />
        <Route path="/buyer" component={B.Gate} exact />
        <Route path="/buyer/signup" component={B.Signup} exact />
        <Route path="/buyer/verify_email" component={B.VerifyEmail} exact />
        <Route path="/buyer/login" component={B.Login} exact />
        <Route path="/buyer/search" component={B.Search} exact />
        <Route path="/buyer/purchase" component={B.Purchase} exact />
        <Route path="/buyer/review" component={B.Review} exact />
        <Route path="/buyer/manage" component={B.Manage} exact />
        <Route path="*" component={Gate} />
      </Switch>
    </ThemeProvider>
  );
}

export default Routing;
