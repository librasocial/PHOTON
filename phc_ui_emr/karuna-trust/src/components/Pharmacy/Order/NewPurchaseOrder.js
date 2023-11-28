import React, { useState, useRef, useEffect } from "react";
import { Col, Row, Form, Button, Modal, InputGroup } from "react-bootstrap";
import TextField from "@mui/material/TextField";
import Autocomplete, { createFilterOptions } from "@mui/material/Autocomplete";
import { useDispatch, useSelector } from "react-redux";
import { loadPharmaList, loadSingleProduct } from "../../../redux/actions";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  popper: {
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

function NewPurchaseOrder(props) {
  const classes = useStyles();

  const form = useRef(null);
  let dispatch = useDispatch();
  const { SinglePharmacy } = useSelector((state) => state.data);
  const [primUom, setPrimUom] = useState([]);

  useEffect(() => {
    let altUom = [];
    let selectUom = [];
    if (SinglePharmacy) {
      selectUom.push(SinglePharmacy.primaryUOM);
      if (SinglePharmacy.umo?.length != 0) {
        for (var i = 0; i < SinglePharmacy.umo?.length; i++) {
          if (SinglePharmacy.umo[i]?.alternateUOM != "") {
            altUom.push(SinglePharmacy.umo[i].alternateUOM);
          }
        }
      }
    }
    setPrimUom([...selectUom, ...altUom]);
  }, [SinglePharmacy]);

  useEffect(() => {
    if (props.editProductID) {
      setAddOrders({ ...props.editProduct });
    }
  }, [props.editProduct, props.editProductID]);

  const [addOrders, setAddOrders] = useState({
    productId: "",
    productName: "",
    quantity: "",
    rate: "",
    cgstPercentage: "",
    sgstPercentage: "",
    cgstAmount: "",
    sgstAmount: "",
    totalAmount: "",
    uom: "",
  });

  const {
    productId,
    productName,
    quantity,
    rate,
    cgstPercentage,
    sgstPercentage,
    cgstAmount,
    sgstAmount,
    totalAmount,
    uom,
  } = addOrders;

  useEffect(() => {
    if (productId) {
      dispatch(loadSingleProduct(productId));
    }
  }, [productId]);

  let setProductValue;
  if (props.editProductID) {
    setProductValue = (value) => {
      if (value) {
        if (value.id != props.editProductID) {
          addOrders.productId = value.id;
          addOrders.productName = value.name;
          dispatch(loadSingleProduct(value.id));
        }
      }
    };
  } else {
    setProductValue = (value) => {
      if (value) {
        addOrders.productId = value.id;
        addOrders.productName = value.name;
        dispatch(loadSingleProduct(value.id));
      }
    };
  }

  // const setProductValue = (value) => {
  //     if (value) {
  //         // setProdId(value.id);
  //         addOrders.productId = value.id;
  //         addOrders.productName = value.name;
  //         dispatch(loadSingleProduct(value.id))
  //     }

  // }

  useEffect(() => {
    if (
      (addOrders.rate && addOrders.cgstPercentage) ||
      (addOrders.rate && addOrders.sgstPercentage)
    ) {
      addOrders.cgstAmount =
        Number(addOrders.quantity) * Number(addOrders.rate) -
        (Number(addOrders.quantity) * Number(addOrders.rate)) /
          (1 + Number(addOrders.cgstPercentage) / 100);
      addOrders.sgstAmount =
        Number(addOrders.quantity) * Number(addOrders.rate) -
        (Number(addOrders.quantity) * Number(addOrders.rate)) /
          (1 + Number(addOrders.sgstPercentage) / 100);
    }
  });

  const handleChange = (e) => {
    let { name, value } = e.target;
    setAddOrders({ ...addOrders, [name]: value });
    if (addOrders.cgstPercentage) {
      addOrders.cgstAmount =
        Number(addOrders.quantity) * Number(addOrders.rate) -
        (Number(addOrders.quantity) * Number(addOrders.rate)) /
          (1 + Number(addOrders.cgstPercentage) / 100);
    }
    if (addOrders.sgstPercentage) {
      addOrders.sgstAmount =
        Number(addOrders.quantity) * Number(addOrders.rate) -
        (Number(addOrders.quantity) * Number(addOrders.rate)) /
          (1 + Number(addOrders.sgstPercentage) / 100);
    }
  };

  if (
    addOrders.quantity &&
    addOrders.rate &&
    addOrders.cgstPercentage &&
    addOrders.sgstPercentage
  ) {
    let totalAmnt = Number(addOrders.quantity) * Number(addOrders.rate);
    let totalGst = Number(addOrders.cgstAmount) + Number(addOrders.cgstAmount);
    addOrders.totalAmount = totalAmnt + totalGst;
  }

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
  }, []);

  const AddProductData = () => {
    if (
      !addOrders.productName ||
      !addOrders.uom ||
      !addOrders.quantity ||
      !addOrders.rate ||
      !addOrders.cgstPercentage ||
      !addOrders.sgstPercentage
    ) {
      alert("Please fill all the fields...!");
    } else {
      props.setProductList([...props.productList, addOrders]);
      props.addPurchaseServiceClose(false);
      setAddOrders({});
    }
  };

  const UpdateProductData = (id) => {
    let array = [];
    const newProductList = props.productList.filter(
      (item) => item.productId !== id
    );
    array = newProductList;
    array.push(addOrders);
    props.setListState(true);
    props.setProductList(array);
    setAddOrders({});
    props.addPurchaseServiceClose(false);
    props.setEditProductID("");
  };

  const closeModal = () => {
    props.addPurchaseServiceClose(true);
    props.setEditProductID("");
    setAddOrders({});
  };

  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling

  return (
    <Modal
      show={props.addPurchaseService}
      onHide={props.addPurchaseServiceClose}
      className="addPurchase-modal"
    >
      <div className="addPurchase-modal">
        <div>
          <div className="add-service-form-col">
            <Row>
              <Col md={6} xs={9}>
                <h1 className="dia-heading pr-mdl-header">
                  {!props.editProductID ? "Add New Item" : "Edit Item"}
                </h1>
              </Col>
              <Col md={6} xs={3} align="right">
                <button
                  onClick={closeModal}
                  className="bi bi-x close-popup"
                ></button>
              </Col>
            </Row>
            <Form className="add-lab-form" ref={form}>
              <div className="allergy-information-div row">
                <div className="pro-div ">
                  <Form.Group>
                    <Form.Label>Product Name</Form.Label>
                    <Autocomplete
                      classes={{
                        inputRoot: classes.inputRoot,
                        paper: classes.popper,
                      }}
                      value={productName || ""}
                      name="productName"
                      onChange={(event, newValue) => {
                        if (typeof newValue === "string") {
                          setProductValue({
                            title: newValue,
                          });
                        } else {
                          setProductValue(newValue);
                        }
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
                            // onFocus: () => {
                            //   params.InputProps.onFocus();
                            //   params.InputProps.style = {
                            //     ...params.InputProps.style,
                            //     fontSize: "12px",
                            //   };
                            // },
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
                <div>
                  <div className="div-space">
                    <Form.Group
                      className="mb-3_timesname alter-label"
                      controlId="exampleForm.TName"
                    >
                      <Form.Label> UOM</Form.Label>
                      <Form.Select
                        aria-label="Default select example"
                        value={uom || ""}
                        name="uom"
                        onChange={handleChange}
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
                </div>
                <Row>
                  <Col md={6}>
                    <div className="div-space">
                      <Form.Group>
                        <Form.Label>PO Quantity</Form.Label>
                        <Form.Control
                          type="number"
                          placeholder="Enter Quantity..."
                          value={quantity || ""}
                          name="quantity"
                          onChange={handleChange}
                        />
                        <Form.Label style={{ color: "#D2CACA" }}>
                          E.g.50
                        </Form.Label>
                      </Form.Group>
                    </div>
                  </Col>
                  <Col md={6}>
                    <div className="div-space">
                      <Form.Group>
                        <Form.Label>PO Rate</Form.Label>
                        <InputGroup className="">
                          <InputGroup.Text id="mypo">&#8377;</InputGroup.Text>
                          <Form.Control
                            placeholder="Enter PO Rate..."
                            type="number"
                            value={rate || ""}
                            name="rate"
                            onChange={handleChange}
                            aria-label="PO Rate"
                            aria-describedby="basic-addon1"
                          />
                        </InputGroup>
                        <Form.Label id="mypo-footer">E.g.240</Form.Label>
                      </Form.Group>
                    </div>
                  </Col>
                </Row>
                <Row className="my-order">
                  <Col md={6}>
                    <div className="div-space">
                      <Form.Group>
                        <Form.Label>CGST %</Form.Label>
                        <InputGroup className="">
                          <InputGroup.Text id="mypo">%</InputGroup.Text>
                          <Form.Control
                            type="number"
                            placeholder="Enter CGST %...."
                            value={cgstPercentage || ""}
                            name="cgstPercentage"
                            onChange={handleChange}
                          />
                        </InputGroup>
                        <Form.Label id="mypo-footer">E.g.5</Form.Label>
                      </Form.Group>
                    </div>
                  </Col>
                  <Col md={6}>
                    <div className="div-space">
                      <Form.Group>
                        <Form.Label>SGST %</Form.Label>
                        <InputGroup className="">
                          <InputGroup.Text id="mypo">%</InputGroup.Text>
                          <Form.Control
                            placeholder="Enter SGST %...."
                            type="number"
                            value={sgstPercentage || ""}
                            name="sgstPercentage"
                            onChange={handleChange}
                          />
                        </InputGroup>
                        <Form.Label id="mypo-footer">E.g.5</Form.Label>
                      </Form.Group>
                    </div>
                  </Col>
                </Row>
                <Row className="add-order">
                  <Col md={6}>
                    <div className="pr-btn-sec">
                      <Button
                        className="item-cancel"
                        variant="outline-secondary"
                        onClick={closeModal}
                      >
                        Cancel
                      </Button>
                    </div>
                  </Col>
                  <Col md={6}>
                    <div className="pr-btn-sec">
                      {props.editProductID ? (
                        <Button
                          variant="outline-secondary"
                          className="item-add"
                          onClick={(e) =>
                            UpdateProductData(props.editProductID)
                          }
                        >
                          Update
                        </Button>
                      ) : (
                        <Button
                          variant="outline-secondary"
                          className="item-add"
                          onClick={AddProductData}
                        >
                          Add
                        </Button>
                      )}
                    </div>
                  </Col>
                </Row>
              </div>
            </Form>
          </div>
        </div>
      </div>
    </Modal>
  );
}

export default NewPurchaseOrder;
