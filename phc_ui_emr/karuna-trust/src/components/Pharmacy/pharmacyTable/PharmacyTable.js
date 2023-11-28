import React from "react";
import { Paper, Table, TableContainer, TablePagination } from "@mui/material";
import TableHeader from "./TableHeader";
import TableBodyPage from "./TableBodyPage";

function PharmacyTable(props) {
  return (
    <div>
      <div className="pro-field">
        <Paper sx={{ width: "100%", overflow: "hidden", margin: "2% 0 0 0" }}>
          <TableContainer sx={{ maxHeight: 440 }}>
            <Table
              stickyHeader
              aria-label="sticky table"
              className="order-paper-table"
            >
              <TableHeader
                tableHeader={props.tableHeader}
                pageType={props.pageType}
                supplierItem={props.supplierItem}
                setSuplierName={props.setSuplierName}
                suplierName={props.suplierName}
                supplierItem1={props.supplierItem1}
                setSelectedType={props.setSelectedType}
                selectedType={props.selectedType}
                supplierItem2={props.supplierItem2}
                setStatusValue={props.setStatusValue}
                statusValue={props.statusValue}
                setUnAlphaState={props.setUnAlphaState}
                setAlphaState={props.setAlphaState}
                setAlphaStateClass={props.setAlphaStateClass}
                setAlphaStateName={props.setAlphaStateName}
                setUnAlphaStateClass={props.setUnAlphaStateClass}
                setUnAlphaStateName={props.setUnAlphaStateName}
                alphaState={props.alphaState}
                alphaStateClass={props.alphaStateClass}
                alphaStateName={props.alphaStateName}
              />
              <TableBodyPage
                Count={props.Count}
                setPageChange={props.setPageChange}
                setApprovalLoading={props.setApprovalLoading}
                PurchaseOrderList={props.PurchaseOrderList}
                setOrderId={props.setOrderId}
                setPageChange1={props.setPageChange1}
                setIndentId={props.setIndentId}
                pageType={props.pageType}
                setViewPage={props.setViewPage}
                setInwardsId={props.setInwardsId}
                addPharmaServiceShow={props.addPharmaServiceShow}
                rowsPerPage={props.rowsPerPage}
                page={props.page}
                setProductId={props.setProductId}
              />
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

export default PharmacyTable;
