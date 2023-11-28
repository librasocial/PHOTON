import React from "react";
import AssignProcess from "../../AssignOwnerAct/AssignProcess";

let center_type = "Phc";
function AssignOwnerAct() {
  return (
    <div>
      <AssignProcess center_type={center_type} />
    </div>
  );
}

export default AssignOwnerAct;
