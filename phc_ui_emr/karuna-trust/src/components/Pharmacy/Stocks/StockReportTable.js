import React, { useState } from "react";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
} from "@mui/material";
import "../../../css/Services.css";
import "./Stock.css";

export default function StockReportTable(props) {
  let tableData = props.tableData;
  let type = props.type;
  let count = parseInt(props.tableData.length);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const handleChangePage = (e, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (e) => {
    setRowsPerPage(+e.target.value);
    setPage(0);
  };
  return (
    <React.Fragment>
      <Paper
        sx={{ width: "100%", overflow: "hidden", margin: "2% 0 0 0" }}
        className="my-paper order-table"
      >
        <TableContainer sx={{ maxHeight: 440 }}>
          <Table
            stickyHeader
            aria-label="sticky table"
            className="my-paper-table"
          >
            <TableHead className="my-paper-table-head">
              <TableRow>
                <TableCell align="center">Sl No.</TableCell>
                <TableCell align="left">Product Name</TableCell>
                <TableCell align="center">UOM</TableCell>
                {type == "detailed" && (
                  <TableCell align="center">Batch No.</TableCell>
                )}
                {type == "detailed" && (
                  <TableCell align="center">EXP Date</TableCell>
                )}
                <TableCell align="center">Current Stock</TableCell>
              </TableRow>
            </TableHead>
            {tableData
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((stockItem, i) => (
                <TableBody key={i}>
                  <TableRow>
                    <TableCell
                      align="center"
                      rowSpan={
                        type == "detailed"
                          ? stockItem.stockDetails.length + 1
                          : ""
                      }
                    >
                      {page * rowsPerPage + (i + 1)}
                    </TableCell>
                    <TableCell
                      align="left"
                      rowSpan={
                        type == "detailed"
                          ? stockItem.stockDetails.length + 1
                          : ""
                      }
                    >
                      {stockItem.product_name}
                    </TableCell>
                    <TableCell
                      align="center"
                      rowSpan={
                        type == "detailed"
                          ? stockItem.stockDetails.length + 1
                          : ""
                      }
                    >
                      {stockItem.uom}
                    </TableCell>
                    {type == "consolidated" && (
                      <TableCell align="center">{stockItem.stock}</TableCell>
                    )}
                  </TableRow>
                  {type == "detailed" &&
                    stockItem.stockDetails.map((detailStock, index) => (
                      <TableRow key={index}>
                        <TableCell align="center">
                          {detailStock.batchNo}
                        </TableCell>
                        <TableCell align="center">
                          {detailStock.expireDate}
                        </TableCell>
                        <TableCell align="center">
                          {detailStock.stock}
                        </TableCell>
                      </TableRow>
                    ))}
                </TableBody>
              ))}
          </Table>
        </TableContainer>
        <TablePagination
          className="pagination-body"
          rowsPerPageOptions={[5, 10, 25, 50]}
          component="div"
          count={count}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </Paper>
    </React.Fragment>
  );
}
