import React from "react";
import { Button } from "react-bootstrap";
import SaveButton from "./components/EMR_Buttons/SaveButton";

function PageNotFound() {
  const goBack = () => {
    window.history.back();
  };
  return (
    <div className="page-not-found-div">
      <div align="center">
        <h2 style={{ fontSize: "60px", fontWeight: "900" }}>404</h2>
        <h3>Page Not Found</h3>
        <div>
          <SaveButton
            class_name="regBtnN"
            button_name="Go Back"
            butttonClick={goBack}
          />
        </div>
      </div>
    </div>
  );
}

export default PageNotFound;
