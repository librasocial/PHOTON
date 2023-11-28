import React, { useState, useEffect } from "react";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import moment from "moment";

export default function PreviousInvestTable(props) {
  const mopostatus = (id) => {
    props.setPageChange(true);
    sessionStorage.setItem("poUser", "medical-Officer");
    sessionStorage.setItem("prevReport", "previous-Report");
    sessionStorage.setItem("LabOrderId", id);
  };

  const { datefrom, dateto } = props;
  return (
    <React.Fragment>
      <Paper
        sx={{ width: "100%", overflow: "hidden" }}
        className="previous-data-details"
      >
        <TableContainer sx={{ maxHeight: 440 }}>
          {props.prevDataArray.lenght == 0 ? (
            <Table stickyHeader aria-label="sticky table">
              <TableHead>
                <TableRow>
                  <TableCell align="center">No Data Found...!</TableCell>
                </TableRow>
              </TableHead>
            </Table>
          ) : (
            <Table stickyHeader aria-label="sticky table">
              <TableHead>
                <TableRow>
                  <TableCell>Date</TableCell>
                  <TableCell>Medical Officer</TableCell>
                  <TableCell colSpan={2}>Order Id</TableCell>
                </TableRow>
              </TableHead>
              {props.prevDataArray.map((investigationData, i) => (
                <TableBody key={i}>
                  <TableRow>
                    <TableCell width="25%">
                      <p className="prev-investigation-date">
                        {moment(investigationData?.orderDate).format(
                          "DD MMM YYYY, hh:mm A"
                        )}
                      </p>
                    </TableCell>
                    <TableCell width="30%">
                      {investigationData?.encounter?.staffName}
                    </TableCell>
                    <TableCell width="35%">
                      <p
                        className="approvalslink"
                        onClick={(e) => mopostatus(investigationData?.id)}
                      >
                        ID-{investigationData.id}
                      </p>
                    </TableCell>
                    <TableCell width="10%" align="right">
                      <i
                        className="bi bi-arrow-right-circle view-more-details"
                        onClick={(e) => mopostatus(investigationData.id)}
                      ></i>
                    </TableCell>
                  </TableRow>
                </TableBody>
              ))}
            </Table>
          )}
        </TableContainer>
      </Paper>
    </React.Fragment>
  );
}
