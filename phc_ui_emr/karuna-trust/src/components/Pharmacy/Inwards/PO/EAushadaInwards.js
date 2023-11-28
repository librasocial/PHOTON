import React from "react";
import {
  Paper,
  Table,
  TableRow,
  TableCell,
  TableContainer,
  TablePagination,
  TableBody,
  TableHead,
} from "@mui/material";

function EAushadaInwards(props) {
  return (
    <div style={{ width: "100%" }}>
      <div className="pro-field" style={{ width: "100%" }}>
        <Paper sx={{ width: "100%", overflow: "hidden", margin: "2% 0 0 0" }}>
          <TableContainer sx={{ maxHeight: 440 }}>
            <Table
              stickyHeader
              aria-label="sticky table"
              className="order-paper-table"
            >
              <TableHead>
                <TableRow>
                  <TableCell>Sl No </TableCell>
                  <TableCell>Inward No</TableCell>
                  <TableCell>Drug ID</TableCell>
                  <TableCell>Drug Name</TableCell>
                  {/* <TableCell>Institute ID</TableCell> */}
                  {/* <TableCell>Institute Name</TableCell> */}
                  {/* <TableCell>Institute Type</TableCell> */}
                  <TableCell>Receipt Date</TableCell>
                  <TableCell>Batch Number</TableCell>
                  <TableCell>Manufacturing Date</TableCell>
                  <TableCell>Expiry Date</TableCell>
                  <TableCell>Quantity In Pack</TableCell>
                  <TableCell>Unit Pack</TableCell>
                  <TableCell>Quantity In Units</TableCell>
                  <TableCell>Institute Name</TableCell>
                  {/* <TableCell>Available Quantity</TableCell> */}
                  {/* <TableCell>Warehouse Name</TableCell> */}
                  {/* <TableCell>Drug ID</TableCell> */}
                  {/* <TableCell>Drug Name</TableCell> */}
                  {/* <TableCell>Standard Quality</TableCell> */}
                </TableRow>
              </TableHead>

              <TableBody>
                {props.fetchedData.length > 0 ? (
                  props.fetchedData.map((item, index) => (
                    <TableRow key={index}>
                      <TableCell>{item.Sl_No}</TableCell>
                      <TableCell>{item.inwardno}</TableCell>
                      <TableCell>{item.Drug_id}</TableCell>
                      <TableCell>{item.Drug_name}</TableCell>
                      {/* <TableCell>{item.instituteid}</TableCell> */}
                      {/* <TableCell>{item.Institute_name}</TableCell> */}
                      {/* <TableCell>{item.InstituteType}</TableCell> */}
                      <TableCell>{item.Receipt_Date}</TableCell>
                      <TableCell>{item.Batch_number}</TableCell>
                      <TableCell>{item.Mfg_date}</TableCell>
                      <TableCell>{item.Exp_date}</TableCell>
                      <TableCell>{item.Quantity_In_Pack}</TableCell>
                      <TableCell>{item.UnitPack}</TableCell>
                      <TableCell>{item.Quantity_In_Units}</TableCell>
                      <TableCell>{item.Institute_name}</TableCell>
                      {/* <TableCell>{item.Available_quantity}</TableCell> */}
                      {/* <TableCell>{item.Warehouse_name}</TableCell> */}
                      {/* <TableCell>{item.Drug_id}</TableCell> */}
                      {/* <TableCell>{item.Drug_name}</TableCell> */}
                      {/* <TableCell>{item.StandardQuality}</TableCell> */}
                    </TableRow>
                  ))
                ) : (
                  <TableRow align="center">
                    <TableCell>No data found...!</TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
          {props.Count > 5 && (
            <TablePagination
              className="pagination-body"
              rowsPerPageOptions={[5, 10, 25, 50]}
              component="div"
              count={props.Count || 0}
              rowsPerPage={props.rowsPerPage}
              page={props.page}
              onPageChange={props.handleChangePage}
              onRowsPerPageChange={props.handleChangeRowsPerPage}
            />
          )}
        </Paper>
      </div>
    </div>
  );
}

export default EAushadaInwards;
