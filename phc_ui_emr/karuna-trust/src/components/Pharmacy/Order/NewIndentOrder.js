import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button, Modal, InputGroup } from "react-bootstrap";
import TextField from "@mui/material/TextField";
import Autocomplete, { createFilterOptions } from "@mui/material/Autocomplete";
import { makeStyles } from "@mui/styles";
import { useDispatch, useSelector } from "react-redux";
import { loadPharmaList, loadSingleProduct } from "../../../redux/actions";

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

function NewIndentOrder(props) {
  const classes = useStyles();

  let dispatch = useDispatch();
  const { SinglePharmacy } = useSelector((state) => state.data);

  const [addOrders, setAddOrders] = useState({
    productId: "",
    productName: "",
    quantity: "",
    uom: "",
  });

  useEffect(() => {
    document.title = "Pharmacy Indent Order";
    if (props.editProductID) {
      setAddOrders({ ...props.editProduct });
    }
  }, [props.editProduct, props.editProductID]);

  const { productId, productName, quantity, uom } = addOrders;
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

  const setProductValue = (value) => {
    if (value) {
      addOrders.productId = value.id;
      addOrders.productName = value.name;
      dispatch(loadSingleProduct(value.id));
    }
  };

  useEffect(() => {
    if (productId) {
      dispatch(loadSingleProduct(productId));
    }
  }, [productId]);

  const handleChange = (e) => {
    let { name, value } = e.target;
    setAddOrders({ ...addOrders, [name]: value });
  };

  const AddProductData = () => {
    if (!addOrders.productName || !addOrders.quantity || !addOrders.uom) {
      alert("Please fill all the fields...!");
    } else {
      props.setIndentList([...props.indentList, addOrders]);
      props.addIndentServiceClose(false);
      setAddOrders({});
    }
  };

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

  // let updateArray;
  const UpdateProductData = (id) => {
    let array = [];
    const newProductList = props.indentList.filter(
      (item) => item.productId !== id
    );
    array = newProductList;
    array.push(addOrders);
    props.setListState(true);
    props.setIndentList(array);
    setAddOrders({});
    props.addIndentServiceClose(false);
    props.setEditProductID("");
  };

  const closeModal = () => {
    props.addIndentServiceClose(true);
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
      show={props.addIndentService}
      onHide={props.addIndentServiceClose}
      className="addPurchase-modal"
    >
      <div className="addPurchase-modal">
        <div>
          <div className="add-service-form-col">
            <Row>
              <Col md={6} xs={9}>
                <h1 className="dia-heading pro-head">
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
            <Form className="add-lab-form">
              <div className="allergy-information-div row">
                <div className="pro-div ">
                  <Form.Group>
                    <Form.Label>Product Name </Form.Label>
                    <Autocomplete
                      classes={{
                        inputRoot: classes.inputRoot,
                        paper: classes.popper,
                      }}
                      value={productName || ""}
                      onChange={(event, newValue) => {
                        if (typeof newValue === "string") {
                          setProductValue({
                            title: newValue,
                          });
                        }
                        // else if (newValue && newValue.inputValue) {
                        //     // Create a new value from the user input
                        //     setProductValue({
                        //         title: newValue.inputValue,
                        //     });
                        // }
                        else {
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
                      selectOnFocus
                      clearOnBlur
                      handleHomeEndKeys
                      id="free-solo-with-text-demo"
                      options={topOrder}
                      getOptionLabel={(option) => {
                        console.log(option);
                        // Value selected with enter, right from the input
                        if (typeof option === "string") {
                          return option;
                        }
                        // Add "xxx" option created dynamically
                        // if (option.inputValue) {
                        //     return option.inputValue;
                        // }
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
                          style={{ zIndex: 99999 }}
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
                          label="Enter Product Name..."
                        />
                      )}
                    />
                  </Form.Group>
                </div>
                <div className="add-order">
                  <Form.Group
                    className="mb-3_timesname alter-label"
                    controlId="exampleForm.TName"
                  >
                    <Form.Label> Select UOM </Form.Label>
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
                <Row>
                  <Col md={6}>
                    <div>
                      <Form.Group>
                        <Form.Label>Indent Quantity</Form.Label>
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
                  <Col md={6} className="my-order"></Col>
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

export default NewIndentOrder;
