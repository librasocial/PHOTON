import React, { useState, useEffect } from "react";
import { Button, Row, Col } from "react-bootstrap";
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
import "../../../css/Services.css";
import "../PharmacyTab.css";
import "./ProductMaster.css";
import AddPharmacy from "./AddPharmacy";
import Sidemenu from "../../Sidemenus";
import { useDispatch, useSelector } from "react-redux";
import { loadPharmaList, loadPharmaByName } from "../../../redux/actions";
import PharmacyTable from "../pharmacyTable/PharmacyTable";

let searchKeyWord = "";
function ProductMaster() {
  let dispatch = useDispatch();
  const { PharmaProdList } = useSelector((state) => state.data);
  const { PharmaSearchList } = useSelector((state) => state.data);

  const { PharmaProdLength } = useSelector((state) => state.data);
  let Count = parseInt(PharmaProdLength);
  const [filteredResults, setFilteredResults] = useState([]);
  const [alphaState, setAlphaState] = useState(false);
  const [alphaStateClass, setAlphaStateClass] = useState(false);
  const [alphaStateName, setAlphaStateName] = useState(false);
  const [unalphaState, setUnAlphaState] = useState(false);
  const [unalphaStateClass, setUnAlphaStateClass] = useState(false);
  const [unalphaStateName, setUnAlphaStateName] = useState(false);

  let tableHeader = [
    { class: "class1", name: "Product Group", type: "group" },
    { class: "class1", name: "Classification", type: "group" },
    { class: "class3", name: "HSN Code", type: "type" },
    { class: "class1", name: "Product Name", type: "group" },
    { class: "class3", name: "Primary UOM", type: "type" },
    { class: "class3", name: "Status", type: "type" },
    { class: "class3", name: "Action", type: "type" },
  ];

  const [filteredData, setFilteredData] = useState([]);

  useEffect(() => {
    document.title = "EMR Pharmacy Master";
    if (searchKeyWord != "") {
      if (PharmaSearchList) {
        setFilteredData(PharmaSearchList);
      }
    } else {
      setFilteredData(PharmaProdList);
    }
  }, [PharmaProdList, PharmaSearchList, searchKeyWord]);

  let sorted;
  if (alphaState == true) {
    sorted = filteredData.sort((a, b) => a.group.localeCompare(b.group));
  } else if (unalphaState == true) {
    sorted = filteredData.sort((a, b) => b.group.localeCompare(a.group));
  } else if (alphaStateClass == true) {
    sorted = filteredData.sort((a, b) =>
      a.classification.localeCompare(b.classification)
    );
  } else if (unalphaStateClass == true) {
    sorted = filteredData.sort((a, b) =>
      b.classification.localeCompare(a.classification)
    );
  } else if (alphaStateName == true) {
    sorted = filteredData.sort((a, b) => a.name.localeCompare(b.name));
  } else if (unalphaStateName == true) {
    sorted = filteredData.sort((a, b) => b.name.localeCompare(a.name));
  } else {
    sorted = filteredData;
  }

  const [addPharmaService, setAddPharmaService] = useState(false);
  const addPharmaServiceClose = () => {
    setAddPharmaService(false);
  };
  const addPharmaServiceShow = () => setAddPharmaService(true);

  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  useEffect(() => {
    if (searchKeyWord != "") {
      dispatch(loadPharmaByName(page, rowsPerPage, searchKeyWord));
    } else {
      dispatch(loadPharmaList(page, rowsPerPage));
    }
  }, [page, rowsPerPage, searchKeyWord]);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const searchProduct = (e) => {
    searchKeyWord = e.target.value;
    if (searchKeyWord !== "") {
      setPage(0);
      const filteredData = PharmaProdList.filter((item) => {
        return item.name.toLowerCase().includes(searchKeyWord.toLowerCase());
      });
      setFilteredResults(filteredData);
    } else {
      setFilteredResults(PharmaProdList);
    }
  };
  const [productId, setProductId] = useState("");
  // const editProductdata = (e) => {
  //     addPharmaServiceShow(true)
  //     setProductId(e);
  // }

  return (
    <Row className="main-div">
      <Col lg={2} className="side-bar">
        <Sidemenu />
      </Col>
      <Col lg={10} md={12} sm={12} xs={12}>
        <div className="regHeader">
          <h1 className="register-Header">Product Master</h1>
        </div>
        <hr style={{ margin: "0px" }} />
        <div className="lab-service-tab pro-tab">
          <AddPharmacy
            addPharmaService={addPharmaService}
            addPharmaServiceClose={addPharmaServiceClose}
            rowsPerPage={rowsPerPage}
            page={page}
            productId={productId}
            setProductId={setProductId}
          />
          <Row>
            <Col md={6}>
              <h3 className="dia-heading pro-head">Pharmacy Product Details</h3>
              <div className="form-group pro-search">
                <input
                  type="text"
                  className="form-control prod-search"
                  placeholder="Enter Product Name To Search"
                  onChange={searchProduct}
                />
                <span className="fa fa-search form-control-pro-feedback d-flex"></span>
              </div>
            </Col>
            <Col md={6} className="text-right">
              <Button
                variant="outline-secondary"
                className="add-product-btn add-pro"
                onClick={addPharmaServiceShow}
              >
                <AddCircleRoundedIcon />
                &nbsp; Add New Product
              </Button>
            </Col>
          </Row>

          <div className="pro-field">
            <PharmacyTable
              tableHeader={tableHeader}
              pageType="Pharma-Products"
              setUnAlphaState={setUnAlphaState}
              setAlphaState={setAlphaState}
              setAlphaStateClass={setAlphaStateClass}
              setAlphaStateName={setAlphaStateName}
              setUnAlphaStateClass={setUnAlphaStateClass}
              setUnAlphaStateName={setUnAlphaStateName}
              alphaState={alphaState}
              alphaStateClass={alphaStateClass}
              alphaStateName={alphaStateName}
              PurchaseOrderList={filteredData}
              addPharmaServiceShow={addPharmaServiceShow}
              setProductId={setProductId}
              Count={Count}
              rowsPerPage={rowsPerPage}
              page={page}
              handleChangePage={handleChangePage}
              handleChangeRowsPerPage={handleChangeRowsPerPage}
            />
          </div>
        </div>
      </Col>
    </Row>
  );
}

export default ProductMaster;
