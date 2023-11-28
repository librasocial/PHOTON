import React, { useState, useRef, useEffect } from "react";
import { Row, Col, Button, Form } from "react-bootstrap";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import MedqueModal from "./MedqueModal";
import moment from "moment";
import "./Medque.css";
import Sidemenu from "../../Sidemenus";
import { useDispatch, useSelector } from "react-redux";
import {
  loadPatDetails,
  loadPharmaList,
  loadOrderDetails,
  loadInventory,
  loadExpireDate,
  loadDispenceForId,
} from "../../../redux/actions";
import LabScreen from "../../LabModule/LabScreens/LabScreen";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import { useHistory } from "react-router-dom";
import * as constant from "../../ConstUrl/constant";
import ViewModalPopups from "../../EMR/ModalPopups/ViewModalPopups";
import TextField from "@mui/material/TextField";
import Autocomplete, { createFilterOptions } from "@mui/material/Autocomplete";
import PrescriptionModal from "../../EMR/ModalPopups/PrescriptionModal";
import FormTableHeader from "../pharmacyTable/FormTableHeader";
import { medicineHeader1, medicineHeader2 } from "../../ConstUrl/OptionsData";
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

function Medque(props) {
  const classes = useStyles();

  const form = useRef(null);
  let history = useHistory();
  const [isLoading1, setLoading1] = useState(false);
  let labResId = sessionStorage.getItem("LabResid");
  let pharmaOrdId = sessionStorage.getItem("LabOrdId");

  let dispatch = useDispatch();
  const { patDetails } = useSelector((state) => state.data);
  const { orderDetails } = useSelector((state) => state.data);
  const { inventory } = useSelector((state) => state.data);
  const { expireData } = useSelector((state) => state.data);
  const { dispenceForId } = useSelector((state) => state.data);

  const [medDetails, setMedDetails] = useState();
  useEffect(() => {
    document.title = "EMR Dispence Medicine";
    if (pharmaOrdId) {
      dispatch(loadOrderDetails(pharmaOrdId));
    }
  }, []);

  const filter = createFilterOptions();
  const { PharmaProdList } = useSelector((state) => state.data);
  let drugListArray = [];
  for (var i = 0; i < PharmaProdList.length; i++) {
    if (PharmaProdList[i].status == "ACTIVE") {
      drugListArray.push(PharmaProdList[i]);
    }
  }
  const topOrder = [
    ...new Map(
      drugListArray.map((item) => [JSON.stringify(item.name), item])
    ).values(),
  ];

  useEffect(() => {
    dispatch(loadPharmaList(0, 1000000));
    dispatch(loadPatDetails(labResId));
  }, [labResId]);

  const [itemArray, setItemArray] = useState([]);
  useEffect(() => {
    if (orderDetails) {
      orderDetails?.items?.map((item) => {
        itemArray.push(item);
      });
    }
    setMedDetails(orderDetails);
  }, [orderDetails]);

  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  const [inputList, setInputList] = useState([
    {
      productId: "",
      productName: "",
      batchNo: "",
      avaqty: "",
      expiryDate: "",
      orderQty: "",
      qtyIssued: "",
      penQnty: "",
    },
  ]);
  const [ordItem, setOrdItem] = useState([
    { productId: "", productName: "", orderQty: "" },
  ]);

  const [inventId, setInventId] = useState([]);

  useEffect(() => {
    let newInputList = [...inputList];
    for (var i = 0; i < newInputList.length; i++) {
      for (var j = 0; j < expireData.length; j++) {
        if (newInputList[i].batchNo == expireData[j].batchNumber) {
          newInputList[i].expiryDate = expireData[j].expiryDate;
          newInputList[i].avaqty = expireData[j].level;
          if (newInputList[i].avaqty && newInputList[i].qtyIssued) {
            newInputList[i].penQnty =
              newInputList[i].avaqty - newInputList[i].qtyIssued;
          } else {
            newInputList[i].penQnty = 0;
          }
          setInventId([
            ...inventId,
            {
              inventProductId: expireData[j].productId,
              inventoryId: expireData[j].id,
              batchNo: newInputList[i].batchNo,
            },
          ]);
        }
      }
    }
    setInputList(newInputList);
  }, [expireData]);

  useEffect(() => {
    let newProductList = [];
    for (var i = 0; i < inputList.length; i++) {
      for (var k = 0; k < inventId.length; k++) {
        if (inputList[i].batchNo == inventId[k].batchNo) {
          newProductList.push(inventId[k]);
        }
      }
    }
    setInventId(newProductList);
  }, [inputList]);

  useEffect(() => {
    dispatch(loadInventory());
    if (pharmaOrdId && medDetails && medDetails.status == "ORDERED") {
      dispatch(loadDispenceForId(pharmaOrdId));
      let medArray = [];
      for (var j = 0; j < medDetails.items.length; j++) {
        medArray.push({
          productId: medDetails.items[j].productId,
          productName: medDetails.items[j].productName,
          batchNo: "",
          expiryDate: "",
          orderQty: medDetails.items[j].orderQty,
          avaqty: "",
          qtyIssued: "",
        });
      }
      setInputList(medArray);
    } else {
      setInputList([
        {
          productId: "",
          productName: "",
          batchNo: "",
          expiryDate: "",
          orderQty: "",
          qtyIssued: "",
        },
      ]);
    }
  }, [medDetails, pharmaOrdId]);

  if (dispenceForId) {
    let dispenceItems = [];
    for (var d = 0; d < dispenceForId.length; d++) {
      for (var it = 0; it < dispenceForId[d].items.length; it++) {
        dispenceItems.push(dispenceForId[d].items[it]);
      }
    }

    for (var i = 0; i < inputList.length; i++) {
      let dispenceQnty = 0;
      for (var di = 0; di < dispenceItems.length; di++) {
        if (inputList[i].productId == dispenceItems[di].productId) {
          dispenceQnty += Number(dispenceItems[di].qtyIssued);
        }
      }
      inputList[i].penQnty = Number(inputList[i].orderQty) - dispenceQnty;
    }
  }

  const handleInputChange = (e, index, newValue) => {
    const { name, value } = e.target;
    const list = [...inputList];
    const item = [...ordItem];
    if (name == "batchNo") {
      inputList[index].batchNo = value;
      dispatch(loadExpireDate(value));
    } else {
      if (newValue) {
        inputList[index].productName = newValue.name;
        ordItem[index].productName = newValue.name;
        inputList[index].productId = newValue.id;
        ordItem[index].productId = newValue.id;
        inputList[index].batchNo = "";
        inputList[index].expiryDate = "";
        inputList[index].avaqty = "";
        ordItem[index].batchNo = "";
      } else {
        list[index][name] = value;
      }
    }

    for (var i = 0; i < inputList.length; i++) {
      if (inputList[i].avaqty && inputList[i].qtyIssued) {
        inputList[i].penQnty = inputList[i].avaqty - inputList[i].qtyIssued;
      }
    }
    setInputList(list);
    setOrdItem(item);
  };

  //disable past date in expiry date
  const disablePastDt = () => {
    const today = new Date();
    const DD = String(today.getDate() + 1).padStart(2, "0");
    const MM = String(today.getMonth() + 1).padStart(2, "0"); //January is 0!
    const YYYY = today.getFullYear();
    return YYYY + "-" + MM + "-" + DD;
  };
  //disable past date in expiry date

  // handle click event of the Remove button
  const handleRemoveClick = (index) => {
    const list = [...inputList];
    const ordList = [...ordItem];
    list.splice(index, 1);
    ordList.splice(index, 1);
    setInputList(list);
    setOrdItem(ordList);
  };

  const formRemoveClick = (index) => {
    const list = [...inputList];
    const ordList = [...ordItem];
    list.splice(index);
    ordList.splice(index);
    setInputList(list);
    setOrdItem(ordList);
  };

  const calcelField = () => {
    sessionStorage.removeItem("LabResid");
    sessionStorage.removeItem("LabOrdId");
    sessionStorage.removeItem("LabPateintid");
    formRemoveClick(true);
    setOrdItem([{ productId: "", productName: "", orderQty: "" }]);
    setInputList([
      {
        productId: "",
        productName: "",
        batchNo: "",
        expiryDate: "",
        orderQty: "",
        qtyIssued: "",
        avaqty: "",
      },
    ]);
    history.push("/Dashboard");
  };

  // handle click event of the Add button
  const handleAddClick = () => {
    setInputList([
      ...inputList,
      {
        productId: "",
        productName: "",
        batchNo: "",
        expiryDate: "",
        orderQty: "",
        qtyIssued: "",
        avaqty: "",
      },
    ]);
    setOrdItem([...ordItem, { productId: "", productName: "", orderQty: "" }]);
  };

  // handle click event of the Update button
  const handleUpdateClick = () => {
    setInputList([
      ...inputList,
      {
        productId: "",
        productName: "",
        batchNo: "",
        expiryDate: "",
        orderQty: "",
        qtyIssued: "",
        avaqty: "",
      },
    ]);
  };

  const [medqueModal, setSedqueModal] = useState(false);
  const medqueModalClose = () => setSedqueModal(false);
  const medqueModalShow = () => setSedqueModal(true);

  function SubmitDash() {
    setLoading1(true);
  }

  function loderCancel() {
    setLoading1(false);
    sessionStorage.removeItem("LabResid");
    sessionStorage.removeItem("LabOrdId");
    sessionStorage.removeItem("LabPateintid");
    setInputList([
      {
        productId: "",
        productName: "",
        batchNo: "",
        expiryDate: "",
        orderQty: "",
        qtyIssued: "",
      },
    ]);
    history.push("/Dashboard");
  }

  let patDob = moment(new Date(patDetails?.Patient?.dob)).toISOString();
  let patEnDate = moment(new Date(patDetails?.reservationTime)).toISOString();
  var currentdate = moment(new Date()).toISOString();
  const [dispOrderId, setDispOrderId] = useState("");

  function SubmitDispence(e) {
    let tempArray = [];
    let isFound;
    for (let i = 0; i < inputList.length; i++) {
      tempArray.push(inputList[i]);

      isFound = tempArray.some((element) => {
        if (
          !element.productName ||
          !element.batchNo ||
          !element.expiryDate ||
          !element.qtyIssued ||
          Number(element.qtyIssued) > Number(element.avaqty)
        ) {
          return true;
        }
        return false;
      });
    }

    if (isFound) {
      alert(
        "Please fill all fields correctly / Issued qunatity should be smaller than available quantity...!"
      );
    } else {
      var subData = {
        patient: {
          patientId: patDetails?.Patient?.patientId,
          memberId: "",
          healthId: patDetails?.Patient?.healthId,
          uhId: patDetails?.Patient?.UHId,
          name: patDetails?.Patient?.name,
          gender: patDetails?.Patient?.gender,
          dob: patDob,
          phone: patDetails?.Patient?.phone,
        },
        status: "REGISTERED",
        encounter: {
          encounterId: patDetails?.encounterId,
          encounterDateTime: patEnDate,
          staffId: patDetails?.Provider?.memberId,
          staffName: patDetails?.Provider?.name,
          prescriptionId: "",
        },
        items: ordItem,
        orderDate: currentdate,
        originatedBy: patDetails?.Provider?.name,
        registeredDate: patEnDate,
      };
      var postResponce = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(subData),
      };

      fetch(
        `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders`,
        postResponce
      )
        .then((resp) => resp.json())
        .then((resp) => {
          if (resp.error) {
          } else {
            setDispOrderId(resp.id);
            let orderId = resp.id;
            const drugsData = {
              orderId: orderId,
              items: inputList,
              orderDate: moment(new Date()).toISOString(),
              deliveredDate: moment(new Date()).toISOString(),
            };
            var requestOptions = {
              headers: serviceHeaders.myHeaders1,
              method: "POST",
              mode: "cors",
              body: JSON.stringify(drugsData),
            };
            fetch(
              `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/${orderId}/dispenses`,
              requestOptions
            )
              .then((resp) => resp.json())
              .then((resp) => {
                for (var i = 0; i < inputList.length; i++) {
                  for (var j = 0; j < inventId.length; j++) {
                    if (
                      inputList[i].productId == inventId[j].inventProductId &&
                      inputList[i].batchNo == inventId[j].batchNo
                    ) {
                      let qntCount =
                        inputList[i].avaqty - inputList[i].qtyIssued;

                      const updateInvet = {
                        level: qntCount,
                      };
                      var requestOptionsInvent = {
                        headers: serviceHeaders.myHeaders1,
                        method: "PATCH",
                        mode: "cors",
                        body: JSON.stringify(updateInvet),
                      };

                      fetch(
                        `${constant.ApiUrl}/product-svc/products/${inventId[j].inventProductId}/inventories/${inventId[j].inventoryId}`,
                        requestOptionsInvent
                      )
                        .then((resp) => resp.json())
                        .then((resp) => {});
                    }
                  }
                }
                setInventId([]);
                setLoading1(true);
              });
          }
        });
    }
  }

  // let updateArray = [...itemArray, ...ordItem]

  function SubmitDash() {
    let tempArray = [];
    let isFound;
    for (let i = 0; i < inputList.length; i++) {
      tempArray.push(inputList[i]);

      isFound = tempArray.some((element) => {
        if (
          !element.productName ||
          !element.batchNo ||
          !element.expiryDate ||
          (element.penQnty != 0 && !element.qtyIssued) ||
          Number(element.qtyIssued) > Number(element.avaqty)
        ) {
          return true;
        }
        return false;
      });
    }

    if (isFound) {
      alert(
        "Please fill all fields correctly / Issued qunatity should be smaller than available quantity...!"
      );
    } else {
      const drugsData = {
        orderId: pharmaOrdId,
        items: inputList,
        orderDate: moment(new Date()).toISOString(),
        deliveredDate: moment(new Date()).toISOString(),
      };
      var requestOptions = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(drugsData),
      };

      fetch(
        `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/${pharmaOrdId}/dispenses`,
        requestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          for (var i = 0; i < inputList.length; i++) {
            for (var j = 0; j < inventId.length; j++) {
              if (
                inputList[i].productId == inventId[j].inventProductId &&
                inputList[i].batchNo == inventId[j].batchNo
              ) {
                let qntCount = inputList[i].avaqty - inputList[i].qtyIssued;

                const updateInvet = {
                  level: qntCount,
                };
                var requestOptionsInvent = {
                  headers: serviceHeaders.myHeaders1,
                  method: "PATCH",
                  mode: "cors",
                  body: JSON.stringify(updateInvet),
                };
                fetch(
                  `${constant.ApiUrl}/product-svc/products/${inventId[j].inventProductId}/inventories/${inventId[j].inventoryId}`,
                  requestOptionsInvent
                )
                  .then((resp) => resp.json())
                  .then((resp) => {
                    alert("diducted for" + " " + inventId[j].inventoryId);
                  });
              }
            }
          }
          setInventId([]);
          setLoading1(true);
        });
    }
  }

  // previous history modal
  const [prescriptionDataShow, setPrescriptionShow] = useState(false);
  const prescriptionClose = () => setPrescriptionShow(false);
  const prescriptionShow = () => setPrescriptionShow(true);
  // previous history modal

  // get future date
  function getFormattedDate(date) {
    let monthNames = [
      "Jan",
      "Feb",
      "Mar",
      "Apr",
      "May",
      "Jun",
      "Jul",
      "Aug",
      "Sep",
      "Oct",
      "Nov",
      "Dec",
    ];
    let day = date.getDate();
    let month = monthNames[date.getMonth()];
    let year = date.getFullYear();
    return `${day} ${month} ${year}`;
  }
  function addDaysToDate(date, daysToAdd) {
    let newDate = new Date(date);
    newDate.setDate(date.getDate() + daysToAdd);
    return newDate;
  }
  let thirtyDaysAfterToday = getFormattedDate(addDaysToDate(new Date(), 12));
  // get future date

  // days between dates
  const dateB = moment(new Date()).format("YYYY-MM-DD");
  const dateC = moment(patDetails?.Patient?.dob).format("YYYY-MM-DD");

  const phc = sessionStorage.getItem("phc");
  const [prescGetData, setPrescGetData] = useState([]);
  useEffect(() => {
    if (
      orderDetails.encounter?.encounterId != "" &&
      orderDetails.encounter?.encounterId != undefined
    ) {
      fetch(
        `${constant.ApiUrl}/prescriptions/filter?page=&size=&encounterId=` +
          orderDetails.encounter?.encounterId,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setPrescGetData(res["data"]);
        });
    }
  }, [orderDetails]);

  return (
    <>
      {isLoading1 == true && (
        <div className="loder-container" align="center">
          <div className="loadBox" align="center">
            <div>
              <img
                className="thumbnail pharmacy-loader-img"
                src="../img/pharmacy/005.png"
              />
            </div>
            <div>
              <h1 className="loader-head">Order Dispensed Successfully.</h1>
            </div>
            <div>
              <h1 className="loader-order-id">
                Order Id :{" "}
                {pharmaOrdId ? <>{pharmaOrdId}</> : <>{dispOrderId}</>}
              </h1>
            </div>
            <div className="okay-btn-row">
              <Button
                className="orderSubmit btn btn-primary"
                onClick={loderCancel}
              >
                Okay
              </Button>
            </div>
          </div>
        </div>
      )}
      <Row className="main-div ">
        <Col lg={2} className="side-bar">
          <Sidemenu />
        </Col>
        <Col lg={10} md={12} sm={12} xs={12}>
          {pharmaOrdId && orderDetails.status == "ORDERED" ? (
            <PrescriptionModal
              descshow={prescriptionDataShow}
              descClose={prescriptionClose}
              phc={phc}
              docName={orderDetails.encounter.staffName}
              EffectiveDate={orderDetails.orderDate}
              PatName={orderDetails?.patient?.name}
              UHID={orderDetails?.patient?.uhId}
              PatHeaId={orderDetails?.patient?.healthId}
              PatDob={orderDetails?.patient?.dob}
              PatMob={orderDetails?.patient?.phone}
              prescGetData={prescGetData}
            />
          ) : (
            <ViewModalPopups
              chiefClose={prescriptionClose}
              cheifShow={prescriptionDataShow}
              PatName={patDetails?.Patient?.name}
              prescriptionUHID={patDetails?.Patient?.UHId}
              PatHeaId={patDetails?.Patient?.healthId}
              PatGen={patDetails?.Patient?.gender}
              PatDob={patDob}
              PatMob={patDetails?.Patient?.phone}
              medicalEncounterId={patDetails?.encounterId}
              dateto={dateto}
              datefrom={datefrom}
            />
          )}

          <MedqueModal
            medqueModal={medqueModal}
            medqueModalClose={medqueModalClose}
          />
          <LabScreen
            setInputList={setInputList}
            formRemoveClick={formRemoveClick}
            setOrdItem={setOrdItem}
          />
          {pharmaOrdId == null || orderDetails.status == "REGISTERED" ? (
            <div className="dispence-div">
              <Row className="pre-pharma">
                <Col md={6}>
                  <Row className="pharma-med-details">
                    <Col md={4}>
                      <p className="order-details">
                        <b>Visit Date & Time</b>
                      </p>
                    </Col>
                    <Col md={8}>
                      <p className="order-ids">
                        {moment(new Date(orderDetails.orderDate)).format(
                          "DD MMM YYYY, hh:mm A"
                        )}
                      </p>
                    </Col>
                  </Row>
                </Col>
                <Col md={6} align="right">
                  <Button
                    className="btn btn-light pharma-prescription-btn"
                    onClick={prescriptionShow}
                  >
                    <i className="fa fa-file-text"></i> &nbsp; View Prescription
                  </Button>
                </Col>
              </Row>
              <Form ref={form}>
                <Paper
                  sx={{ width: "100%", overflow: "hidden" }}
                  className="order-supply"
                >
                  <TableContainer sx={{ maxHeight: 440 }}>
                    <Table stickyHeader aria-label="sticky table">
                      <FormTableHeader tableHeader={medicineHeader1} />
                      <TableBody>
                        {inputList.map((x, i) => {
                          return (
                            <TableRow key={i}>
                              <TableCell className="pro-div">
                                <Form.Group className="mb-3_drugname">
                                  <Autocomplete
                                    classes={{
                                      inputRoot: classes.inputRoot,
                                      paper: classes.popper,
                                    }}
                                    value={x.productName || ""}
                                    name="productName"
                                    onChange={(e, newValue) => {
                                      handleInputChange(e, i, newValue);
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
                                        {...params}
                                        InputProps={{
                                          ...params.InputProps,
                                          className: classes.inputRoot,
                                          // onFocus: () => {
                                          //   params.InputProps.onFocus();
                                          //   params.InputProps.style = {
                                          //     ...params.InputProps.style,
                                          //     fontSize: "12px"
                                          //   };
                                          // }
                                          onFocus: (event) => {
                                            // Customize styles when focused
                                            if (params.InputProps.onFocus) {
                                              params.InputProps.onFocus(event);
                                            }
                                            // Add additional styles here
                                            // For example, set border color to blue when focused
                                            event.target.style.fontSize =
                                              "12px";
                                          },
                                        }}
                                        label="Enter Product Name"
                                      />
                                    )}
                                  />
                                </Form.Group>
                              </TableCell>
                              <TableCell>
                                <Form.Group className="mb-3_drugname">
                                  <Form.Select
                                    aria-label="Default select example"
                                    value={x.batchNo || ""}
                                    className="med-dropdown"
                                    name="batchNo"
                                    onChange={(e) => handleInputChange(e, i)}
                                    disabled={!x.productName}
                                  >
                                    <option value="" disabled hidden>
                                      Select
                                    </option>
                                    {inventory.map((inventory, i) => (
                                      <React.Fragment key={i}>
                                        {inventory.productId == x.productId && (
                                          <option value={inventory.batchNumber}>
                                            {inventory.batchNumber}
                                          </option>
                                        )}
                                      </React.Fragment>
                                    ))}
                                  </Form.Select>
                                </Form.Group>
                              </TableCell>
                              <TableCell>
                                <Form.Group className="mb-3_days">
                                  <Form.Control
                                    className="presription-input"
                                    type="text"
                                    value={x.expiryDate || ""}
                                    name="expiryDate"
                                    disabled
                                    placeholder="Expire Date"
                                    // min={disablePastDt()}
                                    onChange={(e) => handleInputChange(e, i)}
                                  />
                                  {/* }
                                                                            </React.Fragment>
                                                                        ))} */}
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
                                    placeholder="Enter Available Qty"
                                    value={x.avaqty || ""}
                                    name="avaqty"
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
                                    placeholder="Enter Qty Issued"
                                    value={x.qtyIssued || ""}
                                    name="qtyIssued"
                                    onChange={(e) => handleInputChange(e, i)}
                                  />
                                </Form.Group>
                              </TableCell>
                              <TableCell align="center">
                                {inputList.length == 1 ? (
                                  <p
                                    className="medact btn btn-link"
                                    style={{ opacity: "0.6" }}
                                  >
                                    <i className="bi bi-trash"></i>
                                  </p>
                                ) : (
                                  <p
                                    className="medact btn btn-link"
                                    onClick={() => handleRemoveClick(i)}
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
                  </TableContainer>
                </Paper>
              </Form>
            </div>
          ) : (
            <div className="dispence-div">
              <Row className="pre-pharma">
                <Col md={4}>
                  <Row>
                    <Col md={4}>
                      <p className="order-details">
                        <b>Order Id:</b>
                      </p>
                      <p className="order-details">
                        <b>
                          {orderDetails.status == "REGISTERED"
                            ? "Pharmacist:"
                            : "Medical Officer:"}
                        </b>
                      </p>
                    </Col>
                    <Col md={8}>
                      <p className="order-ids">{pharmaOrdId}</p>
                      <p className="order-ids">
                        {orderDetails.encounter?.staffName}
                      </p>
                    </Col>
                  </Row>
                </Col>
                <Col md={4}>
                  <Row>
                    <Col md={4}>
                      <p className="order-details">
                        <b>Status:</b>
                      </p>
                      <p className="order-details">
                        <b>Date & Time:</b>
                      </p>
                    </Col>
                    <Col md={8}>
                      <p className="order-ids">{orderDetails.status}</p>
                      <p className="order-ids">
                        {moment(new Date(orderDetails.orderDate)).format(
                          "DD MMM YYYY, hh:mm A"
                        )}
                      </p>
                    </Col>
                  </Row>
                </Col>
                <Col md={4} align="right">
                  <Button
                    className="btn btn-light pharma-prescription-btn"
                    onClick={prescriptionShow}
                  >
                    <i className="fa fa-file-text"></i> &nbsp; View Prescription
                  </Button>
                </Col>
              </Row>

              <Paper
                sx={{ width: "100%", overflow: "hidden" }}
                className="order-supply"
              >
                <TableContainer sx={{ maxHeight: 440 }}>
                  <Table stickyHeader aria-label="sticky table">
                    <FormTableHeader tableHeader={medicineHeader2} />
                    <TableBody>
                      {inputList.map((x, i) => {
                        return (
                          <TableRow key={i}>
                            <TableCell className="pro-div">
                              <Form.Group className="mb-3_drugname">
                                <Autocomplete
                                  classes={{
                                    inputRoot: classes.inputRoot,
                                    paper: classes.popper,
                                  }}
                                  value={x.productName || ""}
                                  name="name"
                                  onChange={(e, newValue) => {
                                    handleInputChange(e, i, newValue);
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
                                      {...params}
                                      InputProps={{
                                        ...params.InputProps,
                                        className: classes.inputRoot,
                                        onFocus: () => {
                                          params.InputProps.onFocus();
                                          params.InputProps.style = {
                                            ...params.InputProps.style,
                                            fontSize: "12px",
                                          };
                                        },
                                      }}
                                      label="Enter Product Name"
                                    />
                                  )}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell className="medicine-drop">
                              <Form.Group className="mb-3_drugname">
                                <Form.Select
                                  aria-label="Default select example"
                                  value={x.batchNo || ""}
                                  className="med-dropdown"
                                  name="batchNo"
                                  onChange={(e) => handleInputChange(e, i)}
                                >
                                  <option value="" disabled hidden>
                                    Select
                                  </option>
                                  {inventory.map((inventory, i) => (
                                    <React.Fragment key={i}>
                                      {inventory.productId == x.productId && (
                                        <option value={inventory.batchNumber}>
                                          {inventory.batchNumber}
                                        </option>
                                      )}
                                    </React.Fragment>
                                  ))}
                                </Form.Select>
                              </Form.Group>
                            </TableCell>
                            <TableCell>
                              <Form.Group>
                                <Form.Control
                                  className="presription-input"
                                  type="date"
                                  value={x.expiryDate || ""}
                                  name="expiryDate"
                                  disabled
                                  min={disablePastDt()}
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
                                  placeholder="Enter Available Qty"
                                  value={x.avaqty}
                                  name="avaqty"
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
                                  placeholder="Enter Ordered Qty"
                                  value={x.orderQty}
                                  name="orderQty"
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
                                  placeholder="Enter Qty Issued"
                                  value={x.qtyIssued}
                                  name="qtyIssued"
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
                                  placeholder="Enter Qty to be issued"
                                  value={x.penQnty}
                                  name="penQnty"
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </TableCell>
                            <TableCell align="center">
                              {inputList.length == 1 ? (
                                <p
                                  className="medact btn btn-link"
                                  style={{ opacity: "0.6" }}
                                >
                                  <i className="bi bi-trash"></i>
                                </p>
                              ) : (
                                <p
                                  className="medact btn btn-link"
                                  onClick={() => handleRemoveClick(i)}
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
                </TableContainer>
              </Paper>
            </div>
          )}
          <div>
            <Row className="medical-drug-row">
              <Col md={2} align="center">
                <Button
                  type="button"
                  className="OrderCancel btn btn-primary"
                  onClick={calcelField}
                >
                  Cancel
                </Button>
              </Col>
              <Col md={8}>
                <h1 id="ahead"></h1>
              </Col>
              <Col md={2} align="center">
                {!pharmaOrdId ? (
                  <Button
                    type="button"
                    className="orderSubmit btn btn-primary"
                    onClick={SubmitDispence}
                  >
                    Submit
                  </Button>
                ) : (
                  <Button
                    type="button"
                    className="orderSubmit btn btn-primary"
                    onClick={SubmitDash}
                  >
                    Submit
                  </Button>
                )}
              </Col>
            </Row>
          </div>
        </Col>
      </Row>
    </>
  );
}

export default Medque;
