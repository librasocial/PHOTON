import React from "react";
import { Row, Col, Form } from "react-bootstrap";

function OtherInfo({ formData, setFormData }) {
  return (
    <React.Fragment>
      <Row>
        <Col md={2}></Col>
        <Col md={10}>
          <Row>
            <Col md={2}>
              <div className="col-container">
                <Form.Group controlId="exampleForm.Facility">
                  <Form.Label className="require">
                    Country <span style={{ color: "red" }}>*</span>
                  </Form.Label>
                  <Form.Select
                    aria-label="Default select select example"
                    value={formData.country}
                    onChange={(event) =>
                      setFormData({ ...formData, country: event.target.value })
                    }
                  >
                    <option value="India">India</option>
                    <option value="ind">Indonasia</option>
                    <option value="irn">Iran</option>
                    <option value="itl">Italy</option>
                    <option value="irq">Iraq</option>
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={2}>
              <div className="col-container">
                <Form.Group controlId="exampleForm.Facility">
                  <Form.Label className="require">
                    State <span style={{ color: "red" }}>*</span>
                  </Form.Label>
                  <Form.Select
                    aria-label="Default select select example"
                    value={formData.state}
                    onChange={(event) =>
                      setFormData({ ...formData, state: event.target.value })
                    }
                  >
                    <option value="krntk">Karnataka</option>
                    <option value="2">Maharastra</option>
                    <option value="3">Married</option>
                    <option value="4">Divorced</option>
                    <option value="5">Separated</option>
                    <option value="6">Widowed</option>
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={2}>
              <div className="col-container">
                <Form.Group controlId="exampleForm.Facility">
                  <Form.Label className="require">
                    City <span style={{ color: "red" }}>*</span>
                  </Form.Label>
                  <Form.Select
                    aria-label="Default select select example"
                    value={formData.city}
                    onChange={(event) =>
                      setFormData({ ...formData, city: event.target.value })
                    }
                  >
                    <option value="krntk">Karnataka</option>
                    <option value="2">Maharastra</option>
                    <option value="3">Married</option>
                    <option value="4">Divorced</option>
                    <option value="5">Separated</option>
                    <option value="6">Widowed</option>
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={4}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_health"
                  controlId="exampleForm.Health"
                >
                  <Form.Label className="require">
                    Area <span style={{ color: "red" }}>*</span>
                  </Form.Label>
                  <Form.Control
                    type="text"
                    value={formData.area}
                    placeholder="Enter caste here"
                    onChange={(event) =>
                      setFormData({ ...formData, area: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>
          <br />
          <Row>
            <Col md={5}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_mname"
                  controlId="exampleForm.MName"
                >
                  <Form.Label className="require">
                    Address line-1{" "}
                    <span style={{ fontStyle: "italic" }}>
                      (Present Address){" "}
                    </span>
                    <span style={{ color: "red" }}>*</span>
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter present address"
                    value={formData.address1}
                    onChange={(event) =>
                      setFormData({ ...formData, address1: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={5}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_lname"
                  controlId="exampleForm.LName"
                >
                  <Form.Label className="require">
                    Address line-2{" "}
                    <span style={{ fontStyle: "italic" }}>
                      (Permanent Address){" "}
                    </span>
                    <span style={{ color: "red" }}>*</span>
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Permanent address"
                    value={formData.address2}
                    onChange={(event) =>
                      setFormData({ ...formData, address1: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>
          <br />
          <Row>
            <Col md={2}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_fname"
                  controlId="exampleForm.FName"
                >
                  <Form.Label className="require">
                    Pin code <span style={{ color: "red" }}>*</span>
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter name here"
                    value={formData.Pcode}
                    onChange={(event) =>
                      setFormData({ ...formData, Pcode: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>

          <br />
          <Row>
            <Col md={2}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_fname"
                  controlId="exampleForm.FName"
                >
                  <Form.Label className="require">Mobile Number </Form.Label>
                  <Form.Control
                    type="tel"
                    placeholder="Enter mobile number"
                    value={formData.mobile}
                    onChange={(event) =>
                      setFormData({ ...formData, mobile: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={2}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_mname"
                  controlId="exampleForm.MName"
                >
                  <Form.Label className="require">Land line Number</Form.Label>
                  <Form.Control
                    type="tel"
                    placeholder="Enter Birth identifier"
                    value={formData.land}
                    onChange={(event) =>
                      setFormData({ ...formData, land: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_lname"
                  controlId="exampleForm.LName"
                >
                  <Form.Label className="require">Email Id</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="Enter Birth identifier"
                    value={formData.email}
                    onChange={(event) =>
                      setFormData({ ...formData, email: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>
        </Col>
      </Row>
    </React.Fragment>
  );
}

export default OtherInfo;
