import React, { useEffect, useState } from "react";
import "../css/Sidemenu.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import "../../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle";
import { Link, NavLink } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { loadAsssignActivityList } from "../redux/phcAction";
import { NavDropdown, NavItem } from "react-bootstrap";
import DropdownToggle from "react-bootstrap/esm/DropdownToggle";
import DropdownMenu from "react-bootstrap/esm/DropdownMenu";
import DropdownItem from "react-bootstrap/esm/DropdownItem";

function Sidemenu() {
  let typeofuser = sessionStorage.getItem("typeofuser");

  let dispatch = useDispatch();
  let userUuid = sessionStorage.getItem("userid");
  const { activityList } = useSelector((state) => state.phcData);

  useEffect(() => {
    if (userUuid) {
      dispatch(loadAsssignActivityList(userUuid));
    }
  }, [userUuid]);

  const [roleActivity, setRoleActivity] = useState([]);

  useEffect(() => {
    if (activityList) {
      const sorted = activityList.slice().sort(function (a, b) {
        if (a?.targetNode?.id > b?.targetNode?.id) return 1;
        if (a?.targetNode?.id < b?.targetNode?.id) return -1;
        return 0;
      });
      let activities = [];
      sorted.map((item) => {
        if (item.targetNode.properties.name) {
          activities.push(item.targetNode.properties.name);
        }
      });
      setRoleActivity(activities);
    }
  }, [activityList]);

  return (
    <div className="side-bar-static-div">
      <div className="side-bar-top-div"></div>
      <div
        style={{ display: "flex" }}
        className="left-menu-dsgn flex-column side-bar-menu"
      >
        {/* <NavLink className="pd-l apps-sidemenu" to="/dashboard"><i className="bi bi-columns-gap dash-icon"></i>Dashboard </NavLink>
        {roleActivity && roleActivity.map((route, i) => (
          <NavLink className="pd-l apps-sidemenu"
            to={route == "dashboard" ? "/dashboard" : route == "Queue Management" ? "/queue" :
              route == "Register New Patient" ? "/searchpatient" : route == "Create Visit" ? "/EncounterSearch" : ""}
            key={i}><i className="bi bi-columns-gap dash-icon"></i>{route} </NavLink>
        ))}
        {(typeofuser === "Admin") &&
          <NavLink className="pd-l apps-sidemenu" to="/gis-map"><i className="bi bi-geo-alt dash-icon"></i>Information Privilege</NavLink>
        } */}

        {/* <NavLink className="pd-l apps-sidemenu" to="/dashboard"><i className="bi bi-columns-gap dash-icon"></i>Dashboard </NavLink>
        {roleActivity && roleActivity.map((route, i) => (
        <React.Fragment> */}

        {/*   <NavLink className="pd-l apps-sidemenu" to="/generatedIds"><i className="bi bi-person-plus dash-icon"></i>Generate and Issue ABHA Number(Health ID)</NavLink> */}

        {/* {route == "Register New Patient" &&
            <NavLink className="pd-l apps-sidemenu" to="/searchpatient"><i className="bi bi-person-plus dash-icon"></i>Register New Patient</NavLink>
          }
          {route == "Create Visit" &&
            <NavLink className="pd-l apps-sidemenu" to="/EncounterSearch"><i className="bi bi-person-check dash-icon"></i>Create Visit </NavLink>
          }
          {route == "Queue Management" &&
            <NavLink className="pd-l apps-sidemenu" to="/queue"><i className="bi bi-people dash-icon"></i>Queue Management </NavLink>
          }
          {route == "Manage Lab Master" &&
            <NavLink className="pd-l apps-sidemenu" to="/Labtest"><i className="bi bi-gear dash-icon"></i>Lab Master</NavLink>
          }
          {route == "Manage Product Master" &&   
            <NavLink className="pd-l apps-sidemenu" to="/Product-Master"><i className="bi bi-gear dash-icon"></i>Product Master </NavLink>
          }
          {route == "Orders" &&  
            <NavLink className="pd-l apps-sidemenu" to="/Orders"><i className="bi bi-bag-check dash-icon"></i>Orders </NavLink>
          }
          {route == "Inwards" &&
            <NavLink className="pd-l apps-sidemenu" to="/Inwards"><i className="bi bi-bag-plus dash-icon"></i>Inwards </NavLink>
          }
          {route == "Stocks" &&
            <NavLink className="pd-l apps-sidemenu" to="/Stocks"><i className="fa fa-file-text-o dash-icon"></i>Stocks </NavLink>
          }
          {route == "Notifications" &&
            <NavLink className="pd-l apps-sidemenu" to="/notifications"><i className="bi bi-bell dash-icon"></i>Notifications </NavLink>
          }
          {route == "Approvals" && 
            <NavLink className="pd-l apps-sidemenu" to="/approvals"><i className="bi bi-person-rolodex dash-icon"></i>Approvals </NavLink>
          }
          {route == "Approvals" &&      
            <NavLink className="pd-l apps-sidemenu" to="/super-admin"><i className="bi bi-person-circle dash-icon"></i>Super Admin</NavLink>
          }
        </React.Fragment>
        ))}
        <NavLink className="pd-l apps-sidemenu" to="/gis-map"><i className="bi bi-geo-alt dash-icon"></i>Information Privilege</NavLink> */}

        {typeofuser === "Asha Worker" ? (
          <React.Fragment>
            {/* <NavLink className="pd-l apps-sidemenu" to="/NewDashboard"><i className="bi bi-columns-gap dash-icon"></i>Dashboard </NavLink> */}
            <NavLink className="pd-l apps-sidemenu" to="/dashboard">
              <i className="bi bi-columns-gap dash-icon"></i>Dashboard{" "}
            </NavLink>
            <NavLink className="pd-l apps-sidemenu" to="/generatedIds">
              <i className="bi bi-person-plus dash-icon"></i>Generate and Issue
              ABHA Number(Health ID)
            </NavLink>
            {/* <NavLink className="pd-l apps-sidemenu" to="/gis-map"><i className="bi bi-geo-alt dash-icon"></i>Information Privilege</NavLink> */}
          </React.Fragment>
        ) : (
          <>
            {typeofuser === "Admin" ? (
              <React.Fragment>
                <NavLink className="pd-l apps-sidemenu" to="/mainDash">
                  <i className="bi bi-columns-gap dash-icon"></i>Dashboard{" "}
                </NavLink>
                <NavDropdown
                  id="collasible-nav-dropdown"
                  align="center"
                  title={
                    <div>
                      <i className="fa fa-file-text-o dash-icon"></i>
                      <span className="drophead">Station 1 (Registration)</span>
                      <i className="fa fa-caret-down" aria-hidden="true"></i>
                    </div>
                  }
                >
                  {roleActivity &&
                    roleActivity.map((activeTabs, i) => (
                      <div
                        className="showmore-div"
                        aria-expanded="true"
                        key={i}
                      >
                        {activeTabs == "List of visit" && (
                          <NavLink
                            className="pd-l apps-sidemenu"
                            to="/dashboard"
                          >
                            <i className="bi bi-columns-gap dash-icon"></i>
                            {activeTabs}
                          </NavLink>
                        )}
                        {activeTabs == "Register New Patient" && (
                          <NavLink
                            className="pd-l apps-sidemenu"
                            to="/searchpatient"
                          >
                            <i className="bi bi-person-plus dash-icon"></i>
                            {activeTabs}
                          </NavLink>
                        )}
                        {activeTabs == "Create Visit" && (
                          <NavLink
                            className="pd-l apps-sidemenu"
                            to="/EncounterSearch"
                          >
                            <i className="bi bi-person-check dash-icon"></i>
                            {activeTabs}
                          </NavLink>
                        )}
                        {activeTabs == "Queue Management" && (
                          <NavLink className="pd-l apps-sidemenu" to="/queue">
                            <i className="bi bi-people dash-icon"></i>
                            {activeTabs}
                          </NavLink>
                        )}
                      </div>
                    ))}
                </NavDropdown>
                <NavDropdown
                  id="collasible-nav-dropdown"
                  align="center"
                  title={
                    <div>
                      <i className="bi bi-geo-alt dash-icon"></i>
                      <span className="drophead">Information Privilege</span>
                      <i className="fa fa-caret-down" aria-hidden="true"></i>
                    </div>
                  }
                >
                  <div className="showmore-div" aria-expanded="true">
                    <NavLink className="pd-l apps-sidemenu" to="/gis-map">
                      <i className="bi bi-columns-gap dash-icon"></i>Gis Mapping
                    </NavLink>

                    <NavLink className="pd-l apps-sidemenu" to="/view-reports">
                      <i className="bi bi-person-plus dash-icon"></i>View Report
                    </NavLink>
                  </div>
                </NavDropdown>
              </React.Fragment>
            ) : (
              <>
                {typeofuser === "Lab Technician" ? (
                  <React.Fragment>
                    <NavLink className="pd-l apps-sidemenu" to="/mainDash">
                      <i className="bi bi-columns-gap dash-icon"></i>Dashboard{" "}
                    </NavLink>
                    <NavDropdown
                      id="collasible-nav-dropdown"
                      align="center"
                      title={
                        <div>
                          <i className="fa fa-file-text-o dash-icon"></i>
                          <span className="drophead">
                            Station 5 (Laboratory)
                          </span>
                          <i
                            className="fa fa-caret-down"
                            aria-hidden="true"
                          ></i>
                        </div>
                      }
                    >
                      {roleActivity &&
                        roleActivity.map((activeTabs, i) => (
                          <div
                            className="showmore-div"
                            aria-expanded="true"
                            key={i}
                          >
                            {activeTabs == "List of visit" && (
                              <NavLink
                                className="pd-l apps-sidemenu"
                                to="/dashboard"
                              >
                                <i className="bi bi-columns-gap dash-icon"></i>
                                {activeTabs}
                              </NavLink>
                            )}
                            {activeTabs == "Manage Lab Master" && (
                              <NavLink
                                className="pd-l apps-sidemenu"
                                to="/Labtest"
                              >
                                <i className="bi bi-gear dash-icon"></i>
                                {activeTabs}
                              </NavLink>
                            )}
                          </div>
                        ))}
                    </NavDropdown>
                    <NavDropdown
                      id="collasible-nav-dropdown"
                      align="center"
                      title={
                        <div>
                          <i className="bi bi-geo-alt dash-icon"></i>
                          <span className="drophead">
                            Information Privilege
                          </span>
                          <i
                            className="fa fa-caret-down"
                            aria-hidden="true"
                          ></i>
                        </div>
                      }
                    >
                      <div className="showmore-div" aria-expanded="true">
                        <NavLink className="pd-l apps-sidemenu" to="/gis-map">
                          <i className="bi bi-columns-gap dash-icon"></i>Gis
                          Mapping
                        </NavLink>

                        <NavLink
                          className="pd-l apps-sidemenu"
                          to="/view-reports"
                        >
                          <i className="bi bi-person-plus dash-icon"></i>View
                          Report
                        </NavLink>
                      </div>
                    </NavDropdown>
                  </React.Fragment>
                ) : (
                  <>
                    {typeofuser === "Pharmacist" ? (
                      <React.Fragment>
                        <NavLink className="pd-l apps-sidemenu" to="/mainDash">
                          <i className="bi bi-columns-gap dash-icon"></i>
                          Dashboard{" "}
                        </NavLink>
                        <NavDropdown
                          id="collasible-nav-dropdown"
                          align="center"
                          title={
                            <div>
                              <i className="fa fa-file-text-o dash-icon"></i>
                              <span className="drophead">
                                Station 4 (Pharmacy)
                              </span>
                              <i
                                className="fa fa-caret-down"
                                aria-hidden="true"
                              ></i>
                            </div>
                          }
                        >
                          {roleActivity &&
                            roleActivity.map((activeTabs, i) => (
                              <div
                                className="showmore-div"
                                aria-expanded="true"
                                key={i}
                              >
                                {activeTabs == "List of visit" && (
                                  <NavLink
                                    className="pd-l apps-sidemenu"
                                    to="/dashboard"
                                  >
                                    <i className="bi bi-columns-gap dash-icon"></i>
                                    {activeTabs}
                                  </NavLink>
                                )}
                                {activeTabs == "Manage Product Master" && (
                                  <NavLink
                                    className="pd-l apps-sidemenu"
                                    to="/Product-Master"
                                  >
                                    <i className="bi bi-gear dash-icon"></i>
                                    {activeTabs}
                                  </NavLink>
                                )}
                                {activeTabs == "Create PO/Indent" && (
                                  <NavLink
                                    className="pd-l apps-sidemenu"
                                    to="/Orders"
                                  >
                                    <i className="bi bi-bag-check dash-icon"></i>
                                    {activeTabs}
                                  </NavLink>
                                )}
                                {activeTabs == "Create Inwards" && (
                                  <NavLink
                                    className="pd-l apps-sidemenu"
                                    to="/Inwards"
                                  >
                                    <i className="bi bi-bag-plus dash-icon"></i>
                                    {activeTabs}
                                  </NavLink>
                                )}
                                {activeTabs == "View Stocks" && (
                                  <NavLink
                                    className="pd-l apps-sidemenu"
                                    to="/Stocks"
                                  >
                                    <i className="fa fa-file-text-o dash-icon"></i>
                                    {activeTabs}
                                  </NavLink>
                                )}
                              </div>
                            ))}
                        </NavDropdown>
                        <NavDropdown
                          id="collasible-nav-dropdown"
                          align="center"
                          title={
                            <div>
                              <i className="bi bi-geo-alt dash-icon"></i>
                              <span className="drophead">
                                Information Privilege
                              </span>
                              <i
                                className="fa fa-caret-down"
                                aria-hidden="true"
                              ></i>
                            </div>
                          }
                        >
                          <div className="showmore-div" aria-expanded="true">
                            <NavLink
                              className="pd-l apps-sidemenu"
                              to="/gis-map"
                            >
                              <i className="bi bi-columns-gap dash-icon"></i>Gis
                              Mapping
                            </NavLink>

                            <NavLink
                              className="pd-l apps-sidemenu"
                              to="/view-reports"
                            >
                              <i className="bi bi-person-plus dash-icon"></i>
                              View Report
                            </NavLink>
                          </div>
                        </NavDropdown>
                      </React.Fragment>
                    ) : (
                      <>
                        {typeofuser === "Medical Officer" ? (
                          <React.Fragment>
                            <NavLink
                              className="pd-l apps-sidemenu"
                              to="/mainDash"
                            >
                              <i className="bi bi-columns-gap dash-icon"></i>
                              Dashboard{" "}
                            </NavLink>
                            <NavDropdown
                              id="collasible-nav-dropdown"
                              align="center"
                              title={
                                <div>
                                  <i className="fa fa-file-text-o dash-icon"></i>
                                  <span className="drophead">
                                    Station 3 (Medical Consultation)
                                  </span>
                                  <i
                                    className="fa fa-caret-down"
                                    aria-hidden="true"
                                  ></i>
                                </div>
                              }
                            >
                              {roleActivity &&
                                roleActivity.map((activeTabs, i) => (
                                  <div
                                    className="showmore-div"
                                    aria-expanded="true"
                                    key={i}
                                  >
                                    {activeTabs == "List of visit" && (
                                      <NavLink
                                        className="pd-l apps-sidemenu"
                                        to="/dashboard"
                                      >
                                        <i className="bi bi-columns-gap dash-icon"></i>
                                        {activeTabs}
                                      </NavLink>
                                    )}
                                    {activeTabs == "Notifications" && (
                                      <NavLink
                                        className="pd-l apps-sidemenu"
                                        to="/notifications"
                                      >
                                        <i className="bi bi-bell dash-icon"></i>
                                        {activeTabs}
                                      </NavLink>
                                    )}
                                    {activeTabs == "Approvals" && (
                                      <NavLink
                                        className="pd-l apps-sidemenu"
                                        to="/approvals"
                                      >
                                        <i className="bi bi-person-rolodex dash-icon"></i>
                                        {activeTabs}
                                      </NavLink>
                                    )}
                                  </div>
                                ))}
                            </NavDropdown>
                            <NavDropdown
                              id="collasible-nav-dropdown"
                              align="center"
                              title={
                                <div>
                                  <i className="bi bi-geo-alt dash-icon"></i>
                                  <span className="drophead">
                                    Information Privilege
                                  </span>
                                  <i
                                    className="fa fa-caret-down"
                                    aria-hidden="true"
                                  ></i>
                                </div>
                              }
                            >
                              <div
                                className="showmore-div"
                                aria-expanded="true"
                              >
                                <NavLink
                                  className="pd-l apps-sidemenu"
                                  to="/gis-map"
                                >
                                  <i className="bi bi-columns-gap dash-icon"></i>
                                  Gis Mapping
                                </NavLink>

                                <NavLink
                                  className="pd-l apps-sidemenu"
                                  to="/view-reports"
                                >
                                  <i className="bi bi-person-plus dash-icon"></i>
                                  View Report
                                </NavLink>
                              </div>
                            </NavDropdown>
                          </React.Fragment>
                        ) : (
                          <>
                            {typeofuser === "Nurse" ? (
                              <React.Fragment>
                                <NavLink
                                  className="pd-l apps-sidemenu"
                                  to="/mainDash"
                                >
                                  <i className="bi bi-columns-gap dash-icon"></i>
                                  Dashboard{" "}
                                </NavLink>
                                <NavDropdown
                                  id="collasible-nav-dropdown"
                                  align="center"
                                  title={
                                    <div>
                                      <i className="fa fa-file-text-o dash-icon"></i>
                                      <span className="drophead">
                                        Station 2 (Nursing)
                                      </span>
                                      <i
                                        className="fa fa-caret-down"
                                        aria-hidden="true"
                                      ></i>
                                    </div>
                                  }
                                >
                                  {roleActivity &&
                                    roleActivity.map((activeTabs, i) => (
                                      <div
                                        className="showmore-div"
                                        aria-expanded="true"
                                        key={i}
                                      >
                                        {activeTabs == "List of visit" && (
                                          <NavLink
                                            className="pd-l apps-sidemenu"
                                            to="/dashboard"
                                          >
                                            <i className="bi bi-columns-gap dash-icon"></i>
                                            {activeTabs}
                                          </NavLink>
                                        )}
                                      </div>
                                    ))}
                                </NavDropdown>
                                <NavDropdown
                                  id="collasible-nav-dropdown"
                                  align="center"
                                  title={
                                    <div>
                                      <i className="bi bi-geo-alt dash-icon"></i>
                                      <span className="drophead">
                                        Information Privilege
                                      </span>
                                      <i
                                        className="fa fa-caret-down"
                                        aria-hidden="true"
                                      ></i>
                                    </div>
                                  }
                                >
                                  <div
                                    className="showmore-div"
                                    aria-expanded="true"
                                  >
                                    <NavLink
                                      className="pd-l apps-sidemenu"
                                      to="/gis-map"
                                    >
                                      <i className="bi bi-columns-gap dash-icon"></i>
                                      Gis Mapping
                                    </NavLink>

                                    <NavLink
                                      className="pd-l apps-sidemenu"
                                      to="/view-reports"
                                    >
                                      <i className="bi bi-person-plus dash-icon"></i>
                                      View Report
                                    </NavLink>
                                  </div>
                                </NavDropdown>
                              </React.Fragment>
                            ) : (
                              <>
                                {typeofuser === "Super Admin" && (
                                  <React.Fragment>
                                    <NavLink
                                      className="pd-l apps-sidemenu"
                                      to="/super-admin"
                                    >
                                      <i className="bi bi-person-circle dash-icon"></i>
                                      Super Admin
                                    </NavLink>
                                  </React.Fragment>
                                )}
                              </>
                            )}
                          </>
                        )}
                      </>
                    )}
                  </>
                )}
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default Sidemenu;
