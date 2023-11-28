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
import "../../../css/Services.css";
import "../PharmacyTab.css";
import moment from "moment";
import NewPurchaseOrder from "./NewPurchaseOrder";
import { useDispatch, useSelector } from "react-redux";
import {
  loadAddPurchaseOrder,
  loadSingleOrder,
  loadUpdatePurchaseOrder,
  loadOrdersList,
} from "../../../redux/actions";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import CancelOrder from "./CancelOrder";
import Loader from "../Dashboard/Loader";
import { useHistory } from "react-router-dom";

function PurchaseOrder(props) {
  let poUser = sessionStorage.getItem("poUser");
  let orderId;
  if (poUser === "medical-Officer") {
    orderId = sessionStorage.getItem("poID");
  } else {
    orderId = props.orderId;
  }
  let history = useHistory();
  let dispatch = useDispatch();
  const { SingleOrder } = useSelector((state) => state.data);

  useEffect(() => {
    {
      typeofuser === "Pharmacist"
        ? "Pharmacy Purchase Order"
        : "EMR Medical Officer Approvals";
    }
    dispatch(loadSingleOrder(orderId));
  }, [orderId]);

  const [listState, setListState] = useState(false);
  const [productList, setProductList] = useState([]);

  useEffect(() => {
    if (SingleOrder && orderId) {
      setPurchaseOrder({ ...SingleOrder });
      if (listState == false) {
        setPurchOrdDetails({ ...SingleOrder?.poDetails });
        setProductList(SingleOrder?.poDetails?.poItems);
      } else {
        setProductList(productList);
      }
      // setHide(true)
    }
  }, [SingleOrder, orderId]);

  const onClickChange = () => {
    props.setPageChange(false);
    props.setOrderId("");
    dispatch(loadOrdersList(props.page, props.rowsPerPage));
  };

  const onClickApproval = () => {
    props.setPageChange(false);
    sessionStorage.removeItem("poUser");
    sessionStorage.removeItem("poID");
    sessionStorage.removeItem("InUser");
    sessionStorage.removeItem("inID");
    window.history.back();
    // history.push("/approvals");
    dispatch(loadOrdersList(props.page, props.rowsPerPage));
  };

  const handleRemoveClick = (index) => {
    const list = [...productList];
    list.splice(index, 1);
    setProductList(list);
  };

  let date = moment(new Date()).format("YYYY-MM-DD HH:mm:ss");
  let today = moment(new Date(date)).format("YYYY-MM-DDTHH:mm:ss.SSS");
  const [purchOrdDetails, setPurchOrdDetails] = useState({
    poItems: productList,
    netAmount: netTotalAmnt,
    gst: totalGst,
    grandTotal: totalAmount,
  });

  const [purchaseOrder, setPurchaseOrder] = useState({
    supplierName: "",
    type: "",
    remarks: "",
    poDetails: purchOrdDetails,
    status: "",
    poDate: today,
  });

  useEffect(() => {
    if (purchOrdDetails?.length != 0) {
      purchaseOrder.poDetails = purchOrdDetails;
    }
  }, [purchOrdDetails]);

  const { supplierName, type, remarks, poDetails, status, poDate } =
    purchaseOrder;
  const { poItems, netAmount, gst, grandTotal } = purchOrdDetails;

  const [cgstSum, setCgstSum] = useState();
  const [sgstSum, setSgstSum] = useState();
  const [totalAmount, setTotalAmount] = useState();
  const [totalGst, setTotalGst] = useState();
  const [netTotalAmnt, setNetTotalAmnt] = useState();

  useEffect(() => {
    if (productList?.length != 0) {
      let cgstsum = 0;
      let sgstsum = 0;
      let totalAmount1 = 0;
      let totalGst1 = 0;
      let netTotalAmnt1 = 0;
      for (let i = 0; i < productList?.length; i++) {
        cgstsum += productList[i].cgstAmount;
        sgstsum = sgstsum + productList[i].sgstAmount;
        totalAmount1 += productList[i].totalAmount;
      }

      totalGst1 = cgstsum + sgstsum;
      netTotalAmnt1 = totalAmount1 - totalGst1;
      setPurchOrdDetails({
        poItems: productList,
        netAmount: totalAmount1,
        gst: totalGst1,
        grandTotal: netTotalAmnt1,
      });
      setCgstSum(cgstsum);
      setSgstSum(sgstsum);
      setTotalAmount(netTotalAmnt1);
      setTotalGst(totalGst1);
      setNetTotalAmnt(totalAmount1);
    }
  }, [productList]);

  const handlePurchaseInput = (e) => {
    const { name, value } = e.target;
    setPurchaseOrder({ ...purchaseOrder, [name]: value });
  };

  const [addPurchaseService, setAddPurchaseService] = useState(false);
  const addPurchaseServiceClose = () => setAddPurchaseService(false);
  const addPurchaseServiceShow = () => setAddPurchaseService(true);

  const renderTooltip = (props) => (
    <Tooltip id="button-tooltip" {...props}>
      <span>Amount</span>&nbsp;:{" "}
      <b>&#8377; {parseFloat(totalAmount).toFixed(2)}</b> <br />
      <span>GST</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:
      &nbsp;&nbsp;&nbsp;<b>&#8377; {parseFloat(totalGst).toFixed(2)}</b>
    </Tooltip>
  );

  const [purchaseLoading, setPurchaseLoading] = useState(false);
  const addPurchaseOrder = () => {
    if (!supplierName || !type || !status || productList?.length == 0) {
      alert("Please fill required fields/add atleast one product");
    } else {
      var postResponse = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(purchaseOrder),
      };
      dispatch(loadAddPurchaseOrder(postResponse, setPurchaseLoading));
    }
  };
  const updatePurchaseOrder = (orderId) => {
    if (!supplierName || !type || !status || productList?.length == 0) {
      alert("Please fill required fields/add atleast one product");
    } else {
      var updateOrder = {
        type: "PO",
        properties: purchaseOrder,
      };
      var updateResponse = {
        headers: serviceHeaders.myHeaders1,
        method: "PATCH",
        mode: "cors",
        body: JSON.stringify(updateOrder),
      };
      dispatch(
        loadUpdatePurchaseOrder(orderId, updateResponse, setPurchaseLoading)
      );
    }
  };

  // edit added product item
  const [editProduct, setEditProduct] = useState({});
  const [editProductID, setEditProductID] = useState("");

  const editProductItem = (e) => {
    let productId = e;
    setEditProductID(e);
    for (var i = 0; i < productList?.length; i++) {
      if (productList[i].productId == productId) {
        setEditProduct(productList[i]);
      }
    }
    addPurchaseServiceShow(true);
  };
  // edit added product item

  // modal for cancel button
  const [cancelOrderService, setCancelOrderService] = useState(false);
  const [purchaseStatus, setPurchaseStatus] = useState(false);
  const cancelOrderServiceClose = () => setCancelOrderService(false);
  const cancelOrderServiceShow = () => {
    setCancelOrderService(true);
    setPurchaseStatus(true);
  };
  // modal for cancel button

  function closePurchaseLoader() {
    props.setPage(0);
    props.setRowsPerPage(5);
    dispatch(loadOrdersList(props.page, props.rowsPerPage));
    props.setPageChange(false);
  }

  let typeofuser = sessionStorage.getItem("typeofuser");

  return (
    <div>
      <Loader
        orderId={orderId}
        closePurchaseLoader={closePurchaseLoader}
        purchaseLoading={purchaseLoading}
        status={status}
      />
      <NewPurchaseOrder
        addPurchaseService={addPurchaseService}
        addPurchaseServiceClose={addPurchaseServiceClose}
        setProductList={setProductList}
        productList={productList}
        editProduct={editProduct}
        editProductID={editProductID}
        setEditProductID={setEditProductID}
        setListState={setListState}
      />
      <CancelOrder
        cancelOrderService={cancelOrderService}
        cancelOrderServiceClose={cancelOrderServiceClose}
        setPurchaseStatus={setPurchaseStatus}
        purchaseStatus={purchaseStatus}
        setPageChange={props.setPageChange}
      />
      <div className="regHeader">
        <h1 className="register-Header">Purchase Order</h1>
      </div>
      <hr style={{ margin: "0px" }} />
      <div className="porder-tab">
        <div className="pharma-form">
          <Breadcrumb>
            <Breadcrumb.Item
              className="pur-order-breadcrumb"
              onClick={
                typeofuser === "Pharmacist" ? onClickChange : onClickApproval
              }
            >
              {typeofuser === "Pharmacist" ? "Orders" : "Approvals"}
            </Breadcrumb.Item>
            <Breadcrumb.Item active>
              {typeofuser === "Pharmacist" ? (
                <>{orderId ? "Edit Purchase Order" : "New Purchase Order"}</>
              ) : (
                "New Purchase Order"
              )}
            </Breadcrumb.Item>
          </Breadcrumb>
          <div className="pro-tab purchase-order">
            <div>
              <div className="porder-border">
                <h1 className="porder-head">
                  {typeofuser === "Pharmacist" ? (
                    <>
                      {orderId ? "Edit Purchase Order" : "New Purchase Order"}
                    </>
                  ) : (
                    "Purchase Order"
                  )}
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
                          onChange={handlePurchaseInput}
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
                        <Form.Label>PO Type</Form.Label>
                        <Form.Select
                          aria-label="Default select example"
                          name="type"
                          value={type || ""}
                          onChange={handlePurchaseInput}
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
                          onChange={handlePurchaseInput}
                        />
                      </Form.Group>
                    </div>
                  </Col>
                </Row>
              </div>
            </div>
          </div>
          <div className="pro-tab add-purchase-order">
            {productList?.length == 0 ? (
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
                  onClick={addPurchaseServiceShow}
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
                          <TableCell align="left">PO Qty</TableCell>
                          <TableCell align="left">PO Rate</TableCell>
                          <TableCell align="left">CGST %</TableCell>
                          <TableCell align="left">CGST AMT</TableCell>
                          <TableCell align="left">SGST %</TableCell>
                          <TableCell align="left">SGST AMT</TableCell>
                          <TableCell align="left">Total Amount</TableCell>
                          <TableCell align="center" colSpan={2}>
                            Action
                          </TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {productList?.map((addProd, prodIndex) => (
                          <TableRow key={prodIndex}>
                            <TableCell align="left">
                              {addProd.productName}
                            </TableCell>
                            <TableCell align="left">{addProd.uom}</TableCell>
                            <TableCell align="left">
                              {addProd.quantity}
                            </TableCell>
                            <TableCell align="left">{addProd.rate}</TableCell>
                            <TableCell align="left">
                              {parseFloat(addProd.cgstPercentage).toFixed(2)}
                            </TableCell>
                            <TableCell align="left">
                              {parseFloat(addProd.cgstAmount).toFixed(2)}
                            </TableCell>
                            <TableCell align="left">
                              {parseFloat(addProd.sgstPercentage).toFixed(2)}
                            </TableCell>
                            <TableCell align="left">
                              {parseFloat(addProd.sgstAmount).toFixed(2)}
                            </TableCell>
                            <TableCell align="left">
                              <b>
                                {parseFloat(addProd.totalAmount).toFixed(2)}
                              </b>
                            </TableCell>
                            <TableCell align="center" colSpan={2}>
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
                    <Table>
                      <TableBody>
                        <TableRow>
                          <TableCell className="billammount">
                            <p className="billammount-head">
                              Amount&nbsp;&nbsp;:&nbsp;
                            </p>
                            <p className="billammount-head">
                              GST&nbsp;&nbsp;:&nbsp;
                            </p>
                          </TableCell>
                          <TableCell className="bill-ammount">
                            <p className="bill-ammount-head">
                              &#8377; {parseFloat(totalAmount).toFixed(2)}
                            </p>
                            <p className="bill-ammount-head">
                              &#8377;&nbsp; {parseFloat(totalGst).toFixed(2)}
                            </p>
                          </TableCell>
                        </TableRow>
                        <TableRow className="total-bill">
                          <TableCell className="billammount">
                            <p className="billammount-head">
                              Total Amount&nbsp;&nbsp;:&nbsp;
                            </p>
                          </TableCell>
                          <TableCell className="bill-ammount">
                            <p className="bill-ammount-head">
                              &#8377; {parseFloat(netTotalAmnt).toFixed(2)}
                            </p>
                          </TableCell>
                        </TableRow>
                      </TableBody>
                    </Table>
                    <Table>
                      <TableBody>
                        <TableRow>
                          <TableCell className="total-btn" align="center">
                            <Button
                              type="button"
                              className="pharmaBtnPC"
                              onClick={addPurchaseServiceShow}
                            >
                              <AddCircleRoundedIcon /> &nbsp; Add Item
                            </Button>
                          </TableCell>
                        </TableRow>
                      </TableBody>
                    </Table>
                  </TableContainer>
                </Paper>
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
            {productList?.length != 0 && (
              <Row className="d-flex status-row">
                <Col md={6} align="right">
                  <p className="total-amount">
                    <span className="billammount-head">
                      <b>Total Amount </b>
                    </span>
                    : &nbsp;&#8377;<b>{parseFloat(netTotalAmnt).toFixed(2)} </b>
                    &nbsp;&nbsp;
                    <OverlayTrigger
                      placement="top"
                      delay={{ show: 350, hide: 400 }}
                      overlay={renderTooltip}
                    >
                      <i
                        className="fa fa-info-circle purchase-info"
                        aria-hidden="true"
                      ></i>
                    </OverlayTrigger>
                  </p>
                </Col>
                <Col md={1}>
                  <div className="purch-line"></div>
                </Col>
                <Col md={2} align="right">
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
                        onChange={handlePurchaseInput}
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
            {productList?.length != 0 && <div className="purchase-line"></div>}
          </Col>
          <Col md={2} align="center">
            {orderId ? (
              <Button
                type="button"
                className="orderSubmit btn btn-primary"
                onClick={(e) => updatePurchaseOrder(orderId)}
              >
                Update
              </Button>
            ) : (
              <Button
                type="button"
                className="orderSubmit btn btn-primary"
                onClick={addPurchaseOrder}
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

export default PurchaseOrder;
