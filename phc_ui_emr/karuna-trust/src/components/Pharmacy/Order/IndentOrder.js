import React, { useState, useEffect } from "react";
import {
  Button,
  Row,
  Col,
  Breadcrumb,
  Form,
  OverlayTrigger,
  Tooltip,
} from "react-bootstrap";
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
import AddCircleRoundedIcon from "@mui/icons-material/AddCircleRounded";
import moment from "moment";
import CancelOrder from "./CancelOrder";
import NewIndentOrder from "./NewIndentOrder";
import "../../../css/Services.css";
import "../PharmacyTab.css";
import { useDispatch, useSelector } from "react-redux";
import {
  loadAddPurchaseOrder,
  loadSingleOrder,
  loadUpdatePurchaseOrder,
  loadOrdersList,
} from "../../../redux/actions";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import Loader from "../Dashboard/Loader";

function IndentOrder(props) {
  let InUser = sessionStorage.getItem("InUser");
  let indentId;
  if (InUser === "medical-Officer") {
    indentId = sessionStorage.getItem("inID");
  } else {
    indentId = props.indentId;
  }

  let dispatch = useDispatch();
  const { SingleOrder } = useSelector((state) => state.data);

  const onClickChange1 = () => {
    props.setPageChange1(false);
    props.setIndentId("");
    dispatch(loadOrdersList(props.page, props.rowsPerPage));
  };

  useEffect(() => {
    dispatch(loadSingleOrder(indentId));
  }, [indentId]);

  const [listState, setListState] = useState(false);
  useEffect(() => {
    if (SingleOrder && indentId) {
      setIndentOrder({ ...SingleOrder });
      if (listState == false) {
        setIndentList(SingleOrder?.indentItems);
        //     setProductList(SingleOrder?.poDetails?.poItems)
      } else {
        setIndentList(indentList);
      }
    }
  }, [SingleOrder, indentId]);

  const [indentList, setIndentList] = useState([]);

  const handleRemoveClick = (index) => {
    const list = [...indentList];
    list.splice(index, 1);
    setIndentList(list);
  };

  let date = moment(new Date()).format("YYYY-MM-DD hh:mm:ss");
  let today = moment(new Date(date)).format("YYYY-MM-DDThh:mm:ss.SSS");
  const [indentOrder, setIndentOrder] = useState({
    supplierName: "",
    type: "",
    remarks: "",
    indentItems: indentList,
    status: "",
    poDate: today,
  });

  useEffect(() => {
    if (indentList?.length != 0) {
      indentOrder.indentItems = indentList;
    }
  }, [indentList]);

  const { supplierName, type, remarks, indentItems, status, poDate } =
    indentOrder;

  const handleIndentInput = (e) => {
    const { name, value } = e.target;
    setIndentOrder({ ...indentOrder, [name]: value });
  };

  const [addIndentService, setAddIndentService] = useState(false);
  const addIndentServiceClose = () => setAddIndentService(false);
  const addIndentServiceShow = () => setAddIndentService(true);
  const [indentLoading, setIndentLoading] = useState("");

  const addIndentOrder = () => {
    if (!supplierName || !type || !status || indentList?.length == 0) {
      alert("Please fill required fields/add atleast one product");
    } else {
      var postResponse = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(indentOrder),
      };
      dispatch(loadAddPurchaseOrder(postResponse, setIndentLoading));
    }
  };

  const updateIndentOrder = (indentId) => {
    if (!supplierName || !type || !status || indentList?.length == 0) {
      alert("Please fill required fields/add atleast one product");
    } else {
      var updateOrder = {
        type: "PO",
        properties: indentOrder,
      };
      var updateResponse = {
        headers: serviceHeaders.myHeaders1,
        method: "PATCH",
        mode: "cors",
        body: JSON.stringify(updateOrder),
      };
      dispatch(
        loadUpdatePurchaseOrder(indentId, updateResponse, setIndentLoading)
      );
    }
  };

  // edit added product item
  const [editProduct, setEditProduct] = useState({});
  const [editProductID, setEditProductID] = useState("");

  const editProductItem = (e) => {
    let productId = e;
    setEditProductID(e);
    for (var i = 0; i < indentList?.length; i++) {
      if (indentList[i].productId == productId) {
        setEditProduct(indentList[i]);
      }
    }
    addIndentServiceShow(true);
  };
  // edit added product item

  // modal for cancel button
  const [cancelOrderService, setCancelOrderService] = useState(false);
  const [indentStatus, setIndentStatus] = useState(false);
  const cancelOrderServiceClose = () => setCancelOrderService(false);
  const cancelOrderServiceShow = () => {
    setIndentStatus(true);
    setCancelOrderService(true);
  };
  // modal for cancel button

  function closeIndentLoader() {
    props.setPage(0);
    props.setRowsPerPage(5);
    dispatch(loadOrdersList(props.page, props.rowsPerPage));
    props.setPageChange1(false);
  }

  const onClickApproval = () => {
    props.setPageChange1(false);
    sessionStorage.removeItem("poUser");
    sessionStorage.removeItem("poID");
    sessionStorage.removeItem("InUser");
    sessionStorage.removeItem("inID");
    window.history.back();
    // history.push("/approvals");
    dispatch(loadOrdersList(props.page, props.rowsPerPage));
  };

  let typeofuser = sessionStorage.getItem("typeofuser");

  return (
    <div>
      <Loader
        indentLoading={indentLoading}
        closeIndentLoader={closeIndentLoader}
        status={status}
      />
      <CancelOrder
        cancelOrderService={cancelOrderService}
        cancelOrderServiceClose={cancelOrderServiceClose}
        setIndentStatus={setIndentStatus}
        indentStatus={indentStatus}
        setPageChange={props.setPageChange1}
      />
      <NewIndentOrder
        addIndentService={addIndentService}
        addIndentServiceClose={addIndentServiceClose}
        setIndentList={setIndentList}
        editProduct={editProduct}
        editProductID={editProductID}
        setEditProductID={setEditProductID}
        indentList={indentList}
        setListState={setListState}
      />
      <div className="regHeader">
        <h1 className="register-Header">Indent</h1>
      </div>
      <hr style={{ margin: "0px" }} />
      <div className="porder-tab">
        <div className="pharma-form">
          <Breadcrumb>
            <Breadcrumb.Item
              className="pur-order-breadcrumb"
              onClick={
                typeofuser === "Pharmacist" ? onClickChange1 : onClickApproval
              }
            >
              {typeofuser === "Pharmacist" ? "Orders" : "Approvals"}
            </Breadcrumb.Item>
            <Breadcrumb.Item active>
              {indentId ? "Edit Indent" : "New Indent"}
            </Breadcrumb.Item>
          </Breadcrumb>
          <div className="pro-tab purchase-order">
            <div>
              <div className="porder-border">
                <h1 className="porder-head">
                  {indentId ? "Edit Indent" : "New Indent"}
                </h1>
                <Row className="purchase-order-div">
                  <Col md={4}>
                    <div>
                      <Form.Group>
                        <Form.Label>Supplier</Form.Label>
                        <Form.Select
                          aria-label="Default select example"
                          name="supplierName"
                          value={supplierName || ""}
                          onChange={handleIndentInput}
                        >
                          <option value="" disabled hidden>
                            Select Supplier...
                          </option>
                          <option value="District / Taluk Health Office">
                            District / Taluk Health Office
                          </option>
                          <option value="Drug Warehouse Logistics Society">
                            Drug Warehouse Logistics Society
                          </option>
                          <option value="KT Office(Jan Aushadhi Kendra)">
                            KT Office(Jan Aushadhi Kendra)
                          </option>
                          <option value="Other">Other</option>
                          <option value="Vaccine Store">Vaccine Store</option>
                        </Form.Select>
                      </Form.Group>
                    </div>
                  </Col>
                  <Col md={4}>
                    <div>
                      <Form.Group>
                        <Form.Label>Indent Type</Form.Label>
                        <Form.Select
                          aria-label="Default select example"
                          name="type"
                          value={type || ""}
                          onChange={handleIndentInput}
                        >
                          <option value="" disabled hidden>
                            Select Classification
                          </option>
                          <option value="Drugs">Drugs</option>
                          <option value="Catheters">Catheters</option>
                        </Form.Select>
                      </Form.Group>
                    </div>
                  </Col>
                  <Col md={4}>
                    <div>
                      <Form.Group>
                        <Form.Label>Remarks</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter PO Remarks..."
                          name="remarks"
                          value={remarks || ""}
                          onChange={handleIndentInput}
                        />
                      </Form.Group>
                    </div>
                  </Col>
                </Row>
              </div>
            </div>
          </div>
          <div className="pro-tab add-purchase-order">
            {indentList?.length == 0 ? (
              <div className="add-item-btn text-center">
                <img
                  src="../img/Pharmacy/inwards.png"
                  className="rounded mx-auto d-block inwards-img"
                />
                <p className="add-drugs-material">
                  No Drugs or Materials have been added. Please Add Items to
                  Create Purchase Order.
                </p>
                <Button
                  type="button"
                  className="pharmaBtnPC"
                  onClick={addIndentServiceShow}
                >
                  <AddCircleRoundedIcon /> &nbsp; Add Item
                </Button>
              </div>
            ) : (
              <div>
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
                          <TableCell align="left">Product Name</TableCell>
                          <TableCell align="left">UOM</TableCell>
                          <TableCell align="left">Indent Quality</TableCell>
                          <TableCell align="center" colSpan={2}>
                            Action
                          </TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {indentList?.map((addProd, prodIndex) => (
                          <TableRow key={prodIndex}>
                            <TableCell align="left">
                              {addProd.productName}
                            </TableCell>
                            <TableCell align="left">{addProd.uom}</TableCell>
                            <TableCell align="left">
                              {addProd.quantity}
                            </TableCell>
                            <TableCell colSpan={2} align="center">
                              <i
                                className="bi bi-pencil-square mypen"
                                onClick={(e) =>
                                  editProductItem(addProd.productId)
                                }
                              ></i>
                              <i
                                className="bi bi-trash-fill mytrash"
                                onClick={() => handleRemoveClick(prodIndex)}
                              ></i>
                            </TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                </Paper>
                <div className="add-btn">
                  <Button
                    type="button"
                    className="pharmaBtnPC"
                    onClick={addIndentServiceShow}
                  >
                    <AddCircleRoundedIcon /> &nbsp; Add Item
                  </Button>
                </div>
              </div>
            )}
          </div>
        </div>
        <Row className="pharma-row">
          <Col md={2} align="center">
            <Button
              type="button"
              className="OrderCancel btn btn-primary"
              onClick={cancelOrderServiceShow}
            >
              Cancel
            </Button>
          </Col>
          <Col md={7}>
            {indentList?.length != 0 && (
              <Row className="d-flex status-row">
                <Col md={9} align="right">
                  <p className="total-amount">Order Status</p>
                </Col>
                <Col md={3}>
                  <div>
                    <Form.Group className="order-status">
                      <Form.Select
                        aria-label="Default select example"
                        className="pur-select"
                        name="status"
                        value={status}
                        onChange={handleIndentInput}
                      >
                        <option value="" disabled hidden>
                          Select
                        </option>
                        <option value="DRAFT">DRAFT</option>
                        <option value="CREATED">CREATED</option>
                        <option value="AUTHORISED">AUTHORISED</option>
                        <option value="COMPLETED">COMPLETED</option>
                        <option value="RAISED">RAISED</option>
                      </Form.Select>
                    </Form.Group>
                  </div>
                </Col>
              </Row>
            )}
          </Col>
          <Col md={1}>
            {indentList?.length != 0 && <div className="purchase-line"></div>}
          </Col>
          <Col md={2} align="center">
            {indentId ? (
              <Button
                type="button"
                className="orderSubmit btn btn-primary"
                onClick={(e) => updateIndentOrder(indentId)}
              >
                Update
              </Button>
            ) : (
              <Button
                type="button"
                className="orderSubmit btn btn-primary"
                onClick={addIndentOrder}
              >
                Submit
              </Button>
            )}
          </Col>
        </Row>
      </div>
    </div>
  );
}

export default IndentOrder;
