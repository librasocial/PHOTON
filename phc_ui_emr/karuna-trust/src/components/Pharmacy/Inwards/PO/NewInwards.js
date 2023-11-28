import React, { useState, useRef, useEffect } from "react";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { Row, Col, Form, Button, Modal } from "react-bootstrap";
import moment from "moment";
import "./NewInwards.css";
import "../AddBatchDetails/BatchModal.css";
import "./PO.css";
import { useDispatch, useSelector } from "react-redux";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import {
  loadPharmaList,
  loadSingleProduct,
  loadSingleOrder,
} from "../../../../redux/actions";
import TextField from "@mui/material/TextField";
import Autocomplete, { createFilterOptions } from "@mui/material/Autocomplete";
import FormTableHeader from "../../pharmacyTable/FormTableHeader";
import { DirectHeader1 } from "../../../ConstUrl/OptionsData";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  popper: {
    // Your custom styles for the dropdown window
    zIndex: 10110, // Adjust the z-index value as needed
    position: "absolute",
  },
  inputRoot: {
    width: "100% !important",
    height: "30px", // Adjust the height as needed
    padding: "5px", // Adjust the padding as needed
    fontSize: "12px",
  },
  "&.Mui-focused .inputRoot": {
    width: "100%",
    height: "30px", // Adjust the height as needed
    padding: "5px", // Adjust the padding as needed
    fontSize: "12px",
  },
}));

