import React, { useState, useRef, useEffect } from "react";
import { Button, Col, Row, Form } from "react-bootstrap";
import Webcam from "react-webcam";
import moment from "moment";
import { useHistory } from "react-router-dom";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";

let enteredhealthid;
function HealthIdWindow(props) {
  let healthiddata = props.healthiddata;

  const [errorvalue, setErrorvalue] = useState("");
  const [healthid, setHealthid] = useState("");
  const [isuploaded, setIsuploaded] = useState(false);

  const settingerrorvalue = () => {
    if (healthid === "") {
      setErrorvalue("Please enter ABHA Number");
    } else if (healthid.length < 14) {
      setErrorvalue("Please Enter 14 digit ABHA number");
    } else if (healthid.length === 14) {
      setErrorvalue("");
    }
  };

  useEffect(() => {
    settingerrorvalue();
  }, [healthid]);

  // Photo recapture button
  const retake = () => {
    setImgSrc("");
  };
  // Photo recapture button

  const webcamRef = useRef(null);
  const [imgSrc, setImgSrc] = useState(null);
  const [imagefile, setImagefile] = useState("");

  enteredhealthid = healthid;
  const capture = React.useCallback(async () => {
    const imageSrc = webcamRef.current.getScreenshot();
    function dataURLtoFile(dataurl, filename) {
      var arr = dataurl.split(","),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);

      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }

      return new File([u8arr], filename, { type: mime });
    }

    //Usage example:
    var file = dataURLtoFile(imageSrc, "hello.jpeg");

    setImagefile(file);
    setImgSrc(imageSrc);
  }, [webcamRef, setImgSrc]);

  // code for submit button
  const submitdata = () => {
    if (healthid === "") {
      setErrorvalue("Please enter ABHA Number");
    } else {
      if (healthid.length < 14) {
        setErrorvalue("Please Enter 14 digit ABHA number");
      } else if (healthid.length === 14) {
        setErrorvalue("");
        let staffid_uuid = sessionStorage.getItem("uuid");
        const storehealthdata = {
          citizenId: props.citizenid_uusid,
          staffId: staffid_uuid,
        };

        var storedhealthdata = {
          headers: serviceHeaders.myHeaders1,
          method: "POST",
          mode: "cors",
          body: JSON.stringify(storehealthdata),
        };

        fetch(`${constant.ApiUrl}/healthIds`, storedhealthdata)
          .then((res) => res.json())
          .then((res) => {
            const sucessstoreid = res["id"];
            if (res["msg"] == "successful") {
              const isoDateforgenerated = new Date().toISOString();
              const issuehealthid_citizen_data = {
                status: "GENERATED",
                timestamp: isoDateforgenerated,
                success: {
                  healthId: healthid,
                  firstName: healthiddata.firstname,
                  middleName: healthiddata.middlename,
                  lastName: healthiddata.lastname,
                  gender: healthiddata.citizenGender,
                  dob: healthiddata.dateOfBirth,
                  phone: healthiddata.contact,
                  generatedTimeStamp: isoDateforgenerated,
                },
              };

              var genaratedhealthdata = {
                headers: serviceHeaders.myHeaders1,
                method: "PATCH",
                mode: "cors",
                // "Access-Control-Allow-Origin": '*',
                body: JSON.stringify(issuehealthid_citizen_data),
              };

              //generated healthid
              fetch(
                `${constant.ApiUrl}/healthIds/${sucessstoreid}`,
                genaratedhealthdata
              )
                .then((res) => res.json())
                .then((res) => {
                  const generatedid = res["_id"];
                  if (res["status"] == "GENERATED") {
                    //print healthid
                    const isoDateforprinted = new Date().toISOString();
                    // print api
                    var printedhealthdata = {
                      status: "PRINTED",
                      timestamp: isoDateforprinted,
                    };
                    var printbodydata = {
                      headers: serviceHeaders.myHeaders1,
                      method: "PATCH",
                      mode: "cors",

                      body: JSON.stringify(printedhealthdata),
                    };
                    fetch(
                      `${constant.ApiUrl}/healthIds/${generatedid}`,
                      printbodydata
                    )
                      .then((res) => res.json())
                      .then((res) => {
                        const printedhealthid = res["_id"];
                        if (res["status"] == "PRINTED") {
                          //hanover healthid
                          const isoDateforhandover = new Date().toISOString();
                          // hanover api
                          const dandoverdata = {
                            status: "HANDEDOVER",
                            timestamp: isoDateforhandover,
                          };
                          var handoverbodydata = {
                            headers: serviceHeaders.myHeaders1,
                            method: "PATCH",
                            mode: "cors",
                            body: JSON.stringify(dandoverdata),
                          };
                          fetch(
                            `${constant.ApiUrl}/healthIds/${generatedid}`,
                            handoverbodydata
                          )
                            .then((res) => res.json())
                            .then((res) => {
                              const handoverid = res["_id"];
                              if (res["status"] == "HANDEDOVER") {
                                if (imagefile) {
                                  fetch(
                                    `${constant.ApiUrl}/healthIds/presignedUrl/?healthId=${enteredhealthid}`,
                                    serviceHeaders.getRequestOptions
                                  )
                                    .then((res) => res.json())
                                    .then((res) => {
                                      setLoading(true);
                                      let imageurl = res.preFetchURL;
                                      let authtoken5 =
                                        sessionStorage.getItem("token");
                                      let myHeaders5 = new Headers();
                                      let jwttoken5 =
                                        "Bearer " + "" + authtoken5;
                                      myHeaders5.append(
                                        "Content-Type",
                                        "multipart/form-data"
                                      );
                                      myHeaders5.append(
                                        "Access-Control-Allow-Origin",
                                        "*"
                                      );
                                      var requestOptionsimagedata = {
                                        headers: myHeaders5,
                                        method: "PUT",
                                        mode: "cors",
                                        body: imagefile,
                                      };

                                      fetch(imageurl, requestOptionsimagedata)
                                        // .then((data) => data.status
                                        .then((res1) => res1)
                                        .then((res1) => {
                                          const uploadstatus = res1;
                                          if (res1.status === 200) {
                                            setIsuploaded(true);
                                          } else {
                                            setIsuploaded(false);
                                          }
                                        });
                                    });
                                  // capture image data saving

                                  const isoDateforevidenced =
                                    new Date().toISOString();
                                  const evidentdata = {
                                    status: "EVIDENCED",
                                    timestamp: isoDateforevidenced,
                                    evidenced: {
                                      isEvidenced: isuploaded,
                                    },
                                  };
                                  var evidentbodydata = {
                                    headers: serviceHeaders.myHeaders1,
                                    method: "PATCH",
                                    mode: "cors",
                                    body: JSON.stringify(evidentdata),
                                  };
                                  //evidenced healthid
                                  fetch(
                                    `${constant.ApiUrl}/healthIds/${handoverid}`,
                                    evidentbodydata
                                  )
                                    .then((res) => res.json())
                                    .then((res) => {
                                      if (res["status"] == "EVIDENCED") {
                                        setTimeout(() => {
                                          setNewLoading(true);
                                          setLoading(false);
                                        }, 2500);
                                      }
                                    });
                                } else {
                                  alert("Capture the image..!");
                                }
                              }
                            });
                        }
                      });
                  } else {
                    //failure for generate
                    // failure api
                    const isofailure = new Date().toISOString();
                    const failuredata = {
                      status: "FAILURE",
                      timestamp: isofailure,
                      failure: {
                        errorInfo: "string",
                      },
                    };
                    var failurebodydata = {
                      headers: serviceHeaders.myHeaders1,
                      method: "PATCH",
                      mode: "cors",
                      body: JSON.stringify(failuredata),
                    };

                    fetch(
                      `${constant.ApiUrl}/healthIds/${sucessstoreid}`,
                      failurebodydata
                    )
                      .then((res) => res.json())
                      .then((res) => {
                        alert("unable to update, somthing went wrong!");
                        // setLoading(false)
                        setTimeout(() => {
                          // setNewLoading(true);
                          setLoading(false);
                        }, 2000);
                      });
                  }
                });
            } else {
              // failure api
              const isofailure = new Date().toISOString();
              const failuredata = {
                status: "FAILURE",
                timestamp: isofailure,
                failure: {
                  errorInfo: "string",
                },
              };
              var failurebodydata = {
                headers: serviceHeaders.myHeaders1,
                method: "PATCH",
                mode: "cors",
                body: JSON.stringify(failuredata),
              };

              fetch(
                `${constant.ApiUrl}/healthIds/${sucessstoreid}`,
                failurebodydata
              )
                .then((res) => res.json())
                .then((res) => {
                  alert("unable to update, somthing went wrong!");
                  // setLoading(false)
                  // setTimeout(() => {
                  // setNewLoading(true);
                  // setLoading(false);
                  // }, 2000);
                });
            }
          });
      } else {
        setErrorvalue("Please Enter 14 digit ABHA number");
      }
    }
  };
  // code for submit button

  const [isLoading, setLoading] = useState(false);
  const [isNewLoading, setNewLoading] = useState(false);
  const history = useHistory();

  const handleClose = () => {
    props.setIssueHealthIdWindow(false);
    setImgSrc("");
    setHealthid("");
  };

  function loderCancel() {
    setNewLoading(false);
    setImgSrc("");
    history.push("/");
    window.location.reload();
  }

  return (
    <div className="gen-div">
      {/* loader */}
      {isLoading && (
        <div className="loder-container" align="center">
          <div className="loadBox" align="center">
            <div>
              <h4 className="">Updating Health ID</h4>
            </div>
            <br></br>
            <div>
              <img
                style={{ width: "80px", height: "80px" }}
                className=""
                src="./../img/circle.png"
              />
            </div>
            <br />
            <div>
              <p className="">
                Please wait while we update resident into our records
              </p>
            </div>
          </div>
        </div>
      )}
      {isNewLoading && (
        <div className="loder-container" align="center">
          <div className="loadBox" align="center">
            <div>
              <p className="">
                Register ABHA Number(health id) updated successfully
              </p>
            </div>
            <br></br>
            <div>
              <img
                style={{ width: "80px", height: "80px" }}
                className=""
                src="./../img/circle2.png"
              />
            </div>
            <br />
            <div>
              <Button
                variant="outline-secondary"
                className="loadBtn"
                onClick={loderCancel}
              >
                okay{" "}
              </Button>
            </div>
          </div>
        </div>
      )}
      {/* loader */}
      <div className="regHeader">
        <h1 className="register-Header">Issue ABHA Number(Health ID)</h1>
        <hr />
      </div>
      <div className="health-id-div">
        <Row>
          <Col md={2}></Col>
          <Col md={8}>
            <div>
              <Row>
                <Col md={12}>
                  <Form className="HealthForm">
                    <Row className="healthIdRow">
                      <Col md={1} align="center">
                        <img
                          src="../img/admin.png"
                          className="dataImg"
                          alt="person-image"
                        />
                      </Col>
                      <Col md={4} align="center">
                        <div className="dataName">
                          <div>
                            <p className="dataP">{healthiddata.firstName}</p>
                          </div>
                          <div>
                            <p
                              className="dataP"
                              style={{ color: "red", paddingTop: "1%" }}
                            >
                              <em>Issue ABHA Number(Health id)</em>
                            </p>
                          </div>
                        </div>
                      </Col>
                      <Col md={1}>
                        <div className="dataGender" align="center">
                          <div>
                            <p>{healthiddata.gender}</p>
                          </div>
                        </div>
                      </Col>
                      <Col md={3}>
                        <div className="dataAge" align="center">
                          {healthiddata.hasOwnProperty("age") ? (
                            <p className="dataP">{healthiddata.age} yrs </p>
                          ) : (
                            <>
                              {healthiddata.dateOfBirth === "" ? (
                                ""
                              ) : (
                                <>
                                  {" "}
                                  {healthiddata.hasOwnProperty(
                                    "dateOfBirth"
                                  ) ? (
                                    <p className="dataP">
                                      {moment(new Date()).format("YYYY") -
                                        moment(
                                          new Date(healthiddata.dateOfBirth)
                                        ).format("YYYY")}{" "}
                                      yrs
                                    </p>
                                  ) : (
                                    ""
                                  )}{" "}
                                </>
                              )}
                            </>
                          )}
                        </div>
                        {healthiddata.hasOwnProperty("dateOfBirth") ? (
                          <>
                            {healthiddata.dateOfBirth === "" ? (
                              <p className="dataP" style={{ paddingTop: "1%" }}>
                                <em style={{ color: "red" }}></em>
                              </p>
                            ) : (
                              <>
                                <p
                                  className="dataP"
                                  style={{ paddingTop: "1%" }}
                                  align="center"
                                >
                                  {moment(
                                    new Date(healthiddata.dateOfBirth)
                                  ).format("DD MMM YYYY")}
                                </p>
                              </>
                            )}
                          </>
                        ) : (
                          <p className="dataP" style={{ paddingTop: "1%" }}>
                            <em style={{ color: "red" }}></em>
                          </p>
                        )}
                      </Col>
                      <Col md={3}>
                        <div className="data-caste" align="center"></div>
                      </Col>
                    </Row>

                    <Row className="healthIdForm">
                      <Col md={4}>
                        <div className="id-gen-steps">
                          <div className="steps">
                            <h3>Step-1</h3>
                          </div>
                          <div className="step-text">
                            <p>
                              Enter ABHA Number(Health ID) of the resident{" "}
                              <em>{healthiddata.name}</em> for whom you are
                              issuing the health ID
                            </p>
                          </div>
                        </div>
                        <div className="id-gen-steps2">
                          <div className="steps">
                            <h3>Step-2</h3>
                          </div>
                          <div className="step-text">
                            <p>
                              Capture photo of the resident with ABHA
                              Number(Health ID) card in their hand.
                            </p>
                          </div>
                        </div>
                        <div className="id-gen-steps3">
                          <div className="steps">
                            <h3>Step-3</h3>
                          </div>
                          <div className="step-text">
                            <p>
                              After completing step1 and step2 click on submit
                              button
                            </p>
                          </div>
                        </div>
                      </Col>
                      <Col md={8}>
                        <div className="Id-Form">
                          <div className="text-box">
                            <Form.Group className="">
                              <Row>
                                <Form.Label className="require">
                                  Health ID{" "}
                                  <span
                                    style={{ color: "red" }}
                                    className="error-text"
                                  >
                                    *{errorvalue}
                                  </span>
                                  <span></span>
                                </Form.Label>
                                <Col md={10}>
                                  {" "}
                                  <Form.Control
                                    className="formControl "
                                    id="health-id-form"
                                    type="text"
                                    onChange={(e) =>
                                      settingerrorvalue(
                                        setHealthid(e.target.value)
                                      )
                                    }
                                    maxLength={14}
                                    onKeyPress={(event) => {
                                      if (!/[0-9]/.test(event.key)) {
                                        event.preventDefault();
                                      }
                                    }}
                                  />
                                </Col>
                              </Row>
                            </Form.Group>
                          </div>

                          <div>
                            <div id="cameraphoto">
                              {" "}
                              {!imgSrc ? (
                                <div className="healthImg">
                                  <Webcam
                                    style={{
                                      height: "148px",
                                      width: "400px",
                                    }}
                                    audio={false}
                                    ref={webcamRef}
                                    screenshotFormat="image/jpeg"
                                  />
                                </div>
                              ) : (
                                <>
                                  <img
                                    src={imgSrc}
                                    style={{
                                      height: "180px",
                                      width: "400px",
                                    }}
                                  />
                                </>
                              )}
                            </div>

                            <Row className="id-btn">
                              <Col md={2}></Col>
                              <Col md={12}>
                                {healthid.length == "14" ? (
                                  <>
                                    {imgSrc ? (
                                      <Button
                                        className="HealthCancel isselected"
                                        id=""
                                        onClick={retake}
                                        variant="outline-secondary"
                                      >
                                        Retake
                                      </Button>
                                    ) : (
                                      <Button
                                        className="HealthCancel isselected"
                                        id=""
                                        onClick={capture}
                                        variant="outline-secondary"
                                      >
                                        Capture
                                      </Button>
                                    )}
                                  </>
                                ) : (
                                  ""
                                )}
                              </Col>
                            </Row>
                          </div>

                          <Row className="healthRow">
                            <div id="healthidenterdiv" align="center">
                              <div className="HealthButtons">
                                <Button
                                  className="HealthCancel"
                                  variant="outline-secondary"
                                  onClick={handleClose}
                                >
                                  Cancel
                                </Button>
                                <Button
                                  className="HealthSubmit"
                                  variant="secondary"
                                  onClick={submitdata}
                                >
                                  Submit
                                </Button>
                              </div>
                            </div>
                          </Row>
                        </div>
                      </Col>
                    </Row>
                  </Form>
                </Col>
              </Row>
            </div>
          </Col>
          <Col lg={2}></Col>
        </Row>
      </div>
    </div>
  );
}

export default HealthIdWindow;
