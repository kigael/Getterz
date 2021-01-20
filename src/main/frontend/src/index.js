import React from "react";
import ReactDOM from "react-dom";
import Routing from "./Routing";
import reportWebVitals from "./reportWebVitals";
import { HashRouter as Router } from "react-router-dom";
import { createStore } from "redux";
import { Provider } from "react-redux";
import rootReducer from "./info";

const store = createStore(rootReducer);

ReactDOM.render(
  <React.StrictMode>
    <Router>
      <Provider store={store}>
        <Routing />
      </Provider>
    </Router>
  </React.StrictMode>,
  document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
