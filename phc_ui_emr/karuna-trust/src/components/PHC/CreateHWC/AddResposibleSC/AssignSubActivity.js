import React from "react";
import AssignProcess from "../../AssignOwnerAct/AssignProcess";
import { useSelector } from "react-redux";

let center_type = "Sub-Center";
export default function AssignSubActivity(props) {
  const { sunCenterDetails } = useSelector((state) => state.phcData);

  return (
    <React.Fragment>
      <AssignProcess
        center_type={center_type}
        sunCenterDetails={sunCenterDetails}
      />
    </React.Fragment>
  );
}
