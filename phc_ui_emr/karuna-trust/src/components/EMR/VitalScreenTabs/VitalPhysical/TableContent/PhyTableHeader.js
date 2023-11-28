import React from "react";
import { TableCell, TableHead, TableRow } from "@mui/material";

function PhyTableHeader(props) {
  let tableHeader = [
    { name: "Name of the Joint" },
    { name: "Range of Motion" },
    { name: "Strength" },
    { name: "Wasting" },
    { name: "Sensation" },
    { name: "Reflexes" },
  ];
  return (
    <TableHead>
      <TableRow>
        {tableHeader.map((nameHead, i) => (
          <React.Fragment key={i}>
            <TableCell align="left">{nameHead.name}</TableCell>
          </React.Fragment>
        ))}
      </TableRow>
    </TableHead>
  );
}

export default PhyTableHeader;
