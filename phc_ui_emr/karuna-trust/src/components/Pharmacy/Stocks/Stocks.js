import React, { useEffect, useState } from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import moment from "moment";
import Sidemenu from "../../Sidemenus";
import { useDispatch, useSelector } from "react-redux";
import { loadInventory } from "../../../redux/actions";
import StockReportTable from "./StockReportTable";
import StockModal from "./StockModal";

export default function Stocks() {
  useEffect(() => {
    document.title = "EMR Pharmacy Stocks";
    dispatch(loadInventory());
  }, []);

  let dispatch = useDispatch();
  const { inventory } = useSelector((state) => state.data);

  const [cosoliDateStock, setCosoliDateStock] = useState([]);
  const [completeStock, setCompleteStock] = useState([]);

  useEffect(() => {
    let sortedArray = [];
    let sortedObject = [];
    if (inventory) {
      sortedObject = inventory.reduce((obj, item) => {
        if (obj[item.productId]) {
          obj[item.productId].push(item);

          return obj;
        }

        obj[item.productId] = [{ ...item }];
        return obj;
      }, []);
      Object.keys(sortedObject).forEach((el) =>
        sortedArray.push(sortedObject[el])
      );
    }

    let stockArray = [];
    let completeStockArray = [];
    let completeStockDetails = [];
    if (sortedArray) {
      for (var j = 0; j < sortedArray.length; j++) {
        let totalStock = 0;
        completeStockArray.push({
          product_name: sortedArray[j][0].name,
          uom: "",
        });
        for (var k = 0; k < sortedArray[j].length; k++) {
          completeStockDetails.push({
            prodName: sortedArray[j][k].name,
            batchNo: sortedArray[j][k].batchNumber,
            expireDate: sortedArray[j][k].expiryDate,
            stock: sortedArray[j][k].level,
          });
          totalStock += sortedArray[j][k].level;
        }

        stockArray.push({
          product_name: sortedArray[j][0].name,
          uom: "",
          stock: totalStock,
        });
      }
    }

    for (var i = 0; i < completeStockArray.length; i++) {
      completeStockArray[i].stockDetails = [];
      for (var j = 0; j < completeStockDetails.length; j++) {
        if (
          completeStockDetails[j].prodName == completeStockArray[i].product_name
        ) {
          completeStockArray[i].stockDetails.push(completeStockDetails[j]);
        }
      }
    }

    setCosoliDateStock(stockArray);
    setCompleteStock(completeStockArray);
  }, [inventory]);

  const [stockDate, setStockDate] = useState("");
  const [searchvalue, setSearchvalue] = useState("");
  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const disableFutureDt = (current) => {
    return current.isBefore(today);
  };

  const [report, setReport] = useState("");
  const selectSearch = (e) => {
    setReport(e.target.value);
  };

  const [classify, setClassify] = useState("");
  const selectClassifi = (e) => {
    setClassify(e.target.value);
  };

  const [printStocKModal, setPrintStocKModal] = useState(false);

  const printStockClose = () => {
    setPrintStocKModal(false);
  };

  const printStock = () => setPrintStocKModal(true);

  return (
    <React.Fragment>
      {report == "consolidated" && (
        <StockModal
          printStocKModal={printStocKModal}
          printStockClose={printStockClose}
          tableData={cosoliDateStock}
          type={report}
        />
      )}
      {report == "detailed" && (
        <StockModal
          printStocKModal={printStocKModal}
          printStockClose={printStockClose}
          tableData={completeStock}
          type={report}
        />
      )}
      <Row className="main-div">
        <Col lg={2} className="side-bar">
          <Sidemenu />
        </Col>
        <Col lg={10} md={12} sm={12} xs={12}>
          <div>
            <div className="regHeader">
              <h1 className="register-Header">Stock Report</h1>
            </div>
            <hr style={{ margin: "0px" }} />
            <div className="lab-service-tab stock-tab">
              <div className="stock-claffication">
                <Row>
                  <Col lg={3} md={3}>
                    <div className="row-staff-classify">
                      <Form.Group>
                        <Form.Label>Stock Status as On</Form.Label>
                        <Form.Control
                          className="form-control"
                          type="date"
                          key="random1"
                          pattern="/^(0?[1-9]|1[0-2])[\/](0?[1-9]|[1-2][0-9]|3[01])[\/]\d{4}$/"
                          id="pharma-input"
                          name="stDate"
                          value={stockDate}
                          onChange={(e) =>
                            setStockDate(
                              moment(new Date(e.target.value)).format(
                                "YYYY-MM-DD"
                              )
                            )
                          }
                          selected={searchvalue}
                          validate={[disableFutureDt]}
                          max={currentdate}
                        />
                      </Form.Group>
                    </div>
                  </Col>
                  <Col lg={3} md={3}>
                    <div className="row-staff-classify">
                      <Form.Group
                        className="mb-3_dosename"
                        controlId="exampleForm.DName"
                      >
                        <Form.Label>Report Type</Form.Label>
                        <Form.Select
                          aria-label="Default select example"
                          className="mb3_doc"
                          id="doc_list"
                          value={report}
                          onChange={selectSearch}
                        >
                          <option value="" disabled hidden>
                            Select Report Type{" "}
                          </option>
                          <option value="consolidated"> Consolidated</option>
                          <option value="detailed"> Detailed</option>
                        </Form.Select>
                      </Form.Group>
                    </div>
                  </Col>
                  <Col lg={3} md={3}>
                    <div className="row-staff-classify">
                      <Form.Group
                        className="mb-3_dosename"
                        controlId="exampleForm.DName"
                      >
                        <Form.Label>Classification</Form.Label>
                        <Form.Select
                          aria-label="Default select example"
                          className="mb3_doc"
                          id="doc_list"
                          value={classify}
                          onChange={selectClassifi}
                          disabled
                        >
                          <option value="" disabled hidden>
                            Select Classification{" "}
                          </option>
                        </Form.Select>
                      </Form.Group>
                    </div>
                  </Col>
                  <Col lg={3} md={3}></Col>
                </Row>
              </div>
              <br />
              {report == "" ? (
                <div className="stock-display-claffication text-center">
                  <img
                    src="../img/Pharmacy/inwards.png"
                    className="rounded mx-auto d-block inwards-img"
                  />
                  <p className="add-drugs-material">
                    No reports found. Please select the filters to get stock
                    reports.
                  </p>
                </div>
              ) : (
                <div>
                  <Row className="div-print-btn" id="pat_que2">
                    <Col lg={6} md={6}>
                      <h3 className="dia-heading selected-stock-date">
                        {report == "consolidated" && "Consolidated Report"}
                        {report == "detailed" && "Detailed Report"}
                      </h3>
                    </Col>
                    <Col lg={6} md={6} className="text-right">
                      <Button
                        variant="primary"
                        className="regBtnN"
                        onClick={printStock}
                      >
                        <i className="bi bi-printer"></i>&nbsp; Print
                      </Button>
                    </Col>
                  </Row>
                  <div className="div-table-stock">
                    {report == "consolidated" && (
                      <StockReportTable
                        tableData={cosoliDateStock}
                        type={report}
                      />
                    )}
                    {report == "detailed" && (
                      <StockReportTable
                        tableData={completeStock}
                        type={report}
                      />
                    )}
                  </div>
                </div>
              )}
            </div>
          </div>
        </Col>
      </Row>
    </React.Fragment>
  );
}
