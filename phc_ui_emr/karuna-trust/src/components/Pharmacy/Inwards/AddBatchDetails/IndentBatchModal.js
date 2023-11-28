import React, { useState, useRef, useEffect } from "react";
import {
  Paper,
  Table,
  TableContainer,
  TableHead,
  TableRow,
  TableBody,
  TableCell,
} from "@mui/material";
import { Col, Row, Form, Modal, Button } from "react-bootstrap";
import moment from "moment";
import { useDispatch, useSelector } from "react-redux";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";

function IndentBatchModal(props) {
  var modalClose = props.indentBatchModalClose;
  var modalShow = props.indentBatchModal;
  const { SingleOrder } = useSelector((state) => state.data);

  const form = useRef(null);

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
    },
  ]);

  let prodName;
  let prodUom;
  let prodRate;
  if (props.supStatus == true) {
    SingleOrder.indentItems.map((item) => {
      if (item.productId == props.productOrderId) {
        inputList.map((list) => {
          list.productId = props.productOrderId;
          list.productName = item.productName;
          list.uom = item.uom;
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
          prodName = item.productName;
          prodUom = item.uom;
          prodRate = item.rate;
        });
      }
    });
  }

  // handle input change
  const handleInputChange = (e, index) => {
    const { name, value } = e.target;
    const list = [...inputList];
    list[index][name] = value;
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
          recvNty += props.batchDetails[i].quantity;
        }
      }
      setRcQnt(recvNty);
      setInputList(editProdArray);
    }
  }, [props.inwardsId, props.batchDetails, props.productOrderId]);

  const AddInwardsDetails = () => {
    let isFound = inputList.some((element) => {
      if (!element.batchNumber || !element.quantity) {
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
        props.indentBatchModalClose(true);
        props.setBatchDetails(array);

        // props.setBatchDetails(inputList)
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

        props.indentBatchModalClose(true);
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
          },
        ]);
      }
    }
  };

  const closeModalIndent = () => {
    props.setProductOrderId("");
    props.indentBatchModalClose(true);
  };

  return (
    <Modal
      show={modalShow}
      onHide={closeModalIndent}
      className="indent-batch-modal"
    >
      <ToastContainer />
      <div className="indentPurchase-modal">
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
                  onClick={closeModalIndent}
                  className="bi bi-x close-popup"
                ></button>
              </Col>
            </Row>
            <div>
              <h1 className="dia-heading new-drug-header">Drugs Details</h1>
            </div>
            {props.supStatus == true && !props.inwardsI ? (
              <>
                {SingleOrder.indentItems.map((prod, proIndex) => (
                  <Row className="batch-order-div" key={proIndex}>
                    {prod.productId == props.productOrderId && (
                      <>
                        <Col md={4}>
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
                        <Col md={4}>
                          <div>
                            <Form.Group>
                              <Form.Label>PO Type</Form.Label>
                              <Form.Select
                                aria-label="Default select example"
                                name="poType"
                                value={prod.uom}
                                onChange={handlePurchaseInput}
                                disabled
                              >
                                <option value="" disabled hidden>
                                  Select Classification
                                </option>
                                <option value={prod.uom}>{prod.uom}</option>
                              </Form.Select>
                            </Form.Group>
                          </div>
                        </Col>
                        <Col md={4}>
                          <div>
                            <Form.Group>
                              <Form.Label>Received Quantity</Form.Label>
                              <Form.Control
                                type="text"
                                placeholder="Enter Received Quantity"
                                name="drugQty"
                                value={props.rcvQnty}
                                onChange={handlePurchaseInput}
                                readOnly={true}
                              />
                              <Form.Label className="batch-info-footer">
                                E.g.,250.00
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
                  <Col md={4}>
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
                  <Col md={4}>
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
                  <Col md={4}>
                    <div>
                      <Form.Group>
                        <Form.Label>Received Quantity</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter Received Quantity"
                          name="drugQty"
                          value={rcQnt || ""}
                          onChange={handlePurchaseInput}
                          readOnly={true}
                        />
                        <Form.Label className="batch-info-footer">
                          E.g.,250.00
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
              className="batch-supply-batch"
            >
              <TableContainer sx={{ maxHeight: 440 }}>
                <Table stickyHeader aria-label="sticky table">
                  <TableHead>
                    <TableRow>
                      <TableCell>Barcode</TableCell>
                      <TableCell>Batch No.</TableCell>
                      <TableCell>EXP Date</TableCell>
                      <TableCell>Quantity</TableCell>
                      <TableCell>MFG Date</TableCell>
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
                          onClick={closeModalIndent}
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
  );
}

export default IndentBatchModal;
