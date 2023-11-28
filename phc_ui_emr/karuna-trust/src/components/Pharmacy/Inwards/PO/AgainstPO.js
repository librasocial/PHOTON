import React, { useState, useRef, useEffect } from "react";
import { Button, Form } from "react-bootstrap";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import NewBatchModal from "../AddBatchDetails/NewBatchModal";
import "./PO.css";
import moment from "moment";
import AddCircleRoundedIcon from "@mui/icons-material/AddCircleRounded";
import { useDispatch, useSelector } from "react-redux";
import { loadSingleProduct } from "../../../../redux/actions";
import FormTableHeader from "../../pharmacyTable/FormTableHeader";
import { PoHeader1, PoHeader2 } from "../../../ConstUrl/OptionsData";

function AgainstPO(props) {
  let dispatch = useDispatch();

  const { SingleOrder } = useSelector((state) => state.data);
  const { inwardsById } = useSelector((state) => state.data);
  const maxDateExpire = moment(
    new Date(new Date().setFullYear(new Date().getFullYear() + 100))
  ).format("YYYY-MM-DD");

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
      ordQuantity: "",
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
  const sortedList = inputList.sort((a, b) =>
    a.productName.localeCompare(b.productName)
  );

  useEffect(() => {
    if (props.inwardType == "PO") {
      props.setPurchasePoDetails(inputList);
    } else {
      props.setDirectInwards(inputList);
    }
  }, [inputList]);

  const handleInputChange = (e, index) => {
    let cgst = 0;
    let sgst = 0;
    const { name, value } = e.target;
    const list = [...inputList];
    list[index][name] = value;
    if (e.target.name == "quantity") {
      for (var i = 0; i < inputList.length; i++) {
        if (inputList[i].cgstPercentage) {
          cgst =
            e.target.value * inputList[index].rate -
            (e.target.value * inputList[index].rate) /
              (1 + inputList[index].cgstPercentage / 100);
        }
        if (inputList[i].sgstPercentage) {
          sgst =
            e.target.value * inputList[index].rate -
            (e.target.value * inputList[index].rate) /
              (1 + inputList[index].sgstPercentage / 100);
        }
        inputList[index].taxAmount = cgst + sgst;
        inputList[index].totalAmount =
          e.target.value * inputList[index].rate + (cgst + sgst);
      }
    }
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
  };

  // handle click event of the Add button
  const handleAddClick = (e) => {
    inputList.map((item) => {
      if (item.productId == e) {
        setInputList([
          ...inputList,
          {
            productId: item.productId,
            productName: item.productName,
            uom: item.uom,
            ordQuantity: item.ordQuantity,
            barCode: "",
            batchNumber: "",
            quantity: "",
            mfgDate: "",
            expiryDate: "",
            rate: item.rate,
            mrpRate: "",
            cgstPercentage: item.cgstPercentage,
            sgstPercentage: item.sgstPercentage,
            taxAmount: "",
            totalAmount: "",
          },
        ]);
      }
    });
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

  useEffect(() => {
    if (props.purchasePoDetails.length != 0) {
      if (props.purchasePoDetails.length != 0 && props.supStatus == false) {
        setInputList(props.purchasePoDetails);
      }
    } else if (props.directInwards != null) {
      if (props.directInwards.length != 0 && props.supStatus == false) {
        setInputList(props.directInwards);
      }
    }
  }, [props.purchasePoDetails]);

  const [totalRcvAmnt, setTotalRcvAmnt] = useState("");
  const [totalRcvGst, setTotalRcvGst] = useState("");
  const [netRcvAmnt, setNetRcvAmnt] = useState("");
  useEffect(() => {
    let totalGst = 0;
    let totalAmount = 0;
    let netAmount = 0;
    for (var i = 0; i < inputList.length; i++) {
      if (inputList[i].taxAmount && inputList[i].totalAmount) {
        totalGst += inputList[i].taxAmount;
        netAmount += inputList[i].totalAmount;
        totalAmount = netAmount - totalGst;
      }
    }
    setTotalRcvAmnt(totalAmount);
    setTotalRcvGst(totalGst);
    setNetRcvAmnt(netAmount);
  }, [inputList]);

  useEffect(() => {
    props.setTotalBillAmount(totalRcvAmnt);
    props.setNetGstAmount(totalRcvGst);
    props.setNetBillAmount(netRcvAmnt);
  }, [totalRcvAmnt, totalRcvGst, netRcvAmnt]);

  useEffect(() => {
    if (props.inwardType == "PO") {
      if (props.supStatus == true) {
        if (Object.keys(SingleOrder).length != 0) {
          let ordersArray = [];
          for (var i = 0; i < SingleOrder.poDetails.poItems.length; i++) {
            ordersArray.push({
              productId: SingleOrder.poDetails.poItems[i].productId,
              productName: SingleOrder.poDetails.poItems[i].productName,
              ordQuantity: SingleOrder.poDetails.poItems[i].quantity,
              uom: SingleOrder.poDetails.poItems[i].uom,
              barCode: "",
              quantity: "",
              batchNumber: "",
              mfgDate: "",
              expiryDate: "",
              rate: SingleOrder.poDetails.poItems[i].rate,
              mrpRate: "",
              cgstPercentage: SingleOrder.poDetails.poItems[i].cgstPercentage,
              sgstPercentage: SingleOrder.poDetails.poItems[i].sgstPercentage,
              taxAmount: "",
              totalAmount: "",
            });
          }
          setInputList(ordersArray);
        }
      }
    }
  }, [SingleOrder, props.inwardType]);

  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling

  // handle click event of the Remove button
  const handleRemoveClick = (index) => {
    const list = [...inputList];
    list.splice(index, 1);
    setInputList(list);
  };

  return (
    <div className="ag_po">
      <Paper
        sx={{
          width: "100%",
          overflow: "hidden",
          margin: "2% 0 0 0 !important",
        }}
        className="new-inwards-details"
      >
        <TableContainer sx={{ maxHeight: 440 }}>
          {props.supStatus == true && (
            <>
              {props.inwardType == "PO" && (
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableCell className="po-details">
                        <p className="po-sta-head-det">
                          PO Number :{" "}
                          <span className="po-sta-body-det">
                            {SingleOrder.id}
                          </span>
                        </p>
                      </TableCell>
                      <TableCell className="po-details">
                        <p className="po-sta-head-det">
                          PO Date :{" "}
                          <span className="po-sta-body-det">
                            {moment(new Date(SingleOrder.poDate)).format(
                              "DD-MM-YYYY"
                            )}
                          </span>
                        </p>
                      </TableCell>
                      <TableCell className="po-details">
                        <label className="order-badge">Pending</label>
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              )}
            </>
          )}
          <Table stickyHeader aria-label="sticky table">
            <FormTableHeader
              tableHeader={
                props.inwardType == "PO" && !props.inwardsId
                  ? PoHeader1
                  : PoHeader2
              }
            />
            <TableBody>
              {inputList.map((x, i) => (
                <TableRow key={i}>
                  <TableCell align="left">
                    <b>{x.productName}</b>
                  </TableCell>
                  <TableCell align="left">{x.uom}</TableCell>
                  {props.inwardType == "PO" && !props.inwardsId && (
                    <TableCell align="left">{x.ordQuantity}</TableCell>
                  )}
                  <TableCell align="left">
                    <Form.Group
                      className="mb-3_days"
                      controlId="exampleForm.FName"
                    >
                      <Form.Control
                        className="presription-input"
                        type="number"
                        placeholder="Enter Received Quantity"
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
                        value={x.expiryDate || ""}
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
                  <TableCell align="center">
                    {props.inwardType == "PO" ? (
                      <p
                        className=" btn btn-link"
                        onClick={() => handleAddClick(x.productId)}
                      >
                        <i
                          className="fa fa-plus-circle"
                          style={{ color: "#2D5986" }}
                        ></i>
                      </p>
                    ) : (
                      ""
                    )}
                    {inputList.length > 1 && (
                      <p
                        className=" btn btn-link my-trash"
                        onClick={(e) => handleRemoveClick(i)}
                      >
                        <i className="bi bi-trash"></i>
                      </p>
                    )}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <Table>
            <TableBody>
              <TableRow>
                <TableCell className="billammount">
                  <p className="billammount-head">Amount&nbsp;&nbsp;:&nbsp;</p>
                  <p className="billammount-head">GST&nbsp;&nbsp;:&nbsp;</p>
                </TableCell>
                <TableCell className="bill-ammount">
                  <p className="bill-ammount-head">
                    &#8377; {parseFloat(totalRcvAmnt).toFixed(2)}
                  </p>
                  <p className="bill-ammount-head">
                    &#8377;&nbsp; {parseFloat(totalRcvGst).toFixed(2)}
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
                    &#8377; {parseFloat(netRcvAmnt).toFixed(2)}
                  </p>
                </TableCell>
              </TableRow>
              <TableRow className="total-bill"></TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </div>
  );
}

export default AgainstPO;
