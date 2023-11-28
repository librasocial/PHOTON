import React, { useEffect, useState } from "react";
import { Breadcrumb, Col, Nav, Row, Tab } from "react-bootstrap";
import CreateMasters from "./CreatePHCMaster/CreateMasters";
import CreateSubCenter from "./CreateHWC/CreateSubCenter";
import CogsPHC from "./CogsPHC";
import ManagePHC from "./ManagePHC/ManagePHC";
import PageLoader from "../PageLoader";
import { useDispatch, useSelector } from "react-redux";
import { loadGetPhcDetails } from "../../redux/phcAction";
import moment from "moment";
import RegisterForAbdm from "./RegisterABDM/RegisterForAbdm";

export default function ConfigPHC(props) {
  let dispatch = useDispatch();
  const phcUuid = sessionStorage.getItem("uuidofphc");
  const { phcDetails } = useSelector((state) => state.phcData);

  const [patTabIndex, setPatTabIndex] = useState("active1");
  function activeTabSelect(index) {
    setPatTabIndex("active" + index);
  }

  const [pageLoader, setPageLoader] = useState(true);
  useEffect(() => {
    dispatch(loadGetPhcDetails(phcUuid, setPageLoader));
    document.title = "EMR Super Admin Configuration";
  }, [phcUuid, setPageLoader]);

  const removevitalstorege = () => {
    window.history.back();
  };

  const [timeOfDay, setTimeOfDay] = useState("");

  let typeofuser = sessionStorage.getItem("typeofuser");

  function displayTime() {
    var time2 = moment().format("ddd, DD MMM YYYY");
    var time3 = moment().format("hh:mm A");
    $("#clock2").html(time2);
    $("#clock3").html(time3);
    setTimeout(displayTime, 1000);
  }
  $(document).ready(function () {
    displayTime();
  });

  useEffect(() => {
    const date = new Date();
    let hours = date.getHours();
    const timeOfDay1 = `Good ${
      (hours < 12 && "Morning") ||
      (hours < 16 && "Afternoon") ||
      (hours < 20 && "Evening") ||
      "Night"
    }`;
    setTimeOfDay(timeOfDay1);
  }, [setTimeOfDay]);

  return (
    <React.Fragment>
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <div className="div vital-div">
        <div className="regHeader">
          <h1 className="Encounter-Header">PHC Configuration</h1>
          <hr style={{ margin: "0px" }} />
        </div>
        <div className="config-div">
          <div className="super-tab">
            <Row id="card-wish">
              <Col xs={3} lg={3}>
                <div id="wish1">
                  <div className="card-clr dtedesn">
                    <h3 className="date-time countdown" id="clock2"></h3>
                    <h3 className="countdown" id="clock3"></h3>
                  </div>
                  <img
                    src="../img/dashboard-greeting-img.png"
                    style={{ marginBottom: "-2%" }}
                  />
                </div>
              </Col>
              <Col md={9} sm={8} xs={8} id="gm">
                {/* <div className="my_text">
                                    <h1 className="morn"> {timeOfDay} </h1>
                                    <h2 className="user-type">{typeofuser}</h2>
                                    <h2 className="names"> {props.membername} </h2>
                                </div> */}
              </Col>
            </Row>
            <div className="super-breadcrumb">
              <Breadcrumb>
                <Breadcrumb.Item
                  className="pur-order-breadcrumb"
                  onClick={removevitalstorege}
                >
                  Dashboard
                </Breadcrumb.Item>
                <Breadcrumb.Item active>PHC Configuration</Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="super-complaint">
              <Tab.Container
                id="left-tabs-example"
                defaultActiveKey={phcDetails ? "first" : "config"}
              >
                <Row>
                  <Col md={2}>
                    <Nav variant="pills" className="flex-column">
                      <Nav.Item onClick={(e) => activeTabSelect(1)}>
                        <Nav.Link eventKey="first">Manage PHC Profile</Nav.Link>
                      </Nav.Item>
                      <Nav.Item onClick={(e) => activeTabSelect(2)}>
                        <Nav.Link eventKey="second">
                          Create PHC Masters
                        </Nav.Link>
                      </Nav.Item>
                      <Nav.Item onClick={(e) => activeTabSelect(3)}>
                        <Nav.Link eventKey="third">
                          Create Sub-Center / HWC
                        </Nav.Link>
                      </Nav.Item>
                      <Nav.Item onClick={(e) => activeTabSelect(4)}>
                        <Nav.Link eventKey="forth">Register for ABDM</Nav.Link>
                      </Nav.Item>
                    </Nav>
                  </Col>
                  <Col md={10}>
                    <Tab.Content>
                      {patTabIndex == "active0" && (
                        <Tab.Pane eventKey="config">
                          <CogsPHC />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active1" && (
                        <Tab.Pane eventKey="first">
                          <ManagePHC />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active2" && (
                        <Tab.Pane eventKey="second">
                          <CreateMasters />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active3" && (
                        <Tab.Pane eventKey="third">
                          <CreateSubCenter />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active4" && (
                        <Tab.Pane eventKey="forth">
                          <RegisterForAbdm />
                        </Tab.Pane>
                      )}
                    </Tab.Content>
                  </Col>
                </Row>
              </Tab.Container>
            </div>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
