import React from "react";
import { TableCell, TableHead, TableRow } from "@mui/material";

function FormTableHeader(props) {
  return (
    <TableHead>
      <TableRow>
        {props.tableHeader.map((nameHead, i) => (
          <React.Fragment key={i}>
            {nameHead.icon == "rate" ? (
              <TableCell
                align={nameHead.name == "Action" ? "center" : "left"}
                className={
                  nameHead.class == "class1"
                    ? "theader-1"
                    : nameHead.class == "class2"
                    ? "theader-2"
                    : nameHead.class == "class3"
                    ? "theader-3"
                    : nameHead.class == "class4"
                    ? "theader-4"
                    : ""
                }
              >
                {nameHead.name} (&#8377;)
              </TableCell>
            ) : (
              <TableCell
                align={nameHead.name == "Action" ? "center" : "left"}
                className={
                  nameHead.class == "class1"
                    ? "theader-1"
                    : nameHead.class == "class2"
                    ? "theader-2"
                    : nameHead.class == "class3"
                    ? "theader-3"
                    : nameHead.class == "class4"
                    ? "theader-4"
                    : nameHead.class == "class5"
                    ? "theader-5"
                    : ""
                }
              >
                {nameHead.name}
              </TableCell>
            )}
          </React.Fragment>
        ))}
      </TableRow>
    </TableHead>
  );
}

export default FormTableHeader;
