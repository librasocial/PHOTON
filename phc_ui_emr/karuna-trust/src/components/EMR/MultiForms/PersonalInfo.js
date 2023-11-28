import React from "react";
import { Row, Col, Form } from "react-bootstrap";

function PersonalInfo({ formData, setFormData }) {
  return (
    <React.Fragment>
      <Row>
        <Col md={2}></Col>
        <Col md={10}>
          <Row>
            <Col md={3}>
              <div className="col-container">
                <Form.Group controlId="exampleForm.Facility">
                  <Form.Label className="require">
                    Material Status <span style={{ color: "red" }}>*</span>
                  </Form.Label>
                  <Form.Select
                    aria-label="Default select select example"
                    value={formData.material}
                    onChange={(event) =>
                      setFormData({ ...formData, material: event.target.value })
                    }
                  >
                    <option value="">Select Status</option>
                    <option value="2">Single</option>
                    <option value="3">Married</option>
                    <option value="4">Divorced</option>
                    <option value="5">Separated</option>
                    <option value="6">Widowed</option>
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div className="col-container">
                <Form.Group controlId="exampleForm.Facility">
                  <Form.Label className="require">Religion </Form.Label>
                  <Form.Select
                    aria-label="Default select select example"
                    value={formData.religion}
                    onChange={(event) =>
                      setFormData({ ...formData, religion: event.target.value })
                    }
                  >
                    <option value="">Select religion</option>
                    <option value="2">Single</option>
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
                  <Form.Label className="require">Caste</Form.Label>
                  <Form.Control
                    type="text"
                    value={formData.caste}
                    placeholder="Enter caste here"
                    onChange={(event) =>
                      setFormData({ ...formData, caste: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>
          <br />
          <Row>
            <Col md={3}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_fname"
                  controlId="exampleForm.FName"
                >
                  <Form.Label className="require">Ocupation </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Ocupation here"
                    value={formData.ocupation}
                    onChange={(event) =>
                      setFormData({
                        ...formData,
                        ocupation: event.target.value,
                      })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_mname"
                  controlId="exampleForm.MName"
                >
                  <Form.Label className="require">
                    Annual Income (in rupees)
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter income amount here"
                    value={formData.income}
                    onChange={(event) =>
                      setFormData({ ...formData, income: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={4}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_lname"
                  controlId="exampleForm.LName"
                >
                  <Form.Label className="require">
                    Educational status
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter educational status here"
                    value={formData.education}
                    onChange={(event) =>
                      setFormData({
                        ...formData,
                        education: event.target.value,
                      })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>
          <br />
          <Row>
            <Col md={3}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_fname"
                  controlId="exampleForm.FName"
                >
                  <Form.Label className="require">
                    Father/Spouse name{" "}
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter name here"
                    value={formData.fathername}
                    onChange={(event) =>
                      setFormData({
                        ...formData,
                        fathername: event.target.value,
                      })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div className="col-container">
                <Form.Group controlId="exampleForm.Facility">
                  <Form.Label className="require">
                    Patient primary language{" "}
                  </Form.Label>
                  <Form.Select
                    aria-label="Default select select example"
                    value={formData.language}
                    onChange={(event) =>
                      setFormData({ ...formData, language: event.target.value })
                    }
                  >
                    <option value="">Select language</option>
                    <option value="2">Kannada</option>
                    <option value="3">Hindi</option>
                    <option value="4">Telugu</option>
                    <option value="5">Tamil</option>
                    <option value="6">Malayalam</option>
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div className="col-container">
                <Form.Group controlId="exampleForm.Facility">
                  <Form.Label className="require">
                    can patient speak English ?{" "}
                  </Form.Label>
                  <Form.Select
                    aria-label="Default select select example"
                    value={formData.language}
                    onChange={(event) =>
                      setFormData({ ...formData, language: event.target.value })
                    }
                  >
                    <option value="">Select speaking status</option>
                    <option value="2">Yes</option>
                    <option value="3">No</option>
                    <option value="4">Partial</option>
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
          </Row>

          <br />
          <Row>
            <Col md={3}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_fname"
                  controlId="exampleForm.FName"
                >
                  <Form.Label className="require">Birth place </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter birth place"
                    value={formData.Bplace}
                    onChange={(event) =>
                      setFormData({ ...formData, Bplace: event.target.value })
                    }
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div className="col-container">
                <Form.Group
                  className="mb-3_mname"
                  controlId="exampleForm.MName"
                >
                  <Form.Label className="require">
                    Birth identifier 1
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Birth identifier"
                    value={formData.Bidentifier1}
                    onChange={(event) =>
                      setFormData({
                        ...formData,
                        Bidentifier1: event.target.value,
                      })
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
                  <Form.Label className="require">
                    Birth identifier 2
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Birth identifier"
                    value={formData.Bidentifier2}
                    onChange={(event) =>
                      setFormData({
                        ...formData,
                        Bidentifier2: event.target.value,
                      })
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

export default PersonalInfo;
