import React, { useState, useEffect } from "react";
import { Collapse } from "reactstrap";
import { Row, Col } from "react-bootstrap";

function Collapsible({ children, ...props }) {
  const { title, title1, title2, title3, collapse } = props;
  const [isCollapse, setIsCollapse] = useState(collapse);
  const [icon, setIcon] = useState("bi bi-caret-up-fill up-mark");
  const toggle = () => {
    setIsCollapse(!isCollapse);
    setIcon((state) => {
      return state === "bi bi-caret-up-fill up-mark"
        ? "bi bi-caret-down-fill down-mark"
        : "bi bi-caret-up-fill up-mark";
    });
  };

  useEffect(() => {
    toggle();
  }, [collapse]);

  return (
    <div className="coll-panel">
      <Row className="vital-acc-head coll-panel-btn " onClick={() => toggle()}>
        <Col md={2} style={{ textAlign: "center" }}>
          <p className="lab-test-name" style={{ textAlign: "center" }}>
            <i className={icon} style={{ textAlign: "center" }} />
          </p>
        </Col>
        <Col md={3} style={{ textAlign: "left" }}>
          <p className="lab-test-name">
            <b>{title}</b>
          </p>
        </Col>
        <Col md={3} style={{ textAlign: "left" }}>
          <p className="lab-test-name">
            {title1},&nbsp;{title2}
          </p>
        </Col>
        <Col md={2} style={{ textAlign: "left" }}>
          <p className="lab-test-name">{title3}</p>
        </Col>
        <Col md={2}></Col>
      </Row>
      <Collapse className="border text-left p-2" isOpen={isCollapse}>
        {children}
      </Collapse>
    </div>
  );
}

Collapsible.defaultProps = {
  children: "Add node as a child",
  title: "Collapsible Panel",
  collapse: true,
};

export default Collapsible;
