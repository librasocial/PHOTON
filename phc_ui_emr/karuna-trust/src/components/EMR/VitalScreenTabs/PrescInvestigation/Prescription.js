import React, { useState, useEffect, useRef } from "react";
import { Col, Row, Form, Button, Modal, Table } from "react-bootstrap";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import moment from "moment";
import "./Prescription.css";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import TextField from "@mui/material/TextField";
import Autocomplete, { createFilterOptions } from "@mui/material/Autocomplete";
import { useDispatch, useSelector } from "react-redux";
import { loadPharmaList } from "../../../../redux/actions";
import { loadPresciptionDropdown } from "../../../../redux/formUtilityAction";
import PrescriptionModal from "../../ModalPopups/PrescriptionModal";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import SaveButton from "../../../EMR_Buttons/SaveButton";
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

function Prescription(props) {
  const classes = useStyles();

  const form = useRef(null);
  const phc = sessionStorage.getItem("phc");

  let dispatch = useDispatch();
  // set _id of submit data toet data for print page
  const [prescDataId, setPrescDataId] = useState("");
  const [prescGetData, setPrescGetData] = useState([]);
  // const sortedList = prescGetData.sort((a, b) => a.drugs?.name.localeCompare(b.drugs?.name).toString());

  const UHID = props.vitalDatas?.Patient?.UHId;
  const EncID = props.vitalDatas?.encounterId;
  const PatID = props.vitalDatas?.Patient?.patientId;

  const AbhaAddress = props.vitalDatas?.Patient?.abhaAddress;

  const PatName = props.vitalDatas?.Patient?.name;
  const PatHeaId = props.vitalDatas?.Patient?.healthId;
  const PatGen = props.vitalDatas?.Patient?.gender;
  const PatDob = props.vitalDatas?.Patient?.dob;
  const PatMob = props.vitalDatas?.Patient?.phone;
  const docName = props.vitalDatas?.Provider?.name;
  const PresentTime = new Date();
  const EffectiveDate = PresentTime.toISOString();
  const date = new Date();

  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  {
    /* View Prescribed Medicine */
  }
  const [descshow, setdescShow] = useState(false);
  const descClose = () => setdescShow(false);
  const descShow = () => setdescShow(true);
  {
    /* View Prescribed Medicine */
  }

  const filter = createFilterOptions();
  const { PharmaProdList } = useSelector((state) => state.data);
  const { prescDropdown } = useSelector((state) => state.formData);

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
    dispatch(loadPresciptionDropdown());
    dispatch(loadPharmaList(0, 1000000));
  }, []);
  {
    /* new code */
  }
  const [drugGetArray, setDrugGetArray] = useState([
    {
      productId: "",
      name: "",
      strength: "",
      route: "",
      routeCode: "",
      frequency: "",
      duration: "",
      instruction: "",
    },
  ]);

  const [inputList, setInputList] = useState([
    {
      productId: "",
      name: "",
      code: "",
      strength: "",
      route: "",
      routeCode: "",
      frequency: "",
      duration: "",
      instruction: "",
    },
  ]);

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

  console.log(inputList, "inputList");
  // get future date (edited)

  const handleInputChange = (e, index, newValue) => {
    const { name, value } = e.target;
    const list = [...inputList];
    if (e.target.name == "route") {
      let routeValues = value.split(",");

      list[index][name] = routeValues[0];
      list[index].routeCode = routeValues[1];
    } else if (e.target.name == "frequency") {
      inputList[index].frequency = e.target.value.split(",");
    } else if (e.target.name == "duration") {
      inputList[index].duration = Number(e.target.value);
    } else {
      if (newValue) {
        inputList[index].name = newValue.name;
        inputList[index].productId = newValue.id;
        inputList[index].code = newValue.code;
      } else {
        list[index][name] = value;
      }
    }
    setInputList(list);
  };
  // }
  // handle click event of the Remove button

  const formRemoveClick = (index) => {
    const list = [...inputList];
    list.splice(index);
    setInputList(list);
    setInputList([
      {
        productId: "",
        name: "",
        code: "",
        strength: "",
        route: "",
        routeCode: "",
        frequency: "",
        duration: "",
        instruction: "",
      },
    ]);
  };

  const handleRemoveClick = (index) => {
    const list = [...inputList];
    list.splice(index, 1);
    setInputList(list);
  };

  // handle click event of the Add button
  const handleAddClick = () => {
    setInputList([
      ...inputList,
      {
        productId: "",
        name: "",
        code: "",
        strength: "",
        route: "",
        routeCode: "",
        frequency: "",
        duration: "",
        instruction: "",
      },
    ]);
  };
  // handle click event of the Update button
  const handleUpdateClick = () => {
    setInputList([
      ...inputList,
      {
        productId: "",
        name: "",
        code: "",
        strength: "",
        route: "",
        routeCode: "",
        frequency: "",
        duration: "",
        instruction: "",
      },
    ]);
  };

  // set status for rerender
  const [status, setStatus] = useState(false);

  // post presription data
  function createPrescriptions(e) {
    e.preventDefault();
    let tempArray = [];
    let isFound;
    for (let i = 0; i < inputList.length; i++) {
      tempArray.push(inputList[i]);

      isFound = tempArray.some((element) => {
        if (
          !element.name ||
          !element.strength ||
          !element.route ||
          !element.frequency ||
          !element.duration
        ) {
          return true;
        }
        return false;
      });
    }

    if (isFound) {
      Tostify.notifyWarning("Please fill all the fields...!");
    } else {
      const prescriptionsData = {
        UHId: UHID,
        abhaAddress: AbhaAddress,
        encounterId: EncID,
        patientId: PatID,
        prescribedBy: docName,
        prescribedOn: date,
        partOfPrescription: "",
        medicalTests: [""],
        reviewAfter: "",
        products: inputList,
      };

      var requestOptions = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(prescriptionsData),
      };

      fetch(`${constant.ApiUrl}/prescriptions`, requestOptions)
        .then((res) => {
          res.json();
          console.log("hiii");
          console.log(res);
        })
        .then((res) => {
          Tostify.notifySuccess("Prescription Data Saved for" + " " + PatName);
          setStatus(true);
        });
      // clear all input values in the form
      setInputList([
        {
          productId: "",
          name: "",
          code: "",
          strength: "",
          route: "",
          routeCode: "",
          frequency: "",
          duration: "",
          instruction: "",
        },
      ]);
      form.current.reset();
      if (inputList.length > 1) {
        // handleRemoveClick(true);
        formRemoveClick(true);
      }
    }
  }
  // post presription data

  // cancel button
  const cancelFields = () => {
    setInputList([
      {
        productId: "",
        name: "",
        code: "",
        strength: "",
        route: "",
        routeCode: "",
        frequency: "",
        duration: "",
        instruction: "",
      },
    ]);
    form.current.reset();
    // if (drugGetArray.length > 1) {
    //     formRemoveClick(true);
    // }
    setPrescDataId("");
    if (inputList.length > 1) {
      // handleRemoveClick(true);
      formRemoveClick(true);
    }
  };
  // cancel button

  useEffect(() => {
    updateButton(prescDataId);
  }, [prescDataId, status]);

  // function for update recently save data
  function updateButton(e) {
    setPrescDataId(e);
    let prescId = e;
    if (prescId != "" && prescId != undefined) {
      fetch(
        `${constant.ApiUrl}/prescriptions/${prescId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let drugArray = [];
          for (let i = 0; i < res["products"].length; i++) {
            drugArray.push(res["products"][i]);
          }
          setInputList(drugArray);
          setStatus(false);
        });
    }
  }

  useEffect(() => {
    if (EncID != "" && EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/prescriptions/filter?page=&size=&encounterId=` +
          EncID,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setPrescGetData(res["data"]);
          let drugArray = [];
          if (res["data"].length != 0) {
            for (let i = 0; i < res["data"][0]["products"].length; i++) {
              drugArray.push(res["data"][0]["products"][i]);
            }
          }
          setDrugGetArray(drugArray);

          setStatus(false);
          // setPrescDataId(res.data?.['_id'])
        });
    }
  }, [EncID, status]); //EncID, status

  // function for fetching recently save data

  // delete prescription
  const deletePrescriptions = (e) => {
    const prescId = e;

    if (prescDataId != "" && prescDataId != undefined) {
      fetch(
        `${constant.ApiUrl}/prescriptions/${prescId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          const presc_name = res["products"][0]["name"];
          const presc_code = res["products"][0]["code"];
          const presc_strength = res["products"][0]["strength"];
          const presc_frequency = res["products"][0]["frequency"];
          const presc_route = res["products"][0]["route"];
          const presc_routeCode = res["products"][0]["routeCode"];
          const presc_duration = res["products"][0]["duration"];
          const presc_instr = res["instruction"];
          // setPrescDataId(res['_id'])
        });
    }
  };
  // delete prescription

  // previous history modal
  const [prescriptionDataShow, setPrescriptionShow] = useState(false);
  const prescriptionClose = () => setPrescriptionShow(false);
  const prescriptionShow = () => setPrescriptionShow(true);
  // previous history modal

  const patchArray = [...drugGetArray, ...inputList];

  const [isFound1, setIsFound1] = useState(false);
  const [isValid, setIsValid] = useState(false);

  useEffect(() => {
    let tempArray1 = [];
    for (let i = 0; i < inputList.length; i++) {
      tempArray1.push(inputList[i]);
      tempArray1.some((element) => {
        if (
          !element.name ||
          !element.strength ||
          !element.route ||
          !element.frequency ||
          !element.duration
        ) {
          setIsFound1(true);
        } else {
          setIsFound1(false);
        }
        if (
          !element.name &&
          !element.strength &&
          !element.route &&
          !element.frequency &&
          !element.duration
        ) {
          setIsValid(true);
        } else {
          setIsValid(false);
        }
      });
    }
  }, [inputList]);

  // patch
  function updatePrscData(e) {
    let id = e;
    let tempArray = [];
    let isFound;
    for (let i = 0; i < inputList.length; i++) {
      tempArray.push(inputList[i]);
      isFound = tempArray.some((element) => {
        if (
          !element.name ||
          !element.strength ||
          !element.route ||
          !element.frequency ||
          !element.duration
        ) {
          return true;
        }
        return false;
      });
    }

    if (isFound) {
      alert("please fill all the fields");
    } else {
      const updatePrescData = {
        prescribedBy: docName,
        medicalTests: [""],
        products: patchArray,
      };

      var requestOptions = {
        headers: serviceHeaders.myHeaders1,
        method: "PATCH",
        mode: "cors",
        body: JSON.stringify(updatePrescData),
      };

      fetch(`${constant.ApiUrl}/prescriptions/${id}`, requestOptions)
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "Previous Prescription Data Updated for" + " " + PatName
          );
          setStatus(true);
          // setPrescDataId("")
        });
      setInputList([
        {
          productId: "",
          name: "",
          code: "",
          strength: "",
          route: "",
          routeCode: "",
          frequency: "",
          duration: "",
          instruction: "",
        },
      ]);
      form.current.reset();
      if (inputList.length > 1) {
        // handleRemoveClick(true);
        formRemoveClick(true);
      }
    }
  }
  // patch
  // let PatientDays = ((new Date()).getDate() - (new Date(orderedDetails[0]?.patient?.dob)).getDate())
  // update exit one

  function updateExitPrscData(e) {
    let idExit = e;

    let tempArray = [];
    let isFound;
    for (let i = 0; i < drugGetArray.length; i++) {
      tempArray.push(drugGetArray[i]);

      isFound = tempArray.some((element) => {
        if (
          !element.name ||
          !element.strength ||
          !element.route ||
          !element.frequency ||
          !element.duration
        ) {
          return true;
        }
        return false;
      });
    }

    if (isFound) {
      Tostify.notifyWarning("Please fill all the fields...!");
    } else {
      const updatePrescData = {
        prescribedBy: docName,
        medicalTests: [""],
        products: inputList,
      };

      var requestOptions = {
        headers: serviceHeaders.myHeaders1,
        method: "PATCH",
        mode: "cors",
        body: JSON.stringify(updatePrescData),
      };

      if (idExit != "" && idExit != undefined) {
        fetch(`${constant.ApiUrl}/prescriptions/${idExit}`, requestOptions)
          .then((res) => res.json())
          .then((res) => {
            Tostify.notifySuccess(
              "Prescription Data Updated for" + " " + PatName
            );
            setStatus(true);
            // setPrescDataId("")
          });
      }
    }
    setInputList([
      {
        productId: "",
        name: "",
        code: "",
        strength: "",
        route: "",
        routeCode: "",
        frequency: "",
        duration: "",
        instruction: "",
      },
    ]);
    form.current.reset();
    setPrescDataId("");
    if (inputList.length > 1) {
      formRemoveClick(true);
    }
  }
  // update exit one
  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling
  let selectName = "name";
  return (
    <React.Fragment>
      <ToastContainer />
      <ViewModalPopups
        chiefClose={prescriptionClose}
        cheifShow={prescriptionDataShow}
        PatName={PatName}
        prescriptionUHID={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        medicalEncounterId={EncID}
        dateto={dateto}
        datefrom={datefrom}
        modType="prescription"
      />
      <div>
        <div className="form-col">
          <div className="presription-form">
            <Row>
              <Col md={6}>
                <h5 className="vital-header">Prescription</h5>
              </Col>
              <Col md={6}>
                <Button
                  varient="light"
                  className="float-right view-prev-details"
                  onClick={prescriptionShow}
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Prescription
                </Button>
              </Col>
            </Row>
            <div>
              <h6 className="meditation-text">
                <span className="rx-advice">Rx</span> Advice medication for this
                visit
              </h6>
            </div>
            <div>
              <Form ref={form} className="prescription-form-table">
                <Table size="sm" className="presc-table">
                  <thead>
                    <tr>
                      <th className="presc-thead drug-details">Drugs Name</th>
                      <th className="presc-thead drug-align">Dose</th>
                      <th className="presc-thead drug-align">Route</th>
                      <th className="presc-thead drug-align">Frequency</th>
                      <th className="presc-thead drug-align-day">
                        No. of days
                      </th>
                      <th className="presc-thead drug-align">Instructions</th>
                      <th className="presc-thead drug-align">Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {prescDataId ? null : (
                      <>
                        {prescGetData?.map((presc, i) => (
                          <React.Fragment key={i}>
                            {presc.products.map((item, i) => (
                              <tr key={i}>
                                <td className="presc-tbody drug-details">
                                  {item.name}
                                </td>
                                <td className="presc-tbody drug-align">
                                  {item.strength}
                                </td>
                                <td className="presc-tbody drug-align">
                                  {item.route}
                                </td>
                                <td className="presc-tbody drug-align">
                                  {String(item.frequency)}
                                </td>
                                <td className="presc-tbody drug-align-day">
                                  {item.duration}
                                </td>
                                <td className="presc-tbody drug-instruction">
                                  {item.instruction}
                                </td>
                                <td className="presc-tbody drug-align">
                                  <div className="myact">
                                    <Button
                                      variant="link"
                                      className="btnact"
                                      onClick={(e) => updateButton(presc._id)}
                                    >
                                      <i className="bi bi-pencil"></i>
                                    </Button>
                                    <Button
                                      variant="link"
                                      className="btnact"
                                      onClick={(e) =>
                                        deletePrescriptions(presc._id)
                                      }
                                    >
                                      <i className="bi bi-trash del-icon"></i>
                                    </Button>
                                  </div>
                                </td>
                              </tr>
                            ))}
                          </React.Fragment>
                        ))}
                      </>
                    )}
                    {/* {!prescDataId ? */}
                    <>
                      {inputList.map((x, i) => {
                        return (
                          <tr key={i}>
                            <td className="presc-tbody pro-div">
                              <Form.Group className="mb-3_drugname">
                                <Autocomplete
                                  classes={{
                                    inputRoot: classes.inputRoot,
                                    paper: classes.popper,
                                  }}
                                  value={x.name || ""}
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
                                    if (option.name) {
                                      return option.name;
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
                                        //   // Customize styles when focused
                                        //   params.InputProps.onFocus();
                                        //   // Add additional styles here
                                        //   // For example, set border color to blue when focused
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
                                      label="Enter Product Name"
                                    />
                                  )}
                                />
                              </Form.Group>
                            </td>
                            <td className="presc-tbody" align="center">
                              <Form.Group
                                className="mb-3_dosename"
                                controlId="exampleForm.DName"
                              >
                                <Form.Select
                                  aria-label="Default select example"
                                  placeholder="Select a value..."
                                  value={x.strength || ""}
                                  className="pre-drop"
                                  name="strength"
                                  onChange={(e) => handleInputChange(e, i)}
                                >
                                  <option value="" defaultValue hidden>
                                    Select
                                  </option>
                                  {prescDropdown.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName == "Dose" && (
                                        <>
                                          {formItem.elements.map(
                                            (drpItem, drpIndex) => (
                                              <option
                                                value={drpItem.title}
                                                key={drpIndex}
                                              >
                                                {drpItem.title}
                                              </option>
                                            )
                                          )}
                                        </>
                                      )}
                                    </React.Fragment>
                                  ))}
                                </Form.Select>
                              </Form.Group>
                            </td>
                            {/* <td className="presc-tbody" align="center">
                              <Form.Group
                                className="mb-3_dosagename"
                                controlId="exampleForm.DSName"
                              >
                                <Form.Select
                                  aria-label="Default select example"
                                  placeholder="Select a value..."
                                  value={x.route || ""}
                                  className="pre-drop"
                                  name="route"
                                  onChange={(e) => handleInputChange(e, i)}
                                >
                                  <option value="" defaultValue hidden>
                                    Select
                                  </option>
                                  {prescDropdown.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName == "Route" && (
                                        <>
                                          {formItem.elements.map(
                                            (drpItem, drpIndex) => (
                                              <option
                                                value={drpItem.title}
                                                key={drpIndex}
                                              >
                                                {drpItem.title}
                                              </option>
                                            )
                                          )}
                                        </>
                                      )}
                                    </React.Fragment>
                                  ))}
                                </Form.Select>
                              </Form.Group>
                            </td> */}
                            <td className="presc-tbody" align="center">
                              <Form.Group className="mb-3_dosagename">
                                <Form.Select
                                  aria-label="Default select example"
                                  placeholder="Select a value..."
                                  value={x.route + "," + x.routeCode || ""}
                                  className="pre-drop"
                                  name="route"
                                  // id={x.routeCode}
                                  onChange={(e) => handleInputChange(e, i)}
                                >
                                  <option value="" defaultValue hidden>
                                    Select
                                  </option>
                                  {prescDropdown.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName === "Route" && (
                                        <>
                                          {formItem.elements.map(
                                            (drpItem, drpIndex) => (
                                              <option
                                                value={
                                                  drpItem.title +
                                                  "," +
                                                  drpItem.routeCode
                                                }
                                                key={drpIndex}
                                              >
                                                {drpItem.title}
                                              </option>
                                            )
                                          )}
                                        </>
                                      )}
                                    </React.Fragment>
                                  ))}
                                </Form.Select>
                              </Form.Group>
                            </td>

                            <td className="presc-tbody" align="center">
                              <Form.Group
                                className="mb-3_timesname"
                                controlId="exampleForm.TName"
                              >
                                <Form.Select
                                  key={i}
                                  aria-label="Default select example"
                                  value={x.frequency || ""}
                                  className="pre-drop"
                                  name="frequency"
                                  onChange={(e) => handleInputChange(e, i)}
                                >
                                  <option value="" hidden>
                                    Select
                                  </option>
                                  {prescDropdown.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName == "Frequency" && (
                                        <>
                                          {formItem.elements.map(
                                            (drpItem, drpIndex) => (
                                              <option
                                                value={
                                                  drpItem.title == "1-0-0"
                                                    ? "morning"
                                                    : drpItem.title == "1-1-0"
                                                    ? "morning,afternoon"
                                                    : drpItem.title == "1-1-1"
                                                    ? "morning,afternoon,night"
                                                    : drpItem.title == "0-0-1"
                                                    ? "night"
                                                    : drpItem.title == "1-0-1"
                                                    ? "morning,night"
                                                    : drpItem.title == "0-1-0"
                                                    ? "afternoon"
                                                    : ""
                                                }
                                                key={drpIndex}
                                              >
                                                {drpItem.title}
                                              </option>
                                            )
                                          )}
                                        </>
                                      )}
                                    </React.Fragment>
                                  ))}
                                </Form.Select>
                              </Form.Group>
                            </td>
                            <td className="presc-tbody" align="center">
                              <Form.Group
                                className="mb-3_days"
                                controlId="exampleForm.FName"
                              >
                                <Form.Control
                                  className="presription-input days-input"
                                  type="number"
                                  placeholder="Enter Days"
                                  value={x.duration || ""}
                                  name="duration"
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </td>
                            <td className="presc-tbody" align="center">
                              <Form.Group
                                className="mb-3_dosagename"
                                controlId="exampleForm.DSName"
                              >
                                <Form.Select
                                  aria-label="Default select example"
                                  placeholder="Select a value..."
                                  value={x.instruction || ""}
                                  className="pre-drop"
                                  name="instruction"
                                  onChange={(e) => handleInputChange(e, i)}
                                >
                                  <option value="" defaultValue hidden>
                                    Select
                                  </option>
                                  {prescDropdown.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName == "Instructions" && (
                                        <>
                                          {formItem.elements.map(
                                            (drpItem, drpIndex) => (
                                              <option
                                                value={drpItem.title}
                                                key={drpIndex}
                                              >
                                                {drpItem.title}
                                              </option>
                                            )
                                          )}
                                        </>
                                      )}
                                    </React.Fragment>
                                  ))}
                                </Form.Select>
                              </Form.Group>
                            </td>
                            <td className="presc-tbody" align="center">
                              {!isFound1 && (
                                <>
                                  {inputList.length - 1 === i && (
                                    <p
                                      className=" btnact btn btn-link"
                                      onClick={handleAddClick}
                                    >
                                      {/* <i className="fa fa-floppy-o" aria-hidden="true"></i> */}
                                      <i
                                        className="fa fa-plus-circle"
                                        style={{ color: "#2D5986" }}
                                        aria-hidden="true"
                                      ></i>
                                    </p>
                                  )}
                                </>
                              )}

                              {inputList.length !== 1 && (
                                <p
                                  className=" btnact btn btn-link my-trash"
                                  onClick={() => handleRemoveClick(i)}
                                >
                                  <i className="bi bi-trash"></i>
                                </p>
                              )}
                            </td>
                          </tr>
                        );
                      })}
                    </>
                    <tr className="last-tr"></tr>
                  </tbody>
                </Table>
              </Form>
              <Row>
                <Col md={9}>
                  {/* <Button variant="light" className="print-btn" onClick={descShow} >
                                        <i className="bi bi-printer"></i>&nbsp;Print
                                    </Button> */}
                </Col>
                <Col md={3}>
                  <div className="pre-btn-section float-right">
                    <div className="save-btn-section">
                      <SaveButton
                        butttonClick={cancelFields}
                        class_name="regBtnPC"
                        btnDisable={isValid}
                        button_name="Cancel"
                      />
                    </div>
                    <div className="save-btn-section">
                      {prescGetData.length != 0 ? (
                        <>
                          {prescDataId ? (
                            <SaveButton
                              butttonClick={(e) =>
                                updateExitPrscData(prescGetData[0]?._id)
                              }
                              class_name="regBtnN"
                              btnDisable={isValid}
                              button_name="update"
                            />
                          ) : (
                            <SaveButton
                              butttonClick={(e) =>
                                updatePrscData(prescGetData[0]?._id)
                              }
                              class_name="regBtnN"
                              btnDisable={isValid}
                              button_name="Update"
                            />
                          )}
                        </>
                      ) : (
                        <SaveButton
                          butttonClick={createPrescriptions}
                          class_name="regBtnN"
                          btnDisable={isValid}
                          button_name="Save"
                        />
                      )}
                    </div>
                  </div>
                </Col>
              </Row>
            </div>
          </div>
        </div>
        {/* View Prescribed Medicine */}
        <PrescriptionModal
          descshow={descshow}
          descClose={descClose}
          phc={phc}
          docName={docName}
          EffectiveDate={EffectiveDate}
          PatName={PatName}
          UHID={UHID}
          PatHeaId={PatHeaId}
          PatDob={PatDob}
          PatMob={PatMob}
          prescGetData={prescGetData}
        />
      </div>
    </React.Fragment>
  );
}

export default Prescription;
