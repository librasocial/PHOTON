import React, { useEffect, useState } from "react";
import { Col, Form, Image, Row } from "react-bootstrap";
import { TextField } from "@mui/material";
import Autocomplete, { createFilterOptions } from "@mui/material/Autocomplete";
import Sidemenu from "../Sidemenus";
import "../../css/Services.css";
import "../../css/Notifications.css";
import { Link } from "react-router-dom";
import SignedHealthIDImage from "../Dashboard/SignedHealthIDImage";
import { useDispatch, useSelector } from "react-redux";
import {
  loadAllLabOrders,
  loadOrdersList,
  loadUsers,
} from "../../redux/actions";
import moment from "moment";
import { makeStyles } from "@mui/styles";
import NotificationDataRow from "./NotificationDataRow";

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

export default function Notifications(props) {
  const classes = useStyles();

  let dispatch = useDispatch();
  const [searchName, setSearchName] = useState("");
  const [suplierName, setSuplierName] = useState([]);
  const { PurchaseOrderList } = useSelector((state) => state.data);
  const { allLabOrderList } = useSelector((state) => state.data);

  useEffect(() => {
    document.title = "EMR Medical Officer Notification";
    dispatch(loadOrdersList(0, 500));
    dispatch(loadAllLabOrders(0, 500));
  }, []);

  const [notifySelect, setNotifySelect] = useState("");
  const [lab_purchase, setLab_purchase] = useState([]);

  useEffect(() => {
    const interval = setInterval(() => {
      if (notifySelect === "Orders") {
        let order_result = [];
        if (PurchaseOrderList) {
          PurchaseOrderList.map((PIOrders) => {
            order_result.push({
              id: PIOrders.id,
              date: PIOrders.poDate,
              poDetails: PIOrders.poDetails,
              indentItems: PIOrders.indentItems,
              raisedby: PIOrders.audit.createdBy,
              type: "order",
            });
          });
        }
        setLab_purchase(
          order_result.sort((a, b) =>
            Date.parse(b.date) > Date.parse(a.date) ? 1 : -1
          )
        );
      } else if (notifySelect === "Lab Reports") {
        let order_result = [];
        if (allLabOrderList) {
          allLabOrderList.map((LabOrders) => {
            order_result.push({
              id: LabOrders.id,
              date: LabOrders.orderDate,
              name: LabOrders.patient.name,
              healthId: LabOrders.patient.healthId,
              type: "result",
            });
          });
        }
        setLab_purchase(
          order_result.sort((a, b) =>
            Date.parse(b.date) > Date.parse(a.date) ? 1 : -1
          )
        );
      } else {
        let order_result = [];
        if (PurchaseOrderList) {
          PurchaseOrderList.map((PIOrders) => {
            order_result.push({
              id: PIOrders.id,
              date: PIOrders.poDate,
              poDetails: PIOrders.poDetails,
              indentItems: PIOrders.indentItems,
              raisedby: PIOrders.audit.createdBy,
              type: "order",
            });
          });
        }
        if (allLabOrderList) {
          allLabOrderList.map((LabOrders) => {
            order_result.push({
              id: LabOrders.id,
              pat_id: LabOrders.patient.patientId,
              date: LabOrders.orderDate,
              name: LabOrders.patient.name,
              healthId: LabOrders.patient.healthId,
              type: "result",
            });
          });
        }
        setLab_purchase(
          order_result.sort((a, b) =>
            Date.parse(b.date) > Date.parse(a.date) ? 1 : -1
          )
        );
      }
    }, 5000);
    return () => clearInterval(interval);
  }, [PurchaseOrderList, allLabOrderList, notifySelect]);

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

  const byDate = lab_purchase.reduce((obj, item) => {
    if (obj[moment(item.date).format("YYYY-MM-DD")]) {
      obj[moment(item.date).format("YYYY-MM-DD")].push(item);

      return obj;
    }

    obj[moment(item.date).format("YYYY-MM-DD")] = [{ ...item }];
    return obj;
  }, {});

  return (
    <Row className="main-div">
      <Col lg={2} className="side-bar">
        <Sidemenu />
      </Col>
      <Col lg={10} md={12} sm={12} xs={12}>
        <div>
          <div className="regHeader">
            <h1 className="Encounter-Header">Notifications</h1>
            <hr style={{ margin: "0px" }} />
          </div>
          <div className="notifify-div">
            <div className="notifify-border">
              <div className="searchFilter-div flex-column">
                <Row className="search-select">
                  <Col md={4}>
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
                            label="Search Notification.."
                          />
                        )}
                      />
                    </Form.Group>
                  </Col>
                  <Col md={4} className="text-right approval-text">
                    <Form.Label>View</Form.Label>
                  </Col>
                  <Col md={4} className="text-right">
                    <Form.Select
                      aria-label="Default select example"
                      id="approval_list"
                      value={notifySelect}
                      onChange={(e) => setNotifySelect(e.target.value)}
                    >
                      <option value="All">&#x2713;&nbsp;All</option>
                      <option value="Lab Reports">
                        &#x2713;&nbsp;Lab Reports
                      </option>
                      <option value="Orders">&#x2713;&nbsp;Orders</option>
                    </Form.Select>
                  </Col>
                </Row>
              </div>
              <NotificationDataRow
                byDate={byDate}
                PurchaseOrderState={props.PurchaseOrderState}
              />
            </div>
          </div>
        </div>
      </Col>
    </Row>
  );
}
