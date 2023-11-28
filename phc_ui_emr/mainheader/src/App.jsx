import React from "react";
import ReactDOM from "react-dom";
import Mainheader from "./Mainheader";

const App = () => (
  <React.Fragment>
    <Mainheader />
  </React.Fragment>
);
ReactDOM.render(<App />, document.getElementById("app"));
