import React, { useEffect, useState } from "react";
import { TableCell } from "@mui/material";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";

function FacilityHead(props) {
  const [facHeadName, setFacHeadName] = useState("");
  useEffect(() => {
    if (props.facId) {
      props.setPageLoader(true);
      fetch(
        `${constant.ApiUrl}/member-svc/members/relationships/filter?srcType=Facility&srcNodeId=${props.facId}&rel=HEADOF&targetType=Employee`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          if (resp["content"][0]) {
            setFacHeadName(
              resp["content"][0]["targetNode"]["properties"]["type"]
            );
          } else {
            setFacHeadName("-");
          }
          props.setPageLoader(false);
        });
    }
  }, [props.facId]);

  return (
    <TableCell align="left" style={{ width: "20%" }}>
      {facHeadName}
    </TableCell>
  );
}

export default FacilityHead;
