import React, { useState, useEffect } from "react";
import {
  Col,
  Row,
  Form,
  Button,
  Modal,
  InputGroup,
  FormControl,
} from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import {
  loadAddPharma,
  loadSingleProduct,
  loadUpdatePharma,
} from "../../../redux/actions";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import SaveButton from "../../EMR_Buttons/SaveButton";
import Loader from "../Dashboard/Loader";
import { loadProductMaster } from "../../../redux/formUtilityAction";
import { Select } from "@mui/material";
import CreatableSelect from "react-select/creatable";

function AddPharmacy(props) {
  let productId = props.productId;

  let dispatch = useDispatch();
  const { SinglePharmacy } = useSelector((state) => state.data);

  useEffect(() => {
    dispatch(loadSingleProduct(productId));
  }, [productId]);

  const [isNewLoading, setNewLoading] = useState(false);

  const [inputList, setInputList] = useState([
    { alternateUOMUnit: "", alternateUOM: "", equivelentPrimaryUOMUnit: "" },
  ]);

  // handle click event of the Add button
  const [hide, setHide] = useState(false);

  const handleAddClick = () => {
    // setHide(true);
    setInputList([
      ...inputList,
      { alternateUOMUnit: "", alternateUOM: "", equivelentPrimaryUOMUnit: "" },
    ]);
  };

  //var handleInputChange;

  const handleRemoveClick = (index) => {
    const list = [...inputList];
    if (inputList.length == 1) {
      setHide(false);
    }
    if (inputList.length != 1) {
      list.splice(index, 1);
      setInputList(list);
    }
  };

  // hiding Add UOM Button

  const [pharmaState, setPharmaState] = useState({
    name: "",
    group: "",
    code: "",
    classification: "",
    hsnCode: "",
    expiryDateFormat: "",
    primaryUOM: "",
    status: "",
    umo: inputList,
  });

  useEffect(() => {
    if (SinglePharmacy && productId) {
      setPharmaState({ ...SinglePharmacy });
      setInputList(SinglePharmacy.umo);
      if (SinglePharmacy.status == "ACTIVE") {
        setCheckboxValue(true);
      } else {
        setCheckboxValue(false);
      }
      setHide(true);
    }
  }, [SinglePharmacy]);

  const {
    name,
    group,
    code,
    classification,
    hsnCode,
    expiryDateFormat,
    primaryUOM,
    umo,
    status,
  } = pharmaState;

  const handleChangeGroup = (e) => {
    const selectedGroup = e.target.value;
    setPharmaState((prevState) => ({
      ...prevState,
      group: selectedGroup,
    }));
  };
  const handleChangeClassification = (e) => {
    const selectedClassification = e.target.value;
    setPharmaState((prevState) => ({
      ...prevState,
      classification: selectedClassification,
    }));
  };

  const handleChangeExpiry = (e) => {
    const selectedExpiry = e.target.value;
    setPharmaState((prevState) => ({
      ...prevState,
      expiryDateFormat: selectedExpiry,
    }));
  };
  const handleChangeUom = (e) => {
    const selectedUom = e.target.value;
    setPharmaState((prevState) => ({
      ...prevState,
      primaryUOM: selectedUom,
    }));
  };
  const handleHsnCodeChange = (e) => {
    const inputHsnCode = e.target.value.replace(/\D/g, "");
    setPharmaState((prevState) => ({
      ...prevState,
      hsnCode: inputHsnCode,
    }));
  };

  const [productNameToCodeMap, setProductNameToCodeMap] = useState({});

  const { productMasterItems } = useSelector((state) => state.formData);

  // const [name, setName] = useState("");
  // const [code, setCode] = useState("");
  // const [groupName, setGroupName] = useState("");
  const [classificationName, setClassificationName] = useState("");
  const [expiryName, setExpiryName] = useState("");
  const [uomName, setUomName] = useState("");
  const [hsnCodeNumber, setHsnCode] = useState("");
  const [showDropdown, setShowDropdown] = useState(false);
  const [filteredOptions, setFilteredOptions] = useState([]);
  const [filteredItems, setFilteredItems] = useState([]);

  const [selectedProductName, setSelectedProductName] = useState("");
  const [inputValue, setInputValue] = useState("");

  const [filteredProductNames, setFilteredProductNames] = useState([]);

  const handleProductNameChange = (newValue) => {
    setInputValue(newValue);

    const filteredItems = productMasterItems
      .filter((formItem) => formItem.groupName === "Product Name and Code")
      .map((formItem) => formItem.elements)
      .flat()
      .filter((drpItem) =>
        drpItem.title.toLowerCase().includes(newValue.toLowerCase())
      );

    setFilteredProductNames(
      filteredItems.map((drpItem) => ({
        label: drpItem.title,
        value: drpItem.title,
      }))
    );
  };
  const [selectedProductCode, setSelectedProductCode] = useState("");

  const handleProductNameSelect = (selectedOption) => {
    const selectedProductName = selectedOption?.label || "";
    setnewname(selectedOption);

    const selectedProduct = productMasterItems
      .map((formItem) => formItem.elements)
      .flat()
      .find((drpItem) => drpItem.title === selectedProductName);

    setPharmaState((prevState) => ({
      ...prevState,
      name: selectedProductName,
      code: selectedProduct ? selectedProduct.code : "",
    }));

    setInputValue(selectedProductName);
  };

  const [newname, setnewname] = useState("");

  useEffect(() => {
    dispatch(loadProductMaster());
  }, []);

  var handleInputChange = (e, i) => {
    const { name, value } = e.target;
    const list = [...inputList];
    list[i][name] = value;
    setInputList(list);
    pharmaState.umo = list;
  };

  const handleHideButton = () => {
    setHide(true);
  };

  const [statusValue, setStatusValue] = useState(true);

  const [checkValue1, setCheckbox] = useState(true);
  const setCheckboxValue = (e) => {
    setStatusValue(false);
    if (e == false) {
      pharmaState.status = "INACTIVE";
      setCheckbox(e);
    } else if (e == true) {
      setCheckbox(e);
      pharmaState.status = "ACTIVE";
    }
  };

  useEffect(() => {
    if (statusValue == true) {
      pharmaState.status = "ACTIVE";
    }
  }, [statusValue, pharmaState]);

  function savePharmaName() {
    if (
      !group ||
      !name ||
      !code ||
      !classification ||
      !expiryDateFormat ||
      !hsnCode ||
      !primaryUOM
    ) {
      alert("Plese fill all mandatory fields");
    } else {
      var requestOptions = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(pharmaState),
      };
      dispatch(
        loadAddPharma(
          requestOptions,
          props.addPharmaServiceClose,
          setNewLoading,
          props.page,
          props.rowsPerPage
        )
      );
    }
  }

  const [btnStatus, setBtnStatus] = useState(false);
  function updatePharmaName() {
    setBtnStatus(true);
    // var updateProduct = {
    //     "type": "PRODUCT",
    //     "properties": pharmaState
    // }
    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(pharmaState),
    };
    dispatch(
      loadUpdatePharma(
        productId,
        requestOptions,
        props.addPharmaServiceClose,
        setNewLoading,
        setBtnStatus,
        props.page,
        props.rowsPerPage
      )
    );
  }

  function closeLoader() {
    setNewLoading(false);
    setBtnStatus(false);
  }

  const closeModal = () => {
    props.setProductId("");
    setInputList([
      { alternateUOMUnit: "", alternateUOM: "", equivelentPrimaryUOMUnit: "" },
    ]);
    setPharmaState({
      name: "",
      code: "",
      group: "",
      classification: "",
      hsnCode: "",
      expiryDateFormat: "",
      primaryUOM: "",
      umo: inputList,
    });

    props.addPharmaServiceClose(false);
  };

  function closeLoader() {
    setNewLoading(false);
    setBtnStatus(false);
  }

  return (
    <>
      <Loader
        closeLoader={closeLoader}
        productId={productId}
        isNewLoading={isNewLoading}
      />
      <Modal
        show={props.addPharmaService}
        onHide={props.addPharmaServiceClose}
        className="lab-service-modal"
      >
        <div className="">
          <div>
            <div className="add-service-form-col">
              <Row>
                <Col md={6} xs={9}>
                  <h1 className="dia-heading pro-head">
                    {productId ? "Edit Product" : "Add New Product"}
                  </h1>
                </Col>
                <Col md={6} xs={3} align="right">
                  <button
                    onClick={closeModal}
                    className="bi bi-x close-popup"
                  ></button>
                </Col>
              </Row>
              <Form className="add-lab-form">
                <div className="form-fields medicine-div">
                  <Row className="allergy-information-div add-pro-div">
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Product Group{" "}
                            <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            name="group"
                            value={group}
                            onChange={handleChangeGroup}
                          >
                            <option value="" disabled hidden>
                              Select Group
                            </option>
                            {productMasterItems.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Product Group" && (
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
                      </div>
                    </Col>
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Product Classification{" "}
                            <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            name="classification"
                            value={classification}
                            onChange={handleChangeClassification}
                          >
                            <option value="" disabled hidden>
                              Select Classification
                            </option>
                            {productMasterItems.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName ==
                                  "Product Classification" && (
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
                      </div>
                    </Col>
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>HSN Code</Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter HSN Code"
                            name="hsnCode"
                            value={hsnCode}
                            onChange={handleHsnCodeChange}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Availability{" "}
                          </Form.Label>
                          <br />
                          <div
                            className={`checkbox ${
                              checkValue1 && "checkbox--on"
                            }`}
                            name="status"
                            value={checkValue1}
                            onClick={(e) => setCheckboxValue(!checkValue1)}
                          >
                            <div className="checkbox__ball">
                              <span className="status-text">
                                {checkValue1 == false
                                  ? "Un-Available"
                                  : "Available"}
                              </span>
                            </div>
                          </div>
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <Row className="allergy-information-div add-pro-div">
                    <Col md={4}>
                      <Form.Group>
                        <Form.Label>
                          Product Name <span style={{ color: "red" }}>*</span>
                        </Form.Label>
                        <CreatableSelect
                          isClearable
                          options={filteredProductNames}
                          value={newname}
                          onInputChange={handleProductNameChange}
                          onChange={handleProductNameSelect}
                          placeholder="Type or Select a Product Name"
                          formatCreateLabel={(inputValue) =>
                            `Create "${inputValue}"`
                          }
                        />
                      </Form.Group>
                    </Col>
                    <Col md={2}>
                      <Form.Group style={{ width: "120%" }}>
                        <Form.Label>
                          Product code <span style={{ color: "red" }}>*</span>
                        </Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter Code"
                          value={code}
                          readOnly
                        />
                      </Form.Group>
                    </Col>

                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Expiry <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            name="expiryDateFormat"
                            value={expiryDateFormat}
                            onChange={handleChangeExpiry}
                          >
                            <option value="" disabled hidden>
                              Select Expiry Format
                            </option>
                            {productMasterItems.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Expiry" && (
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
                      </div>
                    </Col>
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Primary UOM <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            name="primaryUOM"
                            value={primaryUOM}
                            onChange={handleChangeUom}
                          >
                            <option value="" disabled hidden>
                              Select Primary
                            </option>
                            {productMasterItems.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Primary UOM" && (
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
                      </div>
                    </Col>
                  </Row>
                </div>
                <div className="alternate-medicine pro-med">
                  {hide == false && (
                    <div className="alternate-uom-div">
                      <a
                        className="alternate-uom"
                        variant="light"
                        onClick={handleHideButton}
                      >
                        <i className="bi bi-plus-circle-fill"></i>&nbsp;
                        <b>Add New Alternate UOM</b>
                      </a>
                      <p className="add-uom">
                        No alternate UOMs have been added. Please add if
                        required
                      </p>
                    </div>
                  )}
                  {hide == true && (
                    <div>
                      <div className="product-add-item-div alternate-uom-list">
                        <Row className="pro-border">
                          <Col md={6} className="text-left">
                            <b>Alternate Unit Of measurements</b>
                          </Col>
                          <Col md={6} className="text-right">
                            <a
                              className="alternate-uom"
                              variant="light"
                              onClick={handleAddClick}
                            >
                              <i className="bi bi-plus-circle-fill"></i>&nbsp;
                              <b>Add New Alternate UOM</b>
                            </a>
                          </Col>
                        </Row>
                      </div>
                      {inputList.map((x, i) => (
                        //return (
                        <Row key={i} className="alternate-uom-list">
                          <Col md={4}>
                            <div className="product-add-item-div">
                              <Form.Group
                                className="mb-3_days alter-label"
                                controlId="exampleForm.FName"
                              >
                                <Form.Label>Alternate UOM Unit</Form.Label>
                                <Form.Control
                                  className="presription-input"
                                  placeholder="Enter Alternate UOM Unit"
                                  value={x.alternateUOMUnit}
                                  name="alternateUOMUnit"
                                  //onChange={(event) => setDays(event.target.value)}
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={4}>
                            <div className="product-add-item-div">
                              <Form.Group
                                className="mb-3_timesname alter-label"
                                controlId="exampleForm.TName"
                              >
                                <Form.Label>Alternate UOM</Form.Label>
                                <Form.Select
                                  aria-label="Default select example"
                                  value={x.alternateUOM}
                                  name="alternateUOM"
                                  //onChange={(event) => { setTimes(event.target.value); }}
                                  onChange={(e) => handleInputChange(e, i)}
                                >
                                  <option value="" disabled hidden>
                                    Select
                                  </option>

                                  <option value="Each">Each</option>
                                  <option value="Box">Box</option>
                                  <option value="No">No</option>
                                  <option value="Strips Of 5">
                                    Strips Of 5
                                  </option>
                                  <option value="Strips Of 10">
                                    Strips Of 10
                                  </option>
                                </Form.Select>
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={3}>
                            <div className="product-add-item-div">
                              <Form.Group
                                className="mb-3_days alter-label"
                                controlId="exampleForm.FName"
                              >
                                <Form.Label>
                                  Equivalent Primary UOM Unit
                                </Form.Label>
                                <Form.Control
                                  className="presription-input"
                                  placeholder="Enter Equivalent Primary UOM..."
                                  value={x.equivelentPrimaryUOMUnit}
                                  name="equivelentPrimaryUOMUnit"
                                  //onChange={(event) => setDays(event.target.value)}
                                  onChange={(e) => handleInputChange(e, i)}
                                />
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={1} className="alternate-trash">
                            <span
                              className="my-trash"
                              onClick={() => handleRemoveClick(i)}
                            >
                              <i className="bi bi-trash3-fill"></i>
                            </span>
                          </Col>
                        </Row>
                        //);
                      ))}
                    </div>
                  )}
                </div>
              </Form>
            </div>
          </div>
          <div className="vitalsubmit align-me2 add-pharmacy" align="center">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={closeModal}
                class_name="regBtnPC"
                button_name="Cancel"
              />
            </div>
            <div className="save-btn-section">
              {productId ? (
                <SaveButton
                  butttonClick={updatePharmaName}
                  class_name="regBtnN"
                  button_name="Update"
                />
              ) : (
                <SaveButton
                  butttonClick={savePharmaName}
                  class_name="regBtnN"
                  button_name="Save"
                  btnDisable={btnStatus == true && disabled}
                />
              )}
            </div>
          </div>
        </div>
      </Modal>
    </>
  );
}

export default AddPharmacy;
