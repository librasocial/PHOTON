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
import { Row, Col, Form, Button } from "react-bootstrap";
import IndentBatchModal from "../AddBatchDetails/IndentBatchModal";
import "./PO.css";
import { useDispatch, useSelector } from "react-redux";
import AddCircleRoundedIcon from "@mui/icons-material/AddCircleRounded";
import moment from "moment";
import FormTableHeader from "../../pharmacyTable/FormTableHeader";
import { IndentHeader1, IndentHeader2 } from "../../../ConstUrl/OptionsData";

function AgainstIndent(props) {
  const { SingleOrder } = useSelector((state) => state.data);
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
      avlQuantity: "",
      batchNumber: "",
      quantity: "",
      mfgDate: "",
      expiryDate: "",
    },
  ]);
  const sortedList = inputList.sort((a, b) =>
    a.productName.localeCompare(b.productName)
  );

  // handle input change
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
  };

  useEffect(() => {
    props.setInwardsPoDetails(inputList);
  }, [inputList]);

  useEffect(() => {
    if (props.inwardsPoDetails.length != 0) {
      if (props.inwardsPoDetails.length != 0 && props.supStatus == false) {
        setInputList(props.inwardsPoDetails);
      }
    }
  }, [props.inwardsPoDetails]);

  // handle click event of the Remove button
  const handleRemoveClick = (index) => {
    const list = [...inputList];
    list.splice(index, 1);
    setInputList(list);
  };

  const handleAddClick = (e) => {
    inputList.map((item) => {
      if (item.productId == e) {
        setInputList([
          ...inputList,
          {
            productId: item.productId,
            productName: item.productName,
            uom: item.uom,
            barCode: "",
            batchNumber: "",
            avlQuantity: item.avlQuantity,
            quantity: "",
            mfgDate: "",
            expiryDate: "",
          },
        ]);
      }
    });
  };

  useEffect(() => {
    if (props.supStatus == true) {
      if (Object.keys(SingleOrder).length != 0) {
        let ordersArray = [];
        for (var i = 0; i < SingleOrder.indentItems.length; i++) {
          ordersArray.push({
            productId: SingleOrder.indentItems[i].productId,
            productName: SingleOrder.indentItems[i].productName,
            avlQuantity: SingleOrder.indentItems[i].quantity,
            quantity: "",
            uom: SingleOrder.indentItems[i].uom,
            recieveQty: "",
          });
        }
        setInputList(ordersArray);
      }
    }
  }, [SingleOrder]);

  return (
    <div className="ag_indent">
      <Paper
        sx={{
          width: "100%",
          overflow: "hidden",
          margin: "2% 0 0 0 !important",
        }}
        className="against-indent-details"
      >
        <TableContainer sx={{ maxHeight: 440 }}>
          {props.supStatus == true && (
            <Table>
              <TableBody>
                <TableRow>
                  <TableCell className="indent-row-details">
                    <p className="po-sta-head-det">
                      Indent Number :{" "}
                      <span className="indent-sta-body-det">
                        <b>{SingleOrder.id}</b>
                      </span>
                    </p>
                  </TableCell>
                  <TableCell className="indent-row-details">
                    <p className="po-sta-head-det">
                      Indent Date :{" "}
                      <span className="indent-sta-body-det">
                        <b>
                          {moment(new Date(SingleOrder.poDate)).format(
                            "DD-MM-YYYY"
                          )}
                        </b>
                      </span>
                    </p>
                  </TableCell>
                  <TableCell className="indent-row-details">
                    <label className="order-badge">Pending</label>
                  </TableCell>
                </TableRow>
              </TableBody>
            </Table>
          )}
          <Table stickyHeader aria-label="sticky table">
            <FormTableHeader
              tableHeader={!props.inwardsId ? IndentHeader1 : IndentHeader2}
            />
            <TableBody>
              {inputList.map((x, i) => (
                <TableRow key={i}>
                  <TableCell>
                    <b>{x.productName}</b>
                  </TableCell>
                  <TableCell>{x.uom}</TableCell>
                  {!props.inwardsId && <TableCell>{x.avlQuantity}</TableCell>}
                  <TableCell>
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
                        min={disablePastDt()}
                        value={x.expiryDate || ""}
                        name="expiryDate"
                        max={maxDateExpire}
                        onChange={(e) => handleInputChange(e, i)}
                      />
                    </Form.Group>
                  </TableCell>
                  {/* <TableCell>
                                        <Form.Group className="mb-3_days" controlId="exampleForm.FName">
                                            <Form.Control className='presription-input' type="number" placeholder="Enter Quantity"
                                                value={x.quantity || ""} name="quantity"
                                                onChange={(e) => handleInputChange(e, i)} />
                                        </Form.Group>
                                    </TableCell> */}
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
                  <TableCell align="center">
                    <p
                      className="btn btn-link"
                      onClick={() => handleAddClick(x.productId)}
                    >
                      <i
                        className="fa fa-plus-circle"
                        style={{ color: "#2D5986" }}
                      ></i>
                    </p>
                    <p
                      className="btn btn-link my-trash"
                      onClick={(e) => handleRemoveClick(i)}
                    >
                      <i className="bi bi-trash"></i>
                    </p>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </div>
  );
}

export default AgainstIndent;
