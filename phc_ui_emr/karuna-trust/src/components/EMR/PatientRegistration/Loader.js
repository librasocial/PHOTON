import React from "react";
import { Button } from "react-bootstrap";
import { Link, NavLink } from "react-router-dom";

function Loader(props) {
  function loderCancel() {
    props.setNewLoading(false);
  }
  return (
    <div>
      {props.isLoading1 && (
        <div className="loder-container" align="center">
          <div className="loadBox" align="center">
            <div>
              {props.citizenPatientId_id ||
              props.citizenuuid ||
              props.searchDataID ? (
                <h3 className="load-header">Updating Patient Details</h3>
              ) : (
                <h3 className="load-header">Registering New Patient</h3>
              )}
            </div>
            <br></br>
            <div>
              <img
                style={{ width: "80px", height: "80px" }}
                className=""
                src="../img/circle.png"
              />
            </div>
            <br />
            {props.citizenPatientId_id ||
            props.citizenuuid ||
            props.searchDataID ? (
              <div>
                <p className="">
                  Please wait while we updating patient details into our records
                </p>
              </div>
            ) : (
              <div>
                <p className="">
                  Please wait while we register new patient into our records
                </p>
              </div>
            )}
          </div>
        </div>
      )}
      {props.isLoading2 && (
        <div className="loder-container" align="center">
          <div className="loadBox" align="center">
            <div>
              {props.citizenPatientId_id ||
              props.citizenuuid ||
              props.searchDataID ? (
                <h3 className="load-header">Updating Patient Details</h3>
              ) : (
                <h3 className="load-header">Registering New Patient</h3>
              )}
              <br></br>
            </div>
            <div>
              <img
                style={{ width: "80px", height: "80px" }}
                className=""
                src="../img/circle2.png"
              />
            </div>
            <br />
            <div>
              <p className="">
                Please wait while we register new patient into our records
              </p>
            </div>
          </div>
        </div>
      )}
      {props.isNewLoading && (
        <div className="loder-container" align="center">
          <div className="loadBox1" align="center">
            <div>
              {props.citizenPatientId_id ||
              props.citizenuuid ||
              props.searchDataID ? (
                <p className="">Patient Updated Successfully</p>
              ) : (
                <p className="">Patient Registered Successfully</p>
              )}
            </div>
            <div>
              <img
                style={{ width: "80px", height: "80px" }}
                className="load-img3"
                src="../img/success-icon.png"
              />
            </div>
            <div>
              <hr className="hr" />
            </div>
            {props.citizenPatientId_id ||
            props.citizenuuid ||
            props.searchDataID ? (
              <div>
                <p className="load-text3">
                  Please go ahead and create visit for the registered patient.
                </p>
              </div>
            ) : (
              <div>
                <p className="load-text3">
                  Please go ahead and create visit for the newly registered
                  patient.
                </p>
              </div>
            )}
            <div>
              {props.searchHealthId ? (
                <Button
                  as={Link}
                  variant="outline-secondary"
                  className="loadBtn"
                  to={`/createEncounter/${props.searchDataID}/${props.types}`}
                  onClick={() => loderCancel(props.searchDataID)}
                >
                  Create Visit
                </Button>
              ) : (
                <>
                  {!props.citizenPatientId_id && !props.citizenuuid ? (
                    <Button
                      as={Link}
                      variant="outline-secondary"
                      className="loadBtn"
                      to={`/createEncounter/${props.patientId}/${props.types}`}
                      onClick={() => loderCancel(props.searchDataID)}
                    >
                      Create Visit
                    </Button>
                  ) : (
                    <div>
                      {!props.citizenPatientId_id ? (
                        <Button
                          as={Link}
                          variant="outline-secondary"
                          className="loadBtn"
                          to={`/createEncounter/${props.patientId}/${props.types}`}
                          onClick={() => loderCancel(props.patientId)}
                        >
                          Create Visit
                        </Button>
                      ) : (
                        <Button
                          as={Link}
                          variant="outline-secondary"
                          className="loadBtn"
                          to={`/createEncounter/${props.pid}/${props.types}`}
                          onClick={() => loderCancel(props.pid)}
                        >
                          Create Visit
                        </Button>
                      )}
                    </div>
                  )}
                </>
              )}
            </div>
            <div>
              <NavLink to="/EncounterSearch" className="ancher-Text">
                I will do it later
              </NavLink>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Loader;