function NewInwards(props) {
  const classes = useStyles();

  var inwardsClose = props.newInwardsClose;
  var inwardsShow = props.newInwards;

  const form = useRef(null);
  let dispatch = useDispatch();
  const filter = createFilterOptions();
  const { PharmaProdList } = useSelector((state) => state.data);
  const { SinglePharmacy } = useSelector((state) => state.data);
  const maxDateExpire = moment(
    new Date(new Date().setFullYear(new Date().getFullYear() + 100))
  ).format("YYYY-MM-DD");

  let drugListArray = [];
  for (var i = 0; i < PharmaProdList.length; i++) {
    if (PharmaProdList[i].status == "ACTIVE") {
      drugListArray.push(PharmaProdList[i]);
    }
  }
  const topOrder = [
    ...new Map(
      PharmaProdList.map((item) => [JSON.stringify(item.name), item])
    ).values(),
  ];
  useEffect(() => {
    dispatch(loadPharmaList(0, 1000000));
  }, []);

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

  const [prodId, setProdId] = useState("");
  const [prodName, setProdName] = useState("");

  const [primUom, setPrimUom] = useState([]);

  let getPharma;
  if (prodId) {
    getPharma = SinglePharmacy;
  }

  if (props.directInwards != null) {
    props.directInwards.map((item) => {
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
  }

  const [rcvQnty, setRcvQnty] = useState("");

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

  useEffect(() => {
    let pendArray = [];
    let rcvQnt = 0;
    if (props.directInwards != null && props.directProdId) {
      for (var i = 0; i < props.directInwards.length; i++) {
        pendArray.push({
          productId: props.directInwards[i].productId,
          productName: props.directInwards[i].productName,
          uom: props.directInwards[i].uom,
          barCode: props.directInwards[i].barCode,
          batchNumber: props.directInwards[i].batchNumber,
          quantity: props.directInwards[i].quantity,
          mfgDate: props.directInwards[i].mfgDate,
          expiryDate: props.directInwards[i].expiryDate,
          rate: props.directInwards[i].rate,
          mrpRate: props.directInwards[i].mrpRate,
          cgstPercentage: props.directInwards[i].cgstPercentage,
          sgstPercentage: props.directInwards[i].sgstPercentage,
          taxAmount: props.directInwards[i].taxAmount,
          totalAmount: props.directInwards[i].totalAmount,
        });
        rcvQnt += props.directInwards[i].quantity;
        setRcvQnty(rcvQnt);
        setProdName(props.directInwards[i].productName);
        setProdId(props.directInwards[i].productId);
        // setUomValue(props.directInwards[i].uom);
      }
      setInputList(pendArray);
    }
  }, [props.directInwards, props.directProdId]);

  // let primUom = [];
  useEffect(() => {
    let altUom = [];
    let selectUom = [];
    if (getPharma) {
      selectUom.push(getPharma.primaryUOM);
      if (getPharma.umo?.length != 0) {
        for (var i = 0; i < getPharma.umo?.length; i++) {
          if (getPharma.umo[i]?.alternateUOM != "") {
            altUom.push(getPharma.umo[i].alternateUOM);
          }
        }
      }
    }
    setPrimUom([...selectUom, ...altUom]);
  }, [getPharma]);
  // handle input change
  const [uomValue, setUomValue] = useState("");
  const setProductValue = (newValue) => {
    setProdId(newValue.id);
    setProdName(newValue.name);
    // dispatch(loadSingleProduct(newValue.id))
  };

  useEffect(() => {
    inputList.map((item) => {
      if (prodId) {
        item.productId = prodId;
        dispatch(loadSingleProduct(prodId));
      }
      if (prodName) {
        item.productName = prodName;
      }
      if (uomValue) {
        item.uom = uomValue;
      }
    });
  }, [prodId, prodName, uomValue, inputList]);

  const handleInputChange = (e, index) => {
    const { name, value } = e.target;
    const list = [...inputList];
    list[index][name] = value;
    setInputList(list);
    if (e.target.name === "mfgDate") {
      if (
        !moment(e.target.max).isSameOrAfter(
          moment(e.target.value).format("YYYY-MM-DD")
        )
      ) {
        //Tostify.notifyWarning("You are not Suppose to Enter Future Date...!");
        list[index][name] = "";
        setInputList(list);
      }
    }
    let amnt = 0;
    let cgst = 0;
    let sgst = 0;
    let total_gst = 0;
    // for(var i = 0; i < list.length; i++){
    if (list[index].quantity && list[index].rate) {
      amnt = Number(list[index].quantity) * Number(list[index].rate);
    }
    if (
      list[index].quantity &&
      list[index].rate &&
      list[index].cgstPercentage
    ) {
      cgst =
        Number(list[index].quantity) * Number(list[index].rate) -
        (Number(list[index].quantity) * Number(list[index].rate)) /
          (1 + Number(list[index].cgstPercentage) / 100);
    }
    if (
      list[index].quantity &&
      list[index].rate &&
      list[index].sgstPercentage
    ) {
      sgst =
        Number(list[index].quantity) * Number(list[index].rate) -
        (Number(list[index].quantity) * Number(list[index].rate)) /
          (1 + Number(list[index].sgstPercentage) / 100);
    }
    total_gst = cgst + sgst;
    // }
    list[index].taxAmount = total_gst;
    list[index].totalAmount = amnt + total_gst;
  };

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
  if (props.directInwards == null) {
    array = inputList;
  } else {
    array = [...props.directInwards, ...inputList];
  }

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
      if (Number(qntySum) != Number(rcvQnty)) {
        Tostify.notifyFail(
          "Quantity sholud not be greater than or smaller than recieved quantity"
        );
      } else {
        props.newInwardsClose(true);
        props.setDirectInwards(array);

        // props.setBatchDetails(inputList)
        setProdId("");
        setProdName("");
        setRcvQnty("");
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
      if (Number(qntySum) != Number(rcvQnty)) {
        Tostify.notifyFail(
          "Quantity sholud not be greater than or smaller than recieved quantity"
        );
      } else {
        const newProductList = props.directInwards.filter(
          (item) => item.productId !== id
        );
        let updateBatch = [...newProductList, ...inputList];
        props.newInwardsClose(true);
        props.setDirectInwards(updateBatch);
        // props.setBatchDetails(updateBatch)
        props.setDirectProdId("");
        setRcvQnty("");
        setProdName("");
        setProdId("");
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

  return (
    <Modal
      show={inwardsShow}
      onHide={inwardsClose}
      className="direct-inwards-modal-div"
    >
      <ToastContainer />
      <div className="directInwards-modal">
        <div>
          <div className="add-service-form-col">
            <Row>
              <Col md={6} xs={9}>
                <h1 className="dia-heading new-mdl-header">Add New Item</h1>
              </Col>
              <Col md={6} xs={3} align="right">
                <button
                  onClick={inwardsClose}
                  className="bi bi-x close-popup"
                ></button>
              </Col>
            </Row>
            <div>
              <h1 className="dia-heading new-drug-header">Drugs Details</h1>
            </div>
            <Row className="batch-order-div">
              <Col md={3}>
                <div className="pro-div">
                  <Form.Group className="mb-3_drugname">
                    <Form.Label>Drug Name</Form.Label>
                    <Autocomplete
                      classes={{
                        inputRoot: classes.inputRoot,
                        paper: classes.popper,
                      }}
                      value={prodName || ""}
                      name="productName"
                      onChange={(e, newValue) => {
                        setProductValue(newValue);
                      }}
                      filterOptions={(options, params) => {
                        const filtered = filter(options, params);
                        const { inputValue } = params;
                        // Suggest the creation of a new value
                        const isExisting = options.some(
                          (option) => inputValue === option.name
                        );
                        if (inputValue !== "" && !isExisting) {
                          filtered.push({
                            inputValue,
                            name: `Add "${inputValue}"`,
                          });
                        }
                        return filtered;
                      }}
                      // selectOnFocus clearOnBlur handleHomeEndKeys
                      id="free-solo-with-text-demo"
                      options={topOrder}
                      getOptionLabel={(option) => {
                        // Value selected with enter, right from the input
                        if (typeof option === "string") {
                          return option;
                        }
                        // Add "xxx" option created dynamically
                        if (option.inputValue) {
                          return option.inputValue;
                        }
                        // Regular option
                        return option.name;
                      }}
                      renderOption={(props, option) => (
                        <li {...props}>{option.name}</li>
                      )}
                      sx={{ width: 300 }}
                      freeSolo
                      renderInput={(params) => (
                        <TextField
                          style={{ zIndex: 9999 }}
                          {...params}
                          InputProps={{
                            ...params.InputProps,
                            className: classes.inputRoot,
                            onFocus: (event) => {
                              // Customize styles when focused
                              if (params.InputProps.onFocus) {
                                params.InputProps.onFocus(event);
                              }
                              // Add additional styles here
                              // For example, set border color to blue when focused
                              event.target.style.fontSize = "12px";
                            },
                          }}
                          label="Enter Product Name"
                        />
                      )}
                    />
                  </Form.Group>
                </div>
              </Col>
              <Col md={3}>
                <div>
                  <Form.Group>
                    <Form.Label>Select UOM</Form.Label>
                    <Form.Select
                      aria-label="Default select example"
                      value={uomValue || ""}
                      name="uom"
                      onChange={(e) => setUomValue(e.target.value)}
                    >
                      <option value="" disabled hidden>
                        Select UOM
                      </option>
                      {primUom.map((item, i) => (
                        <option value={item} key={i}>
                          {item}
                        </option>
                      ))}
                    </Form.Select>
                  </Form.Group>
                </div>
              </Col>
              <Col md={3}>
                <div>
                  <Form.Group>
                    <Form.Label>Received Quantity</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="Enter Received Quantity"
                      name="drugQty"
                      value={rcvQnty}
                      onChange={(e) => setRcvQnty(e.target.value)}
                    />
                    <Form.Label className="batch-info-footer">
                      E.g.,250.00
                    </Form.Label>
                  </Form.Group>
                </div>
              </Col>
            </Row>
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
                  <FormTableHeader tableHeader={DirectHeader1} />
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
                                value={x.barCode}
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
                                value={x.batchNumber}
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
                                value={x.quantity}
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
                                value={x.mfgDate}
                                name="mfgDate"
                                max={moment(new Date()).format("YYYY-MM-DD")}
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
                                value={x.expiryDate}
                                name="expiryDate"
                                max={maxDateExpire}
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
                                value={x.rate}
                                name="rate"
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
                                value={x.mrpRate}
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
                                value={x.cgstPercentage}
                                name="cgstPercentage"
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
                                value={x.sgstPercentage}
                                name="sgstPercentage"
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
                                value={x.taxAmount}
                                name="taxAmount"
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
                                value={x.totalAmount}
                                name="totalAmount"
                                onChange={(e) => handleInputChange(e, i)}
                              />
                            </Form.Group>
                          </TableCell>
                          <TableCell align="center">
                            {/* <p className="batchAdd btn btn-link">
                                                                <i className="bi bi-pencil-square" aria-hidden="true"></i>
                                                            </p> */}
                            {inputList.length > 1 && (
                              <p
                                className="my-trash btn btn-link"
                                onClick={handleRemoveClick}
                              >
                                <i className="bi bi-trash"></i>
                              </p>
                            )}
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
                          onClick={inwardsClose}
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
                              UpdateInwardData(props.directProdId);
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

export default NewInwards;
