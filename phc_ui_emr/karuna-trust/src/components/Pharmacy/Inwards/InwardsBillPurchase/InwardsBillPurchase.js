import React, { useState, useEffect } from "react";
import {
  Button,
  Row,
  Col,
  Breadcrumb,
  Form,
  OverlayTrigger,
  Tooltip,
} from "react-bootstrap";
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
import "../../../../css/Services.css";
import "./InwardsBillPurchase.css";
import "../../PharmacyTab.css";
// import { useDispatch, useSelector } from 'react-redux';
// import { loadSingleOrder } from '../../../../redux/actions';
// import Pdf from "react-to-pdf";
// import jsPDF from 'jspdf';
// import { renderToString } from 'react-dom/server';

function InwardsBillPurchase(props) {
  const onClickChange = () => {
    props.setPageChange(false);
  };

  // const pdfRef = React.createRef();
  // let orderId = props.orderId;
  // let dispatch = useDispatch();
  // const { SingleOrder } = useSelector(state => state.data);

  // useEffect(() => {
  //     dispatch(loadSingleOrder(orderId));
  // }, [orderId])

  // const handleClick = () => {
  // };
  // const Print = () => {
  //     // let printContents = document.getElementById('pdgdown').innerHTML;
  //     // let originalContents = document.body.innerHTML;
  //     // document.body.innerHTML = printContents;
  //     window.print(document.getElementById('pdgdown').innerHTML);
  //     // originalContents = document.body.innerHTML;
  //     // document.getElementById('pdgdown').innerHTML = printContents;
  // }

  // const onButtonClick = () => {
  //     let printContents = document.getElementById('pdgdown').innerHTML;
  //     document.body.innerHTML = printContents;
  //     let originalContents = document.body.innerHTML;
  //     const string = renderToString(originalContents);
  //     const pdf = new jsPDF(string);
  //     pdf.fromHTML(string);
  //     pdf.save('pdf')
  // }

  return (
    <div>
      <div className="regHeader">
        <h1 className="register-Header">Purchase Order</h1>
      </div>
      <hr style={{ margin: "0px" }} />
      <div className="bill-form">
        <Breadcrumb>
          <Breadcrumb.Item
            className="pur-order-breadcrumb"
            onClick={onClickChange}
          >
            Orders
          </Breadcrumb.Item>
          <Breadcrumb.Item active>#PO-00012345</Breadcrumb.Item>
        </Breadcrumb>
        <div
          id="pdgdown"
          style={{ width: "100%" }}
          //ref={pdfRef}
        >
          <div className="pro-tab purchase-order">
            <Row className="billorder-border">
              <Col md={9}>
                <h1 className="billorder-head">
                  Purchase Order : # PO-00012345
                </h1>
              </Col>
              <Col md={3} className="downprint">
                <Row>
                  <Col md={6} className="printicon">
                    {/* <h1 className="billorder-head print-head down-head"
                                            onClick={onButtonClick}>
                                            <i className="fa fa-download downpdf" aria-hidden="true"></i>&nbsp;
                                            Download PDF
                                        </h1> */}
                    {/* <Pdf filename="example.pdf" targetRef={pdfRef}> */}
                    {/* {({ toPdf }) => ( */}
                    <h1
                      className="billorder-head print-head down-head"
                      //onClick={() => {toPdf();}}
                    >
                      <i
                        className="fa fa-download downpdf"
                        aria-hidden="true"
                      ></i>
                      &nbsp; Download PDF
                    </h1>
                    {/* )} 
                                        </Pdf> */}
                  </Col>
                  <Col md={1}>
                    <div className="bill-line"></div>
                  </Col>
                  <Col md={5}>
                    <h1
                      className="billorder-head print-head"
                      //onClick={Print}
                    >
                      <i className="fa fa-print" aria-hidden="true"></i>&nbsp;
                      Print
                    </h1>
                  </Col>
                </Row>
              </Col>
            </Row>
            <Row>
              <Col md={4} className="main-head">
                <Row>
                  <Col md={3}>
                    <p className="po-head">PO Date</p>
                    <p className="po-head">Delivery Date</p>
                    <p className="po-head">Status</p>
                    <p className="po-head">Ordered By</p>
                  </Col>
                  <Col md={1}>
                    <p className="po-dot">:</p>
                    <p className="po-dot">:</p>
                    <p className="po-dot">:</p>
                    <p className="po-dot">:</p>
                  </Col>
                  <Col md={5}>
                    <p className="po-body">25-May-2022</p>
                    <p className="po-body">12-Jaune-2022</p>
                    <p className="po-body">
                      {/* {SingleOrder.status} */} Completed
                    </p>
                    <p className="po-body">
                      Jhon Doe <span className="pro-text">(Pharmacist)</span>
                    </p>
                  </Col>
                  <Col md={3}></Col>
                </Row>
              </Col>
              <Col md={4}></Col>
              <Col md={4}>
                <Row align="right">
                  <Col md={3}></Col>
                  <Col md={3}>
                    <p className="po-head po-supply-head">Supplier</p>
                    <p className="po-head po-supply-head">Contact No</p>
                    <p className="po-head po-supply-head">Address</p>
                  </Col>
                  <Col md={1}>
                    <p className="po-dot po-supply-dot">:</p>
                    <p className="po-dot po-supply-dot">:</p>
                    <p className="po-dot po-supply-dot">:</p>
                  </Col>
                  <Col md={5}>
                    <p className="po-body po-supply-body">
                      Drug Warehouse Logistics
                    </p>
                    <p className="po-body po-supply-body">+91-9898989898</p>
                    <p className="po-body po-supply-body">
                      Indiranagar, 100 Feet Road,
                    </p>
                    <p className="po-body po-supply-body">
                      Bangalore, Karnataka - 560068
                    </p>
                  </Col>
                </Row>
              </Col>
            </Row>
          </div>
          <div className="view-page-paper-table">
            <Paper
              sx={{ width: "100%", overflow: "hidden", margin: "2% 0 0 0" }}
              className="inwards-bill-table"
            >
              <TableContainer sx={{ maxHeight: 440 }}>
                <Table stickyHeader aria-label="sticky table">
                  <TableHead>
                    <TableRow>
                      <TableCell align="left">Product Name</TableCell>
                      <TableCell align="left">UOM</TableCell>
                      <TableCell align="left">PO Qty</TableCell>
                      <TableCell align="left">PO Rate</TableCell>
                      <TableCell align="left">CGST %</TableCell>
                      <TableCell align="left">CGST AMT</TableCell>
                      <TableCell align="left">SGST %</TableCell>
                      <TableCell align="left">SGST AMT</TableCell>
                      <TableCell align="right">Total Amount</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {/* {SingleOrder?.poDetails?.poItems.map((item, index) => ( */}
                    <TableRow>
                      <TableCell align="left">
                        {/* {item.productName} */} Paracetomol…
                      </TableCell>
                      <TableCell align="left">{/* {item.uom} */} Box</TableCell>
                      <TableCell align="left">
                        {/* {item.quantity} */} 20.00
                      </TableCell>
                      <TableCell align="left">
                        {/* {item.rate} */} 100.00
                      </TableCell>
                      <TableCell align="left">
                        {/* {item.cgstPercentage} */} 5.00
                      </TableCell>
                      <TableCell align="left">
                        {/* {parseFloat(item.cgstAmount).toFixed(2)} */} 100.00
                      </TableCell>
                      <TableCell align="left">
                        {/* {item.sgstPercentage} */} 5.00
                      </TableCell>
                      <TableCell align="left">
                        {/* {parseFloat(item.sgstAmount).toFixed(2)} */} 100.00
                      </TableCell>
                      <TableCell align="right">
                        <b>
                          {/* {parseFloat(item.totalAmount).toFixed(2)} */}{" "}
                          2200.00
                        </b>
                      </TableCell>
                    </TableRow>
                    {/* ))} */}
                  </TableBody>
                </Table>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableCell className="billammount">
                        <p className="billammount-head">
                          Amount&nbsp;&nbsp;:&nbsp;
                        </p>
                        <p className="billammount-head">
                          GST&nbsp;&nbsp;:&nbsp;
                        </p>
                      </TableCell>
                      <TableCell className="bill-ammount">
                        <p className="bill-ammount-head">
                          &#8377; 14,660.00
                          {/* {parseFloat(SingleOrder?.poDetails?.netAmount).toFixed(2)} */}
                        </p>
                        <p className="bill-ammount-head">
                          &#8377;&nbsp; 2,660.00
                          {/* {parseFloat(SingleOrder?.poDetails?.gst).toFixed(2)} */}
                        </p>
                      </TableCell>
                    </TableRow>
                    <TableRow className="total-bill">
                      <TableCell className="billammount">
                        <p className="billammount-head">
                          Total Amount&nbsp;&nbsp;:&nbsp;
                        </p>
                      </TableCell>
                      <TableCell className="bill-ammount">
                        <p className="bill-ammount-head">
                          &#8377; 17,320.00
                          {/* {parseFloat(SingleOrder?.poDetails?.grandTotal).toFixed(2)} */}
                        </p>
                      </TableCell>
                    </TableRow>
                    <TableRow className="billpurchase"></TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </Paper>
          </div>
        </div>
      </div>
    </div>
  );
}

export default InwardsBillPurchase;
