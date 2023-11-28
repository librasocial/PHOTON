import React, { useState, useEffect } from "react";
import moment from "moment";
import ViewReport from "../../LabModule/LabScreens/ViewReport";

export default function InvestigationReport(props) {
  const previousTable = () => {
    props.setPageChange(false);
    sessionStorage.removeItem("poUser");
    sessionStorage.removeItem("prevReport");
  };

  return (
    <React.Fragment>
      <p className="backList">
        <i
          className="bi bi-arrow-left-circle view-less-details"
          onClick={previousTable}
        ></i>{" "}
        Back To List
      </p>
      <ViewReport />
    </React.Fragment>
  );
}
