import React, { useState } from "react";
import { Container, Col, Row, Button } from "react-bootstrap";
import { useHistory } from "react-router-dom";
import "./css/Login.css";
import { CognitoUser, AuthenticationDetails } from "amazon-cognito-identity-js";
import UserPool from "./UserPool";
import { decode } from "./Decodejwt";
import * as constant from "./ConstUrl/constant";
import PageLoader from "./PageLoader";

var srcType;
export default function Logincontainer() {
  const [passwordType, setpasswordType] = useState("password");
  const [passwordText, setpasswordText] = useState("Show");

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorvalue, setErrorvalue] = useState("");
  const [pageLoader, setPageLoader] = useState(false);

  let history = useHistory();
  const focusemail = () => {
    setErrorvalue("");
  };

  const onSubmit = (event) => {
    event.preventDefault();

    setPageLoader(true);

    const user = new CognitoUser({
      Username: email,
      Pool: UserPool,
    });

    const authDetails = new AuthenticationDetails({
      Username: email,
      Password: password,
    });

    user.authenticateUser(authDetails, {
      onSuccess: (data) => {
        const cognitoUser = UserPool.getCurrentUser();
        cognitoUser.getSession(function (err, data) {
          if (err) {
            // Prompt the user to reauthenticate by hand...
          } else {
            const cognitoUserSession = data;
            const yourIdToken = cognitoUserSession.getIdToken().jwtToken;

            const yourAccessToken =
              cognitoUserSession.getAccessToken().jwtToken;
          }
        });

        let authtoken = data["accessToken"]["jwtToken"];
        let tokendata = data["idToken"]["jwtToken"];
        const jwtPayload = tokendata.split(".")[1];
        const data1 = JSON.parse(decode(jwtPayload));

        const staffuuid = data1["custom:membershipId"];

        sessionStorage.setItem("token", authtoken);
        sessionStorage.setItem("uuid", staffuuid);
        sessionStorage.setItem("x-user-id", tokendata);

        var myHeaders = new Headers();
        let jwttoken = "Bearer " + "" + authtoken;
        myHeaders.append("Authorization", jwttoken);
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("IdToken", tokendata);
        var requestOptions = {
          headers: myHeaders,
          method: "GET",
          mode: "cors",
        };

        fetch(
          `${constant.ApiUrl}/member-svc/members/` + staffuuid,
          requestOptions
        )
          .then((res) => res.json())
          .then((res) => {
            if (
              res.status === 500 ||
              res.status === 503 ||
              res.status === 502
            ) {
              setPageLoader(false);
              alert("Incorrect Username or Password!! Please Verify");
              sessionStorage.clear();
            } else if (res.message == "Service Unavailable") {
              setPageLoader(false);
              alert("Something went wrong! Please Try again later");
              sessionStorage.clear();
            } else if (res.content.length === 1) {
              let loginuuid = res.content[0].properties["uuid"];
              sessionStorage.setItem(
                "userid",
                res.content[0].properties["uuid"]
              );
              let srcType = res.content[0].properties["type"];
              let staffUrl =
                `${constant.ApiUrl}/member-svc/members/relationships/filter?srcType=` +
                srcType +
                "&rel=MEMBEROF&targetType=Phc&sourceTypeId=" +
                loginuuid;
              if (res.content[0].properties["type"] === "HeadMedicalOfficer") {
                srcType = "MedicalOfficer";
                staffUrl =
                  `${constant.ApiUrl}/member-svc/members/relationships/filter?srcType=` +
                  srcType +
                  "&rel=MEMBEROF&targetType=Phc&sourceTypeId=" +
                  loginuuid;
              } else if (res.content[0].properties["type"] === "AshaWorker") {
                srcType = "AshaWorker";
                staffUrl = `${constant.ApiUrl}/member-svc/members/relationships/filter?srcType=AshaWorker&srcNodeId=${res.content[0]["properties"]["uuid"]}&rel=MEMBEROF&targetType=SubCenter`;
              }
              fetch(staffUrl, requestOptions)
                .then((res_sub) => res_sub.json())
                .then((res_sub) => {
                  if (
                    res.content[0].properties["type"] === "PhcAdministrator"
                  ) {
                    sessionStorage.setItem("typeofuser", "Admin");
                  } else if (res.content[0].properties["type"] === "Admin") {
                    sessionStorage.setItem("typeofuser", "Super Admin");
                  } else if (
                    res.content[0].properties["type"] === "MedicalOfficer"
                  ) {
                    sessionStorage.setItem("typeofuser", "Medical Officer");
                  } else if (
                    res.content[0].properties["type"] ===
                    "JuniorHealthAssistant"
                  ) {
                    sessionStorage.setItem("typeofuser", "Nurse");
                  } else if (
                    res.content[0].properties["type"] === "JuniorLabTechnician"
                  ) {
                    sessionStorage.setItem("typeofuser", "Lab Technician");
                  } else if (
                    res.content[0].properties["type"] === "JuniorPharmacist"
                  ) {
                    sessionStorage.setItem("typeofuser", "Pharmacist");
                  }

                  sessionStorage.setItem(
                    "membername",
                    res.content[0]["properties"]["name"]
                  );
                  let subcenter_id =
                    res_sub.content[0]["targetNode"]["properties"]["uuid"];

                  sessionStorage.setItem(
                    "uuidofphc",
                    res_sub.content[0]["targetNode"]["properties"]["uuid"]
                  );
                  sessionStorage.setItem(
                    "phc",
                    res_sub.content[0]["targetNode"]["properties"]["name"]
                  );

                  sessionStorage.setItem(
                    "login",
                    JSON.stringify({ login: true, token: "authtoken" })
                  );
                  // Asa worker
                  if (res.content[0].properties["type"] === "AshaWorker") {
                    fetch(
                      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=SubCenter&srcNodeId=${subcenter_id}&rel=SUBORGOF&targetType=Phc`,
                      requestOptions
                    )
                      .then((res_phc) => res_phc.json())
                      .then((res_phc) => {
                        let phc_uuid =
                          res_phc.content[0]["target"]["properties"]["uuid"];

                        sessionStorage.setItem("IdToken", tokendata);
                        sessionStorage.setItem("uuid_of_phc", phc_uuid);
                        sessionStorage.setItem("token", authtoken);

                        if (window.location.href.indexOf("localhost") !== -1) {
                          window.location.href = "/";
                          setPageLoader(false);
                        } else {
                          window.location.href = constant.AppUrl;
                          setPageLoader(false);
                        }
                      });
                    sessionStorage.setItem("typeofuser", "Asha Worker");
                  } else {
                    if (window.location.href.indexOf("localhost") !== -1) {
                      window.location.href = "/";
                      setPageLoader(false);
                    } else {
                      window.location.href = constant.AppUrl;
                      setPageLoader(false);
                    }
                  }
                });
            } else {
              setPageLoader(false);
              setErrorvalue("*Incorrect Username or Password!! Please Verify");
              sessionStorage.clear();
            }
          });
      },
      onFailure: (err) => {
        setPageLoader(false);
        setErrorvalue("*Incorrect Username or Password!! Please Verify");
        sessionStorage.clear();
      },
      newPasswordRequired: (data) => {},
    });
  };

  const handleToggle = () => {
    if (passwordType === "password") {
      setpasswordType("text");
      setpasswordText("Hide");
    } else {
      setpasswordType("password");
      setpasswordText("Show");
    }
  };

  return (
    <Container>
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <Row className="d-flex justify-content-center align-items-center">
        <Col>
          <h4 className="LoginHeader">Registered Users Login</h4>
          <form onSubmit={onSubmit} className="loginForm">
            <div className="mb-3 FormField" align="left">
              <label htmlFor="exampleInputEmail1" className="form-label fLabel">
                Username
              </label>
              <input
                type="text"
                value={email}
                onFocus={focusemail}
                onChange={(event) => setEmail(event.target.value)}
                className="form-control inputLogin"
                id="exampleInputEmail1"
                aria-describedby="emailHelp"
                placeholder="Enter Your Username"
                required
              />
            </div>
            <div className="mb-3 FormField" align="left">
              <label
                htmlFor="exampleInputPassword1"
                className="form-label fLabel"
              >
                Password
              </label>
              <div className="input-cont">
                <input
                  type={passwordType}
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                  className="form-control inputLogin"
                  id="exampleInputPassword1"
                  placeholder="Enter Password"
                  required
                />
                <a
                  className="button"
                  onClick={handleToggle}
                  style={{ cursor: "pointer", color: "#336699" }}
                >
                  {passwordText}
                </a>
              </div>
            </div>
            <p style={{ color: "red", fontSize: "13px" }}>{errorvalue}</p>
            <div className="btn d-flex justify-content-center align-items-center">
              <Button type="submit" className="btn btn-secondary loginBtn">
                Login
              </Button>
            </div>
          </form>
        </Col>
      </Row>
    </Container>
  );
}
