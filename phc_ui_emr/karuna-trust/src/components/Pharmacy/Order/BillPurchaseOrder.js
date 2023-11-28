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
import "../../../css/Services.css";
import "./BillPurchaseOrder.css";
import "../PharmacyTab.css";
import { useDispatch, useSelector } from "react-redux";
import { loadSingleOrder } from "../../../redux/actions";
import Pdf from "react-to-pdf";
import jsPDF from "jspdf";
import { renderToString } from "react-dom/server";

function BillPurchaseOrder(props) {
  const pdfRef = React.createRef();
  let orderId = props.orderId;
  let dispatch = useDispatch();
  const { SingleOrder } = useSelector((state) => state.data);

  useEffect(() => {
    dispatch(loadSingleOrder(orderId));
  }, [orderId]);

  const [indentStatus, setIndentStatus] = useState(false);
  const [viewTable, setViewTable] = useState([]);

  useEffect(() => {
    if (SingleOrder.poDetails != null) {
      setIndentStatus(false);
      setViewTable(SingleOrder.poDetails.poItems);
    } else if (SingleOrder.indentItems) {
      setIndentStatus(true);
      setViewTable(SingleOrder.indentItems);
    }
  }, [SingleOrder]);

  const onClickChange = () => {
    props.setViewPage(false);
    setIndentStatus(false);
    props.setPage(0);
  };
  const handleClick = () => {};
  const Print = () => {
    // let printContents = document.getElementById('pdgdown').innerHTML;
    // let originalContents = document.body.innerHTML;
    // document.body.innerHTML = printContents;
    window.print(document.getElementById("pdgdown").innerHTML);
    // originalContents = document.body.innerHTML;
    // document.getElementById('pdgdown').innerHTML = printContents;
  };

  const onButtonClick = () => {
    let printContents = document.getElementById("pdgdown").innerHTML;
    document.body.innerHTML = printContents;
    let originalContents = document.body.innerHTML;
    const string = renderToString(originalContents);
    const pdf = new jsPDF(string);
    pdf.fromHTML(string);
    pdf.save("pdf");
  };

  return (
    <div>
      <div className="regHeader">
        <h1 className="register-Header">
          {indentStatus == true ? <> Indent </> : <> Purchase Order </>}
        </h1>
      </div>
      <hr style={{ margin: "0px" }} />
      <div className="bill-form">
        <Breadcrumb className="order-crumb">
          <Breadcrumb.Item
            className="pur-order-breadcrumb"
            onClick={onClickChange}
          >
            Orders
          </Breadcrumb.Item>
          <Breadcrumb.Item active>
            {indentStatus == true ? <> #ID-0012345 </> : <> #PO-00012345 </>}
          </Breadcrumb.Item>
        </Breadcrumb>
        <div ref={pdfRef} id="pdgdown" style={{ width: "100%" }}>
          <div className="pro-tab purchase-order">
            <Row className="billorder-border">
              <Col md={9}>
                <h1 className="billorder-head">
                  {indentStatus == true ? (
                    <> Indent Number : # ID-0012345 </>
                  ) : (
                    <> Purchase Order : # PO-00012345 </>
                  )}
                </h1>
              </Col>
              <Col md={3} className="downprint">
                <Row>
                  <Col md={6} className="printicon">
                    <Pdf targetRef={pdfRef} filename="example.pdf">
                      {({ toPdf }) => (
                        <h1
                          className="billorder-head print-head down-head"
                          onClick={() => {
                            toPdf();
                          }}
                        >
                          <i
                            className="fa fa-download downpdf"
                            aria-hidden="true"
                          ></i>
                          &nbsp; Download PDF
                        </h1>
                      )}
                    </Pdf>
                  </Col>
                  <Col md={1}>
                    <div className="bill-line"></div>
                  </Col>
                  <Col md={5}>
                    <h1 className="billorder-head print-head" onClick={Print}>
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
                    <p className="po-body">{SingleOrder.status}</p>
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
                      {SingleOrder.supplierName}
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
              className="my-paper order-table"
            >
              <TableContainer sx={{ maxHeight: 440 }}>
                <Table
                  stickyHeader
                  aria-label="sticky table"
                  className="my-paper-table"
                >
                  <TableHead className="my-paper-table-head">
                    <TableRow>
                      <TableCell align="left">Product Name</TableCell>
                      <TableCell align="left">UOM</TableCell>
                      <TableCell align="left">
                        {indentStatus == true
                          ? "Indent Quantity"
                          : "PO Quantity"}
                      </TableCell>
                      {indentStatus == false && (
                        <TableCell align="left">PO Rate</TableCell>
                      )}
                      {indentStatus == false && (
                        <TableCell align="left">CGST %</TableCell>
                      )}
                      {indentStatus == false && (
                        <TableCell align="left">CGST AMT</TableCell>
                      )}
                      {indentStatus == false && (
                        <TableCell align="left">SGST %</TableCell>
                      )}
                      {indentStatus == false && (
                        <TableCell align="left">SGST AMT</TableCell>
                      )}
                      {indentStatus == false && (
                        <TableCell align="left">Total Amount</TableCell>
                      )}
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {viewTable.map((item, index) => (
                      <TableRow key={index}>
                        <TableCell align="left">
                          <b>{item.productName}</b>
                        </TableCell>
                        <TableCell align="left">{item.uom}</TableCell>
                        <TableCell align="left">{item.quantity}</TableCell>
                        {indentStatus == false && (
                          <TableCell align="left">{item.rate}</TableCell>
                        )}
                        {indentStatus == false && (
                          <TableCell align="left">
                            {item.cgstPercentage}
                          </TableCell>
                        )}
                        {indentStatus == false && (
                          <TableCell align="left">
                            {parseFloat(item.cgstAmount).toFixed(2)}
                          </TableCell>
                        )}
                        {indentStatus == false && (
                          <TableCell align="left">
                            {item.sgstPercentage}
                          </TableCell>
                        )}
                        {indentStatus == false && (
                          <TableCell align="left">
                            {parseFloat(item.sgstAmount).toFixed(2)}
                          </TableCell>
                        )}
                        {indentStatus == false && (
                          <TableCell align="left">
                            <b>{parseFloat(item.totalAmount).toFixed(2)}</b>
                          </TableCell>
                        )}
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
                <Table>
                  <TableBody>
                    {indentStatus == false && (
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
                            &#8377;{" "}
                            {parseFloat(
                              SingleOrder?.poDetails?.netAmount
                            ).toFixed(2)}
                          </p>
                          <p className="bill-ammount-head">
                            &#8377;&nbsp;{" "}
                            {parseFloat(SingleOrder?.poDetails?.gst).toFixed(2)}
                          </p>
                        </TableCell>
                      </TableRow>
                    )}
                    {indentStatus == false && (
                      <TableRow className="total-bill">
                        <TableCell className="billammount">
                          <p className="billammount-head">
                            Total Amount&nbsp;&nbsp;:&nbsp;
                          </p>
                        </TableCell>
                        <TableCell className="bill-ammount">
                          <p className="bill-ammount-head">
                            &#8377;{" "}
                            {parseFloat(
                              SingleOrder?.poDetails?.grandTotal
                            ).toFixed(2)}
                          </p>
                        </TableCell>
                      </TableRow>
                    )}
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

export default BillPurchaseOrder;
