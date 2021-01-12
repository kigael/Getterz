import React from "react";
import { Route, Switch } from "react-router-dom";
import AboutGetterz from "./AboutGetterz";
import Gate from "./Gate";
import * as B from "./Buyer";
import * as S from "./Seller";
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
        <Route path="/seller" component={Gate} exact />
        <Route path="/seller/login" component={S.Login} exact />
        <Route path="/buyer" component={B.Gate} exact />
        <Route path="/buyer/login" component={B.Login} exact />
        <Route path="/buyer/signup" component={B.Signup} exact />
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
