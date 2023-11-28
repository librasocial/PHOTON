import Footer from "footer/Footer";
import Mainheader from "mainheader/Mainheader";
import React, { useEffect } from "react";
import { Container } from "react-bootstrap";
import ReactDOM from "react-dom";
import { HashRouter as Router, Route, Switch } from "react-router-dom";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "../node_modules/bootstrap/dist/css/bootstrap.css";
import "./App.css";
import moment from "moment";
import Dashboard from "./components/Dashboard/Dashboard";
import CreateEncounter from "./components/EMR/EncounterPages/CreateEncounter";
import EncounterSearch from "./components/EMR/EncounterPages/EncounterSearch";
import FaceCamera from "./components/EMR/FaceCamera/FaceCamera";
import Forms from "./components/EMR/MultiForms/Form";
import SearchPatient from "./components/EMR/PatientRegistration/SearchPatient";
import Queue from "./components/Dashboard/QueueManagement/Queue";
import RecordVitals from "./components/EMR/Vitals";
import GeneratedIds from "./components/HealthId/GeneratedIds/GeneratedIds";
import HomePage from "./components/HomePage";
import CollectSample from "./components/LabModule/LabScreens/CollectSample";
import EnterResult from "./components/LabModule/LabScreens/EnterResult";
import OrderLab from "./components/LabModule/LabScreens/OrderLab";
import ViewReport from "./components/LabModule/LabScreens/ViewReport";
import Orders from "./components/Pharmacy/Order/Orders";
import ProductMaster from "./components/Pharmacy/ProductMaster/ProductMaster";
import Login from "./components/Login";
import Inwards from "./components/Pharmacy/Inwards/Inwards";
import GoogleMap from "./components/GoogleMap/GisMap";
import ViewReports from "./components/GoogleMap/ViewReports";
import "./css/Login.css";
import "./css/mainheader.css";
import "./css/Sidemenu.css";
import Home from "./Home";
import { Provider } from "react-redux";
import store from "./redux/store";
import Medque from "./components/Pharmacy/Dashboard/Medque";
import Approvals from "./components/Notifications/Approvals";
import Notifications from "./components/Notifications/Notification";
import PurchaseOrder from "./components/Pharmacy/Order/PurchaseOrder";
import ConfigPHC from "./components/PHC/ConfigPHC";
import CreateFacility from "./components/PHC/CreatePHCMaster/CreateFacility/CreateFacility";
import AddFacility from "./components/PHC/CreateHWC/AddFacilitySC/AddFacility";
import AssignActivity from "./components/PHC/CreateHWC/AddResposibleSC/AssignActivity";
import CreateSubFacility from "./components/PHC/CreateHWC/AddFacilitySC/CreateSubFacility";
import AssignSubActivity from "./components/PHC/CreateHWC/AddResposibleSC/AssignSubActivity";
import AddResponsibility from "./components/PHC/CreateHWC/AddResposibleSC/AddResponsibility";
import SessionTimeout from "./sessionExpiredModal/SessionTimeout";
import Stocks from "./components/Pharmacy/Stocks/Stocks";
import AddSubCenter from "./components/PHC/CreateHWC/AddAndManadeSC/AddSubCenter";
import CreateStaff from "./components/PHC/CreatePHCMaster/CreateStaff/CreateStaff";
import AssignSubResponsibility from "./components/PHC/CreateHWC/AddResposibleSC/AssignSubResponsibility";
import AssignOwnerAct from "./components/PHC/CreatePHCMaster/ProcessOwnerActivity/AssignOwnerAct";
import AssignresposibilityPhc from "./components/PHC/CreatePHCMaster/AddResponsible/AssignresposibilityPhc";
import PageNotFound from "./PageNotFound";
import PowerbiDashboard from "./components/Dashboard/PowerbiDashboard";
import AbdmM2steps from "./components/EMR/PatientRegistration/ABDMService/AbdmM2steps";

