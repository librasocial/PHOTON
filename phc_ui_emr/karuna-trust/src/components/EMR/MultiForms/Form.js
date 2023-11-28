import React, { useState } from "react";
import PersonalInfo from "./PersonalInfo";
import OtherInfo from "./OtherInfo";
import { Col, Row, Button, Modal, Container } from "react-bootstrap";
import "../../../css/MultiForms.css";
import { Link } from "react-router-dom";
import Register from "./Register";

function Form() {
  const [page, setPage] = useState(0);
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    firstName: "",
    lastName: "",
    username: "",
    nationality: "",
    other: "",
  });

  const FormTitles = ["Sign Up", "Personal Info", "Other"];
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const PageDisplay = () => {
    if (page === 0) {
      return <Register formData={formData} setFormData={setFormData} />;
    } else if (page === 1) {
      return <PersonalInfo formData={formData} setFormData={setFormData} />;
    } else {
      return <OtherInfo formData={formData} setFormData={setFormData} />;
    }
  };

  return (
    <div className="regForm">
      <div className="DivHeader">
        <h1>Register New Patient</h1>
      </div>
      <hr />
      <Row>
        <Col md={3}>
          <div className="Note">
            <span className="note">Note: </span>Fields marked with{" "}
            <span style={{ color: "red" }}>*</span> are mandatory
          </div>
        </Col>
        <Col md={9}>
          <div className="pBtn">
            {/* <Button className="btn btn-primary">Personal</Button>  
              <Button className="btn btn-primary">address</Button> 
              <Button className="btn btn-primary">contact</Button> */}
            {page === 0 ? (
              <div>
                <Button className="pBtn1">&#9900;&nbsp;Patient Details</Button>
                <Button disabled className="pBtn2">
                  Demographic
                </Button>
                <Button disabled className="pBtn3">
                  Address
                </Button>
              </div>
            ) : page === 1 ? (
              <div>
                <Button className="pBtn1">&#10003;&nbsp;Patient Details</Button>
                <Button className="pBtn2 ">&#9900;&nbsp;Demographic</Button>
                <Button disabled className="pBtn3">
                  Address
                </Button>
              </div>
            ) : (
              <div>
                <Button className="pBtn1 ">
                  &#10003;&nbsp;Patient Details
                </Button>
                <Button className="pBtn2">&#10003;&nbsp;Demographic</Button>
                <Button className="pBtn3">&#9900;&nbsp;Address</Button>
              </div>
            )}
          </div>
          <div className="pBtnM" align="center">
            {/* <Button className="btn btn-primary">Personal</Button>  
              <Button className="btn btn-primary">address</Button> 
              <Button className="btn btn-primary">contact</Button> */}
            {page === 0 ? (
              <div>
                <Button className="pBtn1">&#9900;&nbsp;Patient Details</Button>
              </div>
            ) : page === 1 ? (
              <div>
                <Button className="pBtn2 ">&#9900;&nbsp;Demographic</Button>
              </div>
            ) : (
              <div>
                <Button className="pBtn3">&#9900;&nbsp;Address</Button>
              </div>
            )}
          </div>
        </Col>
      </Row>
      <Row>
        <Col md={4}></Col>
        <Col md={8} style={{ width: "100%" }}>
          <Row>
            <div>
              <div className="body">{PageDisplay()}</div>
              <br />
              <hr />
              <br />
              <div className="regFooter">
                {page === 0 ? (
                  ""
                ) : (
                  <Button
                    align="left"
                    className="regBtnPC btn1"
                    onClick={() => {
                      setPage((currPage) => currPage - 1);
                    }}
                  >
                    Previous
                  </Button>
                )}

                <Button
                  align="right"
                  className="regBtnN btn3"
                  onClick={() => {
                    if (page === FormTitles.length - 1) {
                      alert("FORM SUBMITTED");
                    } else {
                      setPage((currPage) => currPage + 1);
                    }
                  }}
                >
                  {page === FormTitles.length - 1 ? "Submit" : "Next"}
                </Button>
                <Button
                  align="right"
                  className="regBtnPC btn2"
                  onClick={handleShow}
                >
                  Cancel
                </Button>
              </div>
            </div>
          </Row>
        </Col>
      </Row>

      <Modal show={show} onHide={handleClose}>
        <Container>
          <Row>
            <Col>
              <h4 className="logout-head">Cancel Registration ?</h4>
            </Col>
          </Row>
          <Row>
            <Col>
              <p className="log-text">
                Are you sure you want to cancel patient registration?
                <br />
                All the data will be lost.
              </p>
            </Col>
          </Row>
          <Row>
            <Col className="btn1">
              <Button
                variant="outline-secondary regContinue"
                onClick={handleClose}
              >
                No, continue
              </Button>
            </Col>
            <Col className="btn2">
              <Button
                as={Link}
                variant="secondary regCancel"
                to="/registerNewPatient"
              >
                Yes, Cancel
              </Button>
            </Col>
          </Row>
        </Container>
      </Modal>
    </div>
  );
}

export default Form;
