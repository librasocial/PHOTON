import React, { useState, useEffect } from "react";
import { Button, Row, Col, Form } from "react-bootstrap";
import AddCircleRoundedIcon from "@mui/icons-material/AddCircleRounded";
import moment from "moment";
import "../../../css/Services.css";
import "../PharmacyTab.css";
import Sidemenu from "../../Sidemenus";
import { Link } from "react-router-dom";
import PurchaseOrder from "./PurchaseOrder";
import BillPurchaseOrder from "./BillPurchaseOrder";
import IndentOrder from "./IndentOrder";
import { useDispatch, useSelector } from "react-redux";
import {
  loadOrdersList,
  loadOrdersBySearch,
  loadOrdersBySTS,
  loadOrdersListByStatus,
  loadOrdersBySearchMo,
} from "../../../redux/actions";
import SupplierName from "./Filter/SupplierName";
import OrderType from "./Filter/OrderType";
import StatusFilter from "./Filter/StatusFilter";
import { useLocalStore } from "mobx-react-lite";
import TextField from "@mui/material/TextField";
import Autocomplete, { createFilterOptions } from "@mui/material/Autocomplete";
import Loader from "../Dashboard/Loader";
import PharmacyTable from "../pharmacyTable/PharmacyTable";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  popper: {
    // Your custom styles for the dropdown window
    zIndex: 10110, // Adjust the z-index value as needed
    position: "absolute",
    fontSize: "12px",
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

function Orders(props) {
  const classes = useStyles();

  let dispatch = useDispatch();
  const { PurchaseOrderList } = useSelector((state) => state.data);
  const { OrderListByStatus } = useSelector((state) => state.data);
  const [orderLists, setOrderLists] = useState([]);
  useEffect(() => {
    let orderArray = [];
    if (props.pageName === "Approvels") {
      orderArray = OrderListByStatus;
    } else {
      orderArray = PurchaseOrderList;
    }
    setOrderLists(orderArray);
  }, [PurchaseOrderList, OrderListByStatus, props.pageName]);

  let typeofuser = sessionStorage.getItem("typeofuser");
  let poUser = sessionStorage.getItem("poUser");
  let InUser = sessionStorage.getItem("InUser");

  const { PharmaProdLength } = useSelector((state) => state.data);
  let Count = parseInt(PharmaProdLength);

  const [searchName, setSearchName] = useState("");
  const [selectStDate, setSelectStDate] = useState("");
  const [selectEdDate, setSelectEdDate] = useState("");

  let tableHeader;
  if (typeofuser === "Pharmacist") {
    tableHeader = [
      { class: "class3", name: "S.I. No.", type: "type" },
      { class: "class4", name: "Indent / PO Number", type: "type" },
      { class: "class1", name: "Indent / PO Date", type: "type" },
      { class: "class4", name: "Supplier Name", type: "group" },
      { class: "class1", name: "Order Type", type: "group" },
      { class: "class3", name: "Status", type: "group" },
      { class: "class1", name: "Action", type: "type" },
    ];
  } else {
    tableHeader = [
      { class: "class3", name: "S.I. No.", type: "type" },
      { class: "class4", name: "Indent / PO Number", type: "type" },
      { class: "class1", name: "Indent / PO Date", type: "type" },
      { class: "class4", name: "Supplier Name", type: "group" },
      { class: "class1", name: "Raised By", type: "type" },
      { class: "class1", name: "Order Type", type: "group" },
      { class: "class3", name: "Status", type: "group" },
      { class: "class1", name: "Action", type: "type" },
    ];
  }

  const state = {
    items: [
      {
        id: "01",
        label: "District / Taluk Health Office",
        add: "Karnataka, Bangalore",
      },
      {
        id: "02",
        label: "Drug Warehouse Logistics Society",
        add: "Karnataka, Bangalore",
      },
      {
        id: "03",
        label: "KT Office(Jan Aushadhi Kendra)",
        add: "Karnataka, Bangalore",
      },
      {
        id: "04",
        label: "Other",
        add: "Karnataka, Bangalore",
      },
      {
        id: "05",
        label: "Vaccine Store",
        add: "Karnataka, Bangalore",
      },
    ],
    items1: [
      {
        id: "01",
        label: "Purchase Order",
      },
      {
        id: "02",
        label: "Indent",
      },
    ],
    items2: [
      {
        id: "01",
        label: "DRAFT",
      },
      {
        id: "02",
        label: "CREATED",
      },
      {
        id: "03",
        label: "AUTHORISED",
      },
      {
        id: "04",
        label: "COMPLETED",
      },
    ],
  };

  const [searchplaceholder, setSearchplaceholder] = useState(
    "Enter name to search"
  );
  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const disableFutureDt = (current) => {
    return current.isBefore(today);
  };

  const [searchvalue, setSearchvalue] = useState("");

  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const [statusValue, setStatusValue] = useState([]);
  const [suplierName, setSuplierName] = useState([]);
  const [selectedType, setSelectedType] = useState([]);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const [pageChange, setPageChange] = useState(false);
  const PurchaseOrderState = () => {
    setPageChange(true);
  };

  const [pageChange1, setPageChange1] = useState(false);
  const IndentOrderState = () => {
    setPageChange1(true);
  };
  const handleSupplier = (newValue) => {
    if (newValue) {
      setSearchName(newValue.name);
    }
  };

  const searchOeders = () => {
    {
      typeofuser === "Pharmacist"
        ? dispatch(
            loadOrdersBySearch(
              searchName,
              selectStDate,
              selectEdDate,
              page,
              rowsPerPage
            )
          )
        : dispatch(
            loadOrdersBySearchMo(
              searchName,
              selectStDate,
              selectEdDate,
              page,
              rowsPerPage
            )
          );
    }
  };

  const resetForm = () => {
    setSearchName(""), setSelectStDate(""), setSelectEdDate("");
  };

  useEffect(() => {
    const interval = setInterval(() => {
      {
        typeofuser === "Pharmacist"
          ? (document.title = "EMR Pharmacy Orders")
          : (document.title = "EMR Medical Officer Approvals");
      }
      if (
        !searchName &&
        !selectStDate &&
        !selectEdDate &&
        suplierName.length == 0 &&
        statusValue.length == 0 &&
        selectedType.length == 0
      ) {
        {
          typeofuser === "Pharmacist"
            ? dispatch(loadOrdersList(page, rowsPerPage))
            : dispatch(loadOrdersListByStatus(page, rowsPerPage));
        }
      } else {
        {
          typeofuser === "Pharmacist" &&
            dispatch(
              loadOrdersBySTS(
                suplierName,
                statusValue,
                selectedType,
                page,
                rowsPerPage
              )
            );
        }
      }
    }, 1000);
    return () => clearInterval(interval);
  }, [
    page,
    rowsPerPage,
    searchName,
    selectStDate,
    selectEdDate,
    suplierName,
    statusValue,
    selectedType,
  ]);

  const [orderId, setOrderId] = useState("");
  const [indentId, setIndentId] = useState("");
  const [viewPage, setViewPage] = useState(false);

  const filter = createFilterOptions();
  let suplerName = [
    {
      id: "01",
      name: "District / Taluk Health Office",
    },
    {
      id: "02",
      name: "Drug Warehouse Logistics Society",
    },
    {
      id: "03",
      name: "KT Office(Jan Aushadhi Kendra)",
    },
    {
      id: "04",
      name: "Other",
    },
    {
      id: "05",
      name: "Vaccine Store",
    },
  ];

  const [approvalLoading, setApprovalLoading] = useState(false);
  // const addApproval = () => {
  //     setApprovalLoading(true)
  // }
  function approvalLoadingClose() {
    setApprovalLoading(false);
  }

  return (
    <Row className="main-div">
      <Col lg={2} className="side-bar">
        <Sidemenu />
      </Col>
      <Col lg={10} md={12} sm={12} xs={12}>
        <Loader
          orderId={orderId}
          approvalLoadingClose={approvalLoadingClose}
          approvalLoading={approvalLoading}
        />
        {viewPage == true ? (
          <BillPurchaseOrder
            setViewPage={setViewPage}
            setPage={setPage}
            orderId={orderId}
          />
        ) : (
          <>
            {pageChange == true || poUser == "medical-Officer" ? (
              <PurchaseOrder
                setPageChange={setPageChange}
                orderId={orderId}
                setOrderId={setOrderId}
                setPage={setPage}
                page={page}
                setRowsPerPage={setRowsPerPage}
                rowsPerPage={rowsPerPage}
              />
            ) : "" || pageChange1 == true || InUser == "medical-Officer" ? (
              <IndentOrder
                setPageChange1={setPageChange1}
                indentId={indentId}
                setIndentId={setIndentId}
                setPage={setPage}
                page={page}
                setRowsPerPage={setRowsPerPage}
                rowsPerPage={rowsPerPage}
              />
            ) : (
              <div>
                <div className="regHeader">
                  <h1 className="register-Header">
                    {typeofuser === "Pharmacist" ? "Orders" : "Approvals"}
                  </h1>
                </div>
                <hr style={{ margin: "0px" }} />
                <div className="pro-tab">
                  <div className="searchFilter-div">
                    <Row className="search-row">
                      <Col md={4} className="search-select">
                        {/* <div className="form-group ord-search">
                                                <input type="text" className="form-control " placeholder="Search Supplier..."
                                                    name="search"
                                                    value={searchName} onChange={(e) => setSearchName(e.target.value)}
                                                />
                                                <span className="fa fa-search form-control-ord-feedback"></span>
                                            </div> */}
                        <Form.Group className="mb-3_drugname">
                          <Autocomplete
                            classes={{
                              inputRoot: classes.inputRoot,
                              paper: classes.popper,
                            }}
                            value={searchName || ""}
                            name="searchName"
                            onChange={(e, newValue) => {
                              handleSupplier(newValue);
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
                            options={suplerName}
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
                                    if (params.InputProps.onFocus) {
                                      params.InputProps.onFocus(event);
                                    }
                                    event.target.style.fontSize = "12px";
                                  },
                                }}
                                label="Search Supplier.."
                              />
                            )}
                          />
                        </Form.Group>
                      </Col>
                      <Col md={4}>
                        <Row className="ourdate">
                          <Col md={6}>
                            <div className="search-date">
                              <input
                                className="form-control "
                                type="date"
                                key="random1"
                                pattern="/^(0?[1-9]|1[0-2])[\/](0?[1-9]|[1-2][0-9]|3[01])[\/]\d{4}$/"
                                id="pharma-input"
                                name="stDate"
                                value={selectStDate}
                                onChange={(e) =>
                                  setSelectStDate(
                                    moment(new Date(e.target.value)).format(
                                      "YYYY-MM-DD"
                                    )
                                  )
                                }
                                placeholder={searchplaceholder}
                                max={currentdate}
                                selected={searchvalue}
                                validate={[disableFutureDt]}
                              />
                            </div>
                          </Col>
                          <Col md={6}>
                            <div className="search-date">
                              <input
                                className="form-control"
                                type="date"
                                key="random1"
                                pattern="/^(0?[1-9]|1[0-2])[\/](0?[1-9]|[1-2][0-9]|3[01])[\/]\d{4}$/"
                                id="pharma-input"
                                name="edDate"
                                value={selectEdDate}
                                onChange={(e) =>
                                  setSelectEdDate(
                                    moment(new Date(e.target.value)).format(
                                      "YYYY-MM-DD"
                                    )
                                  )
                                }
                                placeholder={searchplaceholder}
                                max={currentdate}
                                selected={searchvalue}
                                validate={[disableFutureDt]}
                              />
                            </div>
                          </Col>
                        </Row>
                      </Col>
                      <Col md={4}>
                        <Row>
                          <Col md={6} className="d-flex justify-content-center">
                            <Button
                              type="button"
                              className="orderBtn btn btn-secondary"
                              onClick={searchOeders}
                            >
                              Search
                            </Button>
                            <Button
                              type="button"
                              className="linkBtn btn btn-link"
                              onClick={resetForm}
                            >
                              Clear
                            </Button>
                          </Col>
                          <Col md={1} className="divider1">
                            {typeofuser === "Pharmacist" ? (
                              <div className="col-container">
                                <div className="line" align="center"></div>
                              </div>
                            ) : (
                              ""
                            )}
                          </Col>
                          <Col md={5}>
                            {typeofuser === "Pharmacist" ? (
                              <div className="dropdown">
                                <button
                                  className="btn btn-secondary dropdown-toggle create-btn"
                                  type="button"
                                  data-bs-toggle="dropdown"
                                  aria-expanded="false"
                                >
                                  <AddCircleRoundedIcon className="my-plus" />
                                  &nbsp; Create New &nbsp;
                                </button>
                                <ul className="dropdown-menu dropdown-menu-dark my-menu">
                                  <li>
                                    <Button
                                      variant="outline-secondary"
                                      className="my-menuitem"
                                      onClick={PurchaseOrderState}
                                    >
                                      Purchase Order
                                    </Button>
                                  </li>
                                  <li>
                                    <Button
                                      variant="outline-secondary"
                                      className="my-menuitem"
                                      onClick={IndentOrderState}
                                    >
                                      Indent
                                    </Button>
                                  </li>
                                </ul>
                              </div>
                            ) : (
                              ""
                            )}
                          </Col>
                        </Row>
                      </Col>
                    </Row>
                  </div>
                  <PharmacyTable
                    PurchaseOrderList={orderLists}
                    tableHeader={tableHeader}
                    supplierItem={state.items}
                    setSuplierName={setSuplierName}
                    suplierName={suplierName}
                    supplierItem1={state.items1}
                    setSelectedType={setSelectedType}
                    selectedType={selectedType}
                    supplierItem2={state.items2}
                    setStatusValue={setStatusValue}
                    statusValue={statusValue}
                    setOrderId={setOrderId}
                    setPageChange1={setPageChange1}
                    setIndentId={setIndentId}
                    setViewPage={setViewPage}
                    pageType="Orders"
                    Count={Count}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    setPageChange={setPageChange}
                    setApprovalLoading={setApprovalLoading}
                    handleChangePage={handleChangePage}
                    handleChangeRowsPerPage={handleChangeRowsPerPage}
                  />
                </div>
              </div>
            )}
          </>
        )}
      </Col>
    </Row>
  );
}

export default Orders;
