import React, { useState, useEffect, useRef } from "react";
import { Button, Row, Col, Form } from "react-bootstrap";
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
import moment from "moment";
import Sidemenu from "../../Sidemenus";
import NewInwardsOrder from "./NewInwardsOrder";
import InwardsBillPurchase from "./InwardsBillPurchase/InwardsBillPurchase";
import "../PharmacyTab.css";
import "./Inwards.css";
import { useDispatch, useSelector } from "react-redux";
import {
  loadInwards,
  loadOrdersList,
  loadInwardsById,
  loadInwardsBySearch,
  loadInwardsBySTS,
} from "../../../redux/actions";
import SupplierName from "../Order/Filter/SupplierName";
import { useLocalStore } from "mobx-react-lite";
import OrderType from "../Order/Filter/OrderType";
import StatusFilter from "../Order/Filter/StatusFilter";
import TextField from "@mui/material/TextField";
import Autocomplete, { createFilterOptions } from "@mui/material/Autocomplete";
import PharmacyTable from "../pharmacyTable/PharmacyTable";
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

function Inwards(props) {
  const classes = useStyles();

  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const { InwardsList } = useSelector((state) => state.data);
  const { PurchaseOrderList } = useSelector((state) => state.data);
  const { PharmaInwardLength } = useSelector((state) => state.data);
  let Count = parseInt(PharmaInwardLength);

  let dispatch = useDispatch();

  const [searchplaceholder, setSearchplaceholder] = useState(
    "Enter name to search"
  );
  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const disableFutureDt = (current) => {
    return current.isBefore(today);
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const [pageChange, setPageChange] = useState(false);
  const NewInwardsBillState = () => {
    setPageChange(true);
  };

  const [pageChange3, setPageChange3] = useState(false);
  const NewInwardsOrderState = () => {
    setPageChange3(true);
  };

  const [inwardsId, setInwardsId] = useState("");
  const [editStatus, setEditStatus] = useState(true);
  // const editInwards = (e) => {
  //     setInwardsId(e);
  //     dispatch(loadInwardsById(e))
  //     setPageChange3(true);
  // }

  // filter by name
  const [searchName, setSearchName] = useState("");
  const [selectStDate, setSelectStDate] = useState("");
  const [selectEdDate, setSelectEdDate] = useState("");

  const searchOeders = () => {
    dispatch(
      loadInwardsBySearch(
        searchName,
        selectStDate,
        selectEdDate,
        page,
        rowsPerPage
      )
    );
  };

  const resetForm = () => {
    setSearchName(""), setSelectStDate(""), setSelectEdDate("");
  };
  // filter by name
  // filter by supplier, status, type

  let tableHeader = [
    { class: "class3", name: "S.I. No.", type: "type" },
    { class: "class4", name: "Inwards Number", type: "type" },
    { class: "class4", name: "Indent / PO Type", type: "type" },
    { class: "class1", name: "Inwards Date", type: "type" },
    { class: "class5", name: "Supplier Name", type: "group" },
    { class: "class2", name: "Inwards Type", type: "group" },
    { class: "class2", name: "Inwards Status", type: "group" },
    { class: "class1", name: "Invoice Number", type: "type" },
    { class: "class1", name: "Action", type: "type" },
  ];

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
        label: "PO",
      },
      {
        id: "02",
        label: "Indent",
      },
      {
        id: "02",
        label: "Direct Inwards",
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
    ],
  };

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

  const [suplierName, setSuplierName] = useState([]);
  const [statusValue, setStatusValue] = useState([]);
  const [selectedType, setSelectedType] = useState([]);

  const filter = createFilterOptions();
  const handleSupplier = (newValue) => {
    if (newValue) {
      setSearchName(newValue.name);
    }
  };

  useEffect(() => {
    document.title = "EMR Inwards";
    if (
      !searchName &&
      !selectStDate &&
      !selectEdDate &&
      suplierName.length == 0 &&
      statusValue.length == 0 &&
      selectedType.length == 0
    ) {
      dispatch(loadInwards(page, rowsPerPage));
    } else {
      dispatch(
        loadInwardsBySTS(
          suplierName,
          statusValue,
          selectedType,
          page,
          rowsPerPage
        )
      );
    }
    dispatch(loadOrdersList(0, 10000000));
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

  return (
    <React.Fragment>
      <Row className="main-div">
        <Col lg={2} className="side-bar">
          <Sidemenu />
        </Col>
        <Col lg={10} md={12} sm={12} xs={12}>
          {pageChange == true ? (
            <InwardsBillPurchase setPageChange={setPageChange} />
          ) : "" || pageChange3 == true ? (
            <NewInwardsOrder
              editStatus={editStatus}
              setPageChange3={setPageChange3}
              inwardsId={inwardsId}
              setInwardsId={setInwardsId}
              setPage={setPage}
              page={page}
              setRowsPerPage={setRowsPerPage}
              rowsPerPage={rowsPerPage}
            />
          ) : (
            <div>
              <div className="regHeader">
                <h1 className="register-Header">Inwards</h1>
              </div>
              <hr style={{ margin: "0px" }} />
              <div className="pro-tab">
                <div className="searchFilter-div">
                  <Row className="search-row">
                    <Col md={4} className="search-select">
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
                                  // Customize styles when focused
                                  if (params.InputProps.onFocus) {
                                    params.InputProps.onFocus(event);
                                  }
                                  // Add additional styles here
                                  // For example, set border color to blue when focused
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
                          <div className="col-container">
                            <div className="line" align="center"></div>
                          </div>
                          {/* <div className="my-vertline"></div> */}
                        </Col>
                        <Col md={5}>
                          <div className="dropdown">
                            <button
                              className="btn btn-secondary  create-btn"
                              type="button"
                              onClick={NewInwardsOrderState}
                            >
                              <AddCircleRoundedIcon className="my-plus" />
                              &nbsp; New Inwards &nbsp;
                            </button>
                          </div>
                        </Col>
                      </Row>
                    </Col>
                  </Row>
                </div>
                <PharmacyTable
                  PurchaseOrderList={InwardsList}
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
                  setPageChange1={setPageChange3}
                  setInwardsId={setInwardsId}
                  Count={Count}
                  rowsPerPage={rowsPerPage}
                  page={page}
                  setPageChange={setPageChange}
                  pageType="Inwards"
                  handleChangePage={handleChangePage}
                  handleChangeRowsPerPage={handleChangeRowsPerPage}
                />
              </div>
            </div>
          )}
        </Col>
      </Row>
    </React.Fragment>
  );
}

export default Inwards;
