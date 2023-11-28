import React from "react";
import { Col, Row, Container, Button, Modal } from "react-bootstrap";
import "./CancelOrder.css";

function CancelOrder(props) {
  const closeModal = () => {
    props.cancelOrderServiceClose(false);
  };
  var showModal = props.cancelOrderService;

  const backToOrder = () => {
    props.setPageChange(false);
    if (props.purchaseStatus == true) {
      props.setPurchaseStatus(false);
    } else if (props.indentStatus == true) {
      props.setIndentStatus(false);
    } else if (props.inwardsStatus == true) {
      props.setInwardsStatus(false);
    }
  };

  return (
    <Modal show={showModal} onHide={closeModal} className="cancel-order-modal">
      <Container>
        <Row>
          <Col>
            <h4 className="logout-head cancel-purchase">
              {props.purchaseStatus == true && "Cancel Purchase Order ?"}
              {props.indentStatus == true && "Cancel Indent Order ?"}
              {props.inwardsStatus == true && "Cancel Inwards ?"}
            </h4>
          </Col>
        </Row>
        <Row>
          <Col align="left">
            <p className="cancel-text">
              Are you sure, you want to cancel{" "}
              {props.purchaseStatus == true && "purchase order ?"}
              {props.indentStatus == true && "indent order ?"}
              {props.inwardsStatus == true && "inwards ?"}
            </p>
          </Col>
        </Row>
        <Row>
          <Col className="btn1">
            <Button
              variant="outline-secondary order-cancel"
              onClick={closeModal}
            >
              No, Keep in Draft
            </Button>
          </Col>
          <Col className="btn2">
            <Button
              variant="secondary order-cancel confirm-cancel"
              onClick={backToOrder}
            >
              Yes, Delete
            </Button>
          </Col>
        </Row>
      </Container>
    </Modal>
  );
}
export default CancelOrder;
