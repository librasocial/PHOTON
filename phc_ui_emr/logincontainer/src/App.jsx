import React from "react";
import ReactDOM from "react-dom";
import Logincontainer from "./Logincontainer";
import "./index.scss";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "../node_modules/bootstrap/dist/js/bootstrap.bundle";

const App = () => (
  <React.Fragment>
    <Logincontainer />
  </React.Fragment>
);
ReactDOM.render(<App />, document.getElementById("app"));
