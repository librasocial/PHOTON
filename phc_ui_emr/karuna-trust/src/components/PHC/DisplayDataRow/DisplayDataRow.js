import React, { useState } from "react";
import { useEffect } from "react";
import { Col, Row, Button } from "react-bootstrap";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";

function DisplayDataRow(props) {
  let displayData = props.displayData;
  let facilityType = props.facilityType;

  return (
    <div>
      {props.type !== "register-abdm" && (
        <Row>
          {(props.page != "Assign-Process" ||
            props.page != "SC-Assign-Process" ||
            props.page != "Sub-Center") &&
            props.addNewRow && (
              <Col lg={4} md={4} className="subcenter-card">
                <div className="radio_div">
                  <Row>
                    <Col lg={3} md={3}>
                      {props.page == "Create Staff" ||
                      props.page == "Assign-Process" ? (
                        <img
                          className="staff-photo"
                          src="../img/super/user.png"
                        />
                      ) : (
                        (props.page == "Sub-Center" ||
                          props.page == "SC-Assign-Process") && (
                          <img
                            className="staff-photo"
                            src="../img/super/new-hospital.png"
                          />
                        )
                      )}
                    </Col>
                    <Col lg={9} md={9}>
                      <Row>
                        <Col md={10} className="staff-div">
                          <p className="total-staff">
                            {props.page == "Create Staff" ? (
                              <b>Staff Name</b>
                            ) : (
                              <b>Sub-Center Name</b>
                            )}
                          </p>
                          <p className="total-staff">
                            <span style={{ color: "#707070" }}>
                              {props.page == "Create Staff"
                                ? "Staff No:"
                                : "SC/HWC Code:"}
                            </span>
                          </p>
                          <p className="total-staff">
                            <span style={{ color: "#707070" }}>
                              {props.page == "Create Staff"
                                ? "Staff Designation:"
                                : "Contact No:"}
                            </span>
                          </p>
                        </Col>
                        <Col md={2}>
                          <i
                            className="fa fa-close close-staff-style"
                            onClick={props.removeRow}
                          ></i>
                        </Col>
                      </Row>
                    </Col>
                  </Row>
                  <hr />
                  <Row className="staff-footer">
                    <Col lg={6} md={6}>
                      <div style={{ marginLeft: "5%" }}>
                        <div
                          className={`checkbox`}
                          style={{ opacity: "0.6", cursor: "context-menu" }}
                        >
                          <div className="checkbox__ball">
                            <span className="status-staff-text">
                              {"In-Active"}
                            </span>
                          </div>
                        </div>
                      </div>
                    </Col>
                    <Col lg={6} md={6} align="right" className="editstaffmem">
                      <Button
                        variant="link"
                        className="selectActLink"
                        onClick={props.openModale}
                      >
                        Enter Details
                      </Button>
                    </Col>
                  </Row>
                </div>
              </Col>
            )}
        </Row>
      )}
      {props.type !== "register-abdm" && (
        <Row>
          {displayData &&
            displayData.map((subCenters, i) => (
              <Col
                lg={4}
                md={4}
                key={i}
                className="subcenter-card"
                onClick={(e) => {
                  props.page == "Assign-Process"
                    ? props.updateRadioGroup(i, subCenters)
                    : props.page == "SC-Assign-Process" &&
                      props.createsubCenterFac(subCenters.uuid);
                }}
              >
                <div
                  className={
                    props.page == "Assign-Process" && props.radioState === i
                      ? "radio_div_active"
                      : "radio_div"
                  }
                >
                  <Row>
                    <Col lg={3} md={3}>
                      {subCenters.photo ? (
                        <>
                          {(props.page == "Create Staff" ||
                            props.page == "Assign-Process" ||
                            props.page == "Sub-Center" ||
                            props.page == "SC-Assign-Process") && (
                            <img
                              className="staff-photo"
                              src={subCenters.photo}
                            />
                          )}
                        </>
                      ) : (
                        <>
                          {props.page == "Create Staff" ||
                          props.page == "Assign-Process" ? (
                            <img
                              className="staff-photo"
                              src="../img/super/user.png"
                            />
                          ) : (
                            (props.page == "Sub-Center" ||
                              props.page == "SC-Assign-Process") && (
                              <img
                                className="staff-photo"
                                src="../img/super/new-hospital.png"
                              />
                            )
                          )}
                        </>
                      )}
                    </Col>
                    <Col lg={9} md={9}>
                      <div className="staff-div">
                        <p className="total-staff">
                          {props.page == "Create Staff" ||
                          props.page == "Assign-Process" ? (
                            <b>
                              {subCenters.salutation} {subCenters.name}
                            </b>
                          ) : (
                            <b>{subCenters.name}</b>
                          )}
                        </p>
                        <p className="total-staff">
                          <span style={{ color: "#707070" }}>
                            {props.page == "Create Staff" ||
                            props.page == "Assign-Process"
                              ? "Staff No:"
                              : "SC/HWC Code:"}
                          </span>
                          <span>&nbsp;&nbsp;{subCenters?.code}</span>
                        </p>
                        <p className="total-staff">
                          <span style={{ color: "#707070" }}>
                            {props.page == "Create Staff" ||
                            props.page == "Assign-Process"
                              ? "Staff Designation:"
                              : "Contact No:"}
                          </span>
                          <span>
                            &nbsp;&nbsp;
                            {props.page == "Create Staff" ||
                            props.page == "Assign-Process" ? (
                              subCenters?.staffRole
                            ) : (
                              <>
                                {" "}
                                {subCenters?.contact && (
                                  <>+91-{subCenters?.contact}</>
                                )}{" "}
                              </>
                            )}
                          </span>
                        </p>
                      </div>
                    </Col>
                  </Row>
                  {props.page != "Assign-Process" &&
                    props.page != "SC-Assign-Process" && <hr />}
                  {props.page != "Assign-Process" &&
                    props.page != "SC-Assign-Process" && (
                      <Row className="staff-footer">
                        <Col lg={6} md={6}>
                          <div style={{ marginLeft: "5%" }}>
                            <div
                              className={`checkbox ${
                                subCenters?.status && "checkbox--on"
                              }`}
                              onClick={(e) => {
                                props.page == "Create Staff"
                                  ? props.setStatusEnable(
                                      e,
                                      i,
                                      subCenters?.uuid,
                                      subCenters?.staffRole
                                    )
                                  : props.setStatusEnable(
                                      e,
                                      i,
                                      subCenters?.uuid
                                    );
                              }}
                            >
                              <div className="checkbox__ball">
                                <span className="status-staff-text">
                                  {subCenters?.status == false
                                    ? "In-Active"
                                    : "Active"}
                                </span>
                              </div>
                            </div>
                          </div>
                        </Col>
                        <Col
                          lg={6}
                          md={6}
                          align="right"
                          className="editstaffmem"
                        >
                          <Button
                            variant="link"
                            className="selectActLink"
                            onClick={(e) => props.updateData(subCenters?.uuid)}
                          >
                            Edit Details
                          </Button>
                        </Col>
                      </Row>
                    )}
                </div>
              </Col>
            ))}
        </Row>
      )}
      {props.type === "register-abdm" && (
        <Row>
          {displayData && props.data_type === "object" ? (
            <React.Fragment>
              <Col lg={4} md={4} className="subcenter-card">
                <div className="radio_div">
                  <Row>
                    <Col lg={3} md={3}>
                      {displayData.properties.photo ? (
                        <>
                          <img
                            className="staff-photo"
                            src={displayData.properties.photo}
                          />
                        </>
                      ) : (
                        <>
                          <img
                            className="staff-photo"
                            src="../img/super/new-hospital.png"
                          />
                        </>
                      )}
                    </Col>
                    <Col lg={9} md={9}>
                      <div className="staff-div">
                        <p className="total-staff">
                          <b>{displayData.properties.name}</b>
                        </p>
                        <p className="total-staff">
                          <span style={{ color: "#707070" }}>SC/HWC Code:</span>
                          <span>&nbsp;&nbsp;{displayData.properties.code}</span>
                        </p>
                        <p className="total-staff">
                          <span style={{ color: "#707070" }}>Contact No:</span>
                          <span>
                            &nbsp;&nbsp;{" "}
                            {displayData?.properties?.contact && (
                              <>+91-{displayData?.properties?.contact}</>
                            )}
                          </span>
                        </p>
                      </div>
                    </Col>
                  </Row>
                  <hr />

                  <Row className="staff-footer">
                    <Button
                      variant="link"
                      className="selectActLink"
                      onClick={props.handleRegisterOpen}
                    >
                      {/* Register for ABDM */}
                      {facilityType.some(
                        (e) =>
                          e.name === displayData.properties.name &&
                          ((e.types[0] === "HIP" && e.types[1] === "HIU") ||
                            (e.types[0] === "HIU" && e.types[1] === "HIP"))
                      ) ? (
                        <>Register for ABDM ✅</>
                      ) : (
                        <>Register for ABDM</>
                      )}
                    </Button>
                  </Row>
                </div>
              </Col>
            </React.Fragment>
          ) : (
            <React.Fragment>
              {displayData &&
                displayData.map((subCenters, i) => (
                  <Col lg={4} md={4} key={i} className="subcenter-card">
                    <div className="radio_div">
                      <Row>
                        <Col lg={3} md={3}>
                          {subCenters.photo ? (
                            <>
                              <img
                                className="staff-photo"
                                src={subCenters.photo}
                              />
                            </>
                          ) : (
                            <>
                              <img
                                className="staff-photo"
                                src="../img/super/new-hospital.png"
                              />
                            </>
                          )}
                        </Col>
                        <Col lg={9} md={9}>
                          <div className="staff-div">
                            <p className="total-staff">
                              <b>{subCenters.name}</b>
                            </p>
                            <p className="total-staff">
                              <span style={{ color: "#707070" }}>
                                SC/HWC Code:
                              </span>
                              <span>&nbsp;&nbsp;{subCenters?.code}</span>
                            </p>
                            <p className="total-staff">
                              <span style={{ color: "#707070" }}>
                                Contact No:
                              </span>
                              <span>
                                &nbsp;&nbsp;
                                {subCenters?.contact && (
                                  <>+91-{subCenters?.contact}</>
                                )}
                              </span>
                            </p>
                          </div>
                        </Col>
                      </Row>
                      <hr />
                      <Row className="staff-footer">
                        <Button
                          variant="link"
                          className="selectActLink"
                          onClick={(e) => {
                            props.handleRegisterOpen(subCenters?.uuid);
                          }}
                        >
                          {" "}
                          {/* {facilityType
                            .map((e) => e.name)
                            .includes(subCenters.name) ? (
                            facilityType.map((e) => {
                              if (e.name.includes(subCenters.name)) {
                                if (
                                  e.types[0] === "HIP" &&
                                  e.types[1] === "HIU"
                                ) {
                                  return <>Register for ABDM ✅</>;
                                } else if (
                                  e.types[0] === "HIU" &&
                                  e.types[1] === "HIP"
                                ) {
                                  return <>Register for ABDM ✅</>;
                                } else return <>Register for ABDM</>;
                              }
                            })
                          ) : (
                            <>Register for ABDM</>
                          )} */}
                          {facilityType.some(
                            (e) =>
                              e.name === subCenters.name &&
                              ((e.types[0] === "HIP" && e.types[1] === "HIU") ||
                                (e.types[0] === "HIU" && e.types[1] === "HIP"))
                          ) ? (
                            <>Register for ABDM ✅</>
                          ) : (
                            <>Register for ABDM</>
                          )}
                        </Button>
                      </Row>
                    </div>
                  </Col>
                ))}
            </React.Fragment>
          )}
        </Row>
      )}
    </div>
  );
}

export default DisplayDataRow;