const App = () => {
  let typeofuser = sessionStorage.getItem("typeofuser");

  // create an event listener
  // const [isMobile, setIsMobile] = useState(false)

  // //choose the screen size
  // const handleResize = () => {
  //     if (window.innerWidth < 720) {
  //         setIsMobile(true)
  //     } else {
  //         setIsMobile(false)
  //     }
  // }
  // useEffect(() => {
  //     window.addEventListener("resize", handleResize)
  // })

  useEffect(() => {
    const interval = setInterval(() => {
      var time3 = moment().format("hh:mm:ss A");
      if (time3 == "11:59:59 PM" && sessionStorage.getItem("login")) {
        sessionStorage.clear();
        window.location.href = "/";
      }
    }, 1000);
    return () => clearInterval(interval);
  }, [sessionStorage.getItem("login")]);

  /* Display date and time */

  return (
    <div className="App">
      <React.Fragment>
        <Provider store={store}>
          <Router>
            <Switch>
              {sessionStorage.getItem("login") ? (
                <React.Fragment>
                  <SessionTimeout />
                  <Mainheader
                    membername={sessionStorage.getItem("membername")}
                    typeofuser={sessionStorage.getItem("typeofuser")}
                    phc={sessionStorage.getItem("phc")}
                  />
                  {typeofuser === "Admin" ? (
                    <React.Fragment>
                      <Router>
                        <React.Fragment>
                          <Container fluid style={{ padding: "0px" }}>
                            <Switch>
                              <Route exact path="/mainDash">
                                <PowerbiDashboard />
                              </Route>
                              <Route exact path="/">
                                <Dashboard
                                  typeofuser={sessionStorage.getItem(
                                    "typeofuser"
                                  )}
                                  membername={sessionStorage.getItem(
                                    "membername"
                                  )}
                                />
                              </Route>
                              <Route exact path="/login">
                                <Dashboard
                                  typeofuser={sessionStorage.getItem(
                                    "typeofuser"
                                  )}
                                  membername={sessionStorage.getItem(
                                    "membername"
                                  )}
                                />
                              </Route>
                              <Route exact path="/rootpage">
                                <Dashboard
                                  typeofuser={sessionStorage.getItem(
                                    "typeofuser"
                                  )}
                                  membername={sessionStorage.getItem(
                                    "membername"
                                  )}
                                />
                              </Route>
                              <Route exact path="/dashboard">
                                <Dashboard
                                  typeofuser={sessionStorage.getItem(
                                    "typeofuser"
                                  )}
                                  membername={sessionStorage.getItem(
                                    "membername"
                                  )}
                                />
                              </Route>
                              <Route exact path="/dashboard/:category">
                                <Dashboard
                                  typeofuser={sessionStorage.getItem(
                                    "typeofuser"
                                  )}
                                  membername={sessionStorage.getItem(
                                    "membername"
                                  )}
                                />
                              </Route>
                              <Route exact path="/home">
                                <Dashboard />
                              </Route>

                              <Route exact path="/searchPatient">
                                <SearchPatient
                                  membername={sessionStorage.getItem(
                                    "membername"
                                  )}
                                />
                              </Route>
                              <Route exact path="/searchPatient/:id">
                                <SearchPatient
                                  membername={sessionStorage.getItem(
                                    "membername"
                                  )}
                                />
                              </Route>
                              <Route exact path="/regForm">
                                <Forms />
                              </Route>
                              <Route exact path="/abdm">
                                <AbdmM2steps />
                              </Route>
                              <Route exact path="/createEncounter">
                                <CreateEncounter
                                  membername={sessionStorage.getItem(
                                    "membername"
                                  )}
                                />
                              </Route>
                              <Route exact path="/createEncounter/:id/:types">
                                <CreateEncounter
                                  membername={sessionStorage.getItem(
                                    "membername"
                                  )}
                                />
                              </Route>
                              <Route exact path="/queue">
                                <Queue />
                              </Route>
                              <Route exact path="/Vitals">
                                <RecordVitals />
                              </Route>
                              <Route exact path="/EncounterSearch">
                                <EncounterSearch />
                              </Route>
                              <Route exact path="/gis-map">
                                <GoogleMap />
                              </Route>
                              <Route exact path="/view-reports">
                                <ViewReports />
                              </Route>
                              <Route exact path="*">
                                <PageNotFound />
                              </Route>
                            </Switch>
                          </Container>
                        </React.Fragment>
                      </Router>
                    </React.Fragment>
                  ) : (
                    <React.Fragment>
                      {typeofuser === "Lab Technician" ? (
                        <React.Fragment>
                          <Router>
                            <React.Fragment>
                              <Container fluid style={{ padding: "0px" }}>
                                <Switch>
                                  <Route exact path="/mainDash">
                                    <PowerbiDashboard />
                                  </Route>
                                  <Route exact path="/">
                                    <Dashboard
                                      typeofuser={sessionStorage.getItem(
                                        "typeofuser"
                                      )}
                                      membername={sessionStorage.getItem(
                                        "membername"
                                      )}
                                    />
                                  </Route>
                                  <Route exact path="/login">
                                    <Dashboard
                                      typeofuser={sessionStorage.getItem(
                                        "typeofuser"
                                      )}
                                      membername={sessionStorage.getItem(
                                        "membername"
                                      )}
                                    />
                                  </Route>
                                  <Route exact path="/rootpage">
                                    <Dashboard
                                      typeofuser={sessionStorage.getItem(
                                        "typeofuser"
                                      )}
                                      membername={sessionStorage.getItem(
                                        "membername"
                                      )}
                                    />
                                  </Route>
                                  <Route exact path="/dashboard">
                                    <Dashboard
                                      typeofuser={sessionStorage.getItem(
                                        "typeofuser"
                                      )}
                                      membername={sessionStorage.getItem(
                                        "membername"
                                      )}
                                    />
                                  </Route>
                                  <Route exact path="/LabTest">
                                    <HomePage />
                                  </Route>
                                  <Route exact path="/collect-sample">
                                    <CollectSample />
                                  </Route>
                                  <Route exact path="/enter-result">
                                    <EnterResult />
                                  </Route>
                                  <Route exact path="/order-lab">
                                    <OrderLab />
                                  </Route>
                                  <Route exact path="/view-report">
                                    <ViewReport />
                                  </Route>
                                  <Route exact path="/gis-map">
                                    <GoogleMap />
                                  </Route>
                                  <Route exact path="/view-reports">
                                    <ViewReports />
                                  </Route>
                                  <Route exact path="*">
                                    <PageNotFound />
                                  </Route>
                                </Switch>
                              </Container>
                            </React.Fragment>
                          </Router>
                        </React.Fragment>
                      ) : (
                        <React.Fragment>
                          {typeofuser === "Pharmacist" ? (
                            <React.Fragment>
                              <Router>
                                <React.Fragment>
                                  <Container fluid style={{ padding: "0px" }}>
                                    <Switch>
                                      <Route exact path="/mainDash">
                                        <PowerbiDashboard />
                                      </Route>
                                      <Route exact path="/">
                                        <Dashboard
                                          typeofuser={sessionStorage.getItem(
                                            "typeofuser"
                                          )}
                                          membername={sessionStorage.getItem(
                                            "membername"
                                          )}
                                        />
                                      </Route>
                                      <Route exact path="/login">
                                        <Dashboard
                                          typeofuser={sessionStorage.getItem(
                                            "typeofuser"
                                          )}
                                          membername={sessionStorage.getItem(
                                            "membername"
                                          )}
                                        />
                                      </Route>
                                      <Route exact path="/rootpage">
                                        <Dashboard
                                          typeofuser={sessionStorage.getItem(
                                            "typeofuser"
                                          )}
                                          membername={sessionStorage.getItem(
                                            "membername"
                                          )}
                                        />
                                      </Route>
                                      <Route exact path="/dashboard">
                                        <Dashboard
                                          typeofuser={sessionStorage.getItem(
                                            "typeofuser"
                                          )}
                                          membername={sessionStorage.getItem(
                                            "membername"
                                          )}
                                        />
                                      </Route>
                                      <Route exact path="/Dispence-Medicine">
                                        <Medque />
                                      </Route>
                                      <Route exact path="/Product-Master">
                                        <ProductMaster />
                                      </Route>
                                      <Route exact path="/Orders">
                                        <Orders />
                                      </Route>
                                      <Route exact path="/Inwards">
                                        <Inwards />
                                      </Route>
                                      <Route exact path="/Stocks">
                                        <Stocks />
                                      </Route>
                                      <Route exact path="/gis-map">
                                        <GoogleMap />
                                      </Route>
                                      <Route exact path="/view-reports">
                                        <ViewReports />
                                      </Route>
                                      <Route exact path="*">
                                        <PageNotFound />
                                      </Route>
                                    </Switch>
                                  </Container>
                                </React.Fragment>
                              </Router>
                            </React.Fragment>
                          ) : (
                            <React.Fragment>
                              {typeofuser === "Super Admin" ? (
                                <React.Fragment>
                                  <Router>
                                    <React.Fragment>
                                      <Container
                                        fluid
                                        style={{ padding: "0px" }}
                                      >
                                        <Switch>
                                          <Route exact path="/mainDash">
                                            <PowerbiDashboard />
                                          </Route>
                                          <Route exact path="/">
                                            <Dashboard
                                              typeofuser={sessionStorage.getItem(
                                                "typeofuser"
                                              )}
                                              membername={sessionStorage.getItem(
                                                "membername"
                                              )}
                                            />
                                          </Route>
                                          <Route exact path="/super-admin">
                                            <Dashboard />
                                          </Route>
                                          <Route exact path="/config-phc">
                                            <ConfigPHC
                                              membername={sessionStorage.getItem(
                                                "membername"
                                              )}
                                            />
                                          </Route>
                                          <Route exact path="/CreateFacility">
                                            <CreateFacility />
                                          </Route>
                                          <Route exact path="/CreateStaff">
                                            <CreateStaff />
                                          </Route>
                                          <Route exact path="/AssignProcess">
                                            <AssignOwnerAct />
                                          </Route>
                                          <Route
                                            exact
                                            path="/AssignResponsibility"
                                          >
                                            <AssignresposibilityPhc />
                                          </Route>
                                          <Route exact path="/AddFacility">
                                            <AddFacility />
                                          </Route>
                                          <Route exact path="/AddSubCenter">
                                            <AddSubCenter />
                                          </Route>
                                          <Route exact path="/AssignActivity">
                                            <AssignActivity />
                                          </Route>
                                          <Route
                                            exact
                                            path="/CreateSubFacility"
                                          >
                                            <CreateSubFacility />
                                          </Route>
                                          <Route
                                            exact
                                            path="/AssignSubActivity"
                                          >
                                            <AssignSubActivity />
                                          </Route>
                                          <Route
                                            exact
                                            path="/AddResponsibility"
                                          >
                                            <AddResponsibility />
                                          </Route>
                                          <Route
                                            exact
                                            path="/AssignSubResponsibility"
                                          >
                                            <AssignSubResponsibility />
                                          </Route>
                                          <Route exact path="/gis-map">
                                            <GoogleMap />
                                          </Route>
                                          <Route exact path="/view-reports">
                                            <ViewReports />
                                          </Route>
                                          <Route exact path="*">
                                            <PageNotFound />
                                          </Route>
                                        </Switch>
                                      </Container>
                                    </React.Fragment>
                                  </Router>
                                </React.Fragment>
                              ) : (
                                <React.Fragment>
                                  <Router>
                                    <React.Fragment>
                                      <Container
                                        fluid
                                        style={{ padding: "0px" }}
                                      >
                                        <Switch>
                                          <Route exact path="/mainDash">
                                            <PowerbiDashboard />
                                          </Route>
                                          <Route exact path="/">
                                            <Dashboard
                                              typeofuser={sessionStorage.getItem(
                                                "typeofuser"
                                              )}
                                              membername={sessionStorage.getItem(
                                                "membername"
                                              )}
                                            />
                                          </Route>
                                          <Route exact path="/login">
                                            <Dashboard
                                              typeofuser={sessionStorage.getItem(
                                                "typeofuser"
                                              )}
                                              membername={sessionStorage.getItem(
                                                "membername"
                                              )}
                                            />
                                          </Route>
                                          <Route exact path="/rootpage">
                                            <Dashboard
                                              typeofuser={sessionStorage.getItem(
                                                "typeofuser"
                                              )}
                                              membername={sessionStorage.getItem(
                                                "membername"
                                              )}
                                            />
                                          </Route>
                                          <Route exact path="/dashboard">
                                            <Dashboard
                                              typeofuser={sessionStorage.getItem(
                                                "typeofuser"
                                              )}
                                              membername={sessionStorage.getItem(
                                                "membername"
                                              )}
                                            />
                                          </Route>
                                          <Route exact path="/notifications">
                                            <Notifications />
                                          </Route>
                                          <Route exact path="/approvals">
                                            <Approvals />
                                          </Route>
                                          <Route exact path="/poApprovals">
                                            <Approvals />
                                          </Route>
                                          <Route exact path="/view-report">
                                            <ViewReport />
                                          </Route>
                                          <Route exact path="/Orders">
                                            <PurchaseOrder />
                                          </Route>
                                          <Route exact path="/generatedIds">
                                            <GeneratedIds />
                                          </Route>
                                          <Route exact path="/Vitals">
                                            <RecordVitals />
                                          </Route>
                                          <Route exact path="/Report">
                                            <RecordVitals />
                                          </Route>
                                          <Route exact path="/gis-map">
                                            <GoogleMap />
                                          </Route>
                                          <Route exact path="/view-reports">
                                            <ViewReports />
                                          </Route>
                                          <Route exact path="*">
                                            <PageNotFound />
                                          </Route>
                                        </Switch>
                                      </Container>
                                    </React.Fragment>
                                  </Router>
                                </React.Fragment>
                              )}
                            </React.Fragment>
                          )}
                        </React.Fragment>
                      )}
                    </React.Fragment>
                  )}
                  <Footer />
                </React.Fragment>
              ) : (
                <Router>
                  <Mainheader />

                  {/* <Route exact path="/login">
                    <Login />{" "}
                  </Route> */}
                  {/* <Route exact path="/searchPatient">
                      <SearchPatient
                      />
                  </Route> */}
                  <Route exact path="/dashboard">
                    <Login />
                  </Route>
                  <Route exact path="/generateHealthId">
                    <Login />
                  </Route>
                  <Route exact path="/rootpage">
                    <Login />
                  </Route>
                  {/* <Route exact path="*">
                    <Login />
                  </Route> */}
                  <Route exact path="/">
                    <Home />
                  </Route>
                  <Route exact path="/home">
                    <Home />
                  </Route>
                  <Route exact path="/login">
                    <Login />{" "}
                  </Route>
                  <Route exact path="/faceCamera">
                    <FaceCamera />
                  </Route>
                  <Footer />
                </Router>
              )}
            </Switch>
          </Router>
        </Provider>
      </React.Fragment>
    </div>
  );
  // }
};
// export default App;
ReactDOM.render(<App />, document.getElementById("app"));
