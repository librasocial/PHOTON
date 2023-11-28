import React, { useState, useRef, useEffect } from "react";
import {
  Button,
  Row,
  Col,
  Breadcrumb,
  Form,
  OverlayTrigger,
  Tooltip,
} from "react-bootstrap";
import AddCircleRoundedIcon from "@mui/icons-material/AddCircleRounded";
import AddViewSupplierDetails from "./AddViewSupplier/AddViewSupplierDetails";
import AgainstIndent from "./PO/AgainstIndent";
import AgainstPO from "./PO/AgainstPO";
import NewInwards from "./PO/NewInwards";
import moment from "moment";
import "./NewInwardsOrder.css";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import { useDispatch, useSelector } from "react-redux";
import {
  loadByNameType,
  loadAddInwards,
  loadUpdateInwards,
  loadInwards,
  loadSingleOrder,
} from "../../../redux/actions";
import CancelOrder from "../Order/CancelOrder";
import Loader from "../Dashboard/Loader";
import * as Tostify from "../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
// import EAushadaInwards from "../../Pharmacy/Inwards/PO/EAushadaInwards";
import EAushadaInwards from "./PO/EAushadaInwards";

import {
  Paper,
  Table,
  TableRow,
  TableCell,
  TableContainer,
  TablePagination,
  TableBody,
  TableHead,
} from "@mui/material";

