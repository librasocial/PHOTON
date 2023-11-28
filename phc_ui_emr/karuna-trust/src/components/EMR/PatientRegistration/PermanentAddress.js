import React from "react";
import { Row, Col, Form } from "react-bootstrap";
function PermanentAddress(props) {
  return (
    <div>
      <div className="col-container">
        <Form.Group controlId="exampleForm.Facility">
          <Form.Label className="require">
            {props.type == "present" ? "Present" : "Permanent"} Address
          </Form.Label>
          <Form.Control
            type="text"
            as="textarea"
            rows={3}
            aria-label="Default select select example"
            value={props.permanentAddress.addressLine || ""}
            name="addressLine"
            onChange={props.handlePermanentAddress}
          ></Form.Control>
        </Form.Group>
      </div>
      <Row>
        <Col md={6}>
          <div className="col-container">
            <Form.Group controlId="exampleForm.Facility">
              <Form.Label className="require">
                Country{" "}
                {props.type == "present" && (
                  <span style={{ color: "red" }}>*</span>
                )}
              </Form.Label>
              <Form.Select
                aria-label="Default select select example"
                value={props.permanentAddress.country || ""}
                name="country"
                onChange={props.handlePermanentAddress}
              >
                <option value="" disabled hidden>
                  Select Country
                </option>
                {props.user.content?.map((item, i) => (
                  <React.Fragment key={i}>
                    <option key={i}>
                      {item.target?.properties?.name}-
                      {item.target?.properties.countryCode}
                    </option>
                  </React.Fragment>
                ))}
              </Form.Select>
            </Form.Group>
          </div>
        </Col>
        <Col md={6}>
          <div className="col-container">
            <Form.Group controlId="exampleForm.Facility">
              <Form.Label className="require">
                State{" "}
                {props.type == "present" && (
                  <span style={{ color: "red" }}>*</span>
                )}
              </Form.Label>
              <Form.Select
                aria-label="Default select select example"
                value={props.permanentAddress.state || ""}
                name="state"
                onChange={props.handlePermanentAddress}
              >
                <option value="" disabled hidden>
                  Select State
                </option>

                {props.stateList && props.permanentAddress.state
                  ? props.stateList.map((states, i) => {
                      if (
                        props.permanentAddress.state.toLowerCase() ===
                        states.target.properties.name.toLowerCase()
                      ) {
                        return (
                          <option
                            value={states.target.properties.name}
                            select={states.target.properties.uuid}
                            key={i}
                          >
                            {states.target.properties.name}-
                            {states.target.properties.stateCode}
                          </option>
                        );
                      }
                    })
                  : props.stateList &&
                    props.stateList.map((states, i) => {
                      return (
                        <option
                          value={states.target.properties.name}
                          select={states.target.properties.uuid}
                          key={i}
                        >
                          {states.target.properties.name}-
                          {states.target.properties.stateCode}
                        </option>
                      );
                    })}
                {/* {props.stateList &&
                  props.stateList.map((states, i) => (
                    <option
                      value={states.target.properties.name}
                      select={states.target.properties.uuid}
                      key={i}
                    >
                      {states.target.properties.name}-
                      {states.target.properties.stateCode}
                    </option>
                  ))} */}
              </Form.Select>
            </Form.Group>
          </div>
        </Col>
      </Row>
      <Row>
        <Col md={6}>
          <div className="col-container">
            <Form.Group controlId="exampleForm.Facility">
              <Form.Label className="require">
                District{" "}
                {props.type == "present" && (
                  <span style={{ color: "red" }}>*</span>
                )}
              </Form.Label>
              <Form.Select
                aria-label="Default select select example"
                type="text"
                value={props.permanentAddress.district || ""}
                name="district"
                onChange={props.handlePermanentAddress}
              >
                {" "}
                <option value="" disabled hidden>
                  Select District
                </option>
                {
                  !props.permanentAddress.district
                    ? props.districtListForPermanent &&
                      props.districtListForPermanent.map((dist, i) => (
                        // console.log(dist.target.properties.name.toLowerCase()),
                        <option value={dist.target.properties.name} key={i}>
                          {dist.target.properties.name.charAt(0).toUpperCase() +
                            dist.target.properties.name.slice(1).toLowerCase()}
                        </option>
                      ))
                    : props.districtListForPermanent &&
                      props.districtListForPermanent.map((dist, i) => {
                        if (
                          dist.target.properties.name.toLowerCase() ===
                          props.permanentAddress.district.toLowerCase()
                        ) {
                          // console.log(dist.target.properties.name);
                          return (
                            <option value={dist.target.properties.name} key={i}>
                              {dist.target.properties.name
                                .charAt(0)
                                .toUpperCase() +
                                dist.target.properties.name
                                  .slice(1)
                                  .toLowerCase()}
                            </option>
                          );
                        }
                      })
                  // :
                  // props.districtListForPermanent &&
                  //   props.districtListForPermanent.map((dist, i) => (
                  //     // console.log(dist.target.properties.name.toLowerCase()),
                  //     <option value={dist.target.properties.name} key={i}>
                  //       {dist.target.properties.name.charAt(0).toUpperCase() +
                  //         dist.target.properties.name.slice(1).toLowerCase()}
                  //     </option>
                  //   ))
                }
                {/* {props.districtListForPermanent &&
                  props.districtListForPermanent.map((dist, i) => (
                    <option value={dist.target.properties.name} key={i}>
                      {dist.target.properties.name.charAt(0).toUpperCase() +
                        dist.target.properties.name.slice(1).toLowerCase()}
                      -{dist.target.properties.lgdCode}
                    </option>
                  ))} */}
              </Form.Select>
            </Form.Group>
          </div>
        </Col>
        <Col md={6}>
          <div className="col-container">
            <Form.Group controlId="exampleForm.Facility">
              <Form.Label className="require">
                Taluk{" "}
                {props.type == "present" && (
                  <span style={{ color: "red" }}>*</span>
                )}
              </Form.Label>
              <Form.Select
                aria-label="Default select select example"
                value={props.permanentAddress.taluk || ""}
                name="taluk"
                onChange={props.handlePermanentAddress}
              >
                <option value="" disabled hidden>
                  Select Taluk
                </option>
                {props.talukListForPermanent &&
                  props.talukListForPermanent.map((taluk, i) => (
                    <option value={taluk.target.properties.name} key={i}>
                      {taluk.target.properties.name.charAt(0).toUpperCase() +
                        taluk.target.properties.name.slice(1).toLowerCase()}
                      -{taluk.target.properties.lgdCode}
                    </option>
                  ))}
              </Form.Select>
            </Form.Group>
          </div>
        </Col>
      </Row>
      <Row>
        <Col md={6}>
          <div className="col-container">
            <Form.Group controlId="exampleForm.Facility">
              <Form.Label className="require">
                City / Village{" "}
                {props.type == "present" && (
                  <span style={{ color: "red" }}>*</span>
                )}
              </Form.Label>
              <Form.Select
                aria-label="Default select select example"
                value={props.permanentAddress.village || ""}
                name="village"
                onChange={props.handlePermanentAddress}
                data-val="true"
              >
                <option value="" disabled hidden>
                  Select City
                </option>
                {/* {props.MapVillageNames.map((item, i) => (
                                    <option data-isd2={item?.properties?.lgdCode} key={i}>{item?.properties.name}</option>
                                ))} */}
                {props.villageListForPermanent &&
                  props.villageListForPermanent.map((village, i) => (
                    <option
                      value={village.target.properties.name}
                      data-isd={village.target.properties?.lgdCode}
                      key={i}
                    >
                      {village.target.properties.name.charAt(0).toUpperCase() +
                        village.target.properties.name.slice(1).toLowerCase()}
                      -{village.target.properties.lgdCode}
                    </option>
                  ))}
              </Form.Select>
            </Form.Group>
          </div>
        </Col>
        <Col md={6}>
          <div className="col-container">
            <Form.Group className="mb-3_health" controlId="exampleForm.Health">
              <Form.Label className="require">Area / Street </Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter Area / Street Name Here"
                value={props.permanentAddress.area || ""}
                name="area"
                onChange={props.handlePermanentAddress}
              />
            </Form.Group>
          </div>
        </Col>
      </Row>
      <Row>
        <Col md={6}>
          <div className="col-container">
            <Form.Group className="mb-3_fname" controlid="exampleForm.FName">
              <Form.Label className="require">Pin-Code </Form.Label>
              <Form.Control
                type="phone"
                placeholder="Enter Pin-Code Here"
                autoComplete="off"
                value={props.permanentAddress.pinCode || ""}
                name="pinCode"
                maxLength={6}
                onChange={props.handlePermanentAddress}
                onKeyPress={(event) => {
                  if (!/[0-9]/.test(event.key)) {
                    event.preventDefault();
                  }
                }}
              />
            </Form.Group>
          </div>
        </Col>
      </Row>
    </div>
  );
}
export default PermanentAddress;
