import React from "react";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  Button,
} from "@mui/material";
import moment from "moment";
import { Link } from "react-router-dom";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import {
  loadInwardsById,
  loadUpdatePurchaseOrder,
} from "../../../redux/actions";
import { useDispatch, useSelector } from "react-redux";

function TableBodyPage(props) {
  let dispatch = useDispatch();

  let typeofuser = sessionStorage.getItem("typeofuser");
  const PurchaseOrderState = (e) => {
    props.setPageChange(true);
    props.setOrderId(e);
  };
  const addApproval = (orderId) => {
    var updateOrder = {
      type: "PO",
      properties: {
        status: "AUTHORISED",
      },
    };
    var updateResponse = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updateOrder),
    };
    dispatch(
      loadUpdatePurchaseOrder(orderId, updateResponse, props.setApprovalLoading)
    );
    // props.setApprovalLoading(true)
  };
  const editOrder = (e) => {
    props.setPageChange(true);
    props.setOrderId(e);
  };
  const editOrderIndent = (e) => {
    props.setPageChange1(true);
    props.setIndentId(e);
  };
  const viewOrderDetails = (e) => {
    props.setViewPage(true);
    props.setOrderId(e);
  };

  // for inwards page
  const editInwards = (e) => {
    props.setInwardsId(e);
    dispatch(loadInwardsById(e));
    props.setPageChange1(true);
  };
  // for inwards page

  // for product page
  const editProductdata = (e) => {
    props.addPharmaServiceShow(true);
    props.setProductId(e);
  };
  // for product page

  return (
    <React.Fragment>
      {props.Count == 0 ? (
        <TableBody>
          <TableRow align="center">
            <TableCell>No data found...!</TableCell>
          </TableRow>
        </TableBody>
      ) : (
        <React.Fragment>
          {props.PurchaseOrderList?.map((orders, ordIndex) => (
            <TableBody key={ordIndex}>
              {props.pageType === "Pharma-Products" ? (
                <TableRow>
                  <TableCell>{orders.group}</TableCell>
                  <TableCell>{orders.classification}</TableCell>
                  <TableCell>{orders.hsnCode}</TableCell>
                  <TableCell>{orders.name}</TableCell>
                  <TableCell>{orders.primaryUOM}</TableCell>
                  <TableCell>
                    {orders.status == "ACTIVE" && (
                      <span style={{ color: "green" }}>Available</span>
                    )}
                    {orders.status == "INACTIVE" && (
                      <span style={{ color: "red" }}>Un-Available</span>
                    )}
                  </TableCell>
                  <TableCell align="center">
                    <div className="btn-group product-master-drp">
                      <button
                        type="button"
                        className="btn btn-primary dotBtn"
                        data-toggle="dropdown"
                      >
                        <i
                          className="bi bi-pencil-square"
                          style={{ color: "black" }}
                        ></i>
                      </button>
                      <div className="dropdown-menu" id="show">
                        <div className="dot-dropdown-menu">
                          <p
                            className="dropdown-item"
                            data-toggle="modal"
                            data-target="#PatientDetails-wndow"
                            //   onClick={(e) => addReferance(row.id)}
                          >
                            View
                          </p>
                          <p
                            className="dropdown-item"
                            data-toggle="modal"
                            data-target="#PatientDetails-wndow"
                            onClick={(e) => editProductdata(orders.id)}
                          >
                            Edit
                          </p>
                        </div>
                      </div>
                    </div>
                  </TableCell>
                </TableRow>
              ) : (
                <TableRow>
                  <TableCell align="center">
                    {props.page * props.rowsPerPage + (ordIndex + 1)}
                  </TableCell>
                  <TableCell align="left">
                    {typeofuser === "Pharmacist" ? (
                      <p className="ordersText">
                        {" "}
                        {orders.poDetails ? (
                          <>{orders.id}</>
                        ) : (
                          <>{orders.id}</>
                        )}{" "}
                      </p>
                    ) : (
                      <>
                        {orders.poDetails != null && (
                          <Link
                            className="ordersLink"
                            to="/poApprovals"
                            onClick={(e) => editOrder(orders.id)}
                          >
                            {orders.id}
                          </Link>
                        )}
                        {orders.indentItems != null && (
                          <Link
                            className="ordersLink"
                            to="/poApprovals"
                            onClick={(e) => editOrderIndent(orders.id)}
                          >
                            {orders.id}
                          </Link>
                        )}
                      </>
                    )}
                  </TableCell>
                  {props.pageType === "Inwards" && (
                    <TableCell align="left">{/* Drug */}</TableCell>
                  )}
                  {props.pageType === "Orders" && (
                    <TableCell align="left" style={{ fontStyle: "italic" }}>
                      {moment(new Date(orders.poDate)).format("DD-MMM-YYYY")}
                    </TableCell>
                  )}
                  {props.pageType === "Inwards" && (
                    <TableCell align="left">
                      {orders.indentDetails != null &&
                        moment(
                          new Date(orders.indentDetails.challanDate)
                        ).format("DD-MMM-YYYY")}
                      {orders.poDetails != null &&
                        moment(new Date(orders.poDetails.poDate)).format(
                          "DD-MMM-YYYY"
                        )}
                    </TableCell>
                  )}
                  <TableCell align="left">
                    {orders.supplierName.length > 25
                      ? orders.supplierName.substring(0, 25) + "..."
                      : orders.supplierName}
                  </TableCell>
                  {typeofuser === "Pharmacist" ? (
                    ""
                  ) : (
                    <TableCell align="left">Mr. Robert Binzel</TableCell>
                  )}
                  <TableCell align="left">
                    {props.pageType == "Orders" &&
                      orders.poDetails != null &&
                      "Purchase Order"}
                    {props.pageType == "Orders" &&
                      orders.indentItems != null &&
                      "Indent Order"}
                    {props.pageType == "Inwards" &&
                      orders.inwardType == "PO" &&
                      "Againist PO"}
                    {props.pageType == "Inwards" &&
                      orders.inwardType == "Indent" &&
                      "Againist Indent"}
                    {props.pageType == "Inwards" &&
                      orders.inwardType == "Direct Inwards" &&
                      "Direct Inwards"}
                  </TableCell>
                  <TableCell align="left">
                    {typeofuser === "Pharmacist" ? (
                      <label
                        className={
                          (orders.status == "DRAFT" &&
                            "al-status alert-draft") ||
                          (orders.status == "CREATED" &&
                            "al-status alert-created") ||
                          (orders.status == "AUTHORISED" &&
                            "al-status alert-auth") ||
                          (orders.status == "COMPLETED" &&
                            "al-status alert-comp") ||
                          (orders.status == "RAISED" &&
                            "al-status alert-raised")
                        }
                      >
                        {orders.status}
                      </label>
                    ) : (
                      <label
                        className={
                          (orders.status == "CREATED" &&
                            "al-status alert-created") ||
                          (orders.status == "AUTHORISED" &&
                            "al-status alert-auth")
                        }
                      >
                        {orders.status}
                      </label>
                    )}
                  </TableCell>
                  {props.pageType == "Inwards" && (
                    <TableCell align="left">
                      {orders.indentDetails != null &&
                        orders.indentDetails.deliveryChallanNo}
                      {orders.poDetails != null && orders.poDetails.poId}
                    </TableCell>
                  )}
                  {typeofuser === "Pharmacist" ? (
                    <TableCell align="center" colSpan={2}>
                      {props.pageType === "Inwards" && (
                        <>
                          {orders.indentItems != null && (
                            <Button
                              className={
                                orders.status != "DRAFT"
                                  ? "action-disable"
                                  : "action-btn"
                              }
                              disabled={orders.status != "DRAFT"}
                            >
                              <i
                                className="bi bi-pencil-square"
                                onClick={(e) => editInwards(orders.id)}
                              ></i>
                            </Button>
                          )}
                        </>
                      )}
                      {props.pageType === "Orders" && (
                        <>
                          {orders.poDetails != null && (
                            <Button
                              className={
                                orders.status == "COMPLETED"
                                  ? "action-disable"
                                  : "action-btn"
                              }
                              disabled={orders.status == "COMPLETED"}
                            >
                              <i
                                className="bi bi-pencil-square"
                                onClick={(e) => editOrder(orders.id)}
                              ></i>
                            </Button>
                          )}
                          {orders.indentItems != null && (
                            <Button
                              className={
                                orders.status == "COMPLETED"
                                  ? "action-disable"
                                  : "action-btn"
                              }
                              disabled={orders.status == "COMPLETED"}
                            >
                              <i
                                className="bi bi-pencil-square"
                                onClick={(e) => editOrderIndent(orders.id)}
                              ></i>
                            </Button>
                          )}
                        </>
                      )}
                      |
                      {props.pageType === "Inwards" ? (
                        <Button className="action-btn" disabled>
                          <i
                            className="fa fa-file-text-o"
                            // onClick={NewInwardsBillState}
                          ></i>
                        </Button>
                      ) : (
                        <>
                          {orders.poDetails != null && (
                            <Button
                              className={
                                orders.status == "DRAFT"
                                  ? "action-disable"
                                  : "action-btn"
                              }
                              disabled={orders.status == "DRAFT"}
                            >
                              <i
                                className="fa fa-file-text-o"
                                onClick={(e) => viewOrderDetails(orders.id)}
                              ></i>
                            </Button>
                          )}
                          {orders.indentItems != null && (
                            <Button
                              className={
                                orders.status == "DRAFT"
                                  ? "action-disable"
                                  : "action-btn"
                              }
                              disabled={orders.status == "DRAFT"}
                            >
                              <i
                                className="fa fa-file-text-o "
                                onClick={(e) => viewOrderDetails(orders.id)}
                              ></i>
                            </Button>
                          )}
                        </>
                      )}
                    </TableCell>
                  ) : (
                    <TableCell align="center">
                      {orders.status == "CREATED" ? (
                        <p
                          className="ordersLink"
                          onClick={(e) => addApproval(orders.id)}
                        >
                          <i className="fa fa-thumbs-o-up approvals-details"></i>{" "}
                          Authorise
                        </p>
                      ) : (
                        <p className="" style={{ opacity: "0.6" }}>
                          <i className="fa fa-thumbs-o-up approvals-details"></i>{" "}
                          Authorised
                        </p>
                      )}
                    </TableCell>
                  )}
                </TableRow>
              )}
            </TableBody>
          ))}
        </React.Fragment>
      )}
    </React.Fragment>
  );
}

export default TableBodyPage;
