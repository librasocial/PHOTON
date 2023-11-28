import React, { useRef, useState, useEffect } from "react";
import { Col, Row, Form, Button, Modal, InputGroup } from "react-bootstrap";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import "./NewBatchModal.css";
import moment from "moment";
import { useDispatch, useSelector } from "react-redux";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";

function NewBatchModal(props) {
  const form = useRef(null);

  var showModal = props.addBatchDetails;
  var closeModal = props.addBatchDetailsClose;
  const { SingleOrder } = useSelector((state) => state.data);

  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const disableFutureDt = (current) => {
    return current.isAfter(currentdate);
  };

  const disablePastDt = () => {
    const today = new Date();
    const dd = String(today.getDate() + 1).padStart(2, "0");
    const mm = String(today.getMonth() + 1).padStart(2, "0"); //January is 0!
    const yyyy = today.getFullYear();
    return yyyy + "-" + mm + "-" + dd;
  };

  const [inputList, setInputList] = useState([
    {
      productId: "",
      productName: "",
      uom: "",
      barCode: "",
      batchNumber: "",
      quantity: "",
      mfgDate: "",
      expiryDate: "",
      rate: "",
      mrpRate: "",
      cgstPercentage: "",
      sgstPercentage: "",
      taxAmount: "",
      totalAmount: "",
    },
  ]);

  let prodName;
  let prodUom;
  let prodRate;
  if (props.supStatus == true) {
    SingleOrder.poDetails.poItems.map((item) => {
      if (item.productId == props.productOrderId) {
        inputList.map((list) => {
          list.productId = props.productOrderId;
          list.productName = item.productName;
          list.uom = item.uom;
          list.rate = item.rate;
          list.cgstPercentage = item.cgstPercentage;
          list.sgstPercentage = item.sgstPercentage;
        });
      }
    });
  } else if (props.supStatus == false && props.inwardsId) {
    props.batchDetails.map((item) => {
      if (item.productId == props.productOrderId) {
        inputList.map((list) => {
          list.productId = props.productOrderId;
          list.productName = item.productName;
          list.uom = item.uom;
          list.rate = item.rate;
          list.cgstPercentage = item.cgstPercentage;
          list.sgstPercentage = item.sgstPercentage;
          prodName = item.productName;
          prodUom = item.uom;
          prodRate = item.rate;
        });
      }
    });
  }

  // handle input change
  const handleInputChange = (e, index) => {
    let cgst = 0;
    let sgst = 0;
    if (e.target.name == "quantity") {
      for (var i = 0; i < inputList.length; i++) {
        if (inputList[i].cgstPercentage) {
          cgst =
            e.target.value * inputList[i].rate -
            (e.target.value * inputList[i].rate) /
              (1 + inputList[i].cgstPercentage / 100);
        }
        if (inputList[i].sgstPercentage) {
          sgst =
            e.target.value * inputList[i].rate -
            (e.target.value * inputList[i].rate) /
              (1 + inputList[i].sgstPercentage / 100);
        }
      }
    }
    const { name, value } = e.target;
    const list = [...inputList];
    list[index][name] = value;

    if (e.target.name == "quantity") {
      if (cgst == 0 && sgst == 0) {
        inputList[index].taxAmount = 0;
        inputList[index].totalAmount = e.target.value * inputList[index].rate;
      } else {
        inputList[index].taxAmount = cgst + sgst;
        inputList[index].totalAmount =
          e.target.value * inputList[index].rate + (cgst + sgst);
      }
    }
    setInputList(list);
  };

  const handlePurchaseInput = () => {};

  // handle click event of the Add button
  const handleAddClick = () => {
    setInputList([
      ...inputList,
      {
        productId: "",
        productName: "",
        uom: "",
        barCode: "",
        batchNumber: "",
        quantity: "",
        mfgDate: "",
        expiryDate: "",
        rate: "",
        mrpRate: "",
        cgstPercentage: "",
        sgstPercentage: "",
        taxAmount: "",
        totalAmount: "",
      },
    ]);
  };

  // handle click event of the Update button
  const handleUpdateClick = () => {
    setInputList([
      ...inputList,
      {
        productId: "",
        productName: "",
        uom: "",
        barCode: "",
        batchNumber: "",
        quantity: "",
        mfgDate: "",
        expiryDate: "",
        rate: "",
        mrpRate: "",
        cgstPercentage: "",
        sgstPercentage: "",
        taxAmount: "",
        totalAmount: "",
      },
    ]);
  };

  // handle click event of the Remove button
  const handleRemoveClick = (index) => {
    const list = [...inputList];
    list.splice(index, 1);
    setInputList(list);
  };

  let array = [];
  if (props.batchDetails.length == 0) {
    array = inputList;
  } else {
    array = [...props.batchDetails, ...inputList];
  }

  const [rcQnt, setRcQnt] = useState("");
  useEffect(() => {
    if (props.inwardsId && props.productOrderId) {
      let editProdArray = [];
      let recvNty = 0;
      for (var i = 0; i < props.batchDetails.length; i++) {
        if (props.batchDetails[i].productId == props.productOrderId) {
          editProdArray.push(props.batchDetails[i]);
          recvNty += Number(props.batchDetails[i].quantity);
        }
      }
      setRcQnt(recvNty);
      setInputList(editProdArray);
    }
  }, [props.inwardsId, props.batchDetails, props.productOrderId]);

  const AddInwardsDetails = () => {
    let isFound = inputList.some((element) => {
      if (
        !element.batchNumber ||
        !element.quantity ||
        !element.taxAmount ||
        !element.totalAmount
      ) {
        return true;
      } else {
        return false;
      }
    });
    let qntySum = 0;
    for (var i = 0; i < inputList.length; i++) {
      qntySum += Number(inputList[i].quantity);
    }
    if (isFound == true) {
      Tostify.notifyWarning("please Fill all mandatory fields..!");
    } else {
      if (Number(qntySum) != Number(props.rcvQnty)) {
        Tostify.notifyFail(
          "Quantity sholud not be greater than or smaller than recieved quantity"
        );
      } else {
        props.addBatchDetailsClose(true);
        props.setBatchDetails(array);
        props.setProductOrderId("");
        setInputList([
          {
            productId: "",
            productName: "",
            uom: "",
            barCode: "",
            batchNumber: "",
            quantity: "",
            mfgDate: "",
            expiryDate: "",
            rate: "",
            mrpRate: "",
            cgstPercentage: "",
            sgstPercentage: "",
            taxAmount: "",
            totalAmount: "",
          },
        ]);
      }
    }
  };

  const UpdateInwardData = (id) => {
    let qntySum = 0;
    for (var i = 0; i < inputList.length; i++) {
      qntySum += Number(inputList[i].quantity);
      if (Number(qntySum) != Number(rcQnt)) {
        Tostify.notifyFail(
          "Quantity sholud not be greater than or smaller than recieved quantity"
        );
      } else {
        const newProductList = props.batchDetails.filter(
          (item) => item.productId !== id
        );
        let updateBatch = [...newProductList, ...inputList];
        props.addBatchDetailsClose(true);
        props.setBatchDetails(updateBatch);
        props.setProductOrderId("");
        setInputList([
          {
            productId: "",
            productName: "",
            uom: "",
            barCode: "",
            batchNumber: "",
            quantity: "",
            mfgDate: "",
            expiryDate: "",
            rate: "",
            mrpRate: "",
            cgstPercentage: "",
            sgstPercentage: "",
            taxAmount: "",
            totalAmount: "",
          },
        ]);
      }
    }
  };

  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling

  return (
    <React.Fragment>
      <Modal
        show={showModal}
        onHide={closeModal}
        className="batch-display-modal-div"
      >
        <ToastContainer />
        <div className="addPurchase-modal">
          <div>
            <div className="add-service-form-col">
              <Row>
                <Col md={6} xs={9}>
                  <h1 className="dia-heading new-mdl-header">
                    Add Batch Details
                  </h1>
                </Col>
                <Col md={6} xs={3} align="right">
                  <button
                    onClick={closeModal}
                    className="bi bi-x close-popup"
                  ></button>
                </Col>
              </Row>
              <div>
                <h1 className="dia-heading new-drug-header">Drugs Details</h1>
              </div>
              {props.supStatus == true && !props.inwardsI ? (
                <>
                  {SingleOrder.poDetails.poItems.map((prod, proIndex) => (
                    <Row className="batch-order-div" key={proIndex}>
                      {prod.productId == props.productOrderId && (
                        <>
                          <Col md={3}>
                            <div>
                              <Form.Group>
                                <Form.Label>Drug Name</Form.Label>
                                <Form.Control
                                  type="text"
                                  placeholder="Enter Drug Name"
                                  name="drugName"
                                  value={prod.productName}
                                  onChange={handlePurchaseInput}
                                />
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={3}>
                            <div>
                              <Form.Group>
                                <Form.Label>PO Type</Form.Label>
                                <Form.Select
                                  aria-label="Default select example"
                                  name="poType"
                                  disabled={true}
                                  value={prod.uom}
                                  onChange={handlePurchaseInput}
                                >
                                  <option value="" disabled hidden>
                                    Select Classification
                                  </option>
                                  <option value={prod.uom}>{prod.uom}</option>
                                </Form.Select>
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={2}>
                            <div>
                              <Form.Group>
                                <Form.Label>Received Quantity</Form.Label>
                                <Form.Control
                                  type="text"
                                  placeholder="Enter Received Quantity"
                                  name="drugQty"
                                  readOnly={true}
                                  value={props.rcvQnty}
                                  onChange={handlePurchaseInput}
                                />
                                <Form.Label className="batch-info-footer">
                                  E.g.,250.00
                                </Form.Label>
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={2}>
                            <div className="div-space">
                              <Form.Group>
                                <Form.Label>PO Rate (&#8377;)</Form.Label>
                                <InputGroup className="batch-iput">
                                  <InputGroup.Text id="batch-po">
                                    &#8377;
                                  </InputGroup.Text>
                                  <Form.Control
                                    placeholder="Enter PO Rate"
                                    readOnly={true}
                                    value={prod.rate || ""}
                                    name="rate"
                                    onChange={handlePurchaseInput}
                                    aria-label="PO Rate"
                                    aria-describedby="basic-addon1"
                                  />
                                </InputGroup>
                                <Form.Label className="batch-info-footer">
                                  E.g.,150.00
                                </Form.Label>
                              </Form.Group>
                            </div>
                          </Col>
                        </>
                      )}
                    </Row>
                  ))}
                </>
              ) : (
                <Row className="batch-order-div">
                  <>
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>Drug Name</Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter Drug Name"
                            name="drugName"
                            value={prodName || ""}
                            onChange={handlePurchaseInput}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>PO Type</Form.Label>
                          <Form.Control
                            aria-label="Default select example"
                            name="poType"
                            disabled={true}
                            value={prodUom || ""}
                            onChange={handlePurchaseInput}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={2}>
                      <div>
                        <Form.Group>
                          <Form.Label>Received Quantity</Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter Received Quantity"
                            name="drugQty"
                            readOnly={true}
                            value={rcQnt || ""}
                            onChange={handlePurchaseInput}
                          />
                          <Form.Label className="batch-info-footer">
                            E.g.,250.00
                          </Form.Label>
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={2}>
                      <div className="div-space">
                        <Form.Group>
                          <Form.Label>PO Rate (&#8377;)</Form.Label>
                          <InputGroup className="batch-iput">
                            <InputGroup.Text id="batch-po">
                              &#8377;
                            </InputGroup.Text>
                            <Form.Control
                              placeholder="Enter PO Rate"
                              readOnly={true}
                              value={prodRate || ""}
                              name="rate"
                              onChange={handlePurchaseInput}
                              aria-label="PO Rate"
                              aria-describedby="basic-addon1"
                            />
                          </InputGroup>
                          <Form.Label className="batch-info-footer">
                            E.g.,150.00
                          </Form.Label>
                        </Form.Group>
                      </div>
                    </Col>
                  </>
                </Row>
              )}
              <div>
                <h1 className="dia-heading new-mdl-header">Batch Details</h1>
              </div>
            </div>
            <Form ref={form} className="batch-table-div">
              <Paper
                sx={{ width: "100%", overflow: "hidden" }}
                className="direct-inwards-batch"
              >
                <TableContainer sx={{ maxHeight: 440 }}>
                  <Table stickyHeader aria-label="sticky table">
                    <TableHead>
                      <TableRow>
                        <TableCell>Barcode</TableCell>
                        <TableCell>Batch No.</TableCell>
                        <TableCell>Quantity</TableCell>
                        <TableCell>MFG Date</TableCell>
                        <TableCell>EXP Date</TableCell>
                        <TableCell>PO Rate (&#8377;)</TableCell>
                        <TableCell>MRP Rate (&#8377;)</TableCell>
                        <TableCell>CGST (%)</TableCell>
                        <TableCell>SGST (%)</TableCell>
                        <TableCell>Tax Amount (&#8377;)</TableCell>
                        <TableCell>Total Amount (&#8377;)</TableCell>
                        <TableCell colSpan={2} align="center">
                          Action
                        </TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {inputList.map((x, i) => {
                        return (
                          <TableRow key={i}>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="text"
                                  placeholder="Enter Barcode"
                                  value={x.barCode || ""}
                                  name="barCode"
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="text"
                                  placeholder="Enter Batch No."
                                  value={x.batchNumber || ""}
                                  name="batchNumber"
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="number"
                                  placeholder="Enter Quantity"
                                  value={x.quantity || ""}
                                  name="quantity"
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="date"
                                  value={x.mfgDate || ""}
                                  name="mfgDate"
                                  max={currentdate}
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="date"
                                  min={disablePastDt()}
                                  value={x.expiryDate || ""}
                                  name="expiryDate"
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="number"
                                  placeholder="Enter PO Rate"
                                  value={x.rate || ""}
                                  name="rate"
                                  disabled
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="number"
                                  placeholder="Enter MRP Rate"
                                  value={x.mrpRate || ""}
                                  name="mrpRate"
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="number"
                                  placeholder="Enter CGST"
                                  value={x.cgstPercentage || ""}
                                  name="cgstPercentage"
                                  disabled
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="number"
                                  placeholder="Enter SGST"
                                  value={x.sgstPercentage || ""}
                                  name="sgstPercentage"
                                  disabled
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type="number"
                                  placeholder="Enter Tax Amount"
                                  value={x.taxAmount || ""}
                                  name="taxAmount"
                                  disabled
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input"
                                  type=""
                                  placeholder="Enter Total Amount"
                                  value={x.totalAmount || ""}
                                  name="totalAmount"
                                  disabled
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <p className="batchAdd btn btn-link">
                                <i
                                  className="bi bi-pencil-square"
                                  aria-hidden="true"
                                ></i>
                              </p>
                            </TableCell>
                            <TableCell>
                              <p
                                className="batchDel btn btn-link"
                                onClick={(e) => handleRemoveClick(i)}
                              >
                                <i className="bi bi-trash"></i>
                              </p>
                            </TableCell>
                          </TableRow>
                        );
                      })}
                    </TableBody>
                  </Table>
                  <Table>
                    <TableBody>
                      <TableRow>
                        <TableCell>
                          <Button
                            onClick={handleAddClick}
                            className="btn btn-primary add-drug-row"
                          >
                            <i
                              className="fa fa-plus-square"
                              aria-hidden="true"
                            ></i>
                            &nbsp;Add Row
                          </Button>
                        </TableCell>
                      </TableRow>
                    </TableBody>
                  </Table>
                  <Table>
                    <TableBody>
                      <TableRow>
                        <TableCell className="cancel-new-batch">
                          <Button
                            type="button"
                            className="cancelBatch btn btn-primary"
                            onClick={closeModal}
                          >
                            Cancel
                          </Button>
                        </TableCell>
                        <TableCell className="add-new-batch">
                          {props.inwardsId ? (
                            <Button
                              variant="outline-secondary"
                              className="item-add"
                              onClick={(e) => {
                                UpdateInwardData(props.productOrderId);
                              }}
                            >
                              Update
                            </Button>
                          ) : (
                            <Button
                              type="button"
                              className="addBatch btn btn-primary"
                              onClick={AddInwardsDetails}
                            >
                              Submit
                            </Button>
                          )}
                        </TableCell>
                      </TableRow>
                    </TableBody>
                  </Table>
                </TableContainer>
              </Paper>
            </Form>
          </div>
        </div>
      </Modal>
    </React.Fragment>
  );
}

export default NewBatchModal;
