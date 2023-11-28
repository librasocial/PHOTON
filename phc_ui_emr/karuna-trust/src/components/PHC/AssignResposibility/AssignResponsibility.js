import React, { useState, useEffect } from "react";
import {
  Col,
  Row,
  Form,
  Button,
  Breadcrumb,
  Card,
  Carousel,
} from "react-bootstrap";
import * as Tostify from "../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import ResponsibilityModal from "./ResponsibilityModal";
import { useDispatch, useSelector } from "react-redux";
import ItemsCarousel from "react-items-carousel";
import {
  loadAllStaffData,
  loadStaffData,
  loadSubCenterDetails,
  loadAsssignActivityList,
} from "../../../redux/phcAction";
import { useHistory } from "react-router-dom";

let suCenterId;
export default function AssignResponsibility(props) {
  let dispatch = useDispatch();
  const { staffData } = useSelector((state) => state.phcData);
  let phcuuid = sessionStorage.getItem("uuidofphc");

  const removevitalstorege = () => {
    window.history.back();
  };

  const [displayData, setDisplayData] = useState([]);

  let sunCenterDetails = props.sunCenterDetails;

  useEffect(() => {
    if (staffData) {
      let dataArray = [];
      staffData.forEach((element) => {
        if (element?.targetNode?.properties?.status == "ACTIVE") {
          dataArray.push({
            salutation: element?.targetNode?.properties?.salutation,
            name: element?.targetNode?.properties?.name,
            photo: element?.targetNode?.properties?.photo,
            code: element?.targetNode?.properties?.posted_by,
            staffRole: element?.targetNode?.properties?.type,
            status: true,
            uuid: element?.targetNode?.properties?.uuid,
          });
        } else if (element?.targetNode?.properties?.status == "INACTIVE") {
          dataArray.push({
            salutation: element?.targetNode?.properties?.salutation,
            name: element?.targetNode?.properties?.name,
            photo: element?.targetNode?.properties?.photo,
            code: element?.targetNode?.properties?.posted_by,
            staffRole: element?.targetNode?.properties?.type,
            status: false,
            uuid: element?.targetNode?.properties?.uuid,
          });
        } else {
          dataArray.push({
            salutation: element?.targetNode?.properties?.salutation,
            name: element?.targetNode?.properties?.name,
            photo: element?.targetNode?.properties?.photo,
            code: element?.targetNode?.properties?.posted_by,
            staffRole: element?.targetNode?.properties?.type,
            status: false,
            uuid: element?.targetNode?.properties?.uuid,
          });
        }
        setDisplayData(dataArray);
      });
    }
  }, [staffData]);

  useEffect(() => {
    document.title = "EMR Super Admin Assign Responsibilities";
    dispatch(loadAllStaffData(phcuuid));
    if (sunCenterDetails?.properties?.uuid) {
      suCenterId = sunCenterDetails?.properties?.uuid;
    }
  }, [phcuuid, sunCenterDetails]);

  const [staffActShow, setStaffActShow] = useState(false);

  const staffActClose = () => {
    setStaffActShow(false);
  };

  const assignActivities = (e) => {
    dispatch(loadStaffData(e));
    setStaffActShow(true);
    dispatch(loadAsssignActivityList(e));
  };

  function assignStaffActivity() {
    Tostify.notifySuccess(
      "Are you sure, You want to Assign this Responsibility ?"
    );
    //setStaffActShow(false);
  }

  let history = useHistory();
  const removeCurrentPage = () => {
    history.go(-2);
  };

  const [loading, setLoading] = useState(true);
  const [activeItemIndex, setActiveItemIndex] = useState(0);

  useEffect(() => {
    setTimeout(() => {
      setLoading(false);
    }, 2000);
  }, []);

  return (
    <React.Fragment>
      {staffActShow && (
        <ResponsibilityModal
          staffActShow={staffActShow}
          staffActClose={staffActClose}
          phcuuid={
            props.center_type == "Phc"
              ? phcuuid
              : props.center_type == "Sub-Center" && suCenterId
          }
          center_type={props.center_type}
        />
      )}
      <div className="div vital-div">
        <div className="regHeader">
          <h1 className="Encounter-Header">PHC Configuration</h1>
          <hr style={{ margin: "0px" }} />
        </div>
        <div className="config-div">
          <div className="super-tab">
            <div className="super-breadcrumb">
              <Breadcrumb>
                <Breadcrumb.Item
                  className="pur-order-breadcrumb"
                  onClick={removeCurrentPage}
                >
                  Dashboard
                </Breadcrumb.Item>
                <Breadcrumb.Item
                  className="pur-order-breadcrumb"
                  onClick={removevitalstorege}
                >
                  PHC Configuration
                </Breadcrumb.Item>
                <Breadcrumb.Item active className="phc-breadcrumb">
                  Assign Responsibilities
                </Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="form-col">
              <Form className="super-admin-form">
                <div className="super-complaint">
                  <h3 className="super-config-details">
                    Assign Responsibilities
                    {/* Assign Responsibilities in {sunCenterDetails?.properties?.name} */}
                  </h3>
                  <h3 className="super-owner-details">Process Owner's List</h3>
                  <div className="search-staff-add">
                    <ItemsCarousel
                      placeholderItem={
                        <div style={{ height: 200, background: "#EEE" }} />
                      }
                      // enablePlaceholder={true}
                      numberOfPlaceholderItems={4}
                      numberOfCards={4}
                      gutter={12}
                      slidesToScroll={4}
                      chevronWidth={60}
                      outsideChevron={true}
                      showSlither={false}
                      firstAndLastGutter={false}
                      activeItemIndex={activeItemIndex}
                      requestToChangeActive={setActiveItemIndex}
                      rightChevron={">>"}
                      leftChevron={"<<"}
                    >
                      {loading
                        ? []
                        : displayData.map((staff, i) => (
                            <div className="container_image" key={i}>
                              <div>
                                <Card className="staff-assign-card">
                                  <Row>
                                    <Col lg={3}>
                                      {staff.photo ? (
                                        <img
                                          className="staff-photo"
                                          src={staff.photo}
                                        />
                                      ) : (
                                        <img
                                          className="staff-photo"
                                          src="../img/super/user.png"
                                        />
                                      )}
                                    </Col>
                                    <Col
                                      lg={9}
                                      className="d-flex align-items-center"
                                    >
                                      <div className="staff-div">
                                        <p className="assign-staff">
                                          {staff.salutation} {staff.name}
                                        </p>
                                        <p className="total-staff">
                                          <span style={{ color: "#707070" }}>
                                            Staff No:
                                          </span>
                                          &nbsp; {staff.code}
                                        </p>
                                        <p className="total-staff">
                                          <span style={{ color: "#707070" }}>
                                            Staff Role:
                                          </span>
                                          &nbsp;
                                          {staff.staffRole.length > 15
                                            ? staff.staffRole.substring(0, 15) +
                                              "..."
                                            : staff.staffRole}
                                        </p>
                                      </div>
                                    </Col>
                                  </Row>
                                  <hr />
                                  <div className="staff-footer" align="center">
                                    <p className="total-staff">
                                      {" "}
                                      <b> Facility Head - Registration </b>
                                    </p>
                                  </div>
                                </Card>
                              </div>
                            </div>
                          ))}
                    </ItemsCarousel>
                  </div>
                  <div>
                    <p className="resp-subcenter">
                      <i className="bi bi-info-square-fill add-phc-icon"></i>
                      <b>Select activities</b> to <b>assign</b> responsibilities
                      to the Staff
                    </p>
                    <div className="form-group resp-staff-search">
                      <input
                        type="text"
                        className="form-control staff-search"
                        placeholder="Search Staff By Name / Contact Number"
                      />
                      <span className="fa fa-search form-control-pro-feedback d-flex"></span>
                    </div>
                  </div>
                  <div className="search-staff-add">
                    <Row>
                      {displayData &&
                        displayData.map((staff, i) => (
                          <Col lg={4} key={i}>
                            <Card className="staff-activity-card">
                              <Row>
                                <Col lg={3}>
                                  {staff.photo ? (
                                    <img
                                      className="staff-photo"
                                      src={staff.photo}
                                    />
                                  ) : (
                                    <img
                                      className="staff-photo"
                                      src="../img/super/user.png"
                                    />
                                  )}
                                </Col>
                                <Col
                                  lg={9}
                                  className="d-flex align-items-center"
                                >
                                  <div className="staff-div">
                                    <p className="assign-staff">
                                      {staff.salutation} {staff.name}
                                    </p>
                                    <p className="total-staff">
                                      <span style={{ color: "#707070" }}>
                                        Staff No:
                                      </span>{" "}
                                      {staff.code}
                                    </p>
                                    <p className="total-staff">
                                      <span style={{ color: "#707070" }}>
                                        Staff Role:
                                      </span>{" "}
                                      {staff.staffRole}
                                    </p>
                                  </div>
                                </Col>
                              </Row>
                              <hr />
                              <div className="staff-footer" align="center">
                                <Button
                                  variant="link"
                                  className="selectActLink"
                                  onClick={(e) => assignActivities(staff.uuid)}
                                >
                                  Select Activities
                                </Button>
                              </div>
                            </Card>
                          </Col>
                        ))}
                    </Row>
                  </div>
                </div>
              </Form>
            </div>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