function NewInwardsOrder(props) {
  let dispatch = useDispatch();

  const { SingleOrder } = useSelector((state) => state.data);
  const { inwardsById } = useSelector((state) => state.data);
  const [selectedItems, setSelectedItems] = useState("");
  const [orderStatus, setOrderStatus] = useState(false);

  useEffect(() => {
    document.title = "EMR Inwards Order";
    if (selectedItems) {
      dispatch(loadSingleOrder(selectedItems));
      setOrderStatus(true);
      setSupStatus(true);
    } else {
      setOrderStatus(false);
      setSupStatus(false);
    }
  }, [selectedItems]);

  useEffect(() => {
    if (orderStatus == true) {
      setSelectedOrder(SingleOrder);
    } else if (orderStatus == false) {
      setSelectedOrder(null);
    }
  }, [SingleOrder, orderStatus]);

  const [purchasePoDetails, setPurchasePoDetails] = useState([]);
  const [inwardsPoDetails, setInwardsPoDetails] = useState([]);
  const [directInwards, setDirectInwards] = useState(null);
  const [selectedOrder, setSelectedOrder] = useState(null);

  // loader
  const [inwardsLoading1, setInwardsLoading1] = useState(false);
  const [inwardsLoading2, setInwardsLoading2] = useState(false);
  const [inwardsLoading3, setInwardsLoading3] = useState(false);
  // loader

  useEffect(() => {
    inwardsPo.inwardType = inwardsById.inwardType;
    inwardsPo.supplierName = inwardsById.supplierName;
    inwardsPo.receivedDate = inwardsById.receivedDate;
    inwardsPo.status = inwardsById.status;
    if (
      inwardsById.inwardType == "PO" ||
      inwardsById.inwardType == "Direct Inwards"
    ) {
      poPurcahseDetails.poId = inwardsById.poDetails.poId;
      poPurcahseDetails.poDate = inwardsById.poDetails.poDate;
      poPurcahseDetails.poAmt = inwardsById.poDetails.poAmt;
      poPurcahseDetails.remarks = inwardsById.poDetails.remarks;
    }
    if (inwardsById.inwardType == "Indent") {
      poIndentDetails.deliveryChallanNo =
        inwardsById.indentDetails.deliveryChallanNo;
      poIndentDetails.challanDate = inwardsById.indentDetails.challanDate;
      poIndentDetails.remarks = inwardsById.indentDetails.remarks;
      setInwardsPoDetails(inwardsById.indentItems);
    }
    if (inwardsById.inwardType == "PO") {
      setPurchasePoDetails(inwardsById.indentItems);
    } else if (inwardsById.inwardType == "Direct Inwards") {
      setDirectInwards(inwardsById.indentItems);
    }
  }, [inwardsById, inwardsPo, poPurcahseDetails]);

  const [addSupplierDetails, setAddSupplierDetails] = useState(false);
  const addSupplierDetailsClose = () => setAddSupplierDetails(false);
  const addSupplierDetailsShow = () => setAddSupplierDetails(true);

  const [newInwards, setNewInwards] = useState(false);
  const newInwardsClose = () => setNewInwards(false);
  const newInwardsShow = () => setNewInwards(true);

  const [enterQnty, setEnterQnty] = useState(false);
  const [supStatus, setSupStatus] = useState(false);

  const [poPurcahseDetails, setPoPurchaseDetails] = useState({
    poId: "",
    poDate: "",
    poAmt: "",
    remarks: "",
  });

  const { poId, poDate, poAmt, remarks } = poPurcahseDetails;

  const [poIndentDetails, setPoIndentDetails] = useState({
    deliveryChallanNo: "",
    challanDate: "",
    remarks: "",
  });

  const { deliveryChallanNo, challanDate } = poIndentDetails;

  const [inwardsPo, setInwardsPO] = useState({
    supplierName: "",
    inwardType: "",
    receivedDate: "",
    poDetails: null,
    indentDetails: null,
    indentItems: [],
    status: "",
  });
  console.log("hii there", inwardsPo);

  const {
    supplierName,
    inwardType,
    receivedDate,
    poDetails,
    indentDetails,
    indentItems,
    status,
  } = inwardsPo;

  useEffect(() => {
    if (inwardType == "Indent") {
      inwardsPo.indentDetails = poIndentDetails;
      inwardsPo.indentItems = inwardsPoDetails;
    } else if (inwardType == "PO") {
      inwardsPo.poDetails = poPurcahseDetails;
      inwardsPo.indentItems = purchasePoDetails;
    } else if (inwardType == "Direct Inwards") {
      inwardsPo.poDetails = poPurcahseDetails;
      inwardsPo.indentItems = directInwards;
    }
  }, [
    purchasePoDetails,
    poPurcahseDetails,
    poIndentDetails,
    inwardsPoDetails,
    directInwards,
  ]);

  useEffect(() => {
    if (supplierName && inwardType) {
      dispatch(loadByNameType(supplierName, inwardType, 0, 100));
    }
  }, [supplierName, inwardType]);

  const handleInwardsChange = (e) => {
    setEushadhaReceivedDate(e.target.value);
    if (e.target.name == "inwardType") {
      // if (e.target.value == "e-Aushadha") {
      //   fetchEAushadhaDetails();
      // }
      inwardsPo.supplierName = "";
      setPurchasePoDetails([]);
      setInwardsPoDetails([]);
      setDirectInwards(null);
      setSelectedItems("");
      setOrderStatus(false);
      dispatch(loadSingleOrder(null));
    }
    const { name, value } = e.target;
    setInwardsPO({ ...inwardsPo, [name]: value });
    if (e.target.name === "receivedDate") {
      if (
        !moment(e.target.max).isSameOrAfter(
          moment(e.target.value).format("YYYY-MM-DD")
        )
      ) {
        setInwardsPO({ ...inwardsPo, [name]: "" });
      }
    }
  };

  const handlePoChange = (e) => {
    const { name, value } = e.target;
    setPoPurchaseDetails({ ...poPurcahseDetails, [name]: value });
    if (e.target.name === "poDate") {
      if (
        !moment(e.target.max).isSameOrAfter(
          moment(e.target.value).format("YYYY-MM-DD")
        )
      ) {
        // Tostify.notifyWarning("You are not Suppose to Enter Future Date in Invoice Date...!");
        setPoPurchaseDetails({ ...poPurcahseDetails, [name]: "" });
      }
    }
  };
  const handleIndentChange = (e) => {
    const { name, value } = e.target;
    setPoIndentDetails({ ...poIndentDetails, [name]: value });
    if (e.target.name === "challanDate") {
      if (
        !moment(e.target.max).isSameOrAfter(
          moment(e.target.value).format("YYYY-MM-DD")
        )
      ) {
        // Tostify.notifyWarning("You are not Suppose to Enter Future Date in Delivery Challan Date...!");
        setPoIndentDetails({ ...poIndentDetails, [name]: "" });
      }
    }
  };

  const onClickChange = () => {
    props.setPageChange3(false);
  };

  const [totalBillAmount, setTotalBillAmount] = useState("");
  const [netGstAmount, setNetGstAmount] = useState("");
  const [netBillAmount, setNetBillAmount] = useState("");

  const renderTooltip = (props) => (
    <Tooltip id="button-tooltip" {...props}>
      <span>Amount</span>&nbsp;:{" "}
      <b>&#8377; {parseFloat(totalBillAmount).toFixed(2)} </b>
      <br />
      <span>GST</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:
      &nbsp;&nbsp;&nbsp;
      <b>&#8377; {parseFloat(netGstAmount).toFixed(2)}</b>
    </Tooltip>
  );
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

  function addPurchaseInwards() {
    if (inwardType == "Indent") {
      if (
        !supplierName ||
        !inwardType ||
        !receivedDate ||
        !deliveryChallanNo ||
        !challanDate ||
        !status ||
        enterQnty == true
      ) {
        alert("Please fill mandatory fields...!");
      } else {
        var inwardsResponse = {
          headers: serviceHeaders.myHeaders1,
          method: "POST",
          mode: "cors",
          body: JSON.stringify(inwardsPo),
        };
        dispatch(
          loadAddInwards(
            inwardsResponse,
            setInwardsLoading1,
            setInwardsLoading2,
            setInwardsLoading3
          )
        );
      }
    } else if (inwardType == "PO") {
      if (
        !supplierName ||
        !inwardType ||
        !receivedDate ||
        !poId ||
        !poDate ||
        !poAmt ||
        !status ||
        enterQnty == true
      ) {
        alert("Please fill mandatory fields...!");
      } else {
        var inwardsResponse = {
          headers: serviceHeaders.myHeaders1,
          method: "POST",
          mode: "cors",
          body: JSON.stringify(inwardsPo),
        };
        dispatch(
          loadAddInwards(
            inwardsResponse,
            setInwardsLoading1,
            setInwardsLoading2,
            setInwardsLoading3
          )
        );
      }
    } else if (inwardType == "Direct Inwards") {
      if (
        !supplierName ||
        !inwardType ||
        !receivedDate ||
        !poId ||
        !poDate ||
        !poAmt ||
        !status
      ) {
        alert("Please fill mandatory fields...!");
      } else {
        var inwardsResponse = {
          headers: serviceHeaders.myHeaders1,
          method: "POST",
          mode: "cors",
          body: JSON.stringify(inwardsPo),
        };
        dispatch(
          loadAddInwards(
            inwardsResponse,
            setInwardsLoading1,
            setInwardsLoading2,
            setInwardsLoading3
          )
        );
      }
    } else if (inwardType == "e-Aushadha") {
      // const raw = JSON.stringify({
      //   supplierName: inwardsPo.supplierName,
      //   inwardType: inwardsPo.inwardType,
      //   receivedDate: inwardsPo.receivedDate,
      //   poDetails: null,
      //   indentDetails: null,
      //   indentItems: inputList,
      //   status: "",
      // });
      const raw = JSON.stringify({
        supplierName: inwardsPo.supplierName,
        inwardType: inwardsPo.inwardType,
        receivedDate: inwardsPo.receivedDate,
        poDetails: {
          poAmt: 0,
          poDate: inwardsPo.receivedDate,
          poId: "Not Mendatory",
          remarks: "Not Mendatory",
        },
        indentDetails: null,
        indentItems: inputList,
        status: status,
      });
      var inwardsResponse = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: raw,
      };
      dispatch(
        loadAddInwards(
          inwardsResponse,
          setInwardsLoading1,
          setInwardsLoading2,
          setInwardsLoading3
        )
      );
    }
  }

  const UpdatePurchaseInwards = () => {
    if (inwardType == "Indent") {
      if (
        !supplierName ||
        !inwardType ||
        !receivedDate ||
        !deliveryChallanNo ||
        !challanDate ||
        !status ||
        enterQnty == true
      ) {
        alert("Please fill mandatory fields...!");
      } else {
        var update = {
          type: "INWARD",
          properties: inwardsPo,
        };
        var inwardsResponse = {
          headers: serviceHeaders.myHeaders1,
          method: "PATCH",
          mode: "cors",
          body: JSON.stringify(update),
        };
        dispatch(
          loadUpdateInwards(
            inwardsResponse,
            props.inwardsId,
            setInwardsLoading1,
            setInwardsLoading2,
            setInwardsLoading3
          )
        );
      }
    } else if (inwardType == "PO") {
      if (
        !supplierName ||
        !inwardType ||
        !receivedDate ||
        !poId ||
        !poDate ||
        !poAmt ||
        !status ||
        enterQnty == true
      ) {
        alert("Please fill mandatory fields...!");
      } else {
        var update = {
          type: "INWARD",
          properties: inwardsPo,
        };
        var inwardsResponse = {
          headers: serviceHeaders.myHeaders1,
          method: "PATCH",
          mode: "cors",
          body: JSON.stringify(update),
        };
        dispatch(
          loadUpdateInwards(
            inwardsResponse,
            props.inwardsId,
            setInwardsLoading1,
            setInwardsLoading2,
            setInwardsLoading3
          )
        );
      }
    } else if (inwardType == "Direct Inwards") {
      if (
        !supplierName ||
        !inwardType ||
        !receivedDate ||
        !poId ||
        !poDate ||
        !poAmt ||
        !status
      ) {
        alert("Please fill mandatory fields...!");
      } else {
        var update = {
          type: "INWARD",
          properties: inwardsPo,
        };
        var inwardsResponse = {
          headers: serviceHeaders.myHeaders1,
          method: "PATCH",
          mode: "cors",
          body: JSON.stringify(update),
        };
        dispatch(
          loadUpdateInwards(
            inwardsResponse,
            props.inwardsId,
            setInwardsLoading1,
            setInwardsLoading2,
            setInwardsLoading3
          )
        );
      }
    } else if (inwardType == "e-Aushadha") {
      if (
        !supplierName ||
        !inwardType ||
        !receivedDate ||
        !poId ||
        !poDate ||
        !poAmt ||
        !status
      ) {
        alert("Please fill mandatory fields...!");
      } else {
        var update = {
          type: "INWARD",
          properties: inwardsPo,
        };
        var inwardsResponse = {
          headers: serviceHeaders.myHeaders1,
          method: "PATCH",
          mode: "cors",
          body: JSON.stringify(update),
        };
        dispatch(
          loadUpdateInwards(
            inwardsResponse,
            props.inwardsId,
            setInwardsLoading1,
            setInwardsLoading2,
            setInwardsLoading3
          )
        );
      }
    }
  };

  const handlePurchaseInput = (e) => {
    alert("Handeled successfully...!");
  };

  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling

  // modal for cancel button
  const [cancelOrderService, setCancelOrderService] = useState(false);
  const [inwardsStatus, setInwardsStatus] = useState(false);
  const cancelOrderServiceClose = () => setCancelOrderService(false);
  const cancelOrderServiceShow = () => {
    setCancelOrderService(true);
    setInwardsStatus(true);
  };
  // modal for cancel button
  const [directProdId, setDirectProdId] = useState("");

  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const disableFutureDt = (current) => {
    return current.isBefore(currentdate);
  };

  function closeInwardLoader() {
    props.setPage(0);
    props.setRowsPerPage(5);
    dispatch(loadInwards(props.page, props.rowsPerPage));
    props.setPageChange3(false);
    props.setInwardsId("");
  }
  const [Institutevalue, setInstitutevalue] = useState("");
  const [eAushadhaReceivedDate, setEushadhaReceivedDate] = useState("");
  const [fetchedData, setFetchedData] = useState([]);
  // console.log("here is data", fetchedData);

  function fetchEAushadhaDetails() {
    var raw = JSON.stringify({
      InwardDate: eAushadhaReceivedDate, //"2023-05-05",
      InstituteId: Institutevalue, //"001832",
    });
    fetch(
      "https://dev-api-phcdt.sampoornaswaraj.org/abdm-svc/eAushadha/api/DWInstituteInward",
      {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: raw,
      }
    )
      .then((response) => response.json())
      .then((data) => {
        setFetchedData(data);
        // const dataE = [
        //   {
        //     productId: "",
        //   },
        // ];
        const updatedInputList = data.map((e) => {
          return {
            productId: e.Drug_id,
            productName: e.Drug_name, // Add other properties as needed
            uom: "",
            barCode: "",
            batchNumber: e.Batch_number,
            quantity: e.Quantity_In_Units,
            mfgDate: e.Mfg_date,
            expiryDate: e.Exp_date,
            rate: "",
            mrpRate: "",
            cgstPercentage: "",
            sgstPercentage: "",
            taxAmount: "",
            totalAmount: "",
          };
        });
        setInputList(updatedInputList);

        return inputList;
      })
      .catch((error) => {
        console.error("Error fetching e-Aushadha details:", error);
      });
  }
  console.log("idharrr111 ", inputList);
  const [showPopup, setShowPopup] = useState(false);

  // Function to toggle the visibility of the popup
  const togglePopup = () => {
    setShowPopup(!showPopup);
  };

  return (
    <div>
      <Loader
        inwardsId={props.inwardsId}
        closeInwardLoader={closeInwardLoader}
        inwardsLoading1={inwardsLoading1}
        inwardsLoading2={inwardsLoading2}
        inwardsLoading3={inwardsLoading3}
        status={status}
      />
      <CancelOrder
        cancelOrderService={cancelOrderService}
        cancelOrderServiceClose={cancelOrderServiceClose}
        setInwardsStatus={setInwardsStatus}
        inwardsStatus={inwardsStatus}
        setPageChange={props.setPageChange3}
      />
      <AddViewSupplierDetails
        addSupplierDetails={addSupplierDetails}
        setSupStatus={setSupStatus}
        addSupplierDetailsClose={addSupplierDetailsClose}
        supplierName={supplierName}
        type={inwardType}
        setOrderStatus={setOrderStatus}
        setSelectedItems={setSelectedItems}
      />
      <NewInwards
        newInwards={newInwards}
        directProdId={directProdId}
        setDirectProdId={setDirectProdId}
        newInwardsClose={newInwardsClose}
        directInwards={directInwards}
        inwardsId={props.inwardsId}
        setDirectInwards={setDirectInwards}
      />
      <div className="regHeader">
        <h1 className="register-Header">Inwards</h1>
      </div>
      <hr style={{ margin: "0px" }} />
      <ToastContainer />
      <div className="porder-tab">
        <div className="pharma-form">
          <Breadcrumb>
            <Breadcrumb.Item
              className="pur-order-breadcrumb"
              onClick={onClickChange}
            >
              Inwards
            </Breadcrumb.Item>
            <Breadcrumb.Item active>
              {props.inwardsId ? "Edit Inwards" : "New Inwards"}
            </Breadcrumb.Item>
          </Breadcrumb>
          <div className="pro-tab purchase-order">
            <div>
              <div className="inwards-border">
                <h1 className="inwards-head">
                  {props.inwardsId ? "Edit Inwards" : "New Inwards"}
                </h1>
              </div>
              <Row className="inwards-order-div">
                <Col md={3}>
                  <div>
                    <Form.Group>
                      <Form.Label>Inwards Type</Form.Label>
                      <Form.Select
                        aria-label="Default select example"
                        id="inwardsType"
                        value={inwardType}
                        className="pharma-select"
                        name="inwardType"
                        onChange={handleInwardsChange}
                      >
                        <option value="" disabled hidden>
                          Select Supplier...
                        </option>
                        <option value="PO">Against PO</option>
                        <option value="Indent">Against Indent</option>
                        <option value="Direct Inwards">Direct Inwards</option>
                        <option value="e-Aushadha">e-Aushadha</option>
                      </Form.Select>
                    </Form.Group>
                  </div>
                </Col>

                <Col md={3}>
                  <div>
                    <Form.Group>
                      <Form.Label>Supplier</Form.Label>
                      <Form.Select
                        aria-label="Default select example"
                        name="supplierName"
                        value={supplierName}
                        className="pharma-select"
                        onChange={handleInwardsChange}
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
                        <option value="e-Aushadha">e-Aushadha</option>
                      </Form.Select>
                    </Form.Group>
                  </div>
                </Col>
                {supplierName &&
                  inwardType &&
                  inwardType != "Direct Inwards" &&
                  inwardType != "e-Aushadha" &&
                  !props.inwardsId && (
                    <Col md={4}>
                      <div>
                        <Button
                          className="btn btn-primary order-badge inwards-alert"
                          onClick={addSupplierDetailsShow}
                        >
                          Pending PO for Selected Supplier
                          {/*View Pending PO for Selected Supplier */}
                        </Button>
                      </div>
                    </Col>
                  )}

                <Col md={2}>
                  <div>
                    <Form.Group>
                      <Form.Label>Received Date</Form.Label>
                      <Form.Control
                        type="date"
                        max={moment(new Date()).format("YYYY-MM-DD")}
                        value={
                          moment(new Date(receivedDate)).format("YYYY-MM-DD") ||
                          ""
                        }
                        name="receivedDate"
                        onChange={(event) => handleInwardsChange(event)}
                      />
                    </Form.Group>
                  </div>
                </Col>
                {inwardType == "e-Aushadha" ? (
                  <>
                    <Col md={2}>
                      <div>
                        <Form.Group>
                          <Form.Label>Institute Id</Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter Institute id"
                            // value={poId || ""}
                            // name="poId"
                            onChange={(e) => setInstitutevalue(e.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={2}>
                      <div>
                        <Form.Group>
                          <Form.Label>e-Aushadha </Form.Label>
                          <Button
                            type="button"
                            className="orderSubmit btn btn-primary"
                            onClick={fetchEAushadhaDetails}
                          >
                            Submit
                          </Button>
                        </Form.Group>
                      </div>
                    </Col>
                  </>
                ) : (
                  <></>
                )}
              </Row>

              {inwardType == "Indent" ? (
                <Row className="inwards-order-div">
                  <Col md={3}>
                    <div>
                      <Form.Group>
                        <Form.Label>Delivery Challan Number</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter Delivery Challan Number"
                          maxLength={50}
                          value={deliveryChallanNo || ""}
                          name="deliveryChallanNo"
                          onChange={handleIndentChange}
                        />
                      </Form.Group>
                    </div>
                  </Col>
                  <Col md={3}>
                    <div>
                      <Form.Group>
                        <Form.Label>Delivery Challan Date</Form.Label>
                        <Form.Control
                          type="date"
                          max={moment(new Date()).format("YYYY-MM-DD")}
                          value={challanDate || ""}
                          name="challanDate"
                          onChange={handleIndentChange}
                        />
                      </Form.Group>
                    </div>
                  </Col>
                  <Col md={4}>
                    <div>
                      <Form.Group>
                        <Form.Label>Remarks</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter Invoice Number"
                          value={remarks || ""}
                          name="remarks"
                          onChange={handleIndentChange}
                        />
                      </Form.Group>
                    </div>
                  </Col>

                  <Col md={2}></Col>
                </Row>
              ) : (
                <Row className="inwards-order-div">
                  <Col md={3}>
                    <div>
                      <Form.Group>
                        <Form.Label>Invoice Number</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter Invoice Number"
                          value={poId || ""}
                          name="poId"
                          onChange={handlePoChange}
                        />
                      </Form.Group>
                    </div>
                  </Col>
                  <Col md={2}>
                    <div>
                      <Form.Group>
                        <Form.Label>Invoice Date</Form.Label>
                        <Form.Control
                          type="date"
                          max={moment(new Date()).format("YYYY-MM-DD")}
                          value={
                            moment(new Date(poDate)).format("YYYY-MM-DD") || ""
                          }
                          name="poDate"
                          onChange={handlePoChange}
                        />
                      </Form.Group>
                    </div>
                  </Col>
                  <Col md={2}>
                    <div>
                      <Form.Group>
                        <Form.Label>Invoice Amount&nbsp;(&#8377;)</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter Invoice Number"
                          value={poAmt || ""}
                          name="poAmt"
                          onChange={handlePoChange}
                        />
                      </Form.Group>
                    </div>
                  </Col>
                  <Col md={3}>
                    <div>
                      <Form.Group>
                        <Form.Label>Remarks</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter Invoice Number"
                          value={remarks || ""}
                          name="remarks"
                          onChange={handlePoChange}
                        />
                      </Form.Group>
                    </div>
                  </Col>
                </Row>
              )}
            </div>
          </div>
          {inwardType == "e-Aushadha" ? (
            <div>
              <div
                className="d-flex justify-content-center p-4"
                style={{ width: "100%" }}
              >
                {fetchedData.length !== 0 && (
                  <EAushadaInwards
                    fetchedData={fetchedData}
                    Institutevalue={Institutevalue}
                  />
                )}
              </div>
            </div>
          ) : (
            <>
              {((!props.inwardsId && orderStatus == false) ||
                (SingleOrder.poDetails == null &&
                  SingleOrder.indentItems == null &&
                  directInwards == null &&
                  Object.keys(inwardsById).length == 0)) &&
                props.editStatus == true && (
                  <div className="pro-tab add-purchase-order">
                    <div className="add-item-btn text-center">
                      <img
                        src="../img/Pharmacy/inwards.png"
                        className="rounded mx-auto d-block inwards-img"
                      />
                      <p className="add-drugs-material">
                        No Drugs or Materials have been added. Please Add Items
                        to Create GRN.
                      </p>
                      {inwardType != "PO" && inwardType != "Indent" && (
                        <Button
                          type="button"
                          className="pharmaBtnPC"
                          onClick={newInwardsShow}
                          disabled={!inwardType}
                        >
                          <AddCircleRoundedIcon /> &nbsp; Add Item
                        </Button>
                      )}
                    </div>
                  </div>
                )}
            </>
          )}
          <div>
            {((selectedOrder != null && selectedOrder.poDetails != null) ||
              directInwards != null ||
              (Object.keys(inwardsById).length != 0 &&
                inwardsById.inwardType == "PO")) && (
              <AgainstPO
                purchasePoDetails={purchasePoDetails}
                setPurchasePoDetails={setPurchasePoDetails}
                setEnterQnty={setEnterQnty}
                supStatus={supStatus}
                directInwards={directInwards}
                setDirectInwards={setDirectInwards}
                inwardType={inwardType}
                newInwards={newInwards}
                setNewInwards={setNewInwards}
                directProdId={directProdId}
                setDirectProdId={setDirectProdId}
                setTotalBillAmount={setTotalBillAmount}
                inwardsId={props.inwardsId}
                setNetGstAmount={setNetGstAmount}
                setNetBillAmount={setNetBillAmount}
              />
            )}
            {((selectedOrder != null && selectedOrder.indentItems != null) ||
              (Object.keys(inwardsById).length != 0 &&
                inwardsById.inwardType == "Indent")) && (
              <AgainstIndent
                setInwardsPoDetails={setInwardsPoDetails}
                setEnterQnty={setEnterQnty}
                supStatus={supStatus}
                inwardsPoDetails={inwardsPoDetails}
                inwardsId={props.inwardsId}
              />
            )}
            {inwardType == "Direct Inwards" && directInwards != null && (
              <div className="d-flex justify-content-center p-4">
                <Button
                  type="button"
                  className="pharmaBtnPC"
                  onClick={newInwardsShow}
                  disabled={!inwardType}
                >
                  <AddCircleRoundedIcon /> &nbsp; Add Item
                </Button>
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
            {/* {productList.length != 0 && */}
            <Row className="d-flex status-row">
              <Col md={6} align="right">
                {netBillAmount != 0 && (
                  <>
                    {(SingleOrder.poDetails != null ||
                      directInwards != null) && (
                      <p className="total-amount">
                        <span style={{ color: "#605f5f" }}>
                          <b>Total Amount </b>
                        </span>
                        : &nbsp;&#8377;
                        {/* <b>{parseFloat(netTotalAmnt).toFixed(2)} </b>&nbsp;&nbsp; */}
                        <b>{parseFloat(netBillAmount).toFixed(2)}</b>
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
                    )}
                  </>
                )}
              </Col>
              <Col md={1}>
                <div className="purch-line"></div>
              </Col>
              <Col md={2}>
                <p className="total-amount">Inwards Status</p>
              </Col>
              <Col md={3}>
                <div>
                  <Form.Group className="order-status">
                    <Form.Select
                      aria-label="Default select example"
                      className="pur-select"
                      name="status"
                      value={status}
                      onChange={handleInwardsChange}
                    >
                      <option value="" disabled hidden>
                        Select
                      </option>
                      <option value="DRAFT">DRAFT</option>
                      <option value="CREATED">CREATED</option>
                    </Form.Select>
                  </Form.Group>
                </div>
              </Col>
            </Row>
            {/* } */}
          </Col>
          <Col md={1}>
            {/* {productList.length != 0 && */}
            <div className="purchase-line"></div>
            {/* } */}
          </Col>
          <Col md={2} align="center">
            {props.inwardsId ? (
              <Button
                type="button"
                className="orderSubmit btn btn-primary"
                onClick={UpdatePurchaseInwards}
              >
                Update
              </Button>
            ) : (
              <Button
                type="button"
                className="orderSubmit btn btn-primary"
                onClick={addPurchaseInwards}
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

export default NewInwardsOrder;
