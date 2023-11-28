import React, { useEffect, useState } from "react";
import { Col, Row, Form, Button, Modal } from "react-bootstrap";
import {
  Divider,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import "./AddViewSupplierDetails.css";
import { useDispatch, useSelector } from "react-redux";
import { loadByNTDate, loadByNameType } from "../../../../redux/actions";
import moment from "moment";

function AddViewSupplierDetails(props) {
  let dispatch = useDispatch();
  const { PurchaseOrderList } = useSelector((state) => state.data);

  const [pendingOrder, setPendingOrder] = useState([]);
  useEffect(() => {
    let pendOrder = [];
    for (var i = 0; i < PurchaseOrderList.length; i++) {
      if (PurchaseOrderList[i].status != "DRAFT") {
        pendOrder.push(PurchaseOrderList[i]);
      }
    }
    setPendingOrder(pendOrder);
  }, [PurchaseOrderList]);

  function resetViewDetails() {
    setSearchDate({ stDate: "", toDate: "" });
    dispatch(loadByNameType(props.supplierName, props.type, 0, 100));
  }

  const [searchDate, setSearchDate] = useState({
    stDate: "",
    toDate: "",
  });

  const { stDate, toDate } = searchDate;
  function searchOrders() {
    dispatch(
      loadByNTDate(stDate, toDate, props.supplierName, props.type, 0, 100)
    );
  }
  const handleChangeDate = (e) => {
    const { name, value } = e.target;
    setSearchDate({ ...searchDate, [name]: value });
  };
  const [checkedItems, setCheckedItems] = useState("");

  const checKBoxHandler = (event) => {
    if (event.target.checked) {
      setCheckedItems(event.target.value);
    } else {
      setCheckedItems("");
    }
  };

  function addedSupplyDetails() {
    props.setSelectedItems(checkedItems);
    // dispatch(loadSingleOrder(checkedItems))
    props.addSupplierDetailsClose(true);
    // props.setOrderStatus(true)
    // props.setSupStatus(true)
  }

  return (
    <Modal
      show={props.addSupplierDetails}
      onHide={props.addSupplierDetailsClose}
      className="check-In-modal-div"
    >
      <div className="add-service-form-col">
        <Row>
          <Col md={6} xs={9}>
            <h1 className="add-view-header">Purchase Orders</h1>
          </Col>
          <Col md={6} xs={3} align="right">
            <button
              onClick={props.addSupplierDetailsClose}
              className="bi bi-x close-popup"
            ></button>
          </Col>
        </Row>
        <div className="dem-row">
          <h1 className="demand-supply-header">Suppliers</h1>
          <p className="supply-demand-body">
            District/Taluk Health Office,
            <br /> Bangalore, Karnataka
          </p>
        </div>
        <Row>
          <Col md={1}></Col>
          <Col md={10}>
            <Row className="sup-dem-row">
              <Col md={3}>
                <div>
                  <Form.Group>
                    <Form.Control
                      type="date"
                      value={stDate || ""}
                      name="stDate"
                      onChange={handleChangeDate}
                    />
                  </Form.Group>
                </div>
              </Col>
              <Col md={3}>
                <div>
                  <Form.Group>
                    <Form.Control
                      type="date"
                      value={toDate || ""}
                      name="toDate"
                      onChange={handleChangeDate}
                    />
                  </Form.Group>
                </div>
              </Col>
              <Col md={6}>
                <Row>
                  <Col md={6}>
                    <div>
                      <Button
                        type="button"
                        className="orderSupply btn btn-primary"
                        onClick={searchOrders}
                      >
                        Search
                      </Button>
                    </div>
                  </Col>
                  <Col md={6}>
                    <div>
                      <Button
                        type="button"
                        className="resetSupply btn btn-secondary"
                        onClick={resetViewDetails}
                      >
                        Clear
                      </Button>
                    </div>
                  </Col>
                </Row>
              </Col>
            </Row>
          </Col>
          <Col md={1}></Col>
        </Row>
        <Paper
          sx={{ width: "100%", overflow: "hidden", margin: "2% 0 0 0" }}
          className=""
        >
          <TableContainer sx={{ maxHeight: 440 }}>
            <Table stickyHeader aria-label="sticky table">
              <TableHead>
                <TableRow>
                  <TableCell></TableCell>
                  <TableCell align="left">PO Number</TableCell>
                  <TableCell align="center">PO Date</TableCell>
                  <TableCell align="center">PO Status</TableCell>
                </TableRow>
              </TableHead>
              {pendingOrder.length == 0 ? (
                <TableBody>
                  <TableRow align="center">
                    <TableCell>No data found...!</TableCell>
                  </TableRow>
                </TableBody>
              ) : (
                <TableBody>
                  {pendingOrder.map((pendOrd, pIndex) => (
                    <TableRow key={pIndex}>
                      <TableCell align="left">
                        <input
                          type="checkbox"
                          id={pIndex + 1}
                          className="order-supply-checkbox"
                          value={pendOrd.id}
                          checked={checkedItems == pendOrd.id}
                          onChange={checKBoxHandler}
                        />
                      </TableCell>
                      <TableCell align="left">
                        <p className="po-supp-number">{pendOrd.id}</p>
                      </TableCell>
                      <TableCell align="center">
                        <p className="supply-order-date">
                          {moment(new Date(pendOrd.poDate)).format(
                            "DD-MMM-YYYY"
                          )}
                        </p>
                      </TableCell>
                      <TableCell align="center">
                        <label className="supply-badge">{pendOrd.status}</label>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              )}
            </Table>
          </TableContainer>
        </Paper>
        <Row className="supplier-row">
          <Col md={6} align="right">
            <div className="pr-btn-sec">
              <Button
                type="button"
                className="vitalBtnPC btn btn-outline-secondary"
                onClick={props.addSupplierDetailsClose}
              >
                Cancel
              </Button>
            </div>
          </Col>
          <Col md={6}>
            <div className="pr-btn-sec">
              <Button
                type="button"
                className="vitalBtnN btn btn-secondary"
                disabled={!checkedItems}
                onClick={addedSupplyDetails}
              >
                Add
              </Button>
            </div>
          </Col>
        </Row>
      </div>
    </Modal>
  );
}

export default AddViewSupplierDetails;
